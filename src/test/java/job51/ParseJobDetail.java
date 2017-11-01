package job51;

import static org.apache.commons.lang.StringEscapeUtils.escapeSql;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import db.DBconnectionMgr;
import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import job51.check.JobSalaryCheck;
import job51.entity.Company;
import job51.entity.Job;
import job51.entity.JobBasicCondition;
import job51.entity.condition.RangeCondition;

public abstract class ParseJobDetail {
	public static void main(String[] args) {
		// job_basic_condition
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//51job.db");
		initWelfareMap();
		start();
	}

	private static void start() {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\51job.db");
		String[] cities = new String[] { "010000", "020000", "030000", "040000" };
		for (String city : cities) {
			File file = new File("F:/资源/爬虫/51job/jobs/" + city + "/detail");
			for (File f : file.listFiles()) {
				try {
					parse(f);
				} catch (Exception e) {
					e.printStackTrace();
					BasicFileUtil.writeFileString("joblist-parseErr1002.txt",
							f.getAbsolutePath() + "-" + e.getMessage() + "\r\n", null, true);
				}
			}
		}
	}

	static Map<String, String> welfareMap = new HashMap<String, String>();

	private static void initWelfareMap() {
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap("select ID,NAME from welfare");
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			Map row = (Map) selectAllSqlMap.get(i);
			welfareMap.put(row.get("NAME").toString(), row.get("ID").toString());
		}

	}

	private static void parse(File f) throws IOException, SQLException {
		String jobId = PatternUtil.getLastPattern(f.getAbsolutePath(), "\\d+");
		Job job = new Job(Integer.parseInt(jobId));
		Document parse = Jsoup.parse(f, "GBK");
		parseBasicCondition(jobId, parse);
		parseWelfare(jobId, parse);
		parseSalary(job, parse);
		parseCompany(job, parse);
		parseOthers(job, parse);
		updateJobSalaryAndOthers(job);
	}

	private static void updateJobSalaryAndOthers(Job job) throws SQLException {
		DbFieldSqlUtil util = new DbFieldSqlUtil("JOB");
		RangeCondition salaryRange = job.getSalaryRange();
		if (salaryRange.getBegin() != null)
			util.addUpdateField(new DbField("SALARY1", salaryRange.getBegin() + ""));
		if (salaryRange.getEnd() != null)
			util.addUpdateField(new DbField("SALARY2", salaryRange.getEnd() + ""));
		util.addUpdateField(new DbField("SALARY_RANGE", salaryRange.toString()));
		util.addUpdateField(new DbField("WORK_CITY", job.getWorkCity()));
		util.addUpdateField(new DbField("WORK_ZONE", job.getWorkZone()));
		util.addUpdateField(new DbField("WORK_LOCATION", job.getWorkLocation()));
		util.addWhereField(new DbField("JOB_ID", "" + job.getJobId()));
		DbExecMgr.execOnlyOneUpdate(util.getUpdateSql());
		System.out.println("更新工资成功");
	}

	private static void parseOthers(Job job, Document parse) {
		// 工作地点信息
		String text = parse
				.select("body > div.tCompanyPage > div.tCompany_center.clearfix > div.tHeader.tHjob > div > div.cn > span")
				.text().trim();
		String workCity = PatternUtil.getFirstPatternGroup(text, "[\u4E00-\u9FA5]+");
		String workZone = PatternUtil.getLastPatternGroup(text, "[\u4E00-\u9FA5]+");
		job.setWorkCity(workCity);
		job.setWorkZone(workZone);
		job.setWorkLocation(parse
				.select("body > div.tCompanyPage > div.tCompany_center.clearfix > div.tCompany_main > div:nth-child(5) > div > p")
				.text());

	}

	private static void parseCompany(Job job, Document parse) throws SQLException {
		Element first = parse
				.select("div.tCompanyPage > div.tCompany_center.clearfix > div.tHeader.tHjob > div > div.cn > p.cname a")
				.first();
		Company company = new Company();
		String url = first.attr("href");
		String comId = PatternUtil.getLastPattern(url, "\\d+");
		String name = first.text().trim();
		company.setUrl(url);
		company.setComId(comId);
		company.setComName(name);
		String text = parse
				.select("body > div.tCompanyPage > div.tCompany_center.clearfix > div.tHeader.tHjob > div > div.cn > p.msg.ltype")
				.text();
		String[] split = text.split("\\|");
		Set<String> yewu = new HashSet<String>(8);
		String type = null;
		String size = null;
		if (split.length >= 1) {
			type = split[0].trim();
		}
		if (split.length >= 2) {
			size = split[1].trim();
		}
		if (split.length >= 3) {
			String[] split2 = split[2].trim().split("[/, ]");
			for (int i = 0; i < split2.length; i++) {
				String replace = split2[i].replaceAll("\\s", "").replace("  ", "");
				if (replace.length() > 0) {
					yewu.add(replace);
				}
			}
		}
		company.setCompanysize(size);
		company.setCompanyType(type);
		company.setYewu(yewu);
		job.setCompany(company);

		DbExecMgr.execOnlyOneUpdate("UPDATE JOB SET COM_ID='" + comId + "' WHERE job_id=" + job.getJobId());
		if (!DbExecMgr.isExistData("COMPANY", "COM_ID", comId))
			DbExecMgr.execOnlyOneUpdate(String.format(
					"INSERT INTO COMPANY(COM_ID,URL,COM_NAME,COM_TYPE,COM_SIZE)  VALUES('%s','%s','%s','%s','%s')",
					comId, url, escapeSql(name), escapeSql(type), escapeSql(size)));

		for (String s : yewu) {
			if (!DbExecMgr.isExistData(
					"SELECT 1 FROM COM_YEWU WHERE COM_ID = " + comId + " AND YEWU = '" + escapeSql(s) + "'")) {
				DbExecMgr.execOnlyOneUpdate(
						"INSERT INTO COM_YEWU(COM_ID,YEWU)  VALUES(" + comId + ",'" + escapeSql(s) + "')");
			}
		}
	}

	private static void parseSalary(Job job, Document parse) throws IOException {
		RangeCondition parseSalary = JobSalaryCheck.parseSalary(parse);
		System.out.println(parseSalary);
		job.setSalaryRange(parseSalary);
	}

	private static void parseWelfare(String jobId, Document parse) {
		if (DbExecMgr.isExistData("job_welfare", "job_id", jobId)) {
			return;
		}
		Elements select = parse.select(".jtag .t2 span");
		for (Element element : select) {
			try {
				String name = element.text();
				if (!welfareMap.containsKey(name)) {
					DbExecMgr.execOnlyOneUpdate("INSERT INTO WELFARE(NAME) VALUES('" + name + "')");
					String id = DbExecMgr.getSelectSqlMap("SELECT ID FROM WELFARE WHERE NAME='" + name + "'").get("ID")
							.toString();
					welfareMap.put(name, id);
					System.err.println(name + " - " + id);
				}
				String id = welfareMap.get(name);
				DbExecMgr.execOnlyOneUpdate(
						"INSERT INTO JOB_WELFARE(JOB_ID,WELFARE_ID) VALUES(" + jobId + "," + id + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void parseBasicCondition(String jobId, Document parse) throws SQLException {
		Elements select = parse.select(".jtag.inbox span em");
		JobBasicCondition jbc = new JobBasicCondition();
		jbc.setJobId(jobId);
		for (Element em : select) {
			int index = BasicNumberUtil.getNumber(PatternUtil.getLastPattern(em.attr("class"), "\\d+"));
			String text = em.parent().text();
			String headDesc = "";
			switch (index) {
			case 1:
				headDesc = "工作经验:";
				jbc.setYear(text);
				break;
			case 2:
				headDesc = "教育水平:";
				jbc.setEducation(text);
				break;
			case 3:
				headDesc = "招聘人数:";
				jbc.setNeed(text);
				break;
			case 4:
				break;
			case 5:
				headDesc = "口语要求:";
				jbc.setOral(text);
				break;
			case 6:
				headDesc = "未知条件:";
				jbc.setUnKownTag6(text);
				break;
			case 7:
				headDesc = "专业要求:";
				jbc.setSpecialLine(text);
				break;
			default:
				errLog(jobId + "/ TagErr:" + em);
				break;
			}
			// if (StringUtils.isNotBlank(headDesc))
			// System.out.println(headDesc + "/" + text);
		}
		insertJbc(jbc);
	}

	private static void insertJbc(JobBasicCondition jbc) throws SQLException {
		if (DbExecMgr.isExistData("job_basic_condition", "job_id", jbc.getJobId())) {
			return;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("job_basic_condition");
		util.addInsertField(new DbField("job_id", jbc.getJobId()));
		util.addInsertField(new DbField("year_exp", jbc.getYear()));
		util.addInsertField(new DbField("employees", jbc.getNeed()));
		util.addInsertField(new DbField("edu", jbc.getEducation()));
		util.addInsertField(new DbField("special", jbc.getSpecialLine()));
		util.addInsertField(new DbField("oral", jbc.getOral()));
		util.addInsertField(new DbField("six", jbc.getUnKownTag6()));
		String insertSql = util.getInsertSql();
		System.out.println(insertSql);
		DbExecMgr.execOnlyOneUpdate(insertSql);
	}

	private static void errLog(String string) {
		System.err.println(string);
		BasicFileUtil.writeFileString("joblist-parseErr.txt", string + "\r\n", null, true);

	}
}

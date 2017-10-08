package job51.check;

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

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import job51.entity.Job;
import job51.entity.JobBasicCondition;
import job51.entity.condition.RangeCondition;

public abstract class ParseJobDetail2 {
	public static void main(String[] args) {
		// job_basic_condition
		start();
	}

	private static void start() {
		File file = new File("F:/资源/爬虫/51job/jobs/180200/detail");
		for (File f : file.listFiles()) {
			try {
				parse(f);
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("joblist-parseErr.txt",
						f.getAbsolutePath() + "-" + e.getMessage() + "\r\n", null, true);
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
		// parseBasicCondition(jobId, parse);
		// parseWelfare(jobId, parse);
		// parseSalary(job, parse);
		parseCompany(job, parse);
		parseOthers(job, parse);
	}

	private static void parseOthers(Job job, Document parse) {

	}

	private static void parseCompany(Job job, Document parse) {
		Element first = parse
				.select("div.tCompanyPage > div.tCompany_center.clearfix > div.tHeader.tHjob > div > div.cn > p.cname a")
				.first();
		String url = first.attr("href");
		String name = first.text().trim();

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
		System.out.println(yewu);
	}

	private static void parseSalary(Job job, Document parse) throws IOException {
		RangeCondition parseSalary = JobSalaryCheck.parseSalary(parse);
		System.out.println(parseSalary);
		job.setSalaryRange(parseSalary);
	}

	private static void parseWelfare(String jobId, Document parse) throws SQLException {
		Elements select = parse.select(".jtag .t2 span");
		for (Element element : select) {
			String name = element.text();
			if (!welfareMap.containsKey(name)) {
				DbExecMgr.execOnlyOneUpdate("INSERT INTO WELFARE(NAME) VALUES('" + name + "')");
				String id = DbExecMgr.getSelectSqlMap("SELECT ID FROM WELFARE WHERE NAME='" + name + "'").get("ID")
						.toString();
				welfareMap.put(name, id);
				System.err.println(name + " - " + id);
			}
			String id = welfareMap.get(name);
			DbExecMgr.execOnlyOneUpdate("INSERT INTO JOB_WELFARE(JOB_ID,WELFARE_ID) VALUES(" + jobId + "," + id + ")");
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
		insert(jbc);
	}

	private static void insert(JobBasicCondition jbc) throws SQLException {
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

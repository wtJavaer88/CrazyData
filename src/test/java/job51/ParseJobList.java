package job51;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

import common.spider.node.MyElement;
import db.DBconnectionMgr;
import db.DbExecMgr;

public class ParseJobList {
	public static void main(String[] args) throws IOException {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\51job.db");
		String[] cities = new String[] { "010000", "020000", "030000", "040000" };
		for (String city : cities) {
			String dir = "F:/资源/爬虫/51job/jobs/" + city + "/list/";
			for (File f : new File(dir).listFiles())
				parse(f.getAbsolutePath());
		}
	}

	private static void parse(String file) throws IOException {
		Document parse = Jsoup.parse(new File(file), "GBK");
		Elements select = parse.select("#resultList div.el:gt(1)");
		for (Element element : select) {
			// System.out.println("########");
			String jobName = element.select(".t1").text().trim();
			// System.out.println(jobName);
			MyElement myElement = new MyElement(element.select("a").first());
			String jobUrl = myElement.pattern4Attr("href", "(.*\\.html)");
			if (StringUtils.isBlank(jobUrl)) {
				jobUrl = myElement.attr("href");
			}
			// System.out.println(jobUrl);
			String jobId = PatternUtil.getLastPattern(jobUrl, "\\d+");
			// 公司
			// Element elementT2 = element.select(".t2 a").first();
			// System.out.println(elementT2.attr("title") + " " +
			// elementT2.attr("href"));
			String trim = element.select(".t3").text().trim();
			String workCity = PatternUtil.getFirstPatternGroup(trim, "[\u4E00-\u9FA5]+");
			String workZone = PatternUtil.getLastPatternGroup(trim, "[\u4E00-\u9FA5]+");
			// System.out.println(workCity + "-" + workZone);
			//
			// System.out.println(element.select(".t4").text().trim());
			String publish_date = element.select(".t5").text().trim();
			System.out.println(publish_date);
			// System.out.println("########\n");

			try {
				String currentDateTimeString = BasicDateUtil.getCurrentDateTimeString();
				String sql = "INSERT INTO JOB(JOB_ID,JOB_URL,JOB_NAME,PUBLISH_DATE,CREATE_DATE,UPDATE_DATE) VALUES("
						+ jobId + ",'" + jobUrl + "','" + jobName + "','" + publish_date + "','" + currentDateTimeString
						+ "','" + currentDateTimeString + "')";
				if (!DbExecMgr.isExistData("JOB", "JOB_ID", jobId)) {
					System.out.println(jobName + ":/" + file + "/ " + jobUrl);
					DbExecMgr.execOnlyOneUpdate(sql);
				} else {
					DbExecMgr.execOnlyOneUpdate(
							"UPDATE JOB SET publish_date='" + publish_date + "' WHERE JOB_ID=" + jobId);
				}
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("joblist-dberr.txt", jobId + "-" + e.getMessage() + "\r\n", null, true);
			}
			// BasicFileUtil.writeFileString("joblist-wuhan.txt", pattern4Attr +
			// "\r\n", null, true);
		}
	}
}

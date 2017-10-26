package job51.sej;

import java.sql.SQLException;

import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import db.DBconnectionMgr;
import db.DbExecMgr;

public class KeywordToDb {
	public static void main(String[] args) {
		String trim = "";
		String job_id = "";
		String[] features = new String[] {};
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//51job.db");
		for (String tokens : FileOp.readFrom("F:/资源/爬虫/51job/keywords.txt")) {
			job_id = PatternUtil.getFirstPattern(tokens, "\\d+");
			features = PatternUtil.getFirstPatternGroup(tokens, "\\[(.*?)\\]").split(",");
			boolean flag = false;
			for (String string : features) {
				trim = removeTailchar(string.trim(), '.').trim();
				if (flag || !DbExecMgr.isExistData(
						"SELECT 1 FROM JOB_FEATURES WHERE job_id=" + job_id + " AND FEATURE='" + trim + "'")) {
					flag = true;
					System.out.println(trim);
					try {
						DbExecMgr.execOnlyOneUpdate(
								"INSERT INTO JOB_FEATURES(JOB_ID,FEATURE) values(" + job_id + ",'" + trim + "')");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static String removeTailchar(String trim, char c) {
		while (trim.length() > 1 && trim.endsWith(c + "")) {
			trim = trim.substring(0, trim.length() - 1);
		}
		return trim;
	}
}

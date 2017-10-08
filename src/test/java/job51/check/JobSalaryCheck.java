package job51.check;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import job51.entity.condition.RangeCondition;
import job51.entity.type.RangeType;

public class JobSalaryCheck {
	/**
	 * 检查工资类型,区间
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\51job.db");
		// for (int i = 1; i <= 218; i++)
		// Document parse = Jsoup.parse(new
		// File("F:/资源/爬虫/51job/jobs/180200/list/" + i + ".html"), "GBK");
		// parse(parse);
		System.out.println(getMonSalary(10 + ""));
	}

	public static RangeCondition parseSalary(Document parse) throws IOException {
		String salary = parse
				.select("body > div.tCompanyPage > div.tCompany_center.clearfix > div.tHeader.tHjob > div > div.cn > strong")
				.text();
		return checkSalary(salary);
	}

	private static void checkErr(String err) {
		System.err.println("异常数据:" + err);
		BasicFileUtil.writeFileString("joblist-checkerr.txt", err + "\r\n", null, true);
		// throw new RuntimeException("异常数据:" + err);
	}

	private static RangeCondition checkSalary(String salary) {
		String s1 = PatternUtil.getFirstPattern(salary, "[\\d\\.]+");
		String s2 = PatternUtil.getLastPattern(salary, "[\\d\\.]+");
		Integer salary1 = null;
		Integer salary2 = null;
		if (StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) {
			if (salary.endsWith("月")) {
				System.out.println(s1 + "~" + s2);
				int series = 1;
				if (salary.contains("万")) {
					series = 10;
				}
				salary1 = (int) (BasicNumberUtil.getDouble(s1) * 1000 * series);
				salary2 = (int) (BasicNumberUtil.getDouble(s2) * 1000 * series);
			} else if (salary.endsWith("年")) {
				salary1 = getMonSalary(s1);
				salary2 = getMonSalary(s2);
				System.out.println(salary + "换算成月薪:" + salary1 + "~" + salary2);
			}
			// else {
			// checkErr("SalaryErr:" + salary);
			// }
		} else {
			// if (StringUtils.isNotBlank(s1) || StringUtils.isNotBlank(s2)) {
			// checkErr("SalaryErr:单边薪水" + (StringUtils.isNotBlank(s1) ? s1 :
			// s2));
			// }
			checkErr("SalaryErr:面议");
		}
		return new RangeCondition(salary1, salary2, RangeType.BEGIN_END);
	}

	private static int getMonSalary(String s) {
		int result = 0;
		int number = (int) (BasicNumberUtil.getDouble(s) * 10000);
		int rest = number - (number / 12 / 1000 * 1000) * 12;
		if (rest < 250 * 12) {
			result = (number - number % 12000) / 12;
		}
		if (rest >= 250 * 12 && rest < 750 * 12) {
			result = (number - number % 12000) / 12 + 500;
		}
		if (rest >= 750 * 12) {
			result = (number - number % 12000) / 12 + 1000;
		}
		return result;
	}

}

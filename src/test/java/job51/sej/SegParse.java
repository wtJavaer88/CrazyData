package job51.sej;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

public class SegParse {

	public static void main(String[] args) {
		System.out.println("A. 2323".matches("[a-zA-Z]{1}\\. .*?"));
		String[] citylist = { "180200", "010000", "020000", "030000", "040000" };
		for (String city : citylist) {
			String folder = "F:/资源/爬虫/51job/jobs/" + city + "/detail";
			for (File f : new File(folder).listFiles()) {
				try {
					Document parse = Jsoup.parse(f, "UTF-8");
					String text = parse.select(".bmsg.job_msg.inbox").text();
					List<String> patternStrings = PatternUtil.getPatternStrings(text, "[a-zA-Z]+[\\d-_\\+\\.\\w/ ]*");
					if (patternStrings.size() > 0
							&& StringUtils.join(patternStrings, "").length() / patternStrings.size() < 12) {
						// 最多保存10个到数据库
						List<String> list = new ArrayList<String>(10);
						for (String string : patternStrings) {
							// 如果包含/且不是B/S或者C/S 需要分割
							// 如果包含+且不是C++或VC++, 需要分割
							if (string.contains("/")) {
								if (!string.equalsIgnoreCase("B/S") && !string.equalsIgnoreCase("C/S")) {
									addElements(list, string.split("/"));
								}
							} else if (string.contains("+")) {
								if (!string.toUpperCase().trim().endsWith("C++")) {
									addElements(list, string.split("\\+"));
								}
							} else
								addElements(list, string);

							if (list.size() >= 10) {
								break;
							}
						}
						BasicFileUtil.writeFileString("F:/资源/爬虫/51job/keywords.txt",
								PatternUtil.getFirstPattern(f.getName(), "\\d+") + ":" + list + "\r\n", null, true);
					}
				} catch (IOException e) {
					e.printStackTrace();
					BasicFileUtil.writeFileString("F:/资源/爬虫/51job/keywords-err.txt",
							PatternUtil.getFirstPattern(city + "-" + f.getName(), "\\d+") + "-" + e.toString() + "\r\n",
							null, true);
				}
			}
		}
	}

	private static void addElements(List<String> list, String... arr) {
		for (String key : arr) {
			boolean sameFlag = false;
			for (String s : list) {
				sameFlag = s.equalsIgnoreCase(key.trim());
				if (sameFlag) {
					break;
				}
			}
			if (!sameFlag && StringUtils.isNotBlank(key) && !key.matches("[a-zA-Z]{1}\\. .*?")) {
				list.add(removeTailchar(key.trim(), '.').trim());
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

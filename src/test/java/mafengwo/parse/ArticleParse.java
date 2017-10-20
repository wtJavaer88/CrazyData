package mafengwo.parse;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

public class ArticleParse {
	public static String dir = "D:/project/CrazyData/demo/马蜂窝/";

	public static void main(String[] args) throws IOException {
		Document parse = Jsoup.parse(new File(dir + "article-7260158.html"), "UTF-8");
		String title = parse.select("#_j_cover_box > div._j_titlebg > div.view_info > div > h1.headtext").text();
		System.out.println(title);
		String travel_dir = parse.select("div.travel_directory._j_exscheduleinfo > div").html();
		System.out.println(travel_dir);
		String html = parse.select(".vc_article").html();

		System.out.println("文章字符数:" + html.length());
		// html = replaceHtml(html);
		// System.out.println("文章字符数:" + html.length());
		// BasicFileUtil.writeFileString(dir + "artile-clear.html", html, null,
		// false);
		Element contentEmt = parse.select(".vc_article").first();
		for (Element e : contentEmt.getAllElements()) {
			e.removeAttr("data-rt-src");
			e.removeAttr("data-pid");
			e.removeAttr("data-seq");
			e.removeAttr("alt");
			e.removeAttr("id");
			if ("img".equals(e.tagName())) {
				e.removeAttr("class");
				if (e.hasAttr("data-src")) {
					e.removeAttr("src");
					e.attr("src", PatternUtil.getFirstPatternGroup(e.attr("data-src"), "(.*?)\\?.*?"));
					e.removeAttr("data-src");
				}

				System.out.println(e);
				Element grandParent = e.parent().parent();
				if (grandParent.hasClass("add_pic")) {
					grandParent.select("a").remove();
					grandParent.appendChild(e);
					System.out.println(grandParent);
				}
			}
		}
		String html2 = contentEmt.html().replaceAll("(id|data-seq|data-pid|data-seq)=\"(.*?)\"", "")
				.replaceAll("\\s+", " ").replace("&nbsp;", " ");
		System.out.println("文章字符数:" + html2.length());
		BasicFileUtil.writeFileString(dir + "artile-clear.html", html2, null, false);
	}

	/**
	 * 处理html源码, 去掉多余属性
	 * 
	 * @param html
	 * @return
	 */
	private static String replaceHtml(String html) {
		return html.replaceAll("(id|data-seq|data-pid)=\"(.*?)\"", "").replaceAll("\\s+", " ").replace("&nbsp;", " ")
				.replaceAll("<img .*?style=\"(.*?)\" .*?data-src=\"(.*?)\\?.*?\".*?>", "<img style=\"$1\" src=\"$2\">");
	}
}

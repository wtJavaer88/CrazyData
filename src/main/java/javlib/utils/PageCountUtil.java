package javlib.utils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicNumberUtil;

import common.spider.node.MyElement;

public class PageCountUtil {

	public static int getPageCount(Document doc) {
		Elements select = doc.select(".page_selector .last");
		if (select.size() == 1) {
			return BasicNumberUtil.getNumber(new MyElement(select.get(0)).pattern4Attr("href", "page=(\\d+)"));
		} else if (doc.text().contains("空的列表")) {
			return 0;
		} else {
			return 1;
		}
	}
}

package edu.xhy;

import java.sql.SQLException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.SoupUtil;

import db.DBconnectionMgr;
import db.DbExecMgr;

public class XhyTest {
	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\mydict.db");
		DBconnectionMgr.getConnection();
		for (char i = 65; i < 91; i++) {
			if (i == 'I' || i == 'U' || i == 'V') {
				continue;
			}
			String url = "http://tools.2345.com/xiehouyu/" + i + "/" + i + ".htm"; // .page
																					// a
			Document doc = SoupUtil.getDoc(url);
			parseDoc(doc);
			Elements select = doc.select("div.page a");

			int pages = 1;
			if (select.size() == 0) {
				continue;
			}
			if (select.last().text().equals("下一页")) {
				if (doc.select("div.page em").size() == 0) {
					pages = select.size() - 2;
				} else
					pages = BasicNumberUtil.getNumber(select.last().previousElementSibling().text());
			} else {
				pages = select.size();
			}
			System.out.println(i + " " + pages);
			for (int j = 2; j <= pages; j++) {
				parseDoc(SoupUtil.getDoc("http://tools.2345.com/xiehouyu/" + i + "/" + i + "_" + j + ".htm"));
			}

		}
	}

	private static int count = 0;

	public static void parseDoc(Document doc) {
		Elements select = doc.select(".msg_list_detail li p");
		for (Element element : select) {
			String text = element.text();
			String name = PatternUtil.getFirstPatternGroup(text, "(.*?)——");
			String tail = PatternUtil.getFirstPatternGroup(text, "——(.*+)");
			System.out.println(name);
			System.out.println(tail);
			try {
				DbExecMgr.execOnlyOneUpdate("INSERT INTO xhy_dict(NAME,TAIL) VALUES('" + name + "','" + tail + "')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

package job51.check;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db.DBconnectionMgr;
import db.DbExecMgr;

/**
 * 初始化所有福利
 * 
 * @author wnc
 *
 */
public class WelfareCheck {
	public static void main(String[] args) throws IOException, SQLException {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\51job.db");
		Document parse = Jsoup.parse(new File("job_search.html"), "UTF-8");
		Elements select = parse.select("#filter_p_keyword a");
		for (Element element : select) {
			DbExecMgr.execOnlyOneUpdate("INSERT INTO WELFARE(NAME) VALUES('" + element.text() + "')");
		}
	}
}

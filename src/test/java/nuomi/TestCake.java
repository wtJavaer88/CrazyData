package nuomi;

import org.jsoup.nodes.Document;

import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.SoupUtil;

/**
 * 每个商圈都有一个地址http://www.dianping.com/search/category/9/10/g110r2065o10p2
 * </p>
 * 9代表城市, 10代表美食 g110r2065o10代表行政区或商圈, p2代表第二页
 * 
 * @author wnc
 *
 */
public class TestCake {
	public static void main(String[] args) {
		Document doc = SoupUtil.getDoc("http://www.dianping.com/search/category/9/10/g110o10");
		System.out.println(doc);
		BasicFileUtil.writeFileString("hg.html", doc.toString(), null, false);
	}
}

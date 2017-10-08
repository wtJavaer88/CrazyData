package job51;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import common.spider.HttpClientUtil;
import common.spider.node.MyElement;

/**
 * 北上广深
 * 
 * @author wnc
 *
 */
public class TestBeijShangh {
	static final String pageFormat = "http://search.51job.com/list/{CITY},000000,0100,00,9,99,%2B,2,PPP.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=1&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
	static Map<String, Integer> pageMap = new HashMap<String, Integer>();
	static {
		pageMap.put("010000", 535);
		pageMap.put("020000", 720);
		pageMap.put("030000", 361);
		pageMap.put("040000", 473);
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 4; i++) {
			String city = "0" + i + "0000";
			BasicFileUtil.makeDirectory("F:/资源/爬虫/51job/jobs/" + city + "/list/");
			BasicFileUtil.makeDirectory("F:/资源/爬虫/51job/jobs/" + city + "/detail/");

			downloadJobList(city);
		}
		downloadJobDetailAll();
	}

	private static void downloadJobDetailAll() {
		ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {
				List<String> readFrom = new ArrayList<String>();
				for (int i = 1; i <= 4; i++) {
					String city1 = "0" + i + "0000";
					readFrom = FileOp.readFrom("joblist-" + city1 + ".txt");
					for (String string : readFrom) {
						System.out.println(string);
						if (!BasicFileUtil.isExistFile("F:/资源/爬虫/51job/jobs/" + city1 + "/detail/"
								+ PatternUtil.getLastPattern(string, "\\d+") + ".html"))
							SpiderHttpClient.getInstance().getNetPageThreadPool()
									.execute(new WuHanJobTask(city1, string, true));
					}
				}
			}
		});
	}

	static class WuHanJobTask extends AbstractPageTask {
		String city;

		public WuHanJobTask(String city, String url, boolean b) {
			super(url, b);
			this.city = city;
			this.pageEncoding = "GBK";
		}

		@Override
		protected void retry() {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new WuHanJobTask(city, url, true));
		}

		@Override
		protected void handle(Page page) throws Exception {
			BasicFileUtil.writeFileString(
					"F:/资源/爬虫/51job/jobs/" + city + "/detail/" + PatternUtil.getLastPattern(url, "\\d+") + ".html",
					page.getHtml(), "GBK", false);
			SpiderHttpClient.parseCount.getAndIncrement();
		}

	}

	private static void downloadJobList(String city) {
		for (int i = 1; i <= pageMap.get(city); i++) {
			if (BasicFileUtil.isExistFile("F:/资源/爬虫/51job/jobs/" + city + "/list/" + i + ".html"))
				continue;

			try {
				String webPage = HttpClientUtil
						.getWebPage(new HttpGet(pageFormat.replace("{CITY}", city).replace("PPP", i + "")), "GBK");
				BasicFileUtil.writeFileString("F:/资源/爬虫/51job/jobs/" + city + "/list/" + i + ".html", webPage, "GBK",
						false);
				Document parse = Jsoup.parse(webPage);
				Elements select = parse.select("#resultList div.el:gt(1)");
				for (Element element : select) {
					BasicFileUtil.writeFileString("joblist-" + city + ".txt",
							new MyElement(element.select("a").first()).pattern4Attr("href", "(.*\\.html)") + "\r\n",
							null, true);
				}
				Thread.sleep(800);
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("F:/资源/爬虫/51job/jobs/" + city + "/err.txt",
						i + "Page " + e.toString() + "\r\n", null, true);
			}

		}
	}
}

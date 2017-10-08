package job51;

import java.util.List;

import org.apache.http.client.methods.HttpGet;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import common.spider.HttpClientUtil;

public class TestOnlyWuhan {
	static final String pageFormat = "http://search.51job.com/list/180200,000000,0100,00,9,99,%2B,2,PPP.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=1&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

	public static void main(String[] args) {
		// downloadJobList();
		downloadJobDetail();
	}

	private static void downloadJobDetail() {
		ProxyHttpClient.initProxy();

		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {
				List<String> readFrom = FileOp.readFrom("joblist-wuhan.txt");
				for (String string : readFrom) {
					System.out.println(string);
					if (!BasicFileUtil.isExistFile("F:/资源/爬虫/51job/jobs/180200/detail/"
							+ PatternUtil.getLastPattern(string, "\\d+") + ".html"))
						SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new WuHanJobTask(string, true));
				}
			}
		});
	}

	static class WuHanJobTask extends AbstractPageTask {

		public WuHanJobTask(String url, boolean b) {
			super(url, b);
			this.pageEncoding = "GBK";
		}

		@Override
		protected void retry() {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new WuHanJobTask(url, true));
		}

		@Override
		protected void handle(Page page) throws Exception {
			BasicFileUtil.writeFileString(
					"F:/资源/爬虫/51job/jobs/180200/detail/" + PatternUtil.getLastPattern(url, "\\d+") + ".html",
					page.getHtml(), "GBK", false);
			SpiderHttpClient.parseCount.getAndIncrement();
		}

	}

	private static void downloadJobList() {
		for (int i = 1; i <= 218; i++) {
			try {
				String webPage = HttpClientUtil.getWebPage(new HttpGet(pageFormat.replace("PPP", i + "")), "GBK");
				BasicFileUtil.writeFileString("F:/资源/爬虫/51job/jobs/180200/" + i + ".html", webPage, "GBK", false);
				Thread.sleep(800);
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("F:/资源/爬虫/51job/jobs/180200/err.txt", i + "Page " + e.toString() + "\r\n",
						null, true);
			}

		}
	}
}

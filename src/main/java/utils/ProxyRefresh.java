package utils;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.ProxyPool;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;

public class ProxyRefresh {
	public static void main(String[] args) {
		ProxyPool.parseWithFile("daili.txt");
		ProxyHttpClient.getInstance().startCrawl();
		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {
				for (int i = 0; i < 1000000; i++) {
					SpiderHttpClient.getInstance().getNetPageThreadPool()
							.execute(new AbstractPageTask("https://www.baidu.com", true) {

								@Override
								protected void retry() {
									// TODO Auto-generated method stub

								}

								@Override
								protected void handle(Page page) throws Exception {

								}
							});
				}
			}
		});
	}
}

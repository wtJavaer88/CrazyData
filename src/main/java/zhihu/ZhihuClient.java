package zhihu;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.SpiderHttpClient;

import db.DBconnectionMgr;
import zhihu.action.PostRetryAction;
import zhihu.util.ZhihuAuthUtil;

public class ZhihuClient {
	public static void main(String[] args) throws InterruptedException {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//zhihu.db");
		ZhihuAuthUtil.getInstance().initAuthorization();
		// ProxyHttpClient.getInstance().startCrawl();
		// ProxyPool.parseWithFile("daili.txt");
		// Thread.sleep(15 * 60 * 1000);
		ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new PostRetryAction());
	}

}

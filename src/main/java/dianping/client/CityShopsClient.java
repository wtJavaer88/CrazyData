package dianping.client;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.SpiderHttpClient;

import db.DBconnectionMgr;
import dianping.action.ShopPageAction;

public class CityShopsClient {

	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\dianping.db");
		ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new ShopPageAction());
	}

}

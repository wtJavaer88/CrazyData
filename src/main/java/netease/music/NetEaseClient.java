package netease.music;

import com.crawl.spider.SpiderHttpClient;

import netease.music.action.HotSongsAction;

public class NetEaseClient {
	public static void main(String[] args) {
		// DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\netease.db");
		// ProxyPool.parseWithFile("daili.txt");
		// ProxyHttpClient.getInstance().startCrawl();
		// ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new HotSongsAction());
	}
}

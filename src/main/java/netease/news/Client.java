package netease.news;

import java.util.HashSet;
import java.util.Set;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.ProxyPool;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.string.PatternUtil;

import netease.news.comments.WyNewsApi;
import utils.NewsType;

public class Client {
	public static Set<Integer> seekedCommentIds = new HashSet<Integer>();
	public static Set<String> seekedUserIds= new HashSet<String>();
	
	public static void main(String[] args) {
		ProxyPool.parseWithFile("daili.txt");
		ProxyHttpClient.getInstance().startCrawl();
		SpiderHttpClient.getInstance().startCrawl(new WyNewsApi(NewsType.NBA));
	}
}

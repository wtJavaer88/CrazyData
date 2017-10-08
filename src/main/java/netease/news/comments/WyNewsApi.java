package netease.news.comments;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;

import utils.NewsType;

public class WyNewsApi implements MySpiderAction {
	NewsType newsType;

	public WyNewsApi(NewsType newsType) {
		this.newsType = newsType;
	}

	public void execute() {
		// 最大10页
		for (int page = 1; page <= 10; page++) {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new WyNewsTask(newsType, page));
		}
	}
}

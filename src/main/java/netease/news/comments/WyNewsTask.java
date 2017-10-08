package netease.news.comments;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import netease.news.entity.WyKeyWords;
import netease.news.entity.WyNews;
import utils.NewsType;
import utils.ReflectUtil;

public class WyNewsTask extends AbstractPageTask {
	NewsType newsType;

	public WyNewsTask(NewsType newsType, int page) {
		this.newsType = newsType;
		this.url = newsType.getNewsPageUrl(page);
		this.proxyFlag = true;
		this.pageEncoding = "GBK";
	}

	public WyNewsTask(String url) {
		this.url = url;
		this.proxyFlag = true;
		this.pageEncoding = "GBK";
	}

	@Override
	protected void retry() {
		SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new WyNewsTask(url));
	}

	int count = 0;

	@Override
	protected void handle(Page page) throws Exception {
		String webPage = page.getHtml();
		int a = webPage.indexOf('[');
		int b = webPage.lastIndexOf(']');
		webPage = webPage.substring(a, b + 1);
		JSONArray parseArray = JSONObject.parseArray(webPage);
		for (int i = 0; i < parseArray.size(); i++) {
			JSONObject jsonObject = parseArray.getJSONObject(i);
			try {
				WyNews wyNews = getWyNews(jsonObject);
				BasicFileUtil.writeFileString("wynews.txt", jsonObject.toJSONString() + "\r\n", pageEncoding, true);
				String thread = PatternUtil.getLastPatternGroup(wyNews.getCommenturl(), "/([^/]*?)\\.html");
				SpiderHttpClient.getInstance().getNetPageThreadPool()
						.execute(new NewsCommentsTask(newsType,thread));
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		}
		System.out.println(url + "新闻总数:" + count);
	}

	private WyNews getWyNews(JSONObject jsonObject)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		WyNews wynews = new WyNews();
		ReflectUtil.setObjAllValue(jsonObject, wynews, Arrays.asList("wykeywords"));
		JSONArray keywords = wynews.getKeywords();
		for (int i = 0; keywords != null && i < keywords.size(); i++) {
			WyKeyWords wykw = new WyKeyWords();
			ReflectUtil.setObjAllValue(keywords.getJSONObject(i), wykw, null);
		}
		return wynews;
	}
}

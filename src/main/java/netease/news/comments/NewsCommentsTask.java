package netease.news.comments;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.w3c.dom.views.AbstractView;

import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;

import common.spider.HttpClientUtil;
import netease.news.Client;
import netease.news.entity.WyUser;
import utils.NewsType;

/**
 * 需要计算评论api
 * 
 * @author wnc
 *
 */
public class NewsCommentsTask extends AbstractPageTask {
	NewsType newsType;
	String thread;

	public NewsCommentsTask(NewsType newsType, String thread) {
		this.proxyFlag = true;
		this.newsType = newsType;
		this.url = newsType.getCmtPageUrl(thread, 1);
		this.pageEncoding="GBK";
	}

	public NewsCommentsTask(String url) {
		this.proxyFlag = true;
		this.url = url;
		this.pageEncoding="GBK";
	}

	@Override
	protected void retry() {
		SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new NewsCommentsTask(url));
	}

	@Override
	protected void handle(Page page) throws Exception {
		JSONObject parseObject = JSONObject.parseObject(page.getHtml());

		if (newsType != null) {
			int newListSize = parseObject.getInteger("newListSize");
			for (int i = 1; i <= BasicNumberUtil.getDivSplitPage(newListSize, 30); i++) {
				String cmtPageUrl = newsType.getCmtPageUrl(thread, i);
				System.out.println("第"+i+"页url:"+cmtPageUrl);
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new NewsCommentsTask(cmtPageUrl));
			}
		}
		jsonDetail(parseObject);
	}

	private void jsonDetail(JSONObject parseObject) {
		JSONObject jsonObject = parseObject.getJSONObject("comments");
		Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			JSONObject value = (JSONObject) iterator.next().getValue();
			System.out.println(key + " #################### " + value.getString("content"));// Comment
			WyUser wyUser = JSONObject.parseObject(value.getString("user"), WyUser.class);
			String userId = wyUser.getId();
			Integer cid = value.getInteger("commentId");
			if (!Client.seekedCommentIds.contains(cid)) {
				Client.seekedCommentIds.add(cid);
				value.remove("user");
				BasicFileUtil.writeFileString("wynewscomments.txt", value.toJSONString() + "\r\n", pageEncoding, true);
			}

			if (!Client.seekedUserIds.contains(userId)) {
				Client.seekedUserIds.add(userId);
				BasicFileUtil.writeFileString("wynewsusers.txt", JSONObject.toJSONString(wyUser) + "\r\n", pageEncoding,
						true);
			}
		}
	}
}

package news.comments;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.core.httpclient.AbstractHttpClient;
import com.wnc.tools.SoupUtil;

import common.spider.HttpClientUtil;
import netease.news.entity.TopBoard;

public class CommentsSquare {

	private static final int MAX_PAGE = 7;// 9.1 23:21测试数据

	/**
	 * 精彩跟帖
	 */
	@Test
	public void a() {
		String format = "http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/recommendList/single?offset=%d&limit=30&ibc=newspc";
		int count = 0;
		for (int page = 1; page <= MAX_PAGE; page++) {
			System.out.println("\n精彩跟帖###############################################Page" + page);
			String url = String.format(format, (page - 1) * 30);
			try {
				String webPage = HttpClientUtil.getWebPage(url);
				JSONArray parseArray = JSONObject.parseArray(webPage);
				for (int i = 0; i < parseArray.size(); i++) {
					JSONObject jsonObject = parseArray.getJSONObject(i).getJSONArray("comments").getJSONObject(0)
							.getJSONObject("1");
					String content = jsonObject.getString("content");
					int vote = jsonObject.getInteger("vote");
					System.out.println(vote+" "+content);
					count++;
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(count);
	}

	/**
	 * 网友热议
	 */
	@Test
	public void topVote() {
		String format = "http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/heatedList/allSite?page=%d&ibc=newspc";
		int count = 0;
		for (int page = 1; page <= 3; page++) {
			System.out.println("\n网友热议###############################################Page" + page);
			String url = String.format(format, page);
			try {
				String webPage = HttpClientUtil.getWebPage(url);
				JSONArray parseArray = JSONObject.parseArray(webPage);
				for (int i = 0; i < parseArray.size(); i++) {
					JSONArray jsonArray = parseArray.getJSONObject(i).getJSONArray("comments");
					for (int j = 0; j < jsonArray.size(); j++) {
						JSONObject jsonObject = jsonArray.getJSONObject(j).getJSONObject("1");
						String content = jsonObject.getString("content");
						int vote = jsonObject.getInteger("vote");
						System.out.println(vote+" "+content);
						count++;
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(count);
	}

	/**
	 * 精彩盖楼
	 */
	@Test
	public void c() {

	}

	/**
	 * TopSix
	 */
	/**
	 * 热点新闻
	 * 
	 * @throws IOException
	 */
	// @Test
	public void s() throws IOException {
		String format = "http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/topList/channels/allSite?limit=10&ibc=newspc";// 默认limit6,
																																					// 可以设为10
		String webPage = HttpClientUtil.getWebPage(format);
		System.out.println(webPage);
		List<TopBoard> parseArray = JSONObject.parseArray(webPage, TopBoard.class);
		for (Object object : parseArray) {
			System.out.println(object);
		}
	}

	/**
	 * 跟帖达人
	 */
	public void d() {

	}
}

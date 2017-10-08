package news.comments;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import common.spider.HttpClientUtil;
import netease.news.entity.WyUser;

public class NewsCommentsTest {
	@Test
	public void a() throws IOException {
		String url = "http://comment.sports.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CTAHOEJ80005877U/comments/newList?offset=30&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&ibc=newspc&_=1504363968744";
		String webPage = HttpClientUtil.getWebPage(new HttpGet(url), "GBK");
		JSONObject jsonObject = JSONObject.parseObject(webPage).getJSONObject("comments");
		Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			JSONObject value = (JSONObject)iterator.next().getValue();
			System.out.println(key + " " + value.getString("content"));//Comment
			WyUser parseObject = JSONObject.parseObject(value.getString("user"), WyUser.class);
			System.out.println(parseObject);
		}
	}
}

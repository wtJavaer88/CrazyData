package news.comments;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import common.spider.HttpClientUtil;
import netease.news.entity.WyKeyWords;
import netease.news.entity.WyNews;

public class WyNewsApiTest {

	/**
	 * NBA 新闻
	 * 
	 * @throws IOException
	 */
	@Test
	public void a() throws IOException {
		int count = 0;
		String format = "http://sports.163.com/special/000587PK/newsdata_nba_index_%s.js"; // 最大10页
		// http://sports.163.com/special/000587PK/newsdata_nba_index_02.js
		for (int page = 1; page <= 10; page++) {
			String p = page > 9 ? page + "" : "0" + page;
			String url = String.format(format, p);
			if (page == 1) {
				url = "http://sports.163.com/special/000587PK/newsdata_nba_index.js";
			}
			String webPage = HttpClientUtil.getWebPage(new HttpGet(url), "GBK");
			int a = webPage.indexOf('[');
			int b = webPage.lastIndexOf(']');
			webPage = webPage.substring(a, b + 1);
			JSONArray parseArray = JSONObject.parseArray(webPage);
			for (int i = 0; i < parseArray.size(); i++) {
				JSONObject jsonObject = parseArray.getJSONObject(i);
				try {
					getWyNews(jsonObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				count++;
			}
		}
		System.out.println("新闻总数:" + count);
	}

	private WyNews getWyNews(JSONObject jsonObject)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		WyNews wynews = new WyNews();
		setObjAllValue(jsonObject, wynews, Arrays.asList("wykeywords"));
		JSONArray keywords = wynews.getKeywords();
		for (int i = 0; keywords != null && i < keywords.size(); i++) {
			WyKeyWords wykw = new WyKeyWords();
			setObjAllValue(keywords.getJSONObject(i), wykw, null);
			System.out.println(wykw);
		}
		System.out.println(wynews.getTitle());
		return wynews;
	}

	private void setObjAllValue(JSONObject jsonObject, Object obj, List<String> excludes)
			throws NoSuchFieldException, IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (excludes == null || !excludes.contains(field.getName())) {
				setObjValue(jsonObject, obj, field.getName());
			}
		}
	}

	private void setObjValue(JSONObject jsonObject, Object obj, String[] fieldNames)
			throws NoSuchFieldException, IllegalAccessException {
		for (String string : fieldNames) {
			setObjValue(jsonObject, obj, string);
		}
	}

	private void setObjValue(JSONObject jsonObject, Object obj, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, jsonObject.get(fieldName));
	}
}

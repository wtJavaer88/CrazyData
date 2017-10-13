package mafengwo.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import common.spider.HttpClientUtil;

public class AppDataTest {
	static String countryTreeUrl = "https://mapi.mafengwo.cn/rest/app/v2/mdd/tree/?app_version_code=430&oauth_nonce=5942ecaf-4dcd-4e7b-987e-2b5867ff2275&device_type=android&screen_scale=3.0&oauth_consumer_key=5&hardware_model=PE-TL10&time_offset=480&oauth_signature=dEd1aikxa29sPJMb2yqAPEiFFVI%3D&x_auth_mode=client_auth&o_lng=114.487518&oauth_signature_method=HMAC-SHA1&oauth_token=0_0969044fd4edf59957f4a39bce9200c6&app_code=com.mfw.roadbook&o_lat=30.501112&oauth_version=1.0&mfwsdk_ver=20140507&screen_width=1080&device_id=74%3Aa5%3A28%3A10%3A85%3Afb&sys_ver=4.4.2&channel_id=ZhiHuiYun&open_udid=74%3Aa5%3A28%3A10%3A85%3Afb&app_ver=8.0.3&screen_height=1776&oauth_timestamp=1507814262&";
	static String cityUrl = "http://www.mafengwo.cn/mdd/base/list/pagedata_citylist";

	public static void main(String[] args) throws IOException {
		// getCitylistOfCtr(10169);
		getCountryList();
	}

	private static void getCitylistOfCtr(int country) throws IOException {
		int i = 1, pages = 1;
		while (i <= pages) {
			System.out.println("currentPage:" + i);
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("mddid", country + "");
				params.put("page", "" + i);
				String webPage = HttpClientUtil.postRequest(cityUrl, params);
				JSONObject parseObject = JSONObject.parseObject(webPage.replace("\\\"", "").replace("\\n", ""));
				if (i == 1) {
					pages = BasicNumberUtil
							.getNumber(PatternUtil.getLastPattern(parseObject.getString("page"), "\\d+"));
				}
				// System.out.println(JSONObject.toJSONString(parseObject,
				// SerializerFeature.PrettyFormat));
				String html = parseObject.getString("list");
				BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/citylist/" + country + "/" + i + ".html", html, null,
						false);
				Elements select = Jsoup.parse(html).select("body > li > div.img > a");
				for (Element element : select) {
					System.out.println("city:" + element.attr("data-id") + " " + element.text());
					BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/citylist/cityall.txt", element.attr("data-id") + "\r\n",
							null, true);
				}
			} catch (Exception e) {
				BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/citylist/" + country + "/err" + ".html",
						country + "/" + i + "/" + e.toString() + "\r\n", null, true);
				e.printStackTrace();
			}
			i++;
		}
	}

	private static void getCountryList() throws IOException {
		String webPage = HttpClientUtil.getWebPage(countryTreeUrl);
		JSONObject parseObject = JSONObject.parseObject(webPage);
		// System.out.println(JSONObject.toJSONString(parseObject,
		// SerializerFeature.PrettyFormat));
		JSONArray jsonArray = parseObject.getJSONObject("data").getJSONArray("list");
		for (int i = 1; i < jsonArray.size(); i++) {
			JSONObject zhouJson = jsonArray.getJSONObject(i);
			String string = zhouJson.getString("name");
			System.out.println(string + "#####################");
			JSONArray jsonArray2 = zhouJson.getJSONArray("groups").getJSONObject(0).getJSONArray("list");
			for (int j = 0; j < jsonArray2.size(); j++) {
				JSONObject countryJson = jsonArray2.getJSONObject(j);
				Integer countryid = countryJson.getInteger("id");
				BasicFileUtil.makeDirectory("F:/资源/爬虫/马蜂窝/citylist/" + countryid);
				System.out.println(countryid + "-" + countryJson.getString("title"));
				// System.out.println(countryJson.getString("thumnaiil"));
				// System.out.println(countryJson.getString("jump_url"));

				getCitylistOfCtr(countryid);
			}
		}
	}
}

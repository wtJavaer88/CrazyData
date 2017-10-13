package mafengwo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import common.spider.HttpClientUtil;

public class TestMdd {
	static String url1 = "https://m.mafengwo.cn/jd/11110/gonglve.html";
	static String url2 = "https://m.mafengwo.cn//poi/6300682.html";
	static String url3 = "https://mapi.mafengwo.cn/travelguide/mdd/10169/submdds?o_lat=30.499615&app_version_code=430&oauth_version=1.0&oauth_nonce=66eb26ff-3cce-4106-ae54-d42f701e777d&oauth_consumer_key=5&screen_scale=3.0&device_type=android&hardware_model=PE-TL10&mfwsdk_ver=20140507&screen_width=1080&device_id=74%3Aa5%3A28%3A10%3A85%3Afb&sys_ver=4.4.2&channel_id=ZhiHuiYun&oauth_signature=mZ%2FZFg8dI67Lp8DLVB2QOf7uOng%3D&x_auth_mode=client_auth&o_lng=114.48791&style=squares&start=0&oauth_signature_method=HMAC-SHA1&oauth_token=0_0969044fd4edf59957f4a39bce9200c6&open_udid=74%3Aa5%3A28%3A10%3A85%3Afb&app_ver=8.0.3&app_code=com.mfw.roadbook&oauth_timestamp=1507808067&screen_height=1776";

	public static void main(String[] args) throws IOException {
		HttpGet request = new HttpGet(url1);
		// m.mafengwo.cn
		// Caused by: org.apache.http.client.CircularRedirectException: Circular
		// redirect to '

		request.addHeader("Host", "m.mafengwo.cn");
		// request.addHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// request.addHeader("Referer", "https://m.mafengwo.cn/mdd/11110");
		request.setHeader("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.4.2; PE-TL10 Build/HuaweiPE-TL10)");
		CloseableHttpResponse response = HttpClientUtil.getResponse(request);
		HttpEntity entity = response.getEntity();
		String string = EntityUtils.toString(entity);
		System.out.println(string);
		// BasicFileUtil.writeFileString("jd.html", string, null, false);
		System.out.println(string.contains("市中心"));
	}
}

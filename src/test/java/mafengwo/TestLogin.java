package mafengwo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import common.spider.HttpClientUtil;

public class TestLogin {
	public static void main(String[] args) throws IOException {
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("passport", "15102760362");
		postParams.put("password", "w2101024");
		String postRequest = HttpClientUtil.postRequest("https://passport.mafengwo.cn/login-popup.html", postParams);
		System.out.println(postRequest);
		String webPage = HttpClientUtil.getWebPage("http://www.mafengwo.cn/u/40772750.html");

		webPage = HttpClientUtil
				.getWebPage("http://www.mafengwo.cn/friend/index/follow?flag=1&name=&uid=79568445&page=3");
		System.out.println(webPage);

	}
}

package mafengwo;

import java.io.IOException;

import com.wnc.string.PatternUtil;

import common.spider.HttpClientUtil;

public class TestMusic {
	public static void main(String[] args) throws IOException {
		int note_id = 7260158;
		String url = "http://pagelet.mafengwo.cn/note/pagelet/headOperateApi?params=%7B%22iid%22%3A%22" + note_id
				+ "%22%7D&_=1508328975002";
		String webPage = HttpClientUtil.getWebPage(url);
		System.out.println(webPage.contains(".mp3"));
		// System.out.println(JSONObject.toJSONString(JSONObject.parse(webPage),
		// SerializerFeature.PrettyFormat));
		System.out.println(
				PatternUtil.getFirstPatternGroup(webPage, "data-music=\\\\\"(http.*?mp3)\\\\").replace("\\/", "/"));
	}
}

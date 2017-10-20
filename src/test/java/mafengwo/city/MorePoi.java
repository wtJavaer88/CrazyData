package mafengwo.city;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class MorePoi {
	public static void main(String[] args) throws IOException {
		parseJson();
		// parseDoc();
	}

	private static void parseDoc() throws IOException {
		Document parse = Jsoup.parse(new File("E:/CrazyData/demo/马蜂窝/page11.html"), "GBK");
		Elements select = parse.select(".poi-list > div > a");
		List<JSONObject> poiList = new ArrayList<JSONObject>();
		for (Element element : select) {
			JSONObject jobj = new JSONObject();
			jobj.put("u", element.attr("href").trim());
			jobj.put("t", element.select(".hd").text().replace(" ", ""));

			Element bd = element.select(".bd").first();
			if (bd != null) {
				if (bd.select("dt img").first() != null)
					jobj.put("i", PatternUtil
							.getFirstPatternGroup(bd.select("dt img").first().attr("src"), "(.*?)\\?.*$").trim());
				if (bd.select(".star").first() != null)
					jobj.put("s", PatternUtil.getFirstPatternGroup(bd.select(".star").attr("style"), "width:(\\d+)"));
				String num = bd.select("p span.num").text();
				List<String> patternStrings = PatternUtil.getPatternStrings(num, "\\d+");
				if (patternStrings.size() > 0) {
					jobj.put("n1", patternStrings.get(0));
					if (patternStrings.size() > 1) {
						jobj.put("n2", patternStrings.get(1));
					}
				}
				jobj.put("mt", Arrays.asList(bd.select(".m-t").text().replace(" ", "").split(" ")));
				if (bd.select("p").last() != null)
					jobj.put("l", bd.select("p").last().text());

				if (bd.select(".comment").size() > 0) {
					jobj.put("r", bd.select(".comment").first().ownText().replace(" ", ""));
				}
			}
			poiList.add(jobj);
		}
		JSONObject cityJson = new JSONObject();
		cityJson.put("c", 11110);
		cityJson.put("list", poiList);
		System.out.println(cityJson);
		BasicFileUtil.writeFileString("E:/CrazyData/demo/马蜂窝/poilist.txt", cityJson + "\r\n", null, true);
	}

	private static void parseJson() {
		List<String> readFrom = FileOp.readFrom("E:/CrazyData/demo/马蜂窝/city/city-gw.txt");
		String string = StringUtils.join(readFrom, "");

		JSONObject obj = JSONObject.parseObject(string.replace("\\n", ""));
		System.out.println(obj.getInteger("has_more"));
		System.out.println(obj.getString("html"));

		List<String> readFrom2 = FileOp.readFrom("E:/CrazyData/demo/马蜂窝/city/city-hotel.txt");
		String join = StringUtils.join(readFrom2, "").replace("\\\"", "\"").replace("\\\\", "\\").replace("\"{", "{")
				.replace("}\"", "}");
		System.out.println(join);
		System.out.println(JSONObject.parseObject(join));
		System.out.println(URLDecoder.decode("\u6d6a\u6f2b\u60c5\u4fa3"));
	}
}

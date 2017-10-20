package mafengwo.city;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.tools.FileOp;

import common.spider.HttpClientUtil;

public class HotelTest {
	public static void main(String[] args) {
		// parseJson();
		downloadHotels();
	}

	private static void downloadHotels() {
		String allcity = "F:/资源/爬虫/马蜂窝/citylist/cityall-1.txt";
		String midCity = "";
		boolean flag = StringUtils.isBlank(midCity);
		for (String city : FileOp.readFrom(allcity)) {
			BasicFileUtil.makeDirectory("F:/资源/爬虫/马蜂窝/hotel/" + city);
			int page = 1;
			if (!flag && city.equals(midCity)) {
				flag = true;
			}
			if (!flag) {
				continue;
			}
			String pageUrl = null;
			try {
				while (true) {
					if (!BasicFileUtil.isExistFile("F:/资源/爬虫/马蜂窝/hotel/" + city + "/" + page + ".html")) {

						String url = "http://www.mafengwo.cn/hotel/ajax.php?iMddId=" + city
								+ "&iAreaId=-1&iRegionId=-1&iPoiId=&position_name=&nLat=0&nLng=0&iDistance=10000&sCheckIn=2017-12-22&sCheckOut=2017-12-23&iAdultsNum=2&iChildrenNum=0&sChildrenAge=&iPriceMin=&iPriceMax=&sTags=&sSortType=comment&sSortFlag=DESC&has_booking_rooms=0&has_faved=0&sKeyWord=&iPage={PAGE}&sAction=getPoiList5";
						String webPage;
						pageUrl = url.replace("{PAGE}", page + "");

						webPage = HttpClientUtil.getWebPage(pageUrl);
						System.out.println(city + " / " + page);
						JSONObject parseObject = JSONObject.parseObject(webPage);
						BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/hotel/" + city + "/" + page + ".html",
								parseObject.getString("html"), null, false);
						int divSplitPage = BasicNumberUtil
								.getDivSplitPage(parseObject.getJSONObject("msg").getInteger("count"), 20);
						if (divSplitPage == 0 || page > 10000 || page >= divSplitPage) {
							break;
						}
					}
					page++;

				}
			} catch (IOException e) {
				BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/hotel/hotel-errs.txt",
						pageUrl + " " + e.getMessage() + "\r\n", null, true);
				e.printStackTrace();
			}
		}
	}

	private static void parseJson() {
		List<String> readFrom2 = FileOp.readFrom("E:/CrazyData/demo/马蜂窝/city/hotel-pc.txt");
		String join = StringUtils.join(readFrom2, "");
		JSONObject obj = JSONObject.parseObject(join);
		System.out.println(obj.getString("html"));
	}
}

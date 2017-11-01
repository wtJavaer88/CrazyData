package dianping.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.SoupUtil;

import dianping.client.CityZoneClient;
import dianping.entity.CityZone;

public class CityZoneParser {
	final static String REGION_SELECTOR = "#region-nav a";
	final static String HOT_ZONE_SELECTOR = "#region-nav-sub a";
	final static String FAMOUS_POINT_SELECTOR = "#bussi-nav-sub a";

	/**
	 * 获取行政区
	 * 
	 * @param cityCode
	 */
	public List<CityZone> getRegions(CityZone cityZone) {
		return commonLinkParse(cityZone, REGION_SELECTOR);
	}

	/**
	 * 获取行政区下面的热门商圈
	 * 
	 * @param regionCode
	 */
	public List<CityZone> getRegionHotZones(CityZone cityZone) {
		return commonLinkParse(cityZone, HOT_ZONE_SELECTOR);
	}

	/**
	 * 获取商圈中的著名地标
	 * 
	 * @param zoneDoc
	 *            是静态的, 还是需要动态去获取的
	 */
	public List<CityZone> getFamousPoint(CityZone cityZone) {
		return commonLinkParse(cityZone, FAMOUS_POINT_SELECTOR);
	}

	/**
	 * 公用
	 * 
	 * @param cityZone
	 * @param selector
	 * @param level
	 * @return
	 */
	private List<CityZone> commonLinkParse(CityZone cityZone, String selector) {
		List<CityZone> list = new ArrayList<CityZone>();
		String pCode = cityZone.getCode();
		try {
			Document parse = SoupUtil.getDoc(cityZone.getSearchUrl());
			Elements select = parse.select(selector);
			for (Element element : select) {
				String text = element.text();
				if (!"不限".equals(text) && !"更多".equals(text)) {
					// System.out.println( text );
					String code = PatternUtil.getLastPatternGroup(element.absUrl("href"), "/([^/]*+)$");
					list.add(new CityZone(pCode, code, text, element.absUrl("href"), cityZone.getLevel() + 1,
							cityZone.getOwnString() + code + "-"));
				}
			}
		} catch (Exception e) {
			BasicFileUtil.writeFileString(CityZoneClient.errlogFile,
					"commonLinkParse/" + cityZone.toString() + e.getMessage() + "\r\n", null, true);
			e.printStackTrace();
		}
		return list;
	}

}

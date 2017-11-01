package dianping.client;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.wnc.basic.BasicFileUtil;

import db.DBconnectionMgr;
import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import dianping.entity.CityZone;
import dianping.parser.CityZoneParser;

public class CityZoneClient {
	final static String logFile = "F:/资源/爬虫/大众点评/cityzone/log.txt";
	public final static String errlogFile = "F:/资源/爬虫/大众点评/cityzone/err-log.txt";

	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\dianping.db");
		CityZoneParser cityHotZoneParser = new CityZoneParser();
		CityZone city = new CityZone("2", "北京", "http://www.dianping.com/search/category/2/10");

		List<CityZone> regions = cityHotZoneParser.getRegions(city);
		city.setChildCount(regions.size());
		insertCityZone(city);
		log("北京下属行政区数:" + city.getChildCount());

		for (CityZone region : regions) {
			List<CityZone> regionHotZones = cityHotZoneParser.getRegionHotZones(region);
			region.setChildCount(regionHotZones.size());
			insertCityZone(region);
			log("【" + region.getName() + "】下属商圈数:" + region.getChildCount());

			for (CityZone hotZone : regionHotZones) {
				insertCityZone(hotZone);
				getFamous(cityHotZoneParser, region, hotZone);
			}
		}
	}

	private static void getFamous(CityZoneParser cityHotZoneParser, CityZone region, CityZone hotZone) {
		List<CityZone> famousPoint = cityHotZoneParser.getFamousPoint(hotZone);
		hotZone.setChildCount(famousPoint.size());
		log("【" + region.getName() + "-" + hotZone.getName() + "】下属地标数:" + hotZone.getChildCount());

		for (CityZone cityZone3 : famousPoint) {
			log("地标:" + cityZone3);
			insertCityZone(cityZone3);
		}
	}

	private static void log(String string) {
		System.out.println(string);
		BasicFileUtil.writeFileString(logFile, string + "\r\n", null, true);

	}

	private static void insertCityZone(CityZone cityZone) {
		DbFieldSqlUtil util = new DbFieldSqlUtil("CITY_ZONE");
		util.addInsertField(new DbField("CODE", cityZone.getCode()));
		util.addInsertField(new DbField("NAME", StringEscapeUtils.escapeSql(cityZone.getName())));
		util.addInsertField(new DbField("PCODE", cityZone.getpCode()));
		util.addInsertField(new DbField("SEARCH_URL", cityZone.getSearchUrl()));
		util.addInsertField(new DbField("OWN_STRING", cityZone.getOwnString()));
		util.addInsertField(new DbField("LEVEL", cityZone.getLevel() + "", "NUMBER"));
		util.addInsertField(new DbField("CHILD_COUNT", cityZone.getChildCount() + "", "NUMBER"));
		try {
			DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
		} catch (SQLException e) {
			BasicFileUtil.writeFileString(errlogFile, cityZone.toString() + e.getMessage() + "\r\n", null, true);
			e.printStackTrace();
		}
	}

}

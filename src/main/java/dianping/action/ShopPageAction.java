package dianping.action;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;

import db.DbExecMgr;
import dianping.task.ShopPageTask;

public class ShopPageAction implements MySpiderAction {
	private final static String CITY = "2";
	public final static String huoguoStr = "http://www.dianping.com/search/category/" + CITY + "/10/g110%s";

	public void execute() {
		childZonesParse(CITY);
	}

	private static void childZonesParse(String pcode) {
		Map zoneMap = DbExecMgr.getSelectAllSqlMap("SELECT * FROM CITY_ZONE WHERE PCODE = '" + pcode + "'");
		Map fieldMap;
		for (int i = 1; i <= zoneMap.size(); i++) {
			fieldMap = (Map) zoneMap.get(i);
			String code = String.valueOf(fieldMap.get("CODE"));
			SpiderHttpClient.getInstance().getNetPageThreadPool()
					.execute(new ShopPageTask(String.format(huoguoStr, code), 1, code));

		}
	}

}

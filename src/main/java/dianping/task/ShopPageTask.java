package dianping.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;

import db.DbExecMgr;
import dianping.entity.Shop;
import dianping.entity.ShopDeal;
import dianping.parser.ShopListPageParser;
import javlib.utils.RetryMgr;
import spiderqueue.sqlreflect.SqlFastReflect;

public class ShopPageTask extends AbstractPageTask {
	private final static String huoguoStr = "http://www.dianping.com/search/category/9/10/g110%s";
	private static final String ERR_TXT = "F:/资源/爬虫/大众点评/shop/cq-huoguo-shop-err.txt";

	int pageIndex = 1;
	String headUrl = "";
	String zoneCode;

	public ShopPageTask(String headUrl, int pageIndex, String zoneCode) {
		this.headUrl = headUrl;
		this.pageIndex = pageIndex;
		this.zoneCode = zoneCode;
		this.url = headUrl + 'p' + pageIndex;
		this.proxyFlag = true;
	}

	@Override
	protected void retry() {
		boolean addUrlAndStop = RetryMgr.addUrlAndStop(url, 10);
		if (!addUrlAndStop) {
			SpiderHttpClient.getInstance().getNetPageThreadPool()
					.execute(new ShopPageTask(headUrl, pageIndex, zoneCode));
		} else {
			RetryMgr.logTimeout(url);
		}
	}

	@Override
	protected void handle(Page page) throws Exception {
		Document doc = getDoc(page);
		spiderHttpClient.parseCount.getAndIncrement();

		ShopListPageParser shopListPageParser = new ShopListPageParser(doc);
		if (pageIndex == 1) {
			int maxPage = shopListPageParser.getMaxPage();
			if (maxPage >= 50) {
				// 超过50的页情况下先不保存当前页
				System.out.println(zoneCode + " more than 50 pages");
				Map zoneMap = DbExecMgr.getSelectAllSqlMap("SELECT * FROM CITY_ZONE WHERE PCODE = '" + zoneCode + "'");
				Map fieldMap;
				for (int i = 1; i <= zoneMap.size(); i++) {
					fieldMap = (Map) zoneMap.get(i);
					String code = String.valueOf(fieldMap.get("CODE"));
					SpiderHttpClient.getInstance().getNetPageThreadPool()
							.execute(new ShopPageTask(String.format(huoguoStr, code), 1, code));
				}
			} else {
				System.out.println(zoneCode + " pages:" + maxPage);
				// 否则保存当前页, 并开始后续页数的解析
				saveShopList(shopListPageParser);
				for (int i = 2; i <= maxPage; i++) {
					SpiderHttpClient.getInstance().getNetPageThreadPool()
							.execute(new ShopPageTask(headUrl, i, zoneCode));
				}
			}
		} else {// 如果不是第一页, 保存当前页数据
			saveShopList(shopListPageParser);
		}

	}

	private void saveShopList(ShopListPageParser shopListPageParser) {
		List<Shop> parseShopList = getShopList(shopListPageParser);
		if (parseShopList.isEmpty()) {
			System.out.println("url's data is empty:" + url);
			BasicFileUtil.writeFileString(ERR_TXT, "[saveShopList]" + url + "\r\n", null, true);
			return;
		}
		SqlFastReflect sqlFastReflect = new SqlFastReflect("SHOP");
		for (Shop shop : parseShopList) {
			if (DbExecMgr.isExistData("SHOP", "ID", "" + shop.getId())) {
				continue;
			}
			shop.setCreateDate(BasicDateUtil.getCurrentDateTimeString());
			String insertSql = "";
			try {
				insertSql = sqlFastReflect.setDbEntity(shop).setSqlSysout(false).getInsertSql("ID", "TITLE", "PIC",
						"HAS_BRANCH", "BRANCH_ID", "WAIMAI", "ORDER_SITE", "YOUHUI", "REVIEW_NUM", "MEAN_PRICE",
						"BUSI_TAG", "ADDR_TAG", "ADDRESS_DETAIL", "TASTE_SCORE", "ENV_SCORE", "SRV_SCORE", "DEALS",
						"CREATE_DATE");
				DbExecMgr.execOnlyOneUpdate(insertSql);
			} catch (Exception e) {
				BasicFileUtil.writeFileString(ERR_TXT,
						"[saveShopList]" + url + "/" + insertSql + "/" + e.getMessage() + "\r\n", null, true);
				e.printStackTrace();
			}
			sqlFastReflect.clearDbSqlUtil();

			saveShopDeals(shop.getShopDeals());
		}
	}

	private void saveShopDeals(List<ShopDeal> shopDeals) {
		SqlFastReflect sqlFastReflect = new SqlFastReflect("SHOP_DEAL");
		for (ShopDeal shopDeal : shopDeals) {
			if (DbExecMgr.isExistData("SHOP_DEAL", "ID", "" + shopDeal.getId())) {
				continue;
			}
			String insertSql = "";
			try {
				insertSql = sqlFastReflect.setDbEntity(shopDeal).setSqlSysout(false).getInsertSql("ID", "SHOP_ID");
				DbExecMgr.execOnlyOneUpdate(insertSql);
			} catch (Exception e) {
				BasicFileUtil.writeFileString(ERR_TXT,
						"[saveShopDeals]" + url + "/" + insertSql + "/" + e.getMessage() + "\r\n", null, true);
				e.printStackTrace();
			}
			sqlFastReflect.clearDbSqlUtil();
		}
	}

	private List<Shop> getShopList(ShopListPageParser shopListPageParser) {
		List<Shop> parseShopList = new ArrayList<Shop>();
		try {
			parseShopList = shopListPageParser.parseShopList();
		} catch (Exception e) {
			BasicFileUtil.writeFileString(ERR_TXT, "[getShopList]" + url + "/" + e.getMessage() + "\r\n", null, true);
			e.printStackTrace();
		}
		return parseShopList;
	}
}
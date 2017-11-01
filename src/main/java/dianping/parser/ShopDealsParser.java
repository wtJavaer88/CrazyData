package dianping.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import dianping.entity.ShopDeal;

public class ShopDealsParser {
	Document parse;
	List<ShopDeal> allDeals = new ArrayList<ShopDeal>();

	public ShopDealsParser(Document parse) {
		this.parse = parse;
	}

	public List<ShopDeal> parseDeals() {
		try {
			parseHeadDeal();
			parseDealList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allDeals;
	}

	private void parseHeadDeal() {
		String desc = parse.select(".title").text() + parse.select(".sub-title").text();
		double originPrice = BasicNumberUtil.getDouble(PatternUtil.getFirstPattern(
				parse.select("div.bd > div.price-wrap > h3.price > span.discount > span.price-original").text(),
				"[\\d\\.]+"));
		double actualPrice = BasicNumberUtil.getDouble(PatternUtil.getFirstPattern(
				parse.select("div.bd > div.price-wrap > h3.price > span.price-display").text(), "[\\d\\.]+"));
		int sellCount = crt2num(PatternUtil.getFirstPattern(parse
				.select("div.bd > div.action-box > table > tbody > tr > td:nth-child(1) > div > span > em.J_current_join")
				.text(), "\\d+"));
		ShopDeal shopDeal = new ShopDeal(desc, originPrice, actualPrice, sellCount);
		allDeals.add(shopDeal);
	}

	private void parseDealList() {
		Elements select = parse.select("div.content > div.other-deal-box > div.cont > ul > li > a");
		for (Element element : select) {
			double originPrice = BasicNumberUtil
					.getDouble(PatternUtil.getFirstPattern(element.select(".sellprice").text(), "[\\d\\.]+"));
			double actualPrice = BasicNumberUtil
					.getDouble(PatternUtil.getFirstPattern(element.select(".price").text(), "[\\d\\.]+"));
			int sellCount = crt2num(PatternUtil.getFirstPattern(element.select(".num").text(), "\\d+"));
			String desc = element.select(".infor").text();
			ShopDeal shopDeal = new ShopDeal(desc, originPrice, actualPrice, sellCount);
			allDeals.add(shopDeal);
		}
	}

	private int crt2num(String str) {
		return BasicNumberUtil.getNumber(str);
	}

	public static void main(String[] args) throws IOException {
		Document parse = Jsoup.parse(new File("D:/project/CrazyData/demo/大众点评/deal.html"), "UTF-8");
		List<ShopDeal> parseDeals = new ShopDealsParser(parse).parseDeals();
		for (ShopDeal shopDeal : parseDeals) {
			System.out.println(shopDeal);
		}
	}
}


package dianping.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.SoupUtil;

import common.spider.node.MyElement;
import dianping.entity.Shop;
import dianping.entity.ShopDeal;

public class ShopListPageParser {
	Document parse;

	public ShopListPageParser(Document parse) {
		this.parse = parse;
	}

	public static void main(String[] args) throws IOException {
		// Document parse = Jsoup.parse(new
		// File("D:/project/CrazyData/demo/大众点评/list-zone.html"), "UTF-8");
		Document doc = SoupUtil.getDoc("http://www.dianping.com/search/category/9/10/g110r5742p1");
		new ShopListPageParser(doc).parseShopList();
	}

	public int getMaxPage() {
		String pages = "1";
		Elements select = parse.select("div.page a.PageLink");
		if (select.size() > 0) {
			pages = select.last().text().trim();
		}
		return BasicNumberUtil.getNumber(pages);
	}

	public List<Shop> parseShopList() {
		List<Shop> list = new ArrayList<Shop>();
		Elements select = parse.select("#shop-all-list > ul > li");
		for (Element element : select) {
			Shop shop = new Shop();
			try {
				int id = getInt(PatternUtil.getLastPattern(element.select("div.pic a").attr("href"), "\\d+"));
				shop.setId(id);

				String pic = element.select("div.pic > a:nth-child(1) > img").attr("data-src");
				shop.setPic(pic);

				String title = element.select("div.txt > div.tit > a:nth-child(1) > h4").text();
				shop.setTitle(title);

				boolean b = element.select("a[class=shop-branch]").size() == 1;
				shop.setHasBranch(b ? '1' : '0');
				if (b)
					shop.setBranchId(getInt(PatternUtil
							.getLastPatternGroup(element.select("a[class=shop-branch]").attr("href"), "\\d+")));

				shop.setOrderSite(element.select("div > a.ibook.icon-only").size() > 0 ? '1' : '0');
				shop.setWaimai(element.select("div > a.iout.icon-only").size() > 0 ? '1' : '0');

				if (element.select(".review-num").size() > 0) {
					String reviewNum = new MyElement(element.select(".review-num").first()).pattern4Text("\\d+");
					shop.setReviewNum(getInt(reviewNum));
				}

				if (element.select(".mean-price").size() > 0) {
					String meanPrice = new MyElement(element.select(".mean-price").first()).pattern4Text("\\d+");
					shop.setMeanPrice(getDouble(meanPrice));
				}

				if (element.select(".svr-info a span:contains(优惠)").size() > 0) {
					String ownText = element.select(".svr-info a span:contains(优惠)").get(0).parent().ownText();
					shop.setYouhui(ownText);
				}
				shop.setBusiTag(PatternUtil.getLastPatternGroup(
						element.select("div.tag-addr > a:nth-child(1)").attr("href"), "/([^/]+)$"));
				shop.setAddrTag(PatternUtil.getLastPatternGroup(
						element.select("div.tag-addr > a:nth-child(3)").attr("href"), "/([^/]+)$"));
				shop.setAddressDetail(element.select("div.tag-addr > span.addr").text());

				shop.setTasteScore(getDouble(element.select(".comment-list span:nth-child(1) b").text()));
				shop.setEnvScore(getDouble(element.select(".comment-list span:nth-child(2) b").text()));
				shop.setSrvScore(getDouble(element.select(".comment-list span:nth-child(3) b").text()));

				Elements select2 = element.select(".svr-info .si-deal a[target=_blank]");
				shop.setDeals(select2.size());

				List<ShopDeal> shopDeals = new ArrayList<ShopDeal>();
				for (Element element2 : select2) {
					int dealId = getInt(PatternUtil.getLastPattern(element2.attr("href"), "\\d+"));
					shopDeals.add(new ShopDeal(dealId, id));
				}
				shop.setShopDeals(shopDeals);
				list.add(shop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private Double getDouble(String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		return BasicNumberUtil.getDouble(text);
	}

	private int getInt(String text) {
		return BasicNumberUtil.getNumber(text);
	}
}

package dianping.entity;

import spiderqueue.core.DbEntity;

public class ShopDeal implements DbEntity {
	private int id;
	private int shopId;
	private String desc;
	private double originPrice;
	private double actualPrice;
	private int sellCount;

	public ShopDeal(int id, int shopId) {
		this.id = id;
		this.shopId = shopId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(double originPrice) {
		this.originPrice = originPrice;
	}

	public double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public ShopDeal(String desc, double originPrice, double actualPrice, int sellCount) {
		super();
		this.desc = desc;
		this.originPrice = originPrice;
		this.actualPrice = actualPrice;
		this.sellCount = sellCount;
	}

	public int getSellCount() {
		return sellCount;
	}

	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}

	@Override
	public String toString() {
		return "ShopDeal [id=" + id + ", shopId=" + shopId + ", desc=" + desc + ", originPrice=" + originPrice
				+ ", actualPrice=" + actualPrice + ", sellCount=" + sellCount + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogKey() {
		return this.id + "";
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

}
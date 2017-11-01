package dianping.entity;

import java.util.List;

import spiderqueue.core.DbEntity;

public class Shop implements DbEntity {
	private String title;// 店名
	private String pic;// 店铺图片
	private int id;// 店铺ID

	private char hasBranch = '0';// 是否分店1/0
	private Integer branchId;// 分店id

	private char waimai = '0';// 是否外送1/0
	private char orderSite = '0';// 是否可订座1/0

	private String youhui;

	private Integer reviewNum;// 点评数
	private Double meanPrice;// 人均消费
	private String busiTag;// 业务标签
	private String addrTag;// 地址标签
	private String addressDetail;// 详细地址

	private Double tasteScore;// 口味分
	private Double envScore;// 环境分
	private Double srvScore;// 服务分

	private int deals;// 团购数量

	private List<ShopDeal> shopDeals;// 团购列表
	private String createDate;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getReviewNum() {
		return reviewNum;
	}

	public void setReviewNum(Integer reviewNum) {
		this.reviewNum = reviewNum;
	}

	public Double getMeanPrice() {
		return meanPrice;
	}

	public void setMeanPrice(Double meanPrice) {
		this.meanPrice = meanPrice;
	}

	public String getBusiTag() {
		return busiTag;
	}

	public void setBusiTag(String busiTag) {
		this.busiTag = busiTag;
	}

	public String getAddrTag() {
		return addrTag;
	}

	public void setAddrTag(String addrTag) {
		this.addrTag = addrTag;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public Double getTasteScore() {
		return tasteScore;
	}

	public void setTasteScore(Double tasteScore) {
		this.tasteScore = tasteScore;
	}

	public Double getEnvScore() {
		return envScore;
	}

	public void setEnvScore(Double envScore) {
		this.envScore = envScore;
	}

	public int getDeals() {
		return deals;
	}

	public void setDeals(int deals) {
		this.deals = deals;
	}

	@Override
	public String toString() {
		return "Shop [title=" + title + ", pic=" + pic + ", id=" + id + ", hasBranch=" + hasBranch + ", branchId="
				+ branchId + ", waimai=" + waimai + ", orderSite=" + orderSite + ", youhui=" + youhui + ", reviewNum="
				+ reviewNum + ", meanPrice=" + meanPrice + ", busiTag=" + busiTag + ", addrTag=" + addrTag
				+ ", addressDetail=" + addressDetail + ", tasteScore=" + tasteScore + ", envScore=" + envScore
				+ ", srvScore=" + srvScore + ", deals=" + deals + ", shopDeals=" + shopDeals + "]";
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

	public List<ShopDeal> getShopDeals() {
		return shopDeals;
	}

	public void setShopDeals(List<ShopDeal> shopDeals) {
		this.shopDeals = shopDeals;
	}

	public char getHasBranch() {
		return hasBranch;
	}

	public void setHasBranch(char hasBranch) {
		this.hasBranch = hasBranch;
	}

	public char getWaimai() {
		return waimai;
	}

	public void setWaimai(char waimai) {
		this.waimai = waimai;
	}

	public char getOrderSite() {
		return orderSite;
	}

	public void setOrderSite(char orderSite) {
		this.orderSite = orderSite;
	}

	public String getYouhui() {
		return youhui;
	}

	public void setYouhui(String youhui) {
		this.youhui = youhui;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setSrvScore(Double srvScore) {
		this.srvScore = srvScore;
	}

}
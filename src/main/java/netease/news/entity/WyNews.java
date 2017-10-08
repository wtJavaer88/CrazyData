package netease.news.entity;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

public class WyNews {
	private String add1;
	private String add2;
	private String add3;
	private String commenturl;
	private String docurl;//新闻url
	private String imgurl;
	private JSONArray keywords;//关键字
	private List<WyKeyWords> wykeywords;//关键字
	private String label;//标签
	private String newstype;//新闻类型
	private int tienum;//跟帖数
	private String time;
	private String title;
//	private String tlastid;
	private String tlink;//父类地址
	public String getAdd1() {
		return add1;
	}
	public void setAdd1(String add1) {
		this.add1 = add1;
	}
	public String getAdd2() {
		return add2;
	}
	public void setAdd2(String add2) {
		this.add2 = add2;
	}
	public String getAdd3() {
		return add3;
	}
	public void setAdd3(String add3) {
		this.add3 = add3;
	}
	public String getCommenturl() {
		return commenturl;
	}
	public void setCommenturl(String commenturl) {
		this.commenturl = commenturl;
	}
	public String getDocurl() {
		return docurl;
	}
	public void setDocurl(String docurl) {
		this.docurl = docurl;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getNewstype() {
		return newstype;
	}
	public void setNewstype(String newstype) {
		this.newstype = newstype;
	}
	public int getTienum() {
		return tienum;
	}
	public void setTienum(int tienum) {
		this.tienum = tienum;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTlink() {
		return tlink;
	}
	public void setTlink(String tlink) {
		this.tlink = tlink;
	}
	public List<WyKeyWords> getWykeywords() {
		return wykeywords;
	}
	public void setWykeywords(List<WyKeyWords> wykeywords) {
		this.wykeywords = wykeywords;
	}
	public JSONArray getKeywords() {
		return keywords;
	}
	public void setKeywords(JSONArray keywords) {
		this.keywords = keywords;
	}
	
}

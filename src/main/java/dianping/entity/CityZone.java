package dianping.entity;

public class CityZone {
	private String pCode;
	private String code;
	private String name;
	private String searchUrl;
	private String ownString;
	private int childCount;

	public String getOwnString() {
		return ownString;
	}

	public void setOwnString(String ownString) {
		this.ownString = ownString;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	private int level;// 1:城市, 2:行政区, 3:热门商圈, 4:商圈地标

	// 城市通过此初始化
	public CityZone(String code, String name, String searchUrl) {
		this(null, code, name, searchUrl, 1, code + "-");
	}

	public CityZone(String pCode, String code, String name, String searchUrl, int level, String ownString) {
		super();
		this.pCode = pCode;
		this.code = code;
		this.name = name;
		this.searchUrl = searchUrl;
		this.level = level;
		this.ownString = ownString;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "CityZone [pCode=" + pCode + ", code=" + code + ", name=" + name + ", searchUrl=" + searchUrl
				+ ", ownString=" + ownString + ", childCount=" + childCount + ", level=" + level + "]";
	}

}

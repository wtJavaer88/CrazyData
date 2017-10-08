package utils;

public enum NewsType {
	NBA("http://sports.163.com/special/000587PK/newsdata_nba_index_%s.js",
			"http://comment.sports.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/%s/comments/newList?offset=%d&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&ibc=newspc");
	String newsFormat;
	String cmtFormat;

	NewsType(String newsFormat, String cmtFormat) {
		this.newsFormat = newsFormat;
		this.cmtFormat = cmtFormat;
	}

	public String getNewsPageUrl(int page) {
		String p = page > 9 ? page + "" : "0" + page;
		String url = String.format(newsFormat, p);
		if (page == 1) {
			url = url.replace("_01.js", ".js");
		}
		return url;
	}

	public String getCmtPageUrl(String thread, int page) {
		int offset = (page-1)*30;
		return String.format(cmtFormat, thread, offset);
	}
}
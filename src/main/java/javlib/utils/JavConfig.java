package javlib.utils;

public class JavConfig {
	public static final String domain = "http://j15av.com/cn";
	public static final String indexStarsFt = JavConfig.domain + "/star_list.php?prefix=%s&page=%d";
	public static final String starMoviesFt = JavConfig.domain + "/vl_star.php?&mode=2&s=%s&page=%d";// mode为2所有影片,1是有评论的
	public static final String mvCommentsFt = JavConfig.domain + "/videocomments.php?v=%s&mode=2"; // %s为影片url核心,如javlicrf3q
}

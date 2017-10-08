package javlib;

import java.io.IOException;

import org.jsoup.Jsoup;

import common.spider.HttpClientUtil;
import javlib.service.MovieDetailImpl;

public class JavServiceTest {
	public static void main(String[] args) throws IOException {
		String url = "http://j15av.com/cn/?v=javlipiwya";
		url = "http://j15av.com/cn/?v=javlikan3m";
		new MovieDetailImpl().getMovieDetail(Jsoup.parse(HttpClientUtil.getWebPage(url)));
	}
}

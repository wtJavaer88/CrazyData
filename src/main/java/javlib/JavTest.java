package javlib;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;

import db.DBconnectionMgr;
import db.DbExecMgr;
import javlib.task.MovieDetailTask;
import javlib.task.StarIndexTask;
import javlib.task.StarMoviesTask;
import javlib.utils.JavConfig;

public class JavTest {
	public static JStarQueue jStarQueue = new JStarQueue("JAV_STAR");
	public static JMovieQueue jMovieQueue = new JMovieQueue("JAV_MOVIE");

	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\javlib.db");
		DBconnectionMgr.getConnection();
		// ProxyPool.parseWithFile("daili.txt");
		// ProxyHttpClient.getInstance().startCrawl();
		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {
				// starMoviesTest();
				// starIndexTest();
				// startMvDetailTest();
			}
		});

		// starIndexTest();
	}

	public static void startMvDetailTest() {
		// 将SINGLE_STAR字段作为是否更新标记
		Map starMap = DbExecMgr.getSelectAllSqlMap("SELECT URL,MOVIE_CODE FROM JAV_MOVIE WHERE SINGLE_STAR IS NULL");
		System.out.println("starMap.size():" + starMap.size());
		Map fieldMap;
		String file;
		String url;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			url = JavConfig.domain + fieldMap.get("URL").toString();
			file = "F:/资源/爬虫/javlib/" + fieldMap.get("MOVIE_CODE").toString() + ".json";
			if (!BasicFileUtil.isExistFile(file)) {
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MovieDetailTask(url));
			}
		}
	}

	public static void starMoviesTest() {
		Map starMap = DbExecMgr.getSelectAllSqlMap("SELECT STAR_CODE,MV_PAGES FROM JAV_STAR");
		Map fieldMap;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			String starCode = fieldMap.get("STAR_CODE").toString();
			int pages = BasicNumberUtil.getNumber(fieldMap.get("MV_PAGES") + "");
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new StarMoviesTask(starCode, 1, pages));
		}
	}

	public static void starIndexTest() {
		// jStarQueue.startQueue();
		for (char i = 97; i < 123; i++) {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new StarIndexTask(i, 1));
		}
	}
}

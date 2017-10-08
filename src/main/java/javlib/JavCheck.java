package javlib;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import db.DBconnectionMgr;
import db.DbExecMgr;
import javlib.task.MovieDetailTask;
import javlib.task.StarIndexTask;
import javlib.task.StarMoviesTask;
import javlib.utils.JavConfig;
import javlib.utils.SpiderLogMgr;

public class JavCheck {

	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\javlib.db");
		DBconnectionMgr.getConnection();
		// ProxyPool.parseWithFile("daili.txt");
		// ProxyHttpClient.getInstance().startCrawl();
		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {
				// starIndexCheck();
				// starMoviesTest();
				startMvDetailTest();
			}
		});

		// starIndexTest();
	}

	public static void startMvDetailTest() {
		// 将SINGLE_STAR字段作为是否更新标记
		Map starMap = DbExecMgr.getSelectAllSqlMap("SELECT DISTINCT URL FROM JAV_MOVIE WHERE SINGLE_STAR IS NULL");
		Map fieldMap;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			String url = JavConfig.domain + fieldMap.get("URL").toString();
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MovieDetailTask(url));
		}
	}

	public static void starMoviesTest() {
		Map starMap = DbExecMgr.getSelectAllSqlMap("SELECT  STAR_CODE,MV_PAGES FROM JAV_STAR where mv_pages is null");
		Map fieldMap;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			String starCode = fieldMap.get("STAR_CODE").toString();
			int pages = BasicNumberUtil.getNumber(fieldMap.get("MV_PAGES").toString());
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new StarMoviesTask(starCode, 1, pages));
		}
	}

	/**
	 * 按照拼音索引获取更新明星
	 */
	public static void starIndexCheck() {
		for (String url : SpiderLogMgr.retryUrls) {
			char head = PatternUtil.getFirstPatternGroup(url, "prefix=(\\S)").charAt(0);
			String page = PatternUtil.getLastPatternGroup(url, "(\\d+)");
			SpiderHttpClient.getInstance().getNetPageThreadPool()
					.execute(new StarIndexTask(head, BasicNumberUtil.getNumber(page)));
		}
	}
}

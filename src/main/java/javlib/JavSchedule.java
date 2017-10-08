package javlib;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderSchedule;

import db.DBconnectionMgr;

public class JavSchedule extends SpiderSchedule {
	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\javlib.db");
		DBconnectionMgr.getConnection();
		ProxyHttpClient.initProxy();

		JavSchedule javSchedule = new JavSchedule();
		javSchedule.default_wait_size = 0;

		javSchedule.addAction(new MySpiderAction() {
			public void execute() {
				// JavTest.starIndexTest();
			}
		}).addAction(new MySpiderAction() {
			public void execute() {
				// JavTest.starMoviesTest();
			}
		}).addAction(new MySpiderAction() {
			public void execute() {
				JavTest.startMvDetailTest();
			}
		}).startSchedule();

		JavMovieJsonParse.checkFromLocal();
	}

}

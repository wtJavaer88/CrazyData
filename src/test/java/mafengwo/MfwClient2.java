package mafengwo;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.SpiderHttpClient;

import db.DBconnectionMgr;
import mafengwo.action.MfwNoteRetryAction;
import mafengwo.task.MfwUserTask;

public class MfwClient2 {
	public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

	public static void main(String[] args) throws Exception {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//mafengwo.db");
		doNoteTask();
		// doUsersTask();
	}

	private static void doNoteTask() {
		ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new MfwNoteRetryAction());
	}

	private static void doUsersTask() {
		try {
			TestLogin.login();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.execute(new MfwUserTask(237507));
	}
}

package mafengwo;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import db.DBconnectionMgr;
import mafengwo.task.MfwUserTask;

public class MfwClient {
	public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

	public static void main(String[] args) throws Exception {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//mafengwo.db");
		// ProxyHttpClient.initProxy();
		//
		// SpiderHttpClient.getInstance().startCrawl(new MfwUserAction());
		doNoteTask();
		// doUsersTask();
	}

	private static void doNoteTask() {
		// TODO Auto-generated method stub

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

package mafengwo;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.SpiderHttpClient;

import db.DBconnectionMgr;
import mafengwo.action.MfwNoteDownloadAction;

public class MfwClient3 {
	public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

	public static void main(String[] args) throws Exception {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D://database//mafengwo.db");
		downloadNotes();
	}

	private static void downloadNotes() {
		ProxyHttpClient.initProxy();
		SpiderHttpClient.getInstance().startCrawl(new MfwNoteDownloadAction());
	}

}

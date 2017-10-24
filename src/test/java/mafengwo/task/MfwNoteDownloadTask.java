package mafengwo.task;

import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

import javlib.utils.RetryMgr;

public class MfwNoteDownloadTask extends AbstractPageTask {
	private static final String NOTE_ERR_TXT = "F:/资源/爬虫/马蜂窝/note/note-download-err.txt";
	private String nid;

	public MfwNoteDownloadTask(String url) {
		this.url = url;
		this.nid = PatternUtil.getLastPattern(url, "\\d+");
		this.proxyFlag = true;
	}

	@Override
	protected void retry() {
		boolean addUrlAndStop = RetryMgr.addUrlAndStop(url, 10);
		if (!addUrlAndStop) {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MfwNoteDownloadTask(url));
		} else {
			RetryMgr.logTimeout(url);
		}
	}

	@Override
	protected void handle(Page page) throws Exception {
		try {
			String notefile = "F:/资源/爬虫/马蜂窝/note/best/" + this.nid + ".html";
			if (!BasicFileUtil.isExistFile(notefile)) {
				BasicFileUtil.writeFileString(notefile, page.getHtml() + "\r\n", null, false);
			}
			SpiderHttpClient.parseCount.getAndIncrement();
		} catch (Exception e) {
			BasicFileUtil.writeFileString(NOTE_ERR_TXT, url + "/" + e.getMessage() + "\r\n", null, true);
		}
	}

}

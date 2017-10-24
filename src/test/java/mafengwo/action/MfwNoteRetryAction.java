package mafengwo.action;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.tools.FileOp;

import javlib.utils.SpiderLogMgr;
import mafengwo.task.MfwNoteTask;

public class MfwNoteRetryAction implements MySpiderAction {

	public void execute() {

		for (String url : FileOp.readFrom(spiderqueue.util.QueueConfig.logPath + "retrylog-20171020-0.txt")) {
			if (!SpiderLogMgr.isExist(MfwNoteTask.NOTEALL_TXT, url)) {
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MfwNoteTask(url));
			}
		}
	}

}

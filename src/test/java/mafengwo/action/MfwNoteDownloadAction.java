package mafengwo.action;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import db.DbExecMgr;
import mafengwo.task.MfwNoteDownloadTask;

public class MfwNoteDownloadAction implements MySpiderAction {

	public void execute() {

		String url = "";
		Map starMap = DbExecMgr
				.getSelectAllSqlMap("SELECT ID,URL FROM note ORDER BY STARS desc,UPVOTES desc LIMIT 0,10004");
		Map fieldMap;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			if (fieldMap.get("URL") == null) {
				continue;
			}
			url = "http://www.mafengwo.cn" + fieldMap.get("URL").toString();
			if (BasicFileUtil
					.isExistFile("F:/资源/爬虫/马蜂窝/note/best/" + PatternUtil.getLastPattern(url, "\\d+") + ".html")) {
				continue;
			}
			System.out.println("not Exist:" + PatternUtil.getLastPattern(url, "\\d+"));
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MfwNoteDownloadTask(url));
		}
	}

	public void execute1() {
		for (String id : FileOp.readFrom("F:/资源/爬虫/马蜂窝/note/retry.txt")) {
			id = PatternUtil.getLastPattern(id, "\\d+");
			SpiderHttpClient.getInstance().getNetPageThreadPool()
					.execute(new MfwNoteDownloadTask("http://www.mafengwo.cn/i/" + id + ".html"));
		}
	}

}

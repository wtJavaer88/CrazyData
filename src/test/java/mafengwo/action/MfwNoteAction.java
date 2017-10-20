package mafengwo.action;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicNumberUtil;

import db.DbExecMgr;
import mafengwo.task.MfwNoteTask;

public class MfwNoteAction implements MySpiderAction {

	public void execute() {
		int uid = 79568445;
		uid = 40036836;
		uid = 67936657;
		Map starMap = DbExecMgr.getSelectAllSqlMap("SELECT ID FROM USER WHERE NOTES > 0");
		Map fieldMap;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			SpiderHttpClient.getInstance().getNetPageThreadPool()
					.execute(new MfwNoteTask(BasicNumberUtil.getNumber(fieldMap.get("ID").toString()), 1));
		}
	}

}

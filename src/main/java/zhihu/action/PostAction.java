package zhihu.action;

import java.util.Map;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;

import db.DbExecMgr;
import zhihu.task.UserPostListTask;

public class PostAction implements MySpiderAction {

	public void execute() {
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(
				"select user_token,url from user where posts >= 10 and thanks > 10*posts order  by followers desc, posts desc");
		Map fieldMap = null;
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			fieldMap = (Map) selectAllSqlMap.get(i);
			String token = fieldMap.get("USER_TOKEN").toString();
			BasicFileUtil.makeDirectory("F:/资源/爬虫/知乎/Post/" + token);
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new UserPostListTask(token, 1));
		}
	}

}

package zhihu.action;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import zhihu.task.UserPostListTask;

public class PostRetryAction implements MySpiderAction {

	public void execute() {
		String token = "";
		int page = 1;
		for (String s : FileOp.readFrom(spiderqueue.util.QueueConfig.logPath + "retrylog-20171011-1.txt")) {
			token = PatternUtil.getFirstPatternGroup(s, "members/(.*?)/articles");
			page = BasicNumberUtil.getNumber(PatternUtil.getLastPatternGroup(s, "offset=(\\d+)&")) / 20 + 1;
			System.out.println(token + " " + page);
			BasicFileUtil.makeDirectory("F:/资源/爬虫/知乎/Post/" + token);
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new UserPostListTask(token, page));
		}
	}

}

package zhihu.task;

import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.methods.HttpGet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import javlib.utils.RetryMgr;
import utils.ReflectUtil;
import zhihu.entity.Post;
import zhihu.util.ZhihuAuthUtil;

public class UserPostListTask extends AbstractPageTask {
	final static String BASE_URL = "https://www.zhihu.com/api/v4/members/{UUU}/articles?include=data%5B*%5D.comment_count%2Ccan_comment%2Ccomment_permission%2Cadmin_closed_comment%2Ccontent%2Cvoteup_count%2Ccreated%2Cupdated%2Cupvoted_followees%2Cvoting%2Creview_info%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset={PPP}&limit=20&sort_by=created";

	String token;
	int page_index;

	public UserPostListTask(String token, int page_index) {
		this.token = token;
		this.page_index = page_index;
		this.proxyFlag = true;
		request = new HttpGet(getApiUrl());
		request.setHeader("authorization", "oauth " + ZhihuAuthUtil.getAuthorization());
	}

	private String getApiUrl() {
		return BASE_URL.replace("{UUU}", token).replace("{PPP}", "" + (page_index - 1) * 20);
	}

	@Override
	protected void retry() {
		boolean addUrlAndStop = RetryMgr.addUrlAndStop(url, 1000);
		if (!addUrlAndStop) {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new UserPostListTask(token, page_index));
		} else {
			RetryMgr.logTimeout(getApiUrl());
		}
	}

	@Override
	protected void handle(Page page) throws Exception {
		SpiderHttpClient.parseCount.getAndIncrement();
		JSONObject parseObject = JSONObject.parseObject(page.getHtml());
		if (page_index == 1) {
			int total = parseObject.getJSONObject("paging").getIntValue("totals");
			int pages = (int) Math.ceil(1.0 * total / 20);
			System.out.println("This is first page!");
			System.out.println("总页数:" + pages);
			BasicFileUtil.writeFileString("post-pages.txt", token + " " + total + " " + pages + "\r\n", null, true);
			if (pages > 1) {
				for (int p = 2; p <= pages; p++) {
					SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new UserPostListTask(token, p));
				}
			}
		}
		System.out.println("User:" + token + " page:" + page_index);
		JSONArray jsonArray = parseObject.getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			Post post = new Post();
			post.setAuthor(token);
			ReflectUtil.setObjAllValue(jsonArray.getJSONObject(i), post, Arrays.asList("author"));
			// System.out.println(post);
			String paramString1 = "F:/资源/爬虫/知乎/Post/" + token + "/" + post.getId() + ".html";
			if (!BasicFileUtil.isExistFile(paramString1)) {
				insertPost(post);
				BasicFileUtil.writeFileString(paramString1, post.getContent(), null, false);
			}
		}
		BasicFileUtil.writeFileString("seeked-post.txt", getApiUrl() + "\r\n", null, true);

	}

	private void insertPost(Post post) {
		if (DbExecMgr.isExistData("post", "id", post.getId() + "")) {
			return;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("POST");
		util.addInsertField(new DbField("ID", "" + post.getId()));
		util.addInsertField(new DbField("TITLE", StringEscapeUtils.escapeSql(post.getTitle())));
		util.addInsertField(new DbField("URL", post.getUrl()));
		util.addInsertField(new DbField("author", post.getAuthor()));
		util.addInsertField(new DbField("image_url", post.getImage_url()));
		util.addInsertField(new DbField("type", post.getType()));
		util.addInsertField(new DbField("excerpt", StringEscapeUtils.escapeSql(post.getExcerpt())));
		util.addInsertField(new DbField("updated", "" + post.getUpdated()));
		util.addInsertField(new DbField("created", "" + post.getCreated()));
		util.addInsertField(new DbField("voteup_count", "" + post.getVoteup_count()));
		util.addInsertField(new DbField("comment_count", "" + post.getComment_count()));
		util.addInsertField(new DbField("CREATE_TIME", BasicDateUtil.getCurrentDateTimeString()));
		try {
			DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
		} catch (SQLException e) {
			BasicFileUtil.writeFileString("F:/资源/爬虫/知乎/Post/dberr.txt",
					e.getMessage() + getApiUrl() + " " + post.getTitle() + "\r\n", null, true);
		}
	}

}
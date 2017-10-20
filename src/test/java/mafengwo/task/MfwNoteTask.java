package mafengwo.task;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import javlib.utils.RetryMgr;
import javlib.utils.SpiderLogMgr;
import mafengwo.entity.Note;
import mafengwo.parse.ArticleListParser;

public class MfwNoteTask extends AbstractPageTask {
	private static final String NOTEALL_TXT = "F:/资源/爬虫/马蜂窝/note/noteall.txt";
	private static final String NOTE_ERR_TXT = "F:/资源/爬虫/马蜂窝/note/note-err.txt";
	int uid;
	int pageIndex;

	public MfwNoteTask(String url) {
		this.url = url;
		this.pageIndex = BasicNumberUtil.getNumber(PatternUtil.getFirstPattern(url, "\\d+"));
		this.uid = BasicNumberUtil.getNumber(PatternUtil.getLastPattern(url, "\\d+"));
		// this.proxyFlag = true;
	}

	public MfwNoteTask(int uid, int pageIndex) {
		this.uid = uid;
		this.pageIndex = pageIndex;
		this.url = "http://www.mafengwo.cn/wo/ajax_post.php?sAction=getArticle&iPage=" + pageIndex + "&iUid=" + uid;
		// this.proxyFlag = true;
	}

	@Override
	protected void retry() {
		boolean addUrlAndStop = RetryMgr.addUrlAndStop(url, 10);
		if (!addUrlAndStop) {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MfwNoteTask(url));
		} else {
			RetryMgr.logTimeout(url);
		}
	}

	@Override
	protected void handle(Page page) throws Exception {
		try {
			JSONObject json = getJson(page);
			ArticleListParser articleListParser = new ArticleListParser();
			if (pageIndex == 1) {
				int pages = articleListParser.getMaxPage(Jsoup.parse(json.getJSONObject("payload").getString("pager")));
				for (int i = 2; i <= pages; i++) {
					String url2 = "http://www.mafengwo.cn/wo/ajax_post.php?sAction=getArticle&iPage=" + i + "&iUid="
							+ uid;
					if (!SpiderLogMgr.isExist(NOTEALL_TXT, url2)) {
						SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new MfwNoteTask(url2));
					} else {
						System.out.println("seeked...");
					}
				}
			}
			if (!SpiderLogMgr.isExist(NOTEALL_TXT, url)) {
				parseNotes(articleListParser, json);
				BasicFileUtil.writeFileString(NOTEALL_TXT, url + "\r\n", null, true);
			}
			SpiderHttpClient.parseCount.getAndIncrement();
		} catch (Exception e) {
			BasicFileUtil.writeFileString(NOTE_ERR_TXT, uid + "/" + url + "/" + e.getMessage() + "\r\n", null, true);
		}
	}

	private void parseNotes(ArticleListParser articleListParser, JSONObject json) {
		List<Note> parseList = articleListParser
				.parseList(Jsoup.parse(json.getJSONObject("payload").getString("html")));
		for (Note note : parseList) {
			note.setUid(uid);
			DbFieldSqlUtil util = new DbFieldSqlUtil("NOTE");
			util.addInsertField(new DbField("id", note.getId() + ""));
			util.addInsertField(new DbField("uid", note.getUid() + ""));

			util.addInsertField(new DbField("upvotes", note.getUpvotes() + ""));
			util.addInsertField(new DbField("type", escape(note.getType())));
			util.addInsertField(new DbField("url", note.getUrl()));
			util.addInsertField(new DbField("title", escape(note.getTitle())));
			util.addInsertField(new DbField("thumnail", escape(note.getThunmnail())));
			util.addInsertField(new DbField("thumb_description", escape(note.getThumbDescription())));
			util.addInsertField(new DbField("views", note.getViews() + ""));
			util.addInsertField(new DbField("comments", note.getComments() + ""));
			util.addInsertField(new DbField("stars", note.getStars() + ""));
			util.addInsertField(new DbField("note_time", note.getNoteTime()));
			util.addInsertField(new DbField("exerpt", escape(note.getExerpt())));
			util.addInsertField(new DbField("create_time", note.getCreateTime()));
			try {
				if (!DbExecMgr.isExistData("note", "id", "" + note.getId())) {
					DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
				}
			} catch (SQLException e) {
				BasicFileUtil.writeFileString(NOTE_ERR_TXT,
						note.getUid() + "/" + pageIndex + "/" + note.getTitle() + "/" + e.getMessage() + "\r\n", null,
						true);
			}
		}
	}

	private String escape(String str) {
		return StringEscapeUtils.escapeSql(str);
	}

}

package mafengwo.task;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.wnc.basic.BasicFileUtil;

import common.spider.HttpClientUtil;
import db.DbExecMgr;
import mafengwo.MfwClient;
import mafengwo.parse.MfwUserListParser;
import mafengwo.user.MfwUser;

public class MfwUserTask implements Runnable {
	String url;
	boolean proxyFlag = true;

	public MfwUserTask(String url) {
		this.url = url;
	}

	public MfwUserTask(int uid) {
		this.url = "http://www.mafengwo.cn/friend/index/follow?flag=0&name=&uid=" + uid + "&page=1";
	}

	public void run() {
		Document doc;
		try {
			doc = Jsoup.parse(HttpClientUtil.getWebPage(url));

			List<MfwUser> parseList = new MfwUserListParser().parseList(doc);
			for (MfwUser u : parseList) {
				boolean existData = DbExecMgr.isExistData("USER", "ID", u.getId() + "");
				if (!existData) {
					String sql = "";
					try {
						sql = "INSERT INTO USER(ID,NAME,URL,NOTES,PATHS,FANS) VALUES(" + u.getId() + ",'"
								+ StringEscapeUtils.escapeSql(u.getName()) + "','" + u.getUrl() + "'," + u.getNotes()
								+ "," + u.getPaths() + "," + u.getFans() + ")";
						System.out.println(sql);
						DbExecMgr.execOnlyOneUpdate(sql);
						BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/users/user-success.txt", u.toString() + "\r\n",
								null, true);
						MfwClient.executor.execute(new MfwUserTask(u.getId()));
					} catch (Exception e) {
						BasicFileUtil.writeFileString("F:/资源/爬虫/马蜂窝/users/user-err.txt",
								u.getId() + "/" + e.getMessage() + "/" + sql + "\r\n", null, true);
						// e.printStackTrace();
					}

				}
			}
		} catch (IOException e1) {
		}
	}

}

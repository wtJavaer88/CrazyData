package netease.music.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;

import common.spider.HttpClientUtil;
import db.DbExecMgr;
import netease.music.dao.HotCommentQueue;
import netease.music.dao.NetEaseDao;
import netease.music.entity.Comment;
import netease.music.util.Util;

public class HotCommentsAction implements MySpiderAction {
	HotCommentQueue hotCommentQueue;

	public void execute() {
		hotCommentQueue = new HotCommentQueue("comment");
		hotCommentQueue.startQueue();

		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap("select ID from song where song.comment_count is null");
		System.out.println(selectAllSqlMap.size());
		int j = 0;
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			Map fieldMap = (Map) selectAllSqlMap.get(i);
			int id = Integer.parseInt(fieldMap.get("ID").toString());

			if (Util.notSeeked(id)) {
				j++;
				// BasicFileUtil.writeFileString("netease-log.txt", "开始热门评论:" +
				// id + "\r\n", null, true);
				String url = "http://music.163.com//weapi/v1/resource/comments/R_SO_4_" + id + "?csrf_token=";
				SpiderHttpClient.getInstance().getNetPageThreadPool()
						.execute(new HotCommentsTask(getHttpPost(url), id, true));
				// if(j > 0 && j%166==0){
				// try {
				// Thread.sleep(60*1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
			}

		}

	}

	static Map<String, String> headers = new HashMap<String, String>();
	static String encSecKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
	static Map<String, String> data = new HashMap<String, String>();
	static String params = "{rid:'', offset:'0', total:'true', limit:'20', csrf_token:''}";
	static {
		data.put("params", Util.getEncryParams(params));
		data.put("encSecKey", encSecKey);
		headers.put("Accept", "*/*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Host", "music.163.com");
		headers.put("Origin", "http://music.163.com");
		headers.put("Referer", "http://music.163.com/");
		headers.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
	}

	private static HttpPost getHttpPost(String postUrl) {
		HttpPost post = new HttpPost(postUrl);
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			post.addHeader(entry.getKey(), entry.getValue());
		}
		HttpClientUtil.setHttpPostParams(post, data);
		return post;
	}

	class HotCommentsTask extends AbstractPageTask {
		int songId;

		public HotCommentsTask(HttpRequestBase request, int sid, boolean b) {
			super(request, b);
			songId = sid;
		}

		@Override
		protected void retry() {
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new HotCommentsTask(request, songId, true));
		}

		@Override
		protected void handle(Page page) throws Exception {
			try {
				Map<String, Object> map = JSONObject.parseObject(page.getHtml(),
						new TypeReference<Map<String, Object>>() {
						}.getType());
				List<Object> comments = (List<Object>) map.get("hotComments");
				int total = BasicNumberUtil.getNumber(map.get("total").toString());
				NetEaseDao.updateSong(songId, total);
				List<Comment> list = new ArrayList<Comment>();
				Comment com;
				BigDecimal bd;
				for (Object o : comments) {
					Map<String, Object> tmp = (Map<String, Object>) o;
					com = new Comment();
					com.setSongId(songId);
					com.setId(BasicNumberUtil.getNumber(tmp.get("commentId").toString()));
					com.setContent(tmp.get("content").toString());
					com.setUpvote(tmp.get("likedCount").toString());
					bd = new BigDecimal(tmp.get("time").toString());
					com.setTime(bd.toPlainString());
					bd = new BigDecimal(((Map<String, Object>) tmp.get("user")).get("userId").toString());
					if (bd.toPlainString().contains(".")) {
						com.setUserId(bd.toPlainString().substring(0, bd.toPlainString().length() - 2));
					} else {
						com.setUserId(bd.toPlainString());
					}
					list.add(com);
					// NetEaseDao.insertComment(com);
					hotCommentQueue.addEntity(com);
				}
				if (!list.isEmpty()) {
					System.out.println("----> 获取" + songId + "的热门评论成功!");
				} else {
					System.out.println("----> 获取" + songId + "的热门评论为空!");
				}

				SpiderHttpClient.parseCount.getAndIncrement();
				BasicFileUtil.writeFileString("seeked-log.txt", "完成热门评论:" + songId + "\r\n", null, true);
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("err-log.txt", "失败热门评论:" + songId + "\r\n", null, true);
			}
		}

	}
}

package netease.music;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.crawl.proxy.ProxyHttpClient;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.tools.SoupUtil;

import common.spider.node.MyDocument;
import common.spider.node.MyElement;
import db.DBconnectionMgr;
import db.DbExecMgr;
import netease.music.dao.NetEaseDao;
import netease.music.entity.Album;
import netease.music.entity.Artist;
import netease.music.entity.Catalog;
import netease.music.entity.Comment;
import netease.music.entity.Song;
import netease.music.util.Util;

public class NetEaseMusictest {
	public static void main(String[] args) throws IOException {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\netease.db");
		// testSpider();
		// getCatalog();
		// getArtists(4001);
		// getArtistsAll();
		getHotSongsAll();
		// getHotComments(418603077);
	}

	private static void getArtistsAll() {
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap("SELECT ID FROM CATALOG WHERE ID >= 7001");
		System.out.println(selectAllSqlMap.size());
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			Map fieldMap = (Map) selectAllSqlMap.get(i);
			int id = Integer.parseInt(fieldMap.get("ID").toString());
			System.out.println(id);
			BasicFileUtil.writeFileString("log.txt", "开始分类歌手:" + id + "\r\n", null, true);
			getArtists(id);
		}
	}

	private static void getHotSongsAll() {
		Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap("SELECT ID FROM ARTIST");
		System.out.println(selectAllSqlMap.size());
		boolean flag = false;
		for (int i = 1; i <= selectAllSqlMap.size(); i++) {
			Map fieldMap = (Map) selectAllSqlMap.get(i);
			int id = Integer.parseInt(fieldMap.get("ID").toString());
			if (!flag && id == 4693) {
				flag = true;
			}
			if (flag) {
				BasicFileUtil.writeFileString("log.txt", "开始热门歌曲:" + id + "\r\n", null, true);
				getHotSongs(id);
			}
		}
	}

	private static void getHotComments(int songId) throws IOException {
		String url = "http://music.163.com//weapi/v1/resource/comments/R_SO_4_" + songId + "?csrf_token=";
		String params = "{rid:'', offset:'0', total:'true', limit:'20', csrf_token:''}";
		String postRequest = Util.getJson(params, url);
		Map<String, Object> map = JSONObject.parseObject(postRequest, new TypeReference<Map<String, Object>>() {
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
			try {
				NetEaseDao.insertComment(com);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!list.isEmpty()) {
			System.out.println("----> 获取" + songId + "的热门评论成功!");
		} else {
			System.out.println("----> 获取" + songId + "的热门评论失败!");
		}
	}

	private static void getHotSongs(int artistId) {
		// http://music.163.com/#/artist?id=6452
		String url = "http://music.163.com/artist?id=" + artistId;
		try {
			Document doc = SoupUtil.getDoc(url);
			Element nextElementSibling = doc.select(".f-hide").first().nextElementSibling();
			String escapeHtml = StringEscapeUtils.unescapeHtml(nextElementSibling.text());
			// System.out.println(escapeHtml);
			JSONArray parseArray = JSONObject.parseArray(escapeHtml);
			// System.out.println(JSONObject.toJSONString(parseArray,
			// SerializerFeature.PrettyFormat));
			for (int i = 0, j = parseArray.size(); i < j; i++) {
				Song song = new Song();
				JSONObject jsonObject = parseArray.getJSONObject(i);
				song.setId(jsonObject.getLong("id"));
				song.setName(jsonObject.getString("name"));
				song.setNo(jsonObject.getInteger("no"));// 在专辑中的序号, 从1开始
				song.setMvid(jsonObject.getLong("mvid"));
				song.setScore(jsonObject.getDouble("score"));
				song.setDuration(jsonObject.getInteger("duration"));
				JSONObject albumObj = jsonObject.getJSONObject("album");
				Album album = new Album();
				album.setId(albumObj.getLong("id"));
				album.setName(albumObj.getString("name"));
				album.setPicUrl(albumObj.getString("picUrl"));
				song.setAlbum(album);

				NetEaseDao.insertSong(song);

			}
		} catch (SQLException e) {

		} catch (Exception e) {
			BasicFileUtil.writeFileString("errors.log", artistId + " " + e.getMessage() + "\r\n", null, true);
		}
	}

	private static void getArtists(int catId) {
		int s = 65;
		if (catId == 7001) {
			s = 79;
		}
		for (int i = s; i <= 91; i++) {
			int initial = i == 91 ? 0 : i;
			String url = "http://music.163.com/discover/artist/cat?id=" + catId + "&initial=" + initial;
			try {
				Document doc = SoupUtil.getDoc(url);
				Elements select = doc.select(".nm.nm-icn");

				for (Element element : select) {
					MyElement me = new MyElement(element);
					Artist artist = new Artist();
					artist.setCatId(catId);
					artist.setId(me.number(me.pattern4Attr("href", "(\\d+)$")));
					artist.setName(me.text());
					artist.setInitial(initial);
					NetEaseDao.insertArtist(artist);
				}
				System.out.println(((char) i) + "歌手总数:" + select.size());
			} catch (SQLException e) {

			} catch (Exception e) {
				// 相当于重试
				i--;
			}

		}
	}

	private static void getCatalog() {
		Document doc = SoupUtil.getDoc("http://music.163.com/discover/artist/");
		MyDocument mydoc = new MyDocument(doc);
		Elements select = mydoc.select(".cat-flag");
		for (Element e : select) {
			System.out.println(e.absUrl("href"));
			MyElement me = new MyElement(e);
			Catalog catalog = new Catalog();
			catalog.setId(me.number(me.pattern4Attr("href", "(\\d+)$")));
			catalog.setName(me.text());
			try {
				NetEaseDao.insertCatalog(catalog);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void testSpider() {
		ProxyHttpClient.getInstance().startCrawl();
		SpiderHttpClient.getInstance().startCrawl(new MySpiderAction() {
			public void execute() {

				Integer movies[] = { 26387939, 1291543, 1291571, 1291572, 1291828 };
				for (int id : movies) {
					AbstractPageTask task = new AbstractPageTask("https://movie.douban.com/subject/" + id, true) {
						@Override
						protected void retry() {

						}

						@Override
						protected void handle(Page page) throws Exception {
							System.out.println("getStatusCode:" + page.getStatusCode());
						}
					};
					SpiderHttpClient.getInstance().getNetPageThreadPool().execute(task);
				}
			}
		});
	}
}

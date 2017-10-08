package netease.music.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

import javlib.utils.RetryMgr;
import javlib.utils.SpiderLogMgr;
import netease.music.dao.NetEaseDao;
import netease.music.entity.Album;
import netease.music.entity.Song;

public class HotSongsAction implements MySpiderAction {

	public void execute() {
		List<Map<String, String>> selectAllSqlMap = NetEaseDao.dbExecMgr4Lites
				.getSelectAllList("SELECT ID FROM ARTIST");
		System.out.println(selectAllSqlMap.size());
		boolean flag = false;
		for (int i = 0; i < selectAllSqlMap.size(); i++) {
			Map fieldMap = selectAllSqlMap.get(i);
			int id = Integer.parseInt(fieldMap.get("id").toString());
			BasicFileUtil.writeFileString("netease-log.txt", "开始热门歌曲:" + id + "\r\n", null, true);
			String url = "http://music.163.com/artist?id=" + id;
			if (!SpiderLogMgr.isExist(id + ""))
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new HotSongTask(url, true));
			else
				System.out.println("Get..." + id);
		}

	}

	class HotSongTask extends AbstractPageTask {
		String artistid;

		public HotSongTask(String url, boolean b) {
			super(url, b);
			artistid = PatternUtil.getLastPattern(url, "\\d+");
		}

		@Override
		protected void retry() {
			boolean addUrlAndStop = RetryMgr.addUrlAndStop(url);
			if (!addUrlAndStop)
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new HotSongTask(url, true));
		}

		@Override
		protected void handle(Page page) throws Exception {
			try {
				Document doc = Jsoup.parse(page.getHtml());
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
					if (!NetEaseDao.exist("song", song.getId())) {
						NetEaseDao.insertSong(song);
					}
					NetEaseDao.insertSongArtist(song.getId() + "", artistid);
				}
				SpiderHttpClient.parseCount.getAndIncrement();
				BasicFileUtil.writeFileString("seeked-log.txt", "完成热门歌曲:" + artistid + "\r\n", null, true);
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("err-log.txt", "失败热门歌曲:" + artistid + "\r\n", null, true);
			}
		}

	}
}

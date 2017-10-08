package netease.music.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.spider.MySpiderAction;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;

import common.spider.node.MyElement;
import javlib.utils.RetryMgr;
import netease.music.dao.NetEaseDao;
import netease.music.entity.Song;

public class SongArtistsAction implements MySpiderAction {

	public void execute() {
		try {
			List<Song> entityList = NetEaseDao.dbExecMgr4Lites.getEntityList(
					"SELECT ID songId FROM SONG WHERE SONG.ID NOT IN (SELECT SONG_ID FROM SONG_ARTISTS) order by comment_count",
					Song.class);
			for (Song song : entityList) {
				SpiderHttpClient.getInstance().getNetPageThreadPool()
						.execute(new SongArtistTask(song.getSongId(), true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SongArtistTask extends AbstractPageTask {
		String songId;

		public SongArtistTask(String songId, boolean b) {
			this.songId = songId;
			this.proxyFlag = b;
			this.url = "http://music.163.com/song?id=" + songId;
		}

		@Override
		protected void retry() {
			boolean addUrlAndStop = RetryMgr.addUrlAndStop(url);
			if (!addUrlAndStop) {
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new SongArtistTask(songId, true));
			} else {
				RetryMgr.logTimeout(url);
			}
		}

		@Override
		protected void handle(Page page) throws Exception {
			try {
				Document doc = Jsoup.parse(page.getHtml());
				Elements artistLink = doc.select("p.des.s-fc4 span a");
				for (Element element : artistLink) {
					String artistId = new MyElement(element).pattern4Attr("href", "id=(\\d+)");
					if (StringUtils.isNotBlank(artistId))
						NetEaseDao.insertSongArtist(songId, artistId);
				}
				SpiderHttpClient.parseCount.getAndIncrement();
			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("err-log.txt", "失败歌曲:" + songId + "\r\n", null, true);
			}
		}

	}
}

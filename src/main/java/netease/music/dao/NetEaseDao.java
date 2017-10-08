package netease.music.dao;

import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;

import com.wnc.basic.BasicFileUtil;

import db.DbExecMgr4Lites;
import db.DbField;
import db.DbFieldSqlUtil;
import netease.music.entity.Album;
import netease.music.entity.Artist;
import netease.music.entity.Catalog;
import netease.music.entity.Comment;
import netease.music.entity.Song;

public class NetEaseDao {
	public static DbExecMgr4Lites dbExecMgr4Lites = new DbExecMgr4Lites("D:\\database\\netease.db");

	public static boolean insertSong(Song song) throws SQLException {
		DbFieldSqlUtil util = new DbFieldSqlUtil("song", "");
		util.addInsertField(new DbField("id", song.getId() + "", "STRING"));
		util.addInsertField(new DbField("name", StringEscapeUtils.escapeSql(song.getName()), "STRING"));
		util.addInsertField(new DbField("no", song.getNo() + "", "NUMBER"));
		util.addInsertField(new DbField("mvid", song.getMvid() + "", "NUMBER"));
		util.addInsertField(new DbField("score", song.getScore() + "", "NUMBER"));
		util.addInsertField(new DbField("album_id", song.getAlbum().getId() + "", "NUMBER"));
		util.addInsertField(new DbField("duration", song.getDuration() + "", "NUMBER"));
		util.addInsertField(new DbField("drt_txt", song.getDrt_txt(), "STRING"));

		boolean flag = dbExecMgr4Lites.execUpdate(util.getInsertSql()) == 1;
		if (flag) {
			dblog("插入Song成功: " + song.getId() + " / " + song.getName());
			insertAlbum(song.getAlbum());
		} else {
			dblog("插入Song失败: " + song.getId() + " / " + song.getName());
		}
		return flag;
	}

	public static boolean updateSong(int sid, int commentCount) {
		boolean flag = false;
		try {
			flag = dbExecMgr4Lites
					.execUpdate("UPDATE SONG SET COMMENT_COUNT=" + commentCount + " WHERE ID=" + sid) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!flag) {
			dblog("更新Song失败: " + sid + " / " + commentCount);
		}
		return flag;
	}

	private static boolean insertAlbum(Album album) throws SQLException {
		if (exist("album", album.getId())) {
			return true;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("album", "");
		util.addInsertField(new DbField("id", album.getId() + "", "NUMBER"));
		util.addInsertField(new DbField("name", StringEscapeUtils.escapeSql(album.getName()), "STRING"));
		util.addInsertField(new DbField("pic_url", album.getPicUrl() + "", "STRING"));

		boolean flag = dbExecMgr4Lites.execUpdate(util.getInsertSql()) == 1;
		if (flag) {
			dblog("插入Album成功: " + album.getId() + " / " + album.getName());
		} else {
			dblog("插入Album失败: " + album.getId() + " / " + album.getName());
		}
		return flag;
	}

	public static boolean exist(String table, long id) {
		return dbExecMgr4Lites.isExistData("SELECT 1 FROM " + table + " WHERE ID=" + id);
	}

	static long time = System.currentTimeMillis();

	public static void dblog(String msg) {
		System.out.println(msg);
		BasicFileUtil.writeFileString("db-log" + time + ".txt", msg + "\r\n", null, true);
	}

	public static boolean insertCatalog(Catalog catalog) throws SQLException {
		if (exist("catalog", catalog.getId())) {
			return true;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("catalog", "");
		util.addInsertField(new DbField("id", catalog.getId() + "", "NUMBER"));
		util.addInsertField(new DbField("name", StringEscapeUtils.escapeSql(catalog.getName()), "STRING"));

		boolean flag = dbExecMgr4Lites.execUpdate(util.getInsertSql()) == 1;
		if (flag) {
			dblog("插入Catalog成功: " + catalog.getId() + " / " + catalog.getName());
		} else {
			dblog("插入Catalog失败: " + catalog.getId() + " / " + catalog.getName());
		}
		return flag;
	}

	public static boolean insertArtist(Artist artist) throws SQLException {
		if (exist("artist", artist.getId())) {
			return true;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("artist", "");
		util.addInsertField(new DbField("id", artist.getId() + "", "NUMBER"));
		util.addInsertField(new DbField("name", StringEscapeUtils.escapeSql(artist.getName()), "STRING"));
		util.addInsertField(new DbField("cat_id", artist.getCatId() + "", "NUMBER"));
		util.addInsertField(new DbField("py_idx", artist.getInitial() + "", "NUMBER"));

		boolean flag = dbExecMgr4Lites.execUpdate(util.getInsertSql()) == 1;
		if (flag) {
			dblog("插入artist成功: " + artist.getId() + " / " + artist.getName());
		} else {
			dblog("插入artist失败: " + artist.getId() + " / " + artist.getName());
		}
		return flag;
	}

	public static boolean insertComment(Comment comment) throws SQLException {
		if (exist("comment", comment.getId())) {
			return true;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("comment", "");
		util.addInsertField(new DbField("id", comment.getId() + "", "NUMBER"));
		util.addInsertField(new DbField("content", StringEscapeUtils.escapeSql(comment.getContent()), "STRING"));
		util.addInsertField(new DbField("song_id", comment.getSongId() + "", "NUMBER"));
		util.addInsertField(new DbField("user_id", comment.getUserId() + "", "STRING"));
		util.addInsertField(new DbField("upvote", comment.getUpvote() + "", "STRING"));
		util.addInsertField(new DbField("time", comment.getTime() + "", "STRING"));

		boolean flag = dbExecMgr4Lites.execUpdate(util.getInsertSql()) == 1;
		// if (!flag) {
		// dblog("插入comment失败: " + comment.getId() + " / " +
		// comment.getContent());
		// }
		// else{
		// dblog("插入comment成功: " + comment.getId() + " / " +
		// comment.getContent());
		// }
		return flag;
	}

	public static void insertSongArtist(String l, String artistId) {
		try {
			if (!dbExecMgr4Lites
					.isExistData("SELECT * FROM SONG_ARTISTS WHERE SONG_ID=" + l + " AND ARTIST_ID=" + artistId + ""))
				dbExecMgr4Lites
						.execUpdate("INSERT INTO SONG_ARTISTS(SONG_ID,ARTIST_ID) VALUES(" + l + "," + artistId + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

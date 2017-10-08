package javlib;

import java.io.File;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;

import db.DBconnectionMgr;
import db.DbExecMgr;
import javlib.dao.MvDetailDao;
import javlib.entity.JMovie;

public class JavMovieJsonParse {

	public static void main(String[] args) {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\javlib.db");
		DBconnectionMgr.getConnection();
		// checkFromDb();
		checkFromLocal();
	}

	public static void checkFromLocal() {
		JMovie jMovie;
		String folder = "F:/资源/爬虫/javlib/";
		for (File f : new File(folder).listFiles()) {
			jMovie = JSONObject.parseObject(FileOp.readFrom(f.getAbsolutePath()).get(0), JMovie.class);
			if (MvDetailDao.saveMovieDetailDb(jMovie)) {
				BasicFileUtil.deleteFile(f.getAbsolutePath());
			}
		}
	}

	private static void checkFromDb() {
		Map starMap = DbExecMgr
				.getSelectAllSqlMap("SELECT DISTINCT MOVIE_CODE FROM JAV_MOVIE WHERE SINGLE_STAR IS NULL");
		Map fieldMap;
		String mvCode;
		JMovie jMovie;
		String file;
		for (int i = 1; i <= starMap.size(); i++) {
			fieldMap = (Map) starMap.get(i);
			mvCode = fieldMap.get("MOVIE_CODE").toString();
			doInsert(mvCode);
		}
	}

	private static void doInsert(String mvCode) {
		JMovie jMovie;
		String file;
		file = "F:/资源/爬虫/javlib/" + mvCode + ".json";
		if (BasicFileUtil.isExistFile(file)) {
			jMovie = JSONObject.parseObject(FileOp.readFrom(file).get(0), JMovie.class);
			if (MvDetailDao.saveMovieDetailDb(jMovie)) {
				// System.out.println("解析总数:" + (++count));
				BasicFileUtil.deleteFile(file);
			}
		}
	}
}

package javlib;

import db.DBconnectionMgr;
import db.DbExecMgr;
import javlib.entity.JMovie;
import javlib.entity.JStar;
import spiderqueue.core.DbEntity;
import spiderqueue.core.DbQueue;

public class JMovieQueue extends DbQueue<JMovie> {

	public JMovieQueue(String tableName) {
		super(tableName);
	}

	protected boolean customOperator(DbEntity poll) throws Exception {
		return DbExecMgr.execOnlyOneUpdate(sqlFastReflect.setDbEntity(poll).setSqlSysout(false).getInsertSql("MOVIE_CODE", "URL", "TITLE", "MONO_IMG"));
	}

}

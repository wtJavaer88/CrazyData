package javlib;

import db.DBconnectionMgr;
import db.DbExecMgr;
import javlib.entity.JStar;
import spiderqueue.core.DbEntity;
import spiderqueue.core.DbQueue;

public class JStarQueue extends DbQueue<JStar> {

	public JStarQueue(String tableName) {
		super(tableName);
	}

	protected boolean customOperator(DbEntity poll) throws Exception {
		return DbExecMgr.execOnlyOneUpdate(sqlFastReflect.setDbEntity(poll).getInsertSql("HEAD", "STAR_CODE", "NAME"));
	}

}

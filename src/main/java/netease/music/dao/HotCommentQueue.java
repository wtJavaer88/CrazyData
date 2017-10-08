package netease.music.dao;

import db.DbExecMgr;
import netease.music.entity.Comment;
import spiderqueue.core.DbEntity;
import spiderqueue.core.DbQueue;

public class HotCommentQueue extends DbQueue<Comment> {

	public HotCommentQueue(String tableName) {
		super(tableName);
	}

	protected boolean customOperator(DbEntity t) throws Exception {
		return DbExecMgr.execOnlyOneUpdate(sqlFastReflect.setDbEntity(t).setSqlSysout(false).getInsertSql("id",
				"content", "song_id", "user_id", "upvote", "time"));
	}

}

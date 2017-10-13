package zhihu.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.crawl.core.util.SimpleLogger;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicStringUtil;

import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;
import zhihu.entity.User;

public class ZhiHuDao1Imp implements ZhiHuDao1 {
	private static Logger logger = SimpleLogger.getSimpleLogger(ZhiHuDao1.class);

	public static void DBTablesInit() {
		ResultSet rs = null;
		Properties p = new Properties();
		Connection cn = ConnectionManager.getConnection();
		try {
			// 加载properties文件
			p.load(ZhiHuDao1Imp.class.getResourceAsStream("/config.properties"));
			rs = cn.getMetaData().getTables(null, null, "url", null);
			Statement st = cn.createStatement();
			// 不存在url表
			if (!rs.next()) {
				// 创建url表
				st.execute(p.getProperty("createUrlTable"));
				logger.info("url表创建成功");
				st.execute(p.getProperty("createUrlIndex"));
				logger.info("url表索引创建成功");
			} else {
				logger.info("url表已存在");
			}
			rs = cn.getMetaData().getTables(null, null, "user", null);
			// 不存在user表
			if (!rs.next()) {
				// 创建user表
				st.execute(p.getProperty("createUserTable"));
				logger.info("user表创建成功");
				st.execute(p.getProperty("createUserIndex"));
				logger.info("user表索引创建成功");
			} else {
				logger.info("user表已存在");
			}
			rs.close();
			st.close();
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isExistRecord(String sql) throws SQLException {
		return isExistRecord(ConnectionManager.getConnection(), sql);
	}

	public boolean isExistRecord(Connection cn, String sql) throws SQLException {
		int num = 0;
		PreparedStatement pstmt;
		pstmt = cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			num = rs.getInt("count(*)");
		}
		rs.close();
		pstmt.close();
		if (num == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isExistUser(String userToken) {
		return DbExecMgr.isExistData("SELECT 1 FROM USER WHERE USER_TOKEN='" + userToken + "'");
	}

	public boolean isExistUser(Connection cn, String userToken) {
		String isContainSql = "select count(*) from user WHERE user_token='" + userToken + "'";
		try {
			if (isExistRecord(isContainSql)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertUser(User u) throws SQLException {
		if (isExistUser(u.getUserToken())) {
			logger.info(u.getUserToken() + "已经存在");
			updateuser(u);
			return false;
		}
		DbFieldSqlUtil util = new DbFieldSqlUtil("user", "");
		util.addInsertField(new DbField("location", StringEscapeUtils.escapeSql(u.getLocation()), "STRING"));
		util.addInsertField(new DbField("business", StringEscapeUtils.escapeSql(u.getBusiness()), "STRING"));
		util.addInsertField(new DbField("employment", StringEscapeUtils.escapeSql(u.getEmployment()), "STRING"));
		util.addInsertField(new DbField("education", StringEscapeUtils.escapeSql(u.getEducation()), "STRING"));
		util.addInsertField(new DbField("username", StringEscapeUtils.escapeSql(u.getUsername()), "STRING"));
		util.addInsertField(new DbField("sex", u.getSex(), "STRING"));
		util.addInsertField(new DbField("url", u.getUrl(), "STRING"));
		util.addInsertField(new DbField("agrees", u.getAgrees() + "", "NUMBER"));
		util.addInsertField(new DbField("thanks", u.getThanks() + "", "NUMBER"));
		util.addInsertField(new DbField("asks", u.getAsks() + "", "NUMBER"));
		util.addInsertField(new DbField("answers", u.getAnswers() + "", "NUMBER"));
		util.addInsertField(new DbField("posts", u.getPosts() + "", "NUMBER"));
		util.addInsertField(new DbField("followees", u.getFollowees() + "", "NUMBER"));
		util.addInsertField(new DbField("followers", u.getFollowers() + "", "NUMBER"));
		util.addInsertField(new DbField("hashId", u.getHashId(), "STRING"));
		util.addInsertField(new DbField("user_token", u.getUserToken(), "STRING"));
		util.addInsertField(
				new DbField("created", BasicDateUtil.getCurrentDateTimeString().replace(" ", "T"), "STRING"));
		DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
		logger.info(u.getUserToken() + "插入数据库成功---" + u.getUsername());
		return true;
	}

	private void updateuser(User u) throws SQLException {
		DbFieldSqlUtil util = new DbFieldSqlUtil("user", "");
		util.addUpdateField(new DbField("location", StringEscapeUtils.escapeSql(u.getLocation()), "STRING"));
		util.addUpdateField(new DbField("business", StringEscapeUtils.escapeSql(u.getBusiness()), "STRING"));
		util.addUpdateField(new DbField("employment", StringEscapeUtils.escapeSql(u.getEmployment()), "STRING"));
		util.addUpdateField(new DbField("education", StringEscapeUtils.escapeSql(u.getEducation()), "STRING"));
		if (BasicStringUtil.isNotNullString(u.getUsername()))
			util.addUpdateField(new DbField("username", StringEscapeUtils.escapeSql(u.getUsername()), "STRING"));
		util.addUpdateField(new DbField("sex", u.getSex(), "STRING"));
		util.addUpdateField(new DbField("url", u.getUrl(), "STRING"));
		util.addUpdateField(new DbField("agrees", u.getAgrees() + "", "NUMBER"));
		util.addUpdateField(new DbField("thanks", u.getThanks() + "", "NUMBER"));
		util.addUpdateField(new DbField("asks", u.getAsks() + "", "NUMBER"));
		util.addUpdateField(new DbField("answers", u.getAnswers() + "", "NUMBER"));
		util.addUpdateField(new DbField("posts", u.getPosts() + "", "NUMBER"));
		util.addUpdateField(new DbField("followees", u.getFollowees() + "", "NUMBER"));
		util.addUpdateField(new DbField("followers", u.getFollowers() + "", "NUMBER"));
		util.addUpdateField(new DbField("hashId", u.getHashId(), "STRING"));
		util.addWhereField(new DbField("user_token", u.getUserToken(), "STRING"));
		DbExecMgr.execOnlyOneUpdate(util.getUpdateSql());
		logger.info(u.getUserToken() + "更新成功---" + u.getUsername());
	}

	public boolean insertUser(Connection cn, User u) {
		try {
			if (isExistUser(cn, u.getUserToken())) {
				logger.info(u.getUserToken() + "已经存在");
				return false;
			}
			String column = "location,business,sex,employment,username,url,agrees,thanks,asks,"
					+ "answers,posts,followees,followers,hashId,education,user_token,created";
			String values = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
			String sql = "insert into user (" + column + ") values(" + values + ")";
			PreparedStatement pstmt;
			pstmt = cn.prepareStatement(sql);
			pstmt.setString(1, u.getLocation());
			pstmt.setString(2, u.getBusiness());
			pstmt.setString(3, u.getSex());
			pstmt.setString(4, u.getEmployment());
			pstmt.setString(5, u.getUsername());
			pstmt.setString(6, u.getUrl());
			pstmt.setInt(7, u.getAgrees());
			pstmt.setInt(8, u.getThanks());
			pstmt.setInt(9, u.getAsks());
			pstmt.setInt(10, u.getAnswers());
			pstmt.setInt(11, u.getPosts());
			pstmt.setInt(12, u.getFollowees());
			pstmt.setInt(13, u.getFollowers());
			pstmt.setString(14, u.getHashId());
			pstmt.setString(15, u.getEducation());
			pstmt.setString(16, u.getUserToken());
			pstmt.setString(17, BasicDateUtil.getCurrentDateTimeString().replace(" ", "T"));
			pstmt.executeUpdate();
			pstmt.close();
			logger.info(u.getUserToken() + "插入数据库成功---" + u.getUsername());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// ConnectionManager.close();
		}
		return true;
	}

	public boolean insertUrl(Connection cn, String md5Url) {
		String isContainSql = "select count(*) from url WHERE md5_url ='" + md5Url + "'";
		try {
			if (isExistRecord(cn, isContainSql)) {
				logger.debug("数据库已经存在该url---" + md5Url);
				return false;
			}
			String sql = "insert into url (md5_url) values( ?)";
			PreparedStatement pstmt;
			pstmt = cn.prepareStatement(sql);
			pstmt.setString(1, md5Url);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logger.debug("url插入成功---");
		return true;
	}
}

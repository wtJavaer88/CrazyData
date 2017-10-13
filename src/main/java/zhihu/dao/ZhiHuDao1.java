package zhihu.dao;

import java.sql.Connection;
import java.sql.SQLException;

import zhihu.entity.User;

public interface ZhiHuDao1 {

	boolean isExistRecord(String sql) throws SQLException;

	boolean isExistRecord(Connection cn, String sql) throws SQLException;

	boolean isExistUser(String userToken);

	boolean isExistUser(Connection cn, String userToken);

	boolean insertUser(User user) throws SQLException;

	boolean insertUser(Connection cn, User user);

	/**
	 * 插入url,插入成功返回true，若已存在该url则返回false
	 * 
	 * @param cn
	 * @param md5Url
	 * @return
	 */
	boolean insertUrl(Connection cn, String md5Url);
}

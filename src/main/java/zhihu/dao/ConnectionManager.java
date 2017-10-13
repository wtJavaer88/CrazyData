package zhihu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.crawl.core.util.SimpleLogger;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

/**
 * DB Connection管理
 */
public class ConnectionManager {
	private static Logger logger = SimpleLogger.getSimpleLogger(ConnectionManager.class);
	private static Connection conn;
	static String url = "";
	static String user = "";
	static String password = "";
	static String dbName = "";
	static String dbDriver = "";

	public static Connection getConnection() {
		// 获取数据库连接
		try {
			if (conn == null || conn.isClosed()) {
				conn = createConnection();
			} else {
				return conn;
			}
		} catch (SQLException e) {
			logger.error("SQLException", e);
		}
		return conn;
	}

	static {
		try {
			// Class.forName("org.gjt.mm.mysql.Driver");// 加载驱动
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void close() {
		if (conn != null) {
			// logger.info("关闭连接中");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("SQLException", e);
			}
		}
	}

	public static Connection createConnection() {

		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);// 建立mysql的连接
			logger.debug("success!");
		} catch (MySQLSyntaxErrorException e) {
			logger.error("数据库不存在..请先手动创建创建数据库:" + dbName);
			e.printStackTrace();
		} catch (SQLException e2) {
			logger.error("SQLException", e2);
		}
		return con;
	}

	public static void main(String[] args) throws Exception {
		getConnection();
		getConnection();
		close();
	}
}
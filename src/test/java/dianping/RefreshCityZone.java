package dianping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBconnectionMgr;
import db.DbExecMgr;

public class RefreshCityZone {
	public static void main(String[] args) throws SQLException {
		DBconnectionMgr.setJDBCName("jdbc:sqlite:D:\\database\\dianping.db");
		Map zoneMap = DbExecMgr.getSelectAllSqlMap("SELECT * FROM CITY_ZONE");
		Map fieldMap;
		Map<String, String> map = new HashMap<String, String>();
		List<CZ> czs = new ArrayList<CZ>();
		for (int i = 1; i <= zoneMap.size(); i++) {
			fieldMap = (Map) zoneMap.get(i);
			String code = String.valueOf(fieldMap.get("CODE"));
			String name = String.valueOf(fieldMap.get("NAME"));
			String ownstring = String.valueOf(fieldMap.get("OWN_STRING"));
			czs.add(new CZ(code, name, ownstring));
			map.put(code, name);
		}

		for (CZ cz : czs) {
			String[] split = cz.getOwn().split("-");
			String own = "";
			String own_zone = "";
			for (int i = 0; i < split.length; i++) {
				own += map.get(split[i]) + "-";
				if (i == 1) {
					own_zone = own;
				}
			}
			System.out.println(own);
			DbExecMgr.execOnlyOneUpdate("UPDATE CITY_ZONE SET OWN_NAME='" + own + "', own_zone_name = '" + own_zone
					+ "' WHERE CODE='" + cz.getC() + "'");
		}

	}

	static class CZ {
		private String c;
		private String n;
		private String own;

		public CZ(String c, String n, String own) {
			this.c = c;
			this.n = n;
			this.own = own;
		}

		public String getC() {
			return c;
		}

		public void setC(String c) {
			this.c = c;
		}

		public String getN() {
			return n;
		}

		public void setN(String n) {
			this.n = n;
		}

		public String getOwn() {
			return own;
		}

		public void setOwn(String own) {
			this.own = own;
		}
	}
}

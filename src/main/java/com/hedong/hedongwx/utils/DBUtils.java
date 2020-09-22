package com.hedong.hedongwx.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
//		String url = "jdbc:mysql://47.93.203.50:3306/hdwx?useUnicode=true&characterEncoding=UTF-8";
		String url = "jdbc:mysql://localhost:3306/hdwx?useUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "hedong2018";
		return DriverManager.getConnection(url, username, password);
	}
	
	public static Statement getStatement(Connection conn) throws Exception {
		return conn.createStatement();
	}
	
	public static PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getSQLString(PreparedStatement ps) {
		String string = ps.toString();
		return string.substring(string.indexOf(":") + 1).trim();
	}
	
	public static void updateSqlData1(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData2(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData3(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData4(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData5(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData6(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData7(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData8(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData9(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateSqlData10(String sql) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
}

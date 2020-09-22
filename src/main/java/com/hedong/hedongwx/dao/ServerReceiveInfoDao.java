package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.hedong.hedongwx.utils.DBUtils;

public class ServerReceiveInfoDao {

	public void addServerReceiveInfo(String ipaddr, String message) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_server_receiveinfo (ipaddr,message) values (?,?)";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, ipaddr);
			ps.setString(2, message);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils.close(conn);
		}
	}
}

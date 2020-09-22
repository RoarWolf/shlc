package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hedong.hedongwx.entity.ServerSendMessage;
import com.hedong.hedongwx.utils.DBUtils;


public class ServerSendMessageDao {
	
	public static ServerSendMessageDao serverSendMessageDao = null;
	
	public static ServerSendMessageDao getInstance() {
		if (serverSendMessageDao == null) {
			serverSendMessageDao = new ServerSendMessageDao();
			return serverSendMessageDao;
		}else {
			return serverSendMessageDao;
		}
	}
	
	public void deleteById(Integer id) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "delete from hd_server_sendinfo where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public void deleteServerSendMessage() {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "delete from hd_server_sendinfo";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

	public List<ServerSendMessage> getList() {
		Connection conn = null;
		List<ServerSendMessage> list = new ArrayList<>();
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_server_sendinfo";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			
			ServerSendMessage message = null;
			while (result.next()) {
				message = new ServerSendMessage();
				message.setId(result.getInt("id"));
				message.setMessage(result.getString("message"));
				list.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils.close(conn);
		}
		return list;
	}
}

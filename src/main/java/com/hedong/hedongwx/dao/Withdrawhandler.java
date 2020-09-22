package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.stereotype.Service;

import com.hedong.hedongwx.utils.DBUtils;

@Service
public class Withdrawhandler {
	
	public static void timingConnect() {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id from hd_withdraw where id<3";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
}

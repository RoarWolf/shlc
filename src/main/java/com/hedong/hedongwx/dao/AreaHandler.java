package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.utils.DBUtils;

public class AreaHandler {
	
	/**
	 * 查询合伙的小区
	 * @param aid
	 * @param type
	 * @return
	 */
	public static List<Map<String, Object>> getPartnerInfo(Integer aid, Integer type) {
		Connection conn = null;
		List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT * FROM hd_arearelevance WHERE aid=? AND type = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, aid);
			ps.setInt(2, type);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("aid", result.getInt("aid"));
				map.put("partid", result.getInt("partid"));
				map.put("percent", result.getDouble("percent"));
				map.put("type", result.getInt("type"));
				partInfo.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return partInfo;
	}
}

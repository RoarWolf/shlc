package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedong.hedongwx.utils.DBUtils;
import com.hedong.hedongwx.utils.TempMsgUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年8月27日 下午2:26:05  
 */
public class CommonHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);
	
	/**
	 * @Description： 设备状态变更提醒
	 * @author： origin
	 */
	public static void equipmess(Byte state, Integer merid, String code) {
		Connection conn = null;
		try {
			if(authorSwitch(merid,2)){
				conn = DBUtils.getConnection();
				String first = "您好，您的设备  "+code+" 状态发生改变！";
				String equipstate = state == 1 ? "在线" : "离线";
				if(state==0){/** 设备在线状态1、在线0、不在线 */
					equipstate = "离线";
				}else if(state==1){
					equipstate = "在线";
				}else{
					equipstate = "异常";
				}
				String equipname = code;
				String remark = null;
				TempMsgUtil.equipSendMsg( first, merid, code, equipname, equipstate, remark, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static Boolean authorSwitch(Integer merid,Integer type) {
		Connection conn = null;
		Boolean fole = false;
		String switchs = "2";
		try {
			conn = DBUtils.getConnection();
//			String sql = "SELECT * From  FROM hd_dealerauthority WHERE merid = ?";
			String sql = "SELECT id, merid, withmess, equipmess, incomemess, ordermess, incoinrefund, newsmess, "
					   + "inform, informess, sendmess FROM hd_dealerauthority WHERE merid = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				if(type==1){//提现通知：1.接受通知 2.不接受通知
					switchs = result.getString("withmess");
				}else if(type==2){//设备上下线通知：1.接受通知 2.不接受通知
					switchs = result.getString("equipmess");
				}else if(type==3){//订单收入通知：1.接受通知 2.不接受通知
					switchs = result.getString("ordermess");
				}else if(type==4){//收益通知：1.接受通知 2.不接受通知
					switchs = result.getString("incomemess");
				}
				if(switchs.equals("1")) fole = true;
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		} finally {
			DBUtils.close(conn);
		}
		return fole;
	}
	
	public static Map<String, String> selectEealerauthority(Integer merid,Integer type) {
		Connection conn = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT id, merid, withmess, equipmess, incomemess, ordermess, incoinrefund, newsmess, "
					   + "inform, informess, sendmes FROM hd_dealerauthority WHERE merid = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				map.put("withmess", result.getString("withmess"));//提现通知：1.接受通知 2.不接受通知
				map.put("equipmess", result.getString("equipmess"));//设备上下线通知：1.接受通知 2.不接受通知
				map.put("incomemess", result.getString("incomemess"));//收益通知：1.接受通知 2.不接受通知
				map.put("ordermess", result.getString("ordermess"));//订单收入通知：1.接受通知 2.不接受通知
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return map;
	}
	
}

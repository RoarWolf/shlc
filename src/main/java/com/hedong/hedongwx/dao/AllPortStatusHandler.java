package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Service;

import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.utils.DBUtils;

@Service
public class AllPortStatusHandler {
	
	public static void main(String[] args) {
		new AllPortStatusHandler().updateAllPortStatus1("000001", (byte) 1, (byte) 1,(short)  1,(short)  1, (short) 1, 1, 1);
	}

	
	
	/** add equipment port status info 
	 * @param port_a 
	 * @param port_v */
	public static void addAllPortStatus(String code, byte port, byte portStatus, short time, short power, short elec, double port_v, double port_a) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_allportstatus(equipmentnum,port,port_status,time,power,elec,port_v,port_a,update_time) values(?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, port);
			ps.setByte(3, portStatus);
			ps.setShort(4, time);
			ps.setShort(5, power);
			ps.setShort(6, elec);
			ps.setDouble(7, port_v);
			ps.setDouble(8, port_a);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/** add equipment port status info 
	 * @param port_a 
	 * @param port_v */
	public void addAllPortStatus1(String code, byte port, byte portStatus, short time, short power, short elec, double port_v, double port_a) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_allportstatus(equipmentnum,port,port_status,time,power,elec,port_v,port_a,update_time) values(?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, port);
			ps.setByte(3, portStatus);
			ps.setShort(4, time);
			ps.setShort(5, power);
			ps.setShort(6, elec);
			ps.setDouble(7, port_v);
			ps.setDouble(8, port_a);
			String sqlString = DBUtils.getSQLString(ps);
			System.out.println("sqlString===" + sqlString);
//			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/** query equipment if exist */
	public static String getAllPortStatusByEquipmentnum(String code,Byte port) {
		Connection conn = null;
		String equipmentnum = "";
		try {
			conn = DBUtils.getConnection();
			String sql = "select equipmentnum from hd_allportstatus where equipmentnum='" + code + "' and port = " + port;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				equipmentnum = result.getString("equipmentnum");
			}
		} catch (Exception e) {
			equipmentnum = "wolferror";
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return equipmentnum;
	}
	
	/** update allportstatus data 
	 * @param port_a 
	 * @param port_v */
	public static void updateAllPortStatus(String code, byte port, byte portStatus, short time, short power, short elec, double port_v, double port_a) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_allportstatus set port_status=?,time=?,power=?,elec=?,port_v=?,port_a=?, update_time=now() where equipmentnum=? and port = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setByte(1, portStatus);
			ps.setShort(2, time);
			ps.setShort(3, power);
			ps.setShort(4, elec);
			ps.setDouble(5, port_v);
			ps.setDouble(6, port_a);
			ps.setString(7, code);
			ps.setInt(8, port);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/** update allportstatus data 
	 * @param port_a 
	 * @param port_v */
	public void updateAllPortStatus1(String code, byte port, byte portStatus, short time, short power, short elec, double port_v, double port_a) {
		try {
			String sql = "update hd_allportstatus set port_status="
					+ portStatus + ",time=" + time + ",power=" + power + ",elec=" + elec + ",port_v="
					+ port_v + ",port_a=" + port_a + ", update_time=now() where equipmentnum='" + code + "' and port = " + port;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getAllPortStatusAmount(String code) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id hd_allportstatus where equipmentnum=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static AllPortStatus getEquipmentStatusByCodeAndPort(String code,Integer port) {
		Connection conn = null;
		
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_allportstatus where equipmentnum=? and port=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, port);
			ResultSet result = ps.executeQuery();
			AllPortStatus allPortStatus = null;
			while (result.next()) {
				allPortStatus = new AllPortStatus();
				allPortStatus.setPortStatus(result.getByte("port_status"));
				allPortStatus.setPower(result.getShort("power"));
				allPortStatus.setTime(result.getShort("time"));
				allPortStatus.setElec(result.getShort("elec"));
			}
			return allPortStatus;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return null;
	}
	
	public static void addPortRecord(String code,Byte port,Byte port_status,Short time,Short power,Short elec) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_allportRecord(equipmentnum,port,status,time,power,elec,record_time) values(?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, port);
			ps.setInt(3, port_status);
			ps.setInt(4, time);
			ps.setInt(5, power);
			ps.setInt(6, elec);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
}

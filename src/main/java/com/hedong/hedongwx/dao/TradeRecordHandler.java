package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.utils.DBUtils;

public class TradeRecordHandler {

	public static void addTradeRecord(int merid,int manid,int uid,String ordernum,double money,double mermoney,double manmoney,String code,
			int paysource,int paytype,int status) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_traderecord(merid,manid,uid,ordernum,money,mermoney,manmoney,code,paysource"
					+ ",paytype,status,create_time) values(?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ps.setInt(2, manid);
			ps.setInt(3, uid);
			ps.setString(4, ordernum);
			ps.setDouble(5, money);
			ps.setDouble(6, mermoney);
			ps.setDouble(7, manmoney);
			ps.setString(8, code);
			ps.setInt(9, paysource);
			ps.setInt(10, paytype);
			ps.setInt(11, status);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void addTradeRecordInfo(int merid,int manid,int uid,String ordernum,double money,double mermoney,double manmoney,String code,
			int paysource,int paytype,int status,String hardver,String comment) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_traderecord(merid,manid,uid,ordernum,money,mermoney,manmoney,code,paysource"
					+ ",paytype,status,hardver,comment,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ps.setInt(2, manid);
			ps.setInt(3, uid);
			ps.setString(4, ordernum);
			ps.setDouble(5, money);
			ps.setDouble(6, mermoney);
			ps.setDouble(7, manmoney);
			ps.setString(8, code);
			ps.setInt(9, paysource);
			ps.setInt(10, paytype);
			ps.setInt(11, status);
			ps.setString(12, hardver);
			ps.setString(13, comment);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static TradeRecord getTradereInfo(String ordernum) {
		Connection conn = null;
		TradeRecord tradere = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT id, merid, manid, uid, ordernum, money, mermoney, manmoney, code, paysource, paytype, status, hardver, comment, create_time "
					+ "FROM hd_traderecord WHERE ordernum=" + ordernum + " AND `status`= 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				tradere = new TradeRecord();
				tradere.setOrdernum(ordernum);
				tradere.setMerid(result.getInt("merid"));
				tradere.setManid(result.getInt("manid"));
				tradere.setMoney(result.getDouble("money"));
				tradere.setMermoney(result.getDouble("mermoney"));
				tradere.setManmoney(result.getDouble("manmoney"));
				tradere.setHardver(result.getString("hardver"));
				tradere.setComment(result.getString("comment"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return tradere;
	}
	
	public static void updateTrade(String ordernum,double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_traderecord set money=? where ordernum=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setString(2, ordernum);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
}

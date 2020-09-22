package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.utils.DBUtils;

public class OfflineCardHandler {
	
	public static void findChargeCardEndIdIsUnfinished(Byte res,String card_id,Short card_surp) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_offlinecard set recycletype=?,balance=? where cardID=? and handletype = 1 "
					+ "order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, res);
			ps.setDouble(2, (double)(card_surp) / 10);
			ps.setString(3, card_id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void findChargeCardEndIdIsUnfinished(int id,int paytype) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_offlinecard set paytype=? where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, paytype);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static OfflineCard getOfflineCardEndByParam(String code) {
		Connection conn = null;
		OfflineCard offlineCard = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id,uid,merchantid,begin_time,chargemoney,paytype,ordernum from hd_offlinecard where equipmentnum=? and status=1 and handletype=1 and cardID is not null and recycletype is null order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				offlineCard = new OfflineCard();
				offlineCard.setId(result.getInt("id"));
				offlineCard.setUid(result.getInt("uid"));
				offlineCard.setPaytype(result.getInt("paytype"));
				offlineCard.setMerchantid(result.getInt("merchantid"));
				offlineCard.setBeginTime(result.getTimestamp("begin_time"));
				offlineCard.setChargemoney(result.getDouble("chargemoney"));
				offlineCard.setOrdernum(result.getString("ordernum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return offlineCard;
	}
	
	public static void addOfflineCard(int merchantid,int uid,String ordernum,String equipmentnum,
			String cardID,double balance,double chargemoney,double accountmoney,double discountmoney,
			int paytype,int status,int handletype,int recycletype) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_offlinecard (merchantid,uid,ordernum,equipmentnum,cardID,balance,"
					+ "chargemoney,accountmoney,discountmoney,paytype,status,handletype,recycletype,"
					+ "begin_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merchantid);
			ps.setInt(2, uid);
			ps.setString(3, ordernum);
			ps.setString(4, equipmentnum);
			ps.setString(5, cardID);
			ps.setDouble(6, balance);
			ps.setDouble(7, chargemoney);
			ps.setDouble(8, accountmoney);
			ps.setDouble(9, discountmoney);
			ps.setInt(10, paytype);
			ps.setInt(11, status);
			ps.setInt(12, handletype);
			ps.setInt(13, recycletype);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

}

package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.utils.DBUtils;

public class OnlineCardHandler {
	
	/**
	 * 查看卡号是否存在，存在并获取状态和余额
	 * @param cardID
	 * @return
	 */
	public static OnlineCard cardIsExist(String cardID) {
		Connection conn = null;
		OnlineCard onlineCard = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select merid,uid,money,sendmoney,status,relevawalt from hd_onlinecard where cardID='" + cardID + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				onlineCard = new OnlineCard();
				onlineCard.setMerid(result.getInt("merid"));
				onlineCard.setUid(result.getInt("uid"));
				onlineCard.setStatus(result.getInt("status"));
				onlineCard.setMoney(result.getDouble("money"));
				onlineCard.setSendmoney(result.getDouble("sendmoney"));
				onlineCard.setRelevawalt(result.getInt("relevawalt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return onlineCard;
	}

	public static void activeCard(int merid, String cardID) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_onlinecard set status=1,merid=? where cardID=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ps.setString(2, cardID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void addOnlineCardOperate(int uid,int merid,String ordernum,String cardID,String code,double balance,double money,
			double sendmoney,double accountmoney,Double topupbalance, Double givebalance, int type,int status) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
//			String sql = "insert into hd_onlinecard_record (uid,merid,ordernum,cardID,code,balance,money,"
//					+ "accountmoney,topupbalance,givebalance,type,status,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,now())";
			
			String sql = "insert into hd_onlinecard_record (uid,merid,ordernum,cardID,code,balance,money,sendmoney,"
					+ "accountmoney,topupbalance,givebalance,type,status,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, uid);
			ps.setInt(2, merid);
			ps.setString(3, ordernum);
			ps.setString(4, cardID);
			ps.setString(5, code);
			ps.setDouble(6, balance);
			ps.setDouble(7, money);
			ps.setDouble(8, sendmoney);
			ps.setDouble(9, accountmoney);
			ps.setDouble(10, topupbalance);
			ps.setDouble(11, givebalance);
			ps.setInt(12, type);
			ps.setInt(13, status);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

	public static void updateCardMoney(double money,String cardID,int handletype, Double sendmoney) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (handletype == 0) {
				sql = "update hd_onlinecard set money=money-?, sendmoney=? where cardID=?";
			} else if (handletype == 1) {
				sql = "update hd_onlinecard set money=money+?, sendmoney=? where cardID=?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setDouble(2, sendmoney);
			ps.setString(3, cardID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static OnlineCardRecord getCardRecordEndByCardID(Integer uid, String cardID) {
		Connection conn = null;
		OnlineCardRecord onlineCardRecord = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select ordernum,money from hd_onlinecard_record where cardID=? AND uid=? and type IN (3,6) and status = 1 ORDER BY create_time DESC limit 1";
//			String sql = "select ordernum,money from hd_onlinecard_record where cardID=? and type=3 and status = 1 limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, cardID);
			ps.setInt(2, uid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				onlineCardRecord = new OnlineCardRecord();
				onlineCardRecord.setOrdernum(result.getString("ordernum"));
				onlineCardRecord.setMoney(result.getDouble("money"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return onlineCardRecord;
	}
	
	public static OnlineCardRecord getCardRecordData(String cardID, Integer dealid, Integer tuorid) {
		Connection conn = null;
		OnlineCardRecord onlineCardRecord = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_onlinecard_record where cardID=? and status = 1 ORDER BY create_time DESC limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, cardID);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				onlineCardRecord = new OnlineCardRecord();
				onlineCardRecord.setOrdernum(result.getString("ordernum"));
				onlineCardRecord.setBalance(result.getDouble("balance"));
				onlineCardRecord.setMoney(result.getDouble("money"));
				onlineCardRecord.setSendmoney(result.getDouble("sendmoney"));
				onlineCardRecord.setAccountmoney(result.getDouble("accountmoney"));
				onlineCardRecord.setTopupbalance(result.getDouble("topupbalance"));
				onlineCardRecord.setGivebalance(result.getDouble("givebalance"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return onlineCardRecord;
	}
	
	public static int getTradeRecordByOrdernum(String ordernum) {
		Connection conn = null;
		int temp = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id from hd_traderecord where ordernum=? limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, ordernum);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				temp = result.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return temp;
	}
	
	public static void updateCardRecordMid(String ordernum,int merid) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_onlinecard_record set merid=? where ordernum=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ps.setString(2, ordernum);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	
	public static void insertMnoeyRecord(int uid,String ordernum,int paytype,int status,double money,
			double tmoney,double balance,String remark) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_moneyrecord(uid,ordernum,paytype,status,money,tomoney,balance,paytime,remark) values (?,?,?,?,?,?,?,now(),?)";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, uid);
			ps.setString(2, ordernum);
			ps.setInt(3, paytype);
			ps.setInt(4, status);
			ps.setDouble(5, money);
			ps.setDouble(6, tmoney);
			ps.setDouble(7, balance);
			ps.setString(8, remark);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	
}

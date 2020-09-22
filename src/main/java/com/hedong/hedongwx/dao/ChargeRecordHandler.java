package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DBUtils;
import com.hedong.hedongwx.utils.HttpRequest;

public class ChargeRecordHandler {

	
	
	


	

	

	
	
	/** stop charging */
	public static void updateChargeRecordStopCharging(Integer chargeid,Integer resultinfo) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_chargerecord set endtime=now(),resultinfo=? where id = ? or ifcontinue = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, resultinfo);
			ps.setInt(2, chargeid);
			ps.setInt(3, chargeid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	/** query end unfinish chargerecord id */
	public static ChargeRecord getEndChargeRecordUnfinish(String equipment,Integer port) {
		Connection conn = null;
		ChargeRecord chargeRecord = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id,uid,merchantid,begintime from hd_chargerecord where equipmentnum = ? and number = 0 and port = ? and status = 1 and endtime is null and ifcontinue is null order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, equipment);
			ps.setInt(2, port);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				chargeRecord = new ChargeRecord();
				chargeRecord.setId(result.getInt("id"));
				chargeRecord.setMerchantid(result.getInt("merchantid"));
				chargeRecord.setUid(result.getInt("uid"));
				chargeRecord.setBegintime(result.getTimestamp("begintime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return chargeRecord;
	}
	
	public static List<ChargeRecord> getChargeListUnfinish(int chargeid) {
		Connection conn = null;
		List<ChargeRecord> chargeList = new ArrayList<>();
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_chargerecord where id=? or ifcontinue=? order by id desc";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, chargeid);
			ps.setInt(2, chargeid);
			ResultSet result = ps.executeQuery();
			ChargeRecord charge = null;
			while (result.next()) {
				charge = new ChargeRecord();
				charge.setId(result.getInt("id"));
				charge.setMerchantid(result.getInt("merchantid"));
				charge.setOrdernum(result.getString("ordernum"));
				charge.setExpenditure(result.getDouble("expenditure"));
				charge.setMerchantid(result.getInt("merchantid"));
				charge.setUid(result.getInt("uid"));
				charge.setDurationtime(result.getString("durationtime"));
				charge.setQuantity(result.getInt("quantity"));
				charge.setPaytype(result.getInt("paytype"));
				charge.setBegintime(result.getTimestamp("begintime"));
				charge.setPort(result.getInt("port"));
				charge.setNumber(result.getInt("number"));
				chargeList.add(charge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return chargeList;
	}
	
	/** query end chargerecord id */
	public static Integer getEndChargeRecord(String equipment,Integer port) {
		Connection conn = null;
		Integer chargeid = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select id from hd_chargerecord where equipmentnum = ? and port = ? and status = 1 and number = 0 and ifcontinue is null and endtime is null order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, equipment);
			ps.setInt(2, port);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				chargeid = result.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			chargeid = -1;
		} finally {
			DBUtils.close(conn);
		}
		return chargeid;
	}
	
	/** query unfinish chargeRecord id */
	public static List<Integer> getChargeUnfinshId(String equipment,Integer port) {
		Connection conn = null;
		List<Integer> idlist = new ArrayList<>();
		try {
			conn = DBUtils.getConnection();
			String sql = "select id from hd_chargerecord where equipmentnum = ? and port = ? and paytype in (1,2,4,5,6) and status = 1 and number = 0 and ifcontinue is null and endtime is null and DATE_SUB(CURDATE(), INTERVAL 2 DAY) <= date(begintime) order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, equipment);
			ps.setInt(2, port);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				idlist.add(result.getInt("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return idlist;
	}

	public static ChargeRecord getChargeRecordById(Integer id) {
		Connection conn = null;
		ChargeRecord chargeRecord = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_chargerecord where id=" + id;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				chargeRecord = new ChargeRecord();
				chargeRecord.setId(result.getInt("id"));
				chargeRecord.setMerchantid(result.getInt("merchantid"));
				chargeRecord.setUid(result.getInt("uid"));
				chargeRecord.setExpenditure(result.getDouble("expenditure"));
				chargeRecord.setPaytype(result.getInt("paytype"));
				chargeRecord.setOrdernum(result.getString("ordernum"));
				chargeRecord.setDurationtime(result.getString("durationtime"));
				chargeRecord.setQuantity(result.getInt("quantity"));
				chargeRecord.setBegintime(result.getTimestamp("begintime"));
				chargeRecord.setNumber(result.getInt("number"));
				chargeRecord.setPaytype(result.getInt("paytype"));
				chargeRecord.setConsumeTime(result.getInt("consume_time"));
				chargeRecord.setConsumeQuantity(result.getInt("consume_quantity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return chargeRecord;
	}
	
	public static void updateChargeResult(int number,Integer id,Double expenditure,Double refundMoney,int reason,int flag,int useTime,int useElec) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			if (flag == 1) {
				String sql = "update hd_chargerecord set number=?,refund_money=?,refund_time=now(),endtime=now(),resultinfo=?,consume_time = ? where id = ?";
				PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
				ps.setInt(1, number);
				ps.setDouble(2, refundMoney);
				ps.setInt(3, reason);
				ps.setInt(4, useTime);
				ps.setInt(5, id);
				ps.executeUpdate();
			} else if (flag == 2) {
				String sql = "update hd_chargerecord set expenditure=?,endtime=now(),resultinfo=?,consume_time = ? where id = ?";
				PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
				ps.setDouble(1, refundMoney);
				ps.setInt(2, reason);
				ps.setInt(3, useTime);
				ps.setInt(4, id);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateChargeRefundNumer(int number,Integer id,Double expenditure,Double refundMoney,
			Integer useTime,Integer useElec) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (useTime != null && useElec != null) {
				sql = "update hd_chargerecord set number=?,expenditure=?,refund_money=?,consume_time = ?,consume_quantity = ?,refund_time=now() where id = ?";
			} else if (useTime == null && useElec != null) {
				sql = "update hd_chargerecord set number=?,expenditure=?,refund_money=?,consume_quantity = ?,refund_time=now() where id = ?";
			} else if (useTime != null && useElec == null) {
				sql = "update hd_chargerecord set number=?,expenditure=?,refund_money=?,consume_time = ?,refund_time=now() where id = ?";
			} else {
				sql = "update hd_chargerecord set number=?,expenditure=?,refund_money=?,refund_time=now() where id = ?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, number);
			ps.setDouble(2, expenditure);
			ps.setDouble(3, refundMoney);
			if (useTime != null && useElec != null) {
				ps.setInt(4, useTime);
				ps.setInt(5, useElec);
				ps.setInt(6, id);
			} else if (useTime == null && useElec != null) {
				ps.setInt(4, useElec);
				ps.setInt(5, id);
			} else if (useTime != null && useElec == null) {
				ps.setInt(4, useTime);
				ps.setInt(5, id);
			} else {
				ps.setInt(4, id);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateChargeResultinfo(String code,int port) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_chargerecord set resultinfo = -1 where equipmentnum = ? and port = ? and status = 1 and number = 0 order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, port);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateChargeInfo(int chargeid,int time, int elec) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_chargerecord set consume_time = ?,consume_quantity = ? where id = ? or ifcontinue = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, time);
			ps.setInt(2, elec);
			ps.setInt(3, chargeid);
			ps.setInt(4, chargeid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateChargeEndInfo(int chargeid,int time, Integer elec,int resultinfo) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (elec == null) {
				sql = "update hd_chargerecord set endtime=now(),consume_time = ?,resultinfo = ? where id = ? or ifcontinue = ?";
			} else {
				sql = "update hd_chargerecord set endtime=now(),consume_time = ?,consume_quantity = ?,resultinfo = ? where id = ? or ifcontinue = ?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, time);
			if (elec == null) {
				ps.setInt(2, resultinfo);
				ps.setInt(3, chargeid);
				ps.setInt(4, chargeid);
			} else {
				ps.setInt(2, elec);
				ps.setInt(3, resultinfo);
				ps.setInt(4, chargeid);
				ps.setInt(5, chargeid);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void addChargeRecord(int merchantid,int uid,String ordernum,int paytype,int status,
			String equipmentnum,int port,double expenditure,String durationtime,int quantity,int resultinfo,int ifcontinue) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (ifcontinue != 0) {
				sql = "insert into hd_chargerecord (merchantid,uid,ordernum,paytype,status,"
						+ "equipmentnum,port,expenditure,durationtime,quantity,begintime,resultinfo,ifcontinue) values (?,?,?,?,?,?,?,?,?,?,now(),?,?)";
			} else {
				sql = "insert into hd_chargerecord (merchantid,uid,ordernum,paytype,status,"
						+ "equipmentnum,port,expenditure,durationtime,quantity,begintime,resultinfo) values (?,?,?,?,?,?,?,?,?,?,now(),?)";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merchantid);
			ps.setInt(2, uid);
			ps.setString(3, ordernum);
			ps.setInt(4, paytype);
			ps.setInt(5, status);
			ps.setString(6, equipmentnum);
			ps.setInt(7, port);
			ps.setDouble(8, expenditure);
			ps.setString(9, durationtime);
			ps.setInt(10, quantity);
			ps.setInt(11, resultinfo);
			if (ifcontinue != 0) {
				ps.setInt(12, ifcontinue);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	// 查询单条记录，返回map类型数据
	public static Map<String, Object> executeQuery(String str) {
		Connection conn = null;
		ResultSet result = null;
		Map<String, Object> rowMap = new HashMap<String, Object>();
		try {
			conn = DBUtils.getConnection();
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, str.toString());
			result = ps.executeQuery();
			if (!result.next()) {
				rowMap = null;
			} else {
				// result.first();
				ResultSetMetaData md = result.getMetaData(); // 获得结果集结构信息,元数据
				int columnCount = md.getColumnCount(); // 获得列数
				for (int i = 1; i <= columnCount; i++) {
					rowMap.put(md.getColumnName(i), result.getObject(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return rowMap;

	}
	
	//----------------------inCoins-----------------------------------

	public static void addInCoinsRecord(Integer merchantid,String equipmentnum,Byte port,Byte money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_incoins(merchantid,uid,ordernum,equipmentnum,port,money,status,"
					+ "handletype,recycletype,begin_time) values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merchantid);
			ps.setInt(2, 0);
			ps.setString(3, HttpRequest.createOrdernum(6));
			ps .setString(4, equipmentnum);
			ps.setByte(5, port);
			ps.setDouble(6, money);
			ps.setByte(7, (byte) 1);
			ps.setByte(8, (byte) 3);
			ps.setByte(9, (byte) 1);
			ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void editInCoinsRecord(String code) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_incoins set recycletype = 1 where equipmentnum=? order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	//---------------------------------------------------------------------

	//---------------------------user--------------------------------------
	public static void updateUserMoney(Double money, Integer id, Double sendmoney, Integer handleType) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (handleType == 1) {
				sql = "update hd_user set earnings=earnings-? where id=?";
			} else if (handleType == 2) {
				sql = "update hd_user set balance=balance+? where id=?";
			} else if (handleType == 3) {
				sql = "update hd_user set earnings=earnings+? where id=?";
			} else if (handleType == 4) {
				sql = "update hd_user set balance=balance-? where id=?";
			} else if (handleType == 5) {
				sql = "update hd_user set balance=balance+?,sendmoney=sendmoney+? where id=?";
			} else if (handleType == 6) {
				sql = "update hd_user set balance=balance-?,sendmoney=sendmoney-? where id=?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setInt(2, id);
			if (handleType == 5) {
				ps.setDouble(3, sendmoney);
			} else if (handleType == 6) {
				ps.setDouble(3, sendmoney);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static Double getUserBalance(Integer id) {
		Connection conn = null;
		Double balance = 0.0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select balance from hd_user where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				balance = result.getDouble("balance");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return balance;
	}
	
	//--------------------------userend------------------------------------
	
	//--------------------------money start--------------------------------
	
	public static void addMoneyRecord(Money moneyEntity) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_moneyrecord(uid,ordernum,paytype,status,money,sendmoney,tomoney,"
					+ "balance,topupbalance,givebalance,paytime,remark) values(?,?,?,?,?,?,?,?,?,?,now(),?)";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, moneyEntity.getUid());
			ps.setString(2, moneyEntity.getOrdernum());
			ps.setInt(3, moneyEntity.getPaytype());
			ps.setInt(4, moneyEntity.getStatus());
			ps.setDouble(5, CommUtil.toDouble(moneyEntity.getMoney()));
			ps.setDouble(6, CommUtil.toDouble(moneyEntity.getSendmoney()));
			ps.setDouble(7, CommUtil.toDouble(moneyEntity.getTomoney()));
			ps.setDouble(8, CommUtil.toDouble(moneyEntity.getBalance()));
			ps.setDouble(9, CommUtil.toDouble(moneyEntity.getTopupbalance()));
			ps.setDouble(10, CommUtil.toDouble(moneyEntity.getGivebalance()));
			ps.setString(11, moneyEntity.getRemark());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
}

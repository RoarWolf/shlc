package com.hedong.hedongwx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DBUtils;

public class UserHandler {

	//商户
	public static void addMerDetail(int merid,String ordernum,double money,double balance,int paysource,int paytype,int status) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_mer_detail (merid, ordernum, money, balance, paysource, paytype, status, create_time) values(?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ps.setString(2, ordernum);
			ps.setDouble(3, money);
			ps.setDouble(4, balance);
			ps.setInt(5, paysource);
			ps.setInt(6, paytype);
			ps.setInt(7, status);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	//all用户
	public static double getUserEarningsById(int merid) {
		Connection conn = null;
		double earnings = 0.0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select earnings from hd_user where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				earnings = result.getDouble("earnings");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return earnings;
	}
	
	public static void addGenDetail(int uid,int merid,String ordernum, double money,double sendmoney,double tomoney,
			double balance,double topupbalance,double givebalance, int paysource,String remark) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_gen_detail (uid, merid, ordernum, money, sendmoney, tomoney, balance, "
					+ "topupbalance, givebalance, paysource, remark, create_time) values(?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, uid);
			ps.setInt(2, merid);
			ps.setString(3, ordernum);
			ps.setDouble(4, CommUtil.toDouble(money));
			ps.setDouble(5, CommUtil.toDouble(sendmoney));
			ps.setDouble(6, CommUtil.toDouble(tomoney));
			ps.setDouble(7, CommUtil.toDouble(balance));
			ps.setDouble(8, CommUtil.toDouble(topupbalance));
			ps.setDouble(9, CommUtil.toDouble(givebalance));
			ps.setInt(10, paysource);
			ps.setString(11, remark);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static boolean checkUserBindMidIsMerId(int uid,int merid) {
		Connection conn = null;
		boolean flag = false;
		try {
			conn = DBUtils.getConnection();
			String sql = "select merid from hd_user where id=" + uid;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				int int1 = result.getInt("merid");
				if (merid == int1) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return flag;
	}
	
	public static User getUserInfo(int id) {
		Connection conn = null;
		User user = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select openid,phone_num,servephone,aid,balance,sendmoney from hd_user where id=" + id;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				user = new User();
				user.setOpenid(result.getString("openid"));
				user.setPhoneNum(result.getString("phone_num"));
				user.setServephone(result.getString("servephone"));
				user.setAid(result.getInt("aid"));
				user.setBalance(CommUtil.toDouble(result.getDouble("balance")));
				user.setSendmoney(CommUtil.toDouble(result.getDouble("sendmoney")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return user;
	}
	
	public static PackageMonth getPackageMonth(int uid) {
		Connection conn = null;
		PackageMonth packageMonth = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select everydaynum,everymonthnum,surpnum,todaysurpnum from hd_packagemonth where uid=" + uid;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				packageMonth = new PackageMonth();
				packageMonth.setTodaysurpnum(result.getInt("todaysurpnum"));
				packageMonth.setSurpnum(result.getInt("surpnum"));
				packageMonth.setEverydaynum(result.getInt("everydaynum"));
				packageMonth.setEverymonthnum(result.getInt("everymonthnum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return packageMonth;
	}
	
	public static void updatePackageMonth(int everydaynum, int everymonthnum, int uid) {
		Connection conn = null;
		int addtodaysum = 0;
		int addsurpsum = 0;
		if (everydaynum != 0 && everymonthnum == 0) {
			addtodaysum = 1;
		} else if (everydaynum == 0 && everymonthnum != 0) {
			addsurpsum = 1;
		} else if (everydaynum != 0 && everymonthnum != 0) {
			addtodaysum = 1;
			addsurpsum = 1;
		}
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_packagemonth set todaysurpnum = todaysurpnum + ?,surpnum = surpnum + ? where uid = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, addtodaysum);
			ps.setInt(2, addsurpsum);
			ps.setInt(3, uid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void addPackageMonthRecord(int uid,String ordernum,double money,int everydaynum,
			int surpnum,int time,double elec) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_packagemonth_record (uid,ordernum,money,paysource,status,"
					+ "everydaynum,changenum,surpnum,changemonth,time,elec,create_time) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, uid);
			ps.setString(2, ordernum);
			ps.setDouble(3, money);
			ps.setInt(4, 2);
			ps.setInt(5, 2);
			ps.setInt(6, everydaynum + 1);
			ps.setInt(7, 1);
			ps.setInt(8, surpnum + 1);
			ps.setInt(9, 0);
			ps.setInt(10, time);
			ps.setDouble(11, elec);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	

	public static void updateWalltMoney(double topupmoney, Double sendmoney, int handletype, int uid) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "";
			if (handletype == 0) {
				sql = "UPDATE hd_user SET balance = balance-?, sendmoney = sendmoney-? where id=?";
			} else if (handletype == 1) {
				sql = "UPDATE hd_user SET balance = balance+?, sendmoney = sendmoney+? where id=?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, CommUtil.toDouble(topupmoney));
			ps.setDouble(2, CommUtil.toDouble(sendmoney));
			ps.setInt(3, uid);
			ps.executeUpdate();
//			if (handletype == 0) {
//				sql = "UPDATE hd_user SET balance = balance-"+topupmoney+", sendmoney-"+sendmoney+" where id=" + uid;
//			} else if (handletype == 1) {
//				sql = "UPDATE hd_user SET balance = balance+"+topupmoney+", sendmoney+"+sendmoney+" where id=" + uid;
//			}
//			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
//			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void areaRefund(int aid,double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_area set total_online_earn=total_online_earn-?,now_online_earn=now_online_earn-?,equ_earn=equ_earn-? where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setDouble(2, money);
			ps.setDouble(3, money);
			ps.setInt(4, aid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void equRefund(String code,double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set total_online_earn=total_online_earn-?,now_online_earn=now_online_earn-? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setDouble(2, money);
			ps.setString(3, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void merAmountRefund(int merid,double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_meramount set total_online_earn=total_online_earn-?,now_online_earn=now_online_earn-? where merid=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setDouble(2, money);
			ps.setInt(3, merid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void merAmountAddMoney(int merid,double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_meramount set total_online_earn=total_online_earn+?,now_online_earn=now_online_earn+? where merid=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setDouble(1, money);
			ps.setDouble(2, money);
			ps.setInt(3, merid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 根据充电订单号查询用户是否为特约商户
	 * @param orderNum 充电订单号
	 * @return {@link Integer}
	 */
	public static int selectSubMerByOrderNum(Integer merId){
		Connection conn = null;
		int subMer = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT submer FROM hd_user WHERE id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				subMer = result.getInt("submer");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return subMer;
	}
	
	/**
	 * 根据商家的id查询微信特约商户的微信商户号
	 * @param merId 商家id
	 * @return {@link String}
	 */
	public static String selectSubMerConfig(Integer merId){
		Connection conn = null;
		String subMchId = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT sub_mchid AS subMchId FROM hd_submer WHERE uid =? LIMIT 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				subMchId = result.getString("subMchId");
			}
		} catch (Exception e) {
			System.out.println("这是商家的微信商户号:"+"====="+subMchId);
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return subMchId;
	}
	
	/**
	 * 根据商家的id查询微信特约商户的配置信息
	 * @param merId 商家id
	 * @return {@link String}
	 */
	public static User selectSubMerConfigByMerId(Integer merId){
		Connection conn = null;
		User subMer = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT uid,sub_appid AS subappid,appsecret FROM hd_submer WHERE uid=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, merId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				subMer = new User();
				subMer.setMerid(result.getInt("uid"));
				subMer.setSubAppId(result.getString("subappid"));
				subMer.setAppSecret(result.getString("appsecret"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return subMer;
	}
	
	/**
	 * 根据设备号查询特约商户的配置信息
	 * @param code 设备号
	 * @return {@link User}
	 */
	public static User selectSubMerData(String code){
		Connection conn = null;
		User subMer = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT uid,sub_appid AS subappid,appsecret "
					+ "FROM hd_submer s "
					+ "LEFT JOIN hd_user_equipment ue "
					+ "ON s.uid = ue.user_id "
					+ "WHERE ue.equipment_code=? "
					+ "LIMIT 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				subMer = new User();
				subMer.setMerid(result.getInt("uid"));
				subMer.setSubAppId(result.getString("subappid"));
				subMer.setAppSecret(result.getString("appsecret"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return subMer;
	};
}

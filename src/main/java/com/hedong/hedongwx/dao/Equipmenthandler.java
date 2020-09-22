package com.hedong.hedongwx.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.service.ActiveMqProducer;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DBUtils;
import com.hedong.hedongwx.utils.JedisUtils;

@Service
public class Equipmenthandler {
	
	@Autowired
	private ActiveMqProducer activeMqProducer;

	/**
	 * 获取所有软件版本为00的设备
	 * 
	 * @return
	 */
	public static List<String> getAllSoftVersionIs00() {
		List<String> codeList = new ArrayList<>();
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_equipment where state = 1 and softversionnum='00' and device_type = 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				codeList.add(result.getString("code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return codeList;
	}
	/**
	 * 获取所有设备类型
	 * 
	 * @return
	 */
	public static int getDeviceType(String code) {
		Connection conn = null;
		int deviceType = 1;
		try {
			conn = DBUtils.getConnection();
			String sql = "select device_type from hd_equipment where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				deviceType = result.getInt("device_type");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return deviceType;
	}
	
	/**
	 * 获取所有软件版本为00的设备
	 * 
	 * @return
	 */
	public static TemplateParent getTempPermit(String code) {
		Connection conn = null;
		TemplateParent templateParent = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select permit,common2 from hd_templateparent where id=(SELECT tempid from hd_equipment WHERE `code` = '" + code + "')";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				templateParent = new TemplateParent();
				templateParent.setPermit(result.getInt("permit"));
				templateParent.setCommon2(result.getInt("common2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return templateParent;
	}
	
	/**
	 * 获取模板rank
	 * 
	 * @return
	 */
	public static int getTempRank(String code) {
		Connection conn = null;
		int rank = 720;
		try {
			conn = DBUtils.getConnection();
			String sql = "select rank from hd_templateparent where id=(SELECT tempid from hd_equipment WHERE `code` = '" + code + "')";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				rank = result.getInt("rank");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return rank;
	}
	
	public static void redisDeciceStateAddOrRemove(int state,String code,String hardversion) {
		if (hardversion != null) {
			if (!"03".equals(hardversion) && !"04".equals(hardversion)) {
				if (code != null || !"".equals(code)) {
					Map<String, String> diviceOffline = JedisUtils.hgetAll("diviceOffline");
					if (diviceOffline == null || diviceOffline.isEmpty() || diviceOffline.size() == 0) {
						diviceOffline = new HashMap<String, String>();
						diviceOffline.put("wolf666", CommUtil.toDateTime());
					}
					if (state == 1) {
						String string = diviceOffline.get(code);
						if (string != null && !"".equals(string) && diviceOffline.containsKey(code)) {
							JedisUtils.hdel("diviceOffline", code);
							diviceOffline.remove(code);
							JedisUtils.hmset("diviceOffline", diviceOffline);
						}
					} else {
						diviceOffline.put(code, CommUtil.toDateTime());
						JedisUtils.hmset("diviceOffline", diviceOffline);
					}
				}
			}
		}
	}

	/**
	 * 通过设备号修改设备在线状态
	 * 
	 * @param state
	 * @param code
	 */
	public static void updateState(Byte state, String csq, String code) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set state=?,csq=? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setByte(1, state);
			ps.setString(2, csq);
			ps.setString(3, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 通过设备号修改实时ip
	 * 
	 * @param code
	 * @param ipaddr
	 */
	public void updateipaddr(String code, String ipaddr) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set ipaddr='" + ipaddr + "' where code='" + code + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	/**
	 * 通过设备号修改设备信号
	 * @param csq
	 * @param code
	 */
	public static void updateCsq(String csq, String code) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set csq='" + csq + "' where code='" + code + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * @param code
	 * @param ipaddr
	 * @param ccid
	 * @param csq
	 * @param state
	 * @param softversionnum
	 */
	public static void updateEquipment(String code,String ipaddr,String ccid,String csq,byte state,
			String softversionnum, String hardversionnum) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set ipaddr = ?,ccid = ?,csq = ?,state = ?,softversionnum = ?,hardversionnum=? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, ipaddr);
			ps.setString(2, ccid);
			ps.setString(3, csq);
			ps.setByte(4, state);
			ps.setString(5, softversionnum);
			ps.setString(6, hardversionnum);
			ps.setString(7, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 通过ip获取设备号
	 * @param ipaddr
	 * @return
	 */
	public static String queryCodeByIpaddr(String ipaddr) {
		Connection conn = null;
		String code = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select code from hd_equipment where ipaddr='" + ipaddr + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				code = result.getString("code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return code;
	}
	
	/**
	 * 通过ip获取设备号
	 * @param ipaddr
	 * @return
	 */
	public static Equipment queryEquipmentByIpaddr(String ipaddr) {
		Connection conn = null;
		Equipment equipment = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select code,hardversion from hd_equipment where ipaddr='" + ipaddr + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				equipment = new Equipment();
				equipment.setCode(result.getString("code"));
				equipment.setHardversion(result.getString("hardversion"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtils.close(conn);
		}
		return equipment;
	}

	/**
	 * 通过设备号获取商户id
	 * @param code
	 * @return
	 */
	public static Integer findUserEquipment(String code) {
		Connection conn = null;
		Integer uid = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select user_id from hd_user_equipment where equipment_code='" + code + "'";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				uid = result.getInt("user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return uid;
	}

	public static String queryEndInfo() {
		Connection conn = null;
		String code = "";
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_equipment order by code desc LIMIT 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();

			Equipment equipment = null;
			while (result.next()) {
				equipment = new Equipment();
				equipment.setCode(result.getString("code"));
				code = equipment.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return code;
	}

	public static String getEquipmentHardversion(String code) {
		Connection conn = null;
		String hardver = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select hardversion from hd_equipment where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			
			while (result.next()) {
				hardver = result.getString("hardversion");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return hardver;
	}
	
	public static Equipment getEquipment(String imei) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select code,imei,ccid,softversionnum,mainid,hardversion from hd_equipment where imei=" + imei;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();

			Equipment equipment = null;
			while (result.next()) {
				equipment = new Equipment();
				equipment.setCode(result.getString("code"));
				equipment.setImei(result.getString("imei"));
				equipment.setCcid(result.getString("ccid"));
				equipment.setSoftversionnum(result.getString("softversionnum"));
				equipment.setMainid(result.getString("mainid"));
				equipment.setHardversion(result.getString("hardversion"));
			}
			return equipment;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return null;
	}
	
	public static int getEquipmentByCode(String code) {
		Connection conn = null;
		int aid = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "select aid from hd_equipment where code=" + code;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			
			while (result.next()) {
				aid = result.getInt("aid");
			}
		} catch (Exception e) {
			aid = -1;
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return aid;
	}
	
	public static Equipment getEquipmentData(String code) {
		Connection conn = null;
		Equipment devicedata = new Equipment();
		try {
			conn = DBUtils.getConnection();
			String sql = "select e.*,a.address AS areaaddress,a.`name` AS areaname FROM hd_equipment e LEFT JOIN hd_area a ON e.aid = a.id where e.code=" + code;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				devicedata.setAid(result.getInt("aid"));
				devicedata.setTempid(result.getInt("tempid"));
				devicedata.setRemark(result.getString("remark"));
				devicedata.setAddress(result.getString("areaaddress"));
				devicedata.setName(result.getString("areaname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return devicedata;
	}
	
	public static TemplateParent getEquipmentTempData(Integer tempid) {
		Connection conn = null;
		TemplateParent temppare = new TemplateParent();
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT * FROM hd_templateparent where id = " + tempid;
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				temppare.setRemark(result.getString("remark"));
				temppare.setCommon1(result.getString("common1"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return temppare;
	}

	public List<Equipment> getList() {
		Connection conn = null;
		List<Equipment> list = new ArrayList<>();
		try {
			conn = DBUtils.getConnection();
			String sql = "select * from hd_equipment";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ResultSet result = ps.executeQuery();

			Equipment equipment = null;
			while (result.next()) {
				equipment = new Equipment();
				equipment.setCode(result.getString("code"));
				equipment.setImei(result.getString("imei"));
				equipment.setCcid(result.getString("ccid"));
				equipment.setSoftversionnum(result.getString("softversionnum"));
				list.add(equipment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return list;
	}

	/**
	 * 添加设备
	 * @param code 设备号
	 * @param ccid
	 * @param imei
	 * @param csq 信号
	 * @param ipaddr ip和端口
	 * @param hardversion 硬件版本
	 * @param hardversionnum 硬件版本号
	 * @param softversionnum 软件版本号
	 */
	public static void addEquipment(String code, String ccid, String imei, String csq, String ipaddr, String hardversion,
			String hardversionnum, String softversionnum) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_equipment (code,ccid,imei,csq,ipaddr,hardversion,hardversionnum,softversionnum,create_time,state) values (?,?,?,?,?,?,?,?,now(),?)";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setString(2, ccid);
			ps.setString(3, imei);
			ps.setString(4, csq);
			ps.setString(5, ipaddr);
			ps.setString(6, hardversion);
			ps.setString(7, hardversionnum);
			ps.setString(8, softversionnum);
			ps.setByte(9, (byte) 1);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	 /**
	  * 添加设备操作日志
	  * @param code 设备号
	  * @param sort 操作类型 1上下线  2绑定与解绑 3修改设备硬件版本  4小区修改',
	  * @param type 具体操作类型 1上线 2离(掉)线    1绑定 2解绑    1修改版本号 2修改模板  1绑定小区 2解绑小区',
	  * @param merid 所属商户id
	  * @param opeid 操作人id
	  * @param operate_time 操作时间
	  */
	public static void addCodeOperateLog(String code,int sort,int type,int merid,int opeid) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_codeoperatelog (code,sort,type,merid,opeid,operate_time) values (?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, sort);
			ps.setInt(3, type);
			ps.setInt(4, merid);
			ps.setInt(5, opeid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 添加设备操作日志
	 * @param money 
	 */
	public static void addRealchargerecord(int chargeid,int uid,int merid,String code,byte port,
			int chargetime,int surpluselec,int power,double portV,double portA, double money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_realchargerecord (chargeid,uid,merid,code,port,chargetime,surpluselec,power,port_v,port_a,money,createtime) values (?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, chargeid);
			ps.setInt(2, uid);
			ps.setInt(3, merid);
			ps.setString(4, code);
			ps.setInt(5, port);
			ps.setInt(6, chargetime);
			ps.setInt(7, surpluselec);
			ps.setInt(8, power);
			ps.setDouble(9, portV);
			ps.setDouble(10, portA);
			ps.setDouble(11, money);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 查询设备操作日志
	 */
	public static Realchargerecord queryRealchargerecord(int chargeid) {
		Connection conn = null;
		Realchargerecord realchargerecord = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select power,chargetime,surpluselec,createtime,money from hd_realchargerecord where chargeid=? order by id desc limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, chargeid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				realchargerecord = new Realchargerecord();
				realchargerecord.setChargetime(result.getInt("chargetime"));
				realchargerecord.setSurpluselec(result.getInt("surpluselec"));
				realchargerecord.setCreatetime(result.getTimestamp("createtime"));
				realchargerecord.setPower(result.getInt("power"));
				realchargerecord.setMoney(result.getDouble("money"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return realchargerecord;
	}
	
	/**
	 * 查询设备第一条有效功率
	 */
	public static short queryRealchargerecordFirstPower(int chargeid) {
		Connection conn = null;
		short power = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT power as power FROM hd_realchargerecord WHERE chargeid = ? and power != 0 limit 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, chargeid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				power = result.getShort("power");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return power;
	}
	
	/**
	 * 查询设备最大功率
	 */
	public static short queryRealchargerecordMaxPower(int chargeid) {
		Connection conn = null;
		short power = 0;
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT MAX(power) as power FROM hd_realchargerecord WHERE chargeid = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, chargeid);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				power = result.getShort("power");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return power;
	}
	
	public static CodeSystemParam getCodeSysParam(String code) {
		Connection conn = null;
		CodeSystemParam codeSystemParam = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select code,coin_min from hd_codesystemparam where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				codeSystemParam = new CodeSystemParam();
				codeSystemParam.setCode(result.getString("code"));
				codeSystemParam.setCoinMin(result.getInt("coin_min"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return codeSystemParam;
	}
	
	/**
	 * 添加设备系统参数
	 * @param code
	 * @param coin_min
	 * @param card_min
	 * @param coin_elec
	 * @param card_elec
	 * @param cst
	 * @param power_max_1
	 * @param power_max_2
	 * @param power_max_3
	 * @param power_max_4
	 * @param power_2_tim
	 * @param power_3_tim
	 * @param power_4_tim
	 * @param sp_rec_mon
	 * @param sp_full_empty
	 * @param full_power_min
	 * @param full_charge_time
	 * @param elec_time_first
	 */
	public static void addCodeSysParam(String code, int coin_min, int card_min, double coin_elec, double card_elec, double cst,
			int power_max_1, int power_max_2, int power_max_3, int power_max_4, int power_2_tim,
			int power_3_tim, int power_4_tim, int sp_rec_mon, int sp_full_empty, int full_power_min,
			int full_charge_time, int elec_time_first) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "insert into hd_codesystemparam (code, coin_min, card_min, coin_elec, card_elec, "
					+ "cst, power_max1, power_max2, power_max3, power_max4, power_tim2, power_tim3, "
					+ "power_tim4, sp_rec_mon, sp_full_empty, full_power_min, full_charge_time, "
					+ "elec_time_first, update_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ps.setInt(2, coin_min);
			ps.setInt(3, card_min);
			ps.setDouble(4, coin_elec);
			ps.setDouble(5, card_elec);
			ps.setDouble(6, cst);
			ps.setInt(7, power_max_1);
			ps.setInt(8, power_max_2);
			ps.setInt(9, power_max_3);
			ps.setInt(10, power_max_4);
			ps.setInt(11, power_2_tim);
			ps.setInt(12, power_3_tim);
			ps.setInt(13, power_4_tim);
			ps.setInt(14, sp_rec_mon);
			ps.setInt(15, sp_full_empty);
			ps.setInt(16, full_power_min);
			ps.setInt(17, full_charge_time);
			ps.setInt(18, elec_time_first);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 添加设备系统参数
	 * @param code
	 * @param coin_min
	 * @param card_min
	 * @param coin_elec
	 * @param card_elec
	 * @param cst
	 * @param power_max_1
	 * @param power_max_2
	 * @param power_max_3
	 * @param power_max_4
	 * @param power_2_tim
	 * @param power_3_tim
	 * @param power_4_tim
	 * @param sp_rec_mon
	 * @param sp_full_empty
	 * @param full_power_min
	 * @param full_charge_time
	 * @param elec_time_first
	 */
	public static void editCodeSysParam(String code, int coin_min, int card_min, double coin_elec, double card_elec, double cst,
			int power_max_1, int power_max_2, int power_max_3, int power_max_4, int power_2_tim,
			int power_3_tim, int power_4_tim, int sp_rec_mon, int sp_full_empty, int full_power_min,
			int full_charge_time, int elec_time_first) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_codesystemparam set coin_min=?, card_min=?, coin_elec=?, card_elec=?, "
					+ "cst=?, power_max1=?, power_max2=?, power_max3=?, power_max4=?, power_tim2=?, power_tim3=?, "
					+ "power_tim4=?, sp_rec_mon=?, sp_full_empty=?, full_power_min=?, full_charge_time=?, "
					+ "elec_time_first=?, update_time=now() where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, coin_min);
			ps.setInt(2, card_min);
			ps.setDouble(3, coin_elec);
			ps.setDouble(4, card_elec);
			ps.setDouble(5, cst);
			ps.setInt(6, power_max_1);
			ps.setInt(7, power_max_2);
			ps.setInt(8, power_max_3);
			ps.setInt(9, power_max_4);
			ps.setInt(10, power_2_tim);
			ps.setInt(11, power_3_tim);
			ps.setInt(12, power_4_tim);
			ps.setInt(13, sp_rec_mon);
			ps.setInt(14, sp_full_empty);
			ps.setInt(15, full_power_min);
			ps.setInt(16, full_charge_time);
			ps.setInt(17, elec_time_first);
			ps.setString(18, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static CodeSystemParam selectCodeSysParam(String code) {
		Connection conn = null;
		CodeSystemParam codeSystemParam = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select power_max1,power_max2,power_max3,power_max4,power_tim2,power_tim3,power_tim4 from hd_codesystemparam where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				codeSystemParam = new CodeSystemParam();
				codeSystemParam.setPowerMax1(result.getInt("power_max1"));
				codeSystemParam.setPowerMax2(result.getInt("power_max2"));
				codeSystemParam.setPowerMax3(result.getInt("power_max3"));
				codeSystemParam.setPowerMax4(result.getInt("power_max4"));
				codeSystemParam.setPowerTim2(result.getInt("power_tim2"));
				codeSystemParam.setPowerTim3(result.getInt("power_tim3"));
				codeSystemParam.setPowerTim4(result.getInt("power_tim4"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return codeSystemParam;
	}
	
	public static void updateEquAddr(String code,BigDecimal lon, BigDecimal lat) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set lon=?,lat=? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setBigDecimal(1, lon);
			ps.setBigDecimal(2, lat);
			ps.setString(3, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateEquCoinEarn(String code,int money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set total_coins_earn = total_coins_earn + ?,now_coins_earn = now_coins_earn + ? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, money);
			ps.setInt(2, money);
			ps.setString(3, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateAreaCoinEarn(int aid,int money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_area set total_coins_earn = total_coins_earn + ?,now_coins_earn = now_coins_earn + ? where id=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, money);
			ps.setInt(2, money);
			ps.setInt(3, aid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	public static void updateMerAmountCoinEarn(int merid,int money) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_meramount set total_coins_earn = total_coins_earn + ?,now_coins_earn = now_coins_earn + ? where merid=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, money);
			ps.setInt(2, money);
			ps.setInt(3, merid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static void updateEquMainInfo(String mainid,String mainType,String mainHardver,
			String mainSoftver,String code) {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "update hd_equipment set mainid = ?, main_type = ?,main_hardver = ?,"
					+ "main_softver = ? where code=?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, mainid);
			ps.setString(2, mainType);
			ps.setString(3, mainHardver);
			ps.setString(4, mainSoftver);
			ps.setString(5, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static List<TemplateSon> getV3TempsonList(String code) {
		Connection conn = null;
		List<TemplateSon> tempsonList = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select money,common1,common2 from hd_templateson where tempparid = (SELECT tempid from hd_equipment where code = ?) and "
					+ "type = 1";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			tempsonList = new ArrayList<>();
			while (result.next()) {
				TemplateSon templateSon = new TemplateSon();
				templateSon.setMoney(result.getDouble("money"));
				templateSon.setCommon1(result.getString("common1"));
				templateSon.setCommon2(result.getString("common2"));
				tempsonList.add(templateSon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return tempsonList;
	}
	
	public static Map<String, Object> acquireDeviceRelevaData(String code) {
		Connection conn = null;
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			conn = DBUtils.getConnection();
			String sql = "SELECT e.*, u.id AS merid, a.address AS areaaddress, a.`name` AS areaname, a.tempid AS waltempid, "
					+ "a.tempid2 AS onltempid, u.username AS mernick, u.realname AS realname, u.openid, u.phone_num AS merphone, "
					+ "u.servephone AS merservephone, u.payhint, u.submer "
					+ "FROM hd_equipment e LEFT JOIN hd_user_equipment ue ON e.`code` = ue.equipment_code LEFT JOIN hd_area a ON "
					+ "e.aid = a.id LEFT JOIN hd_user u ON ue.user_id = u.id WHERE e.`code` = ?";
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				datamap.put("devicenum", result.getString("code"));
				datamap.put("hardversion", result.getString("hardversion"));
				datamap.put("devicename", result.getString("remark"));
				datamap.put("aid", result.getInt("aid"));
				datamap.put("tempid", result.getInt("tempid"));
				datamap.put("expirationtime", result.getDate("expiration_time"));
				//商户信息
				datamap.put("merid", result.getInt("merid"));
				datamap.put("mernick", result.getString("mernick"));
				datamap.put("realname", result.getString("realname"));
				datamap.put("openid", result.getString("openid"));
				datamap.put("merphone", result.getString("merphone"));
				datamap.put("merservephone", result.getString("merservephone"));
				datamap.put("payhint", result.getInt("payhint"));
				datamap.put("submer", result.getInt("submer"));
				
				datamap.put("areaname", result.getString("areaname"));
				datamap.put("areaaddress", result.getString("areaaddress"));
				datamap.put("waltempid", result.getInt("waltempid"));
				datamap.put("onltempid", result.getInt("onltempid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
		return datamap;
	}
	/**
	 * @method_name: redactCodeSysParam
	 * @Description: 
	 * @param code
	 * @param type
	 * @param value
	 * @Author: origin  创建时间:2020年9月12日 下午5:26:36
	 * @common:   
	 */
	public static void redactCodeSysParam(String code, Integer type, Integer value) {
		Connection conn = null;
		try {//type 1:报警温度   2:报警烟感     3:报警总功率
			conn = DBUtils.getConnection();
			String sql = "";
			if (type == 1) {
				sql = "UPDATE hd_codesystemparam SET hot_doorsill=?, update_time=now() where code=?";
			} else if (type == 2) {
				sql = "UPDATE hd_codesystemparam SET smoke_doorsill=?, update_time=now() where code=?";
			} else if (type == 3) {
				sql = "UPDATE hd_codesystemparam SET power_total=?, update_time=now() where code=?";
			}
			PreparedStatement ps = DBUtils.getPreparedStatement(conn, sql);
			ps.setInt(1, value);
			ps.setString(2, code);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn);
		}
	}
	
	
	
	
	
}
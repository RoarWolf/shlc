package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.config.WeChatConfig;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.CommonMethodService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.LostOrderService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.SystemSetService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;
import com.hedong.hedongwx.web.controller.WxPayController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description: 
 * @Author: origin  创建时间:2020年7月25日 下午3:52:06
 * @common:   
 */
@Service
public class CommonMethodServiceImpl implements CommonMethodService{
	
//===========================================================================================================	

	private final Logger logger = LoggerFactory.getLogger(CommonMethodServiceImpl.class);
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SystemSetService systemSetService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private BasicsService basicsService;
	@Autowired
	private LostOrderService lostOrderService;
	
	

	/**
	 * /wxpay/doWeChatRefund?id=&ordersource=&refundtype=&password=
	 * @method_name: doWeChatRefund
	 * @Description: 退款统一测试
	 * @param orderid：订单号    ordersource：订单来源 [以交易记录为准]   password：退款类型   refundtype：密码
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:08:15
	 * @common:
	 */
	@Override
	public Map<String, Object> doWeChatRefund(Integer orderid, Integer ordersource, Integer refundtype) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
//			/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付  */
//			private Integer paysource;
//			/** 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值  12银联app*/
//			private Integer paytype;
//			/** 订单状态1、正常2、退款 */
//			private Integer status;
			
			if (ordersource == 1) {//充电
				chargeRefund(orderid, refundtype);
			} else if (ordersource == 2) {//脉冲
				
			} else if (ordersource == 3) {//离线充值机
				System.out.println("ORIGIN： 离线充值机操作    id：  "+orderid+"  "+ refundtype);
				result = offlineCardRefund(orderid, refundtype);
			} else if (ordersource == 4) {//钱包
				
			} else if (ordersource == 5) {//在线卡
				
			} else if (ordersource == 6) {//包月
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Description： 充电退费处理数据
	 * @return 
	 */
	public Map<String, Object> chargeRefund(Integer orderid, Integer refundtype){
		Map<String, Object> result = new HashMap<String, Object>();
		ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(orderid);
		//修改本地数据
		orderid = CommUtil.toInteger(orderid);
		String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
		String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
		String port = CommUtil.toString(chargeRecord.getPort());
		Integer dealid = CommUtil.toInteger(chargeRecord.getMerchantid());
		Integer uid = CommUtil.toInteger(chargeRecord.getUid());
		Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
		Integer orderpaytype = CommUtil.toInteger(chargeRecord.getPaytype());
		//Double ordertype = CommUtil.toDouble(chargeRecord.getPaytype());
		Double orderstatus = CommUtil.toDouble(chargeRecord.getNumber());
		Integer resultinfo = CommUtil.toInteger(chargeRecord.getResultinfo());
		String beginTime = CommUtil.toDateTime(chargeRecord.getBegintime());
		String endTime = CommUtil.toDateTime(chargeRecord.getEndtime());
		String endTimes = CommUtil.toDateTime();
		if(endTime==null) endTime = endTimes;
		
		Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
		Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
		Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
		String devicename = CommUtil.toString(devicedata.getRemark());
		String areaaddress = CommUtil.toString(devicedata.getAddress());
		systemSetService.sendMmessChargeEnd(ordernum, orderid, resultinfo, 1, dealid, uid, paymoney, beginTime,
				devicenum, port, devicename, deviceaid, areaaddress, devicetempid);
		
		Integer merpaysource = MerchantDetail.CHARGESOURCE;
		Integer merpaytype = MerchantDetail.WEIXINPAY;
		Integer merpaystatus = MerchantDetail.REFUND;
		
		//校验商户与合伙金额是否充足
		Map<String, Object>  resultIfRich = refundTradIfRich(ordernum);
		String refund_code = CommUtil.toString(resultIfRich.get("refund_code"));
		String refund_message = CommUtil.toString(resultIfRich.get("refund_message"));
		if("200".equals(refund_code)){//可以退款
			Integer merid = CommUtil.toInteger(resultIfRich.get("merid"));
			String submer = CommUtil.toString(resultIfRich.get("submer"));
			
			String subMchId = null;
			String subAppid = null;
			if(submer.equals(1)){
				Map<String, Object> configData = userService.selectSubMerConfigById(merid);
				subMchId = CommUtil.toString(configData.get("submchid"));
				subAppid = CommUtil.toString(configData.get("subappid"));
				logger.info("C我是特约商户的参数:"+subMchId+"============"+subAppid);
			}
			Map<String, Object> datamap = WeChatPayRefund( ordernum, subAppid, subMchId);
			String wxrefundcode = CommUtil.toString(datamap.get("code"));
			String wxrefundmessage = CommUtil.toString(datamap.get("message"));
			wxrefundcode = wxrefundcode == null ? "200" : wxrefundcode;
			if("200".equals(wxrefundcode)){//退款成功，处理本地数据
				chargeRecordService.updateNumberById( null, 1, orderid, null, null, paymoney, endTime, CommUtil.toDateTime());
				
				if (orderstatus != 1) {
					dealerEarningsDispose(orderid, devicenum, resultIfRich, merpaysource, merpaytype, merpaystatus);
				}
				result.put("result_code", 200);
				result.put("result_message", "退款数据处理完成");
			}else{
				result.put("result_code", 40001);
				result.put("result_message", wxrefundmessage);
			}
		}else{//不可以退款
			result.put("result_code", 40001);
			result.put("result_message", refund_message);
		}
		return result;
	}
	
	
	public Map<String, Object> offlineCardRefund(Integer id, Integer refundtype){
		Map<String, Object> result = new HashMap<String, Object>();
		OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
		//修改本地数据
		Integer orderid = CommUtil.toInteger(offlineCard.getId());
		String ordernum = CommUtil.toString(offlineCard.getOrdernum());
		String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
		Integer dealid = CommUtil.toInteger(offlineCard.getMerchantid());
		Integer uid = CommUtil.toInteger(offlineCard.getUid());
		Double paymoney = CommUtil.toDouble(offlineCard.getChargemoney());
		Double accountmoney = CommUtil.toDouble(offlineCard.getAccountmoney());
		Double orderpaytype = CommUtil.toDouble(offlineCard.getPaytype());
		Double ordertype = CommUtil.toDouble(offlineCard.getHandletype());
		Double orderstatus = CommUtil.toDouble(offlineCard.getStatus());
		Integer merpaysource = MerchantDetail.OFFLINESOURCE;
		Integer merpaytype = MerchantDetail.WEIXINPAY;
		Integer merpaystatus = MerchantDetail.REFUND;
		//校验商户与合伙金额是否充足
		Map<String, Object>  resultIfRich = refundTradIfRich(ordernum);
		String refund_code = CommUtil.toString(resultIfRich.get("refund_code"));
		String refund_message = CommUtil.toString(resultIfRich.get("refund_message"));
		if("200".equals(refund_code)){//可以退款
			Integer merid = CommUtil.toInteger(resultIfRich.get("merid"));
			String submer = CommUtil.toString(resultIfRich.get("submer"));
			
			String subMchId = null;
			String subAppid = null;
			if(submer.equals(1)){
				Map<String, Object> configData = userService.selectSubMerConfigById(merid);
				subMchId = CommUtil.toString(configData.get("submchid"));
				subAppid = CommUtil.toString(configData.get("subappid"));
				logger.info("C我是特约商户的参数:"+subMchId+"============"+subAppid);
			}
//			Map<String, Object> datamap = new HashMap<String, Object>();
			Map<String, Object> datamap = WeChatPayRefund( ordernum, subAppid, subMchId);
			String wxrefundcode = CommUtil.toString(datamap.get("code"));
			String wxrefundmessage = CommUtil.toString(datamap.get("message"));
			wxrefundcode = wxrefundcode == null ? "200" : wxrefundcode;
			if("200".equals(wxrefundcode)){//退款成功，处理本地数据
				offlineCardService.offlineCardRefund(3, id);
				if (ordertype != 3) {
					dealerEarningsDispose(orderid, devicenum, resultIfRich, merpaysource, merpaytype, merpaystatus);
				}
				result.put("result_code", 200);
				result.put("result_message", "退款数据处理完成");
			}else{
				result.put("result_code", 40001);
				result.put("result_message", wxrefundmessage);
			}
		}else{//不可以退款
			result.put("result_code", 40001);
			result.put("result_message", refund_message);
		}
		return result;
	}
	
	/**
	 * @method_name: dealerEarningsDispose
	 * @Description: 
	 * @param orderid
	 * @param devicenum
	 * @param merpaystatus 
	 * @param merpaytype 
	 * @param merpaysource 
	 * @param resultIfRich
	 * @Author: origin  创建时间:2020年8月27日 下午6:07:11
	 * @common:   
	 */
	private void dealerEarningsDispose(Integer orderid, String devicenum, Map<String, Object> trademap, 
			Integer merpaysource, Integer merpaytype, Integer merpaystatus) {
		Integer tradid = CommUtil.toInteger(trademap.get("id"));
		Integer merid = CommUtil.toInteger(trademap.get("merid"));
		Integer uid = CommUtil.toInteger(trademap.get("uid"));
		Double paymoney = CommUtil.toDouble(trademap.get("money"));
		Double mermoney = CommUtil.toDouble(trademap.get("mermoney"));
		Double partmoney = CommUtil.toDouble(trademap.get("manmoney"));
		Integer paysource = CommUtil.toInteger(trademap.get("paysource"));
		Integer paytype = CommUtil.toInteger(trademap.get("paytype"));
		Integer paystatus = CommUtil.toInteger(trademap.get("status"));
		String ordernum = CommUtil.toString(trademap.get("ordernum"));
		String hardver = CommUtil.toString(trademap.get("hardver"));
		String comment = CommUtil.toString(trademap.get("comment"));
		String dealernick = CommUtil.toString(trademap.get("dealernick"));
		String dealername = CommUtil.toString(trademap.get("dealername"));
		String dealerphone = CommUtil.toString(trademap.get("dealerphone"));
		String servephone = CommUtil.toString(trademap.get("servephone"));
		Double dealerearnings = CommUtil.toDouble(trademap.get("dealerearnings"));
		Integer submer = CommUtil.toInteger(trademap.get("submer"));
		String touristnick = CommUtil.toString(trademap.get("touristnick"));
		String touristname = CommUtil.toString(trademap.get("touristname"));
		String touristhone = CommUtil.toString(trademap.get("touristhone"));
		Date time = new Date();
		try {
			Integer manid = 0;
			tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, 
					partmoney, devicenum, paysource, paytype, 2, hardver, comment);
			// 更新设备收益
			equipmentService.updateEquEarn(devicenum, paymoney, 0);
			Equipment equipment = equipmentService.getEquipmentById(devicenum);
			Integer aid = CommUtil.toInteger(equipment.getAid());
			// 更新小区收益
			if (aid != 0) areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
			
			// 普通商家修改退费数据
			if(submer.equals(0)){
				//充电退费数据
				dealerIncomeRefund(comment, merid, paymoney, ordernum, time, merpaysource, merpaytype, merpaystatus);
			}else{
				// 特约商户修改退费数据
				User subMer = userService.selectUserById(merid);
				userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
				merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
			}
			
//			String phone = getTempPhone(incoins.getEquipmentnum(), incoins.getMerchantid(), 2);
//			Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
//			Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//			Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//			String devicename = CommUtil.toString(devicedata.getRemark());
//			String areaaddress = CommUtil.toString(devicedata.getAddress());
//			String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//			systemSetService.sendMmessChargeEnd(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
//					devicenum, port, devicename, deviceaid, areaaddress, servicphone);
//			
			
//			String phone = getTempPhone(devicenum, merid, 0);
//			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
//			returnMsgTemp(uid, phone, devicenum, ordernum, urltem, strtime, paymoney);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
	}

	/**
	 * @method_name: dealerEarningsDispose
	 * @Description: 
	 * @param orderid:订单id [详情推送时使用]
	 * @param devicenum:设备号 [更新设备收益、小区收益]
	 * @param merid:商户id
	 * @param paysource
	 * @param paytype
	 * @param paystatus
	 * @param traderecord
	 * @Author: origin  创建时间:2020年8月27日 下午5:56:02
	 * @common:
	 */
	public void dealerEarningsDispose(Integer orderid, String devicenum, Integer merid,
			   Integer paysource, Integer paytype, Integer paystatus, TradeRecord traderecord) {
			Date time = new Date();
			String strtime = CommUtil.toDateTime(time);
			Integer uid = CommUtil.toInteger(traderecord.getUid());
			Integer merId = CommUtil.toInteger(traderecord.getMerid());
			Double paymoney = CommUtil.toDouble(traderecord.getMoney());
			String ordernum = CommUtil.toString(traderecord.getOrdernum());
			Integer traPaysource = CommUtil.toInteger(traderecord.getPaysource()); 
			Integer traPaytype = CommUtil.toInteger(traderecord.getPaytype());
			Integer traStatus = 2;
			String devicehard = null;
			try {
				// 更新设备收益
				equipmentService.updateEquEarn(devicenum, paymoney, 0);
				String comment = traderecord.getComment();
				try {
					Equipment equipment = equipmentService.getEquipmentById(devicenum);
					devicehard = CommUtil.toString(equipment.getHardversion());
					Integer aid = CommUtil.toInteger(equipment.getAid());
					// 更新小区收益
					if (aid != null && aid != 0) {
						areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
					} 
					User subMer = userService.selectUserById(merId);
					// 普通商家修改退费数据
					if(subMer != null && subMer.getSubMer().equals(0)){
						//充电退费数据
						dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
					}else{
						// 特约商户修改退费数据
						userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
						merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
					}
				} catch (Exception e) {
					logger.warn("小区修改余额错误===" + e.getMessage());
				}
				Double mermoney = traderecord.getMermoney();
				Double partmoney = traderecord.getManmoney();
				Integer manid = 0;
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, traPaysource, traPaytype, traStatus, devicehard, comment);
//				String phone = getTempPhone(incoins.getEquipmentnum(), incoins.getMerchantid(), 2);
//				Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
//				Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//				Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//				String devicename = CommUtil.toString(devicedata.getRemark());
//				String areaaddress = CommUtil.toString(devicedata.getAddress());
//				String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//				systemSetService.sendMmessChargeEnd(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
//						devicenum, port, devicename, deviceaid, areaaddress, servicphone);
//				
				
//				String phone = getTempPhone(devicenum, merid, 0);
//				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
//				returnMsgTemp(uid, phone, devicenum, ordernum, urltem, strtime, paymoney);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(CommUtil.getExceptInfo(e));
			}
		}
		
//	}
	

	
	/**
	 * 根据id查询充电信息
	 * @param refundState 充电类型:
	 * @param id 充电id
	 * @return
	 */
	public Map<String, Object> doRefundMoney(Integer refundState, Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		String out_trade_no = null;
		// 扫码充电记录
//		if (refundState == 1) {
//			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
//			
//		// 离线卡记录
//		} else if (refundState == 2) {
//			OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
//			
//		// 投币充电记录
//		} else if (refundState == 3) {
//			InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
//		// 钱包充值记录
//		} else if (refundState == 4) {
//			Money money = moneyService.payMoneyinfo(id);
//		// 在线充电记录
//		} else if (refundState == 5) {
//			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
//			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
//		// 包月充电记录
//		} else if (refundState == 6) {
//			PackageMonthRecord monthRecord = userService.selectMonthRecordById(id);
//			PackageMonth packageMonth = userService.selectPackageMonthByUid(monthRecord.getUid());
//		}
//		map.put("merid", merid);
//		map.put("check", check);
//		map.put("total_fee", total_fee);
//		map.put("out_trade_no", out_trade_no);
		return map;
	}
	
	
	/**
	 * @method_name: refundTradSelect
	 * @Description: 根据订单号获取交易信息，判断是否可以退款【校验商户与合伙人金额是否充足，满足退费要求】
	 * @param tradeOrder:交易订单
	 * @return
	 * @Author: origin  创建时间:2020年8月26日 下午6:50:22
	 * @common:
	 */
	public Map<String, Object> refundTradIfRich(String tradeOrder){
		List<Map<String, Object>> tradelist = tradeRecordService.refundTradeInquire(tradeOrder);
		Integer tradesize = tradelist.size();
		/** tradesize 0：交易订单不存在(该订单未付款或丢失)  1:正常订单(判断)  大于1:说明全额退款或者部分退款过  */
		Map<String, Object> trademap = new HashMap<String, Object>();
		Integer refund_status = 1;
		Double refund_totalmoney = 0.00;
		String message = "";
		if(tradesize>1){
			for(Map<String, Object> item : tradelist){
				Integer status = CommUtil.toInteger(item.get("status"));
				if(status.equals(1)){
					trademap = item;
				}else if(status.equals(2)){//已退过款
					refund_status = 2;
					refund_totalmoney = CommUtil.toDouble(item.get("money"));
					message = "该订单已全额退款过。";
					break;
				}else{//其他
					refund_status = 4;
					message = "存在未知状态的订单信息，联系管理员处理。";
				}
			}
		}else if(tradesize==1){
			trademap = tradelist.get(0);
		}else{
			refund_status = 5;
			message = "订单未查找到，不存在。";
		}
		trademap.put("refund_status", refund_status);
		trademap.put("refund_totalmoney", refund_totalmoney);
		trademap.put("refund_message", message);
		
		if(refund_status.equals(1)){//正常，可以退费     判断退款金额与商户账户金额对比，或者退款金额与用户钱包金额对比
			Double money = CommUtil.toDouble(trademap.get("money"));
			Double mermoney = CommUtil.toDouble(trademap.get("mermoney"));
			String comment = CommUtil.toString(trademap.get("comment"));
			Double dealerearnings = CommUtil.toDouble(trademap.get("dealerearnings"));
			boolean rich = checkMoneyIfRich( money, mermoney, dealerearnings, comment);
			if(rich){
				trademap.put("refund_code", 200);
				trademap.put("refund_message", "商户金额充足");
			}else{
				trademap.put("refund_code", 3002);
				trademap.put("refund_message", "商户或合伙人余额不足，无法退费");
			}
			return trademap;
		}else{//订单不存在或者订单已退过款
			trademap.put("refund_code", 3003);
			return trademap;
		}
	}
	
	/**
	 * @method_name: checkMoneyIfRich
	 * @Description: 校验判断商户与合伙人金额是否充足
	 * @param money
	 * @param mermoney
	 * @param dealerearnings
	 * @param comment
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:41:30
	 * @common:
	 */
	public boolean checkMoneyIfRich( Double money, Double mermoney, Double dealerearnings, 
			String comment) {
		Boolean fsal = true;
		if(comment.contains("partid")){//存在合伙人
			JSONArray jsona = JSONArray.fromObject(comment);
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject item = jsona.getJSONObject(i);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				if(partid!=0){//合伙人信息
					User partUser = userService.selectUserById(partid);
					if (partUser.getEarnings() < partmoney){
						fsal = false;
						break;
					}
				}else{//商户信息
					if(dealerearnings < partmoney){
						fsal = false;
						break;
					}
				}
			}
		}else{//不存在合伙人
			if(dealerearnings < money) fsal = false;
		}
		return fsal;
	}
	
//	/**
//	 * @method_name: checkMoneyIfRich
//	 * @Description: 
//	 * @param merid
//	 * @param money
//	 * @param mermoney
//	 * @param dealerearnings
//	 * @param comment
//	 * @Author: origin  创建时间:2020年8月27日 上午11:47:01
//	 * @common:   
//	 */
//	public boolean checkMoneyIfRich(Integer merid, Double dealerearnings, String comment) {
//		JSONArray jsona = JSONArray.fromObject(comment);
//		Boolean fsal = true;
//		for (int i = 0; i < jsona.size(); i++) {
//			JSONObject item = jsona.getJSONObject(i);
//			Integer partid = CommUtil.toInteger(item.get("partid"));
//			Double partmoney = CommUtil.toDouble(item.get("money"));
//			if(partid!=0){//合伙人信息
//				User partUser = userService.selectUserById(partid);
//				if (partUser.getEarnings() < partmoney){
//					fsal = false;
//					break;
//				}
//			}else{//商户信息
//				if(dealerearnings < partmoney){
//					fsal = false;
//					break;
//				}
//			}
//		}
//		return fsal;
//	}
//	
//	
//	public boolean checkMoneyIfRich2( String comment) {
//		Boolean fsal = true;
//		JSONArray jsona = JSONArray.fromObject(comment);
//		for (int i = 0; i < jsona.size(); i++) {
//			JSONObject item = jsona.getJSONObject(i);
//			Integer partid = CommUtil.toInteger(item.get("partid"));
//			Integer mercid = CommUtil.toInteger(item.get("merid"));
//			Double partmoney = CommUtil.toDouble(item.get("money"));
//			if(partid!=0){//合伙人信息
//				User partUser = userService.selectUserById(partid);
//				if (partUser.getEarnings() >= partmoney) {
//					fsal = true;
//				} else {
//					fsal =  false;
//					break;
//				}
//			}else{//商户信息
//				User merUser = userService.selectUserById(mercid);
//				if (merUser.getEarnings() >= partmoney) {
//					fsal = true;
//				} else {
//					fsal = false;
//					break;
//				}
//			}
//		}
//		return fsal;
//	}
//	
//	
//	public boolean checkMoneyIfRich3(Integer merid, Double money, Double mermoney, 
//			Double dealerearnings, String comment) {
//		if(dealerearnings<mermoney){//直接判断商户金额信息
//			return false;
//		}else{
//			Boolean fsal = true;
//			if(money>mermoney){//消费金额大于商户收入金额，存在合伙人分成
//				JSONArray jsona = JSONArray.fromObject(comment);
//				for (int i = 0; i < jsona.size(); i++) {
//					JSONObject item = jsona.getJSONObject(i);
//					Integer partid = CommUtil.toInteger(item.get("partid"));
//					Double partmoney = CommUtil.toDouble(item.get("money"));
//					if(partid!=0){//合伙人信息
//						User partUser = userService.selectUserById(partid);
//						if (partUser.getEarnings() < partmoney){
//							fsal = false;
//							break;
//						}
//					}
//				}
//			}
//			return fsal;
//			
//		}
//	}
//	
//	
//	public boolean checkMoneyIfRich4(Integer merid, Double dealerearnings, String comment) {
//		JSONArray jsona = JSONArray.fromObject(comment);
//		Boolean fsal = true;
//		for (int i = 0; i < jsona.size(); i++) {
//			JSONObject item = jsona.getJSONObject(i);
//			Integer partid = CommUtil.toInteger(item.get("partid"));
//			Double partmoney = CommUtil.toDouble(item.get("money"));
//			if(partid!=0){//合伙人信息
//				User partUser = userService.selectUserById(partid);
//				if (partUser.getEarnings() < partmoney){
//					fsal = false;
//					break;
//				}
//			}else{//商户信息
//				if(dealerearnings < partmoney){
//					fsal = false;
//					break;
//				}
//			}
//		}
//		return fsal;
//	}
//
//
//	
//	public boolean checkMoneyIfRich6(Integer merid, Double money, Double mermoney, 
//			Double dealerearnings, String comment) {
//		JSONArray jsona = JSONArray.fromObject(comment);
//		Boolean fsal = true;
//		StringBuffer partnerlist = new StringBuffer(merid);
//		List<Map<String, String>> partnerinfo = new ArrayList<Map<String,String>>();
//		for (int i = 0; i < jsona.size(); i++) {
//			JSONObject item = jsona.getJSONObject(i);
//			Map<String, String> datainfo = CommUtil.toHashMap(item);
//			partnerinfo.add(datainfo);
//			Integer partid = CommUtil.toInteger(item.get("partid"));
//			Integer mercid = CommUtil.toInteger(item.get("merid"));
//			//合伙人信息
//			if(partid!=0) partnerlist.append(partid);
//		}
//		//User partUser = userService.selectUserById(partnerlist.toString());
//		List<Map<String, String>> listuser = new ArrayList<Map<String,String>>();
//		for(Map<String, String> item : listuser){
//			Double merchearnings = CommUtil.toDouble(item.get("earnings"));
//			Integer merchid = CommUtil.toInteger(item.get("id"));
//			for(Map<String, String> objvalue : partnerinfo){
//				Integer partid = CommUtil.toInteger(objvalue.get("partid"));
//				Integer mercid = CommUtil.toInteger(objvalue.get("merid"));
//				String percent = CommUtil.toString(item.get("percent"));
//				Double partmoney = CommUtil.toDouble(item.get("money"));
//				Integer dealid = partid == 0 ? mercid : partid;
//				if(dealid.equals(merchid)){
//					if(merchearnings<partmoney){
//						fsal = false;
//					}
//				}
//				
//			}
//		}
//		return fsal;
//	}
//
//	/**
//	 * @method_name: refundTradSelect
//	 * @Description: 根据订单号获取交易信息，判断是否可以退款
//	 * @param tradeOrder
//	 * @return
//	 * @Author: origin  创建时间:2020年8月26日 下午6:50:22
//	 * @common:
//	 */
//	public Map<String, Object> refundTradSelect(String tradeOrder, String a){
//		List<Map<String, Object>> tradelist = tradeRecordService.refundTradeInquire(tradeOrder);
//		Integer tradesize = tradelist.size();
//		/** tradesize 0：交易订单不存在(该订单未付款或丢失)  1:正常订单(判断)  大于1:说明全额退款或者部分退款过  */
//		Map<String, Object> trademap = null;
//		Integer refund_status = 1;
//		Double refund_totalmoney = 0.00;
//		if(tradesize>1){
//			for(Map<String, Object> item : tradelist){
//				Integer status = CommUtil.toInteger(item.get("status"));
//				Double money = CommUtil.toDouble(item.get("money"));
//				if(status.equals(1)){
//					trademap = item;
//				}else if(status.equals(2)){//已全额退过款
//					refund_status = 2;
//					break;
//				}else if(status.equals(3)){//部分退款
//					refund_totalmoney = refund_totalmoney + money;
//					refund_status = 3;
//				}else{//未知查询
//					refund_status = 4;
//					break;
//				}
//			}
//		}else if(tradesize==1){
//			trademap = tradelist.get(0);
//		}else{
//			trademap = new HashMap<String, Object>();
//			refund_status = 5;
//		}
//		trademap.put("refund_status", refund_status);
//		trademap.put("refund_totalmoney", refund_totalmoney);
//
//		Integer tradid = CommUtil.toInteger(trademap.get("id"));
//		Integer merid = CommUtil.toInteger(trademap.get("merid"));
//		Integer uid = CommUtil.toInteger(trademap.get("uid"));
//		Double money = CommUtil.toDouble(trademap.get("money"));
//		Double mermoney = CommUtil.toDouble(trademap.get("mermoney"));
//		Double manmoney = CommUtil.toDouble(trademap.get("manmoney"));
//		Integer paysource = CommUtil.toInteger(trademap.get("paysource"));
//		Integer paytype = CommUtil.toInteger(trademap.get("paytype"));
//		Integer status = CommUtil.toInteger(trademap.get("status"));
//		String ordernum = CommUtil.toString(trademap.get("ordernum"));
//		String code = CommUtil.toString(trademap.get("code"));
//		String hardver = CommUtil.toString(trademap.get("hardver"));
//		String comment = CommUtil.toString(trademap.get("comment"));
//		String dealernick = CommUtil.toString(trademap.get("dealernick"));
//		String dealername = CommUtil.toString(trademap.get("dealername"));
//		String dealerphone = CommUtil.toString(trademap.get("dealerphone"));
//		String servephone = CommUtil.toString(trademap.get("servephone"));
//		String submer = CommUtil.toString(trademap.get("submer"));
//		String touristnick = CommUtil.toString(trademap.get("touristnick"));
//		String touristname = CommUtil.toString(trademap.get("touristname"));
//		String touristhone = CommUtil.toString(trademap.get("touristhone"));
//		/*
//		id,merid,uid,manid,ordernum,money,mermoney,manmoney,code,
//  `paysource` int(255) NOT NULL COMMENT '支付来源 1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包',
//  `paytype` '支付方式： 1:钱包 2:微信 3:支付宝 4:投币 5:离线卡 6:在线卡',
//  `status`'订单状态：1、正常2、退款',
//  `hardver`  '设备硬件版本号',
//  `create_time`  '记录创建时间',
//  `comment`
//		mu.username AS dealernick,mu.realname AS dealername,mu.phone_num AS dealerphone,mu.servephone,mu.submer,
//			 u.username AS touristnick, u.realname AS touristname,u.phone_num AS touristhone 
//		 */
//		return null;
//		
//	}
	
	/**
	 * @method_name: getWeChatConfig
	 * @Description: 
	 * @param appid：服务商的APPID[公众账号ID]  mch_id：商户号    sub_appid：子商户公众账号ID  sub_mch_id：子商户号
	 * @return
	 * @Author: origin  创建时间:2020年8月24日 上午11:44:46
	 * @common:
	 */
	public SortedMap<String, String> getWeChatConfig(String appid, String mch_id, String sub_appid, 
			String sub_mch_id){
		SortedMap<String, String> params = new TreeMap<>();
		//if(CommUtil.trimToEmpty(submchid).equals(""))
		if(appid==null) appid = WeiXinConfigParam.FUWUAPPID;
		if(mch_id==null) mch_id = WeiXinConfigParam.MCHID;
		if(sub_mch_id==null) sub_mch_id = WeiXinConfigParam.SUBMCHID;
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		if(sub_appid!=null) params.put("sub_appid", sub_appid);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		return params;
	}
	
	
	/**
	 * @method_name: inquireWeChatOrder 
	 * @Description: 根据订单号从微信上查询微信订单信息
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> inquireWeChatOrder(String ordernum, String traordernum, String subappid,
			String submchid){
		try {
			//if(CommUtil.trimToEmpty(subappid).equals("")) subappid = WeiXinConfigParam.SUBMCHID;
			SortedMap<String, String> params = getWeChatConfig(null, null, subappid, submchid);
			if(!CommUtil.trimToEmpty(ordernum).equals("")) params.put("out_trade_no", ordernum);
			if(!CommUtil.trimToEmpty(traordernum).equals("")) params.put("transaction_id", traordernum);
			String sign = HttpRequest.createSign("UTF-8", params);//生成签名并放入map集合里
			params.put("sign", sign);
			String url = WeChatConfig.WXJSAPI_ORDERQUERY;
			String canshu = HttpRequest.getRequestXml(params);
			String result = HttpRequest.sendPost(url, canshu);
			Map<String, String> resultMap = XMLUtil.doXMLParse(result);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
	}
	
	
	/**
	 * @method_name: WeChatPayRefund
	 * @Description: 根据订单号，全额退款该笔微信订单
	 * @param out_trade_no：订单号   subappid   submchid
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:44:55
	 * @common:
	 */
	public Map<String, Object> WeChatPayRefund(String out_trade_no, String subappid, String submchid) {
		Map<String, Object> datamap = new HashMap<>();
		try {
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			// 配置商户退款信息
			//查询交易订单是否存在
			Map<String, String> resultMap = inquireWeChatOrder(out_trade_no, null, subappid, submchid);
			// 退款后的业务处理
	//		logger.info("F退款的参数"+"======================"+resultMap);
			String returncode = CommUtil.trimToEmpty(resultMap.get("return_code"));
			String tradestate = CommUtil.trimToEmpty(resultMap.get("trade_state"));
			//if (returncode.equalsIgnoreCase("SUCCESS")) {//表示该订单存在);
			if (returncode.equals("SUCCESS")) {//表示该订单存在
				String outtradeno = "t"+ out_trade_no;
				String total_fee = CommUtil.toString(resultMap.get("total_fee"));
				String refund_fee = CommUtil.toString(resultMap.get("total_fee"));//退款全部金额
				//if(CommUtil.trimToEmpty(subappid).equals("")) subappid = WeiXinConfigParam.SUBMCHID;
				SortedMap<String, String> data = getWeChatConfig(null, null, subappid, submchid);
				data.put("transaction_id", resultMap.get("transaction_id"));
				data.put("out_trade_no", out_trade_no);// 定单号
				data.put("out_refund_no", outtradeno);
				data.put("total_fee", total_fee);
				data.put("refund_fee", refund_fee);
				data.put("refund_fee_type", "CNY");
				data.put("op_user_id", config.getMchID());
				//退费
				Map<String, String> result = wxpay.refund(data);
				datamap.put("resultrefMap", result);
				if ("SUCCESS".equals(result.get("result_code"))) {
					String outrefundno = CommUtil.toString(result.get("out_refund_no"));
					String wxrefundno = CommUtil.toString(result.get("refund_id"));
					String refundfeemoney = CommUtil.toString(result.get("refund_fee"));
					datamap.put("outrefundno", outrefundno);
					datamap.put("wxrefundno", wxrefundno);
					datamap.put("refund_fee", refundfeemoney);
					CommUtil.responseBuildInfo(200, "SUCCESS", datamap);
				} else if ("FAIL".equals(result.get("result_code"))) {
					CommUtil.responseBuildInfo(3001, "ERROR,退款失败", datamap);
				}
			}else if(tradestate.equals("NOTPAY") || tradestate.equals("CLOSED") || tradestate.equals("REVOKED")){
				CommUtil.responseBuildInfo(3003, "ERROR,该订单未支付或已撤销，不能退款。", datamap);
			}else if(tradestate.equals("USERPAYING") || tradestate.equals("PAYERROR")){
				CommUtil.responseBuildInfo(3003, "ERROR,该订单支付失败或正在支付中，不能退款。", datamap);
			}else{
				CommUtil.responseBuildInfo(3003, "ERROR,未查到该订单，退款失败。", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3003, "ERROR,服务器发生错误，退款失败。", datamap);
		}
		return datamap;
	}
	
	/**
	 * @method_name: WeChatPayRefund
	 * @Description: 根据订单号和订单金额，发生微信退款
	 * @param ordernum：订单号   refundfee：退款金额[单位为分]   subappid   submchid
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:45:59
	 * @common:
	 */
	public Map<String, Object> WeChatPayRefund(String ordernum, String subappid, String submchid, String refundfee){
		Map<String, Object> datamap = new HashMap<>();
		try {
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			// 根据id查询充电信息
			String out_trade_no = CommUtil.trimToEmpty(ordernum);
			String refund_fee = CommUtil.trimToEmpty(refundfee);
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			//------------------------------------------------------
			Map<String, String> resultMap = inquireWeChatOrder(out_trade_no, null, subappid, submchid);
			String total_fee = CommUtil.toString(resultMap.get("total_fee"));
			//默认不传退款金额、则指为全额退款
			if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
			if(CommUtil.toInteger(total_fee)<CommUtil.toInteger(refund_fee)) return CommUtil.responseBuildInfo(3003, "ERROR,退款金额不能大于该笔订单金额", datamap);
			String returncode = CommUtil.trimToEmpty(resultMap.get("return_code"));
			String tradestate = CommUtil.trimToEmpty(resultMap.get("trade_state"));
			if (returncode.equals("SUCCESS")) {//表示该订单存在
				if(tradestate.equals("SUCCESS") || tradestate.equals("REFUND")){//支付成功或部分退款
					Integer refundcount = CommUtil.toInteger(resultMap.get("refund_count"));
					String outtradeno = out_trade_no = "t"+refundcount+"_" + out_trade_no;
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("appid", WeiXinConfigParam.FUWUAPPID);
					data.put("mch_id", WeiXinConfigParam.MCHID);
					data.put("sub_mch_id", submchid);
					data.put("transaction_id", resultMap.get("transaction_id"));
					data.put("out_trade_no", out_trade_no);// 定单号
					data.put("out_refund_no", outtradeno);//商户退款单号
					data.put("total_fee", total_fee);
					data.put("refund_fee", refund_fee);
					data.put("refund_fee_type", "CNY");
					data.put("op_user_id", config.getMchID());
					Map<String, String> result = wxpay.refund(data);
					datamap.put("resultrefMap", result);
					// 处理退款后的订单 成功
					if ("SUCCESS".equals(result.get("result_code"))) {
						String outrefundno = CommUtil.toString(result.get("out_refund_no"));
						String wxrefundno = CommUtil.toString(result.get("refund_id"));
						String refundfeemoney = CommUtil.toString(result.get("refund_fee"));
						datamap.put("outrefundno", outrefundno);
						datamap.put("wxrefundno", wxrefundno);
						datamap.put("refund_fee", refundfeemoney);
						CommUtil.responseBuildInfo(200, "SUCCESS", datamap);
					} else if ("FAIL".equals(result.get("result_code"))) {
						CommUtil.responseBuildInfo(3001, "ERROR,退款失败", datamap);
					}
				}else if(tradestate.equals("NOTPAY") || tradestate.equals("CLOSED") || tradestate.equals("REVOKED")){
					CommUtil.responseBuildInfo(3003, "ERROR,该订单未支付或已撤销，不能退费。", datamap);
				}else if(tradestate.equals("USERPAYING") || tradestate.equals("PAYERROR")){
					CommUtil.responseBuildInfo(3003, "ERROR,该订单支付失败或正在支付中，不能退费。", datamap);
				}else{
					CommUtil.responseBuildInfo(3003, "ERROR,退费失败。", datamap);
				}
			}else{
				CommUtil.responseBuildInfo(3003, "ERROR,未查到该订单，无法退费。", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3003, "ERROR,服务器发生错误，退款失败。", datamap);
		}
		return datamap;
	}
	
//	/**
//	 * @Description： 查看判断钱包金额与退款金额对应的到账金额差
//	 * @author： origin  
//	 */
//	public boolean walletRich(Money moneyRecord) {
//		if (moneyRecord != null) {
//			Double paymoney = CommUtil.toDouble(moneyRecord.getMoney());
//			Double sendmoney = CommUtil.toDouble(moneyRecord.getSendmoney());
//			Double tomoney = CommUtil.toDouble(moneyRecord.getTomoney());
//			Double topupbalance = CommUtil.toDouble(moneyRecord.getTopupbalance());
//			Double givebalance = CommUtil.toDouble(moneyRecord.getGivebalance());
//			Double balance = CommUtil.toDouble(moneyRecord.getBalance());
//			Integer uid = CommUtil.toInteger(moneyRecord.getUid());
//			User user = userService.selectUserById(uid);
//			if(user==null) return false;
//			Double userbalance = CommUtil.toDouble(user.getBalance());
//			Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
//			
//			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
//				sendmoney = CommUtil.subBig(tomoney, paymoney);
//				userbalance = CommUtil.subBig(userbalance, tomoney);
//				usersendmoney = CommUtil.toDouble(sendmoney);
//				if( userbalance >= 0 && usersendmoney >= 0 ){
//					return true;
//				}
//				return false;
//			}else{
//				userbalance = CommUtil.subBig(userbalance, paymoney);
//				usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
//				if( userbalance >= 0 && usersendmoney >= 0 ){
//					return true;
//				}
//				return false;
//			}
////			Double balance = CommUtil.toDouble(user.getBalance());
////			if(balance >= tomoney){
////				return true;
////			}else{
////				return false;
////			}
//		} else {
//			return false;
//		}
//	}
//	
//	/**
//	 * 
//	 * @Description： 查看判断在线卡金额与退款金额差
//	 * @author： origin
//	 */
//	public boolean checkOnlineRich(OnlineCardRecord cardRecord) {
//		OnlineCard onCard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
//		if (onCard != null) {
//			Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
//			Double accountmoney = CommUtil.toDouble(cardRecord.getAccountmoney());
//			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
////			Double sendmoney = CommUtil.toDouble(cardRecord.getSendmoney());
//			
//			Integer merid = CommUtil.toInteger(onCard.getMerid());
//			Double cardamoney = CommUtil.toDouble(onCard.getMoney());
//			Double cardsendmoney = CommUtil.toDouble(onCard.getSendmoney());
//			
//			Double topupbalance = 0.00;
//			Double givebalance = 0.00;
//			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
//				sendmoney = CommUtil.subBig(accountmoney, paymoney);
//				topupbalance = CommUtil.subBig(cardamoney, accountmoney);
//				givebalance = CommUtil.toDouble(sendmoney);
//				if( topupbalance >= 0 && givebalance >= 0 ){
//					return true;
//				}
//				return false;
//			}else{
//				topupbalance = CommUtil.subBig(cardamoney, paymoney);
//				givebalance = CommUtil.subBig(cardsendmoney, sendmoney);
//				if( topupbalance >= 0 && givebalance >= 0 ){
//					return true;
//				}
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
//	
//	/**
//	 * 退款支付回调
//	 * 根据不同的商户号进行退款
//	 * @param id 充电id
//	 * @param refundState 消费的类型(1:充电,2:离线卡,3:投币,4:钱包,5:在线卡,6:包月)
//	 * @param map worfkey
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, Object> dorefund(Integer id, Integer refundState, Map<String, Object> map) throws Exception {
//		WXPayConfigImpl config = WXPayConfigImpl.getInstance();
//		WXPay wxpay = new WXPay(config);
//		// 根据id查询充电信息
//		Map<String, Object> refundmap = doRefundMoney(refundState, id);
//		String total_fee = CommUtil.toString(refundmap.get("total_fee"));
//		String out_trade_no = CommUtil.toString(refundmap.get("out_trade_no"));
//		Integer merid = CommUtil.toInteger(refundmap.get("merid"));
//		String check = CommUtil.toString(refundmap.get("check"));
//		String statuse = CommUtil.toString(refundmap.get("statuse"));
//		if(check!=null && check.equals("error")){
//			map.put("ok", "usererror");
//			return map;
//		}
//		TradeRecord traderecord = new TradeRecord();
//		// 根据订单号查询正常的订单
//		if(statuse != "0"){
//			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
//			if(tradelist.size()<1){
//				map.put("ok", "error");
//				return map;
//			}else if(tradelist.size()>=1){
//				traderecord = tradelist.get(0);
//			}
//		}
//		String subMchId = null;
//		String subAppid = null;
//		// 检查用户的分成信息
//		logger.info("A设备商家的id"+"============"+merid);
//		if (merid != null && merid != 0) {
//			// 判断商户是否为特约商户
//			User merUser = userService.selectUserById(merid);
//			if(merUser.getSubMer().equals(1)){
//				Map<String, Object> configData = userService.selectSubMerConfigById(merid);
//				subMchId = CommUtil.toString(configData.get("submchid"));
//				subAppid = CommUtil.toString(configData.get("subappid"));
//				logger.info("C我是特约商户的参数:"+subMchId+"============"+subAppid);
//			}else{
//				subMchId = WeiXinConfigParam.SUBMCHID;
//				if (checkUserIfRich(traderecord) == false) {
//					map.put("ok", "moneyerror");
//					return map;
//				}
//				logger.info("D我是特约商户退款:"+subMchId+"============"+subAppid);
//			}
//		}
//		// 获取交易订单合伙人信息
//		String comment = CommUtil.toString(traderecord.getComment());
//		// 配置商户退款信息
//		SortedMap<String, String> params = new TreeMap<>();
//		params.put("appid", WeiXinConfigParam.FUWUAPPID);
//		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_mch_id",subMchId);
//		int wolfkey = (int) (map.get("wolfkey") == null ? 0 : map.get("wolfkey"));
//		if (wolfkey == 3) {
//			params.put("sub_appid", WeiXinConfigParam.SMALLAPPID);
//		}
//		params.put("out_trade_no", out_trade_no);
//		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
//		String sign = HttpRequest.createSign("UTF-8", params);
//		params.put("sign", sign);
//		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
//		String canshu = HttpRequest.getRequestXml(params);
//		String sr = HttpRequest.sendPost(url, canshu);
//		Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
//		// 退款后的业务处理
//		logger.info("F退款的参数"+"======================"+resultMap);
//		if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
//			HashMap<String, String> data = new HashMap<String, String>();
//			data.put("appid", WeiXinConfigParam.FUWUAPPID);
//			data.put("mch_id", WeiXinConfigParam.MCHID);
//			data.put("sub_mch_id", subMchId);
//			if (wolfkey == 3) {
//				data.put("sub_appid", resultMap.get("sub_appid"));
//			}
//			data.put("transaction_id", resultMap.get("transaction_id"));
//			data.put("out_trade_no", out_trade_no);// 定单号
//			data.put("out_refund_no", "t" + out_trade_no);
//			data.put("total_fee", total_fee);
//			data.put("refund_fee", total_fee);
//			data.put("refund_fee_type", "CNY");
//			data.put("op_user_id", config.getMchID());
//
//			try {
//				Map<String, String> r = wxpay.refund(data);
//				logger.info("G退款后的参数"+"========================"+r.toString());
//				// 处理退款后的订单 成功
//				if ("SUCCESS".equals(r.get("result_code"))) {
//					// comment字段更改前的处理方式
//					if(comment == null || comment.equals("0")){//之前数据
//						amendmysql( refundState, id, out_trade_no, map);
//					}else{
//						logger.info("I开始处理特约商户的退款数据"+"======================");
//						// 根据消费的类型去更改对应的数据
//						if (refundState == 1) {
//							chargeRefund(id, out_trade_no, map);
//						} else if (refundState == 2) {
//							offlineCardRefund(id, out_trade_no, map);
//						} else if (refundState == 3) {
//							inCoinsRefund(id, out_trade_no, map);
//						} else if (refundState == 4) {
//							walletRefund(id, out_trade_no, map);
//						} else if (refundState == 5) {
//							onlinecardRefund(id, out_trade_no, map);
//						} else if (refundState == 6) {
//							packageMonthRefund(id, out_trade_no, map);
//						}
//					}
//					
//					map.put("ok", "ok");
//				}
//				if ("FAIL".equals(r.get("result_code"))) {
//
//					map.put("ok", "error");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("退款失败");
//				map.put("ok", "error");
//			}
//		} else {
//			map.put("ok", "error");
//		}
//		return map;
//	}
//	
//	/**
//	 * 根据id查询充电信息
//	 * @param refundState 充电类型:
//	 * @param id 充电id
//	 * @return
//	 */
//	public Map<String, Object> doRefundMoney(Integer refundState, Integer id){
//		Map<String, Object> map = new HashMap<String, Object>();
//		String check = "ok";
//		String total_fee = "";
//		String out_trade_no = "";// 退款订单号
//		Integer merid = 0;
//		// 扫码充电记录
//		if (refundState == 1) {
//			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
//			String money = CommUtil.toString(CommUtil.toDouble(chargeRecord.getExpenditure()) * 100);
//			int idx = money.lastIndexOf(".");
//			total_fee = money.substring(0, idx);
//			out_trade_no = CommUtil.toString(chargeRecord.getOrdernum());// 退款订单号
//			merid = chargeRecord.getMerchantid();
//			logger.info("商家的id"+merid+"======================================");
//		// 离线卡记录
//		} else if (refundState == 2) {
//			OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
//			check = "error";
//			if(offlineCard!=null){
//				if(offlineCard.getHandletype()!=2){//充值退费
//					String money = String.valueOf(CommUtil.toDouble(offlineCard.getChargemoney()) * 100);
//					int idx = money.lastIndexOf(".");
//					total_fee = money.substring(0, idx);
//					out_trade_no = offlineCard.getOrdernum();// 退款订单号
//					merid = offlineCard.getMerchantid();
//					check = "ok";
//				}
//			}
//		// 投币充电记录
//		} else if (refundState == 3) {
//			InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
//			String money = String.valueOf(incoins.getMoney() * 100);
//			int idx = money.lastIndexOf(".");
//			total_fee = money.substring(0, idx);
//			out_trade_no = incoins.getOrdernum();// 退款订单号
//			merid = incoins.getMerchantid();
//		// 钱包充值记录
//		} else if (refundState == 4) {
//			Money money = moneyService.payMoneyinfo(id);
////			String paymoney = String.valueOf(money.getMoney() * 100);
//			String paymoney = CommUtil.toString(money.getMoney() * 100);
//			int idx = paymoney.lastIndexOf(".");
//			total_fee = paymoney.substring(0, idx);
//			out_trade_no = money.getOrdernum();// 退款订单号
//			// 根据钱包充值的订单查询商家的信息
//			merid = moneyService.selectMerByMoneyOrdernum(out_trade_no);
//			if (walletRich(money) == false) check = "error";
//		// 在线充电记录
//		} else if (refundState == 5) {
//			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
//			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
//			Integer statuse = CommUtil.toInteger(oncard.getStatus());
//			if(statuse.equals(0)){//在线卡未激活
//				map.put("statuse", 0);
//			}
//			merid = CommUtil.toInteger(oncard.getMerid());
//			if(oncard.getStatus()==0) merid = 0;
//			String paymoney = String.valueOf(cardRecord.getMoney() * 100);
//			int idx = paymoney.lastIndexOf(".");
//			total_fee = paymoney.substring(0, idx);
//			out_trade_no = cardRecord.getOrdernum();// 退款订单号
//			if (checkOnlineRich(cardRecord) == false) check = "error";
//		// 包月充电记录
//		} else if (refundState == 6) {
//			PackageMonthRecord monthRecord = userService.selectMonthRecordById(id);
//			PackageMonth packageMonth = userService.selectPackageMonthByUid(monthRecord.getUid());
//			long currentTime = System.currentTimeMillis();
//			long endTime = packageMonth.getEndTime().getTime();
//			System.out.println("剩余次数比较===" + (packageMonth.getSurpnum() < monthRecord.getChangenum()));
//			System.out.println("时间比较===" + (currentTime > endTime));
////			if (packageMonth.getSurpnum() < monthRecord.getChangenum() || currentTime > endTime) {
////				check = "error";
////			}
//			merid = monthRecord.getMerid();
//			String paymoney = String.valueOf(monthRecord.getMoney() * 100);
//			int idx = paymoney.lastIndexOf(".");
//			total_fee = paymoney.substring(0, idx);
//			out_trade_no = monthRecord.getOrdernum();// 退款订单号
//		}
//		map.put("merid", merid);
//		map.put("check", check);
//		map.put("total_fee", total_fee);
//		map.put("out_trade_no", out_trade_no);
//		return map;
//	}
//	
//	/**
//	 * @Description： 脉冲退费处理数据
//	 */
//	public void inCoinsRefund(Integer id, String out_trade_no, Map<String, Object> map){
//		InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
//		//修改本地数据
//		String ordernum = CommUtil.toString(out_trade_no);
//		String devicenum = CommUtil.toString(incoins.getEquipmentnum());
//		Integer merid = CommUtil.toInteger(incoins.getMerchantid());
//		Integer orderid = CommUtil.toInteger(incoins.getId());
//		Integer paysource = MerchantDetail.INCOINSSOURCE;
//		Integer paytype = MerchantDetail.WEIXINPAY;
//		Integer paystatus = MerchantDetail.REFUND;
//		Integer wolfkey = CommUtil.toInteger(map.get("wolfkey"));
//		if (wolfkey == 3) {
//			inCoinsService.updateInCoinsStatus(ordernum, (byte) 9);
//		} else {
//			inCoinsService.updateInCoinsStatus(ordernum, (byte) 4);
//		}
//		if (incoins.getHandletype() != 4 && incoins.getHandletype() != 9) {
//			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
//			TradeRecord traderecord = new TradeRecord();
//			if(tradelist.size()>1){
//				for(TradeRecord item : tradelist){
//					dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, item);
//				}
//			}else{
//				traderecord = tradeRecordService.getTraderecord(ordernum);
//				dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, traderecord);
//			}
//		}
//	}
//	
//	/**
//	 * 钱包充值退款
//	 * @param refundState
//	 * @param id
//	 * @param out_trade_no
//	 * @param map
//	 */
//	public void walletRefund(Integer id, String out_trade_no, Map<String, Object> map){
//		Money money = moneyService.payMoneyinfo(id);
//		//修改本地数据
//		String ordernum = CommUtil.toString(out_trade_no);
//		Integer uid = CommUtil.toInteger(money.getUid());
//		Double paymoney = CommUtil.toDouble(money.getMoney());
//		Double sendmoney = CommUtil.toDouble(money.getSendmoney());
//		Double tomoney = CommUtil.toDouble(money.getTomoney());
//		Double moneytopup = CommUtil.toDouble(money.getTopupbalance());
//		Double moneygive = CommUtil.toDouble(money.getGivebalance());
//		Double moneybalance = CommUtil.toDouble(money.getBalance());
//		System.out.println("钱包退款");
//		Integer orderid = CommUtil.toInteger(money.getId());
//		Date time = new Date();
//		String strtime = CommUtil.toDateTime(time);
//		Integer paysource = MerchantDetail.WALLETSOURCE;
//		Integer paytype = MerchantDetail.WEIXINPAY;
//		Integer paystatus = MerchantDetail.REFUND;
//		Integer type = 0;
//		Double mermoney = 0.00;
//		String first = "";
//		String remark = "";
//		String status = "退款成功！。";
//		moneyService.updateMoneyByOrder(ordernum, 2);
//		//修改用户钱包金额
//		User user = userService.selectUserById(uid);
//		Integer merid = CommUtil.toInteger(user.getMerid());
//		Double userbalance = CommUtil.toDouble(user.getBalance());
//		Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
//		
//		if(sendmoney.equals(0) && moneytopup.equals(0) && moneygive.equals(0)){//未分离赠送金额
//			sendmoney = CommUtil.subBig(tomoney, paymoney);
//			userbalance = CommUtil.subBig(userbalance, tomoney);
//			usersendmoney = CommUtil.toDouble(sendmoney);
//		}else{
//			userbalance = CommUtil.subBig(userbalance, paymoney);
//			usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
//		}
//		if(userbalance<0) userbalance = 0.00;
//		if(usersendmoney<0) usersendmoney = 0.00;
//		Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
//		Double topupbalance = userbalance;
//		Double givebalance = usersendmoney;
//		Double balance = CommUtil.addBig(userbalance, usersendmoney);
////		userbalance = CommUtil.subBig(userbalance, paymoney);
////		usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
////		if(userbalance<0) userbalance = 0.00;
////		if(usersendmoney<0) usersendmoney = 0.00;
////		Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
////		Double topupbalance = userbalance;
////		Double givebalance = usersendmoney;
////		Double balance = CommUtil.addBig(userbalance, usersendmoney);
////		Double walletmo = (user.getBalance()*100-money.getTomoney()*100)/100;
////		if(walletmo<0) walletmo = 0.00;
////		user.setBalance(walletmo);
//		user.setBalance(userbalance);
//		user.setSendmoney(usersendmoney);
//		userService.updateUserById(user);
////		(Integer uid,Integer merid,Double paymoney, Double sendmoney, Double balance,String ordernum,Date createTime,Integer paysource);
//		generalDetailService.insertGenWalletDetail(uid, merid, paymoney, sendmoney, opertomoney, balance, topupbalance, givebalance, ordernum, time, 6);
//		if (money.getPaytype() != 2) {
//			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
////			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
//			TradeRecord traderecord = new TradeRecord();
//			if(tradelist.size()>=1){
//				traderecord = tradelist.get(0);
//			}
//			String comment = traderecord.getComment();
//			try {
//				Integer aid = CommUtil.toInteger(user.getAid());
//				if (aid != null && aid != 0) {
//					areaService.updateAreaEarn( 0, paymoney, aid, paymoney, null, null);
//				}
//				// 判断用户是不是特约商户
//				User subMer = userService.selectUserById(merid);
//				if(subMer != null && subMer.getSubMer().equals(0)){
//					// 钱包充值退款数据
//					dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
//				}else{
//					// 记录特约商户的数据
//					userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
//					merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
//				}
//			} catch (Exception e) {
//				logger.warn("小区修改余额错误===" + e.getMessage());
//			}
//			mermoney = traderecord.getMermoney();
//			Double partmoney = traderecord.getManmoney();
//			Integer manid = partmoney == 0 ? 0 : -1;
//			tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 2, 2, traderecord.getHardver(), comment);
//		}
//		System.out.println("发送消息");
////		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
////		TempMsgUtil.returnMsg("钱包退费", money.getUid(), user.getMerid(), money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
//		String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 3);
//		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
//		TempMsgUtil.returnMsg("钱包退费", money.getUid(), phone, money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
//		System.out.println("发送消息完成");
//	}
//	
//	/**
//	 * 在线卡充值退款
//	 * @param refundState
//	 * @param id
//	 * @param out_trade_no
//	 * @param map
//	 */
//	public void onlinecardRefund(Integer id, String out_trade_no, Map<String, Object> map){
//		try{
//			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
//			//修改本地数据
//			String ordernum = CommUtil.toString(out_trade_no);
//			Integer uid = CommUtil.toInteger(cardRecord.getUid());
//			Integer merid = CommUtil.toInteger(cardRecord.getMerid());
//			Integer orderid = CommUtil.toInteger(cardRecord.getId());
//			String devicenum = CommUtil.toString(cardRecord.getCode());
//			String cardID = CommUtil.toString(cardRecord.getCardID());
//			
//			
//			Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
//			Double accountmoney = CommUtil.toDouble(cardRecord.getAccountmoney());
//			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
////			Double sendmoney = CommUtil.toDouble(cardRecord.getSendmoney());
//			Date time = new Date();
//			String strtime = CommUtil.toDateTime(time);
//			Integer paysource = MerchantDetail.ONLINESOURCE;
//			Integer paytype = MerchantDetail.WEIXINPAY;
//			Integer paystatus = MerchantDetail.REFUND;
//			Integer type = 0;
//			Double mermoney = 0.00;
//			//处理在线卡信息
//			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardID);
//			Double cardamoney = CommUtil.toDouble(oncard.getMoney());
//			Double cardsendmoney = CommUtil.toDouble(oncard.getSendmoney());
//			Double topupbalance = 0.00;
//			Double givebalance = 0.00;
//			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
//				sendmoney = CommUtil.subBig(accountmoney, paymoney);
//				topupbalance = CommUtil.subBig(cardamoney, accountmoney);
//				givebalance = CommUtil.toDouble(sendmoney);
//			}else{
//				topupbalance = CommUtil.subBig(cardamoney, paymoney);
//				givebalance = CommUtil.subBig(cardsendmoney, sendmoney);
//			}
//			Double cardbalance = CommUtil.addBig(topupbalance, givebalance);
//			Integer aid = CommUtil.toInteger(oncard.getAid());
//			oncard.setMoney(topupbalance);
//			oncard.setSendmoney(givebalance);
//			onlineCardService.updateOnlineCardBycard(oncard);
//			
//			if (oncard.getMerid() != null && oncard.getMerid() != 0) {
//				if (oncard.getStatus()!=0 && cardRecord.getType() != 5) {
//					TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
//					String comment = traderecord.getComment();
//					try {
//						if (aid != null && aid != 0) {
//							areaService.updateAreaEarn(0, paymoney, aid, null, null, paymoney);
//						}
//						// 根据商户的id查询商户信息
//						User subMer = userService.selectUserById(merid);
//						// 判断商户是不是特约商户
//						if(subMer != null && subMer.getSubMer().equals(0)){
//							// 在线卡充值退款数据
//							dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
//						}else{
//							userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
//							merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
//						}
//						mermoney = traderecord.getMermoney();
//						Double partmoney = traderecord.getManmoney();
//						Integer manid = partmoney == 0 ? 0 : -1;
//						tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, traderecord.getCode(), 5, 2, 2, null, comment);
//					} catch (Exception e) {
//						logger.warn("小区修改余额错误===" + e.getMessage());
//					}
//				}
//			}
//			cardRecord.setFlag(2);
//			onlineCardRecordService.updateRecord(cardRecord);
//			cardRecord.setId(null);
//			cardRecord.setType(5);
//			cardRecord.setTopupbalance(topupbalance);
//			cardRecord.setGivebalance(givebalance);
//			cardRecord.setBalance(cardbalance);
//			cardRecord.setCreateTime(new Date());
//			onlineCardRecordService.additionOnlineCardRecord(cardRecord);
////			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
////			TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), cardRecord.getMerid(), cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
//			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
//			String phone = getTempPhone(cardRecord.getCardID(), cardRecord.getMerid(), 4);
//			TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), phone, cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
//			
//			map.put("ok", "ok");
//		}catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("退款失败");
//			map.put("ok", "error");
//		}
//	}
//	
//	
//	
	

	/**
	 * @Description： 计算合伙人与商户分别收益的计算处理
	 * @author： origin 创建时间：2020年5月22日下午4:46:55
	 */
	public Map<String, Object> percentCalculateDispose(List<Map<String, Object>> partInfo, Integer merid, Double money) {
		Double mermoney = 0.00;//商户金额
		Double partmoney = 0.00;//合伙人金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> percentinfo = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				System.out.println("输出分成");
				for(Map<String, Object> item : partInfo){
					Map<String, Object> mappartner = new HashMap<>();
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double percent = CommUtil.toDouble(item.get("percent"));
					Double partnetmoney = CommUtil.toDouble((money * (percent*100))/100);
					mappartner.put("partid", partid);
					mappartner.put("percent", percent);
					mappartner.put("money", partnetmoney);
					percentinfo.add(mappartner);
					tolpercent = tolpercent + percent;
				}
			}
			mermoney = CommUtil.toDouble(money * (1- tolpercent));
			partmoney = CommUtil.subBig(money, mermoney);
			Map<String, Object> mermap = new HashMap<>();
			mermap.put("merid", merid);
			mermap.put("percent", (1-tolpercent));
			mermap.put("money", mermoney);
			percentinfo.add(mermap);
			mapresult.put("mermoney", mermoney);
			mapresult.put("partmoney", partmoney);
			mapresult.put("percentinfo", percentinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapresult;
	}
	
	public void dealerDataDispose(Integer orderid, String devicenum, Integer merid,
		   Integer paysource, Integer paytype, Integer paystatus, TradeRecord traderecord) {
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		Integer uid = CommUtil.toInteger(traderecord.getUid());
		Integer merId = CommUtil.toInteger(traderecord.getMerid());
		Double paymoney = CommUtil.toDouble(traderecord.getMoney());
		String ordernum = CommUtil.toString(traderecord.getOrdernum());
		Integer traPaysource = CommUtil.toInteger(traderecord.getPaysource()); 
		Integer traPaytype = CommUtil.toInteger(traderecord.getPaytype());
		Integer traStatus = 2;
		String devicehard = null;
		try {
			// 更新设备收益
			equipmentService.updateEquEarn(devicenum, paymoney, 0);
			String comment = traderecord.getComment();
			try {
				Equipment equipment = equipmentService.getEquipmentById(devicenum);
				devicehard = CommUtil.toString(equipment.getHardversion());
				Integer aid = CommUtil.toInteger(equipment.getAid());
				// 更新小区收益
				if (aid != null && aid != 0) {
					areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
				} 
				User subMer = userService.selectUserById(merId);
				// 普通商家修改退费数据
				if(subMer != null && subMer.getSubMer().equals(0)){
					//充电退费数据
					dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
				}else{
					// 特约商户修改退费数据
					userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
					merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
				}
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			Double mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = 0;
			tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, traPaysource, traPaytype, traStatus, devicehard, comment);
//			String phone = getTempPhone(incoins.getEquipmentnum(), incoins.getMerchantid(), 2);
//			Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
//			Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//			Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//			String devicename = CommUtil.toString(devicedata.getRemark());
//			String areaaddress = CommUtil.toString(devicedata.getAddress());
//			String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//			systemSetService.sendMmessChargeEnd(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
//					devicenum, port, devicename, deviceaid, areaaddress, servicphone);
//			
			
//			String phone = getTempPhone(devicenum, merid, 0);
//			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
//			returnMsgTemp(uid, phone, devicenum, ordernum, urltem, strtime, paymoney);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	public Object dealerIncomeRefund(String comment, Integer merid, Double money, String ordernum, 
			Date time, Integer merpaysource, Integer merpaytype, Integer merpaystatus) {
		Integer type = 0;
		List<Map<String, Object>> merchlist = new ArrayList<>();
		try {
			JSONArray jsona = JSONArray.fromObject(comment);
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject item = jsona.getJSONObject(i);
				Map<String, Object> map = CommUtil.toHashMaps(item);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Integer mercid = CommUtil.toInteger(item.get("merid"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				Integer dealid = partid == 0 ? mercid : partid;
				User user = userService.selectUserById(dealid);
				Double earnings = user.getEarnings();
				if(type==1){//  1为加  0为减
					earnings = (earnings * 100 + partmoney * 100) / 100;
				}else{
					earnings = (earnings * 100 - partmoney * 100) / 100;
				}
				user.setEarnings(earnings);
				merchantDetailService.insertMerEarningDetail(dealid, partmoney, earnings, ordernum, time, merpaysource, merpaytype, merpaystatus);
				userService.updateUserEarnings(type, partmoney, user.getId());//商户收益  1为加  0为减
				userService.updateMerAmount(type, partmoney, user.getId());//商户收益总额  1为加  0为减
				merchlist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return JSON.toJSON(merchlist);
	}
	
	/**
	 * @Description： 商户收益计算
	 * @author： origin 创建时间：   2019年9月12日 下午5:54:22
	 */
	public void merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Date operattime,
			Integer paysource, Integer paytype, Integer status){
		try {
			User user = userService.selectUserById(merid);
			Double earnings = user.getEarnings();
			if(type==1){//  1为加  0为减
				earnings = (earnings * 100 + money * 100) / 100;
			}else{
				earnings = (earnings * 100 - money * 100) / 100;
			}
			user.setEarnings(earnings);
			merchantDetailService.insertMerEarningDetail(merid, money, earnings, ordernum, operattime, paysource, paytype, status);
			
			userService.updateUserEarnings(type, money, user.getId());//商户收益  1为加  0为减
			userService.updateMerAmount(type, money, user.getId());//商户收益总额  1为加  0为减
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * @method_name: getAlipayConfig
	 * @Description: 获取支付宝配置信息
	 * @param type 1:支付宝  2:支付宝小程序    默认为支付宝小程序
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午5:03:30
	 * @common:
	 */
	public AlipayClient getAlipayConfig(Integer type) {
		AlipayClient alipayClient = null;
		
		if (type == 1) {//支付宝
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID,
					AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		}else if(type == 2) {//支付宝小程序
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
					AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
		}else {//支付宝小程序
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
					AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
//			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID,
//					AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		}
		return alipayClient;
	}
	
	public Map<String, Object> alirefund(Integer type, Integer id, Integer refundState) {
		Map<String, Object> map = new HashMap<>();
		AlipayClient alipayClient = getAlipayConfig(CommUtil.toInteger(type));
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		Double paymoney = 0.0;
		String ordernum = "";
		request.setBizContent("{" + "\"out_trade_no\":\"" + ordernum + "\"," + "\"refund_amount\":" + paymoney + "  }");
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				
			} else {
				map.put("ok", "error");
				return map;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			map.put("ok", "error");
			return map;
		}
		return map;
	}
	
	
}

package com.hedong.hedongwx.web.controller.webpc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.LostOrderService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.OrderDataService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 订单信息控制类
 * @author  origin  创建时间：   2019年11月7日 上午11:28:38  
 */
@Controller
@RequestMapping(value = "/orderData")
public class OrderDataController {
	

	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private OrderDataService orderDataService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private UserService userService;
	@Autowired
	private LostOrderService lostOrderService;
	
	/**
	 * separate
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年12月23日 下午2:28:35
	 */
	@RequestMapping(value="/inquireWeChatLostOrder")
	@ResponseBody
    public Object inquireWeChatLostOrder(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = lostOrderService.inquireListLostInfo(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 解绑在线卡
	 * @param: 
	 * @author： origin   2019年12月23日 下午2:28:35
	 */
	@RequestMapping(value="/unbindingOnlineCard")
	@ResponseBody
    public Object calculateAloneCollect(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  onlineCardService.unbindingOnlineCard(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 订单详情信息  根据id查询
	 * @author： origin 
	 */
	@RequestMapping(value="/orderDetailData")
	@ResponseBody
    public Object orderDetailData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  orderDataService.orderDetailData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 订单详情信息   根据订单号查询
	 * @author： origin 
	 */
	//TODO origin 
	@RequestMapping(value="/orderDetailInfo")
	@ResponseBody
    public Object orderDetailInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  orderDataService.orderDetailInfo(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 查询交易记录信息
	 * @author： origin 
	 */
	@RequestMapping(value="/orderTradeRecordData")
	@ResponseBody
    public Object inquireDeviceOperationData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  tradeRecordService.tradeRecordData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：交易订单详情查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderTradeDataDetail")
	@ResponseBody
    public Object orderTradeDataDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  orderDataService.tradeDataDetail(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：（充电、离线充值、投币、钱包、包月、在线卡）订单详情查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderDataDetail")
	@ResponseBody
    public Object orderDataDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  orderDataService.orderDataDetail(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：交易订单查询合伙人收益信息
	 * @author： origin 
	 */
	@RequestMapping(value="/partnerIncomeDetail")
	@ResponseBody
    public Object partnerIncomeDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
//			result =  tradeRecordService.partnerIncomeDetail(request);
			result =  orderDataService.partnerIncomeDetail(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：充电记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderChargeRecordData")
	@ResponseBody
    public Object orderChargeRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  chargeRecordService.chargeRecordData(request);
		}
		return JSON.toJSON(result);
	}
	
	
	/**
	 * separate
	 * @Description：查询充电记录中的折现图
	 * @author： origin 
	 */
	@RequestMapping(value="/inquirePowerBrokenLine")
	@ResponseBody
    public Object inquirePowerBrokenLine(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = chargeRecordService.inquirePowerBrokenLine(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：离线充值机记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderOfflineRecordData")
	@ResponseBody
    public Object orderOfflineRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  offlineCardService.offlineRecordData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：投币记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderinCoinsRecordData")
	@ResponseBody
    public Object orderinCoinsRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  inCoinsService.inCoinsRecordData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：包月记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderPackageMonthData")
	@ResponseBody
    public Object orderPackageMonthData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  orderDataService.packageMonthData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：提现记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderWithdrawRecordData")
	@ResponseBody
    public Object orderWithdrawRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  withdrawService.withdrawRecordData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：  提现判断
	 * @author： origin   2019年11月15日 下午6:00:58
	 */
//	@RequestMapping(value = "/withdrawResolve", method = RequestMethod.POST)
	@RequestMapping(value = "/withdrawResolve")
	@ResponseBody
	public Object withdrawResolve(HttpServletRequest request, HttpServletResponse response) {
//	public String withdrawResolve(Integer id, Integer status,Double money,Integer userId) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  withdrawService.withdrawResolve(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：钱包记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderWalletRecordData")
	@ResponseBody
    public Object orderWalletRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  moneyService.walletRecordData(request);
		}
		return JSON.toJSON(result);
	}
	

	/**
	 * separate
	 * @Description：在线卡卡信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderOnlineCardData")
	@ResponseBody
    public Object orderOnlineCardData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  onlineCardService.onlineCardData(request);
		}
		return JSON.toJSON(result);
	}
	

	/**
	 * separate
	 * @Description：在线卡消费记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderOnlineCardRecordData")
	@ResponseBody
    public Object orderOnlineCardRecordData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  onlineCardRecordService.onlineCardRecordData(request);
		}
		return JSON.toJSON(result);
	}
	

	/**
	 * separate
	 * @Description：在线卡操作记录信息查看
	 * @author： origin 
	 */
	@RequestMapping(value="/orderOnlineOperateData")
	@ResponseBody
    public Object orderonlineOperateData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  onlineCardRecordService.onlineCardOperateRecord(request);
		}
		return JSON.toJSON(result);
	}
	/**
	 * PC端查询商家缴费的详情
	 * @param request
	 * @return {@link JSON}
	 */
	@RequestMapping("/feescaleRecode")
	@ResponseBody
	public String feescaleRecode(HttpServletRequest request, HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  feescaleService.selectFeescaleRecord(request);
		}
		return JSON.toJSONString(result);
	}
	/**
	 * 根据订单查询商家的缴费记录
	 * @param request
	 * @param response
	 * @return {@link JSON}
	 */
	@RequestMapping("/feescaleRecodeDetail")
	@ResponseBody
	public String selectFeescaleDetails(HttpServletRequest request,HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  feescaleService.selectFeescaleDetails(request);
		}
		return JSON.toJSONString(result);
		
	}
	
	/**
	 * 查询收款人的详细资料
	 */
	@RequestMapping("/reviceUserDetail")
	@ResponseBody
	public String partnerFeescaleRecode(HttpServletRequest request, HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			//获取revice_id
			String reviceId = request.getParameter("reviceid");
			if(reviceId != null && !"".equals(reviceId)){
				User user=userService.selectUserById(CommUtil.toInteger(reviceId));
				result =CommUtil.responseBuild(200, "成功", user);
			}else{
				result =CommUtil.responseBuild(301, "请求错误", "");
			}
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 结束订单
	 */
	@RequestMapping("/stopCharging")
	@ResponseBody
    public Object stopCharging(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			String ordernum = request.getParameter("ordernum");
			if (ordernum != null && !"".equals(ordernum)) {
				result =  chargeRecordService.stopChargeRecord(ordernum, 0.0, 0.0, 0, 0);
			} else {
				result = CommUtil.responseBuild(203, "参数传递异常", "");
			}
		}
		return JSON.toJSON(result);
	}
}

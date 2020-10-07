package com.hedong.hedongwx.web.controller.webpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AdminiStratorService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.service.OrderDataService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年11月6日 上午9:28:15  
 */
@Controller
@RequestMapping(value="/AccountInfo")
public class AccountController {
	@Autowired
	private UserService userService;
	@Autowired
	private AdminiStratorService adminiStratorService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OrderDataService orderDataService;
	@Autowired
	private OperateRecordService operateRecordService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FeescaleService feescaleService;

	/**
	 * separate
	 * @Description： 获取商户信息 (根据type不同，查询条件不同)
	 * @author： origin 
	 */
	@RequestMapping(value="/getDealerInfo")
	@ResponseBody
    public Object getDealerInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.getDealerInfo(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 修改账户信息
	 * @author： origin  
	 */
	@RequestMapping(value="/redactAccountInfo")
	@ResponseBody
    public Object redactAccountInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.redactAccountInfo(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 获取商户信息
	 * @author： origin 
	 */
	@RequestMapping(value="/getDealerListInfo")
	@ResponseBody
    public Object getDealerListInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.getDealerListInfo(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 获取单个商户信息
	 * @param: 
	 * @author： origin  
	 */
	@RequestMapping(value="/getDealerPersonInfo")
	@ResponseBody
    public Object getDealerPersonInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.getDealerPersonInfo(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 商户收益明细查询
	 * @author： origin  
	 */
	@RequestMapping(value="/dealerEarningsDetail")
	@ResponseBody
    public Object dealerEarningsDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.dealerEarningsDetail(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description：商户收益订单明细明信息查询
	 * @author： origin
	 */
	@RequestMapping(value = "/dealerEarningsOrderDetail")
	@ResponseBody
	public Object dealerEarningsOrderDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = orderDataService.dealerEarningsOrderDetail(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 查看商户银行卡信息
	 * @author： origin  
	 */
	@RequestMapping(value="/dealerBankCardData")
	@ResponseBody
    public Object dealerBankCardData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.dealerBankCardData(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 修改指定商户个人费率
	 * @author： origin  
	 */
	@RequestMapping(value = "/editDealerPersonRate")
	@ResponseBody
	public Object editDealerPersonRate(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.editDealerPersonRate(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 修改指定商户对公费率
	 * @author： origin  
	 */
	@RequestMapping(value = "/editDealerCorporateRate")
	@ResponseBody
	public Object editDealerCorporateRate(HttpServletRequest request, HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.editDealerCorporateRate(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 获取用户信息
	 * @author： origin 
	 */
	@RequestMapping(value="/getAccountListInfo")
	@ResponseBody
    public Object getAccountListInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "缓存失效", "");
		}else{
			result = userService.getAccountListInfo(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 获取单个用户信息
	 * @author： origin  
	 */
	@RequestMapping(value="/getAccountPersonInfo")
	@ResponseBody
    public Object getAccountPersonInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.getAccountPersonInfo(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 客户强制被绑定商户
	 * @author： origin  
	 */
	@RequestMapping(value="/bindingDealer")
	@ResponseBody
    public Object bindingDealer(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.touristBindingDealer(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 客户强制解绑商户
	 * @author： origin  
	 */
	@RequestMapping(value="/unbindDealer")
	@ResponseBody
    public Object unbindDealer(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.tourisTunbindDealer(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 客户被强制绑定小区
	 * @author： origin  
	 */
	@RequestMapping(value="/bindingArea")
	@ResponseBody
    public Object bindingArea(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.bindingArea(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 客户被强制解除小区绑定
	 * @author： origin  
	 */
	@RequestMapping(value="/unbindArea")
	@ResponseBody
    public Object unbindArea(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.unbindArea(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 强制绑定小区或商户时查询对应信息
	 * @author： origin  
	 */
	@RequestMapping(value="/bindingInquireData")
	@ResponseBody
    public Object bindingInquireData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.bindingInquireData(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 查询用户钱包详细
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	@RequestMapping(value="/touristWalletData")
	@ResponseBody
    public Object touristWalletData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.touristWalletData(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description：钱包明细根据订单号和来源查询该条订单
	 * @author： origin
	 */
	@RequestMapping(value = "/touristWalletDetail")
	@ResponseBody
	public Object touristWalletDetail(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = orderDataService.touristWalletDetail(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 查询用户在线卡信息
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	@RequestMapping(value="/inquireTouristOnlineData")
	@ResponseBody
    public Object inquireTouristOnlineData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = onlineCardService.inquireTouristOnlineData(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 查询包月信息记录
	 * @author： origin  
	 */
	@RequestMapping(value="/inquireMonthlyData")
	@ResponseBody
    public Object inquireMonthlyData(Integer montid, Integer uid) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Object result = null;
			if(CommonConfig.isExistSessionUser(request)){
				result = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				result = adminiStratorService.selectPackagemonth(montid, uid);
			}
			datamap.put("listdata", result);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 查询获取操作信息
	 * @author： origin   2019年11月11日 下午1:34:51
	 */
	@RequestMapping(value="/accountOperateInfo")
	@ResponseBody
    public Object accountOperateInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = operateRecordService.accountOperateInfo(request);
		}
		return JSON.toJSON(result);
	}

	/**
	 * 查看商家的收费标准
	 *
	 *            商家id
	 * @return model
	 */
	@RequestMapping("/getListFeescale")
	@ResponseBody
	public Object getListFeescaleByMerid(@RequestParam("merId") Integer merId, Model model) {
		return feescaleService.getFeescalBymerid(merId, model);
	}


	/**
	 * 更改商家设备型号的收费标准
	 * @param id 商家的id
 	 * @param userName 商家的姓名
	 * @param netType 网络类型
	 * @param equipmentType 设备类型
	 * @return
	 */
	@RequestMapping("/updateMerFeescale")
	@ResponseBody
	public Object amendFeescale(Integer id,String userName, String netType,String equipmentType) {
		Object result = null;
		User user = CommonConfig.getAdminReq(request);
		if (CommonConfig.isExistSessionUser(request)) {
			result = CommUtil.responseBuild(901, "session缓存失效", "");
			return JSON.toJSON(result);
		} else if (user != null && user.getLevel() != null && user.getLevel() == 0) {
			//将JSON解析成Map
			Map<String,Map<String,Double>> net=new HashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> blue=new HashMap<String,Map<String,Double>>();
			Map<String, Double> map=new HashMap<String,Double>();
			Map<String, Double> map2=new HashMap<String,Double>();
			Map netMapType = JSONObject.parseObject(netType, Map.class);
			Map blueMapType = JSONObject.parseObject(equipmentType, Map.class);
			Map netMap=(Map) netMapType.get("00");
			Map blueMap=(Map)blueMapType.get("01");
			Set<String> set=netMap.keySet();
			Set<String> set1=blueMap.keySet();
			for (String str : set) {
				map.put(str, CommUtil.toDouble(netMap.get(str)));
			}
			for(String str1 :set1){
				map2.put(str1,CommUtil.toDouble(blueMap.get(str1))); 
			}
			net.put("00", map);
			blue.put("01", map2);
			boolean success = feescaleService.updateFeescaleBymerid(id, userName, net, blue);
			if (success) {
				result = CommUtil.responseBuild(200, "成功", "");
				return JSON.toJSON(result);
			} else {
				result = CommUtil.responseBuild(400, "失败", "");
				return JSON.toJSON(result);
			}
		} else {
			result = CommUtil.responseBuild(103, "商家无权修改", "");
			return JSON.toJSON(result);
		}

	}
	/**
	 * 给用户授权为代理商或者商家
	 * @return 200
	 */
	@RequestMapping("/setAgent")
	@ResponseBody
	public Object setAgent(Integer merId,Integer rank){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(merId != null && superUser != null && superUser.getLevel()==0 && rank != 0){
			result = userService.setAgent(merId,rank);
		}else{
			result = CommUtil.responseBuild(301, "失败", "参数错误");
			return result;
		}
		return JSON.toJSON(result);
	}
	/**
	 * 超级管理员查看所有代理商
	 * @return
	 */
	@RequestMapping("/selectAgent")
	@ResponseBody
	public Object selectAgent(HttpServletRequest request,HttpServletResponse response){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(superUser != null && superUser.getLevel()==0){
			Object object = userService.selectAgents(request);
			result =CommUtil.responseBuild(200, "成功",object);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * 查看代理商名下的商家
	 * @param agentId
	 * @return Object
	 */
	@RequestMapping("/selectAgentUnderMer")
	@ResponseBody
	public Object selectAgentUnderMer(Integer agentId){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(agentId != null && superUser != null && superUser.getLevel()==0){
			Parameters parameters = new Parameters();
			parameters.setUid(agentId);
			result = userService.selectMerByAgentId(parameters);
		}else{
			result =CommUtil.responseBuild(301, "失败","");
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * 将商家绑定到代理商
	 * @return
	 */
	@RequestMapping("bindAgent")
	@ResponseBody
	public Object bindAgent(Integer id,Integer merId){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(id != null && merId != null && superUser != null && superUser.getLevel()==0){
			result = userService.bindAgent(id,merId);
		}else{
			result = CommUtil.responseBuild(301, "失败","参数错误");
		}
		return JSON.toJSON(result);
	}
	/**
	 * 将商家从代理商中解除
	 * @param merId 商家id
	 * @return 200
	 */
	@RequestMapping("removeMer")
	@ResponseBody
	public Object removeMer(Integer merId){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(merId != null && superUser != null && superUser.getLevel()==0){
			result = userService.removeBindAgent(merId);;
		}else{
			result =CommUtil.responseBuild(301, "失败","参数错误");
		}
		return JSON.toJSON(result);
	}
	/**
	 * 根据商户的id添加,删除商户id
	 * @param request
	 * @return
	 */
	@RequestMapping("configMchid")
	@ResponseBody
	public Object configMchid(HttpServletRequest request){
		Object result = null;
		User superUser = CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(superUser != null && superUser.getLevel()==0){
			result = userService.configMchid(request);;
		}else{
			result =CommUtil.responseBuild(301, "失败","参数错误");
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * PC端商家开启自动提现前设置真实姓名
	 * @param merId 商家的id
	 * @param realName 商家的真实姓名
	 * @return {@link Object}
	 */
	@RequestMapping("/setMername")
	@ResponseBody
	public Object autoWithDrawSetMername(Integer merId, String realName){
		if(merId == null || realName == null || "".equals(realName)){
			return CommUtil.responseBuild(400, "参数错误", "");
		}
		try {
			User user2 = new User();
			user2.setId(merId);
			user2.setRealname(realName);
			// 修改用户的真实姓名
			userService.updateUserById(user2);
			return CommUtil.responseBuild(200, "添加成功", "");
		} catch (Exception e) {
			return CommUtil.responseBuild(400, "参数错误", "");
		}
	}
	

	/**
	 * @Description：获取商户子账户信息
	 * @param request  response
	 * @return
	 * @author： origin  2020年7月8日上午9:01:26
	 * @comment: bypass account 
	 */
	@RequestMapping(value="/getBypassAccountData")
	@ResponseBody
    public Object getBypassAccountData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.getAccountData(request);
		}
		return JSON.toJSON(result);
	}
	

	@RequestMapping(value="/addOrDelChileUser")
	@ResponseBody
    public Object addOrDelChileUser(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.addOrDelChileUser(request);
		}
		return JSON.toJSON(result);
	}
}

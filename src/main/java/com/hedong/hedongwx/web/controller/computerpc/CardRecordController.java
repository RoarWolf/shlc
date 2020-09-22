package com.hedong.hedongwx.web.controller.computerpc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Controller
@RequestMapping(value = "/pccardrecord")
public class CardRecordController {
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private HttpServletRequest request;
	
	/*** IC卡记录查询  ********************************************************************************/
	//查询在线卡信息 hd_card
	@RequestMapping(value = "/selectonlinecard")
	public String selectOnlineCard(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = onlineCardService.selectOnlinecard(request);
		model.addAttribute("cardnumber", request.getParameter("cardnumber"));
		model.addAttribute("figure", request.getParameter("figure"));
		model.addAttribute("nickname", request.getParameter("nickname"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("areaname", request.getParameter("areaname"));
		model.addAttribute("remark", request.getParameter("remark"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("onlinecard", pageBean.getListMap());
		return "computer/onlinecard";
	}
	
	//根据用户id查询用户名下的所有在线卡信息
	@RequestMapping(value = "/selectonlinecardbyuid")
	public String selectOnlineCardByUid(Integer uid, Model model) {
		List<Map<String, Object>> usonlinecard = onlineCardService.selectonlinecardbyuid(uid);
		model.addAttribute("onlinecard", usonlinecard);
		return "computer/onlinecarduser";
	}
	
	//查询在线卡操作记录信息 hd_onlinecard_record 		/pccardrecord/selectcardoperation
	@RequestMapping(value = "/selectcardoperation")
	public String selectCardOperation(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = onlineCardRecordService.selectOnlineOperation(request);
		model.addAttribute("nickname", request.getParameter("nickname"));
		model.addAttribute("cardID", request.getParameter("cardID"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("cardoperation", pageBean.getListMap());
		return "computer/onlinecardoperation";
	}
	
	/**
	 * @Description： 查询显示在线卡操作记录
	 * @author： origin  
	 */
	@RequestMapping(value = "/onlineoperate")
	public String onlineOperate(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = onlineCardRecordService.selectOnlineOperation(request);
		model.addAttribute("nickname", request.getParameter("nickname"));
		model.addAttribute("cardID", request.getParameter("cardID"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("cardoperation", pageBean.getListMap());
		return "comorder/onlineoperate";
	}
	
	//查询在线卡消费记录信息 hd_onlinecard_record 		
	@RequestMapping(value = "/selectonlineconsume")
	public String selectOonlineConsume(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = onlineCardRecordService.selectOonlineConsume(request);
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("nickname", request.getParameter("nickname"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("cardID", request.getParameter("cardID"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("type", request.getParameter("type"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("cardconsume", pageBean.getListMap());
		return "computer/onlinecardconsume";
	}
	
	/*** 卡操作记录查询  ********************************************************************************/
	//卡操作——根据ID查询该条数据
	@RequestMapping(value = "/onlinecardById")
	@ResponseBody
	public OnlineCard selectOnlinecardById(Integer Id) {
		OnlineCard online = onlineCardService.selectOnlineCardById(Id);
		return online;
	}
	
	//卡操作——根据cardID查询该条数据
	@RequestMapping(value = "/onlinecardBycardID")
	@ResponseBody
	public OnlineCard selectOnlineCardByCardID(String cardID) {
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardID);
		return online;
	}
	
	//添加在线卡
	@RequestMapping(value = "/insertonlinecard")
	@ResponseBody
	public int insertonlinecard(HttpServletRequest request, HttpServletResponse response, Model model) {
		OnlineCard onlineCard = new OnlineCard();
		//id、cardID、cardmoney、status、remark
		onlineCard.setStatus(StringUtil.getIntString(request.getParameter("status")));
		onlineCard.setRemark(request.getParameter("remark"));
		onlineCard.setCardID(request.getParameter("cardID"));
		onlineCard.setMoney(0.00);
		//onlineCard.setMoney(request.getParameter("cardmoney"));
		int num = onlineCardService.insertOnlineCard(onlineCard);
		return num;
	} 	
	
	//在线卡激活、挂失、解挂操作
	@RequestMapping(value = "/updateonlinecard")
	@ResponseBody
	public int updateOnlinecard(String genre, Integer Id ) {
		int status = 0;
		int setstatus = 0;
		if(genre.equals("0")){//激活
			status = 1;
			setstatus = 2;
		}else if(genre.equals("1")){//挂失
			status = 2;
			setstatus = 5;
		}else if(genre.equals("2")){//解挂
			status = 1;
			setstatus = 6;
		}
		OnlineCard onlineCard = new OnlineCard();
		//id、cardID、cardmoney、status、remark
		onlineCard.setId(Id);
		onlineCard.setStatus(status);
		int num = onlineCardService.updateOnlineCard(onlineCard);
		additionOnlineCardRecord( Id, setstatus);
		return num;
	}
	
	//卡操作——解绑、绑定用户
	@RequestMapping(value = "/onbinding")
	@ResponseBody
	public int onbindingCard(String genre, Integer Id, Integer uid) {
		int setstatus = 0;
		int num = 0;
		if(genre.equals("0")){//解绑
			setstatus = 4;
			num = onlineCardService.updateOnlineCarduIdbyId(Id);////解除绑定用户(即将卡用户uid置为Null)
		}else if(genre.equals("1")){//绑定
			setstatus = 3;
			OnlineCard onlineCard = new OnlineCard();
			//id、cardID、cardmoney、status、remark
			onlineCard.setId(Id);
			onlineCard.setUid(uid);
			onlineCard.setStatus(1);
			num = onlineCardService.updateOnlineCard(onlineCard);
		}
		additionOnlineCardRecord( Id, setstatus );
		return num;
	}
	
	//
	public OnlineCard additionOnlineCardRecord( Integer Id, Integer status) {
		OnlineCard online = onlineCardService.selectOnlineCardById(Id);
		OnlineCardRecord onlineCardRecord = new OnlineCardRecord();
		User admin = CommonConfig.getAdminReq(request);
		onlineCardRecord.setStatus(status);
		onlineCardRecord.setUid(online.getUid());
		onlineCardRecord.setMerid(online.getMerid());
		onlineCardRecord.setOperid(admin.getId());
		onlineCardRecord.setOrdernum(HttpRequest.createOrdernum(6));
		onlineCardRecord.setCardID(online.getCardID());
		onlineCardRecord.setBalance(online.getMoney());
		onlineCardRecord.setMoney(0.00);
		onlineCardRecord.setAccountmoney(0.00);
		onlineCardRecord.setType(4);
		onlineCardRecord.setCreateTime(new Date());
		onlineCardRecordService.additionOnlineCardRecord(onlineCardRecord);
		return online;
	}
	
	
	
	//修改在线卡信息
	@RequestMapping(value = "/editonlinecard")
	@ResponseBody
	public int editonlinecard(HttpServletRequest request, HttpServletResponse response ) {
		User admin = CommonConfig.getAdminReq(request);
		OnlineCard onlineCard = new OnlineCard();
		//id、cardID、cardmoney、status、remark
		String status = request.getParameter("status");
		int Id = StringUtil.getIntString(request.getParameter("id"));
		onlineCard.setId(Id);
		onlineCard.setStatus(StringUtil.getIntString(status));
		onlineCard.setRemark(request.getParameter("remark"));
		int num = onlineCardService.updateOnlineCard(onlineCard);
		OnlineCard online = onlineCardService.selectOnlineCardById(Id);
		Integer onstatus = online.getStatus();
		OnlineCardRecord onlineCardRecord = new OnlineCardRecord();
		//卡状态：0初始化、1 正常，2 挂失，3 注销',
		///** 状态 0:支付未成功、 1:支付完成 、2:激活、3:绑定、4:解绑、5:挂失、6:解挂 7注销*/
		int setstatus = 0;
		if(onstatus==1){//正常卡
			if(status.equals("2")) setstatus = 5;
			else if(status.equals("3")) setstatus = 7;
		}else if(onstatus==2){//挂失卡
			if(status.equals("1")) setstatus = 4;
			else if(status.equals("3")) setstatus = 7;
		}else if(onstatus==3){//注销卡
			if(status.equals("1")) setstatus = 2;
			else if(status.equals("2")) setstatus = 5;
		}
		onlineCardRecord.setStatus(setstatus);
		onlineCardRecord.setUid(online.getUid());
		onlineCardRecord.setMerid(online.getMerid());
		onlineCardRecord.setOperid(admin.getId());
		onlineCardRecord.setOrdernum(HttpRequest.createOrdernum(6));
		onlineCardRecord.setCardID(online.getCardID());
		onlineCardRecord.setCode("0");
		onlineCardRecord.setBalance(online.getMoney());
		onlineCardRecord.setMoney(0.00);
		onlineCardRecord.setAccountmoney(0.00);
		onlineCardRecord.setType(4);
		onlineCardRecord.setCreateTime(new Date());
		
		onlineCardRecordService.additionOnlineCardRecord(onlineCardRecord);
		return num;
	} 	
	
	//卡操作——解绑用户
	@RequestMapping(value = "/relievebinding")
	@ResponseBody
	public int relievebinding(Integer Id) {//解除绑定用户
		int num = onlineCardService.updateOnlineCarduIdbyId(Id);////解除绑定用户
		OnlineCard online = onlineCardService.selectOnlineCardById(Id);
		OnlineCardRecord onlineCardRecord = new OnlineCardRecord();
		User admin = CommonConfig.getAdminReq(request);
		onlineCardRecord.setStatus(4);
		onlineCardRecord.setUid(0);
		onlineCardRecord.setMerid(online.getMerid());
		onlineCardRecord.setOperid(admin.getId());
		onlineCardRecord.setOrdernum(HttpRequest.createOrdernum(6));
		onlineCardRecord.setCardID(online.getCardID());
		onlineCardRecord.setCode("0");
		onlineCardRecord.setBalance(online.getMoney());
		onlineCardRecord.setMoney(0.00);
		onlineCardRecord.setAccountmoney(0.00);
		onlineCardRecord.setType(4);
		onlineCardRecord.setCreateTime(new Date());
		onlineCardRecordService.additionOnlineCardRecord(onlineCardRecord);
		return num;
	}
	
	
	//卡操作——删除	  逻辑删除（注销） 修改状态
	@RequestMapping(value = "/deletecardbyId")
	@ResponseBody
	public int deletecardbyId(Integer Id) {//逻辑删除（注销）
		OnlineCard onlineCard = new OnlineCard();
		onlineCard.setId(Id);
		onlineCard.setStatus(3);
		int num = onlineCardService.updateOnlineCard(onlineCard);
		return num;
	}
	
	//卡操作——删除	   物理删除（删除该条数据）
	@RequestMapping(value = "/removecardbyId")
	@ResponseBody
	public int removecardbyId(Integer Id) {//物理删除（删除该条数据）
		int num = onlineCardService.removecardbyId(Id);
		return num;
	}
	
	/***********************************************************************************************************/
	
	
}

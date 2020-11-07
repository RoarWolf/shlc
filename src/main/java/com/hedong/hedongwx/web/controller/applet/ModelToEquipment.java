package com.hedong.hedongwx.web.controller.applet;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.WolfHttpRequest;

/**
 * 通信模块到设备
 * 
 * @author zz
 *
 */
@RestController
@RequestMapping("/deviceConnect")
@EnableScheduling
public class ModelToEquipment {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	
//	@Scheduled(cron = "0/10 * * * * *")
//	public void hartTask() {
//		List<String> devicelist = Server.devicenumList;
//		if (devicelist.size() > 0) {
//			for (String devicenum : devicelist) {
//				SendMsgUtil.send_0x0B(devicenum);
//			}
//		}
//	}
//	
//	@Scheduled(cron = "0 0 3 * * *")
//	public void setTimeTask() {
//		List<String> devicelist = Server.devicenumList;
//		if (devicelist.size() > 0) {
//			for (String devicenum : devicelist) {
//				SendMsgUtil.send_0x06(devicenum);
//			}
//		}
//	}
	
	/**
	 * 预约
	 * @param devicenum
	 * @param port
	 * @param userid
	 * @param userType
	 * @param phonenum
	 * @return
	 */
	@PostMapping("/yuyueCharge")
	public Object yuyueCharge(String devicenum, String port) {
		boolean selectDeviceExsit = equipmentService.selectDeviceExsit(devicenum);
		if (!selectDeviceExsit) {
			return CommUtil.responseBuildInfo(1003, "当前设备不存在", null);
		}
//		String useridStr = DisposeUtil.completeNum(userid + "", 8);
		AllPortStatus allPortStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(devicenum, Integer.parseInt(port));
		if (allPortStatus.getPortStatus() == 1) {
			return CommUtil.responseBuildInfo(1000, "枪号可用", null);
		} else {
			return CommUtil.responseBuildInfo(1001, "枪被占用，不可用", null);
		}
//		return WolfHttpRequest.sendYuyueChargedata(devicenum, port, userid, userType, phonenum);
//		return SendMsgUtil.send_0x1B(devicenum, Byte.parseByte(port), useridStr, Byte.parseByte(userType), phonenum);
	}
	
	/**
	 * 取消预约
	 * @param devicenum
	 * @param port
	 * @return
	 */
	@PostMapping("/cancelyuyue")
	public Object cancelyuyue(String devicenum, Integer port) {
		
		SendMsgUtil.send_0x1D(devicenum, port.byteValue());
		return CommUtil.responseBuildInfo(1000, "取消成功", null);
	}
	
	/**
	 * 启动充电
	 * @param ordernum 
	 * @return
	 */
	@PostMapping("/startCharge")
	public Object startCharge(String ordernum) {
//		return SendMsgUtil.backChargeInfo(ordernum);
		return WolfHttpRequest.sendChargePaydata(ordernum);
	}
	
	/**
	 * 停止充电
	 * @param devicenum
	 * @param port
	 * @return
	 */
	@PostMapping("/stopCharge")
	public Object stopCharge(String devicenum, Integer port) {
//		SendMsgUtil.send_0x26(devicenum, port.byteValue());
//		return SendMsgUtil.backStopCahrgeInfo(devicenum, port);
		return WolfHttpRequest.sendStopChargedata(devicenum, port);
	}
	
	/**
	 * 平台设置新 IP 地址
	 * @param devicenum 充电桩编号
	 * @param serverIP 服务器ip
	 * @param serverPort 端口
	 * @return
	 */
	@PostMapping("/serIpaddr")
	public Object stopCharge(String devicenum, String serverIP, Integer serverPort) {
		SendMsgUtil.send_0x35(devicenum, serverIP, serverPort.shortValue());
		return null;
	}
	
	
}

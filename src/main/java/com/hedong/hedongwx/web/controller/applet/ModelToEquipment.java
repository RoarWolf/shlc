package com.hedong.hedongwx.web.controller.applet;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.SendMsgUtil;

/**
 * 通信模块到设备
 * 
 * @author zz
 *
 */
@RestController
@RequestMapping("/deviceConnect")
public class ModelToEquipment {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private EquipmentService equipmentService;
	
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
	public Object yuyueCharge(String devicenum, String port, Integer userid, String userType,
			String phonenum) {
		System.out.println("devicenum:" + devicenum);
		System.out.println("port:" + port);
		System.out.println("userid:" + userid);
		System.out.println("userType:" + userType);
		System.out.println("phonenum:" + phonenum);
//		Equipment equipment = equipmentService.getEquipmentById(devicenum);
//		if (equipment == null) {
//			return CommUtil.responseBuildInfo(1003, "当前设备不存在", null);
//		}
		String useridStr = DisposeUtil.completeNum(userid + "", 8);
		return SendMsgUtil.send_0x1B(devicenum, Byte.parseByte(port), useridStr, Byte.parseByte(userType), phonenum);
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
		return SendMsgUtil.backCahrgeInfo(ordernum);
	}
	
	/**
	 * 停止充电
	 * @param devicenum
	 * @param port
	 * @return
	 */
	@PostMapping("/stopCharge")
	public Object stopCharge(String devicenum, Integer port) {
		return SendMsgUtil.send_0x26(devicenum, port.byteValue());
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

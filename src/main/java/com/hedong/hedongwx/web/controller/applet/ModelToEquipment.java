package com.hedong.hedongwx.web.controller.applet;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Object yuyueCharge(String devicenum, Integer port, String userid, Integer userType,
			String phonenum) {
		userid = DisposeUtil.completeNum(userid, 8);
		return SendMsgUtil.send_0x1B(devicenum, port.byteValue(), userid, userType.byteValue(), phonenum);
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
	 * @param devicenum 充电装编号
	 * @param port 枪号
	 * @param userid 用户id
	 * @param userType 
	 * @param groupCode
	 * @param money
	 * @param chargeWay
	 * @param startWay
	 * @param userOperCode
	 * @return
	 */
	@PostMapping("/startCharge")
	public Object startCharge(String devicenum, Integer port, String userid, Integer userType,
			String groupCode, Double money, Integer chargeWay, Integer startWay, String userOperCode) {
		userid = DisposeUtil.completeNum(userid, 8);
		money = money * 100;
		return SendMsgUtil.send_0x1F(devicenum, port.byteValue(), "1", userid, userType.shortValue(), groupCode, (byte) 3, 
				money.intValue(), chargeWay.byteValue(), startWay.byteValue(), userOperCode, (byte)1, null);
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

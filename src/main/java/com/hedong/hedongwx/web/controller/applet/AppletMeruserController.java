package com.hedong.hedongwx.web.controller.applet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.utils.CommUtil;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@RestController
@RequestMapping("/applet/meruser")
public class AppletMeruserController {

	public Object addMeruser(Meruser meruser) {
		return CommUtil.responseBuildInfo(1001, "添加成功", null);
	}
}

package com.hedong.hedongwx.web.controller.webpc;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.systemRole.SystemUser;
import com.hedong.hedongwx.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @describe 用户
 * @author zhangyu
 * @date 2019-12-18
 */
@RestController
@RequestMapping("/system/sysUser")
public class SysUserController {

	/** 用户 **/
	@Autowired
	private SysUserService sysUserService;


	/**
	 * 添加
	 * 
	 * @非空参数：用户名username、密码password
	 */
	@PostMapping("/insertSysUser")
	public Object insert(SystemUser sysUser) {
		return JSON.toJSON(sysUserService.insertUser(sysUser));
	}

	/**
	 * 通过id删除
	 * 
	 * @非空参数：编号id
	 */
	@PostMapping("/deleteById")
	public Object deleteById(Long id) {
		return JSON.toJSON(sysUserService.deleteUserById(id));
	}

	/**
	 * 通过id修改
	 * 
	 * @非空参数：编号id
	 */
	@PostMapping("/updateById")
	public Object updateById(SystemUser sysUser) {
		return JSON.toJSON(sysUserService.updateUserById(sysUser));
	}
    /**
     * 查询所有的系统用户信息
     *
     * @param bean
     * @return
     */
    @GetMapping("/selectAllUserPage")
    public Object selectAllForLayUI(SystemUser bean) {
        return JSON.toJSON(sysUserService.selectSysUserList(bean));
    }

	/**
	 * 修改密码
	 * 
	 * @非空参数：编号id
	 */
/*	@PostMapping("/updatePassword")
	public RespBean<Map<String, Object>> updatePassword(SysUser sysUser, String newPassword) {
		if (GuoStringUtil.isEmpty(new String[] { sysUser.getId().toString(), sysUser.getPassword(), newPassword }))
			return GuoRespBeanUtil.initParamNotNullRespBean();
		return sysUserService.updatePassword(sysUser, newPassword);
	}*/






}

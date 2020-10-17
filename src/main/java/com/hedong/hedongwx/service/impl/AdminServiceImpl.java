package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.AdminDao;
import com.hedong.hedongwx.entity.Admin;
import com.hedong.hedongwx.service.AdminService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.MD5Util;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminDao adminDao;

	@Override
	public Map<String,Object> insertAdmin(Admin admin) {
		try {
			String password = admin.getPassword();
			admin.setEncryptPassword(MD5Util.MD5Encode(password, null));
			int result = adminDao.insertAdmin(admin);
			if (result > 0) {
				return CommUtil.responseBuildInfo(200, "添加成功", null);
			} else {
				return CommUtil.responseBuildInfo(201, "添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(201, "添加失败", null);
		}
	}

	@Override
	public Map<String,Object> updateAdmin(Admin admin) {
		try {
			String password = admin.getPassword();
			admin.setEncryptPassword(MD5Util.MD5Encode(password, null));
			int result = adminDao.updateAdmin(admin);
			if (result > 0) {
				return CommUtil.responseBuildInfo(200, "添加成功", null);
			} else {
				return CommUtil.responseBuildInfo(201, "添加失败", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "添加失败", null);
		}
	}

	@Override
	public Map<String,Object> selectAdminById(Integer id) {
		Map<String,Object> map = new HashMap<>();
		try {
			Admin admin = adminDao.selectAdminById(id);
			map.put("adminInfo", admin);
			if (admin != null) {
				return CommUtil.responseBuildInfo(200, "查询成功", map);
			} else {
				return CommUtil.responseBuildInfo(201, "查询失败", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuild(201, "查询失败", e.getMessage());
		}
	}

	@Override
	public Map<String,Object> selectAdminList(Admin admin) {
		Map<String,Object> map = new HashMap<>();
		System.out.println("startindex===" + admin.getStartindex());
		try {
			if (admin.getStartindex() == null) {
				admin.setStartindex(0);
			}
			List<Admin> adminList = adminDao.selectAdminList(admin);
			if (adminList != null && adminList.size() > 0) {
				map.put("adminlist", adminList);
				return CommUtil.responseBuildInfo(200, "查询成功", map);
			} else {
				map.put("adminlist", new ArrayList<>());
				return CommUtil.responseBuildInfo(201, "查询失败", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(201, "查询失败", e.getMessage());
		}
	}
	
	@Override
	public Map<String,Object> selectAdminMenu(Integer id) {
		Map<String,Object> map = new HashMap<>();
		try {
			Admin adminList = adminDao.selectAdminMenu(id);
			if (adminList != null) {
				map.put("adminlist", adminList);
				return CommUtil.responseBuildInfo(200, "查询成功", map);
			} else {
				map.put("adminlist", new ArrayList<>());
				return CommUtil.responseBuildInfo(201, "查询失败", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(201, "查询失败", e.getMessage());
		}
	}

	@Override
	public Map<String, Object> selectAllMenu(Integer id) {
		return null;
	}

}

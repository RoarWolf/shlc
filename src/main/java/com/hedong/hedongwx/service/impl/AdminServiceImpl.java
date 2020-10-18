package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.AdminDao;
import com.hedong.hedongwx.entity.Admin;
import com.hedong.hedongwx.entity.Menu;
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
				return CommUtil.responseBuildInfo(200, "修改成功", null);
			} else {
				return CommUtil.responseBuildInfo(201, "修改失败", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "修改失败", null);
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
			Admin admin = adminDao.selectAdminMenu(id);
			if (admin != null) {
				map.put("admin", admin);
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
	public Map<String, Object> selectSetAdminMenu(Integer id) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<Menu> menuList = adminDao.selectAllMenu();
			Admin admin = adminDao.selectAdminMenu(id);
			if (admin != null) {
				List<Menu> adminMenulist = admin.getMenulist();
				if (adminMenulist != null && adminMenulist.size() > 0) {
					for (Menu menu : menuList) {
						for (Menu menu2 : adminMenulist) {
							if (menu.getId().equals(menu2.getId())) {
								menu.setIscheck(true);
							} else {
								List<Menu> subMenulist = menu.getSubMenulist();
								for (Menu menu3 : subMenulist) {
									if (menu3.getId().equals(menu2.getId())) {
										menu3.setIscheck(true);
									}
								}
							}
						}
					}
				}
			}
			if (menuList != null) {
				map.put("menuList", menuList);
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

	@Transactional
	@Override
	public int updateAdminMenu(Integer id, List<Integer> menuidlist) {
		List<Integer> adminMenulist = adminDao.selectAdminMenuList(id);
		List<Integer> insertlist = new ArrayList<>();
		List<Integer> deletelist = new ArrayList<>();
		for (Integer menuid : adminMenulist) {
			if (!menuidlist.contains(menuid)) {//传入权限list中不含
				deletelist.add(menuid);
			}
		}
		for (Integer menuid : menuidlist) {
			if (!adminMenulist.contains(menuid)) {//传入权限list中不含
				insertlist.add(menuid);
			}
		}
		adminDao.insertAdminMenu(id, insertlist);
		adminDao.deleteAdminMenu(deletelist);
		return 0;
	}

}

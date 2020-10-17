package com.hedong.hedongwx.dao;

import java.util.List;

import com.hedong.hedongwx.entity.Admin;
import com.hedong.hedongwx.entity.Menu;

public interface AdminDao {

    int insertAdmin(Admin admin);
    
    int updateAdmin(Admin admin);
    
    Admin selectAdminById(Integer id);
    
    Admin selectAdminMenu(Integer id);
    
    List<Menu> selectAllMenu();
    
    List<Admin> selectAdminList(Admin admin);
    
}
package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Admin;
import com.hedong.hedongwx.entity.Menu;

public interface AdminDao {

    int insertAdmin(Admin admin);
    
    int updateAdmin(Admin admin);
    
    Admin selectAdminById(Integer id);
    
    Admin selectAdminMenu(Integer id);
    
    List<Menu> selectAllMenu();
    
    List<Admin> selectAdminList(Admin admin);
    
    Integer selectAllAdminSize();
    
    List<Integer> selectAdminMenuList(Integer id);
    
    int insertAdminMenu(@Param("id") Integer id, @Param("menuidlist") List<Integer> menuidlist);
    
    int deleteAdminMenu(@Param("menuidlist") List<Integer> menuidlist);

    Admin selectAdmin(@Param("username") String username);
}
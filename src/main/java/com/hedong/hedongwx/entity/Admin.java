package com.hedong.hedongwx.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 管理员
 * @author RoarWolf
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Admin {
	
	/** 表自增id*/
    private Integer id;

    /** 姓名*/
    private String name;

    /** 用户名*/
    private String username;

    /** 密码*/
    private String password;

    /** 加密后密码*/
    private String encryptPassword;

    /** 是否可用*/
    private Byte enabled;
    
    /** 创建时间*/
    private Date createTime;
    
    /** 修改时间*/
    private Date updateTime;
    
    private List<Menu> menulist;
    
    private Integer startindex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword == null ? null : encryptPassword.trim();
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<Menu> getMenulist() {
		return menulist;
	}

	public void setMenulist(List<Menu> menulist) {
		this.menulist = menulist;
	}

	public Integer getStartindex() {
		return startindex;
	}

	public void setStartindex(Integer startindex) {
		this.startindex = startindex;
	}
	
	
}

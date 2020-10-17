package com.hedong.hedongwx.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 菜单
 * @author RoarWolf
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Menu {
	
	/** 表自增id*/
    private Integer id;

    /** 路径*/
    private String url;

    /** 前端组件*/
    private String component;

    /** 名称*/
    private String name;

    /** 父菜单id*/
    private Integer parentid;
    
    private boolean ischeck;
    
    private List<Menu> subMenulist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component == null ? null : component.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public List<Menu> getSubMenulist() {
		return subMenulist;
	}

	public void setSubMenulist(List<Menu> subMenulist) {
		this.subMenulist = subMenulist;
	}
}

package com.hedong.hedongwx.utils;

import java.util.List;
import java.util.Map;

public class PageUtils<T> {
	
	//一页显示的记录数,默认显示10条
	private int numPerPage;
	//记录总数
	private int totalRows;
	//总页数
	private int totalPages;
	//当前页码
	private int currentPage;
	//起始行数
	private int startIndex;
	//结束行数
	private int lastIndex;
	//分页显示的起始页数,比如在页面上显示1，2，3，4，5页，start就为1，end就为5，这个也是算过来的
    private int start;
    //分页显示的结束页数,
    private int end;
    
	//结果集存放List
	private List<T> list;
	
	private List<Map<String, Object>> ListMap;
	

	private Map<String, Object> map;
	//默认一页显示记录
	private int defaultNumPerPage = 10;
	//默认显示几个页码
	private int defaultNumPage = 5;
	
	
	
	public PageUtils(int numPerPage, int currentPage) {
		setCurrentPage(currentPage);//（当前）页数
		setNumPerPage(numPerPage);//条数
		setStartIndex();//起始查询位置
	}
	 
	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		if(numPerPage != 0){
			this.numPerPage = numPerPage;
		}else{
			this.numPerPage = defaultNumPerPage;
		}
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages() {
		if(totalRows % numPerPage == 0){
			this.totalPages = totalRows / numPerPage;
		}else{
			this.totalPages = (totalRows / numPerPage) + 1;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if(currentPage < 1){
			this.currentPage = 1;
		}else{
			this.currentPage = currentPage;
		}
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex() {
		this.startIndex = (currentPage - 1) * numPerPage;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex() {
		if(totalRows < numPerPage){
			this.lastIndex = totalRows;
		}else{
			this.lastIndex = numPerPage;//limit 10,5表示查询第11到第15条
		}
	}
	
	public int getStart() {
		return start;
	}

	public void setStart() {
		if(totalPages<=defaultNumPage){
			this.start = 1;
		}else if(totalPages>defaultNumPage){
			if(currentPage<=3){
				this.start = 1;
			}else{
				this.start = currentPage-2;
			}
		}
	}

	public int getEnd() {
		return end;
	}

	public void setEnd() {
		if(totalPages<=defaultNumPage){
			this.end = totalPages;
		}else if(totalPages>defaultNumPage){
			if(currentPage<=3){
				this.end = 5;
			}else{
				this.end = currentPage+2;
			}
		}
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getDefaultNumPerPage() {
		return defaultNumPerPage;
	}

	public void setDefaultNumPerPage(int defaultNumPerPage) {
		this.defaultNumPerPage = defaultNumPerPage;
	}

	public List<Map<String, Object>> getListMap() {
		return ListMap;
	}

	public void setListMap(List<Map<String, Object>> listMap) {
		this.ListMap = listMap;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	













	
	
	
	
}

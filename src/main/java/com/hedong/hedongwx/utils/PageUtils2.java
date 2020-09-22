package com.hedong.hedongwx.utils;

import java.util.List;

public class PageUtils2<T> {
	
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
	//默认一页显示记录
	private int defaultNumPerPage = 10;
	
	

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

	public void setStartIndex(int startIndex) {
		this.startIndex = (currentPage - 1) * numPerPage;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		if(totalRows < numPerPage){
			this.lastIndex = totalRows;
		}else{
			this.lastIndex = numPerPage;//limit 10,5表示查询第11到第15条
		}
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
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













	
	
	
	
}

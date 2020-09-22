<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 构建分页导航 --%>
	共有${pageBean.totalRows}条数据，共${pageBean.totalPages}页，当前为${pageBean.currentPage}页
	<br/>
	 <span class="btn btn-success" onclick="currentPage(0)">首页</span>
	 <c:if test="${pageBean.currentPage >1}">
	 	<span class="btn btn-success" onclick="currentPage(2)">上一页</span>
	 </c:if>
	 
	 <c:forEach begin="${pageBean.start}" end="${pageBean.end}" step="1" var="i">
	             	<span onclick="currentPagenum(${i})">${i} </span>
	 </c:forEach>
	 
	 <c:if test="${pageBean.currentPage < pageBean.totalPages}">
	 	<span class="btn btn-success" onclick="currentPage(3)">下一页</span>
	 </c:if>
	<span class="btn btn-success" onclick="currentPage(1)">尾页</span>
	<form style="display: inline;">
		<input type="text" name="pageNumber" style="width: 50px"> 
		<input class="btn btn-info" type="button" onclick="currentPage(4)" value="go">
	</form>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/> 
<title>历史统计</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">历史统计</a></li>
	</ul>
</div>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
  	<div class="conditionsd">
	  <form method="post" action="${hdpath}/pcstatistics/statisticsinfo" id="listform">
	    <div class="searchdiv">
	      <ul class="search" style="padding-left:10px;">
	     		<li>总订单&nbsp;&nbsp;
	     		<select name="ordertotal">
					<option value="0">请选择</option>
					<option value="1" <c:if test="${ordertotal == 1}"> selected="selected"</c:if> >从大到小</option>
					<option value="2" <c:if test="${ordertotal == 2}"> selected="selected"</c:if> >从小到大</option>
				</select>
	     		</li>
	     		<li>总金额&nbsp;&nbsp;
	     		<select name="ordermoney">
					<option value="0">请选择</option>
					<option value="1" <c:if test="${ordermoney == 1}"> selected="selected"</c:if> >从大到小</option>
					<option value="2" <c:if test="${ordermoney == 2}"> selected="selected"</c:if> >从小到大</option>
				</select>
	     		</li>
	     		<li>时间:&nbsp;&nbsp;<input type="text" name="begintime" placeholder="请选择时间" value="${begintime}" onclick="new Calendar().show(this);" size="10" maxlength="10" readonly="readonly">
					  &nbsp;&nbsp; —
					&nbsp;&nbsp;<input type="text" name="endtime" placeholder="请选择时间" value="${endtime}" onclick="new Calendar().show(this);" size="10" maxlength="10" readonly="readonly">
				</li>
	       		<li class="cmbquery">
	            	<input type="submit" style="width: 80px;" value="搜索">
	      	 	</li>
	        </ul>
	   </div>
	</form>
  </div>
  <div class="table table-div">
	<table class="table table-hover" >
	    <thead>
		   <tr>
			<!-- <th>id</th> -->
			<th>时间</th>
			<th>总金额</th>
			<th>总订单</th>
			<th>微信金额</th>
			<th>微信订单</th>
			<th>支付宝金额</th>
			<th>支付宝订单</th>
			<th>微信退费金额</th>
			<th>微信退费订单</th>
			<th>支付宝退费金额</th>
			<th>支付宝退费订单</th>
			<th>投币金额</th>
			<th>投币订单</th>
		  </tr>
		</thead>
		<tbody>
		   <c:forEach items="${statistics}" var="statis"  varStatus="as">
			  <tr id="name${statis.id}" >
				<%-- <td>${statis.id}</td> --%>
				<td><fmt:formatDate value="${statis.count_time}" pattern="yyyy-MM-dd" /></td>
				<td>${statis.moneytotal}</td>
			    <td>${statis.ordertotal}</td>
				<td>${statis.wecmoney}</td>
				<td>${statis.wecorder}</td>
				<td>${statis.alimoney}</td>
				<td>${statis.aliorder}</td>
				<td>${statis.wecretmoney}</td>
				<td>${statis.wecretord}</td>
				<td>${statis.aliretmoney}</td>
				<td>${statis.aliretord}</td>
				<td>${statis.incoinsmoney}</td>
				<td>${statis.incoinsorder}</td>
			  </tr>
			</c:forEach>
			  <tr> 
			  	<td>总计：</td>
				<td>${statotal.tmoneytotal}</td>
				<td>${statotal.tordertotal}</td>
				<td>${statotal.twecmoney}</td>
				<td>${statotal.twecorder}</td>
				<td>${statotal.talimoney}</td>
				<td>${statotal.taliorder}</td>
				<td>${statotal.twecretmoney}</td>
				<td>${statotal.twecretord}</td>
				<td>${statotal.taliretmoney}</td>
				<td>${statotal.taliretord}</td>
				<td>${statotal.tincoinsmoney}</td>
				<td>${statotal.tincoinsorder}</td>
			  </tr>
		 </tbody>
	   </table>
		<div align="center">
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
		</div>
 	</div>
  </div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#12'+' a').addClass('at');
	$('#12').parent().parent().parent().prev().removeClass("collapsed");
	$('#12').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#12').parent().parent().parent().addClass("in");
	$('#12').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcstatistics/statisticsinfo?"+arguments; 
}
function  currentPage(mark){
	var arguments = $("#listform").serialize();
	if(mark==0){//首页
		arguments += "&currentPage=1"; 
	}else if(mark==1){//末页
		arguments += "&currentPage="+${pageBean.totalPages}; 
	}else if(mark==2){//上一页
		arguments += "&currentPage="+${pageBean.currentPage-1};
	}else if(mark==3){//下一页
		arguments += "&currentPage="+${pageBean.currentPage+1}; 
	}else if(mark==4){//go跳转页
		var currentPage = $("input[name='pageNumber']").val();
		if(currentPage==null ||currentPage==""){
			alert("请输入页码!");
			return;
		}
		arguments += "&currentPage="+currentPage; 
	}
	window.location.href="${pageContext.request.contextPath}/pcstatistics/statisticsinfo?"+arguments; 
}
</script>
</html>
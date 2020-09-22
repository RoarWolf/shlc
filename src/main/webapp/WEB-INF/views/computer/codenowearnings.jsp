<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>设备今日收益汇总统计</title>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备今日收益统计</a></li>
	</ul>
</div>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
  	<div class="conditionsd">
	  <form method="post" action="${hdpath}/pcstatistics/nowcodeearnings" id="listform">
	    <div class="searchdiv">
	      <ul class="search" style="padding-left:10px;">
	     		<li>
		     		设备号:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入设备号" name="code" class="frame2" value="${code}"  />
		     	</li>
	     		<%-- <li>
		     		商户名:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入商户名" name="nickname" class="frame3" value="${nickname}"  />
		     	</li>
	     		<li>
		     		小区:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入小区" name="areaname" class="frame2" value="${areaname}"  />
		     	</li> --%>
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
			<th>时间</th>
			<th>设备号</th>
			<th>归属商户</th>
			<th>归属小区</th>
			<th>总收益</th>
			<th>总订单</th>
			<th>微信收益</th>
			<th>微信订单</th>
			<th>支付宝收益</th>
			<th>支付宝订单</th>
			<th>微信退费</th>
			<th>微信退费订单</th>
			<th>支付宝退费</th>
			<th>支付宝退费订单</th>
			<!-- <th>投币金额</th>
			<th>投币订单</th> -->
		  </tr>
		</thead>
		<tbody>
		   <c:forEach items="${codeEarnings}" var="statis"  varStatus="as">
			  <tr>
				<%-- 
				<td><fmt:formatDate value="${statis.counttime}" pattern="yyyy-MM-dd" /></td> --%>
				<td>${statis.counttime}</td>
				<td>${statis.code}</td>
				<td>${statis.realname !=null ? statis.realname:"— —"}</td>
				<td>${statis.name !=null ? statis.name:"— —"}</td>
				<td>${statis.moneytotal}</td>
			    <td>${statis.ordertotal}</td>
				<td>${statis.wechatmoney}</td>
			    <td>${statis.wechatnum}</td>
				<td>${statis.alipaymoney}</td>
				<td>${statis.alipaynum}</td>
				<td>${statis.refwechatmoney}</td>
				<td>${statis.refwechatnum}</td>
				<td>${statis.refalipaymoney}</td>
				<td>${statis.refalipaynum}</td>
				<%-- <td>${statis.incoinsmoney}</td>
				<td>${statis.incoins}</td> --%>
			  </tr>
			</c:forEach>
			  <tr style="color:green;background: #efefef;">
			    <td colspan="4">全部汇总</td>
			    <td>${calculate.moneytotal}</td>
			    <td>${calculate.ordertotal}</td>
			    <td>${calculate.wechatmoney}</td>
			    <td>${calculate.wechatorder}</td>
			    <td>${calculate.alipaymoney}</td>
			    <td>${calculate.alipayorder}</td>
			    <td>${calculate.wechatretmoney}</td>
			    <td>${calculate.wechatretord}</td>
			    <td>${calculate.alipayretmoney}</td>
			    <td>${calculate.alipayretord}</td>
			    <%-- <td>${calculate.incoinsmoney}</td>
			    <td>${calculate.incoinsorder}</td> --%>
			  </tr>
			  <tr style="color:green;background: #efefef;"><td colspan="14"><font style="float: left;"><a href="/pcstatistics/codeearningscollect">设备历史收益信息</a></font></td></tr>
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
	$('#13'+' a').addClass('at');
	$('#13').parent().parent().parent().prev().removeClass("collapsed");
	$('#13').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#13').parent().parent().parent().addClass("in");
	$('#13').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcstatistics/nowcodeearnings?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcstatistics/nowcodeearnings?"+arguments; 
}
</script>
</html>
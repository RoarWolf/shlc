<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/> 
<title>用户在线卡信息</title>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">用户在线卡信息</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd"></div>
  <div class="table table-div">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>卡号</th>
					<th>金额</th>
					<th>状态</th>
					<th>创建时间</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty onlinecard}"><tr><td colspan="6">暂无信息</td></tr>  </c:if>
				<c:if test="${onlinecard != null}">
					<c:forEach items="${onlinecard}" var="order">
						<tr>
							<td>${order.id}</td>
							<td><a href="/pccardrecord/selectonlinecard?cardnumber=${order.cardID}">${order.cardID}</a></td>
							<td>${order.money}</td>
							<td>${order.status == 0 ? "未激活" :order.status == 1 ? "<font color='#5cb85c'>正常</font>" : order.status == 2 ? "<font color='#ea2e2e'>挂失</font>" : order.status == 3 ? "注销" : "其它"}</td>
							<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
  </div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#22'+' a').addClass('at');
	$('#22').parent().parent().parent().prev().removeClass("collapsed");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#22').parent().parent().parent().addClass("in");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>
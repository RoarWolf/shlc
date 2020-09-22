<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>绑定操作页面</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备绑定</a></li>
	</ul>
</div>
 <div class="admin">
	<div class="panel admin-panel" id="adminPanel">
       <div class="conditionsd"></div>
		 <div class="table table-div">
		   <c:if test="${source==1}">
		   	 <table class="table table-hover" >
			 <thead>
			  <tr>
				<th>商户名</th>
				<th>操作</th>
			  </tr>
			 </thead>
			 <tbody>
			  <c:forEach items="${order}" var="order">
			  <tr>
				<td>${order.username!=null ? order.username : order.realname}</td>
				<td>
					<a href="${hdpath}/pcadminiStrator/bindingagent?uid=${uid}&merid=${order.id}" class="btn btn-danger">绑定</a>
				</td>
			  </tr>
			  </c:forEach>
			 </tbody>
		   </table>
		   </c:if>
		   <c:if test="${source==2}">
		   	 <table class="table table-hover" >
			 <thead>
			  <tr>
				<th>小区名</th>
				<th>操作</th>
			  </tr>
			 </thead>
			 <tbody>
			  <c:forEach items="${order}" var="order">
			  <tr>
				<td>${order.name}</td>
				<td>
					<a href="${hdpath }/pcadminiStrator/bindingarea?uid=${uid}&areaid=${order.id}" class="btn btn-danger">绑定</a>
				</td>
			  </tr>
			  </c:forEach>
			 </tbody>
		   </table>
		   </c:if>
	   </div>
	</div>
 </div>
</body>
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(23); })
</script>
</html>
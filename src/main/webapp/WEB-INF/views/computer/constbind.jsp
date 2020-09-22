<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>绑定页面</title>
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
			<table class="table table-hover" >
			 <thead>
			  <tr>
				<th>用户名</th>
				<th>操作</th>
			  </tr>
			 </thead>
			 <tbody>
			  <c:forEach items="${userlist }" var="user">
			  <tr>
				<td>${user.username}</td>
				<td>
					<a href="${hdpath }/pcequipment/pcuserbingequ?userId=${user.id}&code=${code}" class="btn btn-danger">绑定</a>
				</td>
			  </tr>
			  </c:forEach>
			 </tbody>
		   </table>
	   </div>
	</div>
 </div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#41'+' a').addClass('at');
	$('#41').parent().parent().parent().prev().removeClass("collapsed");
	$('#41').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#41').parent().parent().parent().addClass("in");
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>
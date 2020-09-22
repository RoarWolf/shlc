<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商首页</title>
<%@include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
	<div class="container">
		<div align="center">
			<font size="60px" style="font-weight: 900">总收入</font>
		</div>
		<div align="center">
			<span><font size="40px">65.50</font></span>
		</div>
		<div align="center">
			<a href="${hdpath }/agency/eqipmentall" class="btn btn-info"
				style="height: 50px; width: 200px">查看所代理充电站</a>
			<!-- <button class="btn btn-info" style="height: 50px;width: 200px" onclick="alert('暂未开通此功能');">查看所代理充电站</button> -->
		</div>
		<div align="center" style="padding-top: 10px">
			<a href="${hdpath }/agency/orderdetail" class="btn btn-info"
				style="height: 50px; width: 200px">订单详情</a>
			<!-- <button class="btn btn-info" style="height: 50px;width: 200px" onclick="alert('暂未开通此功能');">订单详情</button> -->
		</div>
		<div align="center" style="padding-top: 10px">
			<%-- <a href="${hdpath }/agency/addeqipment" class="btn btn-info" style="height: 50px;width: 200px">添加设备</a> --%>
			<button class="btn btn-warning" style="height: 50px; width: 200px"
				onclick="alert('暂未开通此功能');">添加设备</button>
		</div>
		<div align="center" style="padding-top: 10px">
			<%-- <a href="${hdpath }/agency/withdraw" class="btn btn-info" style="height: 50px;width: 200px">提现</a> --%>
			<button class="btn btn-success" style="height: 50px; width: 200px"
				onclick="alert('暂未开通此功能');">提现</button>
		</div>
	</div>
</body>
</html>
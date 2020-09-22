<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>小区管理</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
tbody td {
	text-align: center;
	vertical-align: middle;
}
label {
    display: inline-block;
    max-width: 100%;
    margin-bottom: 5px;
    font-weight: 500;
}
</style>
</head>
<body>
	<div class="mui-content">
		<table class="table table-bordered" style="width: 100%;">
		<thead>
			<tr align="center">
				<td><strong><font size="1px">设备号</font></strong></td>
				<td><strong><font size="1px">状态</font></strong></td>
				<td><strong><font size="1px">信号</font></strong></td>
			</tr>
		<thead>
		<tbody>
			<c:forEach items="${equlist }" var="equ">
				<tr align="center">
					<td><font size="1px">${equ.code }</font></td>
					<td>${equ.state == 1 ? "<font color='#44b549' size='1px'>在线</font>" : "<font color='red' size='1px'>离线</font>" }</td>
					<td><font size="1px">${equ.csq }</font></td>
				</tr>
			</c:forEach>
		</tbody>
		</table>
	</div>
</body>
</html>
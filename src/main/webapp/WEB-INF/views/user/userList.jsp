<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>用户列表</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<!-- <style type="text/css">
	table.table-striped tr:nth-child(odd) {
		background: skyblue;
	}
	
	table.table-hover tr:hover {
		background: #bbb;
	}
</style> -->
</head>
<body>
	<div class="container" style="border: 1px solid #f00;">
		<div align="center">
			<font size="20px">用户列表</font> <a class="btn btn-info"
				href="${hdpath }/user/add">添加用户</a> <a class="btn btn-info"
				href="${hdpath }/index">回到主页</a>
				<a href="" onclick="javascript:history.back(-1)" class="btn btn-success">返回上一页</a>
		</div>
		<table border="1px" cellpadding="1px" width="100%" height="80%">
			<tr align="center">
				<td>id</td>
				<td>用户名</td>
				<td>密码</td>
				<td>手机号</td>
				<td>等级</td>
				<td>金额</td>
				<td>注册时间</td>
				<td>更新时间</td>
				<td>所持设备</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${userList }" var="user">
				<tr align="center">
					<td>${user.id }</td>
					<td>${user.username }</td>
					<td>${user.password }</td>
					<td>${user.phoneNum }</td>
					<td>${user.rank }</td>
					<td><fmt:formatNumber value="${user.earnings }" pattern="0.00" /></td>
					<td><fmt:formatDate value="${user.createTime }"
							pattern="yyyy年MM月dd日HH点mm分ss秒" /></td>
					<td><fmt:formatDate value="${user.updateTime }"
							pattern="yyyy年MM月dd日HH点mm分ss秒" /></td>
					<td><a href="${hdpath }/equipment/list?id=${user.id}"
						class="btn btn-info">显示个人所有设备</a></td>
					<td><a href="${hdpath }/user/delete?id=${user.id}"
						class="btn btn-danger" onclick="javascript:return confirm('确定要删除吗?');">删除</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
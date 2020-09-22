<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>普通用户注册页面</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
</head>
<body>
<div class="container">
	<div>
		<font style="width: 100%">普通用户注册</font>
	</div>
	<form action="${hdpath }/general/save" method="post">
		<input type="hidden" name="rank" value="${rank }">
		<div class="form-group">
			<label for="username">请输入用户名</label> <input type="text"
				name="username" class="form-control" id="username"
				placeholder="输入用户名">
		</div>
	<!-- 	<div class="form-group">
			<label for="password">请输入密码</label> <input type="password"
				name="password" class="form-control" id="password"
				 -->placeholder="输入密码">
		</div>
		<div class="form-group">
			<label for="phoneNum">请输入手机号码</label> <input type="text"
				name="phoneNum" class="from-control" id="phoneNum"
				placeholder="输入手机号码">
		</div>
		<div>
			<select class="form-control" name="rank" disabled="disabled">
				<option value="1" <c:if test="${rank=='1'}">selected="selected"</c:if> >生产商</option>
				<option value="2" <c:if test="${rank=='2'}">selected="selected"</c:if> >经销商</option>
				<option value="3" <c:if test="${rank=='3'}">selected="selected"</c:if> >商家</option>
			</select>
		</div>
		<input type="submit" class="btn btn-success" value="确认并注册">
	</form>
</div>
</body>
</html>
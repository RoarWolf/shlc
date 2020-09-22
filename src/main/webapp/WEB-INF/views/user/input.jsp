<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>添加用户</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
.form-control {
	width: 220px;
}
</style>
</head>
<body>
	<div class="container">
	<form action="${hdpath }/user/addSucceed" method="post">
		<div>
			<button class="btn btn-default" disabled="disabled">注册用户页面</button>
			<hr>
		</div>
		<div class="form-group">
			<label for="username">请输入用户名</label> <input type="text"
				name="username" class="form-control" id="username"
				placeholder="输入用户名">
		</div>
		<div class="form-group">
			<label for="password">请输入密码</label> <input type="password"
				name="password" class="form-control" id="password"
				placeholder="输入密码">
		</div>
		<div class="form-group">
			<label for="phoneNum">请输入手机号码</label> <input type="text"
				name="phoneNum" class="from-control" id="phoneNum"
				placeholder="输入手机号码">
		</div>
		<div>
			<select class="form-control" name="rank">
				<option value="1">生产商</option>
				<option value="2">经销商</option>
				<option value="3">商家</option>
			</select>
		</div>
		<input type="submit" class="btn btn-success" value="确认并注册">
	</form>
	<br>
	<a href="${hdpath }/index" class="btn btn-success">回到主页</a>
	<a href="" onclick="javascript:history.back(-1)" class="btn btn-success">返回上一页</a>
	</div>
</body>
</html>
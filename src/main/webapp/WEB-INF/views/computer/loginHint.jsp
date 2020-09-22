<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>登录</title>
<link rel="stylesheet" href="${hdpath}/css/logstyles.css">
<style type="text/css">
.ewm{z-index: 1; text-align: center;}
.ewm canvas{ margin-top: 70px;}
.ewm img{ margin-top: 70px; width: 300, height:300,}
.hintInfo{margin-top: 20px; text-align: center;color: red;}
</style>
</head>
<body>
<div class="jq22-container" style="padding-top:8px">
	<div class="nva">
	  <img alt="自助充电平台" src="${hdpath}/images/tengfuchong.jpg">
	  <span>自助充电平台</span>
	</div>
	<div class="login-wrap">
		<div class="ewm">
			<img alt="和动充电站" src="${hdpath}/images/hedongcd.jpg">
			<%-- <img alt="自助充电平台" src="${hdpath}/images/heteng.jpg"> --%>
		</div>
		<div class="hintInfo">
			<span>${hintInfo}</span>
		</div>
	</div>
</div>
</body>
</html>
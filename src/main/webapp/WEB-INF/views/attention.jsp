<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<meta name="viewport" content="width=640,target-densitydpi=device-dpi,user-scalable=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection"content="telephone=no, email=no" />
	<title>关注公众号</title>
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<link rel="stylesheet" href="${hdpath}/css/base.css">
	<style>
	body {
			background-color: #efeff4;
		}
		.app {
			padding-top: 14%;
		}
		.app img{
			display: block;
			margin: 0 auto 10%;
			width: 60%;
		}
		.app .info {
			font-weight: bold;
			font-size: 30px;
			color: #555;
			text-align: center;
		}
		.app .step {
			padding: 0 10%;
			font-size: 24px;
			color: #666;
		}
		.app .step h1 {
			font-size: 28px;
			color: #333;
			padding-bottom: 10px;
		}
		.app .step ul li {
			padding: 7px 0;
		}
		.app .step ul li .num {
			float: left;
		}
		.app .step ul li .content {
			overflow: hidden;
		}
	</style>
</head>
<body>
	<div class="app">
		<img src="${hdpath}/images/hdQRCode.jpg" alt="二维码" title="二维码">
		<!-- <div class="info">请先长按识别二维码，关注公众号</div> -->
		<div class="step">
			<h1>充电流程：</h1>
			<ul>
				<li><div class="num">1、</div><div class="content">请先长按识别二维码，关注公众号</div></li>
				<li><div class="num">2、</div><div class="content">关掉公众号</div></li>
				<li><div class="num">3、</div><div class="content">打开微信扫一扫，扫描充电桩上的二维码进入充电页面</div></li>
				<li><div class="num">4、</div><div class="content">选择对应的端口号，选择支付方式并支付即可</div></li>
			</ul>
		</div>
	</div>
</body>
</html>
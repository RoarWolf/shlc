<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>异常页面</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link rel="stylesheet" href="${hdpath }/css/base.css">
</head>
<style>
body{
		background-color: #efeff4;
	}
	.app {
		height: 100%;
		width: 100%;
		padding-top: 30px;
	}
	.app .top {
		padding-top: 10%;
	}
	.app .top img{
		width: 70%;
		display: block;
		margin: 0 auto;
	}
	.app .bottom {
		padding: 0 20px
	}
	.app .bottom .info {
		text-align: center;
		padding: 15px 0;
		border-bottom: 1px solid #dedede;
	}
	.app .bottom .detail {
		
		margin: 0 auto;
		font-size: 15px;
		color: #666;
		padding-top: 15px;
	}
	.app .bottom .detail ul {
		padding-left: 8%;
		padding-top: 10px;
	}
	.app .bottom .info {
		font-size: 18px;
		color: #545556;
		font-weight: bold;
	}
	.app .bottom .detail ul li {
		list-style: circle inside;
		line-height: 2em;
	}
	.tel {
		position: absolute;
		left: 15px;
		bottom: 5%;
		font-size: 14px;
		color: #666;
	}
	.ulInfo {
		font-size: 14px;
		color: #666;
		border: 1px solid #ccc;
		padding: 15px;
		background-color: #f5f7fa;
		
		border-radius: 6px;
		margin-top: 15px;
	}
	.ulInfo li {
		line-height: 2em;
		overflow: hidden;
	}
	.ulInfo li span:first-child {
		float: left;
	}
	.ulInfo li span:last-child {
		float: right;
	}
	.line {
		height: 1px;
		background-color: #dedede;
		margin-top: 15px;
	}
</style>
<body>
<div class="container">
	<div class="app">
		<div class="bottom">
			<div class="info">${status == 2001 ? '对不起，当前设备未绑定' : status == 2002 ? '当前设备已离线' : status == 2003 ? '当前设备已到期' : status == 2004 ? '当前设备仅支持微信支付' : status == 2005 ? '当前设备仅支持微信支付' : status == 2006 ? '当前设备暂不支持银联支付' :  status == 2007 ? '银联授权失败' : status == 2008 ? '获取端口数据失败' : '异常'}</div>
			<div class="detail">
				<div class="title">设备信息：</div>
				<ul>
					<c:if test="${code != null}">
						<li>设备编号：${code}</li>
					</c:if>
					<c:if test="${remark != null}">
						<li>设备名称：${remark}</li>
					</c:if>
					<c:if test="${area != null}">
						<li>小区名称：${area}</li>
					</c:if>
					<c:if test="${time != null}">
						<li>到期时间：${time}</li>
					</c:if>
				</ul>
			</div>
			<div class="line"></div>
			<c:choose>
				<c:when test="${status == 2002}">
					<div class="detail">
						<div class="title">可能原因：</div>
						<ul>
							<li>设备供电异常</li>
							<li>设备网络异常</li>
						</ul>
					</div>
				</c:when>
			</c:choose>
		</div>
		<div class="tel">如有疑问，敬请联系：<a href="tel:${phonenum == null ? '4006-315-515' : phonenum}">${phonenum == null ? "4006-315-515" : phonenum}</a></div>
	</div>
</div>
</body>
</html>
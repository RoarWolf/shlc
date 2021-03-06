<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>异常</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
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
</head>
<body>
	<div class="app">
		<div class="bottom">
			<div class="info">${errorinfo}</div>
			<div class="detail">
			<c:if test="${code != null}">
				<div class="title">设备信息：</div>
					<ul>
						<li>设备编号：${code}</li>
						<c:if test="${equipment.remark != null}">
							<li>设备名称：${equipment.remark}</li>
						</c:if>
						<c:if test="${area.name != null}">
							<li>小区名称：${area.name}</li>
						</c:if>
						<c:if test="${time != null}">
							<li>到期日期：<fmt:formatDate value="${time}" pattern="yyyy-MM-dd" /></li>
						</c:if>
					</ul>
				</div>
			</c:if>
		</div>
		<c:if test="${phonenum != null}">
			<div class="tel" >如有疑问，敬请联系：<a href="tel:${phonenum}">${phonenum}</a></div>
		</c:if>
		
	</div>
	<%-- <div class="app">
		<div class="info">
			<div class="errInfo">${errorinfo }</div>
			<c:if test="${phonenum != null}">
				<div class="telDiv">联系电话：<a href="tel:${phonenum }">${phonenum }</a></div>
			</c:if>
			
			<c:if test="${code != null}">
				<div class="telDiv">设备号：${code}</div>
			</c:if>
		</div>
	</div> --%>
	
	
</body>
</html>
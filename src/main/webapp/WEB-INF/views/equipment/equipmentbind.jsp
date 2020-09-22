<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<title>设备绑定</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<div class="container">
		<c:choose>
			<c:when test="${info != null}">
				<div align="center" style="padding-top: 50px;">
						<h1>${info}</h1>
						<h1>设备已到期</h1>
				</div>
				<c:if test="${rank != 6 }">
					<div align="center" style="padding-top: 20px;">
							<a href="${hdpath}/wxpay/merShowDeviceAndDervice" class="btn btn-primary">请去缴费管理页面</a>
					</div>
				</c:if>
			</c:when>
		
			<c:when test="${code != null }">
				<div align="center" style="padding-top: 50px;">
					<h1>${code }</h1>
					<h1>设备绑定成功</h1>
				</div>
				<c:if test="${rank != 6 }">
					<div align="center" style="padding-top: 20px;">
						<a href="${hdpath }/merchant/manage" class="btn btn-primary">返回管理页面</a>
					</div>
				</c:if>
			</c:when>
		
			<c:when test="${imei != null }">
				<div align="center" style="padding-top: 50px;">
					<h1 style="color:#666; font-size:16px">设备号:${imei}</h1>
					<h1 style="color:#666; font-size:16px">IMEI过期,请联系销售</h1>
				</div>
				<c:if test="${rank != 6 }">
					<div align="center" style="padding-top: 20px;">
						<a href="${hdpath }/merchant/manage" class="btn btn-primary">返回管理页面</a>
					</div>
				</c:if>
			</c:when>
		</c:choose>
	</div>
</body>
</html>
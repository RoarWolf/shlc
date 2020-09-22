<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<title>常用电站</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />

<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<style type="text/css">
#wolful li:after {
	position: absolute;
    right: 0;
    bottom: 0;
    left: 15px;
    height: 0px;
    content: '';
    -webkit-transform: scaleY(.5);
    transform: scaleY(.5);
    background-color: #c8c7cc;
}

.pay {
	width: 100%;
}

.btn-pay {
	display: block;
	width: 90%;
	height: 40px;
	margin: 0 auto;
	background: #1AAD19;
	color: #fff;
	margin-top: 20px;;
}

.need {
	width: 100%;
	height: 60px;
	line-height: 40px;;
	font-size: 30px;
	text-align: center;
}

.payfor {
	width: 100%;
	height: 40px;
	line-height: 40px;;
	background: #fff;
	color: #999;
}

.payfor-left {
	float: left;
	margin-left: 5%;
}

.payfor-right {
	float: right;
	margin-right: 5%;
	color: #000;
}
</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
	    <a class="mui-action-back mui-icon mui-icon-back mui-pull-left"></a>
	    <h1 class="mui-title">选择记录或输入编号</h1>
	</header>
	<div class="mui-content">
		<form id="chargeForm" action="${hdpath }/charge/allChargePort">
			<div class="mui-input-row">
				<input type="text" name="deviceNum" id="code" style="text-align: center;" class="mui-input-clear" placeholder="点击输入设备编号">
			</div>
			<div class="mui-input-row" align="center">
				常用电站<font color="red">(单击选择)</font>
				<ul id="wolful" class="mui-table-view mui-table-view-chevron">
					<c:forEach items="${codelist }" var="code">
						<li class="mui-table-view-cell">
							<a id="code${code }" data-mycode="${code }" class="mui-navigate-right">
								${code }
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</form>
		<div>
			<button class="miu-btn btn-pay" id="nextStep">下一步</button>
		</div>
	</div>
</body>
</html>
<script>
	$(function() {
		$("#nextStep").click(function() {
			var codeval = $("#code").val();
			if (codeval == null || codeval == "") {
				mui.alert("请输入设备编号或点击以往充电设备编号");
			} else if (codeval.length != 6) {
				mui.alert("请输入正确设备编号");
			} else {
				$.ajax({
					url : '${hdpath}/charge/codeExist',
					data : {
						deviceNum : codeval,
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data == "1") {
							$("#chargeForm").submit();
						} else {
							mui.alert('输入设备编号不存在', '', function() {
							});
						}
					},
				});
			}
		})
		$("a[id^='code']").click(function() {
			var codeVal = $(this).attr("data-mycode");
			$("#code").val(codeVal);
			window.location.href = "${hdpath }/charge/allChargePort?deviceNum=" + codeVal;
		})
	})
</script>
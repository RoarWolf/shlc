<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="/mui/js/mui.min.js"></script>
<style type="text/css">
.title {
	margin: 20px 15px 10px;
	color: #6d6d72;
	font-size: 15px;
}
.btn-success{
	width: 120px;
}
.con_ul {
	overflow: hidden;
	margin: 0;
	padding: 0;
}
.con_ul > li{
	width:50%;
	float: left;
	list-style: none;
	text-align: center;
}
.con_ul > li>div{
	margin-top: 30px;
	display: inline-block;
}
</style>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
</head>
<body>
	<input id="appId" type="hidden" value="${appId }">
	<input id="nonceStr" type="hidden" value="${nonceStr }">
	<input id="timestamp" type="hidden" value="${timestamp }">
	<input id="signature" type="hidden" value="${signature }">
	<header class="mui-bar mui-bar-nav">
		<h1 class="mui-title"><font size="6px">设备管理</font></h1>
	</header>
	<%@ include file="/WEB-INF/views/public/buttomnav.jsp"%>
	<div class="mui-content">
		<ul class="con_ul">
			<li class="left">
				<div id="manage" class="mui-control-content mui-active">
					<a href="${hdpath }/merchant/orderinquire" class="btn btn-success">订单统计</a>
				</div>
				<div>
					<a href="${hdpath }/equipment/list?wolfparam=2" class="btn btn-success">设备管理</a>
				</div>
				<%-- <div style="padding-top: 30px;padding-left: 20px">
					<a href="${hdpath }/merchant/remotecharge" class="btn btn-success">远程操作</a>
				</div> --%>
				<div>
					<button id="equipmentbind" class="btn btn-success">设备绑定</button>
				</div>
				<div>
					<a href="${hdpath}/wctemplate/getTempmanage" class="btn btn-success">模板管理</a>
				</div>
				<div>
					<a href="${hdpath}/merchant/areaManage" class="btn btn-success">小区管理</a>
				</div>
				<div>
					<a href="${hdpath}/merchant/onlineCardList" class="btn btn-success">IC卡管理</a>
				</div>
				<div>
					<a href="${hdpath}/merchant/membersystem?source=1" class="btn btn-success">会员管理</a>
				</div>
				<div>
					<a href="${hdpath}/merchant/earningcollecttime" class="btn btn-success">收益统计</a>
				</div>
			</li>
			<li class="right">
				<div>
					<a href="${hdpath}/wxpay/merShowDeviceAndDervice" class="btn btn-success">缴费管理</a>
				</div>
			</li>
		</ul>
		
	</div>
</body>
<script type="text/javascript">
sessionStorage.removeItem('ulStr')
sessionStorage.removeItem('data')
sessionStorage.removeItem('urlFrom')
sessionStorage.removeItem("ulCon")
	/* $(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
	    	window.location.href = "${hdpath}/merchant/index";
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	}); */
	$(function() {
		$('#equipmentbind').click(function() {
			var timestampstr = $("#timestamp").val();
			var nonceStrstr = '${ nonceStr }';
			var signaturestr = '${ signature }';
			var appIdstr = '${appId}';
			wx.config({
				debug : false,
				appId : appIdstr,
				timestamp : timestampstr,
				nonceStr : nonceStrstr,
				signature : signaturestr,
				jsApiList : [ 'scanQRCode' ]
			});
			wx.ready(function(){
				wx.scanQRCode({
					needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
					desc: 'scanQRCode desc',
					scanType : [ 'qrCode' , 'barCode' ], // 可以指定扫二维码还是一维码，默认二者都有
					success : function(res) {
						var url = res.resultStr;
						if (url.indexOf("http://www.tengfuchong.com.cn/oauth2pay?code=",0) != -1) {
							var index = url.indexOf("=",0);
							var code = url.substring(index +1);
							window.location.href = "${hdpath}/equipment/allChargePortBind?code=" + code;
						} else if (url.indexOf("https://www.tengfuchong.cn/applet/") != -1) {
							var index = url.lastIndexOf("/");
							var code = url.substr(index +1,6);
							window.location.href = "${hdpath}/equipment/allChargePortBind?code=" + code;
						}
					}
				});
			})

			wx.error(function(res) {
				alert("错误：" + res.errMsg);
				// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
			});
		});
	})
</script>
</html>
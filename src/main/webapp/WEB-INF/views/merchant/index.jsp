<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>商家首页</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<input id="appId" type="hidden" value="${appId }">
	<input id="nonceStr" type="hidden" value="${nonceStr }">
	<input id="timestamp" type="hidden" value="${timestamp }">
	<input id="signature" type="hidden" value="${signature }">
	<div class="container">
		<div align="center">
			<font size="60px" style="font-weight: 900">总收入</font>
		</div>
		<div align="center">
			<span><font size="40px">65.50</font></span>
		</div>
		<div align="center">
			<a href="${hdpath }/merchant/eqipmentall" class="btn btn-info"
				style="height: 50px; width: 200px">查看所代理充电站</a>
			<!-- <button class="btn btn-info" style="height: 50px;width: 200px" onclick="alert('暂未开通此功能');">查看所代理充电站</button> -->
		</div>
		<div align="center" style="padding-top: 10px">
			<a href="${hdpath }/merchant/orderdetail" class="btn btn-info"
				style="height: 50px; width: 200px">订单详情</a>
			<!-- <button class="btn btn-info" style="height: 50px;width: 200px" onclick="alert('暂未开通此功能');">订单详情</button> -->
		</div>
		<div align="center" style="padding-top: 10px">
			<%-- <a href="${hdpath }/merchant/addeqipment" class="btn btn-info" style="height: 50px;width: 200px">添加设备</a> --%>
			<button class="btn btn-warning" style="height: 50px; width: 200px"
				onclick="alert('暂未开通此功能');">添加设备</button>
		</div>
		<div align="center" style="padding-top: 10px">
			<%-- <a href="${hdpath }/merchant/withdraw" class="btn btn-info" style="height: 50px;width: 200px">提现</a> --%>
			<button class="btn btn-success" style="height: 50px; width: 200px"
				onclick="alert('暂未开通此功能');">提现</button>
		</div>
		<div align="center" style="padding-top: 10px">
			<button class="btn btn-success" style="height: 50px; width: 200px"
				id="sao">扫一扫</button>
			<span id="saospan"></span>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		$('#sao').click(function() {
			var timestampstr = $("#timestamp").val();
			var nonceStrstr = '${ nonceStr }';
			var signaturestr = '${ signature }';
			wx.config({
				debug : false,
				appId : 'wx3debe4a9c562c52a',
				timestamp : timestampstr,
				nonceStr : nonceStrstr,
				signature : signaturestr,
				jsApiList : [ 'scanQRCode' ]
			});
			wx.ready(function(){
				wx.scanQRCode({
					needResult : 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
					desc: 'scanQRCode desc',
					scanType : [ 'qrCode' , 'barCode' ], // 可以指定扫二维码还是一维码，默认二者都有
					success : function(res) {
						var result = res.resultStr;
						$('#saospan').html(result);
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
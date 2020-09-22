<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<title>投币</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath}/js/union-ad.js"></script> <!-- 银联广告展示 -->
<%@ include file="/WEB-INF/views/public/compWechatFontToCharge.jspf"%>
<style type="text/css">
.demo-table {
	margin: 1rem 0 1.5rem 0;
	width: 100%;
}

#chargechoose {
	margin: 0;
	padding: 0 0 0 0.1rem;
}
ul{width: 100%;}
ul.ui-choose>li {
    box-sizing: border-box;
    border: 1px solid #ccc;
	float: none;
    width: 33%;
    height: 34px;
    line-height: 32px;
    margin: 0 auto;
    cursor: pointer;
    position: relative;
    z-index: 1;
    min-width: 75px;
    text-align: center;
}
#submitbtn {
	float: right;
	margin-right: 2rem;
}

hr {
	margin-top: 20px;
	margin-bottom: 0px;
	border: 0;
	border-top: 1px solid #eee;
}
</style>
<style type="text/css">
.title {
	width: 100%;
	text-align: center;
	height: 50px;
	line-height: 50px;
	font-size: 22px;
	color: #000;
}

.pay {
	width: 100%;
}

.btn-pay {
	display: block;
	width: 100%;
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
	<input type="hidden" id="code" value="${code}">
	<input type="hidden" id="openid" value="${openid}">
	<input type="hidden" id="nowtime" value="${nowtime}">
	<c:if test="${brandname != null }">
		<div align="center" style="font-size: 15px" >
			欢迎使用${brandname}电动车投币设备
		</div>
	</c:if>
	<div style="font-size: 15px;padding-top: 10px" align="center">
		当前设备编号为：${code}
	</div>
	<div class="demo-box">
<table class="demo-table">
	<caption></caption>
	<tr>
		<td>
			<ul id="chargechoose"><li style="font-size: 16px;">请选择投币金额</li></ul>
			<ul class="ui-choose" id="uc_02">
				<c:forEach items="${templateLists}" var="temp">
					<li style="width: 100%">${temp.name}<span style="display:none;">_${temp.id}</span></li>
				</c:forEach>
			</ul>
		</td>
	</tr>
</table>
	</div>
	<form id="payfrom" action="${hdpath }/wxpay/pay" method="post">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="portchoose" name="portchoose" value="1">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultTemp }">
		<label style="font-weight: 350">
				<input type="checkbox" checked="checked" id="attention" name="attention" value="1" style="zoom:90%" >
				<font size="2px">关注'自助充电平台公众号'了解实时充电信息</font>
		</label>
	</form>
	<c:if test="${walletpay == 2 }">
		<div class="mui-row pay">
			<button id="wolfwechatbtn" class="mui-btn btn-pay" onclick="wechatpay();" >微信支付</button>
		</div>
	</c:if>
	<div class="mui-row pay">
		<button id="wolfwalletbtn" class="mui-btn btn-pay" onclick="walletpay();">钱包支付<font color="#ecfd08">（充值有优惠）</font></button>
	</div>
	<div>注：如果不能充电或者网络异常 系统10分钟会自动退款 </div>
	<div style="font-size: 14px;padding-top: 10px;">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
</body>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id);
}
$(function() {
	uc_02._val_ul(1);
})
</script>
<script>
function walletpay() {
	var portchoose = $("#portchoose").val();
	var chargeparam = $("#chargeparam").val();
	var paymoneyinfo = '请选择支付金额';
	var paymoneystate = '正在充值...';
	var attentionval;
	if ($('#attention').is(':checked')) {
		attentionval = $('#attention').val();
	}
	if (portchoose == "" || portchoose == null) {
		mui.alert('未选择投币通道');
	} else if (chargeparam == "" || chargeparam == null) {
		mui.alert(paymoneyinfo);
	} else {
		$("#wolfwalletbtn").attr("disabled",true);
		$.ajax({
			type : "post",
			url : '${hdpath}/general/inCoinsWalletPay',
			dataType : "json",
			data : {
				openId : $("#openid").val(),
				code : $("#code").val(),
				port : portchoose,
				tempid : chargeparam
			},
			success : function(data) {
				if (data.code == 1) {
					/* mui.alert("投币成功"); */
					$("#wolfwalletbtn").removeAttr("disabled");
					/*支付成功、跳转到广告页面*/
					window.location.replace('/general/walletPayment?orderNum='+data.result.orderNum+'&payMoney='+data.result.payMoney)
					
				} else if (data.code == 2) {
					mui.alert("余额不足");
					$("#wolfwalletbtn").removeAttr("disabled");
				} else if (data.code == 3) {
					mui.alert("投币异常，请重新支付");
					$("#wolfwalletbtn").removeAttr("disabled");
				} else if (data.code == 4) {
					mui.alert("你的钱包不可支付当前设备，请使用微信支付");
					$("#wolfwalletbtn").removeAttr("disabled");
				}
			},
			error : function(e) {
				mui.alert("支付异常！");
			}
		});
	}
}

var prepay_id;
var paySign;
var appId;
var timeStamp;
var nonceStr;
var packageStr;
var signType;
var ordernum;
var attention;
function wechatpay() {
	var url = '${hdpath}/wxpay/inCoinsPay';
	var portchoose = $("#portchoose").val();
	var chargeparam = $("#chargeparam").val();
	var paymoneyinfo = '请选择支付金额';
	var paymoneystate = '正在充值...';
	var attentionval;
	if ($('#attention').is(':checked')) {
		attentionval = $('#attention').val();
	}
	if (portchoose == "" || portchoose == null) {
		mui.alert('未选择投币通道');
		return false;
	}
	if (chargeparam == "" || chargeparam == null) {
		mui.alert(paymoneyinfo);
		return false;
	}
	$("#wolfwechatbtn").attr("disabled",true);
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : {
			openId : $("#openid").val(),
			code : $("#code").val(),
			port : portchoose,
			tempid : chargeparam,
			attention : attentionval
		},
		success : function(data) {
			if (data.line == 0) {
				mui.alert('设备离线，可重新扫码连接或联系商家');
			} else if (data.wolferror == 0) {
				mui.alert('支付异常，请重新点击微信支付');
				$("#wolfwechatbtn").removeAttr("disabled");
			}  else {
				appId = data.appId;
				paySign = data.paySign;
				timeStamp = data.date;
				nonceStr = data.nonceStr;
				packageStr = data.packagess;
				signType = data.signType;
				ordernum = data.out_trade_no;
				attention = data.attention;
				callpay();
			}
		}
	});
}
	function onBridgeReady() {
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : appId, //公众号名称，由商户传入
			"paySign" : paySign, //微信签名
			"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数
			"nonceStr" : nonceStr, //随机串
			"package" : packageStr, //预支付交易会话标识
			"signType" : signType
		//微信签名方式
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request:ok") {
				if (attention == 1) {
					window.location.href = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU4MDY5OTg5MQ==&scene=126&subscene=0#wechat_redirect";
				} else {
					WeixinJSBridge.call('closeWindow');
				}
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '微信投币', function() {
					WeixinJSBridge.call('closeWindow');
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '微信投币', function() {
					WeixinJSBridge.call('closeWindow');
				});
			} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
		});
	}
	function callpay() {
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
						false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		} else {
			onBridgeReady();
		}
	}
</script>
</html>
<script>
/* $(function() {
	setTimeout(connectInCoins,10);
	function connectInCoins() {
		$.bootstrapLoading.start({ loadingTips: "正在连接..." });
		$.ajax({
			url : '/connectInCoins',
			type : "POST",
			dataType : "json",
			data : {
				code : $("#code").val(),
				nowtime : $("#nowtime").val(),
			},
			async : true,
			success : function(data) {
				if (data.err == "1") {
					mui.alert(data.errinfo, '', function() {
						WeixinJSBridge.call('closeWindow');
					});
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        },
	        error : function(XMLHttpRequest, textStatus, errorThrown) {
	        	console.log(XMLHttpRequest.readyState + "---" + XMLHttpRequest.status
	        			+ "---" + textStatus);
	        	console.log(XMLHttpRequest.responseText);
	        }
		});
	}
}) */
</script>
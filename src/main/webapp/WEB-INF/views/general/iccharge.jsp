<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IC卡充值</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<style type="text/css">
.demo-table {
	margin: 1rem 0 1.5rem 0;
	width: 100%;
}

.ui-choose .equtemp {
	width: 100%;
	margin: 0.1rem 0 0 0.1rem;
}
ul.ui-choose {
	width: 100%;
}

#chargechoose {
	margin: 0;
	padding: 0 0 0 0.1rem;
}

ul.ui-choose>li {
	box-sizing: border-box;
	border: 1px solid #ccc;
	float: left;
	width: 32.8%;
	height: 40px;
	line-height: 32px;
	margin: -0.1rem 0 0 0.1rem;
	cursor: pointer;
	position: relative;
	z-index: 1;
	min-width: 70px;
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
    <div class="mui-input-row" style="padding-top: 8%;">
        <label>卡号：</label>
        <input type="text" disabled="disabled" id="cardnum" value="${cardnum}">
    </div>
	<div class="demo-box">
		<table class="demo-table">
			<caption></caption>
			<tr>
				<td>
					<ul id="chargechoose"><li style="font-size: 18px;">请选择充值金额</li></ul>
					<ul class="ui-choose" id="uc_01">
						<c:forEach items="${templist }" var="temp">
							<li id="port${temp.id }" value="${temp.money }" data-val="${temp.money }" data-temp="${temp.id }">${temp.name }</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</table>
	</div>
	<input type="hidden" id="openid" value="${openid}">
	<input type="hidden" id="tempid">
	<div id="zidingyi" style="display: none;">
		<div class="input-group">
		    <div class="input-group-addon">RMB</div>
		    <input style="text-align: center;" type="text" class="form-control" id="money" placeholder="请输入金额" onchange="onchangesd()">
		    <div class="input-group-addon">元</div>
		</div>
	</div>
	<div class="need">
		￥<span><font id="choosemoney">0</font>元</span>
	</div>

	<div class="mui-row pay">
		<c:if test="${relevawalt==1}"><button id="wolfsubmit" class="mui-btn btn-pay" onclick="iconlinepay(1);" >确认充值</button></c:if>
		<c:if test="${relevawalt==2}"><button id="wolfsubmit" class="mui-btn btn-pay" onclick="iconlinepay(2);" >确认充值</button></c:if>
	</div>
</body>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose');
uc_01.click = function(index, item) {
	var tempid = item.attr("data-temp");
	var name= item.attr('data-val')
	$("#tempid").val(tempid);
	$("#choosemoney").text(name);
}

function onchangesd(){
	var money = $("#money").val();
	$("#choosemoney").text(money);
}
</script>
<script type="text/javascript">
	function iconlinepay(num){
		if(num==1){
			vawalletpay();
		}else{
			icpay();
		}
	}
	var prepay_id;
	var paySign;
	var appId;
	var timeStamp;
	var nonceStr;
	var packageStr;
	var signType;
	function icpay() {
		if ($("#cardnum").val() == "") {
			mui.alert('请输入卡号，当前未输入', 'IC卡充值', function() {
			});
			return ;
		}
		if ($("#choosemoney").text() == "0") {
			mui.alert('请选择充值金额，当前未选择', 'IC卡充值', function() {
			});
			return ;
		}
		$("#wolfsubmit").attr("disabled",true);
		var url = '${hdpath}/general/icRecharge';
		$.ajax({
			type : "post",
			url : url,
			dataType : "json",
			data : {
				openId : $("#openid").val(),
				cardnum : $("#cardnum").val(),
				choosemoney : $("#choosemoney").text(),
				tempid : $("#tempid").val(),
				source : 1
			},
			success : function(data) {
				if (data.cardifexist == "0") {
					mui.alert('输入卡号不存在，请重新输入', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "3") {
					mui.alert('登陆已过期，请重新登陆', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "2") {
					mui.alert('IC卡未激活，只可充值一次', 'IC卡充值', function() {
					});
					return ;
				} else if (data.cardifexist == "1") {
					appId = data.appId;
					paySign = data.paySign;
					timeStamp = data.date;
					nonceStr = data.nonceStr;
					packageStr = data.packagess;
					signType = data.signType;
					callpay();
				}
			}
		});
	}
	

	function vawalletpay() {
		var merid = "${user.merid}";
		if (merid == null || merid == "") {
			mui.alert('当前未绑定商家，钱包充值不开放', '钱包充值', function() {
			});
			return ;
		}
		if ($("#choosemoney").text().trim() == "0") {
			mui.alert('请选择充值金额，当前未选择', '钱包充值', function() {
			});
			return ;
		}
		$("#wolfsubmit").attr("disabled",true);
		var url = '${hdpath}/general/walletRecharge';
		var cardNum= $('#cardnum').val().trim()
		$.ajax({
			type : "post",
			url : url,
			dataType : "json",
			data : {
				openId : $("#openid").val(),
				choosemoney : $("#choosemoney").text(),
				tempid : $("#tempid").val(),
				val: cardNum
			},
			success : function(data) {
				if (data.err == "0") {
					mui.alert('当前未绑定商家，请重新扫描设备二维码后再充值', '钱包充值', function() {
					});
				} else {
					appId = data.appId;
					paySign = data.paySign;
					timeStamp = data.date;
					nonceStr = data.nonceStr;
					packageStr = data.packagess;
					signType = data.signType;
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
				window.location.replace("${hdpath}/general/payaccess");
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', 'IC卡充值', function() {
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', 'IC卡充值', function() {
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
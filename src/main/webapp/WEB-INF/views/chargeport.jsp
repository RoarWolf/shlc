<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>自助充电平台</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<style type="text/css">
html,body{width: 100%;height: 100%}
.demo-table{margin: 1rem 0 1.5rem 0;}
.ui-choose .equtemp{ width: 98%; margin: 0.1rem 0 0 0.1rem;}
#submitbtn{float: right;  margin-right: 2rem;}
hr {
    margin-top: 0px;
    margin-bottom: 0px;
    border: 0;
    border-top: 1px solid #eee;
}
.container button {
    padding: 6px 6px;
}
#submitbtn {
	margin-right: 0 ;
}
@media screen and (min-width: 390px){
	#submitbtn{
		padding: 6px 12px;
	}
	#walletbtn {	
		padding: 6px 12px;
	}
}
@media screen and (max-width: 360px){
	#submitbtn {
		    float: none;
		    width: 50%;
		    margin: 0 auto;
		    display: block;
		    padding: 6px 12px;
		}
	#walletbtn {
		float: none;
		margin: 0 auto 15px;
		padding: 6px 12px;
		display: block;
	}
}
.rechType {
	padding: 20px 0 15px;
	text-align: center;
}
.rechType button {
	width: 80%;
	border-radius: 5px;
	background-color: #5cb85c ;
	border-color: #5cb85c;
	color: #fff !important;
}
.rechType button:active {
	background-color: #398439 !important;
}
.alert {
		width: 100vw;
		height: 100vh;
		background-color: rgba(0,0,0,.4);
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		top: 0;
		z-index: 99;
		display: none;
}
.alert .alert_div {
	width: 80%;
	background-color: #f5f6fe;
	border-radius: 8px;
	position: absolute;
	left: 50%;
	top: 50%;
	padding-top: 10px;
	transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-webkit-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);
	overflow: hidden;
}
.alert .alert_div h3 {
	font-size: 14px;
	color: #333;
	font-size: 16px;
    text-align: center;
    color: #333;
    margin-bottom: 15px;
    margin-top: 10px;
}
.alert .alert_div p {
	font-size: 12px;
	color: #555;
	line-height: 26px;
	text-indent: 2em;
	padding: 0 15px;
}
.alert .alert_div p span {
	color:  #e65459;
}
.alert .alert_div >div {
	color: #22B14C;
	line-height: 40px;
	border-top: 1px solid #ddd;
	margin-top: 10px;
	text-align: center;

}
.alert .alert_div >div:active {
	background-color: #6BD089;
	color: #fff;
}
body {
	padding:15px 10px;
}
.container {
	padding:0;
 }
 .title-ul {
	 font-size: 14px;
	 margin-bottom: 5px;
	 padding: 0;
	 color: #666;
 }
 .demo-box .demo-table,.demo-box .demo-table tbody {
	width: 100%;
 }
 .bgGary {
 	border: 1px solid #808080 !important;
 	background: #808080 !important;
 	color: rgb(237, 239, 222) !important;
 }
 .wolfmonthDiv {
 	text-align: center;
 }
 #wolfmonth {
 	width: 93%;
 	background: #5cb85c;
 }
  /* .MonthlyList span {
  	color: rgb(236, 253, 8) !important;
  }  */
</style>
</head>
<body id="body"  data-hardversion="${hardversion}">
	<c:if test="${brandname != null }">
		<div align="center" style="font-size: 15px" >
			欢迎使用${brandname}电动车充电设备
		</div>
	</c:if>
	<div align="center" style="font-size: 15px">
		<font>当前设备编号为：${code }-${port }</font>
	</div>
	<div class="demo-box">
  <table class="demo-table">
    <caption></caption>
    <tr>
      <td>
        <ul class="title-ul">请选择充电时间</ul>
        <ul class="ui-choose" id="uc_02">
			<c:forEach items="${templatelist}" var="temp">
				<li class="equtemp">${temp.name}<span style="display:none;">_${temp.id}</span></li>
			</c:forEach>
			<c:if test="${packageMonth != null}">
				<li class="equtemp haveMonthlyList" > 包月&nbsp;&nbsp;<span <c:if test="${packageMonth.everymonthnum == 0 }">style="display: none;"</c:if> >(剩余总次数： ${packageMonth.surpnum}次) </span>&nbsp;&nbsp;<span <c:if test="${packageMonth.everydaynum == 0 }">style="display: none;"</c:if> >(今日剩余次数： ${packageMonth.todaysurpnum}次)</span></li>
			</c:if>
        </ul>
         <ul class="ui-choose">
	        <c:if test="${packageMonth == null}">
					<li class="equtemp bgGary monthlyBrnGary" > 包月</li>
			</c:if>
		 </ul>
      </td>
    </tr>
  </table>
  <input type="hidden" id="defaultindex" value="${defaultindex }">
</div>
<div class="alert">
	<div class="alert_div">
		<h3>充值方式</h3>
		<p>
			进入公众号<span>"自助公众平台"->点击充电中心->进入页面点击个人中心->找到钱包充值-></span>选择金额点击充值按钮即可
		</p>
		<div>确认</div>
	</div>
		
</div>
<script>
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();

var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id)
	if(item.hasClass('haveMonthlyList')){
		$('#walletbtn').fadeOut(0)
		$('.rechType').fadeOut(0)
		if($('#submitbtn').length > 0){
			$('#submitbtn').fadeOut(0)	
		}
		$('.wolfmonthDiv button').fadeIn(0)
	}else{
		$('#walletbtn').fadeIn(0)
		$('.rechType').fadeIn(0)
		if($('#submitbtn').length > 0){
			$('#submitbtn').fadeIn(0);	
		};
		$('.wolfmonthDiv button').fadeOut(0);
	};
}
$(function() {
	var defaultval = $("#defaultindex").val();
	uc_02._val_ul(defaultval);
})
</script>
<script src="${hdpath }/js/my.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<div class="container">
		<input type="hidden" id="nowtime" value="${nowtime}">
		<input type="hidden" id="ifmonth" value="${ifmonth }">
	<form id="payfrom" action="${hdpath }/wxpay/pay" method="post">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="openid" name="openid" value="${openid}">
		<input type="hidden" id="portchoose" name="portchoose" value="${port }">
		<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
		<input type="hidden" id="ifcontinue" name="ifcontinue" value="">
		<div>
			<label style="font-weight: 350">
				<input type="checkbox" name="attention" checked="checked" value="1" style="zoom:90%" >
				<font size="2px">关注'自助充电平台公众号'了解实时充电信息</font>
			</label>
		</div>
	</form>
		<button class="btn btn-success" id="walletbtn" onclick="walletpay();">钱包支付<font color="#ecfd08">（余额${balance }）（充值有优惠）</font></button>
		<c:if test="${ifwallet == 2 }">
			<button class="btn btn-success" id="submitbtn">微信支付</button>
		</c:if>
	<div class="rechType">
		<button type="button" class="mui-btn mui-btn-success mui-btn-outlined">充值方式</button>
	</div>
	<div class="wolfmonthDiv">
	<c:if test="${packageMonth != null}">
		<c:if test="${ifmonth != 2 }">
			<button class="mui-btn mui-btn-success" id="wolfmonth" style="display: none;">包月支付</button>
		</c:if>
	</c:if>
	</div>
	<hr>
	<p style="color: black;">
		友情提醒： <br>
		选择的充电时间为小功率电动车充电时间,仅供参考。大功率电动车充电时间智能动态计算，以实际为准。
	</p>
	<hr/>
	<div style="font-size: 14px;">
		<span>如有疑问，敬请联系：</span>
		<a href="tel:${phonenum }">${phonenum }</a>
	</div>
</div>
</body>
</html>
<script>
function handlePay(){
	var hardversion= $('body').attr('data-hardversion').trim()
	if(hardversion != '01'){
		return false
	}
	var forbidport= [].concat(${forbidport})
	var portNum=$('#uc_01 li.selected').index()+1
	return forbidport.some(function(item,i){
		return parseInt(item) == portNum ;
	})
};
function walletpay() {
	if(handlePay()){
		mui.alert('此端口已被指定', '', function() {});
		return
	}
	var code = $("#code").val();
	var openid = $("#openid").val();
	var portchoose = $("#portchoose").val();
	var chargeparam = $("#chargeparam").val();
	if (portchoose == null || portchoose == "") {
		mui.alert('未选择充电端口', '', function() {
		});
		return false;
	} else if (chargeparam == null || chargeparam == "") {
		mui.alert('未选择充电时间', '', function() {
		});
		return false;
	} else if (code == null || code == "") {
		mui.alert('退回操作，需重新扫码', '', function() {
		});
		WeixinJSBridge.call('closeWindow');
	} else {
		$("#walletbtn").attr("disabled",true);
		$.ajax({
			type : "post",
			url : '${hdpath }/general/chargePortWalletPay',
			dataType : "json",
			data : $('#payfrom').serialize(),
			success : function(data) {
				$("#ifcontinue").val(data.info);
				if (data == 1) {
					mui.alert("支付成功",function() {
						WeixinJSBridge.call('closeWindow');
					});
				} else if (data == 2) {
					mui.alert("余额不足");
				} else if (data == 3) {
					mui.alert("钱包暂时不可用");
				} else if (data == 4) {
					mui.alert("您的钱包不可支付当前设备，请使用微信支付");
				} else {
					mui.alert("充电异常");
				}
				$("#walletbtn").removeAttr("disabled");
			}
		});
	}
}
$(function() {
	
	$("#wolfmonth").click(function() {
		if(handlePay()){
			mui.alert('此端口已被指定', '', function() {});
			return
		}
		var ifmonth = $("#ifmonth").val();
		var code = $("#code").val();
		var openid = $("#openid").val();
		var portchoose = $("#portchoose").val();
		if (portchoose == null || portchoose == "") {
			mui.alert('未选择充电端口');
			return false;
		} else if (ifmonth == 0) {
			mui.alert('本商户暂时停止使用包月功能，如有疑问，请联系商家');
		} else if (ifmonth == 1) {
			$.bootstrapLoading.start({ loadingTips: "正在连接..." });
			$.ajax({
				url : "${hdpath}/general/monthSendData",
				data : {
					code : code,
					openid : openid,
					portchoose : portchoose
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.wolfval == 1) {
						mui.toast('支付成功');
						location.href = "/general/lookUserBagMonthInfo?uid=" + data.uid;
					} else if (data.wolfval == 2) {
						mui.alert("您未开通包月，请前往公众号<自助充电平台>个人中心开通此功能");
					} else if (data.wolfval == 0) {
						mui.alert("用户未注册，请前往公众号<自助充电平台>个人中心");
					} else if (data.wolfval == 4) {
						mui.alert("今日次数已用完");
					} else if (data.wolfval == 5) {
						mui.alert("总次数已用完");
					} else if (data.wolfval == 6) {
						mui.alert("开通包月没有此设备");
					} else if (data.wolfval == 3) {
						mui.alert("您的包月功能已过期，如需使用，请在个人中心续费");
					}
				},
		        complete: function () {
		            $.bootstrapLoading.end();
		        }
			})
		}
	})
	setTimeout(paystate1, 10); //0.01秒后执行
	function paystate1() {
		$.bootstrapLoading.start({ loadingTips: "正在连接..." });
		$.ajax({
			url : "${hdpath}/portstate2",
			data : {
				code : $("#code").val(),
				port : $("#portchoose").val(),
				nowtime : $("#nowtime").val()
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data.state == "error") {
					mui.alert('连接失败，请确认设备是否在线', '提示', function() {
						WeixinJSBridge.call('closeWindow');
					});
				} else {
					if (data.portstatus != "空闲") {
						mui.alert('此端口不可用', '提示', function() {
							WeixinJSBridge.call('closeWindow');
						});
					}
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
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
	$("#submitbtn").click(function() {
		if(handlePay()){
			mui.alert('此端口已被指定', '', function() {});
			return
		}
		var code = $("#code").val();
		var openid = $("#openid").val();
		var portchoose = $("#portchoose").val();
		var chargeparam = $("#chargeparam").val();
		if (portchoose == null || portchoose == "") {
			mui.alert('未选择充电端口', '', function() {
			});
			return false;
		} else if (chargeparam == null || chargeparam == "") {
			mui.alert('未选择充电时间', '', function() {
			});
			return false;
		} else if (code == null || code == "") {
			mui.alert('退回操作，需重新扫码', '', function() {
			});
			WeixinJSBridge.call('closeWindow');
		} else {
			$(this).attr("disabled",true);
			$.ajax({
				type : "post",
				url : '${hdpath }/wxpay/pay',
				dataType : "json",
				data : $('#payfrom').serialize(),
				success : function(data) {
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
			});
		}
	})
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
				mui.alert('支付取消', function() {
					WeixinJSBridge.call('closeWindow');
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', function() {
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
	
	$('.monthlyBrnGary').click(function(){
		$('.alert .alert_div h3').html('包月操作方式')
		$('.alert .alert_div p').html('进入公众号<span>"自助公众平台"-&gt;点击充电中心-&gt;进入页面点击个人中心-&gt;找到包月信息-&gt;</span>选择包月套餐点击确认充值按钮即可')
		$('.alert').fadeIn()
	})
	$('.rechType button').click(function(){
		$('.alert .alert_div h3').html('充值方式')
		$('.alert .alert_div p').html('进入公众号<span>"自助公众平台"-&gt;点击充电中心-&gt;进入页面点击个人中心-&gt;找到钱包充值-&gt;</span>选择金额点击充值按钮即可')
		$('.alert').fadeIn()
	})
		
	$('.alert').click(function(){
		$('.alert').fadeOut()
	})
	$('.alert .alert_div').click(function(e){ 
		var e= e || window.event
		e.stopPropagation()
	})
	$('.alert .alert_div > div').click(function(){
		$('.alert').fadeOut()
	})
	
	
		
})
</script>
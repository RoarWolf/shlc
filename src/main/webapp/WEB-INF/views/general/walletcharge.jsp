<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>钱包充值</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" href="${hdpath}/css/base.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<style type="text/css">
body {
	background-color: #f8f8f8;
}
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1899025_fjf6gjiwz9f.eot?t=1594619712384'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1899025_fjf6gjiwz9f.eot?t=1594619712384#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1899025_fjf6gjiwz9f.woff?t=1594619712384') format('woff'),
  url('//at.alicdn.com/t/font_1899025_fjf6gjiwz9f.ttf?t=1594619712384') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1899025_fjf6gjiwz9f.svg?t=1594619712384#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-yonghuming:before {
  content: "\e63c";
}

.icon-tel-fill:before {
  content: "\e639";
}

.icon-ID:before {
  content: "\e6cf";
}

.icon-xiaoquguanli:before {
  content: "\e600";
}

.icon-guanli:before {
  content: "\e640";
}

.icon-jine:before {
  content: "\e666";
}

.icon-shijian:before {
  content: "\e77c";
}

.icon-shichang:before {
  content: "\e62c";
}

.demo-table {
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
	padding: 0;
}
#chargechoose li {
	font-size: 16px;
    margin: -5px 0 10px 10px;
}

ul.ui-choose>li {
	width: 30%;
	border: 1px solid #ccc;
	box-sizing: border-box;
	border-radius: 5px;
	margin-bottom: 15px;
	text-align: center;
	height: 35px;
	line-height: 35px;
	margin-left: 2vw;
	font-size: 14px;
	background-color: #f5f7fa;
	transition: all .5s;
}
ul.ui-choose>li.selected {
	background-color: #22B14C;
	color: #fff;
	border: 1px solid #1EC63B;
}
ul.ui-choose>li:nth-child(3n-2){
	margin-left: 2.5%;
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
	background: #22B14C;
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

.card-info {
	padding: 15px 10px;
	color: #666;
	font-size: 12px;
}
.card-info ul li {
	border: 1px solid #ccc;
	overflow: hidden;
	padding:10px;
	border-radius: 5px;
	background-color: #f5f7fa;
}
.card-info ul li:active {
	background-color: #EfEfEf;
}
.card-info ul li div{
	min-width: 50%;
	float: left;	
}
.card-info ul li div span {
	/*display: block;*/
	line-height: 30px;
}
.card-info ul li div .title{
	color: #999;
	font-size: 12px;
}
.card-info ul li div span i {
	color: #22B14C;
	font-size: 15px;
	margin-right: 5px;
}
	
</style>
</head>
<body>
<div class="card-info">
			<ul>
				<li>
					<div class="left">
						<span class="title"><i class="iconfont icon-yonghuming"></i>用户名：</span>
						<span>${datamap.nick == null ? '— —' : datamap.nick}</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-tel-fill"></i>用户电话：</span>
						<span>
							<span class="spanNegative">${datamap.phone == null ? '— —' : datamap.phone}</span>
						</span>
					</div>
					<div class="left">
						<span class="title"><i class="iconfont icon-ID"></i>会员号：</span>
						<span>
							<span><fmt:formatNumber value="${datamap.id}" pattern="00000000"/></span>
						</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span>
						<span class="spanJust">${datamap.areaname == null ? '— —' : datamap.areaname}</span>
					</div>
					<div class="left">
						<span class="title"><i class="iconfont icon-yonghuming"></i>所属商户：</span>
						<span>${datamap.dealename == null ? '— —' : datamap.dealename}</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-tel-fill"></i>商户电话：</span>
						<span>
							<span class="spanNegative">${datamap.mobile == null ? '— —' : datamap.mobile}</span>
						</span>
					</div>
					<div class="right">
						<span class="title"><i class="iconfont icon-jine"></i>充值余额：</span>
						<span class="spanJust">${datamap.topupmoney}元</span>
					</div>
					<div class="left">
						<span class="title"><i class="iconfont icon-jine"></i>赠送余额：</span>
						<span>${datamap.sendmoney}元</span>
					</div>
				</li>
			</ul>
		</div>
		
	<div class="demo-box">
		<table class="demo-table">
			<caption></caption>
			<tr>
				<td>
					<ul id="chargechoose"><li>请选择充值金额</li></ul>
					<ul class="ui-choose" id="uc_01">
						<c:forEach items="${templist }" var="temp">
							<li id="port${temp.id }" value="${temp.money }" data-val="${temp.money }" data-temp="${temp.id }" data-remark="${temp.remark}">${temp.name }</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</table>
	</div>
	<input type="hidden" id="openid" value="${user.openid}">
	<input type="hidden" id="merid" value="${user.merid}">
	<input type="hidden" id="tempid">
	<div id="zidingyi" style="display: none;">
		<div class="input-group">
		    <div class="input-group-addon">RMB</div>
		    <input style="text-align: center;" type="text" class="form-control" id="money" placeholder="请输入金额" onchange="onchangesd()">
		    <div class="input-group-addon">元</div>
		</div>
	</div>
	<div class="need" data-totalMoney="0">
		￥<span><font id="choosemoney">0</font>元</span>
	</div>

	<div class="mui-row pay">
		<button id="wolfsubmit" class="mui-btn btn-pay" onclick="walletpay();" >确认充值</button>
	</div>
</body>
<script type="text/javascript">
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();
// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose');
uc_01.click = function(index, item) {
	var name = item.attr('data-val').trim();
	var tempid = item.attr("data-temp");
	$("#tempid").val(tempid);
	$("#choosemoney").text(name);
	var remark= item.attr("data-remark");
	$('.need').attr('data-totalMoney',remark)
}

function onchangesd(){
	var money = $("#money").val();
	$("#choosemoney").text(money);
}
</script>
<script type="text/javascript">
var prepay_id;
var paySign;
var appId;
var timeStamp;
var nonceStr;
var packageStr;
var signType;
function walletpay() {
	var merid = $("#merid").val();
	if (merid == null || merid == "") {
		mui.alert('当前未绑定商家，钱包充值不开放', '钱包充值', function() {
		});
		return ;
	}
	if ($("#choosemoney").text() == "0") {
		mui.alert('请选择充值金额，当前未选择', '钱包充值', function() {
		});
		return ;
	}
	$("#wolfsubmit").attr("disabled",true);
	var url = '${hdpath}/general/walletRecharge';
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		data : {
			openId : $("#openid").val(),
			choosemoney : $("#choosemoney").text(),
			tempid : $("#tempid").val(),
			val : 2
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
				if(GetRequest().from == 1){
					var remark= parseFloat($('.need').attr('data-totalmoney'))//实际到账金额
					var choosemoney= parseFloat($('#choosemoney').text()) //实际支付金额
					var sendmoney= remark- choosemoney
					/*balance 充值金额  ， sendmoney赠送金额 */
					sessionStorage.setItem('remark',JSON.stringify({balance: choosemoney,sendmoney: sendmoney}))
					setTimeout(function(){
						window.history.go(-1);	
					},50)
				}else{
					window.location.replace("${hdpath}/general/payaccess");	
				}
			} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
				mui.alert('支付取消', '钱包充值', function() {
				});
			} else if (res.err_msg == "get_brand_wcpay_request:fail") {
				mui.alert('支付失败', '钱包充值', function() {
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
	
	/* 解析url */
	function GetRequest() {
	    var url = location.search; //获取url中"?"符后的字串
	    var theRequest = new Object();
	    if (url.indexOf("?") != -1) {
	        var str = url.substr(1);
	        strs = str.split("&");
	        for(var i = 0; i < strs.length; i ++) {
	            theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	        }
	    }
	    return theRequest;
	}
</script>
</html>
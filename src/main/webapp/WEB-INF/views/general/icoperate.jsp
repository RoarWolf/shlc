
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IC卡</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.js"></script> 
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
/*toast信息提示*/
.mui-toast-container {bottom: 50% !important;}
.mui-toast-message { opacity: 0.6; color: #fff; width: 180px; padding: 20px 5px 10px 5px;}
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1250366_phcgc3m1vuh.eot?t=1560923504982'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1250366_phcgc3m1vuh.eot?t=1560923504982#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAMMAAsAAAAABxAAAAK+AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcAqCGIIKATYCJAMICwYABCAFhG0HMRsxBhHVk7NkXxzYbT47AB/kBdf5IKyKTVlgOnFw74PneT3ec/NuoF8DE6ykoqfPYWm7e6LnNEBAZf5t0zocNSZCdcmeb/0vzwjQGS7kVw4dFAbA/xwubQIFMj9QjnMu6gUYbw1srEmRFcih3zB2gUs4DQECqmpMes+DR+OhWJMEkHnrzMl4OSuKsoCH4NSsVOQANp6103oK7A9/L19pmQcWtsFKGT6jzzS6f2jsEJNkyTgfgQ0EtN4GDNAYKMjoWu9QDMJwhsCr1MS+EgU+NFnmy5G9ShD2r7PiIwAqQ+o90Vz6reZYACj33j0dmDdqR7CjsuXKll1bfNeN5GZYYvft9NboII2CEzdqB+Wl4fWianLs1ILdt0XS22EYzrkVBE/yK5PcXBjuvlkBRO++XU7OFdYdFp5cnRVcuyUDeEFeU1AnnLRfuxLnopiK4ijy728wiYi36K3uW2IqiK/8yr0c/Xr5MyoRpaXzyiQhW9wFF7jFxUjc51vK3DZpt4joIrm/rjQNjItVPZQ2zy2zJfoVz0S8XCP318XxdAcgu9lv9Jf/DSverU8s1vm/FyoAL7ofKRyTXTEKeDemE/jBtJ5VqjMsmaUqrTZoMR37xgsCAiCDjSW9HSss8zZBwaPMO+ldqqRg8KiNVGxjsAlpDQ4e3SCgkX7rQ0pZgkHUBxraCkAo7j5Y5FAEhuKeIRX7BmzK+g4OxTMQMFZK7RhSN2xVGLWgA/OH1dLlHYWoKPGG4X1ozjJC/As5rw4Y2r4YWPBCnmNO/oRRxINnOmEG5+FxECSmDa20USRNXefrPtRaOgtVYNSCDswfVkuX9+uLKn3/huF9aG6pqFG+kPPaOwxt3wO5cK9eFffyTv6EUcSDZzphBsPwOAhS/bANrbRxhDtNnV3L9+W26/PPr9tAUajCElWlnVIXZU3n6kqKAgAA') format('woff2'),
  url('//at.alicdn.com/t/font_1250366_phcgc3m1vuh.woff?t=1560923504982') format('woff'),
  url('//at.alicdn.com/t/font_1250366_phcgc3m1vuh.ttf?t=1560923504982') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1250366_phcgc3m1vuh.svg?t=1560923504982#iconfont') format('svg'); /* iOS 4.1- */
}
.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.icon-saomiao:before {
  content: "\e60b";
}
* {
	padding: 0;
	margin: 0;
}
li {
	list-style: none;
}
a {
	text-decoration: none;
	-webkit-tap-highlight-color: transparent;
}
body{
	font-size: 12px;
	background-color: #fff;
}
.QRCodeAlert {
	position: fixed;
	width: 270px;
	position: absolute;
	left: 50%;
	top: 50%;
	animation: showAni .3s;
	transform-origin:center center;
	transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-webkit-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);
	z-index: 99;
	display: none;
}
.QRCodeAlert .top {
	position: relative;
    padding: 15px;
    border-radius: 13px 13px 0 0;
    background: rgba(255,255,255,.95);
}
.QRCodeAlert .top::after {
    position: absolute;
    top: auto;
    right: auto;
    bottom: 0;
    left: 0;
    z-index: 15;
    display: block;
    width: 100%;
    height: 1px;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleY(.5);
    transform: scaleY(.5);
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
}
.QRCodeAlert .top h3 {
	font-size: 18px;
    font-weight: 500;
    text-align: center;
}
.QRCodeAlert .top p{
    margin: 5px 0 0;
    font-family: inherit;
    font-size: 14px;
    text-align: center;
    color: #666;
}
.QRCodeAlert .top input {
    width: 100%;
    height: 26px;
    padding: 0 35px 0 5px;
    margin: 0;
    font-size: 14px;
    background: #fff;
    border: 1px solid rgba(0,0,0,.3);
    border-radius: 0;
}
.QRCodeAlert .top .selectInp input {
    width: 18px;
    height: 18px;
    margin: 0 0 0 5px;
}
.QRCodeAlert  .inputDiv>div,
.QRCodeAlert  .inputDiv>div>label {
  	display: flex;
  	justify-content:center;
  	font-size: 14px;
  	color: #333;
  	font-weight: 400;
  	}
.QRCodeAlert .bottom {
	position: relative;
    display: -webkit-box;
    display: -webkit-flex;
    display: flex;
    height: 44px;
    -webkit-box-pack: center;
    -webkit-justify-content: center;
    justify-content: center;
}
.QRCodeAlert .bottom span {
	position: relative;
    display: block;
    width: 100%;
    height: 44px;
    box-sizing: border-box;
    padding: 0 5px;
    overflow: hidden;
    font-size: 17px;
    line-height: 44px;
    color: #007aff;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    background: rgba(255,255,255,.95);
    -webkit-box-flex: 1;
}
.QRCodeAlert .bottom span:first-child {
  		border-radius: 0 0 0 13px;

  	}
  	.QRCodeAlert .bottom span:first-child::after {
	position: absolute;
    top: 0;
    right: 0;
    bottom: auto;
    left: auto;
    z-index: 15;
    display: block;
    width: 1px;
    height: 100%;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleX(.5);
    transform: scaleX(.5);
    -webkit-transform-origin: 100% 50%;
    transform-origin: 100% 50%;
  	}
  	.QRCodeAlert .bottom span:last-child{
  	    border-radius: 0 0 13px;
  	    font-weight: 600;
  	}
  	.QRCodeAlert  .inputDiv {
	position: relative;
	margin: 15px 0 0;
  	}
  	#searchQRCode {
  		position: absolute;
	width: 26px;
	height: 26px;
	right: 5px;
	top: 0;
	display: flex;
	align-items: center;
	justify-content: center;
  	}
  	#searchQRCode i {
  		font-size: 25px;
  		color: #007aff;
  	}
  	.copymark {
  		position: fixed;
 			left: 0;
 			right: 0;
 			bottom: 0;
 			top: 0;
 			background-color: rgba(0,0,0,.4);
 			z-index: 98;
 			display: none;
  	}

.iccop td {
	text-align: center;
	vertical-align: middle;
}
.iccop table {
	width: 100%;
}
.iccop thead td {
	line-height: 40px !important;
	border-bottom: 1px solid #22B14C;
	color: #22B14C;
	padding: 0 !important;
}

.iccop tbody td {
	line-height: 40px !important;
	padding: 0 !important;
	/*border-bottom: 1px solid #ccc;*/
}
.iccop tbody td:first-child {
	font-weight: 600;
}
.iccop tbody td:nth-child(2) a{
	color: #337ab7;
}
.mui-bar-tab {
	box-shadow: none;
	border-top: 1px solid #ccc;		
}
#addCardbtn {
	width: 95%;
	position: absolute;
	left: 50%;
	top: 50%;
	transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-webkit-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);

}
</style>
</head>
<body>
<div class="iccop">
		<table class="table table-hover"> 
		<thead>
			<tr class="tabletr">
				<td class="cardnum">卡号</td>
				<td class="cardoper">充值/赠送</td>
				<td class="cardoper">状态</td>
				<td class="cardoper">备注</td>
				<td class="cardoper">操作</td>
			</tr>
		<tbody>
		<c:forEach items="${cardList}" var="card">
		  <tr class="tabletr">
		  	<td><font>${card.figure}</font></td>
		  	<td><font><a href="${hdpath }/general/onlinecardrecord?cardID=${card.cardID}&uid=${user.id}"><fmt:formatNumber value="${card.money}" pattern="0.00"/> / <fmt:formatNumber value="${card.sendmoney}" pattern="0.00"/></a></font></td>
		  	<td>
			  <c:choose>
				<c:when test="${card.status == 0}"><font color="gray">未激活</font></c:when>
				<c:when test="${card.status == 1}"><font color="green">正常</font></c:when>
				<c:when test="${card.status == 2}"><font color="red">挂失</font></c:when>
				<c:when test="${card.status == 3}"><font color="red">异常</font></c:when>
			  </c:choose>
			</td>
			<td>
				<span onclick="editRemark(${card.id})">
				<font id="remark${card.id}">${card.remark}</font><span  class="mui-icon mui-icon-compose" ></span></span>
			</td>
			<td>
				<a href="/general/operation?id=${card.id}" class="mui-icon mui-icon-more" ></a>
		  	</td>
		  </tr>
		</c:forEach>
		</tbody>

	  </table>
	</div>

	<div class="copymark"></div>
	<div class="QRCodeAlert">
		<div class="popur">
			<div class="top">
				<h3>IC卡绑定</h3>
				<!-- <p>是否修改该IC卡：4206BF49</p> -->
				<div class="inputDiv">
					<input type="text" name="inpName" placeholder="卡号不区分大小写">
					<span id="searchQRCode"><i class="iconfont icon-saomiao"></i></span>
				</div>
				<div class="inputDiv selectInp">
					<div>
						是否跟钱包关联： 
						<label>是  <input type="radio" name="isContentWetall" value="1" /></label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>否  <input type="radio" name="isContentWetall" value="2"  checked /></label>
					</div>
				</div>
			</div>
			<div class="bottom">
				<span class="closeBtn">取消</span>
				<span class="sureBtn">确认</span>
			</div>
		</div>
	</div>

	<nav class="mui-bar mui-bar-tab">
		<a id="addCardbtn" onclick="icbinding(0)" class="mui-btn mui-btn-success"><span class="mui-icon mui-icon-plusempty"></span>添加卡</a>
		<input type="hidden" name="amount" id="amount" value="13">
		<input type="hidden" name="userId" id="userId" value="1">
		<input type="hidden" id="timestamp" value="${timestamp }">
	</nav>

</body>
<script type="text/javascript">
$(function(){
    pushHistory();
    window.addEventListener("popstate", function(e) {
		location.replace('/general/index');
	}, false);
    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, "title", "#");
    }
});
/* window.addEventListener('refresh', function(e){//执行刷新
    location.reload();
}); */
function editRemark(node2) {
	var node1 = $("#remark" + node2).text().trim();
	if (node1 == '') {
		node1 = '备注长度需小于8位';
	}
	mui.prompt( " ", node1, "IC卡备注", ['取消', '确定'],  function(e){
		if(e.index == 1){
			$.ajax({
		        type: "POST",//方法类型
		        dataType: "json",//预期服务器返回的数据类型
		        url: "/general/editRemark" ,//url
		        data: {id:node2,remark:e.value},
		        success: function (e){
		            if (e == 1) {
		            	mui.toast("修改成功");
	            		window.location.reload();
		            }else if (e == 0) {
	            		window.location.reload();
		            };
		        },
		        error : function(){
		        	mui.toast("异常错误！");
            		window.location.reload();
		        }
			});
		}else{
			mui.toast("您取消了该操作！");
		}
	})
}

var userId = $("#userId").val();
var amount = $("#amount").val();
function icbinding(num){//卡绑定操作  (0是绑定  1是解绑)
	if(amount>=200){
		mui.toast('提示！每个人最多绑定200个IC卡');
	}else{
		 //点击显示
			$('.copymark').fadeIn(200)
			$('.QRCodeAlert').css('display','block')
	}
} 

$('.copymark').click(function(){  //点击遮挡层关闭
	$('.QRCodeAlert input').val('')
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
})

$('.QRCodeAlert .closeBtn').click(function(){ //点击取消
	$('.QRCodeAlert input').val('')
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
	mui.toast("您取消了该操作！");
})

$('.QRCodeAlert .sureBtn').click(function(){  //点击确定
	var cardID= $('.QRCodeAlert input').val().trim()
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
	// 获取的值进行处理,处理之后清空输入框
	reg=/^[a-fA-F0-9]+$/;
	regs=/^[0-9]+$/;
	if (cardID == null || cardID == "") {
		mui.toast('卡号为空，请重新输入');
		return
	} 
	if (cardID.length == 8) {
		if(!reg.test(cardID)){
			mui.toast('卡号输入格式不正确');
			return
		}
	}else if(cardID.length == 10){
		if(!regs.test(cardID)){
			mui.toast('卡号输入格式不正确');
			return
		}
	}else{
		mui.toast('卡号位数不正确');
		return
	}
	var relevawalt= $('.QRCodeAlert input[name="isContentWetall"]:checked').val().trim()
	$.ajax({
        type: "POST",
        dataType: "json",
        url: "/general/bindingonline" ,
        data: {uid:userId,cardNum:cardID,relevawalt:relevawalt},
        success: function (e){
            if (e.code == 200) {
            	mui.toast('绑定成功');
           		window.location.reload();
            }else if (e.code == 100) {
            	mui.alert("用户信息获取不到", "", function() { });
            }else{
            	mui.toast(e.message);
            }
        },
        error : function(){
        	mui.toast('异常错误！');
        }
	});
})
$('#searchQRCode').click(function(){
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
				if (url.indexOf("http://www.tengfuchong.com.cn/oauth2online?cardNumber=",0) != -1) {
					var index = url.indexOf("=",0);
					var cardnums = url.substring(index +1);
					$('.popur .inputDiv input[name="inpName"]').val(cardnums)
				}
			}
		});
	})
	wx.error(function(res) {
		alert("错误：" + res.errMsg);
		// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	});
})
</script>
</html>
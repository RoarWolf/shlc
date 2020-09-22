<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IC卡操作</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.js"></script> 
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style>
html,body,.body{width:100%; height: 100%;}
.topdiv{ padding: 25px 0px 20px 30px;}
.topdiv .mui-icon-chatboxes{ float: right; margin-right: 30px;}
table{width: 98%; margin: 15px auto; text-align: center;}
.tabletr{line-height: 40px;}
.cardnum{width:30%;}
.cardoper{width:16%;}
#icsubmit{background: #6bfb71;}
/*toast信息提示*/
.mui-toast-container {bottom: 50% !important;}
.mui-toast-message { opacity: 0.6; color: #fff; width: 180px; padding: 20px 5px 10px 5px;}

/*带扫描的弹框css*/
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
    		display: flex;
			align-items: center;
			justify-content: center;
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
    	@keyframes showAni {
			from {
				opacity: 0;
				transform: translate(-50%,-50%) scale(1.1);
				-ms-transform: translate(-50%,-50%) scale(1.1);
				-moz-transform: translate(-50%,-50%) scale(1.1);
				-webkit-transform: translate(-50%,-50%) scale(1.1);
				-o-transform: translate(-50%,-50%) scale(1.1);
			}
			to {
				transform: translate(-50%,-50%) scale(1);
				-ms-transform: translate(-50%,-50%) scale(1);
				-moz-transform: translate(-50%,-50%) scale(1);
				-webkit-transform: translate(-50%,-50%) scale(1);
				-o-transform: translate(-50%,-50%) scale(1);
				opacity: 1
			}
    	}

</style>
</head>
<body>
<div class="body">
<div id="pullrefresh" class="mui-scroll-wrapper">
    <div class="mui-scroll">
    <input type="hidden" id="openid" value="${user.openid}">
    <input type="hidden" id="userId" value="${user.id}">
	<input id="timestamp" type="hidden" value="${timestamp }"> 
	<input id="isSubUser" type="hidden" value="${isSubUser }"> 
	<div>
		<ul class="mui-table-view">
			<c:if test="${card.status==0 || card.status==1}">
			    <li class="mui-table-view-cell">
			        <a  data-href="${hdpath}/general/iccharge?openid=${user.openid}&cardID=${card.cardID}" class="mui-navigate-right iccard-rechard">IC卡充值 </a>
			    </li>
		    </c:if>
		    <c:if test="${card.status==1}">
			    <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="icsubmit('${card.id}','${card.status}',2)" class="mui-navigate-right">IC卡挂失</a></li>
		    </c:if>
		    <c:if test="${card.status==2}">
		    	<li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="icsubmit('${card.id}','${card.status}',1)" class="mui-navigate-right">IC卡解挂</a></li>
		    </c:if>
			<c:if test="${card.status!=1}">
				<li class="mui-table-view-cell">
			        <a href="javascript:void(0);" id="changeCard" data-id="${card.id}" data-status="${card.status}"  data-cardID="${card.cardID}" onclick="icamend('${card.id}','${card.status}','${card.cardID}')" class="mui-navigate-right">卡号修改</a>
			    </li>
			    <c:if test="${card.money==0 || card.money==null}">
			    <li class="mui-table-view-cell">
			        <a href="javascript:void(0);" onclick="icremove('${card.id}','${card.status}','${card.cardID}')" class="mui-navigate-right">IC卡删除</a>
			    </li>
			    </c:if>
			</c:if>
		</ul>
	</div>
	<div >
	</div>  
  </div>
 </div>
</div>

<!-- 下面是带扫描的弹框 -->
<div class="copymark"></div>
<div class="QRCodeAlert">
	<div class="popur">
		<div class="top">
			<h3>卡号修改</h3>
			<p>是否修改该IC卡：4206BF49</p>
			<div class="inputDiv">
				<input type="text" name="inpName" placeholder="请输入新卡号">
				<span id="searchQRCode"><i class="iconfont icon-saomiao"></i></span>
			</div>
			<!-- <div class="inputDiv selectInp">
				<div>
					是否跟钱包关联： 
					<label>是  <input type="radio" name="isContentWetall" value="1" /></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label>否  <input type="radio" name="isContentWetall" value="2"  checked /></label>
				</div>
			</div> -->
		</div>
		<div class="bottom">
			<span class="closeBtn">取消</span>
			<span class="sureBtn">确认</span>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
var openid = $("#openid").val();
var userId = $("#userId").val();
var title;
var message;
var success;
var hint;
var btnValue = ['取消', '确定'];
var url;
function icsubmit(id,status,num){
	if((status==0&&num==2) || (status==1&&num==2)){
		title="挂失";
		message = "是否挂失该IC卡";
		success = "挂失成功。";
		url="/general/icunregister";
		data = {id:id,num:num,userId:userId}
	}else if(status==2&&num==1){
		title="解挂";
		message = "是否解挂该IC卡";
		success = "解挂成功。";
		url="/general/icunregister";
		data = {id:id,num:num,userId:userId}
	}else{
		mui.alert("该状态下此功能不能使用！", "", function() {
    		mui.back();
    	});
	}
	confirm(message,title,url,data);
} 
function confirm(message,title,url,data){
	mui.confirm(message, title, btnValue, function(e){
		if(e.index == 1){
			$.ajax({
		        type: "POST",
		        dataType: "json",
		        url: url,
		        data: data,
		        success: function (e){
		            if(e == 1){
		            	mui.alert(success, "", function(userId) {
		            		location.href = "/general/icoperate?uid=" + $("#userId").val()+"&openid="+openid;
		            	});
		            }else{
		            	mui.alert("操作失败", "", function() {
		            		window.location.reload();
		            	});
		            };
		        },
		        error : function(){
		        	mui.toast("异常！");
		        }
			});
		}else{
			mui.toast('您取消了该操作');
		}
	})
}

function icremove(id,status,cardID){//删除IC卡
	if(status==0 || status==2){
		title = "删除";
		message = "是否确定删除该IC卡";
		success = "删除成功，欢迎下次使用。";
		url="/general/movecardrelevance";
		data = {id:id,userId:userId}
	}else{
		mui.alert("该状态下此功能不能使用！", "", function() {
    		mui.back();
    	});
	}
	confirm(message,title,url,data);
}
/* ==================================================================================  */
function icamend(id,status,cardID){//修改卡号
	if(status==0 || status==2){
		$('.QRCodeAlert .top p').text("是否修改该IC卡: "+cardID+" 卡号");
	}else{
		mui.alert("该状态下此功能不能使用！", "", function() {
    		mui.back();
    	});
	}
	$('.copymark').fadeIn(200)
	$('.QRCodeAlert').css('display','block')
}

$('.copymark').click(function(){  //点击遮挡层关闭
	$('.QRCodeAlert input').val('')
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
})

$('.QRCodeAlert .closeBtn').click(function(){ //点击取消
	$('.QRCodeAlert input').val('')
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200,function(){
		mui.toast('您取消了操作');
	})
	
})

$('.QRCodeAlert .sureBtn').click(function(){  //点击确定
	var cardName= $('.QRCodeAlert input').val().trim()//新卡号
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
	// 获取的值进行处理,处理之后清空输入框
	reg=/^[a-fA-F0-9]+$/;
	regs=/^[0-9]+$/;
	if (cardName == null || cardName == ""){
		mui.toast('卡号为空，请重新输入');
		return
	} 
	if (cardName.length == 8) {
		if(!reg.test(cardName)){
			mui.toast('卡号输入格式不正确');
			return
		}
	}else if(cardName.length == 10){
		if(!regs.test(cardName)){
			mui.toast('卡号输入格式不正确');
			return
		}
	}else{
		mui.toast('卡号位数不正确');
		return
	}
	
	var id= $('#changeCard').attr('data-id')
	var oricard= $('#changeCard').attr('data-cardID')
	var relevawalt= 1;
	/* var relevawalt= $('.QRCodeAlert input[name="isContentWetall"]:checked').val().trim() */
	$.ajax({
        type: "POST",
        dataType: "json",
        url: "/general/scanamendonline",
        data: {id:id,oricard:oricard,userId:userId,nowcard:cardName,relevawalt:relevawalt},
        success: function (e){
            if(e.code == 200){
         	mui.alert("修改成功", "", function() {
         		location.href = "/general/icoperate?uid=" + $("#userId").val()
         	});
            }else if(e.code == 100){
         		mui.alert("用户信息获取不到", "", function() { });
            }else if(e.code == 103){
         		mui.alert("被修改的IC卡号不存在", "", function() { });
            }else if(e.code == 104){
         		mui.alert("正常卡不能修改", "", function() {});
            }else if(e.code == 105){
         		mui.alert("IC卡号格式不正确", "", function() { });
            }else if(e.code == 106){
         		mui.alert("该IC卡已被绑定", "", function() { });
            }else if(e.code == 403){
         		mui.alert("异常错误，请联系管理员处理", "", function() { });
            }else{
         		mui.alert("操作失败", "", function() { });
         };
        },
        error : function(){
        	mui.toast("异常！");
        }
	});
})
$('#searchQRCode').click(function(){
	var oricard = '${card.cardID}';
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

$('.iccard-rechard').on('click',function(){
	var isSubUser= $('#isSubUser').val().trim() //判断是不是特约商户下的用户
	if(isSubUser == 1){
		return mui.alert('暂不支持在此充值，请使用微信扫描IC卡背面的二维码进行充值！','提示','我知道了',function(){
			
		})
	}
	window.location.href= $(this).attr('data-href').trim()
})


</script>
</html>
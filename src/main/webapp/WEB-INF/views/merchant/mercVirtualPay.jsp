<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>虚拟充值</title>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.js"></script>
<style type="text/css">
body, h1, h2, h3, h4, h5, h6, hr, p, blockquote, dl, dt, dd, ul, ol, li, pre, form, fieldset, legend, button, input, textarea, th, td { margin:0; padding:0; }
/*toast信息提示*/  
.mui-toast-container {bottom: 50% !important;}  
.mui-toast-message {opacity: 0.6; color: #fff; width: 180px; padding: 20px 5px 10px 5px;} 

@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1549443_yz4tnmf04cj.eot?t=1575860128707'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1549443_yz4tnmf04cj.eot?t=1575860128707#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1549443_yz4tnmf04cj.woff?t=1575860128707') format('woff'),
  url('//at.alicdn.com/t/font_1549443_yz4tnmf04cj.ttf?t=1575860128707') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1549443_yz4tnmf04cj.svg?t=1575860128707#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-beizhu:before {
  content: "\e665";
}

.icon-dizhi:before {
  content: "\e61a";
}

.icon-dianhua:before {
  content: "\e60c";
}

.icon-user-s:before {
  content: "\e634";
}

.icon-jine:before {
  content: "\e666";
}

.icon-shijian:before {
  content: "\e77c";
}

.icon-qiapian:before {
  content: "\e6da";
}

.icon-bianhao:before {
  content: "\e600";
}



body{
	padding: 15px 10px;
	font-size: 14px;
	color: #666;
}
h3 {
	text-align: center;
	font-size: 16px;
	font-weight: 400;
	margin-bottom: 15px;
}
.showInfo {
			margin: 0 auto;
			overflow: hidden;
		}
		.showInfo>div {
			width: 49.5%;
			overflow: hidden;
			float: left;
			/* margin-bottom: 15px; */
			height: 37px;
		}
		/*
		.showInfo>div:last-child{
			min-width: 49.5%;
			width: auto;
		}
		*/
		.showInfo>div:nth-child(2n){
			float: right;
		}
		.showInfo .lastDiv{
			width: auto;
		}
		.showInfo .lastDiv>span:last-child{
			padding: 0 12px;
		}
		.showInfo>div>span {
			display: block;
			box-sizing: border-box;
			border: 1px solid #ccc;
			border-radius: 5px;
			text-align: center;
			height: 35px;
			line-height: 35px;
			background-color: rgba(0,0,0,0.01);
		}
		.showInfo>div>span:first-child{
			float: left;
			min-width: 90px;
		}
		.showInfo>div>span:last-child{
			overflow: hidden;
			text-overflow:ellipsis;
			white-space: nowrap;
			
		}

input::-webkit-input-placeholder {
	color: #999;
	font-size: 14px;
}
input::-ms-input-placeholder {
	color: #999;
	font-size: 14px;
}
input::-moz-input-placeholder {
	color: #999;
	font-size: 14px;
}
h5 {
	margin-top: 20px;
	margin-bottom: 10px;
	font-size: 16px;
}

#submit {
	padding: 10px;
	margin-top: 60px;
	width: 100%;
}
	.app ul li {
		font-size:12px;
		border: 1px solid #ccc;
		overflow: hidden;
		padding:10px;
		border-radius: 5px;
		background-color: #f5f7fa;
		margin-bottom: 15px;
	}
	.app ul li:active {
		background-color: #EfEfEf;
	}
	.app ul li div{
		min-width: 50%;
		float: left;	
	}
	/* .app ul li div:nth-child(1){
		width: 100%;
	} */
	
	.app ul li div span {
		/*display: block;*/
		line-height: 30px;
		text-align: center;
	}
	.app ul li div .title{
		color: #666;
	}
	.app ul li div span i {
		color: #22B14C;
		font-size: 15px;
		margin-right: 5px;
	}
	.app .select_btn {
		display: flex;
		justify-content: space-around;
	}
	.app .select_btn button {
		width: 28%;
		text-align: center;
	}
	.editBtn {
		float: right;
		margin-top: 2px;
	}
	
			/*=====  */
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
	color: #000;
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
    color: #000;
}
.QRCodeAlert .top p{
    margin: 12px 0 0;
    font-family: inherit;
    font-size: 14px;
    text-align: center;
    color: #000;
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
.QRCodeAlert  .inputDiv>div,
.QRCodeAlert  .inputDiv>div>label {
 	display: flex;
 	align-items: center;
 	justify-content:center;
 	font-size: 14px;
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
.deleteDiv {
	margin-top: 15px;
	display:flex;
	justify-content: space-around;
	align-items: center;
}

.verInfo {
	margin-bottom: 20px;
}
.verInfo>div {
	float: left;
	width: 48%;
}
.verInfo>div.right {
	float: right;
}
.verInfo>div label {
	width: 65px;
	float: left;
	padding-left:0px;
	padding-right:5px;
	width: auto;
}
.verInfo>div input {
	width: calc(100% - 65px);
	overflow: hidden;
	display: block;
	background-color: #fff;
	border: 1px solid #ccc;
	padding: 10px 0 10px 10px;
	background-color: rgba(255,255,255,.3);
	height: 36px;
	font-size: 14px;
} 
.clearfix:before,
.clearfix:after {
    content: " ";
    display: table;
}

.clearfix:after {
    clear: both;
}

/**
 * For IE 6/7 only
 */
.clearfix {
    *zoom: 1;
}		
</style>
</head>
<body>
<div class="app">
	<input type="hidden" class="receivedata" data-type="${type}" data-objid="${order.id}" data-uid="${user.id}" >
	<c:if test="${type==1}">
		<h3>充值帐号：<fmt:formatNumber value="${order.id}" pattern="00000000"/></h3>
		<h5>会员信息</h5>
		<ul>
			<li>
				<div class="left">
					<span class="title"><i class="iconfont icon-qiapian" style="font-size: 14px;"></i>会员号：</span>
					<span><fmt:formatNumber value="${order.id}" pattern="00000000"/></span>
				</div>
				<div class="right">
					<span class="title">
					<i class="iconfont icon-user-s"></i>昵称 ：</span>
					<span class="spanNegative">
						<c:if test="${order.username == null || order.username == ''}">— —</c:if>
						<c:if test="${order.username != null && order.username != ''}">${order.username}</c:if>
					</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-jine"></i>充值金额：</span>
					<span><a href="${hdpath }/general/touristmoneychange?uid=${order.id}" id="money_a">
					<fmt:formatNumber value="${order.balance}" pattern="0.00"/></a>
					元</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-jine"></i>赠送金额：</span>
					<span><a href="${hdpath }/general/touristmoneychange?uid=${order.id}" id="money_sendmoney">
					<fmt:formatNumber value="${order.sendmoney}" pattern="0.00"/></a>
					元</span>
				</div>
				
				<div class="right">
					<span class="title"><i class="iconfont icon-dianhua"></i>电话 ：</span>
					<span class="spanNegative">
						<c:if test="${order.phoneNum == null || order.phoneNum == ''}">— —</c:if>
						<c:if test="${order.phoneNum != null && order.phoneNum != ''}">${order.phoneNum}</c:if>
					</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-dizhi"></i>小区：</span>
					<span>
						<c:if test="${areadata.name == null}" >— —</c:if>
						<c:if test="${areadata.name != null}" >${areadata.name}</c:if>
					</span>
				</div>
				
			</li>
		</ul>
	<c:if test="${packageMonth != null }" >
		<h5>包月信息</h5>
		<ul>
			<li>
				<div class="left">
					<span class="title"><i class="iconfont icon-bianhao"></i>总剩余次数：</span>
					<span><a href="/general/queryMonthRecord?uid=${id}">${packageMonth.everymonthnum == 0 ? '不限' : packageMonth.surpnum}</a></span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-bianhao"></i>今日剩余次数：</span>
					<span>${packageMonth.everydaynum == 0 ? '不限' : packageMonth.todaysurpnum}</span>
				</div>
				<div class="right">
					<span class="title">
					<i class="iconfont icon-shijian"></i>到期时间：</span>
					<span class="spanNegative"><fmt:formatDate value="${packageMonth.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</div>
			</li>
		</ul>
	</c:if>
		
	</c:if>
	<c:if test="${type==2}">	
		<h3>充值在线卡号：${order.cardID}</h3>
		<h5>在线卡信息</h5>
		<ul>
			<li>
				<div class="left">
					<span class="title"><i class="iconfont icon-qiapian" style="font-size: 14px;"></i>卡号：</span>
					<span>${order.cardID}</span>
				</div>
				<div class="right">
					<span class="title">
					<i class="iconfont icon-user-s"></i>昵称 ：</span>
					<span class="spanNegative">${useronline.username}</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-jine"></i>充值金额：</span>
					<span>
					<a href="${hdpath }/general/onlinecardrecord?cardID=${order.cardID}&uid=${order.uid}" id="money_a">
					<fmt:formatNumber value="${order.money}" pattern="0.00"/>
					</a>
					元</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-jine"></i>赠送金额：</span>
					<span>
						<a href="${hdpath }/general/onlinecardrecord?cardID=${order.cardID}&uid=${order.uid}" id="money_sendmoney">
						<fmt:formatNumber value="${order.sendmoney}" pattern="0.00"/>
						</a>
					元</span>
				</div>
				
				<div class="right">
					<span class="title"><i class="iconfont icon-dianhua"></i>电话 ：</span>
					<span class="spanNegative">${useronline.phoneNum}</span>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-beizhu"></i>关联钱包 ：</span>
					<span class="spanNegative">${order.relevawalt == 1 ? '是' : '否'}</span>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-dizhi"></i>小区：</span>
					<span>
						<c:if test="${areadata.name == null}" >— —</c:if>
						<c:if test="${areadata.name != null}" >${areadata.name}</c:if>
					</span>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-beizhu"></i>备注：</span>
					<span>
						<font class="remarkSpan">
							<c:if test="${order.remark == null}" >— —</c:if>
							<c:if test="${order.remark != null}" >${order.remark}</c:if>
						</font>
						<strong class="editBtn mui-icon mui-icon-compose" data-cardID="${order.cardID}" data-id="${order.id}"></strong>
					</span>
				</div>
				
			</li>
		</ul>
		
		<div class="deleteDiv">
			<button type="button" data-id="${order.id}" class="mui-btn mui-btn-success icCard_de" >删除IC卡</button>
			<button type="button" data-id="${order.id}" class="mui-btn mui-btn-success deleteCard" <c:if test="${order.uid == null || order.uid == 0}">disabled</c:if>>解绑用户</button>
		</div>
	</c:if>
	<h5><c:if test="${type==1}">充值钱包</c:if><c:if test="${type==2}">充值在线卡</c:if></h5>

	<div class="verInfo clearfix">
		<div class="left">
			<div class="mui-input-row">
		        <label>充值金额</label>
			    <input type="text" id="money" placeholder="充值金额">
		    </div>
		</div>
		<div class="right">
			<div class="mui-input-row">
		        <label>赠送金额</label>
			    <input type="text" id="giveMoney" placeholder="赠送金额">
		    </div>
		</div>
	</div>
	<!-- <input type="text" id="money" value="" placeholder="请输入充值金额">	 -->
	<div class="select_btn">
		<button type="button" class="mui-btn mui-btn-success ver_btn"  data-money="50">50元</button>
		<button type="button" class="mui-btn mui-btn-success ver_btn" data-money="100">100元</button>
		<button type="button" class="mui-btn mui-btn-success clearBalance" >清零</button>
	</div>
</div>
	
	<button type="button" id="submit" class="mui-btn mui-btn-success">确认</button>
	
	<div class="copymark"></div>
		<div class="QRCodeAlert">
		<div class="popur">
			<div class="top">
				<h3>提示</h3>
				<p>设置IC卡备注</p>
				<div class="inputDiv">
					<input type="text" name="inpName" class="inpName" placeholder="请输入备注信息">
				</div>
			</div>
			<div class="bottom">
				<span class="closeBtn">取消</span>
				<span class="sureBtn">确认</span>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$('.ver_btn').click(function(){
	var money= $(this).attr('data-money').trim()
	$("#money").val(money)
	$('#giveMoney').val(0)
})
$("#submit").click(function (){
	if($("#money").val().trim()== '' && $('#giveMoney').val().trim() == ''){
		mui.confirm('请先输入充卡金额或赠送金额')
		return
	}
	var type= $('.receivedata').attr('data-type');  //1会员  2在线卡
	var objid= $('.receivedata').attr('data-objid');
	var uid= $('.receivedata').attr('data-uid');
	var money = parseFloat($("#money").val());
	money= isNaN(money) ? 0 : money
	var sendmoney= parseFloat($('#giveMoney').val()) //赠送金额
	sendmoney= isNaN(sendmoney) ? 0 : sendmoney
	$.ajax({
	    type : "POST",
		url : "${hdpath}/merchant/mercVirtualPay",
		data : {
			money : money,
			sendmoney: sendmoney,
			type : type,
			id : objid,
		},
	    success:function(e){
	    	if (e.code==200) {
	    		window.location.href= '/merchant/mercVirtualResult?ordernum='+e.ordernum+'&uid='+e.uid+'&cardID='+e.cardID+'&type='+e.type
	    	} else {
	    		mui.toast(e.messg)
	            return false
	    	}
	    },
	    error:function(){
	    	 mui.toast('数据异常')
	    }
	})
});
/* 点击编辑备注信息 */
$('.QRCodeAlert .closeBtn').click(function(){ //点击取消
	$('.copymark').fadeOut(200)
	$('.QRCodeAlert').fadeOut(200)
	$('.QRCodeAlert .inpName').val('')
})
$('.editBtn').on('tap',function(){
	$('.copymark').fadeIn(200)
	$('.QRCodeAlert').fadeIn(200)
})
$('.QRCodeAlert .sureBtn').on('tap',function(){
	var id= $('.editBtn').attr('data-id').trim()
	var userId= $('.receivedata').attr('data-uid').trim()
	var remark= $('.QRCodeAlert .inpName').val().trim()
	$.ajax({
		url: '/general/editRemark',
		data: {
			id: id,
			userId:userId,
			remark:remark
		},
		type: 'post',
		success: function(res){
			if(res== 1){
				$('.remarkSpan').text(remark)
				mui.toast('备注设置成功')
			}else{
				mui.toast('备注设置失败')
			}
		},
		error: function(err){
			
		},
		complete: function(){
			$('.QRCodeAlert .inpName').val('')
			$('.copymark').fadeOut(200)
			$('.QRCodeAlert').fadeOut(200)
		}
	})
})

$('.deleteCard').on('tap',function(){
	
	var money= $('#money_a').text().trim()
	if(parseFloat(money) > 0){
		mui.confirm('当钱IC卡余额为'+money+'元，解绑用户会自动清除卡余额，确定解除IC卡与用户的绑定吗？',function(e){
			if(e.index == 1){
				unbingICCard()
			}
		})
	}else{
		mui.confirm('解绑用户会自动清除卡余额，是否解除当前IC卡与用户的绑定？',function(options){
			if(options.index == 1){
				unbingICCard()
			}
		})
	}
	
})

function unbingICCard(){ //解绑ic卡
	var id= $('.deleteCard').attr('data-id').trim()
	$.ajax({
		url: '/merchant/unbindingOnlineCard',
		type: 'post',
		data: {
			onlineid: id,
			conti: 1
		},
		success: function(res){
			if(res.code == 200){
				window.location.reload()
			}else {
				mui.toast('解绑失败！')
			}
		},
		error: function(err){
			mui.toast('解绑失败，请稍后重试！')
		}
	})
}

$('.icCard_de').on('tap',function(){
	var money= $('#money_a').text().trim()
	if(parseFloat(money) > 0){
		mui.confirm('当钱IC卡余额为'+money+'元，删除IC会自动解绑用户和清除卡余额，确定删除此IC卡吗？',function(e){
			if(e.index == 1){
				deleteICFn()
			}
		})
	}else{
		mui.confirm('删除IC会自动解绑用户和清除卡余额，是否删除当前IC卡？',function(options){
			if(options.index == 1){
				deleteICFn()
			}
		})
	}
})

function deleteICFn(){
var id= $('.deleteCard').attr('data-id').trim()
	$.ajax({
		url: '/merchant/deleteOnlineCard',
		type: 'post',
		data: {
			onlineid: id
		},
		success: function(res){
			if(res.code == 200){
				mui.confirm('IC卡删除成功！',function(){
					window.location.replace('/merchant/onlineCardList')
				})
			}else {
				mui.toast('删除失败！')
			}
		},
		error: function(err){
			mui.toast('删除失败，请稍后重试！')
		}
	})
}

$('.clearBalance').click(function(){
	var clearMoney= 0-parseFloat($('#money_a').text());
	var clearSendMoney= 0- parseFloat($('#money_sendmoney').text()); 
	$("#money").val(clearMoney)
	$("#giveMoney").val(clearSendMoney)
});
</script>
</html>
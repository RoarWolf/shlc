<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>订单详情</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath }/css/base.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<style>
	@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1345561_1dqeg6gg0af.eot?t=1565571287534'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1345561_1dqeg6gg0af.eot?t=1565571287534#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1345561_1dqeg6gg0af.woff?t=1565571287534') format('woff'),
  url('//at.alicdn.com/t/font_1345561_1dqeg6gg0af.ttf?t=1565571287534') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1345561_1dqeg6gg0af.svg?t=1565571287534#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-qian2:before {
  content: "\e644";
}

.icon-big-Pay:before {
  content: "\e6ec";
}

.icon-shaixuan:before {
  content: "\e686";
}

.icon-weixin:before {
  content: "\e7b4";
}

.icon-seach:before {
  content: "\e624";
}
	body {
		background: #efeff4 !important;
	}

		.app{
			padding: 15px;
		}
		.app .top li {
			border: 0.0427rem solid #add9c0;
			overflow: hidden;
			padding:0.4268rem;
			border-radius: 0.2134rem;
			background-color: #f5f7fa;
			margin-bottom: 0.64rem;
			font-size: 0.597rem;
			color: #666;
		}
		.app li:active {
			background-color: #f5f5f373;
		}
		.app .top .firstLi {
			/*display: flex;*/

		}
		.app .top .firstLi>div{
			width: 50%;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
		.app .top .firstLi>div p {

		}

		.app .top li div{
			width: 100%;
			display: flex;
			justify-content: space-between;	
		}

		.app .top li div span {
			line-height: 1.28rem;
			text-align: center;
		}
		.app .top li div .title{
			color: #999;
		}
		.app .top li div span i {
			color: #22B14C;
			font-size: 0.64rem;
			margin-right: 0.2134rem;
		}
		.app .top .infoTop{
	      padding: 0.8536rem 0;
	      background-color: #fff;
	      border-top:0.0427rem solid #ccc;
	      border-bottom:0.0427rem solid #ccc;
	      margin-bottom: 0.8536rem;
	    }
	    .app .top .left p,
	    .app .top .right p{
	      text-align: center;
	       color: #666;
	       font-size: 0.597rem;
	       margin: 0;
	    }
	    .app .top div.left p:last-child,
	    .app .top div.right p:last-child{
	      font-size: 0.8rem;
	      color: #666;
	      line-height: 1.5em;
	    }
	   .app .top  .left {
	    width: 50% !important;
	    float: left;
	   }
	   .app .nav {
	   	position: fixed;
	   	bottom: 0;
	   	left: 0;
	   	width: 100%;
	   	height: 2.3467rem;
	   	background-color: #fff;
	   }
	   .app .nav ul {
	   	display: flex;
	   	justify-content: flex-end;
	   	height: 100%;
	   	line-height: 2.3467rem;
	   	padding: 0 0.64rem;
	   }
	   .app .nav ul a {
	   	display: inline-block;
	   }
	   .app .nav ul li:first-child {
		font-size: 0.6826rem;
		font-weight: 700;
		color: #000;
	   }
	   .app .nav ul li:first-child span:last-child {
	   	margin-left: 0.21333rem;
	   	color: #F47378;
	   }
	   .app .nav ul li:last-child button{	
		font-size: 0.5973rem;
		width: 3.6267rem;
		height: 1.45rem;
		line-height: 1.45rem;
		text-align: center;
		color: #fff;
		padding: 0;
		border-radius: 6px;
		vertical-align: middle;
	   }
	   .app .nav ul li:last-child button.lineBtn {
	   	background-color: #22B14C;
	   	margin-right: 0.21333rem;
	   	border: 1px solid #21A046;
	   }
	   .app .nav ul li:last-child button.refBtn {
	   	background-color: #0099ff;
	   	border: 1px solid #0066CC;
	   }
	   .refBtnBack {
		   	background-color: #22B14C !important;
		   	border: 1px solid #98E0AD !important;
		   	color: #fff !important;
	   }
	   
	   /* 当退费金额不足的时候弹出退费说明 */
	   .addInfo_con {
				position: fixed;
				left: 0;
				top: 0;
				right: 0;
				bottom: 0;
				background-color: rgba(0,0,0,.4);
				display: none;
				z-index: 99;
			}
			.addInfo {
				width: 80%;
				padding: 15px 15px 20px;
				border-radius: 8px;
				background-color: #fff;
				position: absolute;
				left: 50%;
				top: 33%;
				transform: translate(-50%,-50%); 
				-webkit-transform: translate(-50%,-50%); 
				-moz-transform: translate(-50%,-50%); 
				-ms-transform: translate(-50%,-50%); 
				-o-transform: translate(-50%,-50%); 
			}
			.addInfo p{
				font-size: 12px;
			}
			.addInfo .bottom {
				padding-top: 10px;
			}
			.addInfo h3 {
			    font-size: 20px;
			    text-align: center;
			    font-weight: bold;
			    padding-bottom: 10px;
			    border-bottom:1px solid #eee;
			    color: #333
			}
			.addInfo .tipP {
				text-indent: 2em;
				color: #666;
				font-size:14px;
			}
			
			.addInfo .reg_title {
				color: #333;
			    font-weight: bold;
			    font-size: 14px;
			    padding: 6px 0;
			}
			.addInfo .content {
				text-indent: 2em;
				color: #666;
			}
			.addInfo .content i {
				text-indent: 0;
				color: #666;
			}
			.addInfo .green_span {
				color: #22B14C;
			}
			#popover {
				color: #777;
				padding: 10px;
				line-height:1.6em;
			}
</style>
</head>
<body data-source="${paysource}" data-orderid="${orderid}">
<div class="app">
		<div class="top">
			<li class="firstLi">
	              <div class="left">
	                <p>付款金额</p>
	              	<p class="timePEle">${partrecord.money}元</p>
	              </div>
	              <div class="right">
	                <p>订单状态</p>
	                <p>
	                	<c:if test="${partrecord.status == 1}">正常 </c:if>
	               	 	<c:if test="${partrecord.status != 1}">
	               	 		<c:choose>
							    <c:when test="${paysource == 1 && order.number == 2}">
							        	部分退款 <a href="#popover" id="openPopover" class="mui-icon mui-icon-info"></a>
							    </c:when>
							    <c:otherwise>
							        	退款
							    </c:otherwise>
							</c:choose>
	               	 	</c:if>
	                </p>
	              </div>
			</li>
				<li>
					<div>
						<span class="title"><i class="iconfont icon-icon" style="font-size: 0.5548rem;"></i>订单编号</span>
						<span>${order.ordernum}</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-fukuanfangshisel"></i>付款方式</span>
						<span>${partrecord.paytype == 1 ? "钱包支付" : 
								partrecord.paytype == 2 ? "微信支付" : 
								partrecord.paytype == 3 ? "支付宝支付" : 
								partrecord.paytype == 4 ? "微信小程序" : 
								partrecord.paytype == 5 ? "支付宝小程序" : 
								partrecord.paytype == 6 ? "虚拟充值" : 
								partrecord.paytype == 12 ? "银联支付" : 
								"--"}</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-yonghuming"></i>用户昵称</span>
						<span>${tourist.username}</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-ID"></i>用户ID</span>
						<span><fmt:formatNumber value="${partrecord.uid}" pattern="00000000"/></span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-xiaoquguanli"></i>小区名称</span>
						<span>${equip.areaname==null ? "无" : equip.areaname}</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-shebeimingcheng"></i>设备名称</span>
						<span>${equip.remark==null ? "无" : equip.remark}</span>
					</div>
	
					<div>
						<span class="title"><i class="iconfont icon-bianhao"></i>设备编号</span>
						<span>${equip.code}</span>
					</div>
					<c:if test="${paysource == 1 || paysource == 2 }">
					<div>
						<span class="title"><i class="iconfont icon-bianhao"></i>端口号</span>
						<span><fmt:formatNumber value="${order.port}" pattern="00"/></span>
					</div>	
					</c:if>
				</li>

				
		</div>
		<div class="nav">
			<ul>
				<!-- <li><span>实收</span><span>¥ 3元</span></li> -->
				<li>
					<c:if test="${paysource == 1}">
					<a href="/merchant/powerbrokenline?orderId=${order.id}"><button type="button" class="mui-btn lineBtn">功率曲线</button></a>
					</c:if>
					<c:choose>
						<c:when test="${paysource == 1}"> 
							<c:if test="${order.number == 0}">
								<c:if test="${order.paytype == 1}"><button type="button" class="mui-btn refBtn" id="refund" value="3">退款</button></c:if>
								<c:if test="${order.paytype == 2}"><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></c:if>
								<c:if test="${order.paytype == 3}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
								<c:if test="${order.paytype == 8}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
								<c:if test="${order.paytype == 12}"><button type="button" class="mui-btn refBtn" id="refund" value="7">退款</button></c:if>
							</c:if>
							<c:if test="${order.number == 1}">
								<button type="button" class="mui-btn refBtn" disabled>退款</button>
							</c:if>
							<c:if test="${order.number == 2 && order.paytype != 7}">
								<button type="button" class="mui-btn refBtnBack" data-number="2">撤回</button>
							</c:if>
						</c:when>
						<c:when test="${paysource == 2}">
						 	<c:if test="${order.handletype == 4 || order.handletype == 5 || order.handletype == 7 || order.handletype == 9 || order.handletype == 11 || order.handletype == 13}">
								<button type="button" class="mui-btn refBtn" disabled>退款</button>
							</c:if>
							<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10 || order.handletype == 12}">
								<c:if test="${order.handletype == 1}"><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></c:if>
								<c:if test="${order.handletype == 2}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
								<c:if test="${order.handletype == 6}"><button type="button" class="mui-btn refBtn" id="refund" value="3">退款</button></c:if>
								<c:if test="${order.handletype == 8}"><button type="button" class="mui-btn refBtn" id="refund" value="4">退款</button></c:if>
								<c:if test="${order.handletype == 10}"><button type="button" class="mui-btn refBtn" id="refund" value="5">退款</button></c:if>
								<c:if test="${order.handletype == 12}"><button type="button" class="mui-btn refBtn" id="refund" value="7">退款</button></c:if>
							</c:if>
						</c:when>
						<c:when test="${paysource == 3}">
						  <c:if test="${order.handletype == 1}">
						    <c:if test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 6 || order.paytype == 9}">
								<button type="button" class="mui-btn refBtn" disabled>退款</button>
							</c:if>
							<c:if test="${order.paytype == 1 || order.paytype == 2 || order.paytype == 5 || order.paytype == 8}">
								<c:if test="${order.paytype == 1}"><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></c:if>
								<c:if test="${order.paytype == 2}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
								<c:if test="${order.paytype == 5}"><button type="button" class="mui-btn refBtn" id="refund" value="3">退款</button></c:if>
								<c:if test="${order.paytype == 8}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
							</c:if>
						  </c:if>
						</c:when>
						<c:when test="${paysource == 4}">
						 	<c:if test="${order.paytype == 2 || order.paytype == 3 || order.paytype == 4 || order.paytype == 5 || order.paytype == 6 || order.paytype == 8}">
								<button type="button" class="mui-btn refBtn" disabled>退款</button>
							</c:if>
							<c:if test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7 }">
								<c:if test="${order.paytype == 0}"><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></c:if>
								<c:if test="${order.paytype == 1}"><button type="button" class="mui-btn refBtn" id="refund" value="6">退款</button></c:if>
								<c:if test="${order.paytype == 7}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
							</c:if>
						</c:when>
						<c:when test="${paysource == 5}">
						 	<c:if test="${order.flag==2}">
						 		<button type="button" class="mui-btn refBtn" disabled>退款</button>
						 	</c:if>
							<c:if test="${order.flag==1}">
								<c:if test="${order.type == 3}"><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></c:if>
								<c:if test="${order.type == 6}"><button type="button" class="mui-btn refBtn" id="refund" value="2">退款</button></c:if>
								<c:if test="${order.type == 8}"><button type="button" class="mui-btn refBtn" id="refund" value="6">退款</button></c:if>
								<c:if test="${order.type == 10}"><button type="button" class="mui-btn refBtn" id="refund" value="2" data-refund="5">退款</button></c:if>
							</c:if>
						</c:when>
						<c:when test="${paysource == 6}">
							<c:if test="${order.status != 2 && order.ifrefund == 0}">
								<td><button type="button" class="mui-btn refBtn" id="refund" value="1">退款</button></td>
							</c:if>
						</c:when>
					</c:choose>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="addInfo_con">
		<div class="addInfo">
			<h3>退款失败</h3>
			<div class="bottom">
				<p class="tipP"></p>
				<div class="reg_title">退费流程：</div>
				<!-- <p class="content"><span class="green_span">打开微信</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span">关注“自助公众平台”公众号<i class="mui-icon mui-icon-arrowthinright"></i>点击右下角的“商家登录”进入注册界面</span><i class="mui-icon mui-icon-arrowthinright"></i><span class="green_span">完成注册即可（注： 设备号可以输“888888”）</span></p> -->
				<p class="content1"></p>
				<p class="content2"></p>
			</div>
		</div>
	</div>
	<div id="popover" class="mui-popover">
	  	部分退款金额退款到用户平台钱包
	</div>
	<script>
	$(function(){
		
		var wechatUrl = "${hdpath}/wxpay/doRefund";		//微信
		var alipayUrl = "${hdpath}/alipay/alipayRefund";		//支付宝
		var walletUrl = "${hdpath}/wxpay/doWalletReturn";	//钱包
				
		var virtualUrl = "${hdpath}/wxpay/mercVirtualReturn";	//虚拟充值
		var appwechatUrl = "${hdpath}/wxpay/wxDoRefund";	//微信小程序	wolfkey = 3;
		var appalipayUrl = "${hdpath}/alipay/aliDoRefund";	//支付宝小程序	wolfkey = 4;
		var unionUrl = "${hdpath}/unionpay/doRefund";	//银联
		var wolfkey = 0;
		var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
	    var fontSize= htmlwidth/16
	    var style= document.createElement('style')
	    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
	    var head= document.getElementsByTagName('head')[0]
	    head.insertBefore(style,head.children[0])
	    
	    $('.refBtn').click(function(){
	    	mui.confirm('确认退款吗？',function(params){
	    		if(params.index == 1){
	    			var orderid = $("body").attr('data-orderid').trim();
	    			if(orderid == '' || orderid == '0'){
	    				mui.alert('退款失败',function(){})
	    				return
	    			}
					var refundnum = $("#refund").val();
					var type;
					var refundUrl;
					if(refundnum==1){
						refundUrl = wechatUrl;
					}else if(refundnum==2){
						refundUrl = alipayUrl;
					}else if(refundnum==3){
						refundUrl = walletUrl;
					}else if(refundnum==4){
						refundUrl = appwechatUrl;
						wolfkey = 3;
					}else if(refundnum==5){
						refundUrl = appalipayUrl;
						wolfkey = 4;
					}else if(refundnum==6){
						refundUrl = virtualUrl;
						type = 1;
					}else if(refundnum==7){
						refundUrl = unionUrl;
					}
					paysource = $('body').attr('data-source').trim();
					refundState = paysource;
					if(paysource==2){
						refundState = 3;
					}else if(paysource==3){
						refundState = 2;
					}else if(paysource==5){
						var refund= $("#refund").attr('data-refund') //支付宝小程序传5、其他不传
						refundState= refund ? refund : refundState
						type = 2;
					}
					$.bootstrapLoading.start({ loadingTips: "正在退款..." });
					//alert(refundUrl+"***   orderid:"+orderid+"   ***   refundState:"+refundState+"   ***   wolfkey:"+wolfkey);
					$.ajax({
						url : refundUrl,
						data : {
							id : orderid,
							refundState : refundState,
							wolfkey : wolfkey,
							type : type,
							utype: 2
						},
						type : "POST",
						cache : false,
						success : function(data) {
							if (data.ok == 'error') {
								mui.alert('退款失败','提示',function(){
									window.location.reload();
								})
							} else if (data.ok == 'usererror') {
								var tipP=''
								var content1= ''
								var content2= ''
								if(paysource != 4 && paysource != 5){
									mui.alert('用户金额不足','提示',function(){
										
										window.location.reload();
									})
									return
								}else if(paysource == 4){ //钱包
									tipP= "用户钱包金额不足退费"
									content1= '<span class="green_span">管理中心</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span"> 会员管理 搜素到该会员 点击会员号 进入虚拟充值页面<i class="mui-icon mui-icon-arrowthinright"></i>虚拟充值够需要退款订单的到账金额后</span><i class="mui-icon mui-icon-arrowthinright"></i><span class="green_span">点击该订单退款才可完成退费</span>'
									content2= '<span class="green_span">管理中心</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span"> 会员管理 搜素到该会员 点击会员号 进入虚拟充值页面<i class="mui-icon mui-icon-arrowthinright"></i>虚拟清零后,手动转账</span>'
								}else { //在线卡
									tipP= "在线卡金额不足退费"
									content1= '<span class="green_span">管理中心</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span"> IC卡管理 搜素到该在线卡 点击在线卡卡号进入虚拟充值页面<i class="mui-icon mui-icon-arrowthinright"></i>虚拟充值够需要退款订单的到账金额后</span><i class="mui-icon mui-icon-arrowthinright"></i><span class="green_span">点击该订单退款才可完成退费</span>'
									content2= '<span class="green_span">管理中心</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span"> IC卡管理 搜素到该在线卡 点击在线卡卡号进入虚拟充值页面<i class="mui-icon mui-icon-arrowthinright"></i>虚拟清零后,手动转账</span>'
								}
								$('.addInfo_con .content1').html(content1)
								$('.addInfo_con .content2').html(content2)
								$('.addInfo_con').fadeIn()
								
									
							} else if (data.ok == 'moneyerror') {
								mui.alert('商户或合伙人金额不足','提示',function(){
									window.location.reload();
								})
							} else if (data.ok == 'ok') {
								mui.alert('退款成功','提示',function(){
									window.location.reload();
								})
							}else{
								mui.alert('操作错误','提示',function(){
									window.location.reload();
								})
							}
						},
					});
	    		}
	    	})
	    })
	    $('.refBtnBack').click(function(){
		    	var orderId = $("body").attr('data-orderid').trim();
		    	mui.confirm('确认撤回？',function(opations){
		    		if(opations.index===1){
		    			$.ajax({
		    				url: '/wxpay/withdrawWalletRefund',
		    				data: {
		    					id: orderId
		    				},
		    				type: 'post',
		    				success: function(res){
	    						mui.alert(res.messg,'提示',function(){
									window.location.reload();
								})
		    				},
		    				error:function(err){
		    					window.location.reload();
		    				}
		    			})
		    		}
		    	})
	    })
	    
	    $('.addInfo_con').click(function(){
			$('.addInfo_con').fadeOut()	
		})
	    
	})
	</script>
</body>
</html>
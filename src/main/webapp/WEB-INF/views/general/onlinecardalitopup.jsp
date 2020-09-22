<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>IC卡充值</title>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath }/js/my.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
	
	html,body,header,section,div,ul,li,p,button,h1,h2,h3 {margin: 0; padding: 0;}
	li { list-style: none; }
	a { text-decoration: none; }
	body {
		background-color: #f8f8f8;
	}
	.icPage {
		/*padding: 15px 0;*/
		font-size: 12px;
		color: #666;
	}

	.icPage header div {
		width: 90%;
		margin: 0 auto;
		text-align: center;
		padding: 25px 0 20px;
		/*border-bottom: 1px solid #D9D9D9;*/
	}
	.icPage header div span {
		color: #999;
	}
	.icPage section {
		/*padding-top: 20px; */
	}
	.icPage section h3 {
		margin-left: 5%;
		font-size: 14px;
		font-weight: 400;
		color: #666;
		text-align: center;
		margin: 15px 0 10px;
	}
	.icPage section .selectDiv {
		margin-top: 15px;

	}
	.icPage section .selectDiv ul {
		width: 90%;
		margin: 0 auto;
		padding-bottom: 15px;
		display: flex;
		flex-wrap: wrap;
		border-bottom: 1px solid #D9D9D9;
		justify-content: flex-start;
	}
	.icPage section .selectDiv li {
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
	.icPage section .selectDiv li.active {
		background-color: #4cd964;
		color: #fff;
		border: 1px solid #1EC63B;
	}
	.icPage section .selectDiv li:nth-child(3n-2){
		margin-left: 2vw;
	}
	.icPage section .displayDiv {
		margin-top: 32px;
		text-align: center;
		font-size: 35px;
		height: 35px;
	}
	.icPage section .displayDiv .icon_yen {
		margin-right: 10px;
	}
	.icPage section .displayDiv .textSpan {
		font-size: 33px;
	}
	.icPage section .btn {
		width: 70%;
		height: 10vw;
		border: 1px solid #22B14C;
		border-radius: 6px;
		text-align: center;
		line-height: 10vw;
		margin: 15px auto;
		background-color: #4cd964;
		color: #fff;
	}
	.icPage section .btn:active {
		background-color: #1EC63B;
	}
	#tipDiv{
		display: none;
		height: 240px;
		justify-content: center;
		align-items: center; 
	}
	#tipDiv p {
		color: #999;
		padding: 0 10px;
	 }
	 
		
		.unActivation {
			color: #225ab1d1 !important;
		}
		.loss {
			color: #F47378 !important;
		}
		
		.showInfo {
			width: 90%;
			margin: 0 auto;
			overflow: hidden;
		}
		.showInfo>div {
			width: 49.5%;
			overflow: hidden;
			float: left;
			margin-bottom: 15px;
		}
		.showInfo>div:last-child{
			min-width: 49.5%;
			width: auto;
		}
		.showInfo>div:nth-child(2n){
			float: right;
		}
		
		.showInfo>div>span {
			display: block;
			box-sizing: border-box;
			border: 1px solid #ccc;
			border-radius: 5px;
			text-align: center;
			line-height: 35px;
			background-color: rgba(0,0,0,0.01);
			padding: 0 12px;
		}
		.showInfo>div>span:first-child{
			float: left;
		}
		.showInfo>div>span:last-child{
			overflow: hidden
		}
		#remake {
			position: absolute;
			left: 5%;
			bottom: 5%;
			color: #999;
		}
		.left_right {
			overflow: hidden;
		}
		.left_right .left{
			float: left;
			height: 100%
		}
		.left_right .right{
			overflow: hidden;
		}
		.left_right .right p {
			font-size: 14px;
		}
</style>
</head>
<body data-info="${cardID}" id="bodyHtml">
	<input type="hidden" value='${datamap.code}' id="isIdInp">
	<input type="hidden" value='${datamap.message}' id="isIdmessage">
	<input type="hidden" value='${userid}' id="userid">
	<form action="/alipay/scanqronline" method="POST" style="display: none">
		<input type="hidden" value='${user.openid}' name="openid">
		<input type="hidden" value='${online.cardID}'  name="cardnum">
		<input type="hidden" value=0 name="choosemoney" >
		<input type="hidden" value='' name="tempid">
	</form>
	<div class="icPage">
		<header>
			<div style="font-size: 14px;"><span>卡号：</span>${cardID}</div>
		</header>
		<div class="showInfo">
			<div>
				<span>持卡人</span>
				<span>
					<c:choose>
						<c:when test="${onlineuser.username != null}">${onlineuser.username}</c:when>
						<c:otherwise>— —</c:otherwise>
					</c:choose>
				</span>
			</div>
			<div>
				<span>电&nbsp;&nbsp;&nbsp;话</span>
				<span>
					<c:choose>
						<c:when test="${onlineuser.phoneNum != null}">${onlineuser.phoneNum}</c:when>
						<c:otherwise>— —</c:otherwise>
					</c:choose>
				</span>
			</div>
			<div>
				<span>余额</span>
				<span class="unActivation">
					<a href="/general/scanonlinedemand?cardnum=${online.cardID}">
						<c:if test="${online.relevawalt != 1}" >
						<c:choose>
							<c:when test="${online.money!=null}"><fmt:formatNumber value="${online.money}" pattern="0.00"/></c:when>
							<c:otherwise>0.00</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${online.relevawalt ==1}" >
						<c:choose>
							<c:when test="${online.money!=null}"><fmt:formatNumber value="${user.balance}" pattern="0.00"/></c:when>
							<c:otherwise>0.00</c:otherwise>
						</c:choose>
					</c:if>
					</a>元
				</span>
			</div>
			<div>
				<span>卡状态</span>
				<span>${online.status==0 ? "未激活" : online.status==1 ? "正常" : online.status==2 ? "挂失" : online.status==4 ? "未激活" : "未激活"}</span>
			</div>
			<div>
				<span>小&nbsp;&nbsp;&nbsp;区</span>
				<span>
					<c:choose>
						<c:when test="${areadata.name != null}">${areadata.name}</c:when>
						<c:otherwise>— —</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
		<section id="section1">
			<h3>请选择充值金额</h3>
			<div class="selectDiv">
				<ul>
					<c:if test="${datamap.code==200}">
					<c:forEach items="${datamap.sontemp}" var="sontemp">
						<li data-sontemId= "${sontemp.id}" data-money="<fmt:formatNumber value="${sontemp.money}" pattern="0" />">${sontemp.name}</li>
					</c:forEach>
					</c:if>
				</ul>
			</div>
			<div class="displayDiv"><span class="icon_yen">¥</span><span id="moneySpan">0</span><span class="textSpan">元</span></div>
			<div class="btn">确认充值</div>
		</section>
		<div id="tipDiv">
			<p>提示内容</p>
		</div>
		<div id="remake">
			<div class="left_right">
				<div class="right">
					<c:choose>
						<c:when test="${datamap.code==200}"><p>如有疑问，敬请联系：<a href="tel:${datamap.ptemphone!=null ? datamap.ptemphone : meruser.phoneNum}">${datamap.ptemphone!=null ? datamap.ptemphone : meruser.phoneNum}</a></p></c:when>
						<c:when test="${datamap.telphone!=null}"><p>如有疑问，敬请联系：<a href="tel:${datamap.telphone}">${datamap.telphone}</a></p></c:when>
						<c:otherwise><p></p></c:otherwise>
					</c:choose>
				</div>	
			</div>
		</div>
	</div>
	<script>
		$(function(){
			var selectMoney= 0; //点击获取金钱
			var sontemId
			var cardnum = $('#bodyHtml').attr('data-info').trim();
			var openid = "${openid}";
			$('.selectDiv li').click(function(){
				var Semoney= $(this).attr('data-money').trim()
				$('#moneySpan').text(Semoney)
				$(this).addClass('active')
				$(this).siblings().removeClass('active')
			})
			var code = $('#isIdInp').val().trim();
			var message = $('#isIdmessage').val().trim();
			/* if(code==101){
				 mui.toast("该卡未被绑定,请去微信公众号中先绑定。",{ duration:'1500', type:'div' })
				 $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("该卡未被绑定,请去微信公众号中先绑定")
                 return false
			}else if(code==102){
				 mui.toast("该卡未激活使用,请先刷卡激活。",{ duration:'1500', type:'div' })
				 $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("该卡未激活使用,请先刷卡激活")
                 return false
			}else if(code==100 || code==103 ){
				 mui.toast("未获取账户信息，暂时无法访问",{ duration:'1500', type:'div' })
				 $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("未获取账户信息，暂时无法访问")
                 return false
			}else if(code==''){
				 mui.toast("请在微信关注自助充电平台公众号，再扫码绑定后使用",{ duration:'1500', type:'div' })
				 $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("请在微信关注自助充电平台公众号，再扫码绑定后使")
                 return false
			}else if(code==104){
				 mui.toast("账户未绑定，暂无法充值",{ duration:'1500', type:'div' })
				  $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("账户未绑定，暂无法充值")
                 return false
			}else if(code==105){
				 mui.toast("卡片处于该状态，暂无法充值",{ duration:'1500', type:'div' })
				  $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text("卡片处于该状态，暂无法充值")
                return false
			} */
			
			if(code!=200){
				 mui.toast(message,{ duration:'1500', type:'div' })
				  $('#section1').css('display',"none")
				 $('#tipDiv').css('display',"flex")
				 $('#tipDiv p').text(message)
               return false
			}

			$('.icPage .btn').click(function(){
				selectMoney= parseInt($('#moneySpan').text())
				if(!selectMoney){
					mui.toast('请选择充值金额',{ duration:'1500', type:'div' })
					return false
				}
				sontemId= $('.selectDiv ul li.active').attr('data-sontemId')
				$('input[name="choosemoney"]').val(selectMoney)
				$('input[name="tempid"]').val(sontemId)
				// 这里可以执行点击充值操作的逻辑
				
				//$('form')[0].submit()
				$.ajax({
    			url : "${hdpath}/alipay/useronlinecardpay",
    			data : {
    				cardnum : cardnum,
    				tempid : sontemId,
    				uid : $("#userid").val(),
    			},
    			type : "post",
    			dataType : "json",
    			beforeSend: function() {
    				$.bootstrapLoading.start({ loadingTips: "正在支付..." });
    		    },
    			success : function(data) {
    				if (data.wolfcode == 1) {
		          		 tradePay(data.trade_no);
    				} else if (data.wolfcode == 0) {
    					alert("系统异常");
    				}
    			},
    	        complete: function () {
    	            $.bootstrapLoading.end();
    	        }
        	})
				
			})
			
		})
		// 由于js的载入是异步的，所以可以通过该方法，当AlipayJSBridgeReady事件发生后，再执行callback方法
function ready(callback) {
     if (window.AlipayJSBridge) {
         callback && callback();
     } else {
         document.addEventListener('AlipayJSBridgeReady', callback, false);
     }
}

function tradePay(tradeNO) {
    ready(function(){
         // 通过传入交易号唤起快捷调用方式(注意tradeNO大小写严格)
         AlipayJSBridge.call("tradePay", {
              tradeNO: tradeNO
         }, function (data) {
             if ("9000" == data.resultCode) {
                 alert("支付成功");
             }
             AlipayJSBridge.call('closeWebview');
         });
    });
}
	</script>

</body>
</html>
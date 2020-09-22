 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<title>包月信息</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/mui/js/mui.min.js"></script>
 <style>
        body {
            padding: 15px 10px;
            font-size: 14px;
            color: #666;
        }
        .monthly header h3 {
            font-size: 16px;
            text-align: center;
            margin-bottom: 25px;
        }
        .monthly h5 {
            font-size: 14px;
            color: #999;
            font-weight: 400;
            padding-left: 8px;
        }
        .monthly .monthlyDiv{
            padding: 0 2%;
            margin-top: 15px;
        }
        .monthly .monthlyMoudle{
            display: flex;
            justify-content: flex-start;
            flex-wrap: wrap;
        }
         .monthly .monthlyMoudle li {
            width: 30%;
            margin-left: 5%;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 15px;
            background-color: #f5f7fa;
            transition: all .4s;
            text-align: center;
            height:37px;
            line-height: 37px;
         }
         
        .monthly .monthlyMoudle li:nth-child(3n-2){
            margin-left:0;
        }
        .monthly .confirmBtn{
            margin-top: 20px;
        }
        .monthly .confirmBtn {
            text-align: center;
        }
        .monthly .confirmBtn button {
            width: 85%;
            padding: 8px;
        }
        .monthly .monthlyMoudle li.active {
            background-color: #4cd964;
            border: 1px solid #13853575;
            color: #fff;
        }
        
        .showInfo {
			margin: 0 auto 10px;;
			overflow: hidden;
			padding: 0 8px;
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
			height: 37px;
			line-height: 37px;
			background-color: rgba(0,0,0,0.01);
			padding: 0 12px;
		}
		.showInfo>div>span:first-child{
			float: left;
		}
		.showInfo>div>span:last-child{
			overflow: hidden
		}
		@media screen and (max-width: 355px) {
		   .showInfo>div>span {
		   		padding: 0 5px;
		   }
		}
       
        .info{
        	width: 100vw;
        	height: 60vh;
        	display: flex;
        	align-items: center;
        	justify-content: center;
        }
        .mask {
	        position: fixed;
		    top: 0;
		    left: 0;
		    right: 0;
		    bottom: 0;
		    background-color: rgba(0, 0, 0, .3);
		    z-index: 8;
		    display: flex;
		    align-items: center;
		    justify-content: center;
		    color: #fff;
		    font-size: 16px;
	    }
    </style>
</head>
<body>
	<input id="openid" type="hidden" value="${user.openid}">
	<c:if test="${ifmonth == 2 }"><div class="mask">商家已禁用此功能</div></c:if>
	<c:if test="${errortype == 1 }"><div class="info">暂未绑定商户，此功能未开放</div></c:if>
	<c:if test="${errortype == 2 }"><div class="info">所属商户暂未开放包月功能，如有疑问可联系商户</div></c:if>
	<c:if test="${errortype == 3}">
	<div class="monthly">
        <header>
            <h3>包月信息</h3>
        </header>
       <c:if test="${hasMonth == 1 }">
       		<div class="showInfo">
				<div>
					<span>总剩余次数</span>
					<span class="unActivation">
							<a href="/general/queryMonthRecord?uid=${user.id }">${packageMonth.everymonthnum == 0 ? '不限' : packageMonth.surpnum}次</a>
					</span>
				</div>
				<div>
					<span>今日剩余次数</span>
					<span>${packageMonth.everydaynum == 0 ? '不限' : packageMonth.todaysurpnum}次</span>
				</div>
				<div>
					<span>到期时间</span>
					<span><fmt:formatDate value="${packageMonth.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</div>
			</div>
       </c:if>
        
        <h5>请选择包月套餐</h5>
        <div class="monthlyDiv">
            <ul class="monthlyMoudle">
            	<c:forEach items="${templatelist }" var="temp">
	                <li data-id="${temp.id }">
	                   	${temp.name }
	                </li>
            	</c:forEach>
            </ul>
        </div>
        <div class="confirmBtn">
            <button class="mui-btn mui-btn-success" id="submitbtn">确认充值</button>
        </div>
    </div>
	</c:if>
	<script>
        $(function(){
        	var openid =$('#openid').val().trim()
            $('.monthlyMoudle li').click(function(){
                $(this).addClass('active')
                $(this).siblings().removeClass('active')
            })
            var prepay_id;
			var paySign;
			var appId;
			var timeStamp;
			var nonceStr;
			var packageStr;
			var signType;
			var ordernum;
            $('#submitbtn').click(function(){
            	var $selectEle= $('.monthlyMoudle').find('.active')
            	$('#submitbtn').prop('disabled',true)
            	if($selectEle.length <= 0){
            		mui.alert('请先选择充值套餐');
            		$('#submitbtn').removeAttr('disabled');
            		return;
            	}
            	var id= $selectEle.attr('data-id').trim()
            	$.ajax({
            		url : "${hdpath}/general/packageMonthPay",
					data : {tempid : id,openid : openid},
					type : "post",
					dataType : "json",
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
            	})
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
						mui.alert('支付成功', function() {
							$('#submitbtn').removeAttr('disabled');
							location.reload();
						});
					} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
						mui.alert('支付取消', function() {
							$('#submitbtn').removeAttr('disabled');
						});
					} else if (res.err_msg == "get_brand_wcpay_request:fail") {
						mui.alert('支付失败', function() {
							$('#submitbtn').removeAttr('disabled');
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
        })
        
    </script> 
</body>
</html>
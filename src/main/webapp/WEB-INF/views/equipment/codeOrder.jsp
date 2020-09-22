<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>设备订单</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<link rel="stylesheet" type="text/css" href="${hdpath }/css/iconfont.css" />
<!--App自定义的css-->
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<link rel="stylesheet" href="/css/base.css">
<script src="${hdpath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1793652_iz45o902nu9.eot?t=1588239711468'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1793652_iz45o902nu9.eot?t=1588239711468#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAUMAAsAAAAACigAAAS+AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDSAqHEIYEATYCJAMYCw4ABCAFhG0HYxvFCFFUTnJkPxIyt2s0w7jFGkFYmFR4UDXn/3j4717yvnnzZ2ux2nC903aqx5eWNxUklIAHKEABDed4F/jDv6e9JB2TQXRy5ER/ge2I+2rP+z+XOQQhI9SRPAU0Uuj/a9Ny0o8DIcz2P8eMl2XD/EBzyUhK2huNx8kDirpMKzrB24EUEJ5F7LrZ6sEE3k1g2CJl0Nnt5R2GKnFXIF7yVMHQY1YTGGFQ1yLOrOILOoPlU9kC4LPx9fEf7IKRokvivW6frzEcfik9/U68xwUn4Vl1TtzOI7EPqMTHSPerjpXehxj+LO86B0YNKmpVh/B6Snginn73PR656A+jUopOFvrPE0RD6lFgLjMdv5QUsaPxglFCkwCjAg0BowrNb0gXS98q3DAJxDIA8RZE8r6jUqdIOYyLRYddkJ29OMaG8UCcnOxJaMWx7+gRd9Wo7foRTyVF/Yjsw3x05E0m+5GP/SgU//cVeDkzM1BZ7yohiDlGVKGoofDVCviq3vP4+FG2pobbG812FTSJ5X0VBBGVvFh5OQB4RRozkt3SWGdv4WuNzwvTNyzosE0tiTE0Lgbj3cys76I+T/ShbO97AIR3c7NWemVJdD62CmVRu5gUiu9xMZlcwNrZK2QpUCHgA+HT4dd68CY/+hz/8KM8hWDveCtrIaROrm1FHQIAregp5S8U+YZ3v1OM9G7HZWI63svFdRG+gZnYznfS0bcrVdfufAkxFLT/a80pI+3yvZzbQlravtIfPusgg1uBh1eGk/dIOTxCrVuFJIEWgEeQ8sD1w0IwqgANK61mofHTWKpLMZoAFXvu8hx7LMSigpPzubLzePOSn3ECvegXt15kclSiMOvHOJcCydNZlIZmYNTm2WYqloHSrLydjPgu3GfdL/iMnNjnACeYZ59mnwecYM5tmc2ajUvR0htN++xWN1w3Vuxe3V6SNKoRaHiGwzl1dTmqAhaUsGCuq3FeoIZ073SSnsoAfx1d+gj3f0I2l0hfEYFFII7EbJM42EnbkpGDQ1WQBDH3EuzS3DBjZvsaBtErveApt6zUTs9+Pa/ELoHJj1N7Jdf/Pdc9EpgNgqKCI4iiuiIElpFjLzTwt++6T0JQRITwQiKJEZKCugI6QpaDYoC+NMeTMNh49iXOfp8dmc2uyyckNluZvzLn2vweh9/0v4ZNGP8fRCv4ubcYhlBfWkE14gtMI7PkSpRk7KlfmWhp9d1d2SjQPOlxMI2GpQGuP/+0q6q8c4DjDIN3CaAYsABp0DqiYvahM+IAmkGHMGyP3fkjZmTqiDqEXS8ZhElDUIz7DmnSNqJiLqEzjwrNZCAw7C64rjhiMxLGVoaUoxgjfzAhEk5kmxubta+U+koZt8xgfZKxeQw1VNX9uTsxmSqOyQrpLtslGGukRlt2P6oqwXQjBUVuFe9cs1dTi416p1WU1DB2yiCKQ2Io0g+UICJYog13xoHDvyIpr6KYhK4f/k/EsPL5kQZV6hkQdzVn6tqV5VmC1E42J+G2xTJEDdnCSSopLVB69LoCiTgr8QWSjT016hebVb1qXluv5REYJh7XokRGjS5aDBgZ+6T1dePm4tmD6JzTWPEsjgzxbyacfuc8yMryIlc8iFRTmRIAAAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1793652_iz45o902nu9.woff?t=1588239711468') format('woff'),
  url('//at.alicdn.com/t/font_1793652_iz45o902nu9.ttf?t=1588239711468') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1793652_iz45o902nu9.svg?t=1588239711468#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-dingdan:before {
  content: "\e669";
}

.icon-gerenzhongxin:before {
  content: "\e653";
}

.icon-shijian:before {
  content: "\e681";
}

.icon-bianhao:before {
  content: "\e62b";
}

.mui-content {
	padding: 15px 10px;
}
.mui-input-group {
	margin-bottom: 15px;
	border: 1px solid #6BD089;
	background: #f5f7fa;
	border-radius: 6px;
	overflow: hidden;
	padding: 4px 0;
	box-shadow: 0 0 4px #8f8f94;
}
.mui-input-group.reimburse {
	border-color: #f99599;
}

.mui-table-view ,
.mui-table-view::after,
.mui-table-view::before {
	background-color: transparent;
}
.mui-table-view-cell {
	font-size: 13px;
	color: #333;
	padding: 4px 12px;
	background-color: transparent;
}
.mui-table-view-cell::after {
	background-color: transparent;
}
.mui-table-view-cell i {
	color: #22B14C;
	margin-right: 5px;
	font-size: 15px;
}
.mui-table-view-cell span{
	float: right;
    color: #777;
}
.mui-table-view-cell .btn-col {
	font-size: 12px;
	color:#fff;
	padding: 0px 12px;
	line-height: 1.8em;
}
.mui-table-view-cell .mui-badge {
	font-size: 12px;
	color:#fff;
	padding: 0px 7px;
	line-height: 1.8em;
	border-radius: 4px;
	border: 1px solid #4cd964;
}
.mui-content .mui-table-view-cell .muibadge .mui-btn{
	font-size: 12px;
	padding: 0px 7px;
	line-height: 1.8em;
}
</style>
<body>
  <div class="mui-content">
    <c:if test="${sizenu==0}"><span>暂无数据</span></c:if>
    <c:if test="${sizenu!=0}">
    <c:forEach items="${codeorder}" var="order" varStatus="as">
    	<div class="mui-input-group <c:if test="${order.status==2}">reimburse</c:if>" style="padding-bottom: 8px;">
	    <ul class="mui-table-view">
		    <li class="mui-table-view-cell"><i class="iconfont icon-dingdan"></i>订单号
		        <span class="muibadge">${order.ordernum}</span>
		    </li>
		    <li class="mui-table-view-cell"><i class="iconfont icon-gerenzhongxin"></i>付款人 
		        <span class="muibadge">${order.uusername}</span>
		    </li>
		    <li class="mui-table-view-cell"><i class="iconfont icon-consumption"></i>交易金额
		        <c:if  test="${order.status==1}">
				   <span class="mui-badge mui-badge-success" style="right:75px;">￥ +<fmt:formatNumber value="${order.money !=null ? order.money : 0}" pattern="0.00"/></span>
				   <button type="button" onclick="verifyorder('${order.id}','${order.paytype}','${order.paysource}','${order.ordernum}')" class="mui-btn mui-btn-danger btn-col" data-num="${number}">退款</button>
				</c:if >
				<c:if test="${order.status!=1}">
					<c:if test="${order.number != 2}">
						<span class="mui-badge mui-badge-success" style="right:75px;">￥ -<fmt:formatNumber value="${order.money !=null ? order.money : 0}" pattern="0.00"/></span>
					    <button type="button" disabled="disabled" style="background-color: gray;border-color: gray;" class="mui-btn btn-col" data-num="${number}">退款</button>
					</c:if>
					<c:if test="${order.number == 2}">
						<span class="mui-badge mui-badge-success" style="right:75px;">￥ -<fmt:formatNumber value="${order.money !=null ? order.money : 0}" pattern="0.00"/></span>
					    <button type="button" onclick="refBack('${order.charorder}')" class="mui-btn mui-btn-danger btn-col" data-num="${number}">撤回</button>
					</c:if>
				</c:if>
				<c:if test="${order.paysource==1}">
					 <li class="mui-table-view-cell"><i class="iconfont icon-bianhao"></i>充电功率
				        <span class="muibadge"><a href="/merchant/powerbrokenline?source=1&orderId=${order.id}"><button type="button" class="mui-btn mui-btn-success">功率查看</button></a></span>
				      </li>
				</c:if>
		    </li>
		    <li class="mui-table-view-cell"><i class="iconfont icon-shijian"></i>交易时间
		        <span class="muibadge"><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		    </li>
		 </ul>
		 </div>
	</c:forEach>
	</c:if>
  </div>
</body>
<script type="text/javascript">

function refBack(orderid){ //撤回部分退费
	mui.confirm('确认撤回吗？',function(opations){
		if(opations.index===1){
			$.ajax({
				url: '/wxpay/withdrawWalletRefund',
				data: {
					id: orderid
				},
				type: 'post',
				success: function(res){
					mui.alert(res.messg,'提示',function(){
						window.location.reload();
					})
				},
				error:function(err){
					mui.alert("异常",'提示',function(){
						window.location.reload();
					})
				}
			})
		}
	})
}

function verifyorder(orderId,paytype,paysource,ordernum){
	var url = "${hdpath}/merchant/verifyorder";
	$.ajax({
		url : url,
		data : {
			ordernum : ordernum,
			paysource : paysource
		},
		type : "POST",
		cache : false,
		success : function(e) {
			if (e!=0 && e!=null) {
				refundMoney(e,paytype,paysource);
			}else{
				alert("该订单已退过费用。")
			}
		}
	});
}

function refundMoney(orderId, paytype, paysource){
	var statu = confirm("是否确认退款?");
	if(orderId==0){
		alert("该订单暂无法退款");
        return false;
    }
	if(!statu){
        return false;
    }
	var url;
	var refundState;//1:充电、 2:离线、 3:投币
	var wolfkey = 0;
	if(paysource==1){//充电模块  chargeRecord
		if(paytype==1){
			url = "${hdpath}/wxpay/doWalletReturn";
		}else if(paytype==2){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==3){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}else if(paytype==4){
			url = "${hdpath}/wxpay/wxDoRefund";
			wolfkey = 0;
		}else if(paytype==5){
			url = "${hdpath}/alipay/aliDoRefund";
			wolfkey = 0;
		}
		refundState = 1;
	}else if(paysource==2){//脉冲模块 inCoinsService
		if(paytype==1){
			url = "${hdpath}/wxpay/doWalletReturn";
		}else if(paytype==2){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==3){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}else if(paytype==4){
			url = "${hdpath}/wxpay/wxDoRefund";
			wolfkey = 3;
		}else if(paytype==5){
			url = "${hdpath}/alipay/aliDoRefund";
			wolfkey = 4;
		}
		refundState = 3;
	}else if(paysource==3){//离线充值机 offlineCardService
		if(paytype==1){
				url = "${hdpath}/wxpay/doWalletReturn";
		}else if(paytype==2){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==3){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}
		refundState = 2;
	} else if(paysource==4){//用户充值钱包  moneyService
		if(paytype==2){
			url = "${hdpath}/wxpay/doRefund";
			refundState = 4;
		}/* else if(paytype==1){//离线卡   微信
			
		} */
	}
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : {
			id : orderId,
			refundState : refundState,
			pwd : 0,
			utype : 2,
			wolfkey : wolfkey,
		},
		type : "POST",
		cache : false,
		success : function(data) {
			if (data.ok == 'error') {
				mui.alert('退款失败','提示',function(){
					window.location.reload();
				})
			} else if (data.ok == 'usererror') {
				mui.alert('用户金额不足','提示',function(){
					window.location.reload();
				})
			} else if (data.ok == 'ok') {
				mui.alert('退款成功','提示',function(){
					window.location.reload();
				})
			} else if (data.ok == 'moneyerror') {
				mui.alert('金额不足','提示',function(){
					window.location.reload();
				})
			}else{
				mui.alert('退款异常失败','提示',function(){
					window.location.reload();
				})
			}
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
};

</script>
</html>
<script>
	/* $(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
			location.replace('/equipment/list?wolfparam=2');
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	}); */
</script>
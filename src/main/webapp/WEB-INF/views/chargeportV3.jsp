<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<title>
<c:if test="${brandname != null }">${brandname}电动车充电设备</c:if>
<c:if test="${brandname == null }">自助充电平台</c:if>
</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/css/base.css">
<script src="${hdpath}/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFontToCharge.jspf"%>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1373828_3ris553u4cj.eot?t=1586399943774'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1373828_3ris553u4cj.eot?t=1586399943774#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1373828_3ris553u4cj.woff?t=1586399943774') format('woff'),
  url('//at.alicdn.com/t/font_1373828_3ris553u4cj.ttf?t=1586399943774') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1373828_3ris553u4cj.svg?t=1586399943774#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-xiaoquguanli:before {
  content: "\e60e";
}

.icon-tubiao211:before {
  content: "\e61c";
}

.icon-dianhua:before {
  content: "\e60c";
}

.icon-diannao:before {
  content: "\e61e";
}

.icon-diannao-copy:before {
  content: "\e7b5";
}

.icon-tishishuoming:before {
  content: "\e64c";
}

.icon-tel-fill:before {
  content: "\e639";
}

.icon-weixin:before {
  content: "\e7b4";
}

.icon-weibiaoti--:before {
  content: "\e628";
}

.icon-shouye:before {
  content: "\e651";
}

.icon-qianbao:before {
  content: "\e613";
}




.animated {
  -webkit-animation-duration: 0.4s;
  animation-duration: 0.4s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}
@-webkit-keyframes slideInUp {
  from {
    -webkit-transform: translate3d(0, 500%, 0);
    transform: translate3d(0, 50%, 0);
    visibility: visible;
  }
  to {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }
}
@keyframes slideInUp {
  from {
    -webkit-transform: translate3d(0, 50%, 0);
    transform: translate3d(0, 50%, 0);
    visibility: visible;
  }
  to {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }
}
.slideInUp {
  -webkit-animation-name: slideInUp;
  animation-name: slideInUp;
}
@-webkit-keyframes slideInDown {
  from {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    visibility: visible;
  }
  to {
    -webkit-transform: translate3d(0, 100%, 0);
    transform: translate3d(0, 100%, 0);
  }
}
@keyframes slideInDown {
  from {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
    visibility: visible;
  }
  to {
    -webkit-transform: translate3d(0, 100%, 0);
    transform: translate3d(0, 100%, 0);
  }
}
.slideInDown {
  -webkit-animation-name: slideInDown;
  animation-name: slideInDown;
}
.mui-backdrop {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 3;
  background-color: rgba(0, 0, 0, 0.3);
}
body {
  background-color: #fff;
  overflow: hidden;
  font-family: inherit;
}
.app {
  font-size: 0.512rem;
  color: #666;
}
.app header {
  padding: 0.64rem;
  background-color: #fff;
  border-bottom: 0.04266667rem solid #EAEAEA;
  box-shadow: 0.08533333rem 0.08533333rem 0.256rem #D9D9D9;
}
.app header .info {
  overflow: hidden;
}
.app header .info .deviceName {
    width: 45%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 0.68266667rem;
    text-align: right;
    color: #000;
    font-weight: bold;
    float: right;
}
.app header .info .deviceName i {
  color: #22B14C;
  font-weight: bold;
  font-size: 0.93866667rem;
}
.app header .info .deviceCode {
  font-size: 0.68266667rem;
  color: #000;
  font-weight: bold;
  float: left;
}
.app header .info .deviceCode i {
  color: #22B14C;
  font-size: 0.65rem;
}
.app header .callTel {
  font-size: 0.68266667rem;
  height: 1.06666667rem;
  margin-top: 0.42666667rem;
  line-height: 1.06666667rem;
  color: #666;
}
.app header .callTel i {
  color: #22B14C;
  font-weight: bold;
  font-size: 0.7rem;
}
.app header .callTel a {
  text-decoration: none;
  color: #0956ab;
}
.app header .detailLink {
  font-size: 0.68266667rem;
  color: #22B14C;
  margin-top: 0.42666667rem;
}
.app header .detailLink i {
   color: #22B14C;
   font-weight: bold;
   font-size: 0.7rem;
}
.app section {
  padding: 0.85333333rem 0.64rem;
}
.app section .top > span {
  font-size: 0.68266667rem;
  color: #000;
}
.app section .top > ul {
  float: right;
}
.app section .top > ul li {
  float: right;
  width: 2.98666667rem;
  font-size: 0.68266667rem;
  color: #999;
}
.app section .top > ul li div {
  display: inline-block;
  width: 0.59733333rem;
  height: 0.59733333rem;
  vertical-align: middle;
  margin-right: 0.21333333rem;
  border-radius: 0.128rem;
  -moz-box-shadow: 0rem 0rem 0.128rem #666;
  -webkit-box-shadow: 0rem 0rem 0.128rem #666;
  box-shadow: 0rem 0rem 0.128rem #666;
}
.app section .top > ul li div.use {
  background-color: #E8090A;
  color: #fff;
}
.app section .top > ul li div.fi {
  background-color: #a6a6a6;
  color: #fff;
}
.app section .mid {
  margin-top: 0.64rem;
}
.app section .mid > ul li {
  width: 18%;
  position: relative;
  height: 0;
  padding-bottom: 18%;
  float: left;
  border: 0.02133333rem solid #efefef;
  margin-left: 2.5%;
  margin-bottom: 4%;
  border-radius: 0.34133333rem;
  box-shadow: 0.08533333rem 0.08533333rem 0.256rem #D9D9D9;
}
.app section .mid > ul li.active {
  background-color: #22B14C;
}
.app section .mid > ul li.active div {
  color: #fff;
}
.app section .mid > ul li.use {
  background-color: #E8090A;
}
.app section .mid > ul li.use div {
  color: #fff;
}
.app section .mid > ul li.fi {
  background-color: #a6a6a6;
}
.app section .mid > ul li.fi div {
  color: #fff;
}
.app section .mid > ul li:nth-child(5n-4) {
  margin-left: 0;
}
.app section .mid > ul li > div {
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.85333333rem;
  color: #000;
}
.app footer {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  border-top: 0.04266667rem solid #eaeaea;
  box-shadow: 0rem -0.21333333rem 0.256rem 0rem #D9D9D9;
  z-index: 7;
  font-size: 0.68266667rem;
}
.app footer .payMoney {
  width: 65%;
  height: 3.41333333rem;
  line-height: 3.41333333rem;
  float: left;
  padding: 0 0.64rem;
  color: #333;
  background-color: #fff;
}
.app footer .startCharge {
  overflow: hidden;
  height: 3.41333333rem;
  line-height: 3.41333333rem;
  text-align: center;
  background-color: #22B14C;
  color: #fff;
}
.app .slideDiv {
  position: absolute;
  width: 100%;
  background-color: #fff;
  box-shadow: 0rem -0.21333333rem 0.256rem 0rem #D9D9D9;
  left: 0;
  bottom: 0;
  padding-bottom: 3.456rem;
  z-index: 6;
 /*  overflow: hidden; */
  overflow: auto;
  max-height: 70vh;
}
.app .slideDiv .charge {
  padding: 0.64rem 0.64rem 0;
}
.app .slideDiv .charge .top {
  line-height: 1.70666667rem;
  font-size: 0.68266667rem;
  color: #000;
}
.app .slideDiv .charge .top span:last-child {
  color: #666;
  font-size: 0.59733333rem;
  float: right;
}
.app .slideDiv .charge .top span:last-child #portNum {
  font-weight: 700;
  color: #22B14C;
}
.app .slideDiv .charge .select {
  border-bottom: 0.04266667rem solid #eaeaea;
  padding-bottom: 0.42666667rem;
/*   min-height: 5.1626rem; */
}
.app .slideDiv .charge .select button {
  width: 30%;
  height: 12vw;
  float: left;
  margin-left: 5%;
  margin-bottom: 0.42666667rem;
  padding: 0.21333333rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 0.512rem;
}
.app .slideDiv .charge .select button:nth-child(3n-2) {
  margin-left: 0;
}
.app .slideDiv .charge .select button:active {
  background-color: transparent;
  color: #000;
}
.app .slideDiv .charge .select button.active {
  background-color: #22B14C;
  color: #fff;
}
.app .slideDiv .charge .payType {
  font-size: 0.68266667rem;
  height: 1.28rem;
  line-height: 1.28rem;
  margin-top: 0.64rem;
  color: #000;
}
.app .slideDiv .charge .payType div > span:last-child {
  float: right;
}
.app .slideDiv .charge .payType div > span:last-child i.icon-weixin {
  font-size: 0.768rem;
  color: #48AF52;
  margin-right: 0.21333333rem;
}
.app .slideDiv .charge .payType div > span:last-child i.icon-qianbao {
  font-size: 0.93866667rem;
  color: #DFB94C;
  margin-right: 0.21333333rem;
}
.app .slideDiv .charge .payType .monthPay {
  display: none;
}
.app .slideDiv .paySelect {
  /* position: absolute;
  left: 0;
  bottom: 0;
  right: 0;
  top: 0;
  z-index: 7; */
  background-color: #fff;
  padding: 0.42666667rem 0.64rem 0.64rem;
}
.app .slideDiv .paySelect .title {
  height: 1.92rem;
  line-height: 1.92rem;
  text-align: center;
  font-size: 0.68266667rem;
  color: #000;
  position: relative;
}
.app .slideDiv .paySelect .title .back {
  float: left;
}
.app .slideDiv .paySelect .title:after {
  content: '';
  position: absolute;
  left: -0.64rem;
  right: -0.64rem;
  bottom: 0;
  height: 0.04266667rem;
  background-color: #eaeaea;
}
.app .slideDiv .paySelect ul li {
  height: 2.13333333rem;
  line-height: 2.13333333rem;
  font-size: 0.68266667rem;
  color: #000;
  position: relative;
}
.app .slideDiv .paySelect ul li .walletMoneyDetail {
	font-size: 0.6rem;
}
.app .slideDiv .paySelect ul li .monlyTime {
	font-size: 0.6rem;
}
.app .slideDiv .paySelect ul li.firbid {
	display: none;
}
/* .app .slideDiv .paySelect ul li.firbid:before {
	display: none;
	content: '微信支付已被禁用';
	padding: 0 0.64rem;
	color: #aaa;
	font-size: 0.68266667rem;
	position: absolute;
	left: -0.64rem;
	top:0;
	height: 2.13333333rem;
	right: -0.64rem;
	z-index: 88;
	background: rgba(255,255,255,.5);
	text-align: right;
	line-height: 2.13333333rem;
} */
.app .slideDiv .paySelect ul li i:nth-of-type(1) {
  margin-right: 0.42666667rem;
  font-size: 0.85333333rem;
}
.app .slideDiv .paySelect ul li i.icon-weixin {
  color: #48AF52;
  font-size: 0.8rem;
}
.app .slideDiv .paySelect ul li i.icon-qianbao {
  font-size: 0.8rem;
  color: #DFB94C;
}
.app .slideDiv .paySelect ul li i:nth-of-type(2) {
  float: right;
  font-size: 1.92rem;
  color: #22B14C;
  display: none;
}
.app .slideDiv .paySelect ul li:after {
  content: '';
  position: absolute;
  left: 0;
  right: -0.64rem;
  bottom: 0;
  height: 0.04266667rem;
  background-color: #eaeaea;
}
.app .slideDiv .paySelect ul li.active i:nth-of-type(2) {
  display: block;
}
.app .chargeDetail {
  position: absolute;
  background-color: #fff;
  width: 100%;
  left: 0;
  bottom: 0;
  z-index: 10;
  padding: 0.64rem;
  display: none;
  box-shadow: 0rem -0.21333333rem 0.256rem 0rem #D9D9D9;
}
.app .chargeDetail .areaName {
  font-size: 0.68266667rem;
  color: #999;
}
.app .chargeDetail .deviceCode {
  font-size: 0.59733333rem;
  color: #999;
  line-height: 2em;
}
.app .chargeDetail .h3 {
  font-size: 0.68266667rem;
  color: #000;
  line-height: 2em;
}
.app .chargeDetail ul li,
.app .chargeDetail ul p {
  font-size: 0.59733333rem;
  color: #666;
}
.app .chargeDetail .bottom {
  margin-top: 0.85333333rem;
  text-align: center;
}
.app .chargeDetail .bottom button {
  width: 90%;
  height: 1.70666667rem;
  border-radius: 1.70666667rem;
  background-color: #22B14C;
  color: #fff;
}
.tip {
	/* position: absolute; */
	left: 0.64rem;
	right: 0.64rem;
	bottom: 0.4267rem;
	background: rgba(0,0,0,.06);
	padding: 0.4267rem 0.4rem;
	border-radius: 0.256rem;
}
.tip .tipContent .left{
	width: 1.792rem;
	float: left;
	font-size: 0.7rem;
	color: #22B14C;
	line-height: 0.8533rem; 
}
.tip .tipContent .right{
	display: inline-block;
	width: calc(100% - 3rem);
	padding: 0 0.4267rem;
	font-size: 0.5973rem;
	color: #999;
	position: relative;
	height: 1.70667rem;
	display: flex;
	align-items: center;
}
.tip .tipContent .right:after {
	content:'充值';
	font-size: 0.6rem;
	display: block;
	
	/* height: 0; */
	/* border-top: 10px solid transparent;
	border-right: 10px solid #22B14C;
	border-bottom: 10px solid transparent;
	border-left: 10px solid transparent; */
	position: absolute;
	right: -1rem;
	font-weght: blod;
	color: #22B14C;
	padding: 0.1rem 0.3rem;
	border: 0.04267rem solid #22B14C;
	border-radius: 0.128rem;
	top: 50%;
	transform:  translateY(-50%);
	transform: -webkit-translateY(-50%);
	transform: -moz-translateY(-50%);
	transform: -o-translateY(-50%);
	transform: -ms-translateY(-50%);
	/* transform:  translateY(-50%) rotate(180deg);
	transform: -webkit-translateY(-50%) rotate(180deg);
	transform: -moz-translateY(-50%) rotate(180deg);
	transform: -o-translateY(-50%) rotate(180deg);
	transform: -ms-translateY(-50%) rotate(180deg); */
}

.app .title2 {
	line-height: 1.70666667rem;
    font-size: 0.68266667rem;
    color: #000;
}
.app .slideDiv .paySelect ul li:before {
	content: '';
    position: absolute;
    left: 0;
    right: -0.64rem;
    top: 0;
    height: 0.04266667rem;
    background-color: #eaeaea;
}
.app .slideDiv .paySelect ul li:after {
	content: '';
    position: absolute;
    left: 0;
    right: -0.64rem;
    top: 0;
    height: 0;
   
}
.app .slideDiv .paySelect ul li.hidden {
	display: none;
}

.app .toggle_switch .mui-switch{
	width: 37px;
	height: 20px;
	margin-top: 0.1278rem;
    margin-left: 0.2130rem;
}
.app .toggle_switch .mui-switch .mui-switch-handle {
	width: 18px;
    height: 18px;
}
/* v3设备新添加的css样式 */
 .app .top_title {
     display: flex;
     justify-content: center;
     font-size: 0.6086956521739131rem;
     color: #666;
     margin-bottom: 0.6826666666666666rem;
    }
     .app .top_title>div {
         border: 1px solid #22B14C;
         width: 50%;
         text-align: center;
         line-height: 1.12rem;
         padding: 0.21333333333333335rem 0;
          overflow: hidden;
         text-overflow: ellipsis;
         white-space: nowarp;
    }
     .app .top_title>div.active {
         background-color: #22B14C;
         color: #fff;
    }
     .app .top_title>div.top_d {
         border-radius: 0.2608695652173913rem 0 0 0.2608695652173913rem;
    }
     .app .top_title>div.top_v {
         border-radius: 0 0.2608695652173913rem 0.2608695652173913rem 0;
         border-left: none;
    }
    .app .top_title>div.top_v>span.tag-span {
    	font-size: 12px;
    }
    .select.chargeByMoney {
      display: none;
    }
    .select button.inp {
      padding : 0 !important;
    }
    .select button input {
      width: 100%;
      height:  100%;
      border: none;
      outline:  none;
      font-size:  12px;
      color:  #333;
      text-align: center; 
      margin: 0;
    }
    .select button.active input {
      background-color: #22B14C;
      color: #fff;
      transition: all 0.3s;

    }
.paySelect strong {
	font-weight: 400;
}
/* v3设备新添加的css样式结束 */
/* .app .slideDiv .paySelect ul li .walletMoneyDetail */
@media screen and (max-width : 270px){
	.app header .info .deviceName,
	.app .slideDiv .charge .top .text{
		display:none;
	}
}
/* 修改mui提示 */
.confirmItem {
     text-align: left;
     color: #666;
     font-size: 0.5217391304347826rem;
}
 .confirmItem span:first-child {
     color: #333;
}
 .confirmItem span:last-child {
     color: #777;
     float: right;
     max-width: 52%;
     overflow: hidden;
     text-overflow: ellipsis;
     white-space: nowrap;
}
 .confirmItem.title {
     color: #000;
     text-align: center;
}
 .confirmItem.tip {
     color: #222;
}
 .confirmItem i {
     font-size: 0.5652173913043478rem;
     color: #22B14C;
     margin-right: 0.21739130434782608rem;
}
 .border {
     margin-top: 0.43478260869565216rem;
     border: 1px solid #98E0AD;
     border-radius: 0.17391304347826086rem;
     padding: 0.2608695652173913rem 0.34782608695652173rem;
     background-color: #f5f7fa;
}
</style>
</head>
<body id="body"  data-hardversion="${hardversion}" data-payhint="${userinfo.payhint}" data-chargeInfo="${chargeInfo}">
	<div class="app">
		<input type="hidden" id="nowtime" value="${nowtime}">
		<input type="hidden" id="ifmonth" value="${ifmonth }">
		<input type="hidden" id="wolfstatus" value="${wolfstatus }">
		<form id="payfrom" action="${hdpath }/wxpay/pay" method="post">
			<input type="hidden" id="code" name="code" value="${code}">
			<input type="hidden" id="openid" name="openid" value="${openid}">
			<input type="hidden" id="portchoose" name="portchoose" value="${port }">
			<input type="hidden" id="chargeparam" name="chargeparam" value="${defaultchoose }">
			<input type="hidden" id="ifcontinue" name="ifcontinue" value="">
		</form>
		<header>
			<div class="info">
				<div class="deviceCode">
					<i class="iconfont icon-diannao-copy"></i>
					<span>设备号：${code}-<fmt:formatNumber value="${port}" pattern="00"/></span>
				</div>
				<c:if test="${equname != null && equname != ''}">
					<div class="deviceName">
						<i class="mui-icon mui-icon-home"></i>
						<span>${equname}</span>
					</div>
				</c:if>
			</div>
			<div class="detailLink">
				<i class="iconfont icon-tishishuoming"></i>
				收费说明 >
			</div>
			<div class="callTel">
				<i class="iconfont icon-tel-fill"></i>
				如有疑问，请联系：<a href="tel://${phonenum }">${phonenum }</a>
			</div>
			
		</header>
		<section>
		</section>
		<div class="slideDiv">
			<div class="charge animated slideInUp">
				<div class="top">
		          <span>请选择充电方式</span>
		          <span><span class="text">已选择充电插座：</span> <strong id="portNum">${port}</strong>号</span>
		        </div>
		          <!-- 选择充电方式 -->
		          <c:if test="${temporaryc == 1}">
		          <div class="top_title">
		            <div class="top_d active">按时间付费</div>
		            <div class="top_v">
		            	<span>按金额付费</span>
			            <span class="tag-span">（临时充电）</span>
		            </div>
		          </div>
		          </c:if>
		          <!-- 选择充电方式 -->
		        <div class="select chargeByTime clearfix">
		          <!-- <button class="active" data-val="auto">充满自停</button> -->
		          <c:forEach items="${temtime}" var="timeItem" varStatus="status">
		          	 <button 
		          	 	<c:if test="${status.index == 0}">class="active"</c:if> 
		          	 	data-val="${timeItem.name == '充满自停' ? 'auto' : timeItem.chargeTime}" 
		          	 	data-id="${timeItem.id}"
		          	 >${timeItem.name}</button>
		          </c:forEach>
		          <!-- <button class="inp" data-val="input">
		           <input type="number" class="mui-numbox-input" placeholder="其他时间">
		          </button> -->
		        </div>
				<c:if test="${temporaryc == 1}">
		        <div class="select chargeByMoney clearfix">
		          <c:forEach items="${temmoney}" var="moneyItem" varStatus="status">
		          	 <button <c:if test="${status.index == defaultindex}">class="active"</c:if>  data-val="${moneyItem.money}"  data-id="${moneyItem.id}">${moneyItem.name}</button>
		          </c:forEach>
		          <!-- <button class="inp" data-val="input">
		           <input type="number" class="mui-numbox-input" placeholder="其他金额">
		          </button> -->
		        </div>
		        </c:if>
		      </div>
			  <div class="paySelect animated slideInUp">
					<div class="title2">选择支付方式</div>
		              <ul class="ul1">
		                <li class="wallet active" data-pay-type="2">
		                  <i class="iconfont icon-qianbao"></i><strong class="pay_text">钱包支付</strong><span class="walletMoneyDetail">（赠：¥<span class="sendmoney">${sendmoney}</span> &nbsp;&nbsp; 充：¥<span class="walletMoney">${balance}</span>）</span>
		                  <i class="mui-icon mui-icon-checkmarkempty"></i>
		                </li>
						 <li class="firbid weixin" data-pay-type="1" >
							<i class="iconfont icon-weixin"></i><strong class="pay_text">微信支付</strong><i class="mui-icon mui-icon-checkmarkempty"></i>
						</li>
						<div class="tip">
							<div class="tipContent">
								<div class="left">温馨提示</div>
								<div class="right">
									钱包充值更方便
								</div>
							</div>
						</div>
		              </ul>
				</div>
			</div>
		<div class="chargeDetail animated">
			<div class="areaName">${areaname}</div>
			<div class="deviceCode">设备编号：${code}
				<c:if test="${userinfo.payhint == 1}">
					<div style="float: right;" class="toggle_switch">
						<span>下次不再提醒</span>
						<div class="<c:if test="${userinfo.payhint != 1}">mui-active </c:if>mui-switch mui-switch-mini" style="float: right;">
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
				</c:if>
			</div>
			<div class="h3">收费说明</div>
			<ul>
				<c:if test="${chargeInfo == null || chargeInfo == ''}">
					<c:forEach items="${tempower}" var="item">
						<li>${item.money}元/小时， 功率区间：${item.common1}-${item.common2}瓦</li>
					</c:forEach>
				</c:if>
			</ul>
			<div class="bottom">
				<button class="mui-btn cdBtn">我知道了</button>
			</div>
		</div>
		<footer>
			<div class="payMoney">
				<span class="defaultSpan">支付金额：¥<span id="payMoneySpan">0.00</span></span>
				<span class="monthlySpan" style="display: none;">包月支付</span></span>
			</div>
			<div class="startCharge">开始充电</div>
		</footer>
	</div>
	<c:if test="${chargeInfo != null}">
		<script>
			var chargeInfo= $('body').attr('data-chargeInfo').trim()
			var infoList= chargeInfo.split(/\n/g)
			var $ul= $('.chargeDetail ul')
			$(infoList).each(function(i,item){
				$li= $('<li>'+item+'</li>') 
				$ul.append($li)
			})
		</script>	
	</c:if>
	<script>
		$(function(){
			var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
		    var fontSize= htmlwidth/16
		    var style= document.createElement('style')
		    style.setAttribute('class','fontStyle')
		    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
		    var head= document.getElementsByTagName('head')[0]
		    head.insertBefore(style,head.children[0])
		    if(htmlwidth < 270){
		    	$('.weixin .pay_text').text('微信')
		    	$('.wallet .pay_text').text('钱包')
		    }else{
		    	$('.weixin .pay_text').text('微信支付')
		    	$('.wallet .pay_text').text('钱包支付')
		    }
		    var resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize';
		    window.addEventListener(resizeEvt, function() { 
		    	var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
			    var fontSize= htmlwidth/16
			    if(htmlwidth < 270){
			    	$('.weixin .pay_text').text('微信')
			    	$('.wallet .pay_text').text('钱包')
			    }else{
			    	$('.weixin .pay_text').text('微信支付')
			    	$('.wallet .pay_text').text('钱包支付')
			    }
		    	$('.fontStyle').text('html { font-size: '+fontSize+'px !important;}')
		    }, false);
	
		    var isCanClickPay= true //是否能点击开始充电
		    var payTypeNum= 1 // 1微信 、2钱包 、 3包月
		    /* payTypeNum= $('#paySpan i').hasClass('icon-weixin') ? 1 :  $('#paySpan i').hasClass('icon-qianbao') ?  2 :  undefined */
		  	payTypeNum= parseInt($('.paySelect ul li.active').attr('data-pay-type'))
		  	
		    
		    /* 判断是否是返回的页面，如果是则执行 */
		    window.onpageshow = function(event) {
				if (event.persisted || window.performance && window.performance.navigation.type == 2){
					var remarkObj= JSON.parse(sessionStorage.getItem('remark')) || {}
					/* var remark= parseFloat(remarkStr) */
					var sessBalance= remarkObj.balance || 0
					var sessSendmoney= remarkObj.sendmoney || 0
					var sessionWalletMoney2= sessionStorage.getItem('sessionWalletMoney2') || $('.walletMoney').eq(0).text()
					var sessionSendmoney2= sessionStorage.getItem('sessionSendmoney2') || $('.sendmoney').eq(0).text()
					var sWalletMonsy= parseFloat(sessionWalletMoney2) 
					var sSendmoney= parseFloat(sessionSendmoney2) 
					$('.walletMoney').text(sWalletMonsy+parseFloat(sessBalance))
					$('.sendmoney').text(sSendmoney+parseFloat(sessSendmoney))
					
					sessionStorage.setItem('sessionWalletMoney2',$('.walletMoney').eq(0).text())
					sessionStorage.setItem('sessionSendmoney2',$('.sendmoney').eq(0).text())
					sessionStorage.removeItem('remark')
				}
			};
			
		    
			var mask = mui.createMask(function(){
				toggleslideDiv(false)
				toggleDetail(false)
			})
			/*选择充值方式开始*/
			$('.payType .otherPay').click(function(){
				$('.paySelect').fadeIn(0)
			})
			$('.back').click(function(){
				$('.paySelect').fadeOut(0)
			})

	/*点击收费详情/按功率计费*/
	var he= $('.chargeDetail').outerHeight()
	$('.chargeDetail').css('bottom',-he)
	
	/* 当 payhint == 1的时候显示 */
	var payhint= $('body').attr('data-payhint').trim()
	if(payhint == 1){
		toggleDetail(true)
	}
	
	$('.detailLink').click(function(){

		toggleDetail(true)
	})
	$('.cdBtn').click(function(){
		toggleDetail(false)
		mask.close()
		if($('.toggle_switch').length > 0){
			var isActive= $('.toggle_switch .mui-switch').hasClass('mui-active')
			var payhint= isActive ? 2 : 1
			console.log(payhint)
			$.ajax({
				url: '/general/editAccountAloneData',
				type: 'post',
				data: {
					payhint: payhint,
					uid: ${userinfo.id},
					sourcepath: 1
				},
				sucess: function(res){
					console.log(res)
				}
			})
		}
	})
			

			function toggleDetail(isBlock){
				if(isBlock){
					$('.chargeDetail').css('display','block')
					$('.chargeDetail').animate({
						bottom: 0
					},400,function(){
						
					})
					$('.slideDiv').fadeOut(0)
				}else{
					$('.chargeDetail').animate({
						bottom: -he
					},400,function(){
						$('.chargeDetail').css('display','none')
						$('.slideDiv').fadeIn(0)
					})
				}
				
			}
			/*点击收费详情/按功率计费结束*/
			function toggleslideDiv(isBlock){
				if(isBlock){
					$('.slideDiv').fadeIn(0)
					mask.show()
				}else{
					$('.slideDiv').fadeOut(0)
				}
			}
			/* 点击充值跳转到钱包充值界面 */
			var openid2= $("#openid").val()
			$('.tipContent .right').click(function(){
				var code= $('#code').val().trim()
				$.ajax({
					url: '/general/messageverify',
					data: { code:code ,openid: openid2 },
					type: 'post',
					success: function(data){
						if(data.code == 200){
							window.location.href= '/general/walletcharge?openid='+openid2+'&from=1'
						}else{
							var userid= data.touid || '— —'
							var devdealnick= data.devdealnick || '— —'
							var devtelphone= data.devtelphone || '— —'
							var toudealnick= data.toudealnick || '— —'
							var toutelphone= data.toutelphone || '— —'
							var vittname= data.devareaname || '— —'
							var mervittname= data.touareaname || '— —'
							var confirmStr= '<div><div class="confirmItem tip">你（会员号：'+userid+'）暂不支持在当前页面进行钱包充值！原因如下：你所属的信息与设备所属的信息不一致</div><div class="border"><div class="confirmItem title">设备所属商户信息</div><div class="confirmItem"><span><i class="iconfont icon-tubiao211"></i>设备属于：</span><span>'+devdealnick+'</span></div><div class="confirmItem"><span><i class="iconfont icon-dianhua"></i>商户电话：</span><span>'+devtelphone+'</span></div><div class="confirmItem"><span><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span><span>('+data.devaid+')'+vittname+'</span></div></div><div class="border"><div class="confirmItem title">你所在的商户信息</div><div class="confirmItem"><span><i class="iconfont icon-tubiao211"></i>商户姓名：</span><span>'+toudealnick+'</span></div><div class="confirmItem"><span><i class="iconfont icon-dianhua"></i>商户电话：</span><span>'+toutelphone+'</span></div><div class="confirmItem"><span><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span><span>('+data.touareaid+')'+mervittname+'</span></div></div></div>';
						    mui.alert(confirmStr,'提示','我知道了')
						}
					},
					error: function(err){
						mui.toast('请求出错')
					}
				})
			})
			
			/* v3模板js */
			var initState= {  
	        chargeType: 1, //1按时间充电，2按金额充电
	        selectItem: '' //选的金额，或者时间
	      }
	      // 渲染的时候，获取默认支付方式，和充值时间
	       initState.chargeType= $('.top_title>div').length > 0 ?  ($('.top_title .top_d').hasClass('active') ? 1 : 2) : 1
	       var itemVal=''
	       var chargeparamVal= ''
	       if( initState.chargeType === 1){
	          itemVal= $('.chargeByTime button.active').attr('data-val').trim()
	          chargeparamVal= $('.chargeByTime button.active').attr('data-id').trim()
	       }else{
	          itemVal= $('.chargeByMoney button.active').attr('data-val').trim()
	          chargeparamVal= $('.chargeByMoney button.active').attr('data-id').trim()
	       }
	        $('#chargeparam').val(chargeparamVal)
	       initState.selectItem= itemVal
	       render()
// v3版本选择充电方式
      $('.top_title>div').on('tap',function(){
        initState.chargeType= $(this).hasClass('top_d') ? 1 : 2
        $(this).addClass('active')
        $(this).siblings().removeClass('active')
        // 切换充电方式
        if(initState.chargeType == 1){
          $('.chargeByMoney').hide()
          $('.chargeByTime').show()
          $('.paySelect li.weixin').addClass('firbid').removeClass('active')
          $('.paySelect li.wallet').addClass('active')
          payTypeNum= 2
          // 选择的时候就把选择子项的值给存起来
          initState.selectItem= $('.chargeByTime button.active').attr('data-val').trim()
          $('#chargeparam').val($('.chargeByTime button.active').attr('data-id').trim())
        }else{
          $('.chargeByTime').hide()
          $('.chargeByMoney').show()
          $('.paySelect li.weixin').removeClass('firbid')
          // 选择的时候就把选择子项的值给存起来
          initState.selectItem= $('.chargeByMoney button.active').attr('data-val').trim()
          $('#chargeparam').val($('.chargeByMoney button.active').attr('data-id').trim())
        }
        render()
      })

			
	/*实现选择金额*/
	$('.slideDiv .select button').click(function(){
          $(this).addClass('active')
          $(this).siblings().removeClass('active')
		  var val= $(this).attr('data-val').trim()
		  var chargeparamVal= $(this).attr('data-id').trim()
          if(val === 'input'){
            val= parseFloat($(this).find('input').val())
            val= isNaN(val) ? 'input' : val 
          }
          initState.selectItem= val
          $('#chargeparam').val(chargeparamVal)
          render()
			})
			/*选择金额结束*/

      // 监听.select button中的输入框
      $('.select button input').on('input propertychange',function(){
         var val= parseFloat($(this).val()) //这里过滤掉除了数字之外的其他数字
         val= isNaN(val) ? '' : val 
         initState.selectItem= val
         render()
      })

      // 渲染左下角选择金额
      function render(){
        var titleStr= initState.chargeType === 1 ? '充电时长' : '支付金额'
        var titleSpanStr= ''
        if(initState.selectItem === 'auto'){
            titleSpanStr= '充满自停'
        }else if(initState.selectItem === 'input'){
            titleSpanStr= initState.chargeType === 1 ? (''+'小时') : ('¥'+'')
        }else{
          titleSpanStr= initState.chargeType === 1 ? ((parseFloat(initState.selectItem)/60).toFixed(1)+'小时') : ('¥'+initState.selectItem)
        }
        $('.payMoney').html(titleStr+'：<span>'+titleSpanStr+'</span>')
      }

     $('.startCharge').on('click',function(){ //点击开始充电
          if(initState.selectItem === 'input' || initState.selectItem=== ''){
            var toastText= initState.chargeType === 1 ? '请输入充电时间！' : initState.chargeType === 2 ? '请输入充电金额!' : ''
             return mui.toast(toastText)
          }
           console.log(initState)
          if(isCanClickPay){
			isCanClickPay= false //点击之后不能再点
			if(payTypeNum ===1){//微信支付
				wechartPay()
			}else if(payTypeNum === 2){ //钱包支付
				walletpay()
			}else if(payTypeNum === 3){ //包月支付
				monthlyFn()
			}
		}
      })
      
/* va3模板预览js结束 */
/*选择支付方式开始*/
$('.paySelect ul li').click(function(){
	if($(this).hasClass('firbid')){
		return
	}
	$(this).siblings().removeClass('active')
	$(this).addClass('active')
	var payType= parseInt($(this).attr('data-pay-type'))
	var payStr=''
	if(payType==1){
		payTypeNum= 1
		payStr= '<i class="iconfont icon-weixin"></i>微信支付'
	}else{
		payTypeNum= 2
		var watellMon= $('.walletMoney').eq(0).text()
		payStr= '<i class="iconfont icon-qianbao"></i>钱包支付 （¥<span class="walletMoney">'+watellMon+'</span>）';
	}
	$('#paySpan').html(payStr)
})
/*选择支付方式结束*/

			
			function handlePay(){ 
				var hardversion= $('body').attr('data-hardversion').trim()
				if(hardversion != '01'){
					return false
				}
				var forbidport= [].concat(${forbidport});
				var portNum=$('.section .mid ul li.active').index()+1
				return forbidport.some(function(item,i){
					return parseInt(item) == portNum ;
				})
			};
			
			function walletpay() { //钱包支付方法
				/* if(handlePay()){
					mui.alert('此端口已被指定', '', function() {});
					return
				} */
				var code = $("#code").val(); //获取设备号
				var openid = $("#openid").val(); //获取openid
				var portchoose = $("#portchoose").val(); //获取端口号
				var chargeparam = $("#chargeparam").val(); //获取充电模板id
				if (portchoose == null || portchoose == "") {
					mui.alert('未选择充电端口', '', function() {
						isCanClickPay= true
					});
					return false;
				} else if (chargeparam == null || chargeparam == "") {
					mui.alert('未选择充电时间', '', function() {
						isCanClickPay= true
					});
					return false;
				} else if (code == null || code == "") {
					mui.alert('退回操作，需重新扫码', '', function() {
						isCanClickPay= true
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
								if (data.code == 1) {
									/*支付成功、跳转到广告页面*/
									window.location.replace('/general/walletPayment?orderNum='+data.orderNum+'&payMoney='+data.payMoney)
									/* var wolfstatus = $("#wolfstatus").val();
									if (wolfstatus == 2) {
										mui.alert('支付成功',function() {
											window.location.replace("${hdpath}/general/payaccess");
										});
									} else {
										mui.alert('支付成功',function() {
											WeixinJSBridge.call('closeWindow');
										});
									} */
								} else if (data.code == 2) {
									isCanClickPay= true //请求成功之后能点击
									mui.alert("余额不足");
								} else if (data.code == 3) {
									isCanClickPay= true //请求成功之后能点击
									mui.alert("钱包暂时不可用");
								} else if (data.code == 4) {
									isCanClickPay= true //请求成功之后能点击
									/* mui.alert("您的钱包不可支付当前设备，请使用微信支付"); */
									var userid= data.touid || '— —'
									var devdealnick= data.devdealnick || '— —'
									var devtelphone= data.devtelphone || '— —'
									var toudealnick= data.toudealnick || '— —'
									var toutelphone= data.toutelphone || '— —'
									var vittname= data.devareaname || '— —'
									var mervittname= data.touareaname || '— —'
									var confirmStr= '<div><div class="confirmItem tip">你（会员号：'+userid+'）暂不支持在当前设备使用钱包支付！原因如下：你所属的信息与设备所属的信息不一致</div><div class="border"><div class="confirmItem title">设备所属商户信息</div><div class="confirmItem"><span><i class="iconfont icon-tubiao211"></i>设备属于：</span><span>'+devdealnick+'</span></div><div class="confirmItem"><span><i class="iconfont icon-dianhua"></i>商户电话：</span><span>'+devtelphone+'</span></div><div class="confirmItem"><span><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span><span>('+data.devaid+')'+vittname+'</span></div></div><div class="border"><div class="confirmItem title">你所在的商户信息</div><div class="confirmItem"><span><i class="iconfont icon-tubiao211"></i>商户姓名：</span><span>'+toudealnick+'</span></div><div class="confirmItem"><span><i class="iconfont icon-dianhua"></i>商户电话：</span><span>'+toutelphone+'</span></div><div class="confirmItem"><span><i class="iconfont icon-xiaoquguanli"></i>所属小区：</span><span>('+data.touareaid+')'+mervittname+'</span></div></div></div>';
								    mui.alert(confirmStr,'提示','我知道了')
								} else {
									isCanClickPay= true //请求成功之后能点击
									mui.alert("充电异常");
								}
								$("#walletbtn").removeAttr("disabled");
							}
						});
				}
			}
			/* 微信支付开始 */
			function wechartPay(){
				/* if(handlePay()){
					mui.alert('此端口已被指定', '', function() {});
					return
				} */
				var code = $("#code").val();
				var openid = $("#openid").val();
				var portchoose = $("#portchoose").val();
				var chargeparam = $("#chargeparam").val();
				if (portchoose == null || portchoose == "") {
					mui.alert('未选择充电端口', '', function() {
						isCanClickPay= true
					});
					return false;
				} else if (chargeparam == null || chargeparam == "") {
					mui.alert('未选择充电时间', '', function() {
						isCanClickPay= true
					});
					return false;
				}  else if (code == null || code == "") {
					mui.alert('退回操作，需重新扫码', '', function() {
						isCanClickPay= true
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
				
			}
			/* 微信支付结束 */
			/* 包月支付开始 */
			function monthlyFn() {
				/* if(handlePay()){
					mui.alert('此端口已被指定', '', function() {});
					return
				} */
				var ifmonth = $("#ifmonth").val();
				var code = $("#code").val();
				var openid = $("#openid").val();
				var portchoose = $("#portchoose").val();
				if (portchoose == null || portchoose == "") {
					mui.alert('未选择充电端口',function(){
						isCanClickPay= true
					});
					return false;
				}else if (ifmonth == 0) {
					mui.alert('本商户暂时停止使用包月功能，如有疑问，请联系商家',function(){
						isCanClickPay= true
					});
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
								sCanClickPay= true //完成请求数据之后开启点击
								mui.alert("您未开通包月，请前往公众号<自助充电平台>个人中心开通此功能");
							}else if (data.wolfval == 3) {
								isCanClickPay= true //完成请求数据之后开启点击
								mui.alert("开通包月已过期");
							}else if (data.wolfval == 0) {
								sCanClickPay= true //完成请求数据之后开启点击
								mui.alert("用户未注册，请前往公众号<自助充电平台>个人中心");
							} else if (data.wolfval == 4) {
								sCanClickPay= true //完成请求数据之后开启点击
								mui.alert("今日次数已用完");
							} else if (data.wolfval == 5) {
								sCanClickPay= true //完成请求数据之后开启点击
								mui.alert("总次数已用完");
							} else if (data.wolfval == 6) {
								sCanClickPay= true //完成请求数据之后开启点击
								mui.alert("开通包月没有此设备");
							}
						},
				        complete: function () {
				            $.bootstrapLoading.end();
				        }
					})
				}
			}
			/* 包月支付结束 */
			
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
						var wolfstatus = $("#wolfstatus").val();
						if (wolfstatus == 2) {
							mui.alert('支付成功',function() {
								window.location.replace("${hdpath}/general/payaccess");
							});
						} else if (wolfstatus == 1) {
							if (attention == 1) {
								window.location.href = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU4MDY5OTg5MQ==&scene=126&subscene=0#wechat_redirect";
							} else {
								WeixinJSBridge.call('closeWindow');
							}
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
			
			
			/* 每隔一段事件，请求数据，看端口状态 */
			 var flag = ${flag};
			var portStatus= ${portStatus}
			if (flag) {
				setTimeout(paystate1, 10); //0.01秒后执行
			}else {
				if(portStatus == 2){
					mui.alert('此端口已被占用，请更换其他端口', '提示', function() {
						WeixinJSBridge.call('closeWindow');
					});
				}else if(portStatus == 3 || portStatus == 4){
					mui.alert('此端口为故障端口，请更换其他端口', '提示', function() {
						WeixinJSBridge.call('closeWindow');
					});
				}
			}  
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
			
			/* 看端口状态结束 */
		})
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>收益统计信息</title>
<link rel="stylesheet" href="/mui/css/mui.min.css">
<link rel="stylesheet" href="/css/mobiscroll.custom-3.0.0-beta2.min.css">
<link rel="stylesheet" href="/hdfile/css//icon.css">
<link rel="stylesheet" href="/hdfile/css/time_op.css">
<link rel="stylesheet" href="/css/base.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="/js/jquery.js"></script>
<script src="/js/mobiscroll.custom-3.0.0-beta2.min.js"></script>
<script src="/hdfile/js/time.js"></script>
<script src="/mui/js/mui.min.js"></script>
	<style>
	
	@font-face {
	  font-family: "mathNum";
	  src: url("/hdfile/font/fontsize/DINMITTELSCHRIFT.woff2") format("woff2"),
	       url("/hdfile/font/fontsize/DINMITTELSCHRIFT.woff") format("woff"),
	       url("/hdfile/font/fontsize/DINMITTELSCHRIFT.ttf") format("truetype"),
	       url("/hdfile/font/fontsize/DINMITTELSCHRIFT.eot") format("embedded-opentype"),
	       url("/hdfile/font/fontsize/DINMITTELSCHRIFT.svg") format("svg");
	}
	.app {
		color: #666;
		font-size: 14px;
		width: 100%;
		height: 100%;
		position: relative;
	}
	header {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		z-index: 3;
		padding: 15px;
		background-color: #fff;
	}
	 header .se {
		position: absolute;
		width: 100vw;
		right: -100vw;
		padding: 10px 15px;
		top: 0;
		z-index: 3;
		background-color: #fff;
	}
	 header .se input[type=search]{
		width: 85%;
		border-radius: 1.45067rem;
		background-color: #efeff4;
		padding-left: 30px;
		margin: 0;
		height: 34px;
	}
	 header .se input[type=search]::-webkit-input-placeholder {
		color: #c8c7cc;
		font-size: 14px;
	}
	 header .se input[type=search]::-moz-placeholder{
		color: #c8c7cc;
		font-size: 14px;
	}
	 header .se input[type=search]:-ms-input-placeholder{
		color: #c8c7cc;
		font-size: 14px;
	}
	 header .se i.icon-sousuo {
		position: absolute;
		left: 22px;
		top: 16px;
		color: #c8c7cc;
		font-size: 17px;
	}
	 header .se .closeSpan {
		width: 13%;
		text-align: center;
		color: #2B73D5;
		display: inline-block;
	}


	header .top ul >li {
		float: left;
		margin-right: 30px;
		color: #999;
		position: relative;
	}
	header .top ul >li.active {
		font-weight: 700;
		font-size: 16px;
		color: #000;
	}
	header .top ul >li.active:after {
		content: '';
		position: absolute;
		width: 100%;
		left: 0;
		bottom: 0;
		height: 4px;
		background-color: #22B14C;
	}
	header .top >div {
		float: right;
		height: 22px;
	}
	header .top >div li:nth-child(1) {
		float: left;
		margin-right: 50px;
	}
	header .top >div li:nth-child(2) {
		float: right;
	}
	header .mid {
		margin-top: 20px;
	}
	header .mid div:nth-child(1){
		float: left;
	}
	header .mid div:nth-child(3){
		overflow: hidden;
		text-align: center;
		color: #333;
		font-weight: 700;
		font-size: 16px;
	}
	header .mid div:nth-child(2){
		float: right;
	}
	main {
		position: fixed;
		left: 0;
		top: 93px;
		right: 0;
		bottom: 0;
		padding: 15px 0 50px;
		overflow: auto;
	}
	main ul li {
		padding: 15px;
		background-color: #f5f7fa;
		margin-bottom: 10px;
		display: flex;
    	align-items: center;
	}
	main ul li>div {
		float: left;
	}
	main ul li .left {
		width: 45px;
		height: 45px;
		text-align: center;
		line-height: 45px;
	}
	main ul li .left  i {
		font-size: 45px;
		color: #22B14C;
	}
	main ul li .right {
		width: calc(100% - 45px);
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	main ul li .right strong {
		color: #000;
		font-size: 16px;
	}
	main ul li .right .moneySpan {
		font-size: 16px;
		color: #22B14C;
		font-family: mathNum;
	}
	main ul li .right .orderInfo{
		font-size: 12px;
		padding-left: 15px;
		width: 100%;
	}
	main ul li .right .orderInfo .iconContent {
		overflow: hidden;
		width: 100%;
	}
	main ul li .right .orderInfo .iconContent .timeSpan {
		float: left;
		font-size: 14px;
		color: #000;
		font-weight: 700;
		width: 50%;
		overflow: hidden;
		white-space:nowrap;
		text-overflow: ellipsis;	
	}
	main ul li .right .orderInfo .iconContent .m_span {
		float: right;
	}

	main ul li .right .orderInfo .monSpan{
		font-size: 12px;
		line-height: 2.5em;
		overflow: hidden;
	}
	main ul li .right .orderInfo .monSpan >div {
	 	width: 50%;
	 	float: left;
	 	height: 20px;
	 	line-height: 20px;
	 	white-space: nowrap
	 	
	}
	main ul li .right .orderInfo .monSpan>div span:nth-child(odd) {
		color: #999;
	}
	main ul li .right .orderInfo .monSpan>div span:nth-child(even){
		width: 15%;
		color: #333;
	}
	main ul li .right .moneyInfo {
		overflow: hidden;
	}
	
	/*这是时间收益部分*/
	.middle{margin-top: 10px; width: 100%;}
	.middle > p{font-size:15px;color:#333;text-indent: 15px;font-weight: bold}
	.middle > h2{color: #22B14C;font-size: 30px;text-indent: 15px;font-family:mathNum;}
	.mlist{overflow:hidden;padding:5px 0 10px; width: 100%;}
	.mlist p{ width: 50%; min-width: 107px;float: left;font-size: 12px;color: #999;margin-top: 0.13rem;text-indent: 13px;}
	.mlist p .icon_tip {position: absolute; right: -25px; top: 50%; margin-top: -9px; font-size: 16px; color: #999; width: 18px;}
	.icon_tip_con {width: 80vw; padding: 10px; color: #333; font-size: 14px; background-color: #fff;position:fixed; left: 50%;top: 38%;
		border-radius: 5px;
		line-height: 2em;
		transform:  translate(-50%,-50%);
		-o-transform:  translate(-50%,-50%);
		-mz-transform:  translate(-50%,-50%);
		-webkit-transform:  translate(-50%,-50%);
		z-index: 999;
		display: none;
	}
	.mlist p span{position:relative;font-size: 13px;color: #333;font-family:mathNum;margin-left: 1.5px;}
 	.bottom{padding-bottom: 6px;}
	.bottom > p{line-height: 35px;color: #999;font-size: 12px;text-indent: 15px}
	
	/*这是时间收益部分*/
	.alertDetail {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		top: 0;
		z-index: 999;
		background-color: rgba(0,0,0,.4);
	}
	.slunbo {
		padding: 15px 15px 20px;
		border-radius: 10px;
		background-color: #fff;
		width: 85%;
		position: absolute;
		top: 40%;
		left: 50%;
		transform: translate(-50%,-50%);
		-ms-transform: translate(-50%,-50%);
		-moz-transform: translate(-50%,-50%);
		-webkit-transform: translate(-50%,-50%);
		-o-transform: translate(-50%,-50%);
	}
	.slunbo .stop {
		height: 40px;
		line-height: 40px;
		border-bottom: 1px solid #f7f7f7;
		color: #000;
		font-weight: 700;
		font-size: 16px;
	}
	.slunbo .stop .stleft {
		float: left;
	}
	.slunbo .stop .stright {
		float: right;
		height: 40px;
		line-height: 40px;
	}
	.slunbo .stop .stright span {
		font-size: 12px;
		color: #006bff;
		display: inline-block;
		height: 20px;
		line-height: 20px;
		border-radius: 20px;
		padding: 0 10px;
		background-color: #e5f0ff;
	}
	.slunbo .scontent>p {
	    font-size: 12px;
	    font-weight: bold;
	    color: #444;
	    margin-top: 12px;
	
	}
	.slunbo .scontent .s_total_pay {
		font-size: 18px;
		color: #22B14C;
	}
	.sbox .sboxlist {
		width: 48%;
	    height: 55px;
	    border-radius: 5px;
	    float: left;
	    background: #f7f7f7;
	    margin-top: 7.5px;
	}
	.sboxlist:nth-of-type(even) {
    	margin-left: 4%;
	}
	.sboxlist p:nth-of-type(1) {
	    font-size: 12px;
	    color: #999;
	    font-weight: bold;
	    margin-top: 5px;
	}
	.sboxlist p:nth-of-type(2) {
	    font-size: 18px;
	    color: #333;
	    font-family: mathNum;
	    font-weight: 700;
	}
	.slunbo .cancelswiper {
		width: 100%;
		position: absolute;
		bottom: -45px;
		left: 0;
		text-align: center;
	}
	.slunbo .cancelswiper i {
		font-size: 35px;
		color: #fff;
	}

	/*点击查看收益弹框结束*/
	nav.mui-bar {
		box-shadow: none;
	}
	nav li {
		display: inline-block;
		width: 33.333%;
		line-height: 50px;
		height: 50px;
		text-align: center;
	}
	nav.mui-bar li i {
		font-size: 22px;
		color: #999;
	}
	nav.mui-bar li:nth-child(2) i{
		font-size: 35px;
		color: #22B14C;
	}
	.sArea {
		width: 100%;
		background-color: #fff;
		position: fixed;
		z-index: 999;
		left: 0;
		right: 0;
		bottom: 0;
		display: none;
	}
	.sArea ul{
		padding: 15px;
	}
	.sArea li {
		float: left;
		width: 33.33%;
		box-sizing: border-box;
		height: 80px;
		text-align: center;
		padding-top: 10px;
	}
	.sArea li i {
		font-size: 30px;
		line-height: 30px;
		color: #22B14C;
	}
	
	.sArea li p {
		margin-top: 5px;
		color: #333;
		font-weight: 700;
	}
	.sArea>div {
		border-top: 1px solid #f7f7f7;
		margin-top: 10px;
		text-align: center;
		height: 50px;
		line-height: 50px;
	}
	.sArea>div i {
		font-size: 25px;
		color: #999;
	}
	
	.noDataTip {
			width: 100%;
			height: 30vh;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
	 .noDataTip >div {
			text-align: center;
			font-size: 0.8533rem;
			font-weight: 700;
			color: #333;
			margin-bottom: 0.8533rem;
		}
	 .noDataTip >div i {
			font-size: 2.1333rem;
			line-height: 2.1333rem;
			color: #51BF73;
		}
	.bottomList .title {
		margin-bottom: 8px;
	}
	.bottomList>li {
		/*height: 53px;*/
		border-bottom: 1px solid #f7f7f7;
	}
	.bottomList>li .iconContent {
		width: calc(100% - 50px);
	}
	.bottomList>li .iconDiv {
		margin-right: 20px;
	}

	.bottomList>li .iconDiv i {
		font-size: 30px;
	}
	.bottomList>li .timeSpan {
		font-size: 14px;
		color: #000;
		font-weight: 700;
	}
	.bottomList>li .m_span {
		float: right;
		font-size: 12px;
	}
	.bottomList>li .m_span .moneySpan {
		font-size: 16px;
		color: #22B14C;
		font-family: mathNum;
	}
	.bottomList .mlist {
		padding-bottom: 0;
	}


	.mbsc-material .mbsc-range-btn-sel .mbsc-range-btn {
		color: #22B14C;
	}
	.mbsc-material .mbsc-range-btn-sel .mbsc-range-btn {
		border-color: #22B14C;
	}
	.mbsc-ic-material-keyboard-arrow-right:before, .mbsc-ic-material-keyboard-arrow-left:before, .mbsc-material .mbsc-cal-hl-now .mbsc-cal-today {
		color: #22B14C;
		font-weight: 700;
	}
	.mbsc-material.mbsc-range .mbsc-cal-table .mbsc-cal-day-hl .mbsc-cal-day-i .mbsc-cal-day-fg {
		background: #22B14C;
	}
	.mbsc-material.mbsc-range .mbsc-cal-day-sel .mbsc-cal-day-frame {
		background: #22b14c24;
	}
	.mbsc-material .mbsc-fr-btn {
		color: #22B14C;
	}
	.mbsc-fr-btn0 {
		background-color: #22B14C;
	}
	.mbsc-material .mbsc-cal-sc .mbsc-cal-sc-sel .mbsc-cal-sc-cell-i {
		background-color: #22B14C !important;
	}

	.loading {
		width: 100px;
		height: 115px;
		border-radius: 8px;	
		border: 1px solid #ccc;
		background-color: rgba(0,0,0,.6);
		color: #fff;
		position: fixed;
		z-index: 999;
		left: 50%;
		top: 35%;
		transform: translate(-50%,-50%);
		-ms-transform: translate(-50%,-50%);
		-moz-transform: translate(-50%,-50%);
		-webkit-transform: translate(-50%,-50%);
		-o-transform: translate(-50%,-50%);
		display: none;
		
	}
	.loading img {
		width: 50%;
		height: auto;
		position: absolute;
		left: 50%;
		top: 40%;
		transform: translate(-50%,-50%);
		-ms-transform: translate(-50%,-50%);
		-moz-transform: translate(-50%,-50%);
		-webkit-transform: translate(-50%,-50%);
		-o-transform: translate(-50%,-50%); 
		animation: Load 1.5s linear infinite;
	}
	.loading p {
		margin-top: 80%;
		text-align: center;
		color: #fff;
	}
	@keyframes Load {
		from {
			transform: translate(-50%,-50%) rotate(0deg);
		}
		to {
			transform: translate(-50%,-50%) rotate(360deg);
		}
	}
	@media screen and (max-width: 340px){
		header .mid div:nth-child(3) {
			font-size: 14px;
		}
	}
	
	.mui-backdrop {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 998;
    background-color: rgba(0,0,0,.3);
}

	</style>
</head>
<body data-showincoins="${showincoins}">
	<div class="app">
		<header>
			<div class="se">
				 <input type="search" class="mui-input-clear" placeholder="请输入设备号" id="topSearch">
				 <i class="iconfont icon-sousuo"></i>
				<span class="closeSpan">取消</span>
			</div>
			<div class="top clearfix">
				<ul class="select_d">
					<li data-select_d="1">今天</li><!-- <li class="active" data-select_d="1">今天</li> -->
					<li data-select_d="2">本周</li>
					<li data-select_d="3">本月</li>
				</ul>
				<div>
					<li><!-- <i>i</i> --></li>
					<li class="searchLi" style="display:none;"><i class="iconfont icon-sousuo"></i></li>
				</div>
			</div>
			<div class="mid">
				<div class="upDiv">
					<i class="iconfont icon-left-s"></i>
					<span>前一天</span>
				</div>
				<div class="upDiv"><span>后一天</span>
					 <i class="iconfont icon-right-s"></i>
				</div>
				<div class="inpDiv">
					2019/08/14-2019/08/14
				</div>
			</div>
		</header>
		<main>
			<ul>
			<c:forEach items="${resultotal}" var="totalMoney">
			<li class="clearfix">
				<div class="middle">
					<p>营收总额(元)</p> 
					<h2 id="h_total_pay">
						<c:if test="${showincoins != 2}">
							<fmt:formatNumber value="${totalMoney.totalmoney}" pattern="0.00" />
						</c:if>
						<c:if test="${showincoins == 2}">
							<fmt:formatNumber value="${totalMoney.totalonearn}" pattern="0.00" />
						</c:if>
					</h2>
					<div class="mlist">
						<p data-hideby="lotteryTicket">线上收益：<span id="p_ad_pay"><fmt:formatNumber value="${totalMoney.totalonearn}" pattern="0.00" /></span></p>
						<c:if test="${showincoins != 2}">
							<p data-hideby="lotteryTicket">投币收益：<span id="p_coin_pay"><fmt:formatNumber value="${totalMoney.totalcoinsearn}" pattern="0" /></span></p>
							<p data-hideby="lotteryTicket">刷卡收益：<span id="p_coin_pay"><fmt:formatNumber value="${totalMoney.totaloncardmoney}" pattern="0.00" /></span></p>
						</c:if>
						<p data-hideby="lotteryTicket">耗电量：<span>
							<c:if test="${totalMoney.totalConsume == null}">
								<fmt:formatNumber value="0" pattern="0.00" />
							</c:if>
							<c:if test="${totalMoney.totalConsume != null}">
								<fmt:formatNumber value="${totalMoney.totalConsume}" pattern="0.00" />
							</c:if>
							<!-- <i class="iconfont icon-sousuo icon_tip"></i> -->
							<img class="icon_tip"  src="data:image/png;base64,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"></img>
						</span></p>
						<c:if test="${totalMoney.paymentmoney != 0}">
							<p data-hideby="lotteryTicket">缴费金额：<span><fmt:formatNumber value="${totalMoney.paymentmoney}" pattern="0.00" /></span></p>
						</c:if>
					</div>
				</div>
			</li>
			</c:forEach>	
			<c:if test="${fn:length(result) <= 0}">
				<div class="noDataTip">
					<div><i class="iconfont icon-wodedingdan"></i></div>
					<div>暂无记录</div>
				</div>
			</c:if>
			<c:if test="${fn:length(result) > 0}">
				<ul class="bottomList"><p class="title">营收明细</p></ul>
			</c:if>
			<c:forEach items="${result}" var="resultItem">
				<li class="clearfix liCon">
					<div class="left">
						<i class="iconfont icon-rili"></i>
					</div><div class="right">
						<div class="orderInfo">
							<div class="iconContent">
								<span class="timeSpan">${resultItem.count_time}</span>
								<span class="m_span">营业总额：<span class="moneySpan">¥<fmt:formatNumber value="${resultItem.totalmoney}" pattern="0.00" /></span>
								</span>
							</div>
							<div class="monSpan">
								<div>
									<span>线上收益：</span>
									<span>
										<fmt:formatNumber value="${resultItem.moneytotal}" pattern="0.00" />
									</span>
								</div>
								 <c:if test="${showincoins != 2}">	
									<div>
										<span>投币收益：</span>
										<span>
											<fmt:formatNumber value="${resultItem.totalcoinsearn}" pattern="0" />
										</span>
									</div>
									<div>
										<span>刷卡收益：</span>
										<span>
											<fmt:formatNumber value="${resultItem.oncardmoney}" pattern="0.00" />
										</span>
									</div>
								</c:if>
								
								<div>
									<span>消耗电量：</span>
									<span>
										<c:if test="${resultItem.consumeQuantity == null}">
											<fmt:formatNumber value="0" pattern="0.00" />
										</c:if>
										<c:if test="${resultItem.consumeQuantity != null}">
											<fmt:formatNumber value="${resultItem.consumeQuantity}" pattern="0.00" />
										</c:if>
									</span>
								</div>
								<c:if test="${resultItem.paymentmoney != 0}">
								<div>
									<span>缴费金额：</span>
									<span>
										<fmt:formatNumber value="${resultItem.paymentmoney}" pattern="0.00" />
									</span>
								</div>
								</c:if>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>	
			<%--  <c:forEach items="${result}" var="resultItem">
				<li class="clearfix liCon">
					<div class="left">
						<i class="iconfont icon-chongdianbao"></i>
					</div>
					<div class="right">
						<div class="orderInfo">
							<div class="iconContent">
								<span class="timeSpan">${resultItem.code}</span>
								<span class="m_span">营业总额：<span class="moneySpan">¥<fmt:formatNumber value="${resultItem.totalmoney}" pattern="0.00" /></span></span>
							</div>
						
							<p class="monSpan">
								<span>线上收益：</span><span><fmt:formatNumber value="${resultItem.totalonearn}" pattern="0.00" /></span>
								<span>投币收益：</span><span><fmt:formatNumber value="${resultItem.totalcoinsearn}" pattern="0" /></span>
							</p>
						</div>
					</div>
				</li> --%>
		
			</ul>
		</main>

		</div>
		<div class="sArea">
			<ul class="clearfix">
				<li data-from_s="3">
					<i class="iconfont icon-shijian"></i>
					<p>时间统计</p>
				</li>
				<li data-from_s="1">
					<i class="iconfont icon-chongdianbao"></i>
					<p>设备统计</p>
				</li>
				<li data-from_s="2">
					<i class="iconfont icon-xiaoqu"></i>
					<p>小区统计</p>
				</li>
				
			</ul>
			<div class="closeSDiv"><i class="iconfont icon-quxiao"></i></div>
		</div>	
		<nav class="mui-bar mui-bar-tab">
			<li><a href="/merchant/manage" style="display: inline-block; width: 100%; text-align:center; text-decoration:none;"><i class="iconfont icon-shouye"></i></a></li>
			<li class="toggleLi"><i class="iconfont icon-tianjia"></i></li>
			<li class="reloadLi"><i class="iconfont icon-shuaxin"></i></li>
		</nav>

		<div class="loading"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAKkUlEQVR4nO3dUXXjOhCAYUMohEAohIVQCIVQCGVQCIEQCIEQCAEQSwPh3odV9mZz20Qaa6Sx/H/n+LV1bI+kGcnyNAEAAAAAAAAAAAAAAAAAAAAAAAAoJiIvIvJ6uVx+9T4XwI0UGIcYo8QY/7keInIQkV3v8wO6EZHX+8C4P+hRsEki8vIsONIh9CTYHBHZZwTHdbi1732+QFMxxnNugMQYpff5As2k3CM3OK69yK73eQNNXC6XX6UBQrKOzSBAgAcIEOABAgTuiMhLCOEzxnhKx1FEvnqcy1oC5OaaHWOMJxE5hBA+KRgMZp7nt0eTcvM8v7U8nzUEyDzP70+u2XvL84GREMLHoxvd4wH0HiA5S2AIkgEULOdoOhnnOUDSNTvlXjOGWyuWeo/sh7BVTuI5QFLOkX1eIYSPFucFAyXrnW6CZGd9Xl4DRER2BT3u9XodrM8LRpQBYn7DHQeIy+sFI6VDrFYPo8cA0ZxTGmJ9Wp4XDKWEs/imxxjPluflMUDi77mO4mtFkr5ymmFDjLYlTG8BkuaJNMHB8GrtFvQiZmVfTwGSrs9Zc33oPQahzUWs3uTzFCClZd3rQe4xGGUraTLG9hIgmrJuOs4i8lL7fNCRp3G2owDZa64JS0wGlfaf0jwQVRczeggQbVk3xniseR5wJA0pNA9F1bKvhwCJyrIu76UMzsOwoneAeCtawJElZd9aiWnPAKGsi6fSy0DdWtCeAUJZF1mUrWiVsm+vAKGsi2zasm+sUMXpGCB7zW+mrLtRvcq+PQKEsi6K9Sr79giQSFkXGj2GHa0DZEFZt8u2SHCkcGOH20Nd9mwZID1+HwbTuuyrmYvRPqyUdVFFbFz2LWzVVe+mUNZFNa2rPIVfmFKtKKasu2LXfV9F5EtE9h669JZl38JvFL6W/n1Py/tL3DwXey/PRXOP9n3tWTlZUPZV9SIZvZZoW3NtsPcq66bPYX/99FxsZoO6Z5si974g2mGJNhe5+U76X4ERf+8+r/2bqtyjR+MkIi+5z8TwQz/FjTu1vija1a6aYdC9eZ7fROR1aYKs7AmrrVbOlXrQ3H2AFzVEq6AtOS5pTTU0ZV9PN05TRm451k+NgGoIOPRQS9My9xoCFJ6ru7Kox/N/lmfknqv1eXazJDhujxbDrpKyr8dWraQXtP6AUEmesfUAqXGBrod5fpKzhimVqV31Hlepte7aK2vyjGf33fJ8u9JWiJ7c4IPl+P9BkMgaFvM9yPvEMu9Ykmc8OoaeF1nwPnhOoJg+rPM8v18nNkMIH157jZ+kcz5Yf4izUp7x0zH+AkrtwsDcY/hauVM3ecbZ6N6qJ01XJ41LLVqY69F8/mTLDPKM74Kj6ZeIXchJIpcc1vnJ1qU8Y28ZGJ6LIE00uMi8GVeZcZ5xPY41VicMI3XTZ+Nu+r3371y7tErY8j6dNjmcyqV9f5obYKtFnrHG6mA31vmJx9lvr1KjZTWckvTuB4FRyjo/IUieM+7RyTNqMMxPxp94WmDBRtfPDoa5FixaMypcP1uwPeuPDRJ5RgM1h12938H2rGKDdM0zdr1/02bUyk8IkJ8teMHt9iDP6GlpXX7o1aELLRxisdzHE+VwgCT9AeW2peQZXqUlENnDLhL05woaHvKMtcjJTwiOfBmNDnnGGqVtdO5v7pkhQLl0LW/fEJQY45HvhwxARHapV9kRGHVwHQEAAAAAAAAAAAAAQEUisrtcLr9aH6wm7Yd7/kSjHfdyl7KzlUwD3PNMDXZG1BzCalM73PNMyjfPml2w1XTBK8I9L2C9+XSF49j7Go2Ge17AcUvy53A7Nl0p7nkmEXntfSFyDnfj0hXjnpddrF3vC5FzsNVlPdzzsotl9mHOmoerpG3luOeFLD73W/nwk7ANgntewHmLImxJUx/3vFCaNOp9Yf53odj+0g73vFDpzoaWFyn+3sxs1/uajI57riQir50Wrr26qH9vEPccAAAAAAAAAAAAAAAMIK0X4vNrFXAtB5LW6dzv2cQH7hXSyt3T7QJBETm4eK0VZTI/A33ofZ5rkRqZh9dyNatot6x0lz96kudCCB+5S8759rxTIvIyz/N7LN/lT3qfu2fazeFoeBz5bmzMzayjpPf45iDX6yknz2BYsEyl60t+0lLt3cRDCJ+9f5NXz5JzGiJn5nl+U+QZDLGUagZIOvxuqLBmS/OMRzeMCa+fGW7tc2L+pII0Y1u7FWN4VWBhov5s2LUnP1Fo8dUigiNfCOHT6j6kQCE/ydXgq0UnF5sar0za0d3yvpCfPGKYZ/y5ASGED3KOZSyHXNcGzN22oT3dDKfMAoOxbn3Wuylu/p41+jrqkdbITq3J2ieBsr38pMF4lmUODVnMT92PAjZTFk4X06rXIM/oyDo/Gb64YvjpYNM8Q0ReQgifIvIlIvsQwofF/7EiIi+Xy+VXCOEzhPBp+aBZ7/Y+dF5iVE83zTPSOX8b1GsIlNSqf3f+YjkXlPKT6l+hGnr+KsZ4rHixztYPaM6QwXMSmdMgWedqyvd0Hh0ny/PtKtaZ52iSZ6R5mdW+mVjy9acWY/uK+cnZ+ly7WRogLd8tKOzt3L2ZWDi8afLQVcpPxu1BFuQgTeczNK2dt+QxFg5rWvaCqcxf0gD9OYbOQRTLp6V1IpzOsejhSgGyKIBT6/paq96vePiaL/1X5Cf+vl5bW5oHyQqMHvMZ2l5O24OkwDjEu3xn6XDy/u9l/oa99v8tkdtjr6FqWEVqOb4NjNjxS6UislM+WKo9ttJQ4+H/0/Yo2rF+x2v/KD9pPpJwIfUS+xjjMU3E7Xqej/ah0lSBCiZNRXNdUvAV/5YY47H0f9WWnouDiBw8PBeYysqiNR6okmDUDn20E3XDL+lAuaisqmiHQLEwOdX8jzRk1AT9uHMNKKedxNLOomuGPwuKANk91V0v8q75fxjMggWUqtxgmnTDuQXJevPfh4Foy7pLJqxaBsg0PawaPuu1upR94YS2rBtjPC+Zo2kdINNUPrN+EyS7Jf8XK9ZrfN4jQFpX6bBymTP637Woiz+80yNApomyLwrExmXdWx0DRFv2dbdaGYZal3Xv9QqQaaLsiycWlj2rLJ7sHCCq1cqRsu829Cjr3usZINNE2Rc/6FXWvdc7QKZJ/3bn8O9jbJmXKo6HAKHsi7/0LOve8xAg0+SnwUBnKTHVDClMtrx0FCDaL0hR9h1J77LuPS8BMk367xBu8g2/EXko695zFiCUfbfMQ1n3nqcAmSbKvpu1oPeoWta95y1ApmlR2XdneV4wpO09rKs0HgNEW/YlF1kxzboji7LuPY8BMk3qsi/zImulqNA02bnPcYAUl33JQ1asdIjV6lMGXgNkmsqv2dB75Y6usEVstjet5wApLPtS6l27zBKmtFw+4TlASs6P3mMQz9Zhta7EeA+QjHPc5l65I5P/f9P7ulF28+XbawiQafrv46Xx9xzJOcZ4SnvlsuR9ZD0+q3BrLQECdEGAAA8QIMADBAjwgHLGetf7vIFmYtkqY97gw7YUfmHKfAEl4ErhNwqZd8D2ZCTrwpaf2LSb76T/FRix4+ewAZfmeX5LS2K6zvQDAAAAAAAAAAAAAAAAAAAAAAAAa/UvH+RypHL5YaEAAAAASUVORK5CYII=" alt="">
			<p>正在加载</p></div>	
			<div class="icon_tip_con">
				耗电量根据主板数据汇总，仅供参考，依实际为准。耗电量(包含扫码,投币,刷卡)开始统计时间为2019年9月1日！
			</div>
	</div>
	
	<script>
	$(function(){
		var showincoins= $('body').attr('data-showincoins').trim() //是否显示投币收益 2为不显示，
		var from_s= 3  // 1、设备 2、小区  3、时间（汇总）
		var select_d  // 1、本日 2、本周 3、本月
		select_d= $('.select_d li.active').length>0 ? $('.select_d li.active').index()+1 : undefined
		if(from_s==1 || from_s== 2){
			$('.searchLi').fadeIn(0)
			$('#topSearch').val('')
		}
		
		
		var screenWidth= $('body').width()
		console.log(screenWidth)
		var mask = mui.createMask(function(){
			$('.sArea').fadeOut(0)
			$('.icon_tip_con').fadeOut(200)
		});
		$('.toggleLi').click(function(e){
			mask.show();//显示遮罩
			$('.sArea').fadeIn(0)
		})
		$('.closeSDiv').click(function(){
			$('.sArea').fadeOut(0)
			mask.close();//关闭遮罩
		})
		$('header .searchLi').click(function(){
			$('header .se').animate({
				right: 0
			},300)
		})
		$('.closeSpan').click(function(){
			$('#topSearch').val('')
			$('header .se').animate({
				right: -screenWidth
			},0)
		})
		
		
		
		/* 点击图标显示提示 */
		$('.icon_tip').click(function(){
			$('.icon_tip_con').fadeIn(200)
			mask.show();//显示遮罩
		})
		
		
		
		
		/*点击详情取消图标开始*/
		$('.closeDetailI').click(function(){
			$('.alertDetail').fadeOut()
		})
		$('main').on('click','.liCon',function(){
			$('.alertDetail').fadeIn()
		})
		/*点击详情取消图标结束*/
		$('.sArea ul li').click(function(){ //点击搜索分类
			from_s= $(this).attr('data-from_s').trim()
			if(from_s == 1){
				$('.searchLi').fadeIn()
				$('#topSearch').attr('placeholder','请输入设备号')
				sendAjax(1)
				
			}else if(from_s == 2){
				$('.searchLi').fadeIn()
				$('#topSearch').attr('placeholder','请输入小区名称')
				sendAjax(2)
			}else if(from_s == 3){
				$('.searchLi').fadeOut()
				$('#topSearch').val('')
				$('header .se').animate({
					right: -screenWidth
				},0)
				sendAjax(3)
			}
			$('.sArea').fadeOut(0)
			mask.close();//关闭遮罩
		})
		$('.select_d li').click(function(){ //点击切换本日、本周、本月
			$(this).siblings().removeClass('active')
			$(this).addClass('active')
			select_d= parseInt($(this).attr('data-select_d'))
			if(select_d == 1){ //今日
				startTime = new Date().Format("yyyy/MM/dd");
                endTime = new Date().Format("yyyy/MM/dd");
				$('header .mid .upDiv').eq(0).find('span').text('前一天')
				$('header .mid .upDiv').eq(1).find('span').text('后一天')
			}else if(select_d == 2){
				startTime = ThisWeekStr().split("&&")[0];
                endTime = ThisWeekStr().split("&&")[1];
				$('header .mid .upDiv').eq(0).find('span').text('前一周')
				$('header .mid .upDiv').eq(1).find('span').text('后一周')
			}else if(select_d == 3){
				startTime = ThisMonthStr().split("&&")[0];
                endTime = ThisMonthStr().split("&&")[1];
				$('header .mid .upDiv').eq(0).find('span').text('前一月')
				$('header .mid .upDiv').eq(1).find('span').text('后一月')
			}
			$(".inpDiv").html(startTime + "&nbsp;-&nbsp;" + endTime)
	       $(".inpDiv").mobiscroll('setVal', [new Date(startTime), new Date(endTime)]);

			sendAjax(1)
		})
		$('#topSearch').on('keypress',function(e){
			if(e.keyCode === 13){
				/*获取input中订单号*/
				sendAjax(3)
			}
		})
		$('.reloadLi').on('click',function(){
			 sendAjax()
		})
	function sendAjax(){
		$('main>ul').html('')
		$('.loading').fadeIn(300)
		var param= $('#topSearch').val().trim()
		$.ajax({
			url: '/merchant/earningcollectajax',
			data: {
				type: from_s,
				param: param,
				begintime: startTime,
				endtime: endTime
			},
			dataType: 'json',
			type: 'post',
			success: function(res){
				$('main>ul').html('')
				var liHtml= ''
				if(res.result.length <= 0){
					liHtml= '<div class="noDataTip"><div><i class="iconfont icon-wodedingdan"></i></div><div>暂无记录</div> </div>'
				}
				var fragment= document.createDocumentFragment()
				if(res.type==1){
					for(var i=0; i< res.result.length; i++){
						res.result[i].totalmoney= res.result[i].totalmoney.toFixed(2)
						res.result[i].totalonearn= res.result[i].totalonearn.toFixed(2)
						var consumeQuantity= res.result[i].consumeQuantity
						consumeQuantity= typeof consumeQuantity == 'undefined' ? '0.00' : (consumeQuantity).toFixed(2)
						var iconStr= showincoins != 2 ? '<div><span>投币收益：</span><span>'+res.result[i].totalcoinsearn+'</span></div>' : ''
						var str= '<li class="clearfix liCon"><div class="left"><i class="iconfont icon-chongdianbao"></i></div><div class="right"><div class="orderInfo"><div class="iconContent"><span class="timeSpan">'+res.result[i].code+'</span><span class="m_span">营业总额：<span class="moneySpan">¥'+res.result[i].totalmoney+'</span></span></div><div class="monSpan"><div><span>线上收益：</span><span>'+res.result[i].totalonearn+'</span></div>'+iconStr+'<div><span>消耗电量：</span><span>'+consumeQuantity+'</span></div></div></div></div></li>'
						liHtml+= str		
					}
					$('main>ul')[0].innerHTML= liHtml
				}else if(res.type == 2){
					for(var i=0; i< res.result.length; i++){
						res.result[i].totalmoney= res.result[i].totalmoney.toFixed(2)
						res.result[i].totalonearn= res.result[i].totalonearn.toFixed(2)
						var consumeQuantity= res.result[i].consumeQuantity
						consumeQuantity= typeof consumeQuantity == 'undefined' ? '0.00' : (consumeQuantity).toFixed(2)
						var iconStr= showincoins != 2 ? '<div><span>投币收益：</span><span>'+res.result[i].totalcoinsearn+'</span></div>' : ''
						var str= '<li class="clearfix liCon"><div class="left"><i class="iconfont icon-xiaoqu"></i></div><div class="right"><div class="orderInfo"><div class="iconContent"><span class="timeSpan">'+res.result[i].name+'</span><span class="m_span">营业总额：<span class="moneySpan">¥'+res.result[i].totalmoney+'</span></span></div><div class="monSpan"><div><span>线上收益：</span><span>'+res.result[i].totalonearn+'</span></div>'+iconStr+'<div><span>消耗电量：</span><span>'+consumeQuantity+'</span></div></div></div></div></li>'
						liHtml+= str		
					}	
				
					$('main>ul')[0].innerHTML= liHtml
				}else{
					var info= '<li class="clearfix"><div class="middle"><p>营收总额(元)</p><h2 id="h_total_pay">0.00</h2><div class="mlist"><p data-hideby="lotteryTicket">线上收益：<span id="p_ad_pay">0.00</span></p><p data-hideby="lotteryTicket">投币收益：<span id="p_coin_pay">0</span></p><p data-hideby="lotteryTicket">缴费金额：<span>0</span></p></div></div></li>'
					for(var k=0; k< res.data.length; k++){
						res.data[k].totalmoney= res.data[k].totalmoney.toFixed(2)
						res.data[k].totalonearn= res.data[k].totalonearn.toFixed(2)
						res.data[k].paymentmoney= res.data[k].paymentmoney.toFixed(2)
						res.data[k].totaloncardmoney= res.data[k].totaloncardmoney.toFixed(2)
						var totalConsume= res.data[k].totalConsume
						totalConsume= typeof totalConsume == 'undefined' ? '0.00' : (totalConsume).toFixed(2)
						var totalmoney= showincoins != 2 ? res.data[k].totalmoney : res.data[k].totalonearn
						totalmoney= typeof totalmoney == 'undefined' ? '0.00' : parseFloat(totalmoney).toFixed(2)
						var paymentmoneyStr= res.data[k].paymentmoney == 0 ? '' : '<p data-hideby="lotteryTicket">缴费金额：<span>'+res.data[k].paymentmoney+'</span></p>' 
						var totalIconStr= showincoins != 2 ? '<p data-hideby="lotteryTicket">投币收益：<span id="p_coin_pay">'+res.data[k].totalcoinsearn+'</span></p>' : ''
						var totaloncardmoney= showincoins != 2 ? '<p data-hideby="lotteryTicket">刷卡收益：<span id="p_coin_pay">'+res.data[k].totaloncardmoney+'</span></p>' : ''		
						info= '<li class="clearfix"><div class="middle"><p>营收总额(元)</p><h2 id="h_total_pay">'+totalmoney+'</h2><div class="mlist"><p data-hideby="lotteryTicket">线上收益：<span id="p_ad_pay">'+res.data[k].totalonearn+'</span></p>'+totalIconStr+totaloncardmoney+'<p data-hideby="lotteryTicket">消耗电量：<span>'+totalConsume+'</span></p>'+paymentmoneyStr+'</div></div></li>'
					}
					for(var i=0; i< res.result.length; i++){
						res.result[i].totalmoney= res.result[i].totalmoney.toFixed(2)
						res.result[i].moneytotal= res.result[i].moneytotal.toFixed(2)
						res.result[i].paymentmoney= res.result[i].paymentmoney.toFixed(2)
						res.result[i].oncardmoney= res.result[i].oncardmoney.toFixed(2)
						
						var dt= new Date(res.result[i].count_time).Formit('YYYY-MM-DD')
						var consumeQuantity= res.result[i].consumeQuantity
						consumeQuantity= typeof consumeQuantity == 'undefined' ? '0.00' : (consumeQuantity).toFixed(2)
						var iconStr= showincoins != 2 ? '<div><span>投币收益：</span><span>'+res.result[i].incoinsmoney+'</span></div>' : ''
						var oncardmoneyStr=  showincoins != 2 ? '<div><span>刷卡收益：</span><span>'+res.result[i].oncardmoney+'</span></div>' : ''
						var paymentmoneyStrChild= res.result[i].paymentmoney == 0 ? '' : '<div><span>缴费金额：</span><span>'+res.result[i].paymentmoney+'</span></div>' 
						var str= '<li class="clearfix liCon"><div class="left"><i class="iconfont icon-rili"></i></div><div class="right"><div class="orderInfo"><div class="iconContent"><span class="timeSpan">'+dt+'</span><span class="m_span">营业总额：<span class="moneySpan">¥'+res.result[i].totalmoney+'</span></span></div><div class="monSpan"><div><span>线上收益：</span><span>'+res.result[i].moneytotal+'</span></div>'+iconStr+oncardmoneyStr+'<div><span>消耗电量：</span><span>'+consumeQuantity+'</span></div>'+paymentmoneyStrChild+'</div></div></div></li>'		
						liHtml+= str
					}
					var ulO=$('<ul class="bottomList"><p class="title">营收明细</p></ul>')
					/* ulO[0].appendChild($(str)[0]) */
					$('main>ul')[0].appendChild($(info)[0])
					$('main>ul')[0].appendChild(ulO[0])
					$('main>ul')[0].innerHTML+= liHtml
				}
			},
			error: function(err){
				console.log(err)
			},
			complete: function(){
				$('.loading').fadeOut(300)
			}
		})
	}
    //日期控件初始化
    var a = {
        theme: 'material',
        lang: 'zh',
        display: 'bottom',
        defaultValue: [new Date((new Date()-15*24*60*60*1000)), new Date()],
        //最小时间
        //min: new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * 30),
        max: new Date(),
        onSet: function (event, inst) {
        	console.log(event) //"2019/08/06 - 2019/08/15"
            //点击确定的时候
            $(".inpDiv ").html(event.valueText);
            $('header .mid .upDiv').eq(0).find('span').text('前一天')
			$('header .mid .upDiv').eq(1).find('span').text('后一天')
            var start = event.valueText.split('-')[0];
            var end = event.valueText.split('-')[1];
            startTime = start;
            endTime = end;
            $('.select_d li').removeClass('active');
           	sendAjax(2)
        },
        onInit: function (event, inst) {
            //初始化的时候
            $(".inpDiv").html(startTime + "&nbsp;-&nbsp;" + endTime);
           
        }
    }

    rangeBasicInit(a)

 	$('.upDiv').click(function(){
 		var s = $(".inpDiv").text().split("-")[0].trim();
        var e = $(".inpDiv").text().split("-")[1].trim();
 		var textStr= $(this).find('span').text().trim()
 		console.log(textStr)
 		if(textStr == '前一天'){
 			startTime = new Date(Date.parse(s) - 24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			endTime = new Date(Date.parse(e) - 24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			
 		}else if(textStr == '后一天'){
 			startTime = new Date(Date.parse(s) + 24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			endTime = new Date(Date.parse(e) + 24 * 60 * 60 * 1000).Format("yyyy/MM/dd")

 		}else if(textStr == '前一周'){
 			startTime = new Date(Date.parse(s) - 7*24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			endTime = new Date(Date.parse(e) - 7*24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			
 		}else if(textStr == '后一周'){
 			startTime = new Date(Date.parse(s) + 7*24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			endTime = new Date(Date.parse(e) + 7*24 * 60 * 60 * 1000).Format("yyyy/MM/dd")
 			
 		}else if(textStr == '前一月'){
 			m--;
            if (m == 0) {
                m = 12; y--
            }
            setm()
 			
 		}else if(textStr == '后一月'){
 			 m++;
            if (m == 13) {
                m = 1; y++
            }
            setm()
 		}
 		$(".inpDiv").html(startTime + "&nbsp;-&nbsp;" + endTime);
        $(".inpDiv").mobiscroll('setVal', [new Date(startTime), new Date(endTime)]);
        sendAjax()
 	})
 	
 	Date.prototype.Formit= function(fmt){
		var o= {
			'M+': this.getMonth()+1,
			'D+': this.getDate(),
			'h+': this.getHours(),
			'm+': this.getMinutes(),
			's+': this.getSeconds()
		}
		if(typeof fmt == 'undefined'){
			return this.Formit('YYYY-MM-DD hh:mm:ss')
		}
		var reg= /(Y+)/g
		if(reg.test(fmt)){
			fmt= fmt.replace(/(Y+)/g,this.getFullYear().toString().substr(4-RegExp.$1.length))
		}
		for(var key in o){
			var regKey= new RegExp('('+key+')','g')
			if(regKey.test(fmt)){	
				fmt= RegExp.$1.length==2 ?  fmt.replace(regKey,('00'+o[key]).substr(-2)) : fmt.replace(regKey,o[key]+'') 
			}
		}
		
		return fmt	
		
	}
    
	})
	
	</script>
</body>
</html>
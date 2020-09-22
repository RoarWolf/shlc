<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>首页</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%-- <%@ include file="/WEB-INF/views/public/commons.jspf" %> --%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@ include file="/WEB-INF/views/public/buttomnav.jsp"%>
<link rel="stylesheet" href="/css/weui.css"></link>
<script src="${hdpath}/js/jquery.js"></script>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1946361_rum2azemb8f.eot?t=1595039332360'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1946361_rum2azemb8f.eot?t=1595039332360#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAUMAAsAAAAACagAAAS/AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDSAqGJIUvATYCJAMYCw4ABCAFhG0HUBtdCFGUTk6W7EeBu6eIi6FOeGmKQmIRaSlpmPL/f4KIDnO2fi32T6Xie4vVQhTRIIYg2gRv+t/UB1SpG5lJTVIRIxWn5EFohB5h5pbORAiZqPwtBPH35ux/7yofHXhr2gRrKoVDHWMRDkNtmoNjpktrsLxVukQmZWrhaDwe0IAiEvf/Qq//Yi+o/xhOwO2Ruui+DQF1UwMgy7wSEjhwfJEAOeFYCjj4XDyFLKrWjNmZyS0oqRY36HX4NH9fXvTDASoSvvL+OFcNMu7B4xAbax1rpSOAur4AcNsHBnYADjmLDX3A7OwOpv75CzYuAZqqlN+7b7xtvft7HFoLPh2Piab4lwfKOBGQMjQ1GfY6F/ijOtCPuYEODM0t5MPx/xdArKYamwgAWeOMDvhBdoDjdkw+94q7YYsmiXk6O7i4hXm7veaplZ6T5hcqy6ta+WWTLHfbYKk1mxVrpQZJIi27yuL9HFmuO3g3dM29hAN3QlbeHjEOkR/WyM9J6YHK/Kp2jSVMLcs1lhJvVklSC7psRhC2fHr9wq0AjN02o8sPWWxi20mqTtVYX9tA17Q14iMQJ9QZcQEDWb1TMtfAmSo9r5Ef1r5kecFOf1mzeHnXFh/znNolm7Yt9Zae4+R0fLPJj+m75HJvcuFWfrpv8vgVS7Zs8lEtGrvVfbppXILcF1i0bOlXr38rUCkZluHIrd4Lva0PZJz4bEsJud4r425p6yn8+1WlUpWbOrv0WAnCMEiJkOsKrcWtJm4/cXF5cps+HKTFWBSxP/4FPRpwwl3xvNfB0KNnw/ckHopK31Jo+ueqwp8t3Ym8/WpnQnd+edNlh44vdsaJb9/cmk6mK8vJcmXw1dsKtxjXbsHdXGilAty5WDy6z3ASvvagy+/2FHKMv/EgYei7FI527hgHlHPN8RSNJsV3wMy3a+fbzcfvHAua6ztv8t/JKVPi3QrrxRv06H7VSxMWJW+ydVjnuu7qh1U9VocFjgFFNdMLaDVdfSQ0/86VIwvdDi+8cic/9AhdjVbNnLoRw6IZ4T8/fNwfP2NVSJSiyM00fMRgmnySE89a5aYwss0tJywoVldAWuPESSNu7l7kGjnQf56S/J+dcLruPz/U8Yd7JBiJT7V/9CFlXHqR+B/5DouePyU9T4OtVjyK2RX3GO+fE4Nb3VL+VJ0ygO/pGLWpt7Lq64ykUwAuGzO51ddXIIPCfFm+WnRnfndS5xSrAOnpmKQV1EUPXFWxvqd1mUctqfRI1cgFVTEBUzVDOnYHoWEPqeoAdVv5+xsGBBjJNbAxCUB07QLV9gWm6wbSsU8gjH2B1A0M6l7D68SGRXB4tgApEaqJ9mEER/MGRvGKuNVYBdlBOkrwak1KDRSMXIKICo8MFvKhAQp1rDEOZqNFkSEYgdcTeXAf1Ol4olPgOyAthmtEsVMZEcHEfVE4zesB7oIAooggNUK7YQgcGs+A0fuL8NDPV4FYg+hQhJSOH9kakMCIG54QJVxkDmS+yZCrY1/GGw3GiiYSMWweQ8DTI+RhANKJLI/QGT+tA0QThdOUSHdSiqBuTF5TePsW/VYeAOr4GzOKSJGjFOWosKNh0E7kBho1HKaheHsWGtihnMF+GGXoMIIsAAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1946361_rum2azemb8f.woff?t=1595039332360') format('woff'),
  url('//at.alicdn.com/t/font_1946361_rum2azemb8f.ttf?t=1595039332360') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1946361_rum2azemb8f.svg?t=1595039332360#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-eye:before {
  content: "\e7f5";
}

.icon-ti-shi:before {
  content: "\e600";
}

.icon-hao:before {
  content: "\e614";
}

.icon-gengxin:before {
  content: "\e62d";
}

.icon-yanjing:before {
  content: "\e834";
}


@keyframes Loading_Rotate {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}
@-webkit-keyframes Loading_Rotate {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}
@-mos-keyframes Loading_Rotate {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}
@-ms-keyframes Loading_Rotate {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}			
		  
		* {
		    padding: 0;
		    margin: 0;
		}
		html,body{
		    background-color: #fff !important;
		    width: 100%;
		    height: 100%;
		    overflow: auto;
		}
		.app {
			padding-bottom: 50px;
		}
		li {
		    list-style: none;
		}
		header {
		    background-color: #44AF3D;
		    padding-bottom: 5px;
		}
		header .top{
		    display: flex;
		    justify-content: center;
		    flex-direction: column;
		    align-items: center;
		    color: #fff;
		}
		header .top p {
		    margin-top: 39px;
		    font-size: 16px;
		    color: white;
		}
		header .top span {
		    /* margin-top: 10px; */
		    font-size: 47px;
		    font-weight: 700;
		    font-family:  "黑体","楷体_GB2312";
		}
		header .top .forbiden-style {
			font-size: 20px;
		    font-weight: 700;
		    font-family: none;
		}
		header .top .now-money-wrapper {
			line-height: 1;
			margin-top: 10px;
			height: 45px;
		}
		header .top .icon-yen{
		    font-size: 25px;
		    font-weight: 500;
		    position: relative;
		    top: -21px;
		    font-family: none;
		}
		
		header .top .icon-yen .now-money{
			    font-size: 47px;
			    position: relative;
			    top: 10px;
			    font-weight: bold;
			    font-family: "黑体","楷体_GB2312";
		}
		header .bottom {
		    display: flex;
		    flex-wrap: wrap;
		   /*  margin-top: 19px; */
		    margin-left: 19px;
		    
		}
		
		<c:if test="${showincoins == 2}" >
			header .bottom>div { 
			    width: 49%;
			    height: 60px;
			    display: flex;
			    justify-content: center;
			    flex-direction: column;
			    align-items: center;
			    color: #fff; 
			    margin-bottom: 15px;
			    font-size: 14px;
			}
			header .bottom>div:nth-child(2n) { /**2列*/
			    border-right: none;
			}
		</c:if>
		<c:if test="${showincoins != 2}" >
			header .bottom>div {  
			    width: 32%;
			    height: 60px;
			    display: flex;
			    justify-content: center;
			    flex-direction: column;
			    align-items: center;
			    color: #fff; 
			    margin-bottom: 15px;
			    font-size: 14px;
			} 
			header .bottom>div:nth-child(3n) {
			    border-right: none;
			}
		</c:if>
		
		header .bottom>div>span:last-child{
		    font-weight: 700;
		    margin-top: 14px;
		    font-size: 16px;
		    font-family: 'Trebuchet MS', Helvetica, sans-serif;
		   
		}
		header .bottom>div {
		    border-right: 1px solid rgba(225,225,225,.3);
		   
		}
		
		main ul {
		    margin-top: 9px;
		    overflow: hidden;
		    background-color: #fff;
		}
		main ul li {
		    float: left;
		    width: 33.3333%;
		   /*  height: 100px; */
		    height: 86px;
		    display: flex;
		    justify-content: center;
		    align-items: center;
		}
		main ul li:nth-child(3){
		    border-right:  none;
		}
		main ul li button {
		    width: 90%;
		   /*  height: 75%; */
		    height: 85%;
		    color: #fff;
		    text-decoration: none;
		    display: flex;
		    justify-content: center;
		    align-items: center;
		    font-size: 15px;
		    border-radius: 10px;
		    background-color: #44AF3D;
		    position: relative;
		}
		main ul li button .online {
		    font-size: 12px;
		    color: #fff;
		    position: absolute;
		    bottom: 8px;
		}
		main ul li button .online .icon-hao {
			font-size: 12px;
		}
		button{
		    border: none;
		}
		main ul li button:focus,
		main ul li button:active:focus,
		main ul li button.active:focus,
		main ul li button.focus,
		main ul li button:active.focus,
		main ul li button.active.focus {
		    outline: none ;
		    border-color: transparent !important;
		    box-shadow:none;
		}
		.icon_tip_span {position: relative}
		.icon_tip {position: absolute; top: 0;right: -19; font-size: 16px; color: #fff; width: 18px;}
		.icon_tip_con {width: 80vw; padding: 10px; color: #666; font-size: 14px; background-color: #fff;position:fixed; left: 50%;top: 22%;
		border-radius: 5px;
		line-height: 2em;
		transform:  translate(-50%,-50%);
		-o-transform:  translate(-50%,-50%);
		-mz-transform:  translate(-50%,-50%);
		-webkit-transform:  translate(-50%,-50%);
		z-index: 999;
		display: none;
	}
	.weui-toast {
		top: 35%;
	}
	.weui-mask_transparent {
		z-index: -99;
	}
	
	.contral {
		text-align: center;
	    color: #FFF;
	    font-size: 14px;
	    margin-bottom: 0px;
	}
	.contral .title {
		padding-right: 7px;
	}
	.contral .iconfont {
		padding: 7px;
	}
	.icon-wrapper {
		display: none;
	}
	.icon-hao {
		font-weight: normal;
	}
	.contral .icon-gengxin {
		transform-origin: center;
	}
	.contral .icon-gengxin {
		display: inline-block;
	}
	.contral .Loading {
		animation: Loading_Rotate 1s linear infinite;
	}
	.contral-tip {
		text-align: center;
		font-size: 14px;
        color: #FFF;
        padding: 0 12vw;
	}
	.contral-tip i {
		font-size: 14px;
	}
</style>
<style>
	 /* <c:if test="${rank == 6}">
	     <c:if test="${empty home2}">
		 	main ul li:nth-of-type(1) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty home3}">
		 	main ul li:nth-of-type(2) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty home4}">
		 	main ul li:nth-of-type(3) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty home5}">
		 	main ul li:nth-of-type(4) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty homep}">
		 	main ul li:nth-of-type(5) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty home7}">
		 	main ul li:nth-of-type(6) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
	     <c:if test="${empty home8}">
		 	main ul li:nth-of-type(7) {
		 		display: none;
		 		pointer-events: none;
		 	}
	     </c:if>
		.mui-bar-tab {
			display: none;
			pointer-events: none;
		}
     </c:if> */
     
     <c:if test="${rank == 6}">
	     <c:if test="${empty home2}">
		 	main ul li:nth-of-type(1) {
	 		    pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home3}">
		 	main ul li:nth-of-type(2) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home4}">
		 	main ul li:nth-of-type(3) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home5}">
		 	main ul li:nth-of-type(4) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home6}">
		 	main ul li:nth-of-type(5) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home7}">
		 	main ul li:nth-of-type(6) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
	     <c:if test="${empty home8}">
		 	main ul li:nth-of-type(7) {
		 		pointer-events: none;
   				cursor: not-allowed;
   				opacity: .6;
		 	}
	     </c:if>
		.mui-bar-tab {
			display: none;
			pointer-events: none;
		}
     </c:if>
</style>
</head>
<body data-showincoins="${showincoins}" data-origin="${isCalculate}" data-merid="${merid}">
	<input id="appId" type="hidden" value="${appId }">
	<input id="nonceStr" type="hidden" value="${nonceStr }">
	<input id="timestamp" type="hidden" value="${timestamp }">
	<input id="signature" type="hidden" value="${signature }">
	<input type="hidden" id="begintimeval" value="${begintime}">
	<input type="hidden" id="endtimeval" value="${endtime}">
	<div class="app">
        <header>
            <div class="top">
                 <p>今日收益</p>
                 <div class="now-money-wrapper">
                 	<c:choose>
               			<c:when test="${rank == 6 && empty home1}"><span class="forbiden-style">— —</span></c:when>
               			<c:otherwise>
               				<%-- <span>&yen;&nbsp;</span><fmt:formatNumber type="number" value="${nowMoney}" maxFractionDigits="2" pattern="0.00"/> --%>
               				<span class="icon-yen">&yen;&nbsp; <span class="async-loading now-money" data-name="nowMoney" data-decimal="2" >--</span></span>
               				<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
               			</c:otherwise>
               		</c:choose>
                 </div>
            </div>
            <div class="bottom">
                <div>
                	<span>线上收益</span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${allMoney}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="allMoney" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
                <div>
                	<span>未提现</span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${earnings}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="earnings" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
                <div>
                	<span>昨日收益</span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${yestMoney}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="yestMoney" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
                <c:if test="${showincoins != 2}">
                	<div>
                		<span>投币收益</span>
                		<span>
                			<c:choose>
	                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
	                			<c:otherwise>
	                				<%-- <fmt:formatNumber type="number" value="${totalcoins}" maxFractionDigits="2" pattern="0.00"/> --%>
	                				<span>
	                					<span class="async-loading" data-name="totalcoins" data-decimal="2" >— —</span>
	                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
	                				</span>
	                			</c:otherwise>
	                		</c:choose>
                		</span>
                	</div>
	                <div>
	                	<span>今日投币</span>
	                	<span>
	                		<c:choose>
	                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
	                			<c:otherwise>
	                				<%-- <fmt:formatNumber type="number" value="${codenowcoins}" maxFractionDigits="2" pattern="0.00"/> --%>
	                				<span>
	                					<span class="async-loading" data-name="codenowcoins" data-decimal="2" >— —</span>
	                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
	                				</span>
	                			</c:otherwise>
	                		</c:choose>
	                	</span>
	                </div>
	                <div>
	                	<span>昨日投币</span>
	                	<span>
	                		<c:choose>
	                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
	                			<c:otherwise>
	                				<%-- <fmt:formatNumber type="number" value="${codeyestcoins}" maxFractionDigits="2" pattern="0.00"/> --%>
	                				<span>
	                					<span class="async-loading" data-name="codeyestcoins" data-decimal="2" >— —</span>
	                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
	                				</span>
	                			</c:otherwise>
	                		</c:choose>
	                	</span>
	                </div>
                </c:if>
                <div>
                	<span class="icon_tip_span">总耗电量<i class="icon_tip iconfont icon-ti-shi"></i></span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${totalConsume/100}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="totalConsume" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
                <div>
                	<span>今日耗电</span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${todayConsume/100}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="todayConsume" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
                <div>
                	<span>昨日耗电</span>
                	<span>
                		<c:choose>
                			<c:when test="${rank == 6 && empty home1}">— —</c:when>
                			<c:otherwise>
                				<%-- <fmt:formatNumber type="number" value="${yesterdayConsume/100}" maxFractionDigits="2" pattern="0.00"/> --%>
                				<span>
                					<span class="async-loading" data-name="yesterdayConsume" data-decimal="2" >— —</span>
                					<div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>
                				</span>
                			</c:otherwise>
                		</c:choose>
                	</span>
                </div>
            </div>
            <div class="icon_tip_con">
				耗电量根据主板数据汇总，仅供参考，依实际为准。耗电量(包含扫码,投币,刷卡)开始统计时间为2019年9月1日！
			</div>
			<div class="contral-tip">当前数据非最新数据，如想查看最新数据，请点击下方的 “<i class="iconfont icon-gengxin"></i>”按钮</div>
            <div class="contral">
        		<span class="title"></span>
        		<i class="iconfont icon-gengxin"></i>
        		<i class="iconfont icon-yanjing" data-status="show"></i>
        	</div>
        </header>
        <main>
            <ul>
                <li>
                	<button id="equlist"><span>设备管理</span>
                		<%-- <span class="online load-success">在线${onlines}/${disline+onlines}台</span> --%>
                		<span class="online">在线<span class="${rank == 6 && empty home2 ? '' : 'async-loading'}" data-name="deviceNum">0/0</span><div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>台</span>
                	</button>
                </li>
                <li>
                	<button id="icManage"><span>IC卡管理</span>
                	<%-- <span class="online">${onlincardcount}张</span> --%>
                		<span class="online"><span class="${rank == 6 && empty home3 ? '' : 'async-loading'}" data-name="onlincardcount">0</span><div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>张</span>
                	</button>
                </li>
                <li>
                	<button id="merManage">
                		<span>会员管理</span> 
                		<%-- <span class="online">${clientsnum}人</span> --%>
                		<span class="online" ><span class="${rank == 6 && empty home4 ? '' : 'async-loading'}" data-name="clientsnum">0</span><div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>人</span>
                	</button>
                </li>
                <li>
                	<button id="areaManage">
                		<span>小区管理</span> 
                		<%-- <span class="online >${areanum}个 </span> --%>
                		<span class="online"><span class="${rank == 6 && empty home5 ? '' : 'async-loading'}" data-name="areanum">0</span><div class="icon-wrapper"><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i><i class="iconfont icon-hao"></i></div>个</span>
                	</button>
                </li>
                <li><button id="equipmentbind">设备绑定</button></li>
                <li><button id="historyEarn">历史收益</button></li>
                <li><button id="deviceFreeManage">缴费管理</button></li>
                <c:if test="${origin==1}">
               		 <li><button id="originicManage">测试支付</button></li>
                </c:if>
            </ul>
        </main>
    </div>
    <!-- loading toast -->
    <div id="loadingToast" style="display: none;">
        <div class="weui-mask_transparent"></div>
        <div class="weui-toast">
            <i class="weui-loading weui-icon_toast"></i>
            <p class="weui-toast__content">数据加载中</p>
        </div>
    </div>
</body>
<script>
sessionStorage.removeItem('ulStr')
sessionStorage.removeItem('data')
sessionStorage.removeItem('urlFrom')
mui.init({
	swipeBack : true
//启用右滑关闭功能
});
var isNowGetData= false //是否正在获取数据
	$(function() { 
		/* 进入页面就加载数据 */
		lazyLoad()
		pushHistory();
		window.addEventListener("popstate", function(e) {
			WeixinJSBridge.call('closeWindow');
		}, false);
		function pushHistory() {
			var state = {
				title : "title",
				url : "#"
			};
			window.history.pushState(state, "title", "#");
		}
	});
	$(function() {
		$('#equlist').click(function() {
			location.href = "/equipment/list?wolfparam=1";
		})
		$('#historyEarn').click(function() {
			location.href = "/merchant/earningcollecttime";
		})
		$('#icManage').click(function() {
			location.href = "/merchant/onlineCardList";
		})
		$('#originicManage').click(function() {
			location.href = "/ceshiwxpay/ceshipage";
		})
		$('#merManage').click(function() { //会员管理
			location.href = "/merchant/membersystem?source=1";
		})
		$('#areaManage').click(function() { //小区管理
			location.href = "/merchant/areaManage";
		})
		$('#deviceFreeManage').click(function() { //缴费管理
			location.href = "/wxpay/merShowDeviceAndDervice";
		})
		
		$('#equipmentbind').click(function() {
			var pageUrl = window.location.href;
			pageUrl = pageUrl.substring(0,pageUrl.length-1);
			$.ajax({
				url : '${hdpath}/merchant/jssdkWxGet',
				type : "POST",
				data : {pageUrl : pageUrl},
				cache : false,
				success : function(data) {
					var timestamp = data.timestamp;
					timestamp = parseInt(timestamp);
					wx.config({
						debug : false,
						appId : data.appId,
						timestamp : timestamp,
						nonceStr : data.nonceStr,
						signature : data.signature,
						jsApiList : [ 'scanQRCode' ]
					});
					wx.ready(function(){
						wx.scanQRCode({
							needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
							desc: 'scanQRCode desc',
							scanType : [ 'qrCode' , 'barCode' ], // 可以指定扫二维码还是一维码，默认二者都有
							success : function(res) {
								var url = res.resultStr;
								if (url.indexOf("http://www.tengfuchong.com.cn/oauth2pay?code=",0) != -1) {
									var index = url.indexOf("=",0);
									var code = url.substring(index +1);
									window.location.href = "${hdpath}/equipment/allChargePortBind?code=" + code;
								}
							}
						});
					})

					wx.error(function(res) {
						alert("错误：" + res.errMsg);
					});
				},//返回数据填充
			});
			
		});
	})
	var mask = mui.createMask(function(){
		$('.icon_tip_con').fadeOut(200)
	})
	$(function(){
		/* 点击图标显示提示 */
		$('.icon_tip').click(function(){
			$('.icon_tip_con').fadeIn(200)
			mask.show();//显示遮罩
		})
	})
	
function lazyLoad(type){
	if(type == 1){
		$('.contral .icon-gengxin').addClass('Loading') //让刷新按钮转动
	}else{
		$('#loadingToast').show() //大屏转动
	}
	isNowGetData= true
	$.ajax({
		url: '/mobileMerchant/homePageData',
		data: {type: type}, //type: 1的时候刷新页面
		type:'post',
		success: function(res){
			isNowGetData= false
			$('#loadingToast').hide()
			if(res.code == 200 && (res.hasdata== 1 || res.hasdata== 2)){ //hasdata : 1 有汇总数据，0 没有汇总数据
				$('.contral .title').text(res.renewalTime)
				$('.async-loading').each(function(i,item){
					var dataName= $(item).attr('data-name').trim()
					var str= ''
					if(dataName == 'deviceNum' ){
						str= res.onlines+'/'+(res.onlines+res.disline)
					}else if(['totalConsume','todayConsume','yesterdayConsume'].indexOf(dataName) != -1){
						str= (res[dataName] / 100).toFixed(2)
					}else{
						var dDecimal= parseInt($(item).attr('data-decimal'))
						var decimal = isNaN(dDecimal) ? 0 : dDecimal
						str= res[dataName].toFixed(decimal)
					}
					$(item).text(str)
				})
				/* hasdata ===  2  数据过期，重新进行请求 */
				if(res.hasdata === 2){
					/* 触发汇总 */
					var merid= $('body').attr('data-merid')
					if( res.isCalculate == 1 ){
						$.ajax({
							url: '/systemSetting/selfDynamicCollect',
							type: 'post',
							data: {
								type: 1,
								merid: merid,
								pastday: 15
							},
							success: function(res){
								
							}
						})
					}
					$('.contral .icon-gengxin').click()
				}
			}else if(res.hasdata == 0){
				$('.contral .icon-gengxin').click()
			}else if(res.code == 901){
				mui.toast('登录过期，请重新打开页面')
				setTimeout(function(){
					wx.closeWindow()
				},2000)
			}else{
				mui.toast(res.message)
			}	
		},
		error: function(err){
			isNowGetData= false
			$('#loadingToast').hide()
		},
		complete:  function(res){
			if(type == 1){
				$('.contral .icon-gengxin').removeClass('Loading') //让刷新按钮转动
				$('.contral-tip').remove()
			}
		}
	})
}
/* 点击切换隐藏与否 */
$('.contral .icon-yanjing').on('click',function(e){
	var status= $(this).attr('data-status').trim()
	if(status == 'hide'){ //*隐藏
		$('.icon-yen').show() //隐藏第一行的金额元素
		$('header .async-loading').show()
		$('header .icon-wrapper').hide()
		$(this).attr('data-status','show').removeClass('icon-eye').addClass('icon-yanjing')
	}else{//隐藏余额，显示*
	    $('.icon-yen').hide() //隐藏第一行的金额元素
		$('header .async-loading').hide()
		$('header .icon-wrapper').css({display: 'inline-block'})
		$(this).attr('data-status','hide').removeClass('icon-yanjing').addClass('icon-eye')
	}
})
$('.contral .icon-gengxin').on('click',function(e){
	if(!isNowGetData){ //只有 isNowGetData为false时，没有正在获取数据时才能刷新
		lazyLoad(1)
	}else{
		mui.toast('正在获取数据，请勿重复刷新')
	}
})
</script>
</html>
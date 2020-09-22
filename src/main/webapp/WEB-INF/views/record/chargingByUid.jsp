<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>正在充电</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/css/base.css">
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1758951_hml0n5vg62.eot?t=1588572518031'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1758951_hml0n5vg62.eot?t=1588572518031#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAwIAAsAAAAAGBwAAAu4AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCFZAqebJhbATYCJANQCyoABCAFhG0HggobFxQz0i3OSpDsv06gIrcGqZ1rTqNgCG/4krhhLp6oqY84o97Tg4qKigrfX15Hp2OpOyv4S4YPiwe+ud6/k0yyQHlbQBCKEB2hAtJl4etZmOoenxL9r98QbfOe4+l/QEwwHivA6MIZzR6jGLlkWYGrRBeZrEra/HzgP+TFUikz2eEVDGBs4m0N5mHJ5qQdi+1Avc/vYgKab/rVJH3nwqQ5x2gkKjfh+kBXSArcAwQM3q+WNbv7/+TkmKKGFIUKScrpnb0w6Wr78ywpzmdSDPILiVC7hBgkDqFB4cFTKIVRHv/Zz73n2hqQEBnoxt1G++u95xNQN9xUYcGiFRtURUwGrHDi6OG9qpEysaBLUC3kGfeNwkOQqSa3KD14oH58+GZ2qJLIUuyF1hxaeMCc0vfhu2bR7EjHghvtWSweRIqpiITfmVO/SkMxVal61urRK4w2kv5OSalMZGqahhhmmtl2cIjz/Olv6w//KR7v5O1hZwpG83FNrCXXvL3/wCtUZIKOPlqqenQl2qKaBiFHqokW/9X1oRQMJadMhtKkzIaSUtaGUqdsDqWgHDKUCuUwqY6eDBSgpwB1oKeC96mnAbWgZwNVoXcA9UAfAupCn4d0e/oPhtLmUw+KSm+FVTPSQ8QamOpL95mI5cTf5Eug/V02qvt+ZfFckEqkC6KUi6oSib9KLHqKIu82mV+0abbbDmwOPrZZGxXGFqlZt1s0y/r4n4lgEbrOjmU/s7mz2AiMJdRtwggM+5U5PznTPHCBmLWrO4H915jVL29XoJWLWwG9l8aS13cDydUXdt1+8NTrK2uiReQzu34N/1hUTk0eqGl93peIFQeEdMx+joSkG+M6Vjc1g2Msi9gya+ogqwti6GLSM4MKwmq2zov28kAqUVXYgsKt/Wy2ZwW4IfupQkdSZBxJkJdFmudqiKgFIQIDCoUNYAPjcYJmZEE/52KCjVptlkriHmnOSO7K/qkeqcadLi0E2raIofBJH9MEivldm22/eMPYFJnWJ7WJ5BlpY+Y2gsT3xe6bfWf0mTET1+Hc6PdfXmPdBT2vmlEwsV5cbIAYdhUYA/zFaSaO1u2bWsbKs22MZ8ihIewJRNvlEcsD40Fh2HsBFAw0h19mc0h+bmhzzgh9kjdGnqqZk/rLbHlUU5aL8sf1x7mYnemh3B3cY7IzSRCPT5OpJ0i1ZSLlJMWiExovoxFvscnHGCCqPLxa56Q+fRNivdxaqBkBbYJOvRkg/8auHVHA0is7t4J6dz+/1JrOXYyBYdf371Yu27/5uoEWD+7by1+/vjtuwY3n9l6WdrST2D2N4ehsfK42gaI8JNNP7k0nm8nel6iVHZBEN2iHn6PXjyGMalYyJuiLLGlEexihP9RRiJJoljdGH+UsnqwYm76y5HD20u8T8Zb3znFlNOLWdkN1U/Wc4P7Bgbsdd+ACVnlFgmNX87Iq6js7OQnbleLihw1cRIAH1w0Gn5Xlo1uX2Hl2jaIHZFOrq1tU7dVeTjJq5pAK21uXWoJi9a/zstlTR1ebvSvVNNTg+2BwKRggdR+OZXAKLwCr4QeMJ4A3xt/UrikV37Q42/YfDp9JjjjL+zHmfFpz7g26nGFoJmZ122VL3zNt9ynV7YJ0f9pUVzUMOgYhssbIuPhMAAxrQMXqEkV7fNl5dLTjUFA8PJk4NA/O1U3gZW02FUXD1AcL2D+5o7FImEXlX/XAL1fjKC6T4BIZfmLPhce5kQ9Lk2Kep50xaeljW/MpHKNOD6FJ03AMT30cPEoYfnHscakUk8qk4HgNI5ex/3QPshfxTQmaKBanuCYzjDNphwUNrBORMUeY3VbdI46MkGWME/OMIXZnThV0M/3fgd2pBzIrWZ/C7+GTD0i+yQrdHmOI0W33wYIspmaGSRrj8rbAcnwFuFkB89tgwc3WFQhnWBcWtMH8mxUrjmT2aoWF7QpbdxlsX9zWsk3/KrNbAriRBBr+fyWiTaA5dThJS3f1vy4aEHYxTKeixzDIYxHcR5en0uv2bsaqnjKR0tSgNFFxnCryJgLk9GpRexmbRjwdseklz940os2TOjsJmYuNGnGIKbRJQ08lRgZEqrqK2tuj+Wvz6CqVg+58Db26OCbnFd73v4evcn9bTVJ6dJw13iIKESfKi5Y8oyP5lWc/tIlHVoK6wiXx/KxauVaRhiaB+CI6Ipr1gFUapEbn/8b3Z5TOuM/+HSxEZFwcpdUj2Q+0p5i97P0H4ZR7uf0relgG4JXBq/r97F6mc+qehpEZXso4oSlg3qzf11TMX2MfiBs5bzPwnem7GQiHCTP9sk5fyTuefTrDb3c4/0DqEGcu5x3LPuPt7+oz08eIwgTRMGAZfhslCFTkb88BOf0E2+6ED+XpXfW86pwCo1gvNlYzfmFRVZ1636HhQ630bnqrofWIzlWH1O/SQeS/Oh8Tv5Qo5ZsmqvXFqcA0wQRSR/lBlvMBm4iClIIXH9jB3M1iibshunED99Hizdxg9ocVVYVrWbjmj1zuR7P80iB4EGr0N6LP0G5bZuEedBGfJ+MV8dOINH4RT0YZXeLel5ic2Pdo9/v4Y3oPs76d+bHvexD34U6mX/65y/mXBbs4obNwJ3w1jFiI/Mups85eyb9CgA5OotYiJ9xJfTqMNkt5PLDg0cUZNdHCzK8/rTzc82bPsU5yd78+1he71KqoJw7Px0XvewbnUHZ+WHwC4UyyMIJzEk9eV8P54PCRY1iAfbT/iC0wcD46fOCMzhvNWSVy5NXtAwUava2+fRRn58XM1hGN7fJCsL1mrgFYZg1wXnKAvYzBe4E5m3fsGG9WdfNp6qn2XHZP+sdjvi4kygzDjHSlcZhhfIQ/8019oCxMswf25wzD9EvBjrMFbQbYNel9Gu/HGa9WMyrpRsOww1cdofUdakS9wxlA5PQq2Opdh6g/foQuB3/87EL9+bPJoZ8/PiWbXTN8ljtjIX0WfaHSXalSgfOzuxhPyMN+Y/xgCUSIYL+435ugTrvstxfqzwP6XVGAp/Gl51f/ygHpmUC2Vi7KEU1O9KPOyuJ7BKVXUFacYXPUsOXtVuxa0xw7/SFlIbqaqToC8ZhAE9cg00Y//X0G67Pvw2JEMcf67CzqMaJYlnguOyp2B26H74gVxXK43utzYnnFWTrrJv74+vlZSZlJ8+tdUH2/PA7oq2umTJpEaQbzQeT043s5LWJspi9kvLX7w9TrGdvsti/wuo06O6OIbzm7TBMmI/QWkpydsdlpU6DRCKfw3sbuKRRGXN/BCIa/VgKgd4zfUK/IZOPhMDLWUDiErPAY1pNpvkCVuGLj507dqDmrdpIXsAZF9wchOYOYyQw/YSeZxwy1xAD3wJHNR4TAEW2R4dAEx9aP8h8aftWvBz5P/T8t4sZ+Z7D/0F/y8cZVUmGvoT7YL6OKAizC3/CNIgB6LSHLLCvvG7f/RsBb79fXBKzaBFDXm/x+z+x6eAumn3X4D4Sx0TRB50RTDO5+bOFPM2yhNMeQQOv8ZDy4QUAFQiITAJON9SiCZzulwB2lEE+vT23xgKJsfaA0vAApS1kQPCabZ2hhpajIBg3Zn2gcRLTIuzoVp0oN67JiMdmkbW9QmVnVt4xUqVsWEEGSwHhDBtlJqtq4g1rXFKzRKAiFqquDSPdejGxv7yKUqq5WUq6RNGs0ymipVJF1R4m8qwOwpFAhNdDIQfsjNBo0Zgu5Lp0UZ9ZgJb5/MVITrXYNVAVb3I1ZhqSi1nLlhCASgRZ+huq02uKtrK6m0ySYhqwCEayv0qUDIZ0sqZ3XdiEos6/ViiSnIdFcwylFk9KeFLZK0r/pmAKGyhG3wr7RvmaSrKiabpiW7biez9XN3cMzrCTFkLTv2FgD8kbSsW4UJI8BY8Ovg2kkJdz0oBOuB8MNoEOfa4vGb12K7Rj2jb6W2IX4O1S1rnH+/CpgXR8Xphu5Fo4kr5XyQsJeM1VNltfwxOo23Cix+o+H1Ly+QXMUOLPRh7cZaqdZbBGD7KY51KHZ0gkAAAA=') format('woff2'),
  url('//at.alicdn.com/t/font_1758951_hml0n5vg62.woff?t=1588572518031') format('woff'),
  url('//at.alicdn.com/t/font_1758951_hml0n5vg62.ttf?t=1588572518031') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1758951_hml0n5vg62.svg?t=1588572518031#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-anjian:before {
  content: "\e62a";
}

.icon-wodedingdan:before {
  content: "\e679";
}

.icon-tubiaozhizuo-:before {
  content: "\e605";
}

.icon-duankouxinxi:before {
  content: "\e711";
}

.icon-yuanyin:before {
  content: "\e61d";
}

.icon-xiaoquguanli:before {
  content: "\e600";
}

.icon-zhuangtai:before {
  content: "\e61b";
}

.icon-zhuangtai1:before {
  content: "\e632";
}

.icon-zhifufangshi:before {
  content: "\e759";
}

.icon-duankou:before {
  content: "\e609";
}

.icon-dian:before {
  content: "\ec1e";
}

.icon-diannao:before {
  content: "\e602";
}

.icon-bianhao:before {
  content: "\e62d";
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-jiazai:before {
  content: "\e62b";
}

.icon-Prompt:before {
  content: "\e60c";
}

.icon-ico_home_committed:before {
  content: "\e65c";
}

.icon-jine:before {
  content: "\e666";
}

.icon-shichang:before {
  content: "\e62c";
}


body {
	background-color: #f4f4f4;
}
.app {
	font-size: 14px;
	position: absolute;
	left: 0;
	right: 0;
	bottom: 50px;
	top: 0;
	overflow: auto;
}
.title {
	margin-left: 10px;
}
header {
	padding-top: 10px;
	padding-bottom: 15px;
}
header .tip_info {
	text-align: center;
	margin-top: 8px;
	font-size: 16px;
	color: #000;
}
.charge_info {
	margin-bottom: 15px;
}
.charge_info li{
	padding: 12px 10px;
	background-color: #f5f7fa;
	box-shadow: 0 0 5px 1px #ddd;
	border-radius: 6px;
	color: #333;
	margin: 0 10px 15px 10px;
}
.charge_info li:last-child {
	margin-bottom: 0;
}
.charge_info .charge_item {
	display: flex;
	justify-content: space-between;
	line-height: 1.8em;
}
.charge_info .charge_item span:first-child i{
	margin-right: 5px;
	color: #22B14C;
	font-size: 14px;
}
.charge_info .charge_item span i.icon-Prompt{
	color: #dc8f21;
	margin-left: 3px;
}

.charge_info .charge_item span:last-child{
	color: #777;
}
.charge_info .charge_handle {
	margin-top: 12px;
	display: flex;
	justify-content: space-around;
}
.charge_info .charge_handle button {
	font-size: 13px;
	padding: 3px 12px;
	min-width: 20%;
}
.change_list {
	padding-bottom: 25px;
}
.change_list li a{
	padding: 10px;
	font-size: 12px;
	display: flex;
	flex-wrap: wrap;
	margin: 0 10px 15px 10px;
	border-radius: 6px;
	background-color: #f5f7fa;
	color: #333;
	border: 1px solid #d1e0d5;
	position: relative;
}
.change_list li:active {
	background-color: #ececec;
}
.change_list li a>div {
	min-width: 50%;
	line-height: 2em;
}
.change_list li a>div>span:first-child i{
	margin-right: 5px;
	font-size: 14px;
	color: #22B14C;
}
.change_list li a>div>span:last-child {
	color: #777;
}
.change_list .mui-icon-arrowright {
	position: absolute;
	right: 10px;
	top: 50%;
	transform: translateY(-50%);
	color: #999;
	font-size: 20px;
}
</style>
<style>
@keyframes rotateAndUp {
	0% {
		margin-top: -100px;
	}
	100% {
		margin-top: -215px;
		transform: rotate(360deg);
	}
}
@keyframes charge {
	0% {
		margin-top: 100px;
		background-color: red;
	}
	50% {
		margin-top: 25px;
		background-color: orange;
	}
	100% {
		margin-top: -50px;
		background-color: limegreen;
	}
}
#battery1 {
	width: 65px;
	height: 100px;
	margin: 0 auto;
	position: relative;

}
#battery1 .border-shadow {
	border: lightgrey 2px solid;
	box-shadow: 0 0 10px lightgrey;
}

#battery1 .battery-anode {
    width: 20px;
    height: 10px;
    border-radius: 5px 5px 0 0;
    border: lightgrey 2px solid;
    box-shadow: 0 0 10px lightgrey;
    border-bottom: 0;
    position: absolute;
    top: -10px;
    left: 23px;
}
#battery1 .battery-body {
	height: 100px;
	border-radius: 15px;
	margin-top: 15px;
	overflow: hidden;
	position: relative;
}
#battery1 .charge {
	width: 100px;
	height: 200px;
	animation: charge 8s linear infinite;
}
		
#battery1 .wave {
	opacity: 0.9;
	width: 200px;
	height: 200px;
	border-radius: 65px;
	background-color: white;
	position: absolute;
	left: -50px;
	animation: rotateAndUp 8s linear infinite;
}
.loading,.noData {
	height: 35px;
	line-height: 35px;
	font-size: 14px;
	color: #999;
	text-align: center;
	margin-top: -10px;
}
.loading i {
	margin-left: 8px;
	color: #666;
	display: inline-block;
	animation: Load 1.5s linear infinite;
	-webkit-animation: Load 1.5s linear infinite;
	-moz-animation: Load 1.5s linear infinite;
	-o-animation: Load 1.5s linear infinite;
}
.chargeTimeLoad {
    position: absolute;
	right: 95px;
	visibility: hidden;
	color: #22B14C;
}
#wolful li span.predict {
	position: static;
}
.loadClass{
	visibility: visible;
	animation: Load 1.5s linear infinite;
	-webkit-animation: Load 1.5s linear infinite;
	-moz-animation: Load 1.5s linear infinite;
	-o-animation: Load 1.5s linear infinite;
}
@keyframes Load {
	from {
		transform: rotate(0deg);
	}
	to {
		transform: rotate(360deg);
	}
}
.charge_img {
	width: 94px;
	height: 100px;
	display: block;
	margin: 0 auto;
}
.tip_info span {
	display: flex;
	justify-content: center;
}
</style>
</head>
<body>
	<div class="app">
		<div class="content">
			<header>
				<c:choose>
					<c:when test="${errormsg == 1}">
						<div class=tip_info>
							暂无充电信息
						</div>
					</c:when>
					<c:otherwise>
						<img class="charge_img" src="${hdpath }/images/charging1.png" alt="充电">
						<div class=tip_info>
							<span>正在充电</span>
						</div>
					</c:otherwise>
				</c:choose>
			</header>
			<main>
				<c:if test="${fn:length(chargingList) > 0}">
					<p class="title">正在充电信息</p>
					<ul class="charge_info">
						<c:forEach items="${chargingList}" var="charging">
							<li data-id="${charging.id }" data-code="${charging.equipmentnum }" data-port="${charging.port }">
								<div class="charge_item"><span><i class="iconfont icon-wodedingdan"></i>订单编号</span><span>${charging.ordernum }</span></div>
								<div class="charge_item"><span><i class="iconfont icon-diannao"></i>机器编号</span><span>${charging.equipmentnum }</span></div>
								<div class="charge_item"><span><i class="iconfont icon-zhuangtai1"></i>端口号码</span><span>${charging.port }</span></div>
								<div class="charge_item"><span><i class="iconfont icon-consumption"></i>付款金额</span><span>${charging.totalMoney }元</span></div>
								<div class="charge_item"><span><i class="iconfont icon-shichang"></i>预计剩余时间<i class="iconfont icon-jiazai chargeTimeLoad"></i></span>
									<span class="predict">
										<c:if test="${charging.allPortStatus.predict==charging.durationtime}">— —</c:if>
										<c:if test="${charging.allPortStatus.predict!=charging.durationtime}">${charging.allPortStatus.predict}分钟</c:if>
										<i class="iconfont icon-Prompt"></i>
									</span>
								</div>
								<div class="charge_item"><span><i class="iconfont icon-ico_home_committed"></i>充电信息更新</span>
									<span class="updatetimeid">${charging.allPortStatus.updateTime }</span>
								</div>
								<div class="charge_handle">
									<button type="button" class="mui-btn mui-btn-success updateBtn" data-id="${charging.id }">更新</button>
									<c:if test="${charging.paytype ==1 || charging.paytype ==2}">
										<button type="button" class="mui-btn mui-btn-warning moreBtn" data-id="${charging.id }">续充</button>
									</c:if>
									<button type="button" class="mui-btn mui-btn-danger closeBtn" data-id="${charging.id }">断电</button>
								</div>
							</li>
						</c:forEach>
					</ul>
				</c:if>
				<p class="title">历史充电记录</p>
				<div class="change_list">
					<ul>
						<c:forEach items="${chargedList}" var="charge">
							<li>
								<a style="text-decoration: none;"  href="${hdpath}/charge/chargeRecordOne?id=${charge.id}" > 
									<div><span><i class="iconfont icon-zhuangtai1"></i>订单编号：</span>
										<span>${charge.ordernum}</span>
									</div>
									<div><span><i class="iconfont icon-zhifufangshi"></i>支付方式：</span>
										<span>${charge.paytype == 1 ? '钱包支付' : charge.paytype == 2 ? '微信支付' : charge.paytype == 3 ? '支付宝支付' : charge.paytype == 4 ? '包月下发数据' : charge.paytype == 5 ? '投币' : charge.paytype == 6 ? '离线卡支付' : charge.paytype == 7 ? '在线卡支付' : '— —'}</span>
									</div>
									<div><span><i class="iconfont icon-consumption"></i>支付金额：</span>
										<span>${charge.expenditure}元</span>
									</div>
						            <div><span><i class="iconfont icon-duankou"></i>设备信息：</span>
						            	<span>${charge.equipmentnum}-${charge.port}</span>
						            </div>
						            <div><span><i class="iconfont icon-zhuangtai"></i>订单状态：</span>
						            	<span>
						            		<c:choose>
						            			<c:when test="${charge.number == 0}">正常</c:when>
						            			<c:when test="${charge.number == 1}">全额退款</c:when>
						            			<c:when test="${charge.number == 2}">部分退款（${charge.refundMoney}元）</c:when>
						            		</c:choose>
						            	</span>
						            </div>
									<div><span><i class="iconfont icon-yuanyin"></i>结束原因：</span>
										<span>${charge.resultinfo==0?"充电完成":charge.resultinfo==1?"插头松动或已拔掉":charge.resultinfo==2?"充满": charge.resultinfo==3?"超功率自停":charge.resultinfo==4?"远程断电" :charge.resultinfo==5?"刷卡断电" :charge.resultinfo==11?"被迫停止":charge.resultinfo==255?"日志结束":"— —"}</span>
									</div>
									<div><span><i class="iconfont icon-ico_home_committed"></i>开始时间：</span>
										<span><fmt:formatDate value="${charge.begintime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
									</div>
									<div><span><i class="iconfont icon-ico_home_committed"></i>结束时间：</span>
										<span><fmt:formatDate value="${charge.endtime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
									</div>
									<i class="mui-icon mui-icon-arrowright"></i>
								</a>
							</li>
						</c:forEach>
					</ul>
					<c:if test="${fn:length(chargedList)>0 && fn:length(chargedList)< 10}"><div class="noData">暂无更多数据</div></c:if>
				</div>
			</main>
		</div>
	</div>
<script>
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
			fmt= RegExp.$1.length==2 ?  fmt.replace(regKey,('00'+o[key]).substr(-2)) : fmt.replace(regKey,o[key]+'');
		}
	}
	
	return fmt;
}
$(function(){
	/* 上啦加载 */
	var currentPage= 2 //请求页码
	var flag= false //是否到底部
	var totalRows= 10 //页面总条数
	var isHasData= true //是否还有数据
	var uid= ${uid}
	isHasData= $('.change_list li').length <10 ? false : true
	var obj= {
		url: '/charge/chargeRecordInfo',
		error: function(err){
		}
	}
	scrollFn($('.app')[0],$('.content')[0],0.98,obj)
		
	/**
	@parames ele <Dom> 滚动父元素
	@parames cEle <Dom> 滚动子元素
	@parames rate <Number> 比值，当大于这个比值时请求数据 [0,1]
	@parames obj <Object> {url:String,success:Function,error:Function} //ajax数据
	*/
	function scrollFn(ele,cEle,rate,obj){
		$(ele).scroll(function(e){
			var cEleHeight= $(cEle).height()
			var target= e.srcElement || e.target
			var targetHeight= $(target).height()
			if(!isHasData){
				return 
			}
			if(($(ele)[0].scrollTop+targetHeight)/cEleHeight >= rate && !flag){
				var divLi= $('<div class="loading"><span>正在加载</span><i class="iconfont icon-jiazai"></i></div>')[0]
				$('.change_list')[0].appendChild(divLi)
				$.ajax({
					url: obj.url,
					data:{
						uid: uid,
						offset: 10,
						page: currentPage
					},
					type: 'post',
					success: function(res){
						var Fragmeng = document.createDocumentFragment();
						$(res.list).each(function(i,item){
							var payType= item.paytype == 1 ? '钱包支付' : item.paytype == 2 ? '微信支付' : item.paytype == 3 ? '支付宝支付' : item.paytype == 4 ? '包月下发数据' : item.paytype == 5 ? '投币' : item.paytype == 6 ? '离线卡支付' : item.paytype == 7 ? '在线卡支付' : '— —'
								var expenditure= item.expenditure.toFixed(1)
								var orderStatus= item.number == 0 ? '正常' : item.number == 1 ? '全额退款' :  item.number == 2 ? ('部分退款（'+item.refundMoney+'）') : '— —'
								var resuleInfo = item.resultinfo==0 ? "充电完成" : item.resultinfo==1 ? "空载断电" : item.resultinfo==2 ? "充满" : item.resultinfo==3 ? "超功率自停" : item.resultinfo==4 ? "远程断电" : item.resultinfo==5 ? "刷卡断电" : item.resultinfo==11 ? "被迫停止" : item.resultinfo==255 ? "日志结束" : "— —";
								var str='<li><a style="text-decoration: none;" href="/charge/chargeRecordOne?id='+item.id+'"><div><span><i class="iconfont icon-zhuangtai1"></i>订单编号：</span><span>'+item.ordernum+'</span></div><div><span><i class="iconfont icon-zhifufangshi"></i>支付方式：</span><span>'+payType+'</span></div><div><span><i class="iconfont icon-consumption"></i>支付金额：</span><span>'+expenditure+'元</span></div><div><span><i class="iconfont icon-duankou"></i>设备信息：</span><span>'+item.equipmentnum+'-'+item.port+'</span></div><div><span><i class="iconfont icon-zhuangtai"></i>订单状态：</span><span>'+orderStatus+'</span> </div><div><span><i class="iconfont icon-yuanyin"></i>结束原因：</span><span>'+resuleInfo+'</span></div><div><span><i class="iconfont icon-ico_home_committed"></i>开始时间：</span><span>'+item.begintimes+'</span></div><div><span><i class="iconfont icon-ico_home_committed"></i>结束时间：</span><span>'+item.endtimes+'</span></div><i class="mui-icon mui-icon-arrowright"></i></a></li>'
							Fragmeng.appendChild($(str)[0]);
						})
						$('.change_list ul')[0].appendChild($(Fragmeng)[0]);	
						flag= false ;
						currentPage++
						totalRows+=10;
						if(res.totalRows<=totalRows){
							$('.change_list')[0].appendChild($('<div class="noData">暂无更多数据</div>')[0]);
							isHasData= false
						}
					},
					error: obj.error,
					complete:function(){
						$('.change_list .loading').remove()
					}
				})
				flag= true
			}
		})
	}
/* 更新充电信息 */
$(".updateBtn").click(function() {
	var parentLi= $(this).parents('li')
	$.bootstrapLoading.start({
		loadingTips : "正在更新数据..."
	});
	$.ajax({
		url : '${hdpath}/charge/updateChargeData',
		data : {
			code : parentLi.attr('data-code').trim(),
			port : parentLi.attr('data-port').trim()
		},
		type : "POST",
		cache : false,
		success : function(res) {
			if(res.wolfcode == "1001") {
				mui.toast('更新失败',{duration: 1500});
				parentLi.find('.predict').html('— —')
			}else{
				parentLi.find('.predict').html(res.time+'分钟<i class="iconfont icon-Prompt"></i>');
				parentLi.find('.updatetimeid').text(new Date(parseInt(res.updatetime)).Formit('YYYY-MM-DD hh:mm:ss'))
			}
		}.bind(this),
		error: function(){
			parentLi.find('.predict').text('— —')
		},
		complete : function() {
			$.bootstrapLoading.end();
		}
	});
})
/* 续充 */
$('.moreBtn').click(function(){
	var id= $(this).attr('data-id').trim()
	window.location.href= '/charge/chargeContinue?chargeid='+id
})
/* 断电 */
$('.closeBtn').click(function(){
	var parentLi= $(this).parents('li')
	var id= $(this).attr('data-id').trim()
	var codeval = parentLi.attr('data-code').trim()
	var portval = parentLi.attr('data-port').trim()
	var btnArray = ['确认', '取消'];
	mui.confirm('确认断开电源吗 ？', '充电中...', btnArray, function(e) {
		if (e.index == 0) {
			$.bootstrapLoading.start({
				loadingTips : "断电中..."
			});
			$.ajax({
				url : '${hdpath}/charge/remoteOutage',
				data : {
					code : codeval,
					port : portval,
					chargeid : id
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.status == "1") {
						window.location.reload();
					} else if (data.status == "0") {
						mui.alert(data.errinfo);
					}
				},
				complete : function() {
					$.bootstrapLoading.end();
				}
			});
		} else {
			return ;
		}
	})
})
$('.app').click(function(even){
	even= even ||window.event
	var target = even.target || even.srcElement
	if($(target).hasClass('icon-Prompt')){
		mui.alert('充电时间依据电动车充电器功率智能动态计算，仅供参考，以实际为准。','充电时间计算说明','我知道了')
	}
})

/* 实现进入局部刷新时间 */
if($('.charge_info li').length> 0){ 
	var arr= []
	$('.charge_info li').each(function(i,item){
		var obj= {}
		obj.code= $(item).attr('data-code')
		obj.port= $(item).attr('data-port')
		arr.push(obj)
	})
	var k= 0
	openPageUpdataTime(k,arr[k].code,arr[k].port,handleCallBack)
	function handleCallBack(lastK){ //成功之后的回调
		$('.chargeTimeLoad').eq(lastK).removeClass('loadClass')
		k++
		if(k< arr.length){ //判断数据中的充电信息长度是否大于当前下一次即将循环的索引k、如果大于则继续请求
			openPageUpdataTime(k,arr[k].code,arr[k].port,handleCallBack)
		}else{
			console.log('没有数据了')
		} 
	}
}

//k、当前请求数组的索引，设备号、端口号，回调
function openPageUpdataTime(k,code,port,callback){ //进入页面更新时间
	$('.chargeTimeLoad').eq(k).addClass('loadClass')
	$.ajax({
		url : '${hdpath}/charge/updateChargeData',
		data : {
			code : code,
			port : port
		},
		type : "POST",
		cache : false,
		success : function(res) {
			console.log(res)
			if(res.wolfcode == "1001") {
				mui.toast('更新失败',{duration: 1500});
				$('.charge_info li').eq(k).find('.predict').html('— —')
			}else{
				$('.charge_info li').eq(k).find('.predict').html(res.time+'分钟<i class="iconfont icon-Prompt"></i>')
				$('.charge_info li').eq(k).find('.updatetimeid').html(new Date(parseInt(res.updatetime)).Formit('YYYY-MM-DD hh:mm:ss'))
			}
			
		},
		error: function(){
			$('.charge_info li').eq(k).find('.predict').html('— —')
		},
		complete : function() {
			callback && callback(k)
		}
	});
}


})
</script>
<%@ include file="/WEB-INF/views/public/generalBtnNav.jsp"%>
</body>
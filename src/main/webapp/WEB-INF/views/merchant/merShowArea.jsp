<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>商户缴费列表</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/css/base.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script src="${hdpath}/js/jquery.js"></script>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1238088_untj044ww2.eot?t=1564646949015'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1238088_untj044ww2.eot?t=1564646949015#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1238088_untj044ww2.woff?t=1564646949015') format('woff'),
  url('//at.alicdn.com/t/font_1238088_untj044ww2.ttf?t=1564646949015') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1238088_untj044ww2.svg?t=1564646949015#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 0.6826666rem;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-qiehuan:before {
  content: "\e62a";
}

.icon-xingming:before {
  content: "\e631";
}

.icon-weibiaoti16:before {
  content: "\e622";
}

.icon-consumption:before {
  content: "\e6f1";
}

.icon-xiala:before {
  content: "\e654";
}

.icon-tishi:before {
  content: "\e661";
}

.icon-dianhua:before {
  content: "\e729";
}

.icon-yinhang:before {
  content: "\e66e";
}

.icon-yinhangqia:before {
  content: "\e690";
}

.icon-yingbi:before {
  content: "\e632";
}

.icon-icon:before {
  content: "\e611";
}

.icon-shijian:before {
  content: "\e681";
}

.icon-tongji:before {
  content: "\e61b";
}

.icon-shouxufei:before {
  content: "\e62c";
}

.icon-bianhao:before {
  content: "\e62b";
}

.icon-shouxufei-copy:before {
  content: "\e72a";
}


/* p {
margin-bottom: 0 !important;
}
body {
font-size: 12px;
color: #666;
}
header h3 {
text-align: center;
line-height: 40px;
font-size: 14px;
margin: 0;
}
.list {
padding: 0 10px;
color: #666;
font-size: 12px;
position: absolute;
top: 0;
overflow: auto;
bottom: 45px;
left: 0;
right: 0;
}
.list h3 {
font-size: 14px;
color: #22b14c;
margin-bottom: 8px;
}
.list ul li {
border: 1px solid #22B14C;
overflow: hidden;
padding:10px;
border-radius: 5px;
background-color: #f8f8f8;
margin-bottom: 15px;
box-shadow: 5px 5px 5px #aaa;
}

.list ul li .listDiv div{
min-width: 50%;
float: left;	
}

.list ul li .listDiv div span {
line-height: 30px;
}
.list ul li .listDiv div .title{
color: #999;
}
.list ul li .listDiv div span i {
color: #22B14C;
font-size: 14px;
margin-right: 5px;
}
.list ul li .listDiv div:last-child {
width: 100%;
display: flex;
justify-content: space-between;
}
.list ul li .listDiv div a {
display: block;
width: 35%;
height: 30px;
line-height: 30px;
border: 1px solid #22B14C;
overflow: hidden;
text-align: center;
color: #22B14C;
border-radius: 30px;
margin: auto;
text-decoration: none;
}
.list ul li .listDiv >a:active {
color: #fff;
background-color: #22B14C;
}
.mui-bar-tab {
height: 49px;
display: flex;
align-items: center;
justify-content: center;
box-shadow: none;
background-color: #f7f7f7;
border-top: 1px solid #ccc;
box-sizing: border-box;
z-index: 999;
}
.mui-bar-tab a {
font-size: 14px;
display: block;
width: 80%;
height: 37px;
line-height: 37px;
border: 1px solid #22B14C;
text-align: center;
color: #fff;
border-radius: 30px;
text-decoration: none;
background-color: #22B14C;
}
.mui-bar-tab a:active {
color: #22B14C;
background-color: #fff;
}

.areaBelongInfo2 {
	padding: 0 10px;
	display: none;
}
.areaBelongInfo2 input::-webkit-input-placeholder{
	font-size: 12px;
	color: #999;
}
.areaBelongInfo2 input:-moz-placeholder{
	font-size: 12px;
	color: #999;
}
.areaBelongInfo2 input::-moz-placeholder{
	font-size: 12px;
	color: #999;
}
.areaBelongInfo2 input:-ms-input-placeholder {
	font-size: 12px;
	color: #999;
}
.areaBelongInfo2 .formBottom {
	display: flex;
	align-items: center;
	justify-content: space-around;
}
.areaBelongInfo2 .formBottom button {
	width: 30%;
	border-radius: 8px;
	font-size: 12px;
}
.areaBelongInfo2 form {
	padding: 10px 0;
	border-radius: 8px;
	overflow: hidden;
}
.areaBelongInfo2 .rate {
	line-height: 40px;
}
.areaBelongInfo2 .rate .symbol {
	float: right;
	display: block;
	width: 20%;
	text-align: center;
	font-size: 14px;
}
.areaBelongInfo2 .rate input {
	width: 45%;
	float: left;
}
.areaBelongInfo2 .toast {
	margin-top: 20px;
	text-align: center;
	color: #999;
}
.areaBelongInfo2 h5 {
	text-align: center;
	padding: 10px 0;
}
.title {
	font-size: 16px;
	color: #666;

}
.tip_area {
	font-size: 16px;
	text-align: center;
	margin-top: 15vh;
	color: #999;
} */

/* 转化之后的样式 */

p {
     margin-bottom: 0 !important;
}
 body {
     font-size: 0.5217391304347826rem;
     color: #666;
}
 header h3 {
     text-align: center;
     line-height: 1.7391304347826086rem;
     font-size: 0.6086956521739131rem;
     margin: 0;
}
 .list {
     padding: 0 0.43478260869565216rem;
     color: #666;
     font-size: 0.5217391304347826rem;
     position: absolute;
     top: 0;
     overflow: auto;
     bottom: 1.9565217391304348rem;
     left: 0;
     right: 0;
}
 .list h3 {
     font-size: 0.6086956521739131rem;
     color: #22b14c;
    /*color: #b12222;
     */
     margin-bottom: 0.34782608695652173rem;
}
 .list ul li {
     border: 1px solid #22B14C;
     overflow: hidden;
     padding:0.43478260869565216rem;
     border-radius: 0.21739130434782608rem;
     background-color: #f8f8f8;
     margin-bottom: 0.6521739130434783rem;
     box-shadow: 0.21739130434782608rem 0.21739130434782608rem 0.21739130434782608rem #aaa;
}
 .list ul li .listDiv div{
     min-width: 50%;
     float: left;
}
 .list ul li .listDiv div span {
     line-height: 1.3043478260869565rem;
}
 .list ul li .listDiv div .title{
     color: #999;
}
 .list ul li .listDiv div span i {
     color: #22B14C;
     font-size: 0.6086956521739131rem;
     margin-right: 0.21739130434782608rem;
}
 .list ul li .listDiv div:last-child {
     width: 100%;
     display: flex;
     justify-content: space-between;
}
 .list ul li .listDiv div a {
     display: block;
     width: 35%;
     height: 1.3043478260869565rem;
     line-height: 1.3043478260869565rem;
     border: 1px solid #22B14C;
     overflow: hidden;
     text-align: center;
     color: #22B14C;
     border-radius: 1.3043478260869565rem;
     margin: auto;
     text-decoration: none;
}
 .list ul li .listDiv >a:active {
     color: #fff;
     background-color: #22B14C;
}
 .mui-bar-tab {
     height: 2.130434782608696rem;
     display: flex;
     align-items: center;
     justify-content: center;
     box-shadow: none;
     background-color: #f7f7f7;
     border-top: 0.043478260869565216rem solid #ccc;
     box-sizing: border-box;
     z-index: 999;
}
 .mui-bar-tab a {
     font-size: 0.6086956521739131rem;
     display: block;
     width: 80%;
     height: 1.608695652173913rem;
     line-height: 1.608695652173913rem;
     border: 1px solid #22B14C;
     text-align: center;
     color: #fff;
     border-radius: 1.3043478260869565rem;
     text-decoration: none;
     background-color: #22B14C;
}
 .mui-bar-tab a:active {
     color: #22B14C;
     background-color: #fff;
}
/*areaBelongInfo2��ʼ*/
 .areaBelongInfo2 {
     padding: 0 0.43478260869565216rem;
     display: none;
}
 .areaBelongInfo2 input::-webkit-input-placeholder{
     font-size: 0.5217391304347826rem;
     color: #999;
}
 .areaBelongInfo2 input:-moz-placeholder{
     font-size: 0.5217391304347826rem;
     color: #999;
}
 .areaBelongInfo2 input::-moz-placeholder{
     font-size: 0.5217391304347826rem;
     color: #999;
}
 .areaBelongInfo2 input:-ms-input-placeholder {
     font-size: 0.5217391304347826rem;
     color: #999;
}
 .areaBelongInfo2 .formBottom {
     display: flex;
     align-items: center;
     justify-content: space-around;
}
 .areaBelongInfo2 .formBottom button {
     width: 30%;
     border-radius: 0.34782608695652173rem;
     font-size: 0.5217391304347826rem;
}
 .areaBelongInfo2 form {
     padding: 0.43478260869565216rem 0;
     border-radius: 0.34782608695652173rem;
     overflow: hidden;
}
 .areaBelongInfo2 .rate {
     line-height: 1.7391304347826086rem;
}
 .areaBelongInfo2 .rate .symbol {
     float: right;
     display: block;
     width: 20%;
     text-align: center;
     font-size: 0.6086956521739131rem;
}
 .areaBelongInfo2 .rate input {
     width: 45%;
     float: left;
}
 .areaBelongInfo2 .toast {
     margin-top: 0.8695652173913043rem;
     text-align: center;
     color: #999;
}
 .areaBelongInfo2 h5 {
     text-align: center;
     padding: 0.43478260869565216rem 0;
}
 .title {
     font-size: 0.6956521739130435rem;
     color: #666;
}
 .tip_area {
     font-size: 0.6956521739130435rem;
     text-align: center;
     margin-top: 15vh;
     color: #999;
}
 
</style>
</head>
<body>
<div class="areaManage">
		<div class="list">
			<ul>
				<c:if test="${fn:length(areaInfo) > 0 }">
					<c:forEach items="${areaInfo}" var="areaItem">
							<li>
								<h3>小区名称：
									<c:if test="${areaItem.name == null }">--</c:if>
									<c:if test="${areaItem.name != null }">${areaItem.name}</c:if>
								</h3>
								<div class="listDiv clearfix"> 
									<div><span><i class="iconfont icon-bianhao"></i></span>设备数量：<span>${areaItem.equcount}台</span></div>
									<div><span><i class="iconfont icon-bianhao"></i></span>合伙人数：<span>${areaItem.manid}人</span></div>
									<div><span><i class="iconfont icon-bianhao"></i></span>已到期设备数：<span>${areaItem.expiredEquNum}台</span></div>
									<div><span><i class="iconfont icon-bianhao"></i></span>即将到期设备数：<span>${areaItem.almostExEquNum}台</span></div>
									<div>
										<a href="/wxpay/merShowAreaDevice?areaId=${areaItem.id}">缴费</a>
									</div>
								</div>
							</li>
					  </c:forEach>	
				</c:if>
				<c:if test="${fn:length(areaInfo) <= 0 }">
					<div class="tip_area">暂无小区</div>
				</c:if>
			</ul>
		</div>
	</div>
	<nav class="mui-bar mui-bar-tab">
		<a href="/merchant/merShowDevice" id="addArea">为未绑定小区设备缴费</a>
	</nav>
	<script>
		var htmlwidth = document.documentElement.clientWidth || document.body.clientWidth;
	    var fontSize= htmlwidth/16
	    var style= document.createElement('style')
	    style.innerHTML= 'html { font-size: '+fontSize+'px !important;}'
	    var head= document.getElementsByTagName('head')[0]
	    head.insertBefore(style,head.children[0])
	</script>
</body>
</html>
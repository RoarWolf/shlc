<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>包月消费记录</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1297828_ivy5xhwzvgh.eot?t=1563269929566'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1297828_ivy5xhwzvgh.eot?t=1563269929566#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,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') format('woff2'),
  url('//at.alicdn.com/t/font_1297828_ivy5xhwzvgh.woff?t=1563269929566') format('woff'),
  url('//at.alicdn.com/t/font_1297828_ivy5xhwzvgh.ttf?t=1563269929566') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1297828_ivy5xhwzvgh.svg?t=1563269929566#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-shezhi:before {
  content: "\e600";
}

.icon-laiyuan2:before {
  content: "\e77a";
}

.icon-consumption:before {
  content: "\e6f1";
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
		* {
			padding: 0;
			margin: 0;
		}
		li {
			list-style: none;
		}
		body {
			background-color: #efeff4;
		}
		header h3 {
			text-align: center;
			padding: 5px 0 10px;
			font-size: 14px;
		}
		.noData {
			position: absolute;
			left: 0;
			top: 40px;
			right: 0;
			bottom: 0;
			text-align: center;
			margin-top: 50%;
			color: #999;
			font-size: 14px;
		}
		.userDetail {
			padding: 15px 10px;
			color: #666;
			font-size: 12px;
		}
		.userDetail ul li {
			border: 1px solid #ccc;
			overflow: hidden;
			padding:10px;
			border-radius: 5px;
			background-color: #f8f8f8;
			margin-bottom: 15px;
		}
		.userDetail ul li:active {
			background-color: #EfEfEf;
		}
		.userDetail ul li div{
			min-width: 50%;
			float: left;	
		}
		.userDetail ul li div:nth-child(1){
			width: 100%;
		}
		
		.userDetail ul li div span {
			/*display: block;*/
			line-height: 30px;
			text-align: center;
		}
		.userDetail ul li div .title{
			color: #999;
		}
		.userDetail ul li div span i {
			color: #22B14C;
			font-size: 15px;
			margin-right: 5px;
		}
		
		.spanJust {
			color: #22B14C !important;
		}
		.spanNegative{
			color: #F47378 !important;
		}
</style>
</head>
<body>
	<div class="userDetail">
		<header>
			<h3>包月消费</h3>
		</header>
		<ul>
		<c:forEach items="${monthList}" var="consume">
			<li>
				<div class="left">
					<span class="title"><i class="iconfont icon-icon" style="font-size: 14px;"></i>订单号：</span>
					<span>${consume.ordernum}</span>
				</div>
				<c:if test="${consume.paysource == 1}">
					<div class="right">
						<span class="title"><i class="iconfont icon-consumption" style="font-size: 16px;"></i>金额：</span>
						<span>${consume.money}</span>
						<strong>元</strong>
					</div>
				</c:if>
				<div class="left">
					<span class="title"><i class="iconfont icon-laiyuan2" style="font-size: 16px;"></i>支付来源：</span>
					<c:if test="${consume.paysource == 1}"><span>支付开通包月</span></c:if>
					<c:if test="${consume.paysource == 2}"><span>包月充电</span></c:if>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-shezhi"></i>变动次数 ：</span>
					<c:if test="${consume.paysource == 1 && consume.status == 1}">
						<span class=spanJust>+${consume.changenum}</span>
					</c:if>
					<c:if test="${consume.paysource == 1 && consume.status == 2}">
						<span class="spanNegative">-${consume.changenum}</span>
					</c:if>
					<c:if test="${consume.paysource == 2 && consume.status == 1}">
						<span class="spanNegative">-${consume.changenum}</span>
					</c:if>
					<c:if test="${consume.paysource == 2 && consume.status == 2}">
						<span class="spanJust">+${consume.changenum}</span>
					</c:if>
					<strong>次</strong>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-tongji"></i>剩余次数：</span>
					<span>${consume.changenum == 0 ? '不限' : consume.surpnum == -1 ? '不限' : consume.surpnum}</span>
					<strong>次</strong>
				</div>
				<div class="left">
					<span class="title"><i class="iconfont icon-shijian" style="font-size: 18px;"></i>记录时间：</span>
					<span><fmt:formatDate value="${consume.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</div>			
			</li>
		</c:forEach>
		</ul>
	</div>
</body>
</html>
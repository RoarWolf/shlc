<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<title>余额明细</title>
<style>
.flex_sp {
	display: flex;
	justify-content:space-between;
	padding-top: 5px;
	font-size: 13px;
}
</style>
<style>
@font-face {font-family: "iconfont";
  src: url('//at.alicdn.com/t/font_1899025_it5td6j37vc.eot?t=1592792557555'); /* IE9 */
  src: url('//at.alicdn.com/t/font_1899025_it5td6j37vc.eot?t=1592792557555#iefix') format('embedded-opentype'), /* IE6-IE8 */
  url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAR0AAsAAAAACRAAAAQlAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDMgqFLIRUATYCJAMUCwwABCAFhG0HTBvkB8iusCnDvVBDQAFl4QIWUPbwuePnQ+9lbzKTbTrJtk7PKMWTUPDrJC/OX5C2pqrMgYJ1PBsaJd2TVvfWe0/Ci0pgAS8uD5+397d/M5kFkkTWXUkXRJSMLeBJtQeYJ1Fr+Sla/D/37rU5wea1LJc1hy0teifAaAIFNMb0LA+kQPqi/B/gGbaDNbobl4EAuC7skS3y7g36GJlFAOSq0Qj6JRvWwyYko6ZlY4K8EElloXAVsDz+PvmxhAQIxApZmHycq3CY9s+n0Bn/Iz9EQb4/D4DRNoAC7AEMyNPqG6KV5uxRfPF3bcEAnEQgTPtnwc8qn0/5/5+l6ooMXP/hAQwhoEAaRKcQPjjP4NnSl+X6A1pAUC9RCAI8r/V4vlhxAwdoAZcB6RBaL1UUI2BNT1MVrZdirVgqSl4WiXdVCvpqxq/7tMS3sWfbda7T2dQaBqy/2mu/rluL3mbNmhlrZe3iupm1qoOi06Xr9Vl6Y5bBkL547Yw1nEMGlI044CyqVQmPz6yzkxElD8isbbSzq1tsm5GFz1lXb5OO6nuSrn+aYXyeZXhWUM8XqaW1jYTWbVm8CSgZDYP3TskwLsnhtbWEzKxbvBaQxxGQhdtFJhgMmQaJrUqnC2/M8SzWqzh0H5YPi/YDGRTVMr8xSu4n+FeSzi1fHxXWsUxeW9Y+8qdDn4uYdw8MSWvOOMfPmxaanufnZrQtKvWed5MsZw0/30130Y9DaQPm0r86rPyz/ad4dfBS/qp/d3p5yH/bveWeM3t7r56B5vHhaTPLcr7tvPAAPXaeak7Hp1yN0HVP/WOcs0vsoswvRNIcLT46pOxXNRmlTp19WtaYh3UJs/hARusb8E+WNGbNIiUkDiWBio/urawoPmyyQnqp3pOGj/B/1Jbl7DLTalF9mZGXqUrZYzlb2LlTIPacnTvYwo4dS2fv2IkJAMD/6RHqF/PR7bSCfZOmFqKPaOEf5zdga+vlInP/r5LMAAAPFp5vMfofw3ayb18Ps/xvIjci2FIpdjHntb5s7hdVFmwQFi0c8OcIWXm8H4OtuRQgJLY5CEx0BkrSC83w9iCSuYOGJAi4gaK2y6yN0kLSFMAAcwEICztAYOYEUBauoRn+EYhUb0HDIihwmWF9oKxP1PCwUUwxq1g6EWvKPFd6zniYlL+wltgUFpUx54PDyEiii6NztXjJLof7WDJ6nLYfM0Zi5SjPwUi4jm3bwxGjPJNFvK4eE/uKk1Nl25PWZcMdGAoTYiTGVKhUAdUQPC4ZNo8Na59/wTQJGwl76i5EP7BQxOgfuXDkbEBf6lxT3XMZPlpKszdGTPK0SiGPA0Wih9ma86AR7eNMTIitVW+R8SmcqFGlVbc+vsF5jXsAOHlIDIFQwohINNCcY8m1jfagGpd7o6uNmE1unyR0rwYAAAAA') format('woff2'),
  url('//at.alicdn.com/t/font_1899025_it5td6j37vc.woff?t=1592792557555') format('woff'),
  url('//at.alicdn.com/t/font_1899025_it5td6j37vc.ttf?t=1592792557555') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+ */
  url('//at.alicdn.com/t/font_1899025_it5td6j37vc.svg?t=1592792557555#iconfont') format('svg'); /* iOS 4.1- */
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-guanli:before {
  content: "\e640";
}

.icon-jine:before {
  content: "\e666";
}

.icon-shijian:before {
  content: "\e77c";
}

.icon-shichang:before {
  content: "\e62c";
}

.mui-content {
	padding: 15px 10px 0 10px;
}
.mui-content ul{
	background: transparent;
	padding: 0;
}
.mui-content ul li {
	border: 1px solid #ccc;
	overflow: hidden;
	padding:10px;
	border-radius: 5px;
	background-color: #f5f7fa;
	margin-bottom: 15px;
	font-size:12px;
}
.mui-content ul li:active {
	background-color: #EfEfEf;
}
.mui-content ul li div{
	min-width: 50%;
	float: left;	
}
.mui-content ul li div:nth-child(1){
	width: 100%;
}

.mui-content ul li div span {
	/*display: block;*/
	line-height: 30px;
}
.mui-content ul li div .title{
	color: #999;
}
.mui-content ul li div span i {
	color: #22B14C;
	margin-right: 5px;
}
.spanJust {
    color: #22B14C !important;
}
.spanNegative {
    color: #F47378 !important;
}
</style>
</head>
<body>
	<div style="padding-top: 10px" class="mui-content">
		<ul class="mui-table-view">
			<%-- <c:forEach items="${walletList}" var="wallet">
				<li class="mui-table-view-cell" id="wolfli${wallet.id }" value="${wallet.id }">
					<c:choose>
						<c:when test="${wallet.paysource == 1 }">
							<font size="3px">
								<strong>充值钱包</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： +<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： +<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 2 }">
							<font size="3px">
								<strong>钱包充电</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： -<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 3 }">
							<font size="3px">
								<strong>投币消费</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： -<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 4 }">
							<font size="3px">
								<strong>充值离线卡</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额：-<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 5 }">
							<font size="3px">
								<strong>退款到钱包</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： +<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： +<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 6 }">
							<font size="3px">
								<strong>钱包退款</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： -<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 7 }">
							<font size="3px">
								<strong>虚拟充值钱包</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： +<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： +<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:when test="${wallet.paysource == 8 }">
							<font size="3px">
								<strong>钱包退款</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： -<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:when>
						<c:otherwise>
							<font size="3px">
								<strong>消费</strong>
							</font>
							<div class="flex_sp">
								<div>充值金额： -<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></div>
								<div>赠送金额： -<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></div>
							</div>
						</c:otherwise>
					</c:choose>
				
				<div class="flex_sp">
					<div>充值余额： <fmt:formatNumber value="${wallet.topupbalance}" pattern="0.00" /></div>
					<div>赠送余额： <fmt:formatNumber value="${wallet.givebalance}" pattern="0.00" /></div>
				</div>
				<font style="font-size: 13px;">
					<fmt:formatDate value="${wallet.createTime }" pattern="yyyy-MM-dd HH:mm:ss" />
				</font> 
				</li>
			</c:forEach> --%>
			<c:forEach items="${walletList}" var="wallet">
			<li class="list-item" data-id="${wallet.id }">
				<c:set 
					value="${wallet.paysource == 1 ? '+' 
						: wallet.paysource == 2 ? '-' 
						: wallet.paysource == 3 ? '-' 
						: wallet.paysource == 4 ? '-' 
						: wallet.paysource == 5 ? '+' 
						: wallet.paysource == 6 ? '-' 
						: wallet.paysource == 7 ? '+' 
						: wallet.paysource == 8 ? '-' 
						: '-' 
						}" 
					var="symbol" 
				/>
				<div class="left">
					<span class="title"><i class="iconfont icon-guanli"></i>操作类型：</span>
					<span>${wallet.paysource == 1 ? '充值钱包' 
							: wallet.paysource == 2 ? '钱包充电' 
							: wallet.paysource == 3 ? '投币消费'
							: wallet.paysource == 4 ? '充值离线卡'
							: wallet.paysource == 5 ? '退款到钱包'
							: wallet.paysource == 6 ? '钱包退款'
							: wallet.paysource == 7 ? '虚拟充值钱包'
							: wallet.paysource == 8 ? '虚拟退款'
							: '消费'
							}</span>
				</div>
				<div class="left">
				    <span class="title"><i class="iconfont icon-jine"></i>充值金额：</span>
					<span class="${symbol == '-' ? 'spanNegative' : 'spanJust'}">${symbol}<fmt:formatNumber value="${wallet.money}" pattern="0.00" /></span>
					<strong>元</strong>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-jine"></i>赠送金额：</span>
					<span class="${symbol == '-' ? 'spanNegative' : 'spanJust'}">${symbol}<fmt:formatNumber value="${wallet.sendmoney}" pattern="0.00" /></span>
					<strong>元</strong>
				</div>
				<div class="left">
				    <span class="title"><i class="iconfont icon-jine"></i>充值余额：</span>
					<span><fmt:formatNumber value="${wallet.topupbalance}" pattern="0.00" /></span>
					<strong>元</strong>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-jine"></i>赠送余额：</span>
					<span><fmt:formatNumber value="${wallet.givebalance}" pattern="0.00" /></span>
					<strong>元</strong>
				</div>
				<div class="right">
					<span class="title"><i class="iconfont icon-shichang" style="font-size: 17px;"></i>记录时间：</span>
					<span><fmt:formatDate value="${wallet.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				</div>
			</li>
			</c:forEach>
		</ul>
	</div>
</body>
<script>
$(function() {
	$("li.list-item").click(function() {
		window.location.href = "${hdpath}/general/genWalletDetailInfo?id=" + $(this).attr('data-id').trim();
	});
})
</script>
</html>

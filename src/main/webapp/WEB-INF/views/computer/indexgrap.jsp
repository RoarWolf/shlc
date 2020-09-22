<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>自助充电平台</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="${hdpath}/css/animate.min.css"/>
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" href="${hdpath}/hdfile/css/index.css">
<title>首页</title>
<script src="${hdpath}/hdfile/js/echarts.js"></script>
<script src="${hdpath}/js/jquery.js"></script>
</head>
<body data-list=${hebdomadmap}>

	<div class="app">
		<header>
			<h1>自助充电平台</h1>
			<h5> SELF-CHARGING PLATFROM </h5>
		</header>
		<div class="disData">
			<ul>
				<li>
					<a href="javascript:;">
						<span>${tourist.alltourist}</span>
						<span>用户总数/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.monthtourist}</span>
						<span>本月新增用户/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.daytourist}</span>
						<span>今日新增用户/人</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.ispeople}</span>
						<span>今日充电/人次</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${totalmap.totalmoney}</span>
						<span>总的营业额/元</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${earningsyest.moneytotal}</span>
						<span>昨日营业额/元 </span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<%-- <span>${earningsnow.incomemoney}</span> --%>
						<span>${earningsnow.nowonlineearn}</span>
						<span>今日营业额/元</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<span>${tourist.online}</span>
						<span>在线设备数/台</span>
					</a>
				</li>
				
			</ul>
		</div>
		<section>
			
				<div class="ct3">
					<div class="title">昨日设备收入排行</div>
					<!-- 
					<div class="top">
						<c:forEach items="${codeincomefirst}" var="order"  varStatus="as">
						<li>
							<span><i class="iconfont icon-chongdianzhuang"></i></span>
							<span>${order.code}</span>
							<span>
								<span>${order.moneytotal}</span><span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span>
							</span>
						</li>
						</c:forEach>
					</div>
					 -->
					
					<ul>
					<li id="firstLi">
							<p>
								<span></span>
								<span>设备号</span>
							</p>
							<p>
								<span>金额</span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
							<p>
								<span>时间</span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
						</li>
						<c:forEach items="${codeincome}" var="order"  varStatus="as">
						<li>
							<p>
								<span><i class="iconfont icon-chongdianzhuang"></i></span>
								<span>${order.code}</span>
							</p>
							<p>
								<span>${order.moneytotal}</span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
							<p>
								<span><fmt:formatDate value="${order.count_time}" pattern="yyyy-MM-dd"/></span>
								<!-- <span class="activeUp"><i class="iconfont icon-arrowUp-copy"></i></span> -->
							</p>
						</li>
						</c:forEach>
					</ul>
				</div>
		
				<!-- <div class="ct2"></div> -->
		
			
				<div class="ct4"></div>
				<div class="ct5">
					<div class="title">最近订单</div>
					<ul>
						<li class="top"><span>设备号</span> <span>金额</span> <span>支付方式</span> <span>状态</span> <span>来源</span></li>
					</ul>
					<div class="scrollView">
						<ul>
							<c:forEach items="${sometimeorder}" var="order"  varStatus="as">
							<li data-id="${order.id}">
								<span>${order.code}</span> <span>${order.money}</span> 
								<span>
								${order.paytype==1 ? "钱包" : order.paytype==2 ? "微信" : order.paytype==3 ? "支付宝" : order.paytype==4 ? "微信小程序" : order.paytype==5 ? "支付宝小程序" : "其它" }
								</span> 
								<span>
								${order.status==1 ? "正常" : order.status==2 ? "退款" : "其它" }
								</span>
								<span>
								${order.paysource==1 ? "充电" : order.paysource==2 ? "脉冲" : order.paysource==3 ? "充值机" : order.paysource==4 ? "钱包" : order.paysource==5 ? "在线卡" : "其它" }
								</span>
							</li>
							</c:forEach>
						</ul>
					</div>
				</div>
		</section>
		<a id="goBack" href="/pcstatistics/collectinfo"><i class="iconfont icon-shuangjiantouzuo"></i></a>	
		<!-- <div class="time">
			<span><b class="year">2019</b>年<b class="month">5</b>月<b class="day">17</b>日</span><span> <b class="time_hms">16:55:12</b></span>
		</div> -->
	</div>
	<script src="${hdpath}/hdfile/js/main.js"></script>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#10'+' a').addClass('at');
	$('#10').parent().parent().parent().prev().removeClass("collapsed");
	$('#10').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#10').parent().parent().parent().addClass("in");
	$('#10').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>
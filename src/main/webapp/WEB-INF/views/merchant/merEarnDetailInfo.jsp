<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">

		<!--标准mui.css-->
		<link rel="stylesheet" href="/mui/css/mui.min.css">
		<!--App自定义的css-->
		<!--<link rel="stylesheet" type="text/css" href="../css/app.css" />-->
		<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
		<style>
			.mui-card .mui-control-content {
				padding: 10px;
			}
			
			.mui-control-content {
				height: 300px;
			}
			.wolfspan1{
				color: gray;
			}
			.wolfspan2{
				position: absolute;
				right: 25px;
			}
		</style>
	</head>

	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">余额明细</h1>
		</header>
		<div class="mui-content">
			<!-- <div style="padding: 10px 10px;">
				<div id="segmentedControl" class="mui-segmented-control">
					<a class="mui-control-item mui-active" href="#item1">明细详情</a>
					<a class="mui-control-item" href="#item2">金额来源</a>
				</div>
			</div> -->
			<div>
				<div id="item1" class="mui-control-content mui-active">
					<div id="scroll" class="mui-scroll-wrapper">
						<div class="mui-scroll">
							<ul class="mui-table-view" style="font-size: 14px;">
								<li class="mui-table-view-cell">
									<span class="wolfspan1">金额</span>
									<span class="wolfspan2" style="font-size: 32px; color: rgb(68, 181, 73);">${merchantDetail.money }</span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">类型</span>
									<span class="wolfspan2">${merchantDetail.status == 1 ? "收入" : merchantDetail.status == 2 ? "支出" : "？？？" }</span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">时间</span>
									<span class="wolfspan2"><fmt:formatDate
							value="${merchantDetail.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">交易单号</span>
									<span class="wolfspan2">${merchantDetail.ordernum }</span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">剩余零钱</span>
									<span class="wolfspan2">${merchantDetail.balance }</span>
								</li>
								<li class="mui-table-view-cell" style="text-align: center;">
									<c:choose>
										<c:when test="${paysource == 1 }">
											<a href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=1">点击查看详情</a>
										</c:when>
										<c:when test="${paysource == 2 }">
											<a href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=3">点击查看详情</a>
										</c:when>
										<c:when test="${paysource == 3 }">
											<a href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=2">点击查看详情</a>
										</c:when>
										<c:when test="${paysource == 4 }">
											<a href="${hdpath }/merchant/withcardinfo?id=${order.id}">点击查看详情</a>
										</c:when>
										<c:when test="${paysource == 5 }">
											<a href="${hdpath }/money/payMoneyinfo?id=${order.id}&rank=2">点击查看详情</a>
										</c:when>
										<c:otherwise>
											无
										</c:otherwise>
									</c:choose>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<%-- <div id="item2" class="mui-control-content">
					<ul class="mui-table-view">
						<c:choose>
							<c:when test="${paysource == 1 }">
								<li class="mui-table-view-cell">
									<span class="wolfspan1">收益来源</span>
									<span class="wolfspan2">${order.equipmentnum }号充电设备</span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">支付金额</span>
									<span class="wolfspan2">${order.expenditure }</span>
								</li>
								<li class="mui-table-view-cell">
									<span class="wolfspan1">充电时常</span>
									<span class="wolfspan2">${order.equipmentnum }号充电设备</span>
								</li>
							</c:when>
						</c:choose>
					</ul>
				</div> --%>
			</div>
		</div>
		<script src="/mui/js/mui.min.js"></script>
		<script>
			mui.init({
				swipeBack: true //启用右滑关闭功能
			});
		</script>

	</body>

</html>
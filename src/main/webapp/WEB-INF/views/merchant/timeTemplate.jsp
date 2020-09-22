<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${templatelist == null ? '新增模板' : '编辑模板'}</title>
	<link rel="stylesheet" type="text/css" href="${hdpath}/css/base.css">
	<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
	<link rel="stylesheet" href="${hdpath }/hdfile/css/timeTem.css">
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<script src="${hdpath }/mui/js/mui.min.js"></script>
	<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
</head>
<body data-from="${templatelist == null ? 1 : 2}">
<!-- data-from 1为新增模板 2为修改模板  -->
<form action="/merchant/tempPreview" method="post" id="prevForm">
  <input type="hidden" value="" name="paratem" id="paratem" />
  <input type="hidden" value="${code}" name="code" id="code" />
</form>
	<input type="hidden" id="code" value="${code}">
	<div class="app">
		<!-- 编辑模板开始 -->
		<c:if test="${templatelist != null}">
		<!-- 判断是不是系统模板，是：不能修改，否则： 能修改 -->
		<c:set var="isSysTem"  value="${templatelist.merchantid == 0 ? 'disabled' : null}"/>
		<div class="div_list" data-id="${templatelist.id}">
			<c:if test="${templatelist.merchantid == 0}">
				<h3 class="sysTemTitle">此模板为系统模板（不能修改）</h3>
			</c:if>
			<div class="top">
				<p class="item_p">
					<span class="item_p_title">模板名称：</span>
					<span class="item_span_inp">
						<input type="text" data-value="${templatelist.name}" value="${templatelist.name}" ${isSysTem} >
					</span>
				</p>
				<p class="item_p">
					<span class="item_p_title">品牌名称：</span>
					<span class="item_span_inp">
						<input type="text" data-value="${templatelist.remark}" value="${templatelist.remark}" ${isSysTem}>
					</span>
				</p>
				<p class="item_p">
					<span class="item_p_title">售后电话：</span>
					<span class="item_span_inp">
						<input type="text"  data-value="${templatelist.common1}" value="${templatelist.common1}" ${isSysTem}>
					</span>
				</p>
			</div>

			<!-- 子模板开始 -->
			<div class="mid mid1">
				<ul class="mui-table-view">
					<h5>收费标准</h5>
					<p style="text-align: center; margin-bottom: 0;">(每小时收费：单位：元，功率区间：单位：瓦)</p>
					<c:forEach items="${templatelist.gather.tempower}" var="ctem1">
				    <li class="mui-table-view-cell">
						<div class="item">
							<p class="item_p">
								<span class="item_p_title">每小时：</span>
								<span class="item_span_inp">
									<input class="mui-numbox-input everymoney" data-value="${ctem1.money}" value="${ctem1.money}" type="number" ${isSysTem} />
								</span>
							</p>
							<p class="item_p">
								<span class="item_p_title">功率区间：</span>
								<span class="item_span_inp c_inp">
									<input class="mui-numbox-input powerstart" data-value="${ctem1.common1}" value="${ctem1.common1}" type="number" ${isSysTem} />~<input class="mui-numbox-input powerend" data-value="${ctem1.common2}" value="${ctem1.common2}" type="number" ${isSysTem} />
								</span>
							</p>
						</div>
						<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${ctem1.id}" ${isSysTem} ></button>
				    </li>
				    </c:forEach>
					<div class="bottom_bun">
						<!-- <button type="button" class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem">删除一行</button> -->
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem " ${isSysTem}>添加一行</button>
					</div>
					<p class="tip">注意：设备能承受的最大功率由机器决定</p>
				</ul>
				<p class="item_p chargeins">
					<span class="item_p_title">收费说明：</span>
				</p>
				<div class="mui-input-row textarea-box">
					<textarea rows="5" placeholder="请输入充电说明" ${isSysTem} >${templatelist.chargeInfo}</textarea>
				</div>
			</div>
			<div class="mid mid2">
				<ul class="mui-table-view">
					<h5>按照时间充电</h5>
					<p style="text-align: center; margin-bottom: 0;">(充电时间:单位：分钟)</p>
					<c:forEach items="${templatelist.gather.temtime}" var="ctem2">
				    <li class="mui-table-view-cell">
				    	<div class="item">
					    	<p class="item_p">
								<span class="item_p_title">显示名称：</span>
								<span class="item_span_inp">
									<input type="text" class="showtitle" data-value="${ctem2.name}" value="${ctem2.name}" ${isSysTem} >
								</span>
							</p>
							<p class="item_p">
								<span class="item_p_title">充电时间：</span>
								<span class="item_span_inp">
									<input class="mui-numbox-input chargetime" data-value="${ctem2.chargeTime}" value="${ctem2.chargeTime}" type="number" ${isSysTem} />
								</span>
							</p>
						</div>
						<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${ctem2.id}" ${isSysTem} ></button>
				    </li>
				    </c:forEach>
				    <div class="bottom_bun">
						<!-- <button type="button" class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem">删除一行</button> -->
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem" ${isSysTem} >添加一行</button>
					</div>
					<p class="tip"></p>
				</ul>
				<!-- 是否支持临时充电 -->
				<ul class="mui-table-view">
					<div class="onlinecard-time contralWrapper">
						<span class="item_p_title">刷卡最大充电时间:&nbsp;&nbsp;&nbsp;</span>
						<div class="input-box-oc">
						  <input type="text" class="oc-time" data-value="${templatelist.rank}" value="${templatelist.rank}" ${isSysTem} > 分钟
						</div>
					</div>
					<div class="isSupCha contralWrapper">
						<span class="item_p_title">是否支持临时充电:&nbsp;&nbsp;&nbsp;</span>
						<div 
							<c:choose>
								<c:when test="${templatelist.walletpay == 1 && templatelist.merchantid == 0}">
									data-checked="true" class="mui-switch mui-active mui-disabled" 
								</c:when>
								<c:when test="${templatelist.walletpay == 1 && templatelist.merchantid != 0}">
									data-checked="true" class="mui-switch mui-active" 
								</c:when>
								<c:when test="${templatelist.walletpay != 1 && templatelist.merchantid == 0}">
									data-checked="false" class="mui-switch mui-disabled" 
								</c:when>
								<c:when test="${templatelist.walletpay != 1 && templatelist.merchantid != 0}">
									data-checked="false" class="mui-switch"
								</c:when>
							</c:choose>
							data-name="temporaryc"
						>
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<%-- <div class="ifalipay_div contralWrapper">
						<span class="item_p_title">是否支持支付宝充电:</span>
						<div 
							<c:choose>
								<c:when test="${templatelist.ifalipay == 1 && templatelist.merchantid != 0}">
									data-checked="true"
									<c:if test="${templatelist.permit != 1}">class="mui-switch mui-active mui-disabled"</c:if>
									<c:if test="${templatelist.permit == 1}">class="mui-switch mui-active "</c:if>
								</c:when>
								<c:when test="${templatelist.ifalipay == 1 && templatelist.merchantid == 0}">
									data-checked="true"
									class="mui-switch mui-active mui-disabled"
								</c:when>
								<c:when test="${templatelist.ifalipay != 1 && templatelist.merchantid != 0}">
									data-checked="false" 
									<c:if test="${templatelist.permit != 1}">class="mui-switch mui-disabled"</c:if>
									<c:if test="${templatelist.permit == 1}">class="mui-switch"</c:if>
								</c:when>
								<c:when test="${templatelist.ifalipay != 1 && templatelist.merchantid == 0}">
									data-checked="false"
									class="mui-switch mui-disabled"
								</c:when>
							</c:choose>
							
							data-name="isalipay"
						>
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<p class="pad_l10">提示：支付宝充电暂不支持部分退费</p> --%>
					<div class="ref_div contralWrapper">
						<span class="item_p_title">是否支持退费:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<div
							data-name="ref"
							<c:choose>
								<c:when test="${templatelist.walletpay == 1 && templatelist.merchantid != 0}">
									<c:if test="${templatelist.permit != 1}">class="mui-switch" data-checked="false"</c:if>
									<c:if test="${templatelist.permit == 1}">class="mui-switch mui-active" data-checked="true"</c:if>
								</c:when>
								<c:when test="${templatelist.walletpay == 1 && templatelist.merchantid == 0}">
									data-checked="true"
									class="mui-switch mui-active mui-disabled"
								</c:when>
								<c:when test="${templatelist.walletpay != 1 && templatelist.merchantid != 0}">
									<c:if test="${templatelist.permit != 1}">class="mui-switch mui-disabled" data-checked="false"</c:if>
									<c:if test="${templatelist.permit == 1}">class="mui-switch mui-active" data-checked="true"</c:if>
								</c:when>
								<c:when test="${templatelist.walletpay != 1 && templatelist.merchantid == 0}">
									data-checked="false"
									class="mui-switch mui-disabled"
								</c:when>
							</c:choose>
						 >
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<p class="pad_l10">提示：充不完的费用 退回到虚拟钱包，下次充电可用</p>
				</ul>
			</div>
			
			<div class="mid mid3">
				<div class="mid_mask" <c:if test="${templatelist.walletpay != 1}">style="display: block;"</c:if> >
					<p>未开启临时充电</p>
				</div>
				<ul class="mui-table-view">
					<h5>按照金额充电（临时充电）</h5>
					<p style="text-align: center; margin-bottom: 0;">(付款金额:单位：元)</p>
					<c:forEach items="${templatelist.gather.temmoney}" var="ctem3">
				    <li class="mui-table-view-cell">
				    	<div class="item">
					    	<p class="item_p">
								<span class="item_p_title">显示名称：</span>
								<span class="item_span_inp">
									<input type="text" class="showmoney" data-value="${ctem3.name}" value="${ctem3.name}" ${isSysTem} >
								</span>
							</p>
							<p class="item_p">
								<span class="item_p_title">付款金额：</span>
								<span class="item_span_inp">
									<input class="mui-numbox-input money" data-value="${ctem3.money}" value="${ctem3.money}" type="number" ${isSysTem} />
								</span>
							</p>
						</div>
						<button class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem" data-id="${ctem3.id}" ${isSysTem}></button>
				    </li>
				    </c:forEach>
				    <div class="bottom_bun">
						<!-- <button type="button" class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem">删除一行</button> -->
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem" ${isSysTem} >添加一行</button>
					</div>
					 <p class="tip">备注：默认按照金额给予小功率电动车充电时间，如果电动车功率大，费用用完会提前自动停止。如果费用没有用完，如果支持退回虚拟钱包，就退回虚拟钱包，下次充电可用。如果不支持退费，不承担充电过程中的风险</p>
				
				</ul>
			</div>
			<ul class="mui-table-view">
				<div class="deleteTemDiv"> <button type="button" class="mui-btn mui-btn-danger mui-icon mui-icon-trash delTem" ${isSysTem}>删除模板</button> </div>
			</ul>
		</div>
			<c:if test="${templatelist.merchantid == 0}">
				<div class="dis-tip" style="font-size: 14px; color:#777; margin-bottom: 15px; padding-left: 10px;"><strong>提示：</strong>当前模板为系统默认模板，不允许被修改</div>
			</c:if>
		</c:if>
		<!-- 编辑模板结束 -->
		<!-- 新添加模板 -->
		<c:if test="${templatelist == null}" >
		<div class="div_list" data-id="-1">
			<div class="top">
				<p class="item_p">
					<span class="item_p_title">模板名称：</span>
					<span class="item_span_inp">
						<input type="text" data-value="" value="">
					</span>
				</p>
				<p class="item_p">
					<span class="item_p_title">品牌名称：</span>
					<span class="item_span_inp">
						<input type="text" data-value="" value="">
					</span>
				</p>
				<p class="item_p">
					<span class="item_p_title">售后电话：</span>
					<span class="item_span_inp">
						<input type="text"  data-value="" value="">
					</span>
				</p>
			</div>

			<!-- 子模板开始 -->
			<div class="mid mid1">
				<ul class="mui-table-view">
					<h5>收费标准</h5>
					<p style="text-align: center; margin-bottom: 0;">(每小时收费：单位：元，功率区间：单位：瓦)</p>
					<div class="bottom_bun">
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem ">添加一行</button>
					</div>
					<p class="tip">注意：设备能承受的最大功率由机器决定</p>
				</ul>
				<p class="item_p chargeins">
					<span class="item_p_title">收费说明：</span>
				</p>
				<div class="mui-input-row textarea-box">
					<textarea rows="5" placeholder="请输入充电说明">${templatelist.chargeInfo}</textarea>
				</div>
			</div>

			<div class="mid mid2">
				<ul class="mui-table-view">
					<h5>按照时间充电</h5>
					<p style="text-align: center; margin-bottom: 0;">(充电时间:单位：分钟)</p>
				    <div class="bottom_bun">
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem">添加一行</button>
					</div>
					<p class="tip"></p>
				</ul>
				<!-- 是否支持临时充电 -->
				<ul class="mui-table-view">
					<div class="onlinecard-time contralWrapper">
						<span class="item_p_title">刷卡最大充电时间:&nbsp;&nbsp;&nbsp;</span>
						<div class="input-box-oc">
						  <input type="text" class="oc-time" data-value="720" value="720"> 分钟
						</div>
					</div>
					<div class="isSupCha contralWrapper">
						<span class="item_p_title">是否支持临时充电:&nbsp;&nbsp;&nbsp;</span>
						<div  class="mui-switch mui-active" data-name="temporaryc" >
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<!-- <div class="ifalipay_div contralWrapper">
						<span class="item_p_title">是否支持支付宝充电:</span>
						<div class="mui-switch mui-active" data-name="isalipay" >
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<p class="pad_l10">提示：支付宝支付、暂不支付退费</p> -->
					<div class="ref_div contralWrapper">
						<span class="item_p_title">是否支持退费:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<div class="mui-switch mui-active" data-name="ref" >
						  <div class="mui-switch-handle"></div>
						</div>
					</div>
					<p class="pad_l10">提示：充不完的费用 退回到虚拟钱包，下次充电可用</p>
				</ul>
			</div>
			
			<div class="mid mid3">
				<div class="mid_mask">
					<p>未开启临时充电</p>
				</div>
				<ul class="mui-table-view">
					<h5>按照金额充电（临时充电）</h5>
					<p style="text-align: center; margin-bottom: 0;">(付款金额:单位：元)</p>
				    <div class="bottom_bun">
						<!-- <button type="button" class="mui-btn mui-btn-danger mui-icon mui-icon-trash deleteItem">删除一行</button> -->
						<button type="button" class="mui-btn mui-btn-success mui-icon mui-icon-plusempty addItem">添加一行</button>
					</div>
					 <p class="tip">备注：默认按照金额给予小功率电动车充电时间，如果电动车功率大，费用用完会提前自动停止。如果费用没有用完，如果支持退回虚拟钱包，就退回虚拟钱包，下次充电可用。如果不支持退费，不承担充电过程中的风险</p>
				
				</ul>
			</div>
		</div>
		</c:if>
		<!-- 新添加模板結束 -->
	</div>

	<nav class="mui-bar mui-bar-tab">
			<button 
				type="button" 
				class="mui-btn mui-btn-success goBack" 
				>返回</button>
			 <c:if test="${templatelist == null}" >
			<button type="button" class="mui-btn mui-btn-success addNewTem" ${isSysTem}>新增</button>
			</c:if>
			<c:if test="${templatelist != null}" >
			<button type="button" class="mui-btn mui-btn-success prevScan">预览</button>
			<button type="button" class="mui-btn mui-btn-success saveTem" ${isSysTem}>保存</button>
			</c:if>
		
	</nav>

<script type="text/javascript" src="${hdpath}/hdfile/js/timeTem.js"></script>
</body>
</html>
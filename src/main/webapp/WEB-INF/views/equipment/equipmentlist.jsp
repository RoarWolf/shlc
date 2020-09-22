<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>设备管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<link rel="stylesheet" type="text/css" href="${hdpath }/css/iconfont.css" />
<!--App自定义的css-->
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.js"></script>
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
.wolf-right {
	position: absolute;
	right: 13.5px;
}

.mui-control-content {
	background-color: white;
	min-height: 500px;
}

.mui-control-content .mui-loading {
	margin-top: 50px;
}

#topPopover .mui-popover-arrow {
	left: auto;
	right: 6px;
}

.mui-popover {
	height: 300px;
}

.mui-content {
	padding: 10px;
}
/*移除底部或顶部三角,需要在删除此代码*/
.mui-popover .mui-popover-arrow:after {
	width: 0px;
}
.mui-btn,button{padding: 6px 10px;}
.mui-search{width: 80%;float: right;}
.muisearchfirst, .muisearchsecond, .muisearchthird{border: 1px solid #c5bfbf;}
.mui-scroll select{height: 34px;margin: 0;padding: 1px;text-align: center;font-size: 14px;width: 20%;/* background: #eaead9; */}
.muioutlined{height: 34px;line-height: normal;}
.muisearch{text-align: center;border:1px solid #e0d9d9}
input[type=search]{width: 55%;margin: 0;}
.mui-table-view-cell{border: 1px solid #2436f1; border-radius: 15px; margin-top: 2px;}
.cooperate{border: 1px solid #37d825;}
</style>
</head>
<body>
	<div class="mui-content">
		<input type="hidden" id="wolfparam" value="${wolfparam }"> 
		<input type="hidden" id="uid" value="${uid }">
		<div id="slider" class="mui-slider">
			<div id="sliderSegmentedControl" class="mui-slider-indicator mui-segmented-control mui-segmented-control-inverted">
				<a class="mui-control-item" href="#item1mobile" data-id="0"> 在线设备${onNum} </a>
				<a class="mui-control-item" href="#item2mobile" data-id="1"> 离线设备${offNum} </a> 
				<a class="mui-control-item" href="#item3mobile" data-id="2"> 全部设备${allNum} </a>
			</div>
			<div id="sliderProgressBar" class="mui-slider-progress-bar mui-col-xs-4"></div>
			<div class="mui-slider-group">
				<div id="item1mobile" class="mui-slider-item mui-control-content mui-active">
					<input type="hidden" id="getOnEquinput" value="5">
					<div id="scroll1" class="mui-scroll-wrapper">
						<div class="mui-scroll">
							<div class="muisearchfirst" id="muisearchfirst">
									<select name="sourcefirst">
										<option value="1" <c:if test="${sourcefirst == 1}">selected="selected"</c:if>>设备编号</option>
										<option value="2" <c:if test="${sourcefirst == 2}">selected="selected"</c:if>>设备名称</option>
										<option value="3" <c:if test="${sourcefirst == 3}">selected="selected"</c:if>>小区名称</option>
									</select>&gt;
									<input name="parameterfirst" type="search" class="mui-input-clear" value="${parameterfirst}">
									<button type="button" class="mui-btn muioutlined" onclick="searchfirst()">搜索</button>
							</div>
							<ul class="mui-table-view" id="onlineEqu">
								<c:forEach items="${onlineList }" var="equ">
									<li class="mui-table-view-cell <c:if test="${uid!=equ.manid}">cooperate</c:if>">
									  <span class="wolf-left">设备编号：</span><span id="equNum${equ.code}" class="wolf-right">${equ.code }</span><br>
									  <span>线上收益：</span><span class="wolf-right"><fmt:formatNumber type="number" value="${equ.totalMoney == null ? 0 : equ.totalMoney }" maxFractionDigits="2" pattern="0.00" />元</span><br>
									  <span>投币收益：</span><span class="wolf-right">${equ.coinsMoney}元</span><br>
									  <span>设备名称：</span><span class="wolf-right">${equ.remark}</span><br>
									  <span>小区名称：</span><span class="wolf-right">${equ.name}</span><br>
									  <c:if test="${equ.deviceType == 1}">
										<c:if test="${equ.csq >= 0 && equ.csq <= 5}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji3"></span>
												</button>
											</span>
										</c:if> 
										<c:if test="${equ.csq > 5 && equ.csq <= 10}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji2"></span>
												</button>
											</span>
										</c:if> 
										<c:if test="${equ.csq > 10 && equ.csq <= 20}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji1"></span>
												</button>
											</span>
										</c:if> 
										<c:if test="${equ.csq > 20 && equ.csq <= 31}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji"></span>
												</button>
											</span>
										</c:if> 
										<span> <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}"
											class="mui-btn mui-btn-success">统计</a></span> 
										<c:if test="${uid==equ.manid}">
										<span><a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}"
											class="mui-btn mui-btn-success">订单</a></span> 
										</c:if>
										<c:if test="${uid!=equ.manid}">
										<span><a href="${hdpath }/equipment/equinfo?code=${equ.code}"
											class="mui-btn mui-btn-success">管理</a></span> 
										<span><a href="${hdpath }/merchant/remotechargechoose?code=${equ.code}"
											class="mui-btn mui-btn-success">远程</a></span> 
										<span><a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}"
											class="mui-btn mui-btn-success">订单</a></span> 
										<span>
										<c:choose>
										  <c:when test="${equ.hardversion != '03' && equ.hardversion != '04' }">
											<a href="${hdpath }/merchant/charge?code=${equ.code}" class="mui-btn mui-btn-success">状态</a>
										  </c:when>
										  <c:otherwise>
											 <button disabled="disabled" class="btn btn-default">状态</button>
										 </c:otherwise>
									    </c:choose>
										</span>
										</c:if>
									  </c:if>
									  <c:if test="${equ.deviceType == 2}">
									  	<span> <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}"
											class="mui-btn mui-btn-success">统计</a></span> 
									  	<c:if test="${uid==equ.manid}">
										<span><a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}"
											class="mui-btn mui-btn-success">订单</a></span> 
										</c:if>
										<c:if test="${uid!=equ.manid}">
										<span><a href="${hdpath }/equipment/equinfo?code=${equ.code}"
											class="mui-btn mui-btn-success">管理</a></span> 
										<span><a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}"
											class="mui-btn mui-btn-success">订单</a></span> 
										</c:if>
									  </c:if>
								</c:forEach>
							</ul>
							<ul class="mui-table-view" style="text-align: center;">
								<li class="mui-table-view-cell" id="getOnEqu" style=" border: 0;"><a>点击加载更多</a></li>
							</ul>
						</div>
					</div>
				</div>
				<div id="item2mobile" class="mui-slider-item mui-control-content">
				<input type="hidden" id="getOffEquinput" value="5">
					<div id="scroll2" class="mui-scroll-wrapper">
						<div class="mui-scroll">
							<div class="muisearchsecond" id="muisearchsecond">
								<select name="source" class="mui-select">
									<option value="1" <c:if test="${source == 1}">selected="selected"</c:if>>设备编号</option>
									<option value="2" <c:if test="${source == 2}">selected="selected"</c:if>>设备名称</option>
									<option value="3" <c:if test="${source == 3}">selected="selected"</c:if>>小区名称</option>
								</select>&gt;
								<input name="parameter" type="search" class="mui-input-clear" value="${parameter}">
								<button type="button" onclick="searchsecond()" class="mui-btn muioutlined">搜索</button>
							</div>
							<ul class="mui-table-view" id="offlineEqu">
								<c:forEach items="${offlineList }" var="equ">
									<li class="mui-table-view-cell <c:if test="${uid!=equ.manid}">cooperate</c:if>">
									<span class="wolf-left">设备编号：</span><span id="equNum${equ.code }" class="wolf-right">${equ.code }</span><br> 
									<span class="wolf-left">线上收益：</span><span class="wolf-right">
									  <fmt:formatNumber type="number" value="${equ.totalMoney == null ? 0 : equ.totalMoney }" maxFractionDigits="2" pattern="0.00" />元</span><br> 
									<span class="wolf-left">投币收益：</span><span class="wolf-right">${equ.coinsMoney}元</span><br>
									<span class="wolf-left">设备名称：</span> <span class="wolf-right">${equ.remark }</span><br> 
									<span class="wolf-left">小区名称：</span><span class="wolf-right">${equ.name }</span><br> 
									<span>
										<button class="mui-btn mui-btn-danger">
											<span class="icon iconfont icon-no-signal"></span>
										</button>
									</span>
									<span>
									<a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}" class="mui-btn mui-btn-success">统计</a></span> 
									<c:if test="${uid==equ.manid}"><span><a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}" class="mui-btn mui-btn-success">订单</a></span> </c:if>
									<c:if test="${uid!=equ.manid}">
									<span><a href="${hdpath }/equipment/equinfo?code=${equ.code}" class="mui-btn mui-btn-success">管理</a></span> 
									<span><button class="mui-btn mui-btn-default" disabled="disabled">远程</button></span>
									<span><a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}" class="mui-btn mui-btn-success">订单</a></span> 
									<span> 
									<c:choose>
										<c:when test="${equ.hardversion != '03' && equ.hardversion != '04' }">
										<a href="${hdpath }/merchant/charge?code=${equ.code}" class="mui-btn mui-btn-success">状态</a>
										</c:when>
										<c:otherwise>
											<button disabled="disabled" class="mui-btn mui-btn-default">状态</button>
										</c:otherwise>
									</c:choose>
									</span>
									</c:if>
								</c:forEach>
							</ul>
							<ul class="mui-table-view" style="text-align: center;">
								<li class="mui-table-view-cell" id="getOffEqu" style=" border: 0;"><a>点击加载更多</a></li>
							</ul>
						</div>
					</div>

				</div>
				<div id="item3mobile" class="mui-slider-item mui-control-content">
				<input type="hidden" id="getAllEquinput" value="5">
					<div id="scroll3" class="mui-scroll-wrapper">
						<div class="mui-scroll">
							<div class="muisearchthird" id="muisearchthird">
								<select name="source" class="mui-select">
									<option value="1" <c:if test="${source == 1}">selected="selected"</c:if>>设备编号</option>
									<option value="2" <c:if test="${source == 2}">selected="selected"</c:if>>设备名称</option>
									<option value="3" <c:if test="${source == 3}">selected="selected"</c:if>>小区名称</option>
								</select>&gt;
								<input name="parameter" type="search" class="mui-input-clear" value="${paramete}">
								<button type="submit" onclick="searchthird()" class="mui-btn muioutlined">搜索</button>
							</div>
							<ul class="mui-table-view" id="allEqu">
								<c:forEach items="${allList }" var="equ">
									<li class="mui-table-view-cell <c:if test="${uid!=equ.manid}">cooperate</c:if>">
									  <span class="wolf-left">设备编号：</span> <span id="equNum${equ.code }" class="wolf-right">${equ.code }</span><br> 
									  <span class="wolf-left">线上收益：</span> <span class="wolf-right">
									     <fmt:formatNumber type="number" value="${equ.totalMoney == null ? 0 : equ.totalMoney }" maxFractionDigits="2" pattern="0.00" />元</span><br>
									  <span class="wolf-left">投币收益：</span><span class="wolf-right">${equ.coinsMoney}元</span><br>
									  <span class="wolf-left">设备名称：</span><span class="wolf-right">${equ.remark }</span><br> 
									  <span class="wolf-left">小区名称：</span><span class="wolf-right">${equ.name }</span><br> 
									  <c:if test="${equ.deviceType == 1}">
									  	<c:if test="${equ.csq == 0 }">
									  		<c:if test="${equ.state == 1 }">
									  			<span>
													<button class="mui-btn mui-btn-danger">
														<span class="icon iconfont icon-WIFIxinhao-ji3"></span>
													</button>
												</span>
									  		</c:if>
									  		<c:if test="${equ.state == 0 }">
									  			<span>
													<button class="mui-btn mui-btn-danger">
														<span class="icon iconfont icon-no-signal"></span>
													</button>
												</span>
									  		</c:if>
										</c:if> <c:if test="${equ.csq > 0 && equ.csq <= 5}">
											<span>
												<button class="mui-btn mui-btn-danger">
													<span class="icon iconfont icon-WIFIxinhao-ji3"></span>
												</button>
											</span>
										</c:if> <c:if test="${equ.csq > 5 && equ.csq <= 10}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji2"></span>
												</button>
											</span>
										</c:if> <c:if test="${equ.csq > 10 && equ.csq <= 20}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji1"></span>
												</button>
											</span>
										</c:if> <c:if test="${equ.csq > 20 && equ.csq <= 31}">
											<span>
												<button class="mui-btn mui-btn-success">
													<span class="icon iconfont icon-WIFIxinhao-ji"></span>
												</button>
											</span>
										</c:if> 
										<span> <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}" class="mui-btn mui-btn-success">统计</a></span> 
										<c:if test="${uid==equ.manid}"><span><a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}" class="mui-btn mui-btn-success">订单</a></span> </c:if>
										<c:if test="${uid!=equ.manid}">
										<span><a href="${hdpath }/equipment/equinfo?code=${equ.code}" class="mui-btn mui-btn-success">管理</a></span> 
										<span> 
										<c:choose>
											<c:when test="${equ.state == 1 }">
											<a href="${hdpath }/merchant/remotechargechoose?code=${equ.code}" class="mui-btn mui-btn-success">远程</a>
											</c:when>
											<c:otherwise>
												<button disabled="disabled" class="mui-btn mui-btn-default">远程</button>
											</c:otherwise>
										</c:choose>
										</span>
										<span><a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}" class="mui-btn mui-btn-success">订单</a></span> 
										<span> 
										<c:choose>
											<c:when test="${equ.hardversion != '03' && equ.hardversion != '04' }">
												<a href="${hdpath }/merchant/charge?code=${equ.code}" class="mui-btn mui-btn-success">状态</a>
												</c:when>
												<c:otherwise>
													<button disabled="disabled" class="mui-btn mui-btn-default">状态</button>
												</c:otherwise>
											</c:choose>
										</span>
										</c:if>
									</c:if>
								<c:if test="${equ.deviceType == 2}">
								  	<span> <a href="${hdpath}/equipment/codeDayEarn?code=${equ.code}"
										class="mui-btn mui-btn-success">统计</a></span> 
								  	<c:if test="${uid==equ.manid}">
									<span><a href="${hdpath }/equipment/codetotrade?souce=2&code=${equ.code}"
										class="mui-btn mui-btn-success">订单</a></span> 
									</c:if>
									<c:if test="${uid!=equ.manid}">
									<span><a href="${hdpath }/equipment/equinfo?code=${equ.code}"
										class="mui-btn mui-btn-success">管理</a></span> 
									<span><a href="${hdpath }/equipment/codetotrade?souce=1&code=${equ.code}"
										class="mui-btn mui-btn-success">订单</a></span> 
									</c:if>
								  </c:if>
								</c:forEach>
							</ul>
							<ul class="mui-table-view" style="text-align: center;">
								<li class="mui-table-view-cell" id="getAllEqu" style=" border: 0;"><a>点击加载更多</a></li>
							</ul>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<script>
		mui.init({
			swipeBack : false
		});
		(function($) {
			$('.mui-scroll-wrapper').scroll({
				indicators : true
			//是否显示滚动条
			});
		})(mui);
	</script>
</body>
</html>
<script>
function searchfirst(){
	var source = $("#muisearchfirst").find("select[name='sourcefirst']").val();
	var parameter = $("#muisearchfirst").find("input[name='parameterfirst']").val();
	search(1,source,parameter);
}

function searchsecond(){
	var source = $("#muisearchsecond").find("select[name='source']").val();
	var parameter = $("#muisearchsecond").find("input[name='parameter']").val();
	search(2,source,parameter);
}

function searchthird(){
	var source = $("#muisearchthird").find("select[name='source']").val();
	var parameter = $("#muisearchthird").find("input[name='parameter']").val();
	search(3,source,parameter);
}

function search(querynum,source,parameter){
	var uid = ${uid};
	var uidVal = $("#uid").val();
	$.bootstrapLoading.start({
		loadingTips : "加载中..."
	});
	$.ajax({
		url : "${hdpath}/equipment/getAjaxEquList",
		data : {
			uid : uidVal,
			equnum : 0,
			querynum : querynum,
			source : source,
			parameter : parameter
		},
		type : "post",
		dataType : "json",
		success : function(data) {
			$("#getAllEquinput").val(data.equnum);
			var addli = "";
			for (var i = 0; i < data.equlist.length; i++) {
				var equlistval = data.equlist[i];
				var str1 = ''
				if(uid!=equlistval.manid){
					str1 += '<li class="mui-table-view-cell cooperate">';
				}else{
					str1 += '<li class="mui-table-view-cell">';
				}
				str1 += '<span class="wolf-left">设备编号：</span><span id="equNum'+ equlistval.code + '" class="wolf-right">'
						+ equlistval.code
						+ '</span><br>';
				str1 += '<span class="wolf-left">线上收益：</span><span class="wolf-right">'
						+ (equlistval.totalMoney == null ? '0.00' : equlistval.totalMoney.toFixed(2))
						+ '元</span><br>';
				str1 += '<span class="wolf-left">投币收益：</span><span class="wolf-right">'+equlistval.coinsMoney+ '元</span><br>';
				str1 += '<span class="wolf-left">设备名称：</span><span class="wolf-right">'
						+ (equlistval.remark == null ? '--' : equlistval.remark)
						+ '</span><br>';
				str1 += '<span class="wolf-left">小区名称：</span><span class="wolf-right">'
						+ (equlistval.name == null ? '--' : equlistval.name)
						+ '</span><br>';
				var csq = equlistval.csq;
				if (csq == 0) {
					if (equlistval.state == 1) {
						str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji3"></span></button></span>';
					} else {
						str1 += '<span><button class="mui-btn mui-btn-danger"><span class="icon iconfont icon-no-signal"></span></button></span>';
					}
				} else if (csq > 0 && csq <= 5) {
					str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji3"></span></button></span>';
				} else if (csq > 5 && csq <= 10) {
					str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji2"></span></button></span>';
				} else if (csq > 10
						&& csq <= 20) {
					str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji1"></span></button></span>';
				} else if (csq > 20
						&& csq <= 31) {
					str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji"></span></button></span>';
				}
				str1 += '&nbsp;<span><a href="${hdpath}/equipment/codeDayEarn?code='
						+ equlistval.code
						+ '" class="mui-btn mui-btn-success">统计</a></span>';
				if(uid==equlistval.manid){
					str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=2&code='
						+ equlistval.code +'" class="mui-btn mui-btn-success">订单</a></span> ';
				}else{
					str1 += '&nbsp;<span><a href="${hdpath }/equipment/equinfo?code='
							+ equlistval.code
							+ '" class="mui-btn mui-btn-success">管理</a></span>';
					if (equlistval.state == 1) {
						str1 += '&nbsp;<span><a href="${hdpath }/merchant/remotechargechoose?code='
								+ equlistval.code
								+ '" class="mui-btn mui-btn-success">远程</a></span>';
					} else {
						str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">远程</button></span>';
					}
					str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=1&code='
						+ equlistval.code
						+ '" class="mui-btn mui-btn-success">订单</a></span>';
					if (equlistval.hardversion != '03'
							&& equlistval.hardversion != '04') {
						str1 += '&nbsp;<span><a href="${hdpath }/merchant/charge?code='
								+ equlistval.code
								+ '" class="mui-btn mui-btn-success">状态</a></span>'
					} else {
						str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">状态</button></span></ul>'
					}
				}
				addli += str1;
			}
			if(querynum==1){
				$("#onlineEqu").replaceWith("<ul class='mui-table-view' id='onlineEqu'>"+addli);
			}else if(querynum==2){
				$("#offlineEqu").replaceWith("<ul class='mui-table-view' id='offlineEqu'>"+addli);
			}else if(querynum==3){
				$("#allEqu").replaceWith("<ul class='mui-table-view' id='allEqu'>"+addli);
			}
			if (data.listnum < 5) {
				mui.toast("查询已到最后");
			}
		},
		error : function(e) {
		},
		complete : function() {
			$.bootstrapLoading.end();
		}
	});
}
	

	$(function() {
		pushHistory();
		window.addEventListener("popstate", function(e) {
			var param = $("#wolfparam").val();
			if (param == 1) {
				location.replace('/merchant/index');
			} else if (param == 2) {
				location.replace('/merchant/manage');
			}
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
		var uid = ${uid};
		$("#getAllEqu").click(function() {
			var source = $("#muisearchthird").find("select[name='source']").val();
			var parameter = $("#muisearchthird").find("input[name='parameter']").val();
			var uidVal = $("#uid").val();
			$.bootstrapLoading.start({
				loadingTips : "加载中..."
			});
			$.ajax({
				url : "${hdpath}/equipment/getAjaxEquList",
				data : {
					uid : uidVal,
					equnum : $("#getAllEquinput").val(),
					querynum : 3,
					source : source,
					parameter :parameter
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					$("#getAllEquinput").val(data.equnum);
					var addli = "";
					for (var i = 0; i < data.equlist.length; i++) {
						var equlistval = data.equlist[i];
						var str1 = ''
						if(uid!=equlistval.manid){
							str1 += '<li class="mui-table-view-cell cooperate">';
						}else{
							str1 += '<li class="mui-table-view-cell">';
						}
						str1 += '<span class="wolf-left">设备编号：</span><span id="equNum' + equlistval.code + '" class="wolf-right">'
								+ equlistval.code
								+ '</span><br>';
						str1 += '<span class="wolf-left">线上收益：</span><span class="wolf-right">'
								+ (equlistval.totalMoney == null ? '0.00' : equlistval.totalMoney.toFixed(2))
								+ '元</span><br>';
						str1 += '<span class="wolf-left">投币收益：</span><span class="wolf-right">'+equlistval.coinsMoney+ '元</span><br>';
						str1 += '<span class="wolf-left">设备名称：</span><span class="wolf-right">'
								+ (equlistval.remark == null ? '--' : equlistval.remark)
								+ '</span><br>';
						str1 += '<span class="wolf-left">小区名称：</span><span class="wolf-right">'
								+ (equlistval.name == null ? '--' : equlistval.name)
								+ '</span><br>';
						var csq = equlistval.csq;
						if (csq == 0) {
							if (equlistval.state == 1) {
								str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji3"></span></button></span>';
							} else {
								str1 += '<span><button class="mui-btn mui-btn-danger"><span class="icon iconfont icon-no-signal"></span></button></span>';
							}
						} else if (csq > 0 && csq <= 5) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji3"></span></button></span>';
						} else if (csq > 5 && csq <= 10) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji2"></span></button></span>';
						} else if (csq > 10
								&& csq <= 20) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji1"></span></button></span>';
						} else if (csq > 20
								&& csq <= 31) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji"></span></button></span>';
						}
						str1 += '&nbsp;<span><a href="${hdpath}/equipment/codeDayEarn?code='
								+ equlistval.code + '" class="mui-btn mui-btn-success">统计</a></span>';
						if(uid==equlistval.manid){
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=2&code='
								+ equlistval.code +'" class="mui-btn mui-btn-success">订单</a></span> ';
						}else{
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/equinfo?code='
									+ equlistval.code
									+ '" class="mui-btn mui-btn-success">管理</a></span>';
							if (equlistval.state == 1) {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/remotechargechoose?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">远程</a></span>';
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">远程</button></span>';
							}
							
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=1&code='
								+ equlistval.code
								+ '" class="mui-btn mui-btn-success">订单</a></span>';
							
							if (equlistval.hardversion != '03'
									&& equlistval.hardversion != '04') {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/charge?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">状态</a></span>'
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">状态</button></span>'
							}
						}
						addli += str1;
					}
					$("#allEqu").append(addli);
					if (data.listnum < 5) {
						mui.toast("查询已到最后");
					}
				},
				error : function(e) {
				},
				complete : function() {
					$.bootstrapLoading.end();
				}
			});
		})
		$("#getOffEqu").click(function() {
			var source = $("#muisearchsecond").find("select[name='source']").val();
			var parameter = $("#muisearchsecond").find("input[name='parameter']").val();
			var uidVal = $("#uid").val();
			$.bootstrapLoading.start({
				loadingTips : "加载中..."
			});
			$.ajax({
				url : "${hdpath}/equipment/getAjaxEquList",
				data : {
					uid : uidVal,
					equnum : $("#getOffEquinput").val(),
					querynum : 2,
					source : source,
					parameter :parameter
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					$("#getOffEquinput").val(data.equnum);
					var addli = "";
					for (var i = 0; i < data.equlist.length; i++) {
						var equlistval = data.equlist[i];
						var str1 =  ''
						if(uid!=equlistval.manid){
							str1 += '<li class="mui-table-view-cell cooperate">';
						}else{
							str1 += '<li class="mui-table-view-cell">';
						}
						str1 += '<span class="wolf-left">设备编号：</span><span id="equNum' + equlistval.code + '" class="wolf-right">'
								+ equlistval.code
								+ '</span><br>';
						str1 += '<span class="wolf-left">线上收益：</span><span class="wolf-right">'
								+ (equlistval.totalMoney == null ? '0.00' : equlistval.totalMoney.toFixed(2))
								+ '元</span><br>';
						str1 += '<span class="wolf-left">投币收益：</span><span class="wolf-right">'+equlistval.coinsMoney+ '元</span><br>';
						str1 += '<span class="wolf-left">设备名称：</span><span class="wolf-right">'
								+ (equlistval.remark == null ? '--' : equlistval.remark)
								+ '</span><br>';
						str1 += '<span class="wolf-left">小区名称：</span><span class="wolf-right">'
								+ (equlistval.name == null ? '--' : equlistval.name)
								+ '</span><br>';
						var csq = equlistval.csq;
						str1 += '<span><button class="mui-btn mui-btn-danger"><span class="icon iconfont icon-no-signal"></span></button></span>';
						str1 += '&nbsp;<span><a href="${hdpath}/equipment/codeDayEarn?code='
								+ equlistval.code
								+ '" class="mui-btn mui-btn-success">统计</a></span>';
						if(uid==equlistval.manid){
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=2&code='
								+ equlistval.code +'" class="mui-btn mui-btn-success">订单</a></span> ';
						}else{
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/equinfo?code='
									+ equlistval.code
									+ '" class="mui-btn mui-btn-success">管理</a></span>';
							if (equlistval.state == 1) {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/remotechargechoose?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">远程</a></span>';
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">远程</button></span>';
							}
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=1&code='
								+ equlistval.code + '" class="mui-btn mui-btn-success">订单</a></span>';
							if (equlistval.hardversion != '03'
									&& equlistval.hardversion != '04') {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/charge?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">状态</a></span>'
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">状态</button></span>'
							}
						}
						addli += str1;
					}
					$("#offlineEqu").append(addli);
					if (data.listnum < 5) {
						mui.toast("查询已到最后");
					}
				},
				error : function(e) {
				},
				complete : function() {
					$.bootstrapLoading.end();
				}
			});
		})
		$("#getOnEqu").click(function() {
			var source = $("#muisearchfirst").find("select[name='sourcefirst']").val();
			var parameter = $("#muisearchfirst").find("input[name='parameterfirst']").val();
			var uidVal = $("#uid").val();
			$.bootstrapLoading.start({
				loadingTips : "加载中..."
			});
			$.ajax({
				url : "${hdpath}/equipment/getAjaxEquList",
				data : {
					uid : uidVal,
					equnum : $("#getOnEquinput").val(),
					querynum : 1,
					source : source,
					parameter :parameter
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					$("#getOnEquinput").val(data.equnum);
					var addli = "";
					for (var i = 0; i < data.equlist.length; i++) {
						var equlistval = data.equlist[i];
						var str1 =  ''
						if(uid!=equlistval.manid){
							str1 += '<li class="mui-table-view-cell cooperate">';
						}else{
							str1 += '<li class="mui-table-view-cell">';
						}
						str1 += '<span class="wolf-left">设备编号：</span><span id="equNum' +  equlistval.code + '" class="wolf-right">'
								+ equlistval.code
								+ '</span><br>';
						str1 += '<span class="wolf-left">线上收益：</span><span class="wolf-right">'
								+ (equlistval.totalMoney == null ? '0.00' : equlistval.totalMoney.toFixed(2))
								+ '元</span><br>';
						str1 += '<span class="wolf-left">投币收益：</span><span class="wolf-right">'+equlistval.coinsMoney+ '元</span><br>';
						str1 += '<span class="wolf-left">设备名称：</span><span class="wolf-right">'
								+ (equlistval.remark == null ? '--' : equlistval.remark)
								+ '</span><br>';
						str1 += '<span class="wolf-left">小区名称：</span><span class="wolf-right">'
								+ (equlistval.name == null ? '--' : equlistval.name)
								+ '</span><br>';
						var csq = equlistval.csq;
						if (csq >= 0 && csq <= 5) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji3"></span></button></span>';
						} else if (csq > 5 && csq <= 10) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji2"></span></button></span>';
						} else if (csq > 10
								&& csq <= 20) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji1"></span></button></span>';
						} else if (csq > 20
								&& csq <= 31) {
							str1 += '<span><button class="mui-btn mui-btn-success"><span class="icon iconfont icon-WIFIxinhao-ji"></span></button></span>';
						}
						str1 += '&nbsp;<span><a href="${hdpath}/equipment/codeDayEarn?code='
								+ equlistval.code
								+ '" class="mui-btn mui-btn-success">统计</a></span>';
						if(uid==equlistval.manid){
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=2&code='
								+ equlistval.code +'" class="mui-btn mui-btn-success">订单</a></span> ';
						}else{
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/equinfo?code='
									+ equlistval.code
									+ '" class="mui-btn mui-btn-success">管理</a></span>';
							if (equlistval.state == 1) {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/remotechargechoose?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">远程</a></span>';
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">远程</button></span>';
							}
							str1 += '&nbsp;<span><a href="${hdpath }/equipment/codetotrade?souce=1&code='
								+ equlistval.code + '" class="mui-btn mui-btn-success">订单</a></span>';
							if (equlistval.hardversion != '03'
									&& equlistval.hardversion != '04') {
								str1 += '&nbsp;<span><a href="${hdpath }/merchant/charge?code='
										+ equlistval.code
										+ '" class="mui-btn mui-btn-success">状态</a></span>'
							} else {
								str1 += '&nbsp;<span><button disabled="disabled" class="mui-btn mui-btn-default">状态</button></span>'
							}
						}
						addli += str1;
					}
					$("#onlineEqu").append(addli);
					if (data.listnum < 5) {
						mui.toast("查询已到最后");
					}
				},
				error : function(e) {
				},
				complete : function() {
					$.bootstrapLoading.end();
				}
			});
		})
	})
</script>
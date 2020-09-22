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
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<title>订单列表</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
input[disabled]{color:#000;opacity:1}
tbody td {
	text-align: center;
	vertical-align: middle;
}
</style>
</head>
<body>
	<div class="container">
		<br> <input type="hidden" id="begintimeval" value="${begintime}">
		<input type="hidden" id="endtimeval" value="${endtime}">
		<form action="#" class="mui-input-group">
			<div class="mui-input-row">
				<label>设备号</label>
				<input type="text" name="code" id="code" value="${code}" placeholder="请输入设备号">
			</div>
			<div class="mui-input-row">
				<label>订单号</label>
				<input type="text" name="ordernum" id="ordernum" value="${ordernum}" placeholder="请输入订单号">
			</div>
			<div class="mui-input-row">
				<label>开始时间</label> <input type="date" id="begintime"
					name="begintime">
			</div>
			<div class="mui-input-row">
				<label>结束时间</label> <input type="date" id="endtime" name="endtime">
			</div>
			<div class="mui-input-row">
				<label>设备类型</label>
				<select id="hardverChoose" style="text-align: center;font-size: 14px">
					<option value="1" <c:if test="${hardversion == 1 }">selected="selected"</c:if> >智慧款十路</option>
					<option value="2" <c:if test="${hardversion == 2 }">selected="selected"</c:if> >离线充值机</option>
					<option value="3" <c:if test="${hardversion == 3 }">selected="selected"</c:if> >脉冲</option>
				</select>
			</div>
			<div class="mui-input-row">
				<label>收益(元)</label>
				<input type="text" value="${totalMoney == null ? '0' : totalMoney }" disabled="disabled">
			</div>
			<div style="padding-top: 10px">
				<button type="button" id="submitbtn"
					class="btn btn-primary btn-lg btn-block">查询</button>
			</div>
		</form>
	</div>
	<div style="padding-top: 30px;width: 100%">
		<div>
			<h5 class="mui-content-padded">智慧款充电设备</h5>
			<table class="table table-bordered">
				<thead>
					<tr align="center">
						<td width="20%"><strong>编号</strong></td>
						<td width="10%"><strong>金额</strong></td>
						<td width="45%"><strong>订单号</strong></td>
						<td width="22%"><strong>操作</strong></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${chargeRecords }" var="order">
						<tr align="center">
							<td style="vertical-align: middle;"><font size="1px">${order.equipmentnum }</font></td>
							<td style="vertical-align: middle;">
								<font size="1px">
									<fmt:formatNumber value="${order.expenditure }" pattern="0.00" />
								</font>
							</td>
							<td align="center" style="vertical-align: middle;">
								<font size="1px">${order.ordernum }</font>
							</td>
							<td style="vertical-align: middle;"><a
								href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=1"
								class="btn btn-info">详情</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<h5 class="mui-content-padded">离线充值机</h5>
			<table class="table table-bordered">
				<thead>
					<tr align="center">
						<td width="20%"><strong>编号</strong></td>
						<td width="13%"><strong>金额</strong></td>
						<td width="45%"><strong>订单号</strong></td>
						<td width="22%"><strong>操作</strong></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${offlineRecords }" var="order">
						<tr align="center">
							<td style="vertical-align: middle;"><font size="1px">${order.equipmentnum }</font></td>
							<td style="vertical-align: middle;">
								<font size="1px">
									<fmt:formatNumber value="${order.chargemoney }" pattern="0.00" />
								</font>
							</td>
							<td align="center" style="vertical-align: middle;">
								<font size="1px">${order.ordernum }</font>
							</td>
							<td style="vertical-align: middle;"><a
								href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=2"
								class="btn btn-info">详情</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<h5 class="mui-content-padded">脉冲（模拟投币）</h5>
			<table class="table table-bordered">
				<thead>
					<tr align="center">
						<td width="20%"><strong>编号</strong></td>
						<td width="13%"><strong>金额</strong></td>
						<td width="45%"><strong>订单号</strong></td>
						<td width="22%"><strong>操作</strong></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${inCoinsRecords }" var="order">
						<tr align="center">
							<td style="vertical-align: middle;"><font size="1px">${order.equipmentnum }</font></td>
							<td style="vertical-align: middle;">
								<font size="1px">
									<fmt:formatNumber value="${order.money }" pattern="0.00" />
								</font>
							</td>
							<td align="center" style="vertical-align: middle;">
								<font size="1px">${order.ordernum }</font>
							</td>
							<td style="vertical-align: middle;"><a
								href="${hdpath }/merchant/orderdetail?orderid=${order.id}&hardversion=3"
								class="btn btn-info">详情</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div style="padding-bottom: 50px"></div>
	<nav class="mui-bar mui-bar-tab">
		<a  <c:if test="${manageclick == 1 }"> class="mui-tab-item mui-active" </c:if>
			<c:if test="${manageclick != 1 }"> class="mui-tab-item" </c:if>
			href="${hdpath }/merchant/manage">
			<span class="mui-icon mui-icon-list"></span>
			<span class="mui-tab-label">返回</span>
		</a>
	</nav>
</body>
<script>
	mui('body').on('tap','a',function(){
	    window.top.location.href=this.href;
	});
	$(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
	    	location.replace('/merchant/manage');
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	});
	$(function() {
		function currentMonthNowDay() {
			var today = new Date();
			var h = today.getFullYear();
			var m = today.getMonth() + 1;
			var d = today.getDate();
			if (m < 10) {
				if (d < 10) {
					return h + "-0" + m + "-0" + d;
				} else {
					return h + "-0" + m + "-" + d;
				}
			} else {
				if (d < 10) {
					return h + "-" + m + "-0" + d;
				} else {
					return h + "-" + m + "-" + d;
				}
			}
		}
		if ($("#begintimeval").val() == null || $("#begintimeval").val() == "") {
			$("#begintime").val(currentMonthNowDay());
		} else {
			$("#begintime").val($("#begintimeval").val());
		}
		if ($("#endtimeval").val() == null || $("#endtimeval").val() == "") {
			$("#endtime").val(currentMonthNowDay());
		} else {
			$("#endtime").val($("#endtimeval").val());
		}
		$("#submitbtn").click(function() {
			var hardverVal = $("#hardverChoose option:selected").val();
			var codeval = $("#code").val();
			var ordernumval = $("#ordernum").val();
			var begintimeval = $("#begintime").val();
			var endtimeval = $("#endtime").val();
			if (begintimeval == null || begintimeval == "" || endtimeval == "" || endtimeval == null) {
				alert("时间不可为空")
				return;
			}
			if (begintimeval > endtimeval) {
				alert("开始时间不可以大于结束时间");
				return;
			}
			$.ajax({
				url : '${hdpath}/merchant/getorderlistbycondition',
				data : {
					code : codeval,
					begintime : begintimeval,
					endtime : endtimeval
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data == "0") {
						alert("选取时间不可大于当前时间");
						return;
					} else if (data == "1") {
						window.location.href = "${hdpath}/merchant/orderlistbycondition?code="
								+ codeval
								+ "&ordernum="
								+ ordernumval
								+ "&hardversion="
								+ hardverVal
								+ "&begintime="
								+ begintimeval
								+ "&endtime="
								+ endtimeval;
					}
				},//返回数据填充
			});
		})
	})
</script>
</html>
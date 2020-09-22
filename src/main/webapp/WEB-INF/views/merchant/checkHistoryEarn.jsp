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
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="/mui/css/app.css" />
<script type="text/javascript" src="/mui/js/mui.min.js"></script>
<script src="${hdpath }/js/my.js"></script>
<title>每日收益</title>
</head>
<body>
	<div style="padding-top: 30px">
		<form class="mui-input-group">
			<div class="mui-input-row">
				<label style="font-size: 90%; padding: 13px 15px; font-weight: 400;">开始时间</label>
				<input type="date" id="begintime" name="begintime"
					value="${begintime}">
			</div>
			<div class="mui-input-row">
				<label style="font-size: 90%; padding: 13px 15px; font-weight: 400;">结束时间</label>
				<input type="date" id="endtime" name="endtime" value="${endtime }">
			</div>
		</form>
		<div style="padding-top: 10px" align="center">
			<button id="submitbtn" class="btn btn-success">查询</button>
		</div>
	</div>
	<div style="padding-top: 10px" class="mui-content">
		<ul class="mui-table-view" id="daysEarnDiv">
			<li class="mui-table-view-cell">总计 
			    <span style="position: absolute; right: 90px; color: red">
				<fmt:formatNumber type="number" value="${allTotalMoney}" maxFractionDigits="2" pattern="0.00" />元</span>
				&nbsp;<span style="position: absolute; right: 27px; color: red">
				<fmt:formatNumber type="number" value="${allcoinsmoney}" maxFractionDigits="2" pattern="0.00" />元</span>
			</li>
			<li class="mui-table-view-cell">
				<span style="position: absolute; right: 95px;">线上</span>
					&nbsp;<span style="position: absolute; right: 38px;">投币</span>
			</li>
			<c:forEach items="${daysEarn}" var="earn">
				<li class="mui-table-view-cell">
					<fmt:formatDate value="${earn.countTime}" pattern="yyyy-MM-dd" /> 
					<span style="position: absolute; right: 90px; color: red">
					<fmt:formatNumber type="number" value="${earn.moneytotal-earn.wechatretmoney-earn.alipayretmoney}" maxFractionDigits="2" pattern="0.00" />元</span>
					&nbsp;<span style="position: absolute; right: 27px; color: red">
					<fmt:formatNumber type="number" value="${earn.incoinsmoney==null ? 0.00 : earn.incoinsmoney}" maxFractionDigits="2" pattern="0.00" />元</span> </li>
			</c:forEach>
		</ul>
	</div>
</body>
</html>
<script>
function parseTime(time){
	console.log(time)
	var Y= time.getFullYear()
	var M= (time.getMonth()+1) >= 10 ? (time.getMonth()+1) : '0'+(time.getMonth()+1)
	var D= time.getDate() >= 10 ?time.getDate() : '0'+time.getDate()
	return `${Y}-${M}-${D}`
}
	mui.init({
		swipeBack : true
	//启用右滑关闭功能
	});
	$(function() {
		pushHistory();
		window.addEventListener("popstate", function(e) {
			location.replace("/merchant/index");
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
		function currentMonthSevenDayBefore() {
			var today = new Date();
			today.setDate(today.getDate() - 7);
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
			$("#begintime").val(currentMonthSevenDayBefore());
		} else {
			$("#begintime").val($("#begintimeval").val());
		}
		if ($("#endtimeval").val() == null || $("#endtimeval").val() == "") {
			$("#endtime").val(currentMonthNowDay());
		} else {
			$("#endtime").val($("#endtimeval").val());
		}
		$("#submitbtn").click(function() {
			var begintimeval = $("#begintime").val();
			var endtimeval = $("#endtime").val();
			if (begintimeval == null || begintimeval == ""
					|| endtimeval == null || endtimeval == "") {
				mui.alert("查询时间不可为空");
				return false;
			} else if (begintimeval > endtimeval) {
				mui.alert("开始时间不可以大于结束时间");
				return false;
			} else {
				$.bootstrapLoading.start({ loadingTips: "正在查询..." });
				$.ajax({
					url : '${hdpath}/merchant/queryDaysEarn',
					data : {
						begintime : begintimeval,
						endtime : endtimeval
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data.status == "0") {
							mui.alert("选取时间不可大于当前时间");
						} else if (data.status == "1") {
							$("#daysEarnDiv").html("");
							var daysEarnDivhtml = "<li class='mui-table-view-cell'>总计 <span style='position: absolute; right: 90px; color: red'>" 
							+ data.allTotalMoney.toFixed(2) + "元</span>&nbsp;<span style='position: absolute; right: 27px; color: red'>" 
							+ data.allcoinsmoney.toFixed(2) + "元</span></li><li class='mui-table-view-cell'>"
							+"<span style='position: absolute; right: 95px;'>线上</span>&nbsp;<span style='position: absolute; right: 38px;'>投币</span></li>"
							for(var i = 0;i<data.daysEarn.length;i++){
							             /* str1 = "<li class='mui-table-view-cell'>" + data.daysEarn[i].countIOSTime + "<span style='position: absolute; right: 27px; color: red'>" + data.daysEarn[i].moneytotal.toFixed(2) + "元</span></li>" */
							             str1 = "<li class='mui-table-view-cell'>" + data.daysEarn[i].countTime.substring(0, 10) + "<span style='position: absolute; right: 90px; color: red'>" 
							             + (data.daysEarn[i].moneytotal - data.daysEarn[i].wechatretmoney -data.daysEarn[i].alipayretmoney ).toFixed(2) + "元</span>"
							             +"&nbsp;<span style='position: absolute; right: 27px; color: red'>" 
							             + data.daysEarn[i].incoinsmoney.toFixed(2) + "元</span></li>"
							             daysEarnDivhtml += str1;
							         }
							$("#daysEarnDiv").html(daysEarnDivhtml);
						}
					},//返回数据填充
					complete: function () {
			            $.bootstrapLoading.end();
			        },
			        error: function () {
			            $.bootstrapLoading.end();
			            mui.alert("系统错误！");
			        }
				});
			}
		})
	})
	
</script>
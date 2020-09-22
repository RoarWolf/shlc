<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>商户汇总信息记录</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath }/js/md5.js"></script>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
<style type="text/css">
.table>tbody>tr>td{border: 1px solid #ddd;}
.table>tbody>tr>th{border: 1px solid #ddd;text-align: left;}
.table>tbody .headline{color:green;background: #efefef;}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">商户汇总信息</a></li>
</ul>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd"></div>
  <div class="table table-div">
		<table class="table table-hover">
			<tbody>
				<tr class="headline"><th colspan="10">设备信息:</th></tr>
				<tr>
					<th>设备总数</th><td>${codelines.totalline}</td>
					<th>设备在线总数</th><td>${codelines.onlines}</td>
					<th>设备离线总数</th><td>${codelines.disline}</td>
					<!-- <th></th><td></td> -->
					<td></td>
				</tr>
				<tr class="headline"><th colspan="10">总订单信息:</th></tr>
				<tr>
					<th>资金总额</th><td>${datainfo.totalmoney}</td>
					<th>在线收款</th><td>${datainfo.onlinemoney}</td>
					<th>在线退款</th><td>${datainfo.refonlinemoney}</td>
					<th>提现总额</th><td>${datainfo.extractmoney}</td>
					<th>提现费用</th><td>${datainfo.sumservicecharge}</td>
				</tr>
				<tr>
					<th>设备缴费金额</th><td>${datainfo.paymentmoney}</td>
					<th>收益缴费金额</th><td>${datainfo.incomepaymentmoney}</td>
					<th>微信缴费金额</th><td>${datainfo.wechatpaymentmoney}</td>
					<th>待提现金额</th><td>${datainfo.penextractmoney}</td>
					<th>未提现金额</th><td>${datainfo.earningsmoney}</td>
				</tr>
				<tr class="headline"><th colspan="10">今日订单信息:</th></tr>
				<tr>
					<th>今日金额</th><td>${agentnowdata.totalmoney}</td>
					<th>今日在线收款</th><td>${agentnowdata.onlinemoney}</td>
					<th>今日在线退款</th><td>${agentnowdata.refonlinemoney}</td>
					<th></th><td></td>
					<th></th><td></td>
				</tr>
				<tr class="headline"><th colspan="10"><a href="${hdpath}/pcstatistics/codeearningscollect">设备历史统计</a></th></tr>
				<tr class="headline"><th colspan="10" style="color: #da391d;">
														       注：总订单信息为绑定设备后的在线信息；今日订单信息为当天收入信息</br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;资金总额为在线收款、在线退款和提现金额的总计</br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提现金额为已从平台提取的金额；提现费用为微信提现时收取的费用
				</th></tr>
			</tbody>
		</table>
	</div>
  </div>
  </div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#14'+' a').addClass('at');
	$('#14').parent().parent().parent().prev().removeClass("collapsed");
	$('#14').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#14').parent().parent().parent().addClass("in");
	$('#14').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
</html>
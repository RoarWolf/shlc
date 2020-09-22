<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备系统参数</title>
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
</head>
<body>
<form id="sysParamForm">
	<input type="hidden" id="code" name="code" value="${code }">
	<table border="1" style="width: 100%; height: 100%">
			<tr align="center">
				<td colspan="5">系统参数表</td>
			</tr>
			<tr align="center">
				<td>含义</td>
				<td>数值</td>
				<td>单位</td>
				<td>最大值</td>
				<td>最小值</td>
			</tr>
			<tr align="center">
				<td>设置投币充电时间(单位为分钟)</td>
				<td><input type="text" id="coin_min" name="coinMin" value="${sysparam.coinMin == null ? 240 : sysparam.coinMin}"></td>
				<td>分钟</td>
				<td>999</td>
				<td>0</td>
			</tr>
			<tr align="center">
				<td>设置刷卡充电时间 (单位为分钟)</td>
				<td><input type="text" id="card_min" name="cardMin" value="${sysparam.cardMin == null ? 240 : sysparam.cardMin}"></td>
				<td>分钟</td>
				<td>999</td>
				<td>0</td>
			</tr>
			<tr align="center">
				<td>设置单次投币最大用电量(单位为度,KWH)</td>
				<td><input type="text" id="coin_elec" name="coinElec" value="${sysparam.coinElec == null ? 1 : sysparam.coinElec}"></td>
				<td>0.1度</td>
				<td>15</td>
				<td>0.1</td>
			</tr>
			<tr align="center">
				<td>设置单次刷卡最大用电量(单位为度,KWH)</td>
				<td><input type="text" id="card_elec" name="cardElec" value="${sysparam.cardElec == null ? 1 : sysparam.cardElec}"></td>
				<td>0.1度</td>
				<td>15</td>
				<td>0.1</td>
			</tr>
			<tr align="center">
				<td>设置刷卡扣费金额(单位为元)</td>
				<td><input type="text" id="cst" name="cst" value="${sysparam.cst == null ? 1 : sysparam.cst}"></td>
				<td>元</td>
				<td>15</td>
				<td>0.1</td>
			</tr>
			<tr align="center">
				<td>设置第一档最大充电功率（最大功率以机器支持为准）</td>
				<td><input type="text" id="power_max_1"
					name="powerMax1" value="${sysparam.powerMax1 == null ? 200 : sysparam.powerMax1}"></td>
				<td>瓦</td>
				<td>3500</td>
				<td>50</td>
			</tr>
			<tr align="center">
				<td>设置第二档最大充电功率（最大功率以机器支持为准）</td>
				<td><input type="text" id="power_max_2"
					name="powerMax2" value="${sysparam.powerMax2 == null ? 400 : sysparam.powerMax2}"></td>
				<td>瓦</td>
				<td>3500</td>
				<td>50</td>
			</tr>
			<tr align="center">
				<td>设置第三档最大充电功率（最大功率以机器支持为准）</td>
				<td><input type="text" id="power_max_3"
					name="powerMax3" value="${sysparam.powerMax3 == null ? 600 : sysparam.powerMax3}"></td>
				<td>瓦</td>
				<td>3500</td>
				<td>50</td>
			</tr>
			<tr align="center">
				<td>设置第四档最大充电功率（最大功率以机器支持为准）</td>
				<td><input type="text" id="power_max_4"
					name="powerMax4" value="${sysparam.powerMax4 == null ? 800 : sysparam.powerMax4}"></td>
				<td>瓦</td>
				<td>3500</td>
				<td>50</td>
			</tr>
			<tr align="center">
				<td>设置第二档充电时间百分比</td>
				<td><input type="text" id="power_2_tim"
					name="powerTim2" value="${sysparam.powerTim2 == null ? 75 : sysparam.powerTim2}"></td>
				<td>%</td>
				<td>100</td>
				<td>1</td>
			</tr>
			<tr align="center">
				<td>设置第三档充电时间百分比</td>
				<td><input type="text" id="power_3_tim"
					name="powerTim3" value="${sysparam.powerTim3 == null ? 50 : sysparam.powerTim3}"></td>
				<td>%</td>
				<td>100</td>
				<td>1</td>
			</tr>
			<tr align="center">
				<td>设置第四档充电时间百分比</td>
				<td><input type="text" id="power_4_tim"
					name="powerTim4" value="${sysparam.powerTim4 == null ? 25 : sysparam.powerTim4}"></td>
				<td>%</td>
				<td>100</td>
				<td>1</td>
			</tr>
			<tr align="center">
				<td>是否支持余额回收（1为支持 0为不支持)</td>
				<td><input type="text" id="sp_rec_mon"
					name="spRecMon" value="${sysparam.spRecMon == null ? 1 : sysparam.spRecMon}"></td>
				<td>无</td>
				<td>1</td>
				<td>0</td>
			</tr>
			<tr align="center">
				<td>是否支持断电自停（1为支持 0为不支持)</td>
				<td><input type="text" id="sp_full_empty"
					name="spFullEmpty" value="${sysparam.spFullEmpty == null ? 1 : sysparam.spFullEmpty}"></td>
				<td>无</td>
				<td>1</td>
				<td>0</td>
			</tr>
			<tr align="center">
				<td>设置充电器最大浮充功率 （功率为瓦，当充电器功率低于这个值的话，可视为充电器已充满)</td>
				<td><input type="text" id="full_power_min"
					name="fullPowerMin" value="${sysparam.fullPowerMin == null ? 30 : sysparam.fullPowerMin}"></td>
				<td>瓦</td>
				<td>200</td>
				<td>10</td>
			</tr>
			<tr align="center">
				<td>设置浮充时间 （充电器充满变绿灯之后的，继续浮充时间，单位为分钟）</td>
				<td><input type="text" id="full_charge_time"
					name="fullChargeTime" value="${sysparam.fullChargeTime == null ? 120 : sysparam.fullChargeTime}"></td>
				<td>分钟</td>
				<td>240</td>
				<td>30</td>
			</tr>
			<tr align="center">
				<td>是否初始显示电量 （此功能是否支持和设备相关）</td>
				<td><input type="text" id="elec_time_first"
					name="elecTimeFirst" value="${sysparam.elecTimeFirst == null ? 0 : sysparam.elecTimeFirst}"></td>
				<td>无</td>
				<td>1</td>
				<td>0</td>
			</tr>
		</table>
</form>
<table border="1" style="width: 100%; height: 100%">
	<tr align="center">
		<td><button id="readsystem" value="30" class="btn btn-info">读取参数</button></td>
		<td colspan="3">更新时间：
			<span id="timespan"><c:if test="${sysparam.updateTime != null}"><fmt:formatDate value="${sysparam.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></c:if>
					<c:if test="${sysparam.updateTime == null}">无记录，请点击读取参数</c:if>
			</span></td>
		<td><button id="submitbtn" class="btn btn-info">保存参数</button>
	</tr>
</table>
</body>
</html>
<script>
$(function() {
	function post_data(){
		return false;
	}
	$("#readsystem").click(function() {
		$.bootstrapLoading.start({ loadingTips: "正在获取数据..." });
		$.ajax({
			url : '${hdpath}/readsysteminfo',
			data : {
				code : $("#code").val()
			},
			type : "POST",
			cache : false,
			success : function(data) {
				if (data.wolfcode == "1001") {
					mui.alert(data.wolfmsg);
				} else {
					$("#coin_min").val(data.param1);
					$("#card_min").val(data.param2);
					$("#coin_elec").val(data.param3/10);
					$("#card_elec").val(data.param4/10);
					$("#cst").val(data.param5/10);
					$("#power_max_1").val(data.param6);
					$("#power_max_2").val(data.param7);
					$("#power_max_3").val(data.param8);
					$("#power_max_4").val(data.param9);
					$("#power_2_tim").val(data.param10);
					$("#power_3_tim").val(data.param11);
					$("#power_4_tim").val(data.param12);
					$("#sp_rec_mon").val(data.param13);
					$("#sp_full_empty").val(data.param14);
					$("#full_power_min").val(data.param15);
					$("#full_charge_time").val(data.param16);
					$("#elec_time_first").val(data.param17);
					$("#timespan").html(data.readtime);
				}
			},//返回数据填充
			complete: function () {
	            $.bootstrapLoading.end();
	        }
		});
	})
	$("#submitbtn").click(function() {
		var reg=/^[0-9]+$/;
		var firstreg=/[0-9]+$/;
		var elecreg=/^\d+(\.\d)?$/;
		var coin_minval = $("#coin_min").val();
		var card_minval = $("#card_min").val();
		var coin_elecval = $("#coin_elec").val();
		var card_elecval = $("#card_elec").val();
		var cstval = $("#cst").val();
		var power_max_1val = $("#power_max_1").val();
		var power_max_2val = $("#power_max_2").val();
		var power_max_3val = $("#power_max_3").val();
		var power_max_4val = $("#power_max_4").val();
		var power_2_timval = $("#power_2_tim").val();
		var power_3_timval = $("#power_3_tim").val();
		var power_4_timval = $("#power_4_tim").val();
		var sp_rec_monval = $("#sp_rec_mon").val();
		var sp_full_emptyval = $("#sp_full_empty").val();
		var full_power_minval = $("#full_power_min").val();
		var full_charge_timeval = $("#full_charge_time").val();
		var elec_time_firstval = $("#elec_time_first").val();
		var parsedata = parseInt(elec_time_firstval);
		console.log(parseInt(elec_time_firstval));
		if (coin_minval == null || coin_minval == "" || !reg.test(coin_minval)) {
			mui.alert("设置投币充电时间参数不可为空且不可为非法字符")
		} else if (card_minval == null || card_minval == "" || !reg.test(card_minval)) {
			mui.alert("设置刷卡充电时间参数不可为空且不可为非法字符")
		} else if (coin_elecval == null || coin_elecval == "" || !elecreg.test(coin_elecval)) {
			mui.alert("设置单次投币最大用电量参数不可为空且不可为非法字符")
		} else if (card_elecval == null || card_elecval == "" || !elecreg.test(card_elecval)) {
			mui.alert("设置单次刷卡最大用电量参数不可为空且不可为非法字符")
		} else if (cstval == null || cstval == "" || !elecreg.test(cstval)) {
			mui.alert("设置刷卡扣费金额参数不可为空且不可为非法字符")
		} else if (power_max_1val == null || power_max_1val == "" || !reg.test(power_max_1val)) {
			mui.alert("设置第一档最大充电功率参数不可为空且不可为非法字符")
		} else if (power_max_2val == null || power_max_2val == "" || !reg.test(power_max_2val)) {
			mui.alert("设置第二档最大充电功率参数不可为空且不可为非法字符")
		} else if (power_max_3val == null || power_max_3val == "" || !reg.test(power_max_3val)) {
			mui.alert("设置第三档最大充电功率参数不可为空且不可为非法字符")
		} else if (power_max_4val == null || power_max_4val == "" || !reg.test(power_max_4val)) {
			mui.alert("设置四档最大充电功率参数不可为空且不可为非法字符")
		} else if (power_2_timval == null || power_2_timval == "" || !reg.test(power_2_timval)) {
			mui.alert("设置第二档充电时间百分比参数不可为空且不可为非法字符")
		} else if (power_3_timval == null || power_3_timval == "" || !reg.test(power_3_timval)) {
			mui.alert("设置第三档充电时间百分比参数不可为空且不可为非法字符")
		} else if (power_4_timval == null || power_4_timval == "" || !reg.test(power_4_timval)) {
			mui.alert("设置第四档充电时间百分比参数不可为空且不可为非法字符")
		} else if (sp_rec_monval == null || sp_rec_monval == "" || !reg.test(sp_rec_monval)) {
			mui.alert("设置是否支持余额回收参数不可为空且不可为非法字符")
		} else if (sp_full_emptyval == null || sp_full_emptyval == "" || !reg.test(sp_full_emptyval)) {
			mui.alert("设置是否支持断电自停参数不可为空且不可为非法字符")
		} else if (full_power_minval == null || full_power_minval == "" || !reg.test(full_power_minval)) {
			mui.alert("设置充电器最大浮充功率参数不可为空且不可为非法字符")
		} else if (full_charge_timeval == null || full_charge_timeval == "" || !reg.test(full_charge_timeval)) {
			mui.alert("设置浮充时间参数不可为空且不可为非法字符")
		} else if (elec_time_firstval == null || elec_time_firstval == "" || (parseInt(elec_time_firstval) > 1 || parseInt(elec_time_firstval) < -1)) {
			mui.alert("设置是否初始显示电量 参数不可为空且不可为非法字符")
		} else {
			$.bootstrapLoading.start({ loadingTips: "正在设置..." });
			$.ajax({
				url : '/equipment/setSysParam',
				data : $("#sysParamForm").serialize(),
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.status == "0") {
						mui.alert("设置失败");
					} else {
						mui.alert("设置成功");
						location.reload();
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		}
	})
})
</script>
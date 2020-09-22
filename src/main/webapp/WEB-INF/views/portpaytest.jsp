<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>请选择一下参数进行支付</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
<div class="container">
	<input type="hidden" id="payport" value="${payport }">
	<input type="hidden" id="code" value="${code }">
	<div>
		<font>当前充电机号为：${code }</font>
		<font>端口为：${payport }</font>
	</div>
	<hr>
	<div>
		<input type="radio" name="chargeparam" value="1">1元 -- 240分钟 -- 1度电<br>
		<input type="radio" name="chargeparam" value="2">2元 -- 480分钟 -- 2度电<br>
		<input type="radio" name="chargeparam" value="3">3元 -- 720分钟 -- 3度电<br>
	</div>
	<button class="btn btn-success" id="test">确认提交</button>
</div>
<script>
	$(function() {
		$("#test").click(function() {
			var elecval = $('input:radio[name="chargeparam"]:checked').val();
			var payportval = $("#payport").val();
			var codeval = $("#code").val();
			var timeval = elecval * 240;
			$.ajax({
				url : '${hdpath}/testpaytoport',
				data : {
					payport : payportval,
					time : timeval,
					elec : elecval,
					code : codeval
				},
				type : "POST",
				cache : false,
				success : function(data) {
				},//返回数据填充
			});
		})
	})
</script>
</body>
</html>
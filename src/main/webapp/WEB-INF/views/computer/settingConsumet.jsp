<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<title>收益结算</title>
<style>
#formWrapper {
	width: 400px;
	margin: 150px auto;
}
#formWrapper .from_item {
	overflow: hidden;
	padding: 15px 0;
}
#formWrapper .from_item div:nth-child(1){
	width:　70px;
	float: left;
}
#formWrapper .from_item div:nth-child(2){
	width:　50px;
	overflow: hidden;
    text-align: center;
}
#formWrapper .from_item div:nth-child(2) input {
	width: 80%;
	display: block;
	margin: 0 auto;
}
#formWrapper .from_item div:nth-child(2) button {
	padding: 2px 12px;
    width: 100px;
}

</style>
</head>
<body  style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">商户电量、时间结算</a></li>
</ul>
<div id="formWrapper">
	<div class="from_item">
		<div>起始时间</div>
		<div><input id="past" name="past" type="text" class="input"></div>
	</div>
	<div class="from_item">
		<div>截止时间</div>
		<div><input id="cutoff" name="cutoff" type="text" class="input"></div>
	</div>
	<div class="from_item">
		<div>类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</div>
		<div><input id="type" name="type" type="text" class="input"></div>
	</div>
	<div class="from_item">
		<div>操&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作</div>
		<div><button class="btn btn-success" id="calculate">计算</button></div>
	</div>
	<div class="from_item">
		<div>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</div>
		<div>起始时间指计算到距今天最早时间；截止时间指计算到距今天的莫一天时间；类型指【1:指计算据今天的某一天   2:指计算莫一段时间   3:指计算昨日到某一天的时间】</div>
	</div>
</div>

		<!-- shijianyi 	<input id="past" name="past" type="text" class="input"></br>
		s	<input id="cutoff" name="cutoff" type="text" class="input"></br>
			<input id="type" name="type" type="text" class="input"></br>
			<button class="btn btn-success" id="calculate">计算</button> -->
<script>
$(function(){
	$("button[id^='calculate']").click(function() {
		var past = $("#past").val().trim();
		var cutoff = $("#cutoff").val().trim();
		var type = $("#type").val().trim();
		if(type == '' || type == null){
			alert("请选择类型")
			 return false
		}
		
		if(past == '' || past == null){
			alert("选择计算时间")
			 return false
		}
		if(type==2){
			if(cutoff == '' || cutoff == null){
				alert("选择截止计算时间")
				 return false
			}
		}
		$.ajax({
	  		url: "${hdpath}/systemSetting/disposeDealerElect",
	  		data: {past : past, cutoff : cutoff, type : type,},
	  		success: function(res){
	  			if(res == "succeed"){
	  				alert("成功")
	  			}else{
	  				alert("失败")
	  			}
	  		},
	  		error: function(err){
				alert("错误")
	  		}
	  	})
	})
})

</script>
</body>
</html>
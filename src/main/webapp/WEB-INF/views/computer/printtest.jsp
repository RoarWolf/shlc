<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<title>打印</title>
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
  <li><a href="javascript:void(0)" target="right" class="icon-home">打印</a></li>
</ul>
<div id="formWrapper">
	<div class="from_item">
		<div>打印Demo链接</div>
		<div><a href="${hdpath}/print/printDemooutput"><button class="btn btn-success">打印Demo链接</button></a></div>
	</div>
	<div class="from_item">
		<div>打&nbsp;印&nbsp;Demo </div>
		<div><button class="btn btn-success" id="printDemo">打&nbsp;印&nbsp;Demo</button></div>
	</div>
	<div class="from_item">
		<div>直接打印 </div>
		<div><button class="btn btn-success" id="printTestDirect">直接打印</button></div>
	</div>
	<div class="from_item">
		<div>弹框打印</div>
		<div><button class="btn btn-success" id="printpopup">弹框打印</button></div>
	</div>
	<div class="from_item">
		<div>弹框打印测试</div>
		<div><button class="btn btn-success" id="printPopupTest">弹框打印测试</button></div>
	</div>
	<div class="from_item">
		<div>小票打印（结算单）测试</div>
		<div><button class="btn btn-success" id="printReceiptTest">打印小票（结算单）</button></div>
	</div>
	<div class="from_item">
		<div>小票打印（点餐单）测试  </div>
		<div><button class="btn btn-success" id="printReceiptSettle">打印三（小票）</button></div>
	</div>
	<div class="from_item">
		<div>小票打印（点餐单、与结算单）测试</div>
		<div><button class="btn btn-success" id="printResult">打印四（小票）</button></div>
	</div>
	<div id="toolbar"><input id="butn" type="button" value="打印" onclick="printit();"/></div>
	<!-- <div class="from_item">
		<div>操&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作</div>
		<div><button class="btn btn-success" id="calculate">计算</button></div>
	</div> -->
</div>
<script>
function printit(){
	if(confirm('确认打印吗？')){
		
		document.getElementById('butn').style.display="none";//隐藏
		window.print();//打印
		document.getElementById('butn').style.display="inline";//显示
	}
}
$(function(){
	var content = document.documentElement.outerHTML;
	console.log();
	/* 打印Demo */
	$("button[id^='printDemo']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printDemo",
	  		/* data: {past : content}, */
	  		data: {},
	  		success: function(res){
	  			alert("结果zeroprintDemo1   "+res)
	  		},
	  		error: function(err){
				alert("结果zeroprintDemo2   错误")
	  		}
	  	})
	})
	
	/* 直接打印 */
	$("button[id^='printTestDirect']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printTestDirect",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果text   "+res)
	  		},
	  		error: function(err){
				alert("结果text   错误")
	  		}
	  	})
	})
	
	/* 弹框打印 */
	$("button[id^='printpopup']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printTestPopup",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果zero 1  "+res)
	  		},
	  		error: function(err){
				alert("结果zero 2  错误")
	  		}
	  	})
	})
	
	/* 弹框打印测试 */
	$("button[id^='printPopupTest']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printPopupTest",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果1   "+res)
	  		},
	  		error: function(err){
				alert("结果1   错误")
	  		}
	  	})
	})
	
	/*  */
	$("button[id^='printReceiptTest']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printReceiptTest",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果2   "+res)
	  		},
	  		error: function(err){
				alert("结果2   错误")
	  		}
	  	})
	})
	
	/*  */
	$("button[id^='printReceiptSettle']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printTest3",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果3   "+res)
	  		},
	  		error: function(err){
				alert("结果3   错误")
	  		}
	  	})
	})
	
	$("button[id^='printResult']").click(function() {
		$.ajax({
	  		url: "${hdpath}/print/printResult",
	  		data: {past : "1",},
	  		success: function(res){
	  			alert("结果4   "+res)
	  		},
	  		error: function(err){
				alert("结果4   错误")
	  		}
	  	})
	})
})

</script>
</body>
</html>
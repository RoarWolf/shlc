<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>银联支付测试页面</title>
<script src="${hdpath}/js/jquery.js"></script>
<style>



</style>


</head>
<body>
	<h1>成功后的回调地址</h1>
	<button id="a">点我用户授权</button>
	<br/>
	<br/>
	<br/>
	<button id="b">点我进行支付</button>
	<script type="text/javascript">
	$(function(){
		$('#a').on('click',function(){
			$.ajax({
				url: '/unionpay/yinlianTest',
				type:'post',
				success:function(res){
					alert(JSON.stringify(res))					
				},
				error:function(e){
					alert(e)					
				},
			})
		})
	});
	$(function(){
		$('#b').on('click',function(){
			$.ajax({
				url: '/unionpay/pay',
				type:'get',
				success:function(res){
					alert(JSON.stringify(res))					
				},
				error:function(e){
					alert(e)					
				},
			})
		})
	})
	</script>
</body>
</html>
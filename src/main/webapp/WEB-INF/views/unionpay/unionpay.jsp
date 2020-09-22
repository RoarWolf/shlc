<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>银联测试支付页面</title>
	<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
	<style>
		* {
			padding: 0;
			margin: 0;
		}
		body {
			background-color: #336699;
		}
		label {
			color: #EEE;
			font-weight: block
			font-size: 16px;
			display: block;
			margin-bottom: 1vh;
		}
		form {
			display: flex;
			flex-direction: column;
			justify-content: center;
			padding: 15vh 15vw 5vh;
		}
		input {
			background-color: rgba(0,0,0,.1);
			height: 28px;
			border: none;
			outline: none;
			color: #fff;
			padding: 7px 20px;
			font-size: 14px;
		}
		input::placeholder {
			color: #BBB;
		}
		.contral {
			text-align: center;
		}
		.submit {
			width: 70vw;
			margin-top: 20px;
			border:none;
			padding: 11px 12px;
			color: #FFF;
			transition: .3s;
		}
		.submit.active {
			background-color: #FF6600;
		}
		#result {
			position: fixed;
			left: 0;
			right: 0;
			bottom: 0;
			height: 20vh;
			border: 1px solid #666;
			color: #ccc;
			overflow-y: auto;
			padding: 15px;
		}
	</style>
</head>
<body>
	<div class="app">
		<form action="/unionpay/pay" method="post">
			<label for="money">支付金额</label>
			<input type="text" placeholder="请输入支付金额" id="money" name="money">
			<input type="hidden" id="userId" value="${userId}" name="userId">
			<div class="contral">
				<button type="submit" id="submit" class="submit active" >点击支付</button>
			</div>
		</form>
		
		<div id="result">
		</div>
	</div>

	<!-- <script>
		var moneyEl= document.getElementById('money')
		var button= document.getElementById('submit')
		var result= document.getElementById('result')
		var userId= document.getElementById('userId').value
		var money
		var reg= /^\d+(\.\d+)?$/
		moneyEl.addEventListener('keyup',function(event){
			var target= event.target || event.srcElement
			money= target.value.trim()
			if(!reg.test(money)) {
				 button.classList.remove('active')
				 button.disabled= true
				 return
			}
			button.disabled= false
			button.classList.add('active')
		})


		button.addEventListener('click',function(e){
			console.log(1111)
			$.ajax({
				url: '/unionpay/pay',
				data: {money: money,userId:userId },
				type: 'post',
				success: function(res){
					result.innerHTML= 'success<br/>'+JSON.stringify(res,null,2)
				},
				error: function(err) {
					result.innerHTML= 'error<br/>'+JSON.stringify(err,null,2)
				}
			})
		})
	</script> -->
</body>
</html>
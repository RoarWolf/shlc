<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>填写银行卡信息</title>
	<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
	<link rel="stylesheet" href="${hdpath}/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
	<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
	<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script> 
	<script src="${hdpath}/js/jquery.js"></script>
	<style>
		.app {
			padding: 15px;
			font-size: 14px;
			counter-reset: #666;
		}
		.app .bankCardInfo h3{
			font-weight: normal;
			color: #97a6bd;
			margin-bottom: 10px;
		}
		.app .bankCardInfo .item {
			background-color: #fff;
			line-height: 45px;
			margin-bottom: 10px;
			border-radius: 4px;
		}
		.app .bankCardInfo .item label {
			float: left;
			width: 88px	;
			padding: 0 10px 0 15px;
			color: #333;
		}
		.app .bankCardInfo .item input {
			overflow: hidden;
			width: auto;
			margin: 0;
			border: none;
			outline: none;
			width: calc(100% - 88px);
		}
		.confirmBtn {
			margin-top: 20px;
		}
		.confirmBtn button {
			width: 100%;
			line-height: 28px;
		}
	</style>
</head>
<body>
<!-- 	<div class="app">
		<div class="content">
			<div class="title">请填写以下对公账户信息</div>
			<form>
				<label for="company">
					<span>公司名称：</span>
					<input type="text" id="company" name="company"
					placeholder="请输入公司名称">
				</label>
				<label for="name">
					<span>联系人：</span>
					<input type="text" id="name" name="name" placeholder="请输入联系人姓名">
				</label>
				<label for="cardNum">
					<span>银行账号：</span>
					<input type="text" id="cardNum" name="cardNum"
					placeholder="请输入对公账户卡号">
				</label>
				<label for="info">
					<span>开户银行：</span>
					<input type="text" id="info" name="info"
					placeholder="请输入对公账户所属银行">
				</label>
			</form>
		</div>
		<div class="submitBtnDiv">
			<button class="mui-btn mui-btn-success" id="submitBtn">提交</button> 
		</div>
	</div> -->
	
	<div class="app">
		<div class="bankCardInfo">
			<h3>请填写以下绑定银行卡信息</h3>
			<form action="">
				<div class="item">
					<label for="company" class="clearfix">
						<span>公司名称</span>
					</label>
					<input type="text" name="company" id="company" placeholder="请填写公司名称">
				</div>
				
				<div class="item">
					<label for="name" class="clearfix">
						<span>联系人</span>
					</label>
					<input type="text" name="name" id="name" placeholder="请填写联系人">
				</div>
					
				<div class="item">
					<label for="cardNum" class="clearfix">
					<span>银行卡号</span>
					</label>
					<input type="text" name="cardNum" id="cardNum" placeholder="请填写对公账户卡号">
				</div>
				
				<div class="item">
					<label for="info" class="clearfix">
					<span>开户行</span>
					</label>
					<input type="text" name="info" id="info" placeholder="请填写对公账户所属银行">
				</div>
			</form>
			<div class="confirmBtn">
				<button type="button" class="mui-btn mui-btn-success submitbtn">确认提交</button>
			</div>
		</div>
	</div>
	<script>
		$(function(){
			$('.submitbtn').click(function(){
				handleUpData()
			})

			function handleUpData(){
				var name= $('input[name="name"]').val().trim()
				var cardNum= $('input[name="cardNum"]').val().trim()
				var info= $('input[name="info"]').val().trim()
				var company= $('input[name="company"]').val().trim()
				if(company== '' || company== null){
					mui.toast('请输入公司名称',1500)
					return
				}
				if(name== '' || name== null){
					mui.toast('请输入真实姓名',1500)
					return
				}
				if(cardNum== '' || cardNum== null){
					mui.toast('请输入对公账户卡号',1500)
					return
				}
				var reg= /^[0-9]\d+$/
				if(!reg.test(cardNum)){
					mui.toast('请输入正确的对公账户卡号',1500)
					return
				}
				
				/* 
				var reg= /^[0-9]\d{11,19}$/
				if(!reg.test(cardNum)){
					mui.toast('请输入正确的对公账户卡号',1500)
					return
				} 
				*/
				if(info== '' || info== null){
					mui.toast('请输入银行卡所属银行',1500)
					return
				}
				$.ajax({
					url : '${hdpath}/merchant/addCompanyCardAccess',
					data : {
						name : name,
						cardNum : cardNum,
						info : info,
						company : company
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data == 0) {
							mui.alert('错误用户', '银行卡绑定', function() {
							});
							return ;
						} else if (data == 2) {
							mui.alert('银行卡已存在，不需要再添加', '银行卡绑定', function() {
							});
							return ;
						} else if (data == 3) {
							mui.alert('与首次绑定的银行卡实名不一致，添加不予通过', '银行卡绑定', function() {
							});
							return ;
						} else if (data == 1) {
							mui.alert('添加成功', '银行卡绑定', function() {
								window.location.href = "${hdpath}/merchant/mybankcard";
							});
						}
					},//返回数据填充
				});
			}

	
		})
	</script>
</body>
</html>

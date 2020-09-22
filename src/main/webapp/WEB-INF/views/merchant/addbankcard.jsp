<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>填写银行卡信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script> 
<style type="text/css">
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
			/*height: 40px;*/
			line-height: 45px;
			margin-bottom: 10px;
			border-radius: 4px;
			position: relative;
		}
		.app .bankCardInfo .item .helpIcon {
			position: absolute;
			right: 15px;
			top: 50%;
			font-size: 20px;
			color: #666;
			transform: translateY(-50%);
			-webkit-transform: translateY(-50%);
			-moz-transform: translateY(-50%);
			-o-transform: translateY(-50%);
			-ms-transform: translateY(-50%)
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
		#popover {
			top: 50%;
			left: 50%;
			transform: translate(-50%,-50%);
			-webkit-transform: translate(-50%,-50%);
			-moz-transform: translate(-50%,-50%);
			-o-transform: translate(-50%,-50%);
			-ms-transform: translate(-50%,-50%);
			max-height: 70%;
			overflow-y: auto;
		}
		#popover>ul {
			max-height:none;
			padding-bottom: 20px;
		}
		#popover>ul li.ch,
		#popover>ul li.ch a{
			display: inline;
			line-height: 1.8em;
			text-decoration: none;
		}
		#popover>ul li.ch:after {
			height: 0;
		}
		#popover>ul a {
		font-size: 14px;
		color: #666;
		text-align: center
		}
		#popover>ul li:nth-child(1) a{
			font-weight: bold;
			text-decortain: none;
		}
</style>
</head>
<body>
	<div class="app">
		<div class="bankCardInfo">
			<h3>请填写以下绑定银行卡信息</h3>
			<form action="">
				<div class="item">
					<label for="personName" class="clearfix">
						<span>开户姓名</span>
					</label>
					<input type="text" name="personName" id="personName" placeholder="请填写开户姓名">
				</div>
					
				<div class="item">
					<label for="bankCardNum" class="clearfix">
					<span>银行卡号</span>
					</label>
					<input type="text" name="bankCardNum" id="bankCardNum" placeholder="请填写银行卡号">
					<span class="mui-icon mui-icon-help helpIcon"></span>
				</div>
				
				<div class="item">
					<label for="bankName" class="clearfix">
					<span>开户行</span>
					</label>
					<input type="text" name="bankName" id="bankName" placeholder="请填写开户行">
				</div>
			</form>
			<div class="confirmBtn">
				<button type="button" class="mui-btn mui-btn-success submitbtn">确认提交</button>
			</div>
		</div>
	</div>
	<div id="popover" class="mui-popover">
	  <ul class="mui-table-view">
	    <li class="mui-table-view-cell"><a href="#">微信官方仅支持以下银行卡</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">招商银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">工商银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">建设银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">农业银行</a></li>
	    
	    <li class="mui-table-view-cell ch"><a href="#">民生银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">兴业银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">平安银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">交通银行</a></li>
	    
	    <li class="mui-table-view-cell ch"><a href="#">中信银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">光大银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">华夏银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">中国银行</a></li>
	    
	    <li class="mui-table-view-cell ch"><a href="#">北京银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">宁波银行</a></li>
	    
	    <li class="mui-table-view-cell ch"><a href="#">邮政储蓄</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">浦发银行</a></li>
	    <li class="mui-table-view-cell ch"><a href="#">广发银行</a></li>
	  </ul>
	</div>
</body>
</html>
<script>
$(function() {
	var bankName=  {
		 	CMB: "招商银行",
		 	ICBC: "工商银行",
		 	CCB: "建设银行",
		 	ABC: "农业银行",
		 	CMBC: "民生银行",
		 	CIB: "兴业银行",
		 	SPABANK: "平安银行",
		 	COMM: "交通银行",
		 	CITIC: "中信银行",
		 	CEB: "光大银行",
		 	HXBANK: "华夏银行",
		 	BOC: "中国银行",
		 	BJBANK: "北京银行",
		 	NBBANK: "宁波银行",
		 	PSBC: "邮政储蓄银行",
		 	SPDB: "浦发银行",
		    GDB: "广发银行",
		 }
	var bankIsCheck= false //默认银行卡没被校验
	/* 点击弹出微信官方支持的银行卡种类 */
	$('.helpIcon').on('click',function(){
		mui('.mui-popover').popover('show');
	})
	$('#bankCardNum').on('blur',function(){ //输入银行卡号失去焦点的时候查询开户行，并设置开户行的value
		var bankCardNumVal= $(this).val().trim() //银行卡号
		if(bankCardNumVal != '' && bankCardNumVal != null){
			$.ajax({
				url: 'https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8',
				data: {
					cardNo: bankCardNumVal,
					cardBinCheck: true
				},
				type: "post",
				success: function(res){
					console.log(res)
					if(res.validated){
						if( typeof bankName[res.bank] == 'undefined'){
							mui.alert('对不起，你输入的银行卡不在使用范围之内，请您更换银行卡！', '提示', function() {
								bankIsCheck= false
							});
						}else{
							$('#bankName').val(bankName[res.bank])
							bankIsCheck= true
						}
						
					}else{
						mui.toast('请输入正确的银行卡号')
						bankIsCheck= false
					}
				},
				error: function(err){
					bankIsCheck= false
					console.log(err)
				}
			})	
		}
	})
	$('.submitbtn').on('tap',function(){
		console.log(bankIsCheck)
		if(!bankIsCheck){
			mui.alert('请您输入正确的银行卡');
			return
		}
		var personName= $('#personName').val().trim() //开户姓名
		var bankCardNum= $('#bankCardNum').val().trim() //银行卡号
		var bankName= $('#bankName').val().trim() //开户行
		/* 校验开户姓名，卡号  开户行*/
		if(personName == '' || personName == null){
			mui.alert('请填写开户姓名')
			return
		}
		if(bankCardNum == '' || bankCardNum == null){
			mui.alert('请填写银行卡号')
			return
		}
		if(bankName == '' || bankName == null){
			mui.alert('请填写开户行')
			return
		}
		
		$.ajax({
			url : '${hdpath}/merchant/addbankcardaccess',
			data : {
				realname : personName,
				bankcardnum : bankCardNum,
				bankname : bankName,
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
		
	})
	
})
</script>
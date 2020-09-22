<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<title>对公账户提现</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/css/base.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<style type="text/css">
.app {
			padding: 15px 0;
		}
		h1 {
			font-size: 18px;
			text-align: center;
			margin-bottom: 20px;
			color: #666;
			font-weight: bold;
		}
		.cashType {
			background-color: #f5f7fa;
			padding: 15px;
		}
		.cashType .left {
			width: 20%;
			height: 50px;
			font-size: 14px;
			float: left;
		}
		.cashType .right {
			overflow: hidden;
			font-size: 14px;
			padding-left: 15px;
		}
		.cashType .right p {
			line-height: 2em;
		}
		.cashType .right .right_arr{
			float: right;
			color: #999;
			font-size: 22px;
		}
		.cashInfo {
			background-color: #fff;
			padding: 15px;
		}
		.cashInfo h3 {
			font-size: 14px;
			color: #777;
			margin-bottom: 5px;
			font-weight: normal;
		}
		.cashInfo form {
			position: relative;

		}
		.cashInfo form input {
			height: 50px;	
			font-size: 28px;
			color: #333;
			margin: 0;
			padding-left: 35px;
			outline: none;
			border: none;
			border-bottom: 1px solid #f7f7f7;
			margin-bottom: 5px;
		}
		.iconMon {
			position: absolute;
			height: 100%;
			line-height: 50px;
			font-size: 40px;
			color: #333;
		}
		
		.btnDiv {
			text-align: center;
			margin-top: 35px;
		}
		.btnDiv button{
			width: 92%;
			height: 40px;
		}
		.otherType {
			font-size: 14px;
			color: #0984B5;
			text-align: center;
			text-decoration: underline;
			position: absolute;
			width: 100%;
			left: 0;
			bottom: 20%;
		}
		.allMoneySpan {
			margin-left: 5px;
			color: #0984B5;
			font-size: 14px;
		}
		strong {
			color: #999;
		}
		.publicAccount li{
			padding: 20px 15px;
			border: 1px solid #f7f7f7;
			background-color: #f5f7fa;
			border-radius: 4px;
			font-size: 14px;
			margin-bottom: 10px;
			position:relative;
		}
		.publicAccount li p {
			color: #666;
			min-width: 49%;
			display: inline-block;
		}

		.extraCharging {
			display: none;
		}
		.tipInfo {
			display: none;
		}
		.tipInfo p{
			color: #D14B50;
		}
</style>
</head>
<body>
	<%-- <div class="mui-content">
		<input type="hidden" id="balance" value="${user.earnings}">
		<input type="hidden" id="realname" value="${user.realname}">
		<input type="hidden" id="feerate" value="${bankcard.rate}">
		<input type="hidden" id="bankcardid" value="${bankcard.id}">
		<input type="hidden" id="bankcard" value="${bankcard}">
		<div class="topInfo">
			<div style="font-size: 14px">公司名称：${bankcard.company }</div>
			<div style="font-size: 14px">银行卡号：${bankcard.bankcardnum }</div>
			<div style="font-size: 14px">归属银行：${bankcard.bankname }</div>
			<div style="font-size: 14px">联系人：${bankcard.realname }</div>
		</div>
		<br>
		<div>
			<div>
				<font size="3px">提现金额:</font>
			</div>
			<div style="height: 80px" class="mui-input-row">
				<input id="withdrawmoney" 
					type="text" oninput="changeMoney();" style="height: 80px; width: 100%">
			</div>
			<div id="wolfWithdraw1" style="font-size: 14px"></div>
			<div id="wolfWithdraw2" style="font-size: 14px">
				<font>当前余额为：<font id="moneyval">${user.earnings == null ? "0.0" : user.earnings }
				</font>&nbsp;元&nbsp;&nbsp;
					<a id="allwithdraw" >全部提现</a>
				</font>
			</div>
		</div>
		<div style="padding-top: 15px">
			<button id="withdrawbtn" class="mui-btn mui-btn-success mui-btn-block">立即提现</button>
		</div>
		<li class="mui-table-view-cell ">
			<strong style="font-size: 14px;">注意事项</strong><br>
			<p class="PDetail2">
				<strong>1、开票信息：</strong><br>
				<p><span class="left">公司名称：</span><span class="right">郑州和腾信息技术有限公司</span></p>
				<p><span class="left">纳税人号：</span><span class="right">91410100MA44BL3G5L</span></p>
				<p><span class="left">地址电话：</span><span class="right">郑州高新技术开发区莲花街338号12号楼2层09号 &nbsp;0371-56788915</span></p>
				<p><span class="left">开户银行及账号：</span><span class="right">中国民生银行郑州航海路支行 &nbsp; 153715248</span></p>
				<p><strong>2、开票金额：<font id="invoiceMoney">请输入金额</font></strong><br></p>
				<p><span class="left"><strong>3、发票邮寄地址：</strong></span><span class="right">郑州市高新区莲花街电子电器 产业园西区12号楼二单元2楼09(财务部)郑州和腾信息技术有限公司 赵女士  19137642510</span></p>
			</p>			
		</li class="mui-table-view-cell ">
	</div> --%>
	<div class="app">
		<input type="hidden" id="balance" value="${user.earnings}">
		<input type="hidden" id="realname" value="${user.realname}">
		<input type="hidden" id="feerate" value="${bankcard.rate}">
		<input type="hidden" id="bankcardid" value="${bankcard.id}">
		<input type="hidden" id="bankcard" value="${bankcard}">
		<script type="text/javascript">
			var bankcard= $('#bankcard').val().trim()
			if(bankcard == '' || bankcard.length <= 0){
				mui.confirm('对不起，你未绑定对公账户，请先绑定对公账户！',function(opation){
					if(opation.index===1){
						window.location= '/merchant/addCompanyCard'
					}else {
						window.location= '/merchant/personcenter'
					}
				})
			}
		</script>
		<ul class="publicAccount">
			<li>
				<p>公司名称：<span>${bankcard.company }</span></p>
				<p>归属银行：<span>${bankcard.bankname }</span></p>
				<p>银行卡号：<span>${bankcard.bankcardnum }</span></p>
				<p>联系人：<span>${bankcard.realname }</span></p>
			</li>
		</ul>
		<div class="cashType">
			<div class="left">到账方式</div>
			<div class="right">
				<div class="typeName"><span class="typeSpan">${bankcard.bankname }</span> <strong class="bankNumStr">（对公账户）</strong> <span class="right_arr mui-icon mui-icon mui-icon-forward"></span></div>
				<p>七个工作日内到账</p>
			</div>
		</div>
		<div class="cashInfo">
			<h3>提现金额</h3>
			<form action="">
				<span class="iconMon">¥</span>
				<input type="text" name="cashOut">
			</form>
			<div class="totalMonInfo"><p class="pLast">总金额：<span class="totalMonNum">${user.earnings == null ? "0.0" : user.earnings }</span>元  <span class="allMoneySpan">全部提现</span></p></div>
			<div class="extraCharging"><p>额外扣除￥<span >0</span>服务费（费率${bankcard.rate/10}%）</p></div>
			<div class="tipInfo"><p>提现金额小于5元</p></div>
		</div>
		<div class="btnDiv">
			<button type="button" class="mui-btn mui-btn-success">七天内到账，确认提现</button>
		</div>

		<!-- <div class="publicAcc">
			<li class="mui-table-view-cell">
				<strong style="font-size: 14px;">注意事项</strong><br>
				<p class="PDetail2">
					<strong>1、开票信息：</strong><br>
					</p><p><span class="left">公司名称：</span><span class="right">郑州和腾信息技术有限公司</span></p>
					<p><span class="left">纳税人号：</span><span class="right">91410100MA44BL3G5L</span></p>
					<p><span class="left">地址电话：</span><span class="right">郑州高新技术开发区莲花街338号12号楼2层09号 &nbsp;19137642510</span></p>
					<p><span class="left">开户银行及账号：</span><span class="right">中国民生银行郑州航海路支行 &nbsp; 153715248</span></p>
					<p><strong>2、开票金额：<font id="invoiceMoney">请输入金额</font></strong><br></p>
					<p><span class="left"><strong>3、发票邮寄地址：</strong></span><span class="right">郑州市高新区莲花街电子电器 产业园西区12号楼二单元2楼09(财务部)郑州和腾信息技术有限公司 赵女士  18937195215</span></p>
				<p></p>			
			</li>
		</div> -->
		<!-- <div class="otherType">
			其他提现方式
		</div> -->
	</div>
	<script>
		$(function(){
			var clickFlag= true //是否能点击
			var isCheckCashOut= false //是否核对isCheckCashOut
			var cashOutType= 3  //1是退费到微信零钱 3，对公账户
			var feeRateMon= parseFloat($('#feerate').val())/1000
			console.log(feeRateMon)
			/*点击全部提现*/
			$('.allMoneySpan').on('click',function(){
				var totalMoney= parseFloat($('.totalMonNum').text())
				if(totalMoney < 5){
					$('.totalMonInfo').hide()
					$('.extraCharging').hide()
					$('.tipInfo p').text('提现金额不能小于5元')
					$('.tipInfo').show()
					isCheckCashOut= false
				}else if(totalMoney >= 10000 && cashOutType == 1){
					$('input[name="cashOut"]').val(10000)
					extraChargingFn((10000*feeRateMon).toFixed(2))
					isCheckCashOut= false
				}else{
					$('input[name="cashOut"]').val(totalMoney)
					extraChargingFn((totalMoney*feeRateMon).toFixed(2))
					isCheckCashOut= true
				}
			})

			/*input的抬起时间*/
		$('input[name="cashOut"]').on('keyup',function(e){
			e= e || window.event
			var totalMoney= parseFloat($('.totalMonNum').text())
				if($(this).val() == '' || $(this).val() == null){
					$('.tipInfo').hide()
					$('.extraCharging').hide()
					$('.totalMonInfo').show()
					isCheckCashOut= false
					return
				}
				var money= parseFloat($(this).val())
				if(typeof money == 'number' && parseFloat(money)< 5){
					$('.totalMonInfo').hide()
					$('.extraCharging').hide()
					$('.tipInfo p').text('提现金额不能小于5元')
					$('.tipInfo').show()
					isCheckCashOut= false
					return
				}
				if(typeof money == 'number' && parseFloat(money)> 10000 && cashOutType == 1){
					$('.totalMonInfo').hide()
					$('.extraCharging').hide()
					$('.tipInfo p').text('单日提现金额不能大于10000元')
					$('.tipInfo').show()
					isCheckCashOut= false
					return
				}
				if(typeof money == 'number' && parseFloat(money)> totalMoney){
					$('.totalMonInfo').hide()
					$('.extraCharging').hide()
					$('.tipInfo p').text('提现金额不能超过账户余额')
					$('.tipInfo').show()
					isCheckCashOut= false
					return
				}
				money= isNaN(money) ? 0 : money
				var extraChargingMoney= (money*feeRateMon).toFixed(2)
				extraChargingFn(extraChargingMoney)
				isCheckCashOut= true
		})


			/*更改并显示退款扣费钱数*/
			function extraChargingFn(extraChargingMoney){
				$('.extraCharging span').text(extraChargingMoney)
				$('.totalMonInfo').hide()
				$('.tipInfo').hide()
				$('.extraCharging').show()
			}
			
			//点击提现
			$('.btnDiv button').on('click',function(){
				if(!isCheckCashOut){  return } //如果没有检验提现金额没通过，阻止发送请求
				var withdrawmoneyval= $('.cashInfo form input').val().trim()
				var bankcardid = $("#bankcardid").val().trim();
				var dataval = {
						bankcardid : bankcardid,
						money : withdrawmoneyval,
					};
				var url = '${hdpath}/merchant/withdrawaccess';
				var realname = $("#realname").val();
					mui.confirm('确认提现吗 ？', '提现', ['取消', '确认'], function(e) {
						if (e.index == 1) {
							if(!clickFlag){return} //阻止多次误点
								clickFlag= false
								toggleBtnClick(true)
								$.bootstrapLoading.start({ loadingTips: "提现中..." });
								$.ajax({
									url : url,
									data : dataval,
									type : "POST",
									cache : false,
									success : function(data) {
										if (data == "0") {
											toggleBtnClick(false)
											mui.alert('非法用户或银行卡和提现金额有误', '提现', function() {
											});
										} else if (data == "1") {
											toggleBtnClick(true)
											mui.alert('操作成功', '提现', function() {
												location.replace("${hdpath}/merchant/personcenter");
											});
										} else if (data == "2") {
											toggleBtnClick(false)
											mui.alert('提现失败，请重新登陆后操作', '提现', function() {
											});
										} else if (data == "3") {
											toggleBtnClick(false)
											mui.alert('提现失败，需实名绑定银行卡后再提现，实名必须和此微信相同', '提现', function() {
											});
										} else if (data == "4") {
											toggleBtnClick(false)
											mui.alert('提现总额超出10000，请明日再次操作', '提现', function() {
											});
										} else {
											toggleBtnClick(false)
											mui.alert(data, '提现', function() {
											});
										}
									},//返回数据填充
									complete: function () {
							            $.bootstrapLoading.end();
							            clickFlag= true
							        },
							        error : function() {
							        	mui.alert("系统错误！！！", '提现', function() {
										});
							        }
								});
							} //e.index
					}) //confirm
			})
			
			function toggleBtnClick(flag){
				$('.cashInfo form input').prop('disabled',flag)
			}
		})
	</script>
</body>
<!-- <script type="text/javascript">
	function changeMoney() {
		var moneyval = $("#balance").val();
		var withdrawmoneyval = $("#withdrawmoney").val().trim();
		if (withdrawmoneyval != null && withdrawmoneyval != "" && withdrawmoneyval != "0") {
			if (withdrawmoneyval < 5) {
				$("#wolfWithdraw2").hide();
				$("#wolfWithdraw1").show();
				$("#wolfWithdraw1").html("<font color='red'>金额小于5元</font>");
			} else if (withdrawmoneyval - moneyval > 0) {
				$("#wolfWithdraw2").hide();
				$("#wolfWithdraw1").show();
				$("#wolfWithdraw1").html("<font color='red'>输入金额大于零钱余额</font>");
			} else {
				var feerate = $("#feerate").val();
				$("#wolfWithdraw2").hide();
				$("#wolfWithdraw1").show();
				var feerateMoney = withdrawmoneyval * feerate / 1000;
				feerateMoney = feerateMoney.toFixed(2);
				var account = withdrawmoneyval-feerateMoney;
				$("#wolfWithdraw1").html("额外扣除￥" + feerateMoney + "服务费（费率" + feerate/10 + "%）实际到账" + account.toFixed(2) + "元");
				$("#invoiceMoney").text(account.toFixed(2) + "元");
			}
		} else {
			$("#wolfWithdraw2").show();
			$("#wolfWithdraw1").hide();
		}
	}
	$(function() {
		return
		$("#allwithdraw").click(function() {
			var withMoney = $("#balance").val();
			$("#withdrawmoney").val(withMoney);
			var feerate = $("#feerate").val();
			$("#wolfWithdraw2").hide();
			$("#wolfWithdraw1").show();
			var feerateMoney = withMoney * feerate / 1000;
			feerateMoney = feerateMoney.toFixed(2);
			var account = withMoney-feerateMoney;
			$("#wolfWithdraw1").html("额外扣除￥" + feerateMoney + "服务费（费率" + feerate/10 + "%）实际到账：" + account.toFixed(2) + "元");
			$("#invoiceMoney").text(account.toFixed(2) + "元");
		})
		document.getElementById("withdrawbtn").addEventListener('tap', function() {
			var isFir = true;
			var moneyval = $("#moneyval").text();
			var withdrawmoneyval = $("#withdrawmoney").val();
			if ($("#bankcard").val() == null || $("#bankcardlist").val() == "") {
				mui.alert('对公账户未绑定', '提现', function() {
				});
				return ;
			}
			if (withdrawmoney == null || withdrawmoneyval == ""){
				mui.alert('请输入提现金额', '提现', function() {
				});
			} else if (withdrawmoneyval < 5) {
				mui.alert('提现金额不得低于5元', '提现', function() {
				});
			} else if (withdrawmoneyval - moneyval > 0) {
				mui.alert('提现金额超出余额', '提现', function() {
				});
			} else {
				var dataval;
				var url;
				var bankcardval = $("#bankcardid").val();
				dataval = {
						bankcardid : bankcardval,
						money : withdrawmoneyval,
					};
				url = '${hdpath}/merchant/withdrawaccess';
				var btnArray = ['取消', '确认'];
				var realname = $("#realname").val();
				if (realname != null && realname != "") {
					mui.confirm('确认提现吗 ？', '提现', btnArray, function(e) {
						if (e.index == 1) {
							if (isFir) {
								isFir = false;
								$.bootstrapLoading.start({ loadingTips: "提现中..." });
								$.ajax({
									url : url,
									data : dataval,
									type : "POST",
									cache : false,
									success : function(data) {
										if (data == "0") {
											mui.alert('非法用户或银行卡和提现金额有误', '提现', function() {
											});
										} else if (data == "1") {
											mui.alert('操作成功', '提现', function() {
												location.replace("${hdpath}/merchant/mywallet");
											});
										} else if (data == "2") {
											mui.alert('提现失败，请重新登陆后操作', '提现', function() {
											});
										} else if (data == "3") {
											mui.alert('提现失败，需实名绑定银行卡后再提现，实名必须和此微信相同', '提现', function() {
											});
										} else if (data == "4") {
											mui.alert('提现总额超出5000，请明日再次操作', '提现', function() {
											});
										} else {
											mui.alert(data, '提现', function() {
											});
										}
									},//返回数据填充
									complete: function () {
							            $.bootstrapLoading.end();
							        },
							        error : function() {
							        	mui.alert("系统错误！！！", '提现', function() {
										});
							        }
								});
							}
						} else {
							return ;
						}
					})
				} else {
					mui.prompt( " ", "请输入真实姓名，与微信实名一致", "提现", btnArray,  function(e){
						if (e.index == 1) {
							$.bootstrapLoading.start({ loadingTips: "提现中..." });
							$.ajax({
								url : url,
								data : {
									money : withdrawmoneyval,
									realname : e.value
								},
								type : "POST",
								cache : false,
								success : function(data) {
									if (data == "0") {
										mui.alert('非法用户或银行卡和提现金额有误', '提现', function() {
										});
									} else if (data == "1") {
										mui.alert('操作成功', '提现', function() {
											location.replace("${hdpath}/merchant/mywallet");
										});
									} else if (data == "2") {
										mui.alert('提现失败，请重新登陆后操作', '提现', function() {
										});
									} else if (data == "3") {
										mui.alert('提现失败，需实名绑定银行卡后再提现，实名必须和此微信相同', '提现', function() {
										});
									} else if (data == "4") {
										mui.alert('提现总额超出5000，请明日再次操作', '提现', function() {
										});
									} else {
										mui.alert(data, '提现', function() {
										});
									}
								},//返回数据填充
								complete: function () {
						            $.bootstrapLoading.end();
						        },
						        error : function() {
						        	mui.alert("系统错误！！！", '提现', function() {
									});
						        }
							});
						} else {
							return ;
						}
					})
				}
			}
		});
	})
</script> -->
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<title>修改银行卡信息</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<style type="text/css">
body {
	background-color: #efeff4;
}
.mui-content {
	background: #f5f7fa;
	font-size: 14px;
	color: #666;
	padding: 10px;
	border-radius: 0 0 5px 5px;
}
.mui-content .title {
	padding: 15px 0;
}
.mui-input-row {
	background: rgba(0,0,0,.04);
	border-radius: 5px;
	margin-bottom: 10px;
}
.mui-content button {
	weight: 92%;
	padding: 0;
	line-height: 45px;
	font-size: 14px;
	background: #22B14C;
}
.mui-content
</style>
</head>
<body>
	<div class="mui-content">
		<div id="tabbar" class="mui-control-content mui-active">
			<div class="title">
				请修改以下银行卡信息
			</div>
		</div>
		<input type="hidden" id="id" value="${bankcard.id }">
		<input type="hidden" id="type" value="${bankcard.type }">
		<c:if test="${bankcard.type == 2 }">
			<div class="mui-input-row">
				<label>公司名称</label> <input type="text" class="mui-input"
					id="company" name="company" value="${bankcard.company }" <c:if test="${isedit == 1 }">disabled="disabled"</c:if> placeholder="请输入公司名称">
			</div>
			<div class="mui-input-row">
				<label>联系人</label> <input type="text" class="mui-input"
					id="realname" name="realname" value="${bankcard.realname }" placeholder="请输入联系人姓名">
			</div>
		</c:if>
		<c:if test="${bankcard.type == 1 }">
			<div class="mui-input-row">
				<label>真实姓名</label> <input type="text" class="mui-input"
					id="realname" name="realname" value="${bankcard.realname }" disabled="disabled" placeholder="请输入真实姓名">
			</div>
		</c:if>
		<div class="mui-input-row">
			<label>银行卡号</label> <input type="text" class="mui-input"
				id="bankcardnum" name="bankcardnum" value="${bankcard.bankcardnum }" placeholder="请输入账号">
		</div>
		<div class="mui-input-row">
			<label>所属银行</label> <input type="text" class="mui-input"
				id="bankname" name="bankname" value="${bankcard.bankname }" placeholder="请输入银行卡所属银行">
		</div>
		<div style="padding-top: 15px">
			<button id="submitbtn" class="mui-btn mui-btn-success mui-btn-block">确认修改</button>
		</div>
	</div>
	<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
</body>
</html>
<script>
$(function() {
	document.getElementById("submitbtn").addEventListener('tap', function() {
		var btnArray = ['取消', '确认'];
		mui.confirm('确认修改以上信息吗？', '绑定银行卡', btnArray, function(e) {
			if (e.index == 1) {
				var realnameval = $("#realname").val();
				var bankcardnumval = $("#bankcardnum").val();
				var banknameval = $("#bankname").val();
				var company = '';
				var type = $("#type").val();
				if (type == 2) {
					company = $("#company").val();
				}
				var res = /^\d+$/;
				if (realnameval == null || realnameval == "") {
					mui.alert('未输入真实姓名', '银行卡绑定', function() {
					});
					return ;
				} else if (bankcardnumval == null || bankcardnumval == "") {
					mui.alert('未输入银行卡号', '银行卡绑定', function() {
					});
					return ;
				} else if (bankcardnumval.length != 19 || res.test(bankcardnumval) == false) {
					mui.alert('银行卡号输入错误', '银行卡绑定', function() {
					});
					return ;
				} else if (banknameval == null || banknameval == "") {
					mui.alert('未输入银行卡所属银行', '银行卡绑定', function() {
					});
					return ;
				}
				$.ajax({
					url : '${hdpath}/merchant/editbankcardaccess',
					data : {
						id : $("#id").val(),
						realname : realnameval,
						bankcardnum : bankcardnumval,
						bankname : banknameval,
						company : company
					},
					type : "POST",
					cache : false,
					success : function(data) {
						if (data == 0) {
							mui.alert('错误用户', '银行卡绑定', function() {
							});
							return ;
						} else if (data == 1) {
							mui.alert('修改成功', '银行卡绑定', function() {
								window.location.href = "${hdpath}/merchant/mybankcard";
							});
						}
					},//返回数据填充
				});
			} else {
				return ;
			}
		})
	});
})
</script>
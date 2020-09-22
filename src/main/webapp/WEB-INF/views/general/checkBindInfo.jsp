<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>绑定商户</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
.mui-btn{
	width: 100%;
}
</style>
</head>
<body>
	<hr>
	<div class="mui-content">
		<div class="mui-content-padded" style="margin: 5px;">
			<h5>填写商家手机号，暂时只可绑定一次</h5>
			<form class="mui-input-group">
				<div class="mui-input-row">
					<label>手机号</label>
					<input value="${merUser.phoneNum }" id="phonenum" type="text" placeholder="请输入手机号" <c:if test="${merUser != null }"> disabled="disabled" </c:if>>
				</div>
			</form>
			<button class="mui-btn mui-btn-success" id="submitbtn" <c:if test="${merUser != null }"> disabled="disabled" </c:if> >确认绑定</button>
		</div>
	</div>
</body>
</html>
<script>
$(function() {
	$("#submitbtn").click(function() {
		var phonenumval = $("#phonenum").val();
		if (phonenumval == null || phonenumval == "") {
			mui.alert('手机号码未填写！！！','绑定商家');
		} else {
			$.ajax({
				url : '/general/userBindMid',
				data : {
					phonenum : phonenumval
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data == '0') {
						mui.alert('登陆已过期，请重新登陆绑定','绑定商家');
					} else if (data == '1') {
						mui.alert('绑定成功','绑定商家');
						mui.back();
					} else if (data == '2') {
						mui.alert('您已绑定','绑定商家');
					} else if (data == '3') {
						mui.alert('没有此商家，请重新输入','绑定商家');
					}
				},//返回数据填充
		        error : function() {
		        	mui.alert("系统错误！！！", '绑定商家');
		        }
			});
		}
	})
})
</script>
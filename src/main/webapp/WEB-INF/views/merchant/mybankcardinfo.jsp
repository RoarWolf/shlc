<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-2.1.0.js"></script>
<title>银行卡信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%-- <link href="${pageContext.request.contextPath }/mui/css/style.css" rel="stylesheet" /> --%>
<link href="${pageContext.request.contextPath }/mui/css/mui.min.css" rel="stylesheet" />
<style type="text/css">
h1 {
	font-size: 16px;
	color: #666;
	margin-bottom: 8px;
	text-align: center;
}
li {
	list-style: none;
	margin-top: 8px;
	margin-bottom: 12px
}

li span {
	margin: 12px 15px 8px 0;
	font-size: 14px;
}
.container {
	padding: 15px;
}
.container ul {
	background: #f5f7fa;
	font-size: 14px;
	padding: 15px;
	color: #666;
	border-radius: 5px;
}

.container ul li span:nth-child(2){
	color: #999;
}
.container ul li:last-child {
	display: flex;
	justify-content: space-around;
	margin-top: 15px;
}
.container ul li:last-child button{
	background: #22B14C;
	border: none;
	padding: 4px 12px;	
}
.container ul li:last-child a {
	background: #22B14C;
	padding: 5px 12px;		
}
.container ul li:last-child a:active{
	background: #22B14C;
}
</style>
</head>
<body>
	<div class="container">
		<h1>银行卡信息</h1>
		<input id="id" type="hidden" value="${bankcard.id }">
		<ul style="list-sytle: none;">
			<c:if test="${bankcard.type == 2 }">
				<li><span>公司名称：</span><span>${bankcard.company }</span></li>
				<li><span>联系人：</span><span>${bankcard.realname }</span></li>
			</c:if>
			<c:if test="${bankcard.type == 1 }">
				<li><span>真实姓名：</span><span>${bankcard.realname }</span></li>
			</c:if>
			<li><span>银行卡号：</span><span>${bankcard.bankcardnum }</span></li>
			<li><span>归属银行：</span><span>${bankcard.bankname }</span></li>
			<li><a
				href="${hdpath }/merchant/editmybankcard?id=${bankcard.id}"
				class="btn btn-info">修改信息</a>
				<button id="confirmBtn" class="btn btn-danger">解除绑定</button></li>
		</ul>
	</div>
	<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
</body>
</html>
<script>
	$(function() {
		document.getElementById("confirmBtn").addEventListener('tap', function() {
			var btnArray = ['否', '是'];
			mui.confirm('确认此银行卡解除绑定吗 ？', '银行卡绑定', btnArray, function(e) {
				if (e.index == 1) {
					$.ajax({
						url : '${hdpath}/merchant/unbindbankcard',
						data : {
							id : $("#id").val(),
						},
						type : "POST",
						cache : false,
						success : function(data) {
							if (data == 0) {
								alert("错误用户");
								return false;
							} else if (data == 1) {
								mui.alert('操作成功', '银行卡绑定', function() {
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
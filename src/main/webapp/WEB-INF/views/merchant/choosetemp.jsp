<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收费模板选择</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.js"></script> 
<style type="text/css">
.title {
	margin: 20px 15px 10px;
	color: #6d6d72;
	font-size: 15px;
}
html,body{
margin-bottom: 50px;
background-color: #e3e3e6;
}
</style>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<h1 class="mui-title">当前设备编号：${code }号</h1>
	</header>
	<input type="hidden" id="code" value="${code }">
	<div class="mui-content">
		<c:forEach items="${templatelist }" var="temp">
			<div id="tempparent${temp.id }" data-mytemp="${temp.id }" align="center" style="height: 50px; border: 1px solid gray;"
				class="mui-control-content mui-active">
				<label> <input id="temp${temp.id }" value="${temp.id }" name="temp"
					type="radio" <c:if test="${tempid == temp.id }">checked="checked"</c:if> > <font size="3px">${temp.name }</font>
				</label>
			</div>
			<div align="center" id="conceal${temp.id }" style="display: none;border: 1px solid gray;">
				<c:forEach items="${temp.gather}" var="tempSon">
					<div id="tempchild${tempSon.id }">
						<span>${tempSon.name}</span>
					</div>
				</c:forEach>
			</div>
		</c:forEach>

		<div style="padding-top: 60px">
		<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
			<span style="padding-left: 50px"><button id="equbindtempbtn" class="btn btn-success">确认</button></span>
			<span style="padding-left: 50px"><a href="${hdpath }/merchant/manage" class="btn btn-info">回管理页面</a></span>
		</nav>
		</div>
	</div>
</body>
<script>
	$(function(){
	    pushHistory();
	    window.addEventListener("popstate", function(e) {
	    	window.location.href = "${hdpath}/equipment/list";
		}, false);
	    function pushHistory() {
	        var state = {
	            title: "title",
	            url: "#"
	        };
	        window.history.pushState(state, "title", "#");
	    }
	});
	$(function() {
		$("div[id^=tempparent]").click(function(){
			var id = $(this).attr('data-mytemp');
			var tempid = $("#temp" + id).val();
			$("#conceal" + id).toggle();
			$('input:radio').each(function(){
				if ($(this).val() == tempid) {
					$(this).parent().parent().css("background","#44b549");
					$(this).attr("checked","checked");
				} else {
                    $(this).parent().parent().css("background","#fff");
                }
			});
        });
		$("#equbindtempbtn").click(function() {
			var check = $("input[name='temp']:checked").val();
			if (check == null) {
				alert("没有选择收费模板");
				return ;
			}
			$.ajax({
				url : '${hdpath}/merchant/equbindtemp',
				data : {
					tempid : check,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data == '1') {
						mui.back();
					}
				},//返回数据填充
			});
		})
		$("input[name='temp']").click(function() {
			var id = "conceal" + $(this).val();
			$("#" + id).toggle();
		})
	})
</script>
</html>
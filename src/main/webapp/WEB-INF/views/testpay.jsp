<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<html>
<head>
<title>
	<c:choose>
		<c:when test="${remoteparam == 1 }">
			远程投币
		</c:when>
		<c:otherwise>
			投币测试页面
		</c:otherwise>
	</c:choose>
</title>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${pageContext.request.contextPath }/mui/js/mui.min.js"></script>
<style type="text/css">
html,body{width: 100%;height: 100%}
.demo-table {
	margin: 1rem 0 1.5rem 0;
	width: 100%;
}

#chargechoose {
	margin: 0;
	padding: 0 0 0 0.1rem;
}
hr {
    margin-top: 0px;
    margin-bottom: 0px;
    border: 0;
    border-top: 1px solid #eee;
}
ul{width: 100%;}
ul.ui-choose>li {
    box-sizing: border-box;
    border: 1px solid #ccc;
	float: none;
    width: 100%;
    height: 34px;
    line-height: 32px;
    margin: 0 auto;
    cursor: pointer;
    position: relative;
    z-index: 1;
    min-width: 75px;
    text-align: center;
}
</style>
<script src="${hdpath }/js/my.js"></script>
</head>
<body>
<div>
	<div align="center" style="font-size: 15px">
		<font>当前设备编号为：${code }</font>
	</div>
	<hr>
	<div class="demo-box" >
	<table class="demo-table">
	    <caption></caption>
		    <tr>
		      <td>
		        <ul id="chargechoose">请选择投币金额</ul>
		        <ul class="ui-choose" id="uc_02">
		          <li class="equtemp">1元<span style="display:none;">_1</span></li>
		          <li class="equtemp">2元<span style="display:none;">_2</span></li>
		          <li class="equtemp">3元<span style="display:none;">_3</span></li>
		          <li class="equtemp">4元<span style="display:none;">_4</span></li>
		          <li class="equtemp">5元<span style="display:none;">_5</span></li>
		        </ul>
		      </td>
		    </tr>
	</table>
	<input type="hidden" id="code" value="${code }">
	<input type="hidden" id="remoteparam" value="${remoteparam }">
	<div style="text-align: center;">
		<button id="operate" class="mui-btn mui-btn-success mui-btn-block" style="bacabackground-color: #44b549" value="0">
			<c:choose>
				<c:when test="${remoteparam == 1 }">
					远程投币(默认1元)
				</c:when>
				<c:otherwise>
					模拟投币(默认1元)
				</c:otherwise>
			</c:choose>
		</button>
	</div>
	<div <c:if test="${remoteparam == 1 }"> style="display: none;" </c:if> >
		<div style="padding-top: 30px;text-align: center;">
			<a style="text-decoration: none;" class="mui-btn mui-btn-primary mui-btn-block"
 						href="${hdpath}/merchant/infoverdict?openid=${openid}&code=${code}&existuser=${existuser}">登陆绑定</a>
		</div>
	</div>
</div>
</div>
<script>
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();

// uc_01 ul 单选
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="money"]').val(id)
}
$(function() {
	uc_02._val_ul(0);
})
</script>
<input type="hidden" name="money" id="money" value="1">
</body>
</html>
<script>
$(function() {
	$("#operate").click(function() {
		var explain = "投币测试成功";
		if ($("#remoteparam").val() == 1) {
			explain = "远程投币成功";
		}
		$.bootstrapLoading.start({ loadingTips: "投币中..." });
		$.ajax ({
			url : '${hdpath}/incoins',
			data : {
				code : $("#code").val(),
				money : $("#money").val()
			},
			type : "POST",
			cache : false,
			success : function(data) {
				if (data.err == 1) {
					mui.alert(data.errinfo,'投币测试');
				} else {
					mui.alert(explain,'远程投币');
				}
			},
	        complete: function () {
	            $.bootstrapLoading.end();
	        }
		})
	})
})
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>选择模块类型</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<style type="text/css">
a:hover {
	text-decoration: none;
}
</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<h1 class="mui-title">当前设备号：${code }</h1>
	</header>
	<div class="mui-content">
		<h5 class="mui-content-padded">请为模块选择设备类型</h5>
		<ul class="mui-table-view mui-table-view-radio">
			<li class="mui-table-view-cell mui-selected">
				<a class="mui-navigate-right" data-myselectval="01" id="hardchoose1">
					智慧款十路
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="02" id="hardchoose2">
					电轿款
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="03" id="hardchoose3">
					脉冲板子
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="04" id="hardchoose4">
					离线充值机
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="05" id="hardchoose5">
					智慧款十六路
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="06" id="hardchoose6">
					智慧款二十路
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="07" id="hardchoose7">
					单路交流桩
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="08" id="hardchoose8">
					新版十路智慧款v3
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="09" id="hardchoose9">
					新版二路智慧款v3
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a class="mui-navigate-right" data-myselectval="10" id="hardchoose10">
					新版二十路智慧款v3
				</a>
			</li>
		</ul><br>
		<form id="idform" action="${hdpath}/equipment/editHardversion" method="post">
			<input type="hidden" id="hardversion" name="hardversion" value="01">
			<input type="hidden" id="code" name="code" value="${code }">
			<input type="hidden" id="openid" name="openid" value="${openid }">
			<input type="hidden" id="existuser" name="existuser" value="${existuser }">
			<button class="mui-btn mui-btn-success mui-btn-block" id="submitbtn">确认选择</button>
		</form>
	</div>
</body>
</html>
<script>
	mui.init({
		swipeBack:true //启用右滑关闭功能
	});
	$(function() {
		$("a[id^='hardchoose']").click(function() {
			$("#hardversion").val($(this).attr("data-myselectval"));
		})
	})
	$("#submitbtn").click(function() {
		var hardversionval = $("#hardversion").val();
		if (hardversionval == null || hardversionval == "") {
			mui.alert('请选择模块类型');
		} else {
			$("#idform").submit();
		}
	})
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>loading</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath }/js/my.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.min.js"></script>
<script type="text/javascript">
        $(function () {
            $("#btn_submit").on("click", function () {
                $.bootstrapLoading.start({ loadingTips: "正在处理数据，请稍候..." });
                $.ajax({
                    type: 'get',
                    url: '${hdpath}/load',
                    data: {},
                    success: function (data, statu) {
                        /* debugger; */
                    },
                    complete: function () {
                        $.bootstrapLoading.end();
                    }
                });
            })
            
            /* setTimeout(paystate, 500); //1秒后执行
			function paystate() {
				$.bootstrapLoading.start({ loadingTips: "正在加载，请稍后..." });
				$.ajax({
                    type: 'get',
                    url: '${hdpath}/load',
                    data: {},
                    success: function (data, statu) {
                        debugger;
                   },
                    complete: function () {
                       $.bootstrapLoading.end();
                    }
                });
			} */
            
        });
    </script>
</head>
<body>
	<div class="panel-body" style="padding: 0px">
		<div class="panel panel-default" style="height: 450px;">
			<div class="panel-heading">查询条件</div>
			<div class="panel-body">
				<form id="formSearch" class="form-horizontal">
					<div class="form-group">
						<div class="col-xs-4">
							<button type="button" id="btn_submit" class="btn btn-primary">
								<span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>加载测试
							</button>
						</div>
						<div class="col-xs-4">
							<button style="background-color: #e80808;" type="button"
								onclick="test()" id="wolf" class="btn btn-primary">获取背景</button>
						</div>
						<div class="col-xs-4">
							<button type="button" id="loading" class="btn btn-primary">mui加载</button>
						</div>
						<div class="col-xs-4">
							<button type="button" id="clickbtn" class="btn btn-primary">点击变化</button>
							<font id="changefont">123</font>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
<script>
	function test() {
		var v = "wolf";
		var color = document.getElementById(v).style.backgroundColor;
		if (color == "#e80808") {
			alert("1");
		} else {
			alert("2");
		}
	}
	$(function() {
		$("#clickbtn").click(function() {
			$("#changefont").html("456");
		})
	})
</script>
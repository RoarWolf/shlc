<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>qrcode demo</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.qrcode.js"></script>
<script type="text/javascript" src="${hdpath }/js/qrcode.js"></script>
</head>
<body>
	<!-- <p>Render in table</p>
	<div id="qrcodeTable"></div>
	<p>Render in canvas</p>
	<div id="qrcodeCanvas"></div> -->
	<img id="qrCodeIco" src="${hdpath }/images/hd.png"
		style="position: absolute; width: 30px; height: 30px;">
	<img id="qrCodeIco1" src="${hdpath }/images/hd.png"
		style="position: absolute; width: 30px; height: 30px;">
	<div id="qrcode"></div>
	<!-- Button trigger modal -->
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  Launch demo modal
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Modal title</h4>
      </div>
      <div class="modal-body">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>
	<!-- Large modal -->
	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target=".bs-example-modal-lg">Large modal</button>

	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
		aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div id="jumpqr" class="modal-content"></div>
		</div>
	</div>

	<!-- Small modal -->
	<button type="button" class="btn btn-primary" data-toggle="modal"
		data-target=".bs-example-modal-sm">Small modal</button>

	<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
		aria-labelledby="mySmallModalLabel" align="center">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div id="wolf"></div>
				<div>000001</div>
			</div>
		</div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="gridSystemModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">Modal title</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-4">.col-md-4</div>
						<div class="col-md-4 col-md-offset-4">.col-md-4
							.col-md-offset-4</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-md-offset-3">.col-md-3
							.col-md-offset-3</div>
						<div class="col-md-2 col-md-offset-4">.col-md-2
							.col-md-offset-4</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-md-offset-3">.col-md-6
							.col-md-offset-3</div>
					</div>
					<div class="row">
						<div class="col-sm-9">
							Level 1: .col-sm-9
							<div class="row">
								<div class="col-xs-8 col-sm-6">Level 2: .col-xs-8
									.col-sm-6</div>
								<div class="col-xs-4 col-sm-6">Level 2: .col-xs-4
									.col-sm-6</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary">Save changes</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<script>
		//jQuery('#qrcode').qrcode("this plugin is great");
		/* jQuery('#qrcodeTable').qrcode({
			render : "table",
			text : "http://jetienne.com"
		});
		jQuery('#qrcodeCanvas').qrcode({
			text : "http://jetienne.com"
		}); */
		$("#qrcode").qrcode({
			render : "canvas", //设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
			text : "${qrcodeinfo}", //扫描二维码后显示的内容,可以直接填一个网址，扫描二维码后自动跳向该链接
			width : "200", // //二维码的宽度
			height : "200", //二维码的高度
			background : "#ffffff", //二维码的后景色
			foreground : "#000000", //二维码的前景色
			src : "/images/hd.png" //二维码中间的图片
		});
		var margin = ($("#qrcode").height() - $("#qrCodeIco").height()) / 2; //控制Logo图标的位置
		$("#qrCodeIco").css("margin", margin);
		$("#jumpqr").qrcode({
			render : "canvas", //设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
			text : "hello world", //扫描二维码后显示的内容,可以直接填一个网址，扫描二维码后自动跳向该链接
			width : "200", // //二维码的宽度
			height : "200", //二维码的高度
			background : "#ffffff", //二维码的后景色
			foreground : "#000000", //二维码的前景色
			src : "/images/hd.png" //二维码中间的图片
		});
		$("#wolf").qrcode({
			render : "canvas", //设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
			text : "hello world", //扫描二维码后显示的内容,可以直接填一个网址，扫描二维码后自动跳向该链接
			width : "200", // //二维码的宽度
			height : "200", //二维码的高度
			background : "#ffffff", //二维码的后景色
			foreground : "#000000", //二维码的前景色
			src : "/images/hd.png" //二维码中间的图片
		});
	</script>
</body>
</html>
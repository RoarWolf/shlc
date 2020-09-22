<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html lang="en-US">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

ol, ul {
	list-style: none;
}

blockquote, q {
	quotes: none;
}

a {
	text-decoration: none;
	color: #2d2f30;
	-webkit-transition: all .3s linear;
	-moz-transition: all .3s linear;
	-o-transition: all .3s linear;
	-ms-transition: all .3s linear;
	transition: all .3s linear;
}

a:focus {
	outline: none;
}

.content {
	margin: auto;
	width: 1200px;
}

.sx_updown {
	width: 100%;
	border-top: 1px solid #f2f2f2;
	background: #fff;
	overflow: hidden;
	padding: 32px 0 40px 0;
	z-index: 9999;
	position: absolute;
	left: 0;
	top: 60px;
	text-align: left;
}

.updown_box {
	float: left;
	min-height: 198px;
	width: 388px;
	border-left: 1px solid #f2f2f2;
	padding: 0 50px;
	-webkit-box-sizing: content-box;
	-moz-box-sizing: content-box;
	box-sizing: content-box;
}

.updown_box h3 {
	font-size: 16px;
	margin-bottom: 18px;
	padding-left: 10px;
}

.updown_box ul li {
	float: left;
	margin: 0 10px 10px 10px;
}

.updown_box ul li a {
	display: block;
	font-size: 14px;
	padding: 6px 8px;
	line-height: 18px;
}

.updown_box ul li.selected a {
	background: #7ecbc8;
	color: #fff;
}

.w136 {
	width: 136px;
	border-left: 0;
	padding: 0;
}

.w290 {
	width: 290px;
}

.w160 {
	width: 160px;
	padding: 0 40px;
}

.sousuo {
	padding: 10px 0 36px 0;
	width: 100%;
}

.select-result {
	width: 900px;
	float: left;
}

.select-result ul li a {
	height: 30px;
	padding: 0 24px 0 10px;
	line-height: 30px;
	display: block;
	position: relative;
	float: left;
	margin: 0 10px 10px 0;
	color: #fff;
	font-size: 14px;
	background: url(close1.png) 90% center no-repeat #7ecbc8;
}
</style>

<head>

<meta charset="UTF-8">

<title>jQuery商品分类多项筛选菜单代码 - 站长素材</title>

</head>

<body>
	<div class="sx_updown clearfix">
		<div class="content">
			<div class="updown_box w136">
				<h3>全部</h3>
			</div>
			<div class="updown_box w290">
				<h3>按品牌</h3>
				<ul id="select1">
					<li><a href="javascript:;">美国ULTHERA</a></li>
					<li><a href="javascript:;">美国SOLTA</a></li>
					<li><a href="javascript:;">以色列飞顿</a></li>
					<li><a href="javascript:;">赛诺秀康奥</a></li>
					<li><a href="javascript:;">美国赛诺秀</a></li>
					<li><a href="javascript:;">美国赛诺龙</a></li>
					<li><a href="javascript:;">美国科医人</a></li>
					<li><a href="javascript:;">美国Candela</a></li>
					<li><a href="javascript:;">德国欧洲之星</a></li>
				</ul>
			</div>
			<div class="updown_box w160">
				<h3>按功能</h3>
				<ul id="select2">
					<li date-type="1"><a href="javascript:;">纤体</a></li>
					<li date-type="2"><a href="javascript:;">提拉</a></li>
					<li date-type="3"><a href="javascript:;">嫩肤</a></li>
					<li date-type="4"><a href="javascript:;">祛疤</a></li>
					<li date-type="5"><a href="javascript:;">脱毛</a></li>
					<li date-type="6"><a href="javascript:;">祛红</a></li>
					<li date-type="7"><a href="javascript:;">祛黑</a></li>
				</ul>
			</div>
			<div class="updown_box w160">
				<h3>按分类</h3>
				<ul id="select3">
					<li><a href="javascript:;">皮肤检测</a></li>
					<li><a href="javascript:;">超声</a></li>
					<li><a href="javascript:;">射频</a></li>
					<li><a href="javascript:;">激光</a></li>
					<li><a href="javascript:;">IPL强脉冲光</a></li>
				</ul>
			</div>
		</div>

	</div>
	<div class="sousuo clearfix">
		<div class="select-result clearfix">
			<ul>
			</ul>
		</div>
	</div>
	<script type="text/javascript">
		$("#select1 li").click(function() {
			$(this).addClass("selected").siblings().removeClass("selected");
			var copyThisA = $(this).clone();
			//console.log(copyThisA);
			if ($("#selectA").length > 0) {
				$("#selectA a").html($(this).text());
			} else {
				$(".select-result ul").append(copyThisA.attr("id", "selectA"));
			}
		});
		$("#select2 li").click(function() {
			var type = $(this).attr("date-type");
			var copyThisB = $(this).clone();
			if ($(this).hasClass("selected")) {
				$(".select-result li[date-type='" + type + "']").fadeToggle();
			} else {
				$(".select-result ul").append(copyThisB);
			}
			;
			$(this).toggleClass("selected");

		});
		$("#select3 li").click(function() {
			$(this).addClass("selected").siblings().removeClass("selected");
			var copyThisC = $(this).clone();
			if ($("#selectC").length > 0) {
				$("#selectC a").html($(this).text());
			} else {
				$(".select-result ul").append(copyThisC.attr("id", "selectC"));
			}
		});
		$("#selectA").live("click", function() {
			$(this).remove();
			$("#select1 li").removeClass("selected");
		});
		$(".select-result ul").delegate("li", "click", function() {
			var type = $(this).attr("date-type");
			$(this).fadeOut();
			$("#select2 li[date-type='" + type + "']").removeClass("selected");
		});
		$("#selectC").live("click", function() {
			$(this).remove();
			$("#select3 li").removeClass("selected");
		});
	</script>
</body>

</html>

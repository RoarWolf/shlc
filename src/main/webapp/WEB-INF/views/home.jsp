<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>郑州和动新能源有限公司</title>
<link rel="icon" href="${pageContext.request.contextPath }/images/hd.ico" type="image/x-icon"/>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/bootstrap.min.js"></script>
<style type="text/css">
.carousel-control.left {
	background-image: none;
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#80000000',
		endColorstr='#00000000', GradientType=1);
}

.carousel-control.right {
	left: auto;
	right: 0;
	background-image: none;
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#00000000',
		endColorstr='#80000000', GradientType=1);
}
</style>
</head>
<body>
	<div class="container">
		<div style="padding-top: 10px">
			<h2>欢迎致电郑州和动新能源有限公司-首页</h2>
		</div>
		<div style="padding-top: 20px">
			<h4><strong>产品展示</strong></h4>
		</div>
		<div id="myCarousel" class="carousel slide" style="padding-top: 20px">
			<!-- 轮播（Carousel）指标 -->
			<ol class="carousel-indicators">
				<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
				<li data-target="#myCarousel" data-slide-to="1"></li>
				<li data-target="#myCarousel" data-slide-to="2"></li>
				<li data-target="#myCarousel" data-slide-to="3"></li>
			</ol>
			<!-- 轮播（Carousel）项目 -->
			<div class="carousel-inner">
				<div align="center" class="item active">
					<img style="width: 500px; height: 420px"
						src="${hdpath }/images/hd1.jpg" alt="First slide">
					<p style="color: black">和动双路立式充电站&nbsp;&nbsp;&nbsp;&nbsp; 普及款和加强款</p>
				</div>
				<div align="center" class="item">
					<img style="width: 500px; height: 420px"
						src="${hdpath }/images/hd2.jpg" alt="Second slide">
						<p style="color: black">和动双路立式充电站&nbsp;&nbsp;&nbsp;&nbsp; 普及款和加强款</p>
				</div>
				<div align="center" class="item">
					<img style="width: 500px; height: 420px"
						src="${hdpath }/images/hd3.jpg" alt="Third slide">
						<p style="color: black">低速电动汽车充电桩</p>
				</div>
				<div align="center" class="item">
					<img style="width: 500px; height: 420px"
						src="${hdpath }/images/hd4.jpg" alt="Fourth slide">
						<p style="color: black">低速电动汽车充电桩</p>
				</div>
			</div>
			<!-- 轮播（Carousel）导航 -->
			<a class="left carousel-control" href="#myCarousel" role="button"
				data-slide="prev"> <span
				class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
				<span class="sr-only">Previous</span>
			</a> <a class="right carousel-control" href="#myCarousel" role="button"
				data-slide="next"> <span
				class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<span class="sr-only">Next</span>
			</a>
		</div>
		<div style="padding-top: 20px">
			<div>
				<font size="20px"><strong>关于我们</strong></font>
			</div>
			<p>
				<strong>公司简介：</strong>郑州和动新能源有限公司是一家集产品销售、研发、维修、服务于一体的企业；专业从事电动车
				快速充电站、蓄电池修复仪、蓄电池检测仪以及蓄电池维护等系列产品销售与投
				放。公司依靠科技求发展，不断为用户提供满意的产品，是我们始终不变的追求。 &nbsp;
				&nbsp;近年来公司深入贯彻科学发展观，采取科技兴企策略，采取多项举措广泛招聘吸纳专业技术人才，不断加强人才储备，形成了专业技术职称高低搭配，年龄层次高
				低搭配较为合理的人才队伍。他们技术娴熟、经验丰富、责任心强、实力雄厚。企业信奉质量第一的经营理念，以科技创新作为企业壮大的手段。公司承诺：本公司
				真诚为广大客户提供全方位的售前、售中、售后服务,免费提供专业的技术指导和培训，免费提供设备一年保修，不定期回访，产品终身维护。
			</p>
		</div>
		<div class="row">
			<div class="span12">
				<address>
					<strong>地址：</strong><span>郑州市高新区电子电器产业园盛世嘉业9号楼</span>
				</address>
				<address>
					<strong>电话：</strong><span>4006-315-515 0371-56788915</span>
				</address>
				<address>
					<strong>网站：</strong><span>www.hedong.com.cn</span>
				</address>
				<address>
					<strong>邮箱：</strong><span>admin@hedong.com.cn</span>
				</address>
			</div>
		</div>
		<div style="padding-top: 10px">
			<p>Copyright 2008 - 2016 郑州和动新能源有限公司 www.hedong.com.cn All Rights
				Reserved</p>
			<p>全国服务电话：4006-315-515 电话：0371-56788915 网址：www.hedong.com.cn</p>
		</div>
		<div style="bottom: 10px;">
			<span><font>版权归和动新能源有限公司所有</font></span> <a
				href="http://www.beian.miit.gov.cn/" style="text-decoration: none;"
				target="_blank">豫ICP备18017276号</a>
		</div>
	</div>
</body>
</html>
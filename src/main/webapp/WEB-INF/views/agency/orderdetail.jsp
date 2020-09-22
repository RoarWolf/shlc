<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单详情</title>
<%@ include file="/WEB-INF/views/public/commons.jspf" %>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>
<body>
<div class="container  bs-docs-section">
	<nav class="navbar navbar-default navbar-fixed-top">
		<div align="center" class="bs-example bs-example-tabs" data-example-id="togglable-tabs">
			<ul class="nav nav-tabs" role="tablist">
				<li class="active" role="presentation">
					<a class="btn btn-success" href="#hd1" id="hd1-tab" aria-controls="hd1" role="tab" data-toggle="tab">
					<font size="4px">4月20日</font></a>
				</li>
				<li role="presentation">
					<a class="btn btn-success" href="#hd2" id="profile-tab" aria-controls="hd2" role="tab" data-toggle="tab">
					<font size="4px">4月21日</font></a>
				</li>
				<li role="presentation">
					<a class="btn btn-success" href="#hd3" id="" aria-controls="hd3" role="tab" data-toggle="tab">
					<font size="4px">4月22日</font></a>
				</li>
			</ul>
		</div>
	</nav>
	<div id="myTabContent" class="tab-content" align="center">
	<div role="tabpanel" class="tab-pane fade in active" id="hd1" aria-labelledby="hd1-tab">
		<ol>
			<li>152xxxxx232支付了3元</li>
			<li>152xxxxx232支付了23元</li>
			<li>152xxxxx232支付了10元</li>
			<li>152xxxxx232支付了6元</li>
			<li>152xxxxx232支付了9元</li>
			<li>152xxxxx232支付了12元</li>
			<li>152xxxxx232支付了7.5元</li>
			<li>152xxxxx232支付了15元</li>
		</ol>
	</div>
	<div role="tabpanel" class="tab-pane fade" id="hd2" aria-labelledby="hd2-tab">
		<ol>
			<li>152xxxxx232支付了3元</li>
			<li>152xxxxx232支付了23元</li>
			<li>152xxxxx232支付了10元</li>
			<li>152xxxxx232支付了6元</li>
		</ol>
	</div>
	<div role="tabpanel" class="tab-pane fade" id="hd3" aria-labelledby="hd3-tab">
		<ol>
			<li>152xxxxx232支付了3元</li>
			<li>152xxxxx232支付了23元</li>
			<li>152xxxxx232支付了10元</li>
			<li>152xxxxx232支付了6元</li>
			<li>152xxxxx232支付了9元</li>
			<li>152xxxxx232支付了12元</li>
			<li>152xxxxx232支付了7.5元</li>
			<li>152xxxxx232支付了5.5元</li>
			<li>152xxxxx232支付了6.5元</li>
			<li>152xxxxx232支付了15元</li>
			<li>152xxxxx232支付了8元</li>
			<li>152xxxxx232支付了18元</li>
		</ol>
	</div>
	</div>
</div>
<div style="position: fixed;bottom: 0px; ">
	<a href="${hdpath }/agency/index" style="width: 100%" class="btn btn-success"><font size="9px">返回代理首页</font></a>
</div>
</body>
</html>
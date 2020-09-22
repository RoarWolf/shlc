<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>使用说明</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<style type="text/css">
	body {
			background-color: rgba(239,239,244,.4);
		}
		.app {
			font-size: 14px;
			color: #666;
			padding: 55px 0 0 0;
		}
		.app .detail ul {
			background: transparent;
		}
		.app .detail ul li {
			background-color: #fff;
			font-size: 12px;
		}
		.app .detail ul li a {
			text-decoration: none;
		}
		.app .detail ul li:after{
			left: 0;
		}
		.app .icon-reg {
			padding:10px 0;
			background-color: #fff;
			margin: 0 auto;
			border-radius: 0 0 8px 8px;
			border: 1px solid #c8c7cc;
			border-top: none;
			overflow: hidden;
			margin-bottom: 20px;
			background-color: #f5f7fa;
			display: none;
		}
		ol {
			padding: 5px 15px;
			color: #666;
			list-style: none;
		}
		ol li{
			padding: 3px 0;
			color: #333;
		}
		ol li strong {
			margin-top: 15px;
			display: inline-block;
			color: #333;
			font-size: 15px;
		}
		ol li .ser-wechart {
			display: block;
			width: 80%;
			margin: 10px auto;
		}
</style>
</head>
<%-- <body>
<header class="mui-bar mui-bar-nav">
	<h1 class="mui-title"><font size="6px">使用说明</font></h1>
</header>
<div class="mui-content">
	<ul id="list" class="mui-table-view mui-table-view-chevron">
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>注册账号</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.关注公众号（自助充电平台）<br>
					2.点击商家登陆<br>
					3.进入手机号绑定<br>
					4.绑定成功后进入属于自己的商家后台<br><br>
					<strong>说明：点击商家登陆需注意</strong><br>
					1.进入绑定手机号页面（未注册）<br>
					2.进入商家后台（已注册）<br>
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>模块与账号绑定</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.进入商家后台<br>
					2.点击管理 -> 设备绑定（会弹出扫描二维码的场景）<br>
					3.扫描模块的二维码后自动绑定到自己的账户上<br><br>
					<strong>模块绑定需注意</strong><br>
					如果第一次绑定模块，进行绑定显示已绑定，敬请联系厂家	
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>绑定银行卡</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.进入商家后台<br>
					2.	->点击我的<br>
						&nbsp;&nbsp;&nbsp;&nbsp;->点击我的钱包<br>
						&nbsp;&nbsp;&nbsp;&nbsp;->点击我的银行卡<br>
						&nbsp;&nbsp;&nbsp;&nbsp;->点击添加银行卡<br>
						&nbsp;&nbsp;&nbsp;&nbsp;->输入完信息后确认提交<br><br>
					<strong>查看银行卡信息</strong><br>
					1.进入我的银行卡<br>
					2.随意点击你想查看的银行卡，可以查看信息<br>
					3.信息功能（修改银行卡信息、解除绑定银行卡）<br><br>
					<strong>绑定银行卡需注意</strong><br>
					1、填写信息必须真实，否则提现不予通过 <br>
					2、银行卡实名按照第一次绑定的实名为准，再次添加银行卡实名不唯一，添加银行卡将不通过。
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>如何提现</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					微信零钱提现：<br>
					1.进入商家后台<br>
					2.
					<ul class="mui-table-view mui-table-view-chevron" style="margin-bottom: 0px;margin-top: 0px">
						<li class="mui-table-view-cell">
							->a、点击我的
							->b、点击我的钱包<br>
							->c、点击提现<br>
							->d、进行选择微信零钱提现<br>
							->e、输入金额<br>
							->f、点击立即提现（首次提现会弹出填写姓名，填写与当前微信实名认证相同的姓名）
						</li>
					</ul>
					3.提现最低5元<br><br>
					银行卡提现：<br>
					同微信零钱提现，在第二步第d步时选择银行卡提现<br><br>
					注：微信零钱提现实时到账（首次提现需填写与微信实名认证相同的姓名）<br>
						银行卡提现需审核，到账时间1-2个工作日（首次提现需绑定一张银行卡）<br>
						推荐使用微信零钱提现，实时到账，更方便
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>设备测试</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					<strong>未绑定</strong><br>
					1.扫描充电设备上的二维码<br>
					2.选择端口、充电时间<br>
					3.点击测试提交按钮，完成测试<br><br>
					<strong>已绑定</strong>（远程续充）<br>
					1.进入自助充电平台公众号<br>
					2.点击右下角商家登陆，进入商家后台<br>
					3.点击底部导航管理<br>
					4.点击远程充操作<br>
					5.点击远程充电<br>
					6.选择端口、充电时间<br>
					7.点击确认按钮完成操作<br>
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>支付页面品牌、服务电话显示与修改</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.点击商家登陆，进入商家后台<br>
					2.点击最下方管理导航，进入管理页面<br>
					3.点击收费模板管理，进入收费模板页面<br>
					4.根据自己设备所绑定的收费模板点击当前行最右边‘+’<br>
					5.点击最下方编辑，即可根据自己需要进行修改<br><br>
					提示：收费模板系统会默认收费模板，如需修改可查看下方收费模板修改					
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>收费模板修改</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.点击商家登陆，进入商家后台<br>
					2.点击最下方管理导航，进入管理页面<br>
					3.点击设备管理，进入设备管理页面<br>
					4.根据自己需要选择设备最右方操作按钮<br>
					5.弹出框后，点击收费模板选择<br>
					6.根据自己需要修改收费模板，完成以后点击确认按钮即可<br>
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>如何续充</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1.进入公众号【自助充电平台】<br>
					2.点击下方菜单【正在充电】<br>
					3.会出现自己正在充电的信息以及以往的充电记录<br>
					4.点击续充，选择充电时间，点击立即支付则续充成功<br>
					5.成功后会跳回正在充电信息页面，可以点击更新查看实时充电信息<br>
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>设置模块类型</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					未绑定<br>
					&nbsp;&nbsp;1.用手机扫描模块二维码<br>
					&nbsp;&nbsp;2.扫描进入选择类型<br><br>
					已绑定<br>
					&nbsp;&nbsp;1.先在商户后台解绑模块<br>
					&nbsp;&nbsp;2.重复未绑定的操作<br><br>
					
					模块类型：<br>
					&nbsp;&nbsp;智慧款十路<br>
					&nbsp;&nbsp;电轿款（适用于大功率充电机器，只有两路）<br>
					&nbsp;&nbsp;脉冲板子（替代投币器，保险丝板子）<br>
					&nbsp;&nbsp;离线充值机（给卡充值模块）<br><br>
					
					注：类型只可选择一次，后续修改请联系客服
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>小区管理</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px">
					1、查看小区列表<br>
					&nbsp;&nbsp;①进入商家后台，点击底部导航‘管理’<br>
					&nbsp;&nbsp;②点击按钮‘小区管理’即可<br><br>
					2、添加小区<br>
					&nbsp;&nbsp;①进入小区列表，点击底部按钮‘添加小区’<br>
					&nbsp;&nbsp;②信息输入完成后点击确认即添加成功<br><br>
					3、管理小区<br>
					&nbsp;&nbsp;进入小区列表，点击随机一个小区后面的管理按钮<br>
					&nbsp;&nbsp;①绑定副账号<br>
					&nbsp;&nbsp;&nbsp;&nbsp;1)点击底部按钮‘绑定副号’<br>
					&nbsp;&nbsp;&nbsp;&nbsp;2)填写完手机号确认即可（注：手机号须平台已注册过的手机号）<br><br>
					&nbsp;&nbsp;②添加设备<br>
					&nbsp;&nbsp;&nbsp;&nbsp;1)点击底部按钮‘添加设备’<br>
					&nbsp;&nbsp;&nbsp;&nbsp;2)选择需要添加的设备后点击确认即可<br><br>
					&nbsp;&nbsp;③删除设备<br>
					&nbsp;&nbsp;&nbsp;&nbsp;找到需要删除的设备点击删除按钮即可<br><br>
					&nbsp;&nbsp;④编辑小区<br>
					&nbsp;&nbsp;&nbsp;&nbsp;1)点击底部按钮‘编辑小区’<br>
					&nbsp;&nbsp;&nbsp;&nbsp;2)填写需要修改的信息后点击确认即可<br><br>
					&nbsp;&nbsp;⑤删除小区<br>
					&nbsp;&nbsp;&nbsp;&nbsp;1)点击底部按钮‘删除小区’<br>
					&nbsp;&nbsp;&nbsp;&nbsp;2)弹出确认、取消框，点击确认即删除成功<br><br>
					4、查看未绑定小区的设备<br>
					&nbsp;&nbsp;①进入小区列表，找到小区列表下面的‘未绑定设备’链接并点击即可<br><br>
					5、副账号查看订单<br>
					&nbsp;&nbsp;①进入商家后台，点击底部导航‘管理’<br>
					&nbsp;&nbsp;②点击最上方按钮‘订单统计’即可<br><br>
					注：<br>
					如副账号未查看到订单，请检查设备--小区--副账号是否关联，删除小区或者小区删除设备，副账号是看不到设备的任何订单
				</li>
			</ul>
		</li>
		<li class="mui-table-view-cell mui-collapse">
			<a class="mui-navigate-right" href="#" style="font-size: 14px">
				<strong>客服微信</strong>
			</a>
			<ul class="mui-table-view mui-table-view-chevron">
				<li id="innerli" class="mui-table-view-cell" style="font-size: 14px;text-align: center;">
					如有疑问可扫描下方二维码添加客服微信
					<img src="${hdpath }/images/server1.jpg" style="width: 60%">
				</li>
			</ul>
		</li>
	</ul>
</div>
</body> --%>
<body>
	<div class="app">
		<header class="mui-bar mui-bar-nav">
			<h1 class="mui-title">使用说明</h1>
		</header>
		<main>
			<ul class="mui-table-view">
			    <li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >注册账号</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、关注公众号（自助充电平台）</li>
			   				<li>2、点击“后台管理”-“商家登录”</li>
			   				<li>3、进入手机号绑定</li>
			   				<li>4、绑定成功后进入属于自己的商家后台</li>
			   				<li><strong>点击商家登陆需注意</strong></li>
			   			
			   				<li>1、进入绑定手机号页面（未注册）</li>
			   				<li>2、进入商家后台（已注册）</li>
			   			</ol>
			   		</div>
			   <li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >绑定设备</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、进入“商家后台” </li>
			   				<li>2、点击“设备绑定”（会弹出扫描二维码的场景）</li>
			   				<li>3、扫描模块的二维码后自动绑定到自己的账户上</li>
			   				
			   				<li><strong>设备绑定需注意 </strong></li>
			   				<li>如果第一次绑定模块，进行绑定显示已绑定，敬请联系厂家	</li>
			   			</ol>
			   		</div>
			   <li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >绑定银行卡</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、进入“商家后台” -&gt; 点击“我的”进入个人中心 -&gt; 点击“我的银行卡” -&gt; 点击“添加银行卡”填写并提交即可</li>
			   				<li><strong>查看绑定的银行卡信息 </strong></li>
			   				<li>1、进入“我的银行卡”</li>
			   				<li>2、已绑定的卡会展示在“我的银行卡”页面，点击银行卡列表查看对应的银行卡信息</li>
			   				<li>3、银行卡信息功能（修改银行卡信息、解除绑定的银行卡）</li>
			   				<li><strong>绑定银行卡需注意 </strong></li>
			   				<li>1、填写信息必须正视，否则提现不予通过</li>
			   				<li>2、银行卡实名按照第一次绑定的实名为准，再次添加银行卡实名不唯一，添加银行卡将不通过。</li>
			   			</ol>
			   		</div>
			   	 <li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >如何提现</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>进入“商家后台” -&gt; 点击“我的”进入个人中心 -&gt; 点击“提现” 选择提现方式</li>
			   				<li><strong>提现到微信零钱 </strong></li>
			   				<li>1、点击“提现到微信零钱”，会跳转到“提现到微信零钱”界面</li>
			   				<li>2、输入“提现金额”（每次最低提现5元）</li>
			   				<li>3、点击“确认提现”即可（首次提现会弹出填写姓名，填写与当前微信实名认证相同的姓名）</li>
			   				<li><strong>提现到银行卡</strong></li>
			   				<li>1、点击“提现到银行卡”，会跳转到“提现到银行卡”界面</li>
			   				<li>2、选择需要提现到的银行卡，输入“提现金额”</li>
			   				<li>3、点击“确认提现”即可</li>
			   				<li><strong>提现到对公账户</strong></li>
			   				<li>1、点击“提现到对公账户”，会跳转到“提现到对公账户”界面</li>
			   				<li>2、输入“提现金额”</li>
			   				<li>3、点击“确认提现”即可</li>
			   				<li><strong>提示：</strong></li>
			   				<li>1、微信零钱提现实时到账（首次提现需填写与微信实名认证相同的姓名）</li>
			   				<li>2、银行卡提现需审核，到账时间1-2个工作日（首次提现需绑定一张银行卡）</li>
			   				<li>3、对公账户提现一般7个工作日内到账</li>
			   				<li>4、推荐使用微信零钱提现，实时到账，更方便</li>
			   			</ol>
			   		</div>

			   	<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >设备测试</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li><strong>未测试</strong></li>
			   				<li>1、扫描充电设备上的二维码</li>
			   				<li>2、选择端口、充电时间</li>
			   				<li>3、点击测试提交按钮，完成测试</li>
			   				<li><strong>已绑定</strong>（远程续充）</li>
			   				<li>1、进入自助充电平台公众号</li>
			   				<li>2、点击右下角商家登陆，进入“商户平台”</li>
			   				<li>3、点击“设备列表”，点击“远程”按钮</li>
			   				<li>4、选择端口和充电时间，点击“确定”即可</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >支付页面品牌、服务电话显示与修改</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、进入“后台管理”，点击“设备管理”找到对应的设备，点击“管理”按钮</li>
			   				<li>2、进入设备管理界面，点击“收费模板”</li>
			   				<li>3、在选中的设备模板，点击“编辑”按钮，进入“收费模板管理”界面（如没有模板的话会使用系统默认模板，可以添加下面的“添加主模板”来添加自己的收费模板）</li>
			   				<li>4、点击“编辑”按钮来编辑对应充电界面要展示的信息</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >收费模板修改</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、进入“后台管理”，点击“设备管理”找到对应的设备，点击“管理”按钮</li>
			   				<li>2、进入设备管理界面，点击“收费模板”</li>
			   				<li>3、在选中的设备模板，点击“编辑”按钮，进入“收费模板管理”界面（如没有模板的话会使用系统默认模板，可以添加下面的“添加主模板”来添加自己的收费模板）</li>
			   				<li>4、根据自己需要修改收费模板，（如：十路智慧款的模板，显示名称： 扫码界面用户选择显示的名称、充电价格： 用户支付的金额，充电时间： 用户充电的时间，消耗电量：用户本次充电最大消耗电量）</li>
			   				<li>5、根据自己需要修改收费模板，完成以后点击确认按钮即可</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >用户如何 续充</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、进入“后台管理”，点击“设备管理”找到对应的设备，点击“管理”按钮</li>
			   				<li>2、进入设备管理界面，点击“收费模板”</li>
			   				<li>3、在选中的设备模板，点击“编辑”按钮，进入“收费模板管理”界面（如没有模板的话会使用系统默认模板，可以添加下面的“添加主模板”来添加自己的收费模板）</li>
			   				<li>4、根据自己需要修改收费模板，（如：十路智慧款的模板，显示名称： 扫码界面用户选择显示的名称、充电价格： 用户支付的金额，充电时间： 用户充电的时间，消耗电量：用户本次充电最大消耗电量）</li>
			   				<li>5、根据自己需要修改收费模板，完成以后点击确认按钮即可</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >设置模块类型</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li><strong>未绑定</strong></li>
			   				<li>1、用手机扫描模块二维码</li>
			   				<li>2、扫描进入选择类型</li>
			   				<li><strong>已绑定</strong></li>
			   				<li>1、先在商户后台解绑模块</li>
			   				<li>2、重复未绑定的操作</li>
			   				<li><strong>智慧款十路</strong></li>
			   				<li>1、电轿款（适用于大功率充电机器，只有两路）</li>
			   				<li>2、离线充值机（给卡充值模块）</li>
			   				<li>注：类型只可选择一次，后续修改请联系客服</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >小区管理</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li><strong>查看小区列表</strong></li>
			   				<li>1、进入“后台管理”，点击底部导航“管理”</li>
			   				<li>2、点击“小区管理”，进去小区列表页</li>
			   				<li><strong>添加小区</strong></li>
			   				<li>1、小区列表页中点击底部的“添加小区”按钮</li>
			   				<li>2、输入“小区名”和“地址”（可选）点击确定即可创建小区</li>
			   				<li><strong>添加设备</strong></li>
			   				<li>1、在小区管理中点击底部导航栏“添加设备”按钮</li>
			   				<li>2、点击“请选择设备编号”，会弹出设备选项框</li>
			   				<li>3、选中设备点击确定即可将选中的设备添加到该小区中</li>

			   				<li><strong>选中模板</strong></li>
			   				<li>1、在小区管理中点击底部导航栏“模板按钮”按钮</li>
			   				<li>2、会弹出选择模板弹窗，点击“钱包模板”进入钱包模板页面，选择想要选中的模板左下角的“选择模板”</li>
			   				<li>3、选择成功后即可在本小区中使用该钱包模板（在线卡模板选择同钱包模板操作一样）</li>

			   				<li><strong>添加设备</strong></li>
			   				<li>1、在小区管理中点击底部导航栏“添加设备”按钮</li>
			   				<li>2、点击“请选择设备编号”，会弹出设备选项框</li>
			   				<li>3、选中设备点击确定即可将选中的设备添加到该小区中</li>

			   				<li><strong>添加合伙人</strong></li>
			   				<li>1、在小区管理中点击“添加合伙人”，会弹出输入框</li>
			   				<li>2、在输入框中输入合伙人的电话和分成比即可，合伙人昵称和姓名会自动显示（注意：合伙人必须提前在自助充电平台注册，注册方式见上面“注册账号”，所有合伙人的分成比相加不能大于100%）</li>
			   				<li>3、填完之后点击确定即可（每个小区最多添加4个合伙人）</li>

			   				<li><strong>删除小区</strong></li>
			   				<li>1、在小区管理中点击底部导航栏“删除小区”按钮，会弹出提示框</li>
			   				<li>2、点击“确定”可以删除本小区</li>
			   			</ol>
			   		</div>
	
			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >设备缴费说明</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>设备出厂我们默认为每台设备免费赠送一年的流量费</li>
			   				<li>设备到期需要对设备进行续费，每次续费成功到期时间往后延迟一年</li>
			   				<li><strong>缴费流程</strong></li>
			   				<li>商户进入“后台管理”，点击“管理”、“缴费管理”进入“商户缴费列表”</li>
			   				<li><strong>为未绑定小区设备缴费</strong></li>
			   				<li>1、在“商户缴费列表”中，选择顶部的导航栏“按设备缴费”，显示未绑定小区的设备列表（字体为红色的设备表示为已到期设备，字体为橘色的设备为即将到期的设备）</li>
			   				<li>2、选中需要续费的设备，点击右下角的“去结算”按钮</li>
			   				<li>3、选择支付方式（支付方式有2种： 1、钱包支付、使用商户收益进行缴费。 2、微信支付），并支付即可完成缴费</li>
			   				<li><strong>为绑定在小区中的设备缴费</strong></li>
			   				<li>1、在“商户缴费列表”中，选择顶部的导航栏“按小区缴费”，会展示小区列表（列表中会显示，到期设备和即将到期设备的数量）</li>
			   				<li>2、找到需要缴费的小区，点击“缴费”按钮进入缴费界面，（如果改小区有合伙人，默认没有开启合伙人分摊交费，如果需要开启，请看下面“开启合伙人分摊缴费”）</li>
			   				<li>3、选择支付方式（支付方式有2种： 1、钱包支付、使用商户收益进行缴费。 2、微信支付），并支付即可完成缴费</li>
			   				<li><strong>开启合伙人分摊缴费</strong></li>
			   				<li>1、商户在“后台管理”中，点击“我的”，找到“设置”-“缴费设置”，将“是否开通合伙人自动分摊缴费”的开关打开即可，</li>
			   				<li>注意： 如果开启开启合伙人分摊缴费，合伙人的缴费金额是由合伙人的分成比来确定的。并且合伙人分摊交费金额是从合伙人收益中扣除。</li>
			   				<li><strong>查看缴费记录</strong></li>
			   				<li>1、在“商户缴费列表”中，点击“查看历史缴费记录”即可查看“缴费记录”</li>
			   			</ol>
			   		</div>

			   		<!-- <li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >钱包退费说明</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>场景：用户充值钱包，因某种原因用户不再使用钱包，需要退费；分为以下两种情况</li>
			   				<li><strong>用户未使用过钱包消费</strong></li>
			   				<li>1、在商户在“后台管理”中，点击“管理”，然后点击“订单统计”找到改用户充值的钱包订单</li>
			   				<li>2、点击该订单，跳转到订单详情界面，点击右下角的“退款”按钮，进行退款操作即可。（用户支付金额会原路返回，充值钱包的充值/赠送金额也会相应减去）</li>
			   				<li><strong>用户使用过钱包消费</strong></li>
			   				<li>1、将用户消费的“充值金额”退还的给商户（私下交易（可通过微信转账的方式转给商户），不经过平台。退还金额计算方式： 订单的“充值消费”—用户钱包现有的“充值金额”）</li>
			   				<li>2、点击虚拟充值，给用户钱包的“充值金额”进行虚拟充值，使用户的“充值余额”等于订单的“充值消费”</li>
			   				<li>3、然后在“后台管理”中，点击“管理”，然后点击“订单统计”找到改用户充值的钱包订单</li>
			   				<li>4、点击该订单，跳转到订单详情界面，点击右下角的“退款”按钮，进行退款操作即可。（用户支付金额会原路返回，充值钱包的充值/赠送金额也会相应减去）</li>
			   			</ol>
			   		</div> -->
			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >包月使用及操作说明</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li><strong>商户包月操作说明</strong></li>
			   				<li><a href="https://mp.weixin.qq.com/s/MeldrFHBRRgwgHKJhjc5Cw">商户包月操作说明</a></li>
			   				<li><strong>用户包月使用说明</strong></li>
			   				<li><a href="https://mp.weixin.qq.com/s/ocYZmJeDCevilU0eS9NInw">用户包月使用说明</a></li>
			   			</ol>
			   		</div>
			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >在线卡使用及操作说明</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li><strong>商户在线卡操作说明</strong></li>
			   				<li><a href="https://mp.weixin.qq.com/s/P_8QZS_XjUxdVRTBXtZM2A">商户在线卡操作说明</a></li>
			   				<li><strong>用户在线卡使用说明</strong></li>
			   				<li><a href="https://mp.weixin.qq.com/s/wAlVp7l9sGUwD8vitQklUA">用户在线卡使用说明</a></li>
			   			</ol>
			   		</div>
			   		
			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >商户绑定子账号流程</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li>1、注册的微信需关注”自助充电平台“公众号，进入公众号，点击”后台管理“-”商家登录“</li>
			   				<li>2、进入注册页面（注：此微信未在本平台注册过商户/商户子账号），依次输入手机号、邀请码、验证码。<strong>注意： 邀请码一栏要输入商户的手机号</strong></li>
			   				<li>3、输入完成点击 ”登录“即可完成注册子账号</li>
			   				<li>4、商户进入“后台管理”-“我的”，找到子账号管理，点击“添加”，输入子账号的手机号，点击“提交”，即可完成子账号的绑定</li>
			   			</ol>
			   		</div>

			   		<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="javascript:void(0);" >客服微信</a></li>
			   		<div class="icon-reg">
			   			<ol>
			   				<li style="text-align: center;"><strong>如有疑问可扫描下方二维码添加客服微信</strong></li>
			   				<li>
								<img class="ser-wechart" src="${hdpath}/images/server-wechart.gif" alt="">
			   				</li>
			   			</ol>
			   		</div>

			</ul>
		</main>
	</div>

	<script>
	$(function(){
		var index=1
		// $('.app .mui-table-view .mui-table-view-cell').eq(1).slideDown(250)
		$('.app .mui-table-view .mui-table-view-cell').click(function(e){
			if($(this).index() != index){
				index= $(this).index()
				$('.detail .mui-table-view>div').slideUp(250)
				$(this).parent().children().eq($(this).index()+1).slideDown(250)
			}else{
				$(this).parent().children().eq($(this).index()+1).slideUp(250)
				index= undefined
			}
			$(this).siblings('.mui-table-view-cell').next().slideUp(250)
		})
	})
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <title>智慧款模板管理</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <link rel="stylesheet" href="${hdpath}/css/base.css"/>
    <link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css"/>
    <link rel="stylesheet" href="${hdpath}/hdfile/css/tem.css"/>
    <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <script src="${hdpath}/js/jquery.js"></script>
    <script src="${hdpath}/mui/js/mui.min.js"></script>
    <script src="${hdpath}/hdfile/js/tem.js"></script>
<style>
	.app {
		padding: 15px 10px 50px;
		color: #666;
		font-size: 14px;
	}
	.app .content>li{
		padding: 15px 10px;
		border: 1px solid #81e29e;
		border-radius: 6px;
		background-color: #f5f7fa;
		margin-bottom: 10px;
		box-shadow: 5px 5px 5px #aaa;
	}
	.app .content>li.gradLi {
		margin-bottom: 0;
	}
	.app .content>li.lastGrade {
		margin-bottom: 10px;
	}
	.app .content>li.lastGrade,
	.app .content>li.secondGrade {
		border-top: none; 
	}
	.app .content>li.lastGrade .select,
	.app .content>li.secondGrade .select {
		display: none;
	}
	.app .content>li.lastGrade .edit,
	.app .content>li.secondGrade .edit {
		visibility: hidden;
	}
	
	.app .content>li.active {
		border-color: #81e29e;
	}
	.select_icon {
		width: 25px;
	 	height: 25px;
		border-radius: 50%;
		border: 1px solid #999;
		text-align: center;
		line-height: 25px;
		font-size: 22px;
		color: transparent;
	}
	.app .content>li.active .select_icon {
		background-color: #007aff;
		color: #fff;
		border-color: #007aff;
	}
	.app .content>li>.top {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.app .content>li>.bottom {
		display: flex;
		justify-content: space-around;
		align-items: center;
	}
	.app .content>li>.bottom {
		margin-top: 15px;
	}
	.app .content>li>.bottom .btn {
		width: 31%;
		text-align: center;
		/* background-color: #4cd964; */
		border: 1px solid #4cd964;
		border-radius: 30px;
	    color: #4cd964;
	    height: 30px;
	    line-height: 30px;
	}
	.app .content>li>.bottom .btn a {
		display: bolck;
		widht: 100%;
		text-align: center;
		/* background-color: #4cd964; */
	    color: #4cd964;
	    height: 30px;
	    line-height: 30px;
	}
	.app .content input[type=checkbox] {
		top: 0px;
		right: 0px;
	}
	.app .content .mui-checkbox input:before {
		font-size: 25px;
	}
	.app .content .mui-checkbox input[disabled]:before{
		opacity: 1;
	}
	.navBar {
		/* display: flex;
		align-items: center;
		justify-content: center; */
		text-align: center;
	}
	.navBar button {
		width: 75%;
	}
	.navBar button a {
		display: block;
		text-decoration: none;
		color: #fff;
	}

.tem-mask2 {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 8;
  display: none;
}
.list-center1,
.list-center2 {
  z-index: 9;
  width: 80vw;
  background-color: #fff;
  border-radius: 0.38647343rem;
  position: absolute;
  left: 50%;
  top: calc(50% - 0.9961rem);
  transform: translate(-50%, -50%);
}
.list-center1,
.list-center2 {
  padding: 0.57971014rem;
}
.list-center1 h3,
.list-center2 h3 {
  margin: 0 auto;
  font-size: 0.57971014rem;
  font-weight: 600;
  text-align: center;
  margin-top: 0.57971014rem;
  padding-bottom: 0.57971014rem;
  border-bottom: 0.03864734rem solid #DCDFE6;
}
.list-center1 form,
.list-center2 form {
  font-size: 0.5410628rem;
  color: #666;
  padding-top: 0.38647343rem;
}
.list-center1 form .inp,
.list-center2 form .inp {
  margin-bottom: 0.57971014rem;
}
.list-center1 form input,
.list-center2 form input {
  margin-top: 0.19323671rem;
  height: 1.27536232rem;
  border-radius: 0.23188406rem;
  width: 80%;
  margin-bottom: 0;
  font-size: 0.46376812rem;
}
.list-center1 form input::-webkit-input-placeholder,
.list-center2 form input::-webkit-input-placeholder {
  color: #c0c0c0;
  font-size: 0.46376812rem;
}
/* 濠电偠鎻紞锟芥繛澶嬫礋瀵偊藝濠曠�妅it闂備礁鎲￠崝鏇㈠箠韫囨稒鍋嬮柟鎯板Г閸庡秹鏌涢弴銊ュ闁靛牆顭烽幃宄扳枎韫囨洖顣哄┑鐐叉嫅閹凤拷 */
.list-center1 form input:-moz-placeholder,
.list-center2 form input:-moz-placeholder {
  color: #c0c0c0;
  font-size: 0.46376812rem;
}
/* Firefox闂備胶绮〃鍛存偋婵犲偊鑰块柨鐕傛嫹4-18 */
.list-center1 form input::-moz-placeholder,
.list-center2 form input::-moz-placeholder {
  color: #c0c0c0;
  font-size: 0.46376812rem;
}
/* Firefox闂備胶绮〃鍛存偋婵犲偊鑰块柨鐕傛嫹19+ */
.list-center1 form input:-ms-input-placeholder,
.list-center2 form input:-ms-input-placeholder {
  color: #c0c0c0;
  font-size: 0.46376812rem;
}
/* IE婵犵數鍋炲娆戞崲濡ゅ拑缍栫�广儱顦梻顖炴煥閻曞倹瀚� */
.list-center1 form .inp,
.list-center2 form .inp {
  position: relative;
}
.list-center1 form .btn,
.list-center2 form .btn {
  position: absolute;
  right: 0.57971014rem;
  bottom: 0.19323671rem;
  color: #4cd964;
}
.list-center1 .bottom,
.list-center2 .bottom {
  width: 100%;
  margin: 0.77294686rem 0 0 0;
  display: flex;
  justify-content: flex-end;
}
.list-center1 .bottom .mui-btn-success,
.list-center2 .bottom .mui-btn-success {
  font-size: 0.5410628rem;
  padding: 0.11594203rem 0.23188406rem;
  margin-right: 0.19323671rem;
}
.list-center1 .close,
.list-center2 .close {
  position: absolute;
  top: 0.38647343rem;
  right: 0.38647343rem;
  color: #666;
}
.list-center2 form .inp.radio-inp {
  display: flex;
  /*justify-content: space-between;*/
}
.list-center2 form .inp.radio-inp h5 {
  color: #666;
  margin-right: 0.69565217rem;
}
.list-center2 form .inp.radio-inp label {
  display: flex;
  align-items: center;
  font-size: 0.46376812rem;
  color: #666;
}
.list-center2 form .chargeInfoTe {
	overflow: hidden;
}
.list-center2 form .chargeInfoTe h5 {
	float: left;
	color: #666;
}
.list-center2 form .chargeInfoTe button {
	float: left;
	margin-right: 0.19323671rem;
    padding: 0.11594203rem 0.23188406rem;
    font-size: 0.46376812rem;
    margin-left: 0.8rem;
}

.list-center2 form .inp input[name="isRef"],
.list-center2 form .inp input[name="isWalletPay"],
.list-center2 form .inp input[name="isMonthly"],
.list-center2 form .inp input[name="isGrad"] {
  width: 0.69565217rem;
  height: 0.69565217rem;
  margin: 0;
  margin-left: 0.38647343rem;
}
#popover {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 8;
  display: none;
}
#popover ul li label {
  display: flex;
  align-items: center;
}
#popover ul {
  overflow: hidden;
  width: 50%;
  z-index: 12;
  background-color: #fff;
  border-radius: 0.38647343rem;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.5410628rem;
}
#popover ul input {
  width: 0.69565217rem;
  height: 0.69565217rem;
  margin-right: 0.38647343rem;
}
#popover label {
  color: #666;
}
#popover .lastButton button:first-child {
  float: left;
  position: initial;
  -webkit-transform: none;
  transform: none;
  padding: 0.15458937rem 0.23188406rem;
  font-size: 0.46376812rem;
  margin-left: 15%;
}
#popover .lastButton button:last-child {
  float: right;
  position: initial;
  -webkit-transform: none;
  transform: none;
  padding: 0.15458937rem 0.23188406rem;
  font-size: 0.46376812rem;
  margin-right: 15%;
}
.chargeTextMask {
	position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.6);
    z-index: 8;
    display: none;
}
.chargeTextMask .ctm {
	overflow: hidden;
    width: 80%;
    z-index: 12;
    background-color: #fff;
    border-radius: 0.38647343rem;
    position: absolute;
    left: 50%;
   	top: calc(50% - 0.9961rem);
    transform: translate(-50%, -50%);
    font-size: 0.5410628rem;
    padding: 0.57971014rem;
}
.chargeTextMask .ctm h3{
	font-size: 0.57971014rem;
	text-align: center;
	font-weight: 600;
    text-align: center;
    margin-top: 0.57971014rem;
    padding-bottom: 0.57971014rem;
    border-bottom: 0.03864734rem solid #dedede;
}
.chargeTextMask .ctm .areaText {
	padding: 0.65rem 0 0;
}
.chargeTextMask .ctm .areaText textarea {
	padding: 0.5rem;
	height: 6rem;
	color: #333;
	margin: 0;
	font-size: 0.46376812rem;
	border: 0.03864734rem solid #ddd;
}
.chargeTextMask .ctm .tipDiv {
	padding: 0.5rem 0;
	color: #999;
	font-size: 0.46376812rem;
}
.chargeTextMask .ctm .tipDiv p {
	margin-bottom: 0;
	color: #8f8f94;
}
.chargeTextMask .ctm .bottom{
	display: flex;
	justify-content: flex-end;
	padding-top: 0.5rem;
	border-top: 0.03864734rem solid #dedede;
}
.chargeTextMask .ctm .bottom button {
	font-size: 0.5410628rem;
    padding: 0.15rem 0.23188406rem;
}
.chargeTextMask .ctm .bottom .closeText {
	margin-right: 0.19323671rem;
}
.chargeTextMask .ctm .selectText {
	display: flex;
	justify-content: space-around;
	margin-top: 0.3rem;
}
.chargeTextMask .ctm .selectText button {
	margin-right: 0.19323671rem;
    padding: 0.15rem 0.5rem;
    font-size: 0.46376812rem;
    margin-left: 0.8rem;
}


@media screen and (max-height: 600px) {
  .list-center1 form,
  .list-center2 form {
    display: flex;
    flex-wrap: wrap;
  }
  .list-center1 form input,
  .list-center2 form input {
    width: 95%;
  }
  .list-center1 form .inp,
  .list-center2 form .inp {
    width: 50%;
    margin-bottom: 0.19323671rem;
  }
  .list-center1 form .inp.radio-inp,
  .list-center2 form .inp.radio-inp,
  .list-center1 form .inp.ol,
  .list-center2 form .inp.ol {
    width: 100%;
  }
  .list-center1 .bottom,
  .list-center2 .bottom {
    margin-top: 0.19323671rem;
  }
  .list-center1 form .btn,
   .list-center2 form .btn {
   	bottom: 0;
   }
   .chargeTextMask .ctm .areaText {
   	padding: 0;
   }
   .chargeTextMask .ctm .selectText {
   	margin-top: 0;
   }
   .chargeTextMask .ctm .tipDiv {
   	padding: 0;
   }
}
</style>
</head>
<body>
	<!-- 重设设备类型 -->
	<c:set 
		var="fmtDeviceType" 
		value="${(deviceType == '00' || deviceType == '01' || deviceType == '02' || deviceType == '05' || deviceType == '06' || deviceType == '07') ? '01' : '08'}" />
	<div class="app" data-code="${code}">
		<ul class="content">
		<c:if test="${fmtDeviceType == '08'}">
			<c:forEach items="${templatelist}" var="tempparent">
				<li  data-id="${tempparent.id}" class="<c:if test="${tempparent.pitchon == 1}">active</c:if>">
					<div class="top">
							<div class="tem_Title">
								<span>模板名称：</span>
								<span>
									${tempparent.name}
									${tempparent.merchantid == 0 ? '（系统模板）' : ''}
								</span>
							</div>
							<div class="select">
								<div class="mui-icon mui-icon-checkmarkempty select_icon"></div>
							</div>
					</div>
					<div class="bottom">
						<div class="btn">
							<a href="/merchant/appointTemdata?code=${code}&tempid=${tempparent.id}">预览</a>
						</div>
						<div class="btn tempMoreToDevice" data-id="${tempparent.id}">
							模板复用
						</div>
						<div  class="btn">
							<a href="/merchant/timetemdata?code=${code}&tempid=${tempparent.id}">${tempparent.merchantid == 0 ? '查看' : '编辑'}</a>
						</div>
					</div>
				</li>
			</c:forEach>
		</c:if>
		<!-- 智慧款模板开始 -->
		<c:if test="${fmtDeviceType != '08'}">
		<c:forEach items="${templatelist}" var="tempparent">
			<c:if test="${tempparent.grade == 2 || tempparent.grade == 0}">  <!-- 过滤掉分类模板，因为在templatelist中存在分类模板 -->
				<li  data-id="${tempparent.id}" class="<c:if test="${tempparent.pitchon == 1}">active</c:if>">
					<div class="top">
							<div class="tem_Title">
								<span>模板名称：</span>
								<span>
								${tempparent.name}
								${tempparent.merchantid == 0 ? '（系统模板）' : ''}
								</span>
							</div>
							<div class="select">
								<div class="mui-icon mui-icon-checkmarkempty select_icon"></div>
							</div>
					</div>
					<div class="bottom">
						<div class="btn">
							<a href="/merchant/appointTemdata?code=${code}&tempid=${tempparent.id}">预览</a>
						</div>
						<div class="btn tempMoreToDevice" data-id="${tempparent.id}">
							模板复用
						</div>
						<div  class="btn">
							<a href="/merchant/appointTeminfo?code=${code}&tempid=${tempparent.id}">${tempparent.merchantid == 0 ? '查看' : '编辑'}</a>
						</div>
					</div>
				</li>
			</c:if>

		 </c:forEach>
		 
		 <!-- 分等级模板开始 -->
		 <c:forEach items="${tempgather}" var="temggather" >
		 	<li data-id="${temggather.id}" class="gradLi 
		 	<c:if test="${temggather.rank == 1}">firstGrade</c:if>
		 	<c:if test="${temggather.rank == 2}">secondGrade</c:if>
		 	<c:if test="${temggather.rank == 3}">lastGrade</c:if>
		 	<c:if test="${temggather.pitchon == 1}">active</c:if>
		 ">
					<div class="top">
							<div class="tem_Title">
								<span>模板名称：</span>
								<span>${temggather.name}</span>
							</div>
							<div class="select">
								<div class="mui-icon mui-icon-checkmarkempty select_icon"></div>
							</div>
					</div>
					<div class="bottom" >
						<div class="btn">
							<a href="/merchant/appointTemdata?code=${code}&amp;tempid=${temggather.id}">预览</a>
						</div>
						<div class="btn edit tempMoreToDevice" data-id="${tempparent.id}">
							模板复用
						</div>
						<div class="btn edit">
							<a href="/merchant/appointTeminfo?code=${code}&amp;tempid=${temggather.id}">编辑</a>
						</div>
					</div>
			</li>
		 </c:forEach>
		 <!-- 分等级模板结束 -->
		 </c:if>
		 <!-- 智慧款模板结束 -->
		</ul>
	</div>
	<style>
		.moreDevice {
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			bottom: 0;
			background-color: rgba(0,0,0,.5);
			color: #666;
			font-size: 12px;
			z-index: 15;
			display: none;
		}
		/* .moreDevice .mui-scroll-wrapper {
			position: relative;
		} */
		.moreDevice .more_con {
			position: absolute;
			left: 50%;
			top: 50%;
			transform: translate(-50%,-50%);
			-webkit-transform: translate(-50%,-50%);
			-moz-transform: translate(-50%,-50%);
			-o-transform: translate(-50%,-50%);
			-ms-transform: translate(-50%,-50%);
		}
		.moreDevice .more_con .title {
			text-align: center;
			font-size:16px;
			color: #333;
			padding: 0 0 10px;
    		font-weight: 700;
		}
		.moreDevice .more_info {
			max-height: calc(100vh - 200px);
			/* overflow: auto; */
			position: relative;
		}
		.moreDevice .more_con {
			width: 92%;
			background-color: #fff;
			border-radius: 5px;
			max-height: 90%;
			padding: 15px 10px;
		}
		.moreDevice .more_con table{
			width: 100%;
			table-layout: fixed;
		}
		.moreDevice .more_con table td {
			width: 25%;
			border: 0.5px solid #add9c0;
			line-height: 35px;
			text-align: center;
			color: #666;
			font-size: 12px;
			overflow: hidden;
		    text-overflow: ellipsis;
		    white-space: nowrap
		}
		
		.moreDevice .more_con table thead td {
			background-color: #C8EFD4;
			color: #333;
			font-weight: 600;
		}
		.moreDevice .more_con .bottom {
			margin-top: 15px;
			display: flex;
			justify-content: space-around;
		}
		.moreDevice .more_con .bottom button {
			padding: 3px 25px;
		}
		.che_con {
			display: flex;
			justify-content:center;
			align-items:center;
		}
		.che_con .che {
			width:26px;
			height: 26px;
			border-radius: 50%;
			border: 1px solid #ccc;
			display: inline-flex;
			justify-content:center;
			align-items:center;
			background-color: #fff;
		}
		.che_con .che >span {
			color: #fff; 
		}
		.che_con.active .che {
			background-color: #007aff;
			border-color: #007aff;
		}
		.che_con.pitchon .che {
			background-color: rgba(0,122,255,0.2);
			border-color: rgba(0,122,255,0.2);
		}
	</style>
	<!-- 复用模板弹框 -->
	<div class="moreDevice">
		<div class="more_con">
			<div class="title">复用此模板到其他设备</div>
			<div class="tab_title">
				<table>
					<thead>
						<tr>
							<td>设备号</td>
							<td>设备名</td>
							<td>所属小区</td>
							<td>
								<div class="che_con titleInp">
									<div class="che">
										<span class="mui-icon mui-icon-checkmarkempty"></span>
									</div>
								</div>
							</td>
						</tr>
					</thead>
				</table>
			</div>
			<div class="more_info mui-scroll-wrapper" >
				<div class="mui-scroll">
					<table>
						<tbody></<tbody>
					</table>
				</div>
			</div>
			<div class="bottom">
			  	<button type="button" class="mui-btn mui-btn-success closeBtn">取消</button>
			  	<button type="button" class="mui-btn mui-btn-success submitBtn">确认</button>
			</div>
		</div>
	</div>
	<!-- 复用模板弹框结束 -->
	
   </div>

	<!-- 添加主模板弹框结束 -->
	<nav class="mui-bar mui-bar-tab navBar">
		 <button type="button" class="mui-btn mui-btn-success" >
		 	<a
		 	<c:if test="${fmtDeviceType == '08'}">href="/merchant/timetemdata?code=${code}"</c:if>
		 	<c:if test="${fmtDeviceType != '08'}">href="/merchant/appointTeminfo?code=${code}"</c:if>
		 	>添加主模板</a>
		 </button>
	</nav>
<script>
$(function(){
	window.onpageshow = function(event) {
		if (event.persisted || window.performance &&
				window.performance.navigation.type == 2)
				{
				window.location.reload();
				}
		//window.location.reload();
	};

	var selecctTempid= 0 // 复用模板id
    var code= $('.app').attr('data-code').trim()
	$('.content').on('tap',function(ev){
		ev= ev || window.event
		var target= ev.target || ev.srcElement
		if($(target).hasClass('select_icon') && !$(target).parents('li').hasClass('active')){
				$(target).parents('li').siblings().removeClass('active')
				$(target).parents('li').addClass('active')
				var tempid= $(target).parents('li').attr('data-id').trim()
				$.ajax({
				 	url: '/wctemplate/templatechoice',
				 	data: {
				 		source: 1,
				 		obj: code,
				 		temid:tempid
				 	},
					success: function(res){
						if(res == 1){
							 mui.toast(code+'设备充电模板设置成功！',{ duration:'1500', type:'div' })
						}else{
							 mui.toast('选择充电模板失败！',{ duration:'1500', type:'div' })
						}
				 	},
				 	error: function(err){
				 		 mui.toast('选择充电模板失败，请稍后再试！',{ duration:'1500', type:'div' })
				 	}
				 })
				// $(target).prop('checked',false)
			  // $(target).prop('disabled',true)
		}
	})
	
	$('.tempMoreToDevice').on('tap',function(){ //点击模板复用
		selecctTempid= $(this).attr('data-id').trim()
		
		$.ajax({
			url:'/merchant/getDeviceData',
			type: 'post',
			dataType: 'json',
			data:{
				code:code,
				tempid: selecctTempid
			},
			success: function(res){
				if(res instanceof Array){
					var str= ''
					var picLength= 0 //返回的元素中已经选中模板啊的长度
					if(res.length <= 0){
						$('.tab_title input[type="checkbox"]').prop('disabled',true)
						str= '<tr><td colspan="4" style="text-align: center; color: #666;">暂无同类型设备</td></tr>'
					}else{
						$('.tab_title input[type="checkbox"]').prop('disabled',false)
						 for(var i=0 ; i<res.length ; i++){
							res[i].devicename= res[i].devicename ==null ? '— —' : res[i].devicename
							res[i].areaname= res[i].areaname ==null ? '— —' : res[i].areaname
							picLength= res[i].pitchon == 1 ? picLength+1 : picLength
							var activeStr= res[i].pitchon == 1 ? 'active pitchon' :'' //当设备已经使用此模板，则添加class类为active 和pitchon
							var trStr= '<tr><td>'+res[i].code+'</td><td>'+res[i].devicename+'</td><td>'+res[i].areaname+'</td><td><div class="che_con chInp '+activeStr+'" data-code="'+res[i].code+'"><div class="che"><span class="mui-icon mui-icon-checkmarkempty"></span></div></div></td></tr>'	
							str += trStr
						}
					}
					
					if(picLength == res.length){//返回的时候，元素已经全部选中，则直接勾选全选按钮
						$('.titleInp').addClass('active')
					}else{
						$('.titleInp').removeClass('active')
					}
					$('.moreDevice tbody').html(str)
					/* 获取mui-scroll的高度 */
					setTimeout(function(){
						var he= $('.more_info .mui-scroll').height()
						console.log(he)
						$('.more_info').css({
							height: he
						})
						mui('.mui-scroll-wrapper').scroll({
							deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
						});
					},150)
					
					$('.moreDevice').fadeIn()
					$('html').css('overflow','hidden')
					$('body').css('overflow','hidden')
					/* $('.titleInp').removeClass('active') */
					
				}else{
					mui.toast('数据获取失败',{ duration:'long', type:'div' })
				}
			},
			error: function(err){
				mui.toast('数据获取失败，请稍后重试！',{ duration:'long', type:'div' })
			}
		})
		
	})
	$('.moreDevice').on('tap',function(e){
		e= e || window.event
		var target= e.target || e.srcElement
				
		if($(target).hasClass('titleInp') || $(target).parents('.titleInp').length > 0){ //点击的是权限按钮
			var parentEle= $(target).hasClass('titleInp') ?  $(target).hasClass('titleInp') : $(target).parents('.titleInp')
			if(parentEle.hasClass('active')){ //选中
				parentEle.removeClass('active')
				$('.chInp').each(function(i,item){ //当取消全选按钮，过滤掉class包含pitchon的元素，其余的移除active
					if(!$(item).hasClass('pitchon')){
						$(item).removeClass('active')
					}
				})
				
			}else{
				parentEle.addClass('active')
				$('.chInp').addClass('active')
			}
			
		}else if($(target).hasClass('chInp') || $(target).parents('.chInp').length > 0){ //点击的是普通复选按钮
			var chiEle= $(target).hasClass('chInp') ?  $(target).hasClass('chInp') : $(target).parents('.chInp')
			if(chiEle.hasClass('active')){ //选中
				if(!chiEle.hasClass('pitchon')){
					chiEle.removeClass('active')
				}
			}else{
				chiEle.addClass('active')
			}
			/* 判断子元素带active的长度 是否等于子元素的长度 */		
			if($('.chInp.active').length == $('.chInp').length){
				$('.titleInp').addClass('active')
			}else{
				$('.titleInp').removeClass('active')
			}
		}

		if($(target).hasClass('moreDevice')){ 
			$('.moreDevice').fadeOut()
			
			$('html').css('overflow','auto')
			$('body').css('overflow','auto')
		}
		if($(target).hasClass('closeBtn')){
			$('.moreDevice').fadeOut()
			$('html').css('overflow','auto')
			$('body').css('overflow','auto')
		}
		if($(target).hasClass('submitBtn')){ //点击提交
			var list= []
			$('.moreDevice .more_info .che_con.active').each(function(i,item){
				list.push($(item).attr('data-code').trim())
			})
			/* 发送请求 */
			$.ajax({
				url: '/merchant/updateDeviceTemplate',
				type: 'post',
				dataType: 'json',
				data: {
					tempid: selecctTempid,
					deviceList: JSON.stringify(list)
				},
				success: function(res){
					if(res.code == 200){
						mui.toast('模板复用成功',{ duration:'long', type:'div' }) 
					}else{
						mui.toast('模板复用失败',{ duration:'long', type:'div' }) 
					}
					$('.moreDevice').fadeOut()
					$('html').css('overflow','auto')
					$('body').css('overflow','auto')
				},
				error: function(err){
					mui.toast('模板复用失败，请稍后重试！',{ duration:'long', type:'div' }) 
					$('.moreDevice').fadeOut()
					$('html').css('overflow','auto')
					$('body').css('overflow','auto')
				},
			})
		}
	})
	

})
</script>
</body>
</html>
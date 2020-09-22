<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>${area.name }</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="${hdpath }/hdfile/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.min.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.picker.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/mui.poppicker.css"/>
<link rel="stylesheet" type="text/css" href="${hdpath }/hdfile/css/areaBelongInfo.css"/>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath }/mui/js/mui.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.picker.js"></script>
<script type="text/javascript" src="${hdpath }/mui/js/mui.poppicker.js"></script>
<script type="text/javascript" src="${hdpath }/js/jquery.js"></script>
</head>
<body>
	<input id="aid" type="hidden" value="${area.id}">
	<input id="subMer" type="hidden" value="${meruser.subMer}">
	<header>
		<h3>${area.name}</h3>
	</header>
	<div class="areaBelongInfo1">
		<div class="container">
		<%-- <div class="deviceList2 childManage">
		<h4>子账号信息</h4>
			<table cellspacing="0">
						<thead>
							<tr>
								<td>昵称</td>
								<td>姓名</td>
								<td>电话</td>
								<td>管理</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listscanma}" var="item1">
								<tr>
									<td>${item1.nickname}</td>
									<td>
										<c:if test="${item1.realname == null || item1.realname == ''}">--</c:if>
										<c:if test="${item1.realname != null && item1.realname != ''}">${item1.realname}</c:if>
									</td>
									<td>${item1.phone}</td>
									<td>
										<button type="button" class="mui-btn mui-btn-danger deleteManagePerson" data-id="${item1.id}">删除</button>
									</td>
								</tr>
							</c:forEach>
							<c:if test="${fn:length(listscanma) <= 0}">
								<tr class="lastTr"> <td colspan="4"><i class="mui-icon mui-icon-plusempty"></i>添加子账号</td></tr>
							</c:if>
						</tbody>
				</table>
			</div> --%>
			
		<div class="deviceList2 parInfo">
		<h4>合伙人信息</h4>
			<table cellspacing="0">
				<thead>
					<tr>
						<td>昵称</td>
						<td>姓名</td>
						<td>电话</td>
						<td>分成比</td>
						<td>管理</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${listscanpa}" var="item2">
					<tr data-percent="${item2.percent*100}">
						<td>${item2.nickname}</td>
						<td>
						<c:if test="${item2.realname == null || item2.realname == ''}">--</c:if>
						<c:if test="${item2.realname != null && item2.realname != ''}">${item2.realname}</c:if>
						</td>
						<td>${item2.phone}</td>
						<td class="percentTd" data-id="${item2.id}">${item2.percent*100}%</td>
						<td><button type="button" class="mui-btn mui-btn-danger deleteManagePerson personBtn"  data-id="${item2.id}">删除</button></td>
					</tr>
					</c:forEach>
					<c:if test="${fn:length(listscanpa) <= 3}">
						<tr class="lastTr"> <td colspan="5"><i class="mui-icon mui-icon-plusempty"></i>添加合伙人</td></tr>
					</c:if>
				</tbody>
			</table>
			
			</div>
			<!-- 测试假数据 -->
			<div class="deviceList2">
				<h4>设备列表</h4>
				<ul>
					<li class="title">
						<span>设备号</span>
						<span>设备名称</span>
						<span>状态</span>
						<span>操作</span>
					</li>
					<c:forEach items="${equlist }" var="equ">
						<li >
							<span>${equ.code }</span>
							<span>
							<c:if test="${equ.remark == null || equ.remark == ''}">--</c:if>
							<c:if test="${equ.remark != null && equ.remark != ''}">${equ.remark}</c:if>
							</span>
							<c:if test="${equ.state==1}">
								<span class="onLine">在线</span>
							</c:if>
							<c:if test="${equ.state==0}">
								<span class="offLine">离线</span>
							</c:if>
							
							<span>
								<button type="button" class="mui-btn mui-btn-danger deleteSpan"  value="${equ.code }">删除</button>
							</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<!-- 编辑小区开始了 -->
		<div class="areaBelongInfo2">
				<form class="mui-input-group">
					<div class="mui-input-row">
						<label>小区名称</label>
						<input type="text" placeholder="请输入小区名称" name="name" id="name" value="${area.name }">
					</div>
					<div class="mui-input-row">
						<label>小区地址</label>
						<input type="text" class="mui-input-clear" placeholder="请输入小区地址" name="address" id="address" value="${area.address}" ><span class="mui-icon mui-icon-clear mui-hidden"></span>
					</div>
					<div class="mui-button-row formBottom">
						<button type="button" class="mui-btn mui-btn-primary" onclick="return false;">确认</button>&nbsp;&nbsp;
						<button type="button" class="mui-btn mui-btn-danger" onclick="return false;">取消</button>
					</div>
				</form>
		</div>
	<div class="alertDiv">
			<ul>
				<li>选择模板</li>
				<li class="sLi">
					<div>
						<span>钱包默认模板：</span>
						<span class="temSpan" id="wallTem"></span>
					</div>
					<div class="buttonA"><a href="/merchant/areaWalletTemp?id=${area.id }">钱包模板</a></div>
				</li>
				<li class="tLi">
					<div>
						<span>在线卡默认模板：</span>
						<span class="temSpan" id="onLineTem">在线卡模板一</span>
					</div>
					<div class="buttonA"><a href="/merchant/areaOnlineCardTemp?id=${area.id }">在线卡模板</a></div>
				</li>
				<li class="cancelBtn">取消</li>
			</ul>
		</div>
	<div class="areaBelongInfo3" >
		<c:forEach items="${codelist }" var="equ">
			<input type="hidden" value="${equ.code }" class="listCode"/>
	    </c:forEach>
		<form class="mui-input-group">
			<div class="mui-input-row selectDeviceNum">
				<label>设备编号</label>
				<input type="text" id="deviceInp" placeholder="请选择设备编号" value="" disabled>
			</div>
			<div class="mui-button-row formBottom">
				<button type="button" class="mui-btn mui-btn-primary" onclick="return false;">确认</button>&nbsp;&nbsp;
				<button type="button" class="mui-btn mui-btn-danger" onclick="return false;">取消</button>
			</div>
		</form>
		<div class="toast">选择设备后点击确认</div>
	</div>
	<nav class="mui-bar mui-bar-tab">
		<a href="javascript:void(0)" id="edit">编辑小区</a>
		<a href="javascript:void(0)" id="alertBtn">&nbsp;&nbsp;&nbsp;&nbsp;模板&nbsp;&nbsp;&nbsp;&nbsp;</a>
		<a href="javascript:void(0)" id="addBtn">添加设备</a>
		<a href="javascript:void(0)" id="deleteBtn">删除小区</a>
	</nav>
	
	<div class="addManAndPart">
		<ul>
			<li class="title">添加合伙人</li>
			<form>
				<div>
					<div class="">
						
						<div class="mui-input-row">
							<label>合伙人电话</label>
							<input type="text" class="mui-input-clear phone" placeholder="请输入电话" name="phone" value="" data-input-clear="6"><span class="mui-icon mui-icon-clear mui-hidden"></span>
						</div>
						
						<div class="mui-input-row">
							<label>合伙人昵称</label>
							<input type="text" class="mui-input-clear nick" placeholder="昵称" name="nick" disabled="disabled" value="" data-input-clear="7"><span class="mui-icon mui-icon-clear mui-hidden"></span>
						</div>
						
						<div class="mui-input-row">
							<label>合伙人姓名</label>
							<input type="text" class="mui-input-clear realNameInp" placeholder="姓名" name="realName" value="" disabled="" data-input-clear="8"><span class="mui-icon mui-icon-clear mui-hidden"></span>
						</div>
						
						<div class="mui-input-row rate">
							<label>分成比</label>
							<input type="text" class="mui-input-clear divideinto" placeholder="分成比（合伙人/总收益）" name="divideinto" value="" data-input-clear="9"><span class="mui-icon mui-icon-clear mui-hidden"></span>
							<span class="symbol">%</span>
						</div>
					</div>
					<p class="dangerTip"></p>
					<div class="addBtnContent">
						<button type="button" class="mui-btn mui-btn-success exitBtn">取消</button>
						<button type="button" class="mui-btn mui-btn-success sureBtn">确认</button>
					</div>
				</div>
			</form>
		</ul>
	</div>
	<div class="addInfo_con">
		<div class="addInfo">
			<h3>提示</h3>
			<div class="bottom">
				<p class="tipP">添加合伙人手机号需要在“自助充电平台”提前注册</p>
				<div class="reg_title">注册流程：</div>
				<p class="content"><span class="green_span">打开微信</span> <i class="mui-icon mui-icon-arrowthinright"></i> <span class="green_span">关注“自助公众平台”公众号<i class="mui-icon mui-icon-arrowthinright"></i>点击右下角的“商家登录”进入注册界面</span><i class="mui-icon mui-icon-arrowthinright"></i><span class="green_span">完成注册即可（注： 设备号可以输“888888”）</span></p>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			var isCanCommitAddInfo= false //是否可以提交添加的子账号/合伙人信息
			var from //判断来源，1是点击子账号，2是点击合伙人
			/* 点击添加管子账号或者合伙人 */
			$('.lastTr').on('click',function(e){
				from= $(this).parents('.childManage').length > 0 ? 1: 2 //判断来源，1是点击子账号，2是点击合伙人
				var subMer= $('#subMer').val().trim()
				var obj= {}
				if(from == 1){
					obj= {
							title: '添加子账号',
							labelPhoneName: '子账号电话',
							labelNickName: '子账号昵称',
							labelRealName: '子账号姓名',
							phoneEdit: false, //手机号disabled属性
							divideinto: false, //true为显示，false为不显示
					}
				}else{
					if(subMer == 1){
						return mui.alert('特约商户不能添加合伙人！','提示','我知道了')
					}
					obj={
						title: '添加合伙人',
						labelPhoneName: '合伙人电话',
						labelNickName: '合伙人昵称',
						labelRealName: '合伙人姓名',
						phoneEdit: false, //手机号disabled属性
						divideinto: true, //true为显示，false为不显示
					}
				}
				$('.addManAndPart>ul .title').text(obj.title)
				$('.addManAndPart>ul label').eq(0).text(obj.labelPhoneName)
				$('.addManAndPart>ul label').eq(1).text(obj.labelNickName)
				$('.addManAndPart>ul label').eq(2).text(obj.labelRealName)
				$('.addManAndPart>ul .phone').val('')
				$('.addManAndPart>ul .nick').val('')
				$('.addManAndPart>ul .realNameInp').val('')
				$('.addManAndPart>ul .divideinto').val('')
				$('.addManAndPart>ul .phone').prop('disabled',obj.phoneEdit)
				obj.divideinto ? $('.addManAndPart>ul .rate').show() : $('.addManAndPart>ul .rate').hide() 
				$('.dangerTip').text('')
				$('.addManAndPart').fadeIn() 
			})
			/* 点击修改分成比 */
			var clickedPercent= 0 //被点击的分成比
			var clickedId //被点击修改百分比的id
			$('.percentTd').click(function(){
				/* 获取被点击合伙人的信息 */
				var parNick= $(this).parent().find('td').eq(0).text().trim()
				var parRealName= $(this).parent().find('td').eq(1).text().trim() 
				parRealName= parRealName == '--' ?  '' : parRealName
				var parPhone= $(this).parent().find('td').eq(2).text().trim()
				var parPhoneDivideinto= parseFloat($(this).text())
				clickedPercent= parPhoneDivideinto
				clickedId= $(this).attr('data-id').trim()
				var alertOpations= {
						title: '编辑合伙人分成比',
						labelPhoneName: '合伙人电话',
						labelNickName: '合伙人昵称',
						labelRealName: '合伙人姓名',
						phoneEdit: true, //手机号的disabled属性
						divideinto: true, //true为显示，false为不显示	
					}
				$('.addManAndPart>ul .title').text(alertOpations.title)
				$('.addManAndPart>ul label').eq(0).text(alertOpations.labelPhoneName)
				$('.addManAndPart>ul label').eq(1).text(alertOpations.labelNickName)
				$('.addManAndPart>ul label').eq(2).text(alertOpations.labelRealName)
				$('.addManAndPart>ul .phone').val(parPhone)
				$('.addManAndPart>ul .nick').val(parNick)
				$('.addManAndPart>ul .realNameInp').val(parRealName)
				$('.addManAndPart>ul .divideinto').val(parPhoneDivideinto)
				$('.addManAndPart>ul .phone').prop('disabled',alertOpations.phoneEdit)
				alertOpations.divideinto ? $('.addManAndPart>ul .rate').show() : $('.addManAndPart>ul .rate').hide()
				$('.dangerTip').text('')
				$('.addManAndPart').fadeIn() 
			})
			
			/* 点击确定，提交添加的合伙人信息/子账号信息 */
			$('.addManAndPart .sureBtn').on('click',function(){
				if($('.addManAndPart .title').text().trim() == '编辑合伙人分成比'){
					var percentInp= $('.addManAndPart>ul .divideinto').val().trim()
					var percentInpCopy= isNaN(parseFloat(percentInp)) ? 0 : parseFloat(percentInp) //修改之后的分成比
					var totalPercent= 0 //所有分成比
					$('.percentTd').each(function(i,item){
						totalPercent+= parseFloat($(item).text())
					})
					var totalEndPercent= totalPercent - clickedPercent + percentInpCopy // 总分成-被点击的分成+修改之后的分成
					if(totalEndPercent >= 0 && totalEndPercent <= 100){
						/* 发送ajax进行修改 */
						$('.addManAndPart').fadeOut()
						$.ajax({
							url: '/merchant/editBindAreaPartner',
							type: 'post',
							data: {
								id: clickedId,
								percent: percentInpCopy
							},
							success: function(res){
								if(res.code == 200){
									location.reload()
								}else{
									mui.alert(res.message)
								}
							},
							error: function(err){
								mui.alert('修改失败，请稍后重试！')
							},
							complete: function(){
								
							}
						})
						
					}else {
						$('.addManAndPart .dangerTip').text('合伙人分成比之和必须在0-100之间')
					}
					return
				}
				if(isCanCommitAddInfo){ //可以点击提交
					var aid= $('#aid').val().trim()
					var phone= $('.addManAndPart>ul .phone').val().trim()
					var percent= $('.addManAndPart>ul .divideinto').val().trim()
					var percentCopy= percent
					if(from == 2){
						var allPercent= 0
						$('.parInfo tbody tr').each(function(i,item){
							var itemPercent= typeof $(item).attr('data-percent') == 'undefined' ? 0 : parseFloat($(item).attr('data-percent'))
							allPercent +=itemPercent
						})
						percentCopy= isNaN(parseFloat(percent)) ? 0 : parseFloat(percent)
						allPercent+= percentCopy
						if(allPercent < 0 || allPercent > 100){
							mui.alert('百分比之和必须在0-100之间')
							return
						}
					}
					$.ajax({
						url: '/merchant/bindAreaPartner',
						type: 'post',
						data: {
							aid: aid,
							type: from,
							phone: phone,
							percent: percent
						},
						dataType: 'json',
						success: function(res){
							console.log(res)
							if(res.code == 200){
								location.reload();
							}else{
								if(res.message !== '' || res.message !== null){
									mui.alert(res.message)
								}else{
									mui.alert('添加失败')	
								}
							}
						},
						error: function(err){
							res.alert('添加失败，请稍后重试！')
						}
					})
					$('.addManAndPart').fadeOut()
				}else{ //不能提交
					mui.alert('请输入正确的手机号',function(options){
						
					})
				}
			})
			
			$('.addManAndPart .exitBtn').on('click',function(e){
				$('.addManAndPart').fadeOut()
			})
			/* 删除子账号/合伙人 */
			$('.deleteManagePerson').on('click',function(){
				var tipInfoStr= $(this).hasClass('personBtn') ? '确认删除合伙人' : '确认删除子账号'
				mui.confirm(tipInfoStr,function(opations){
					if(opations.index === 1){
						var id= $(this).attr('data-id').trim()
						$.ajax({
							url: '/merchant/removeAreaPartner',
							type: 'post',
							data: {id: id},
							success: function(res){
								if(res.code === 200){
									location.reload()
								}else{
									mui.confirm('删除失败')
								}
							},
							error: function(err){
								mui.confirm('删除失败')
							}
						})
					}
				}.bind(this))
			})
			
			function handleTem(){
				//发送请求，获取选择的模板
				$.ajax({
					url: '/merchant/queryAreaTemp',
					data: {id:$("#aid").val()},
					type: 'POST',
					success: function(e){
						e= e || window.event
						var walletTempName= e.walletTempName
						var onlineTempName= e.onlineTempName
						if(!walletTempName){
							walletTempName= '未选择'
						}
						if(!onlineTempName){
							onlineTempName= '未选择'
						}
						$('#wallTem').text(walletTempName)
						$('#onLineTem').text(onlineTempName)
					}
				})
				//$('.alert').fadeIn()	
			}
			
			function openOrClose(flag){ //控制弹框显示和隐藏
				if(flag){ //当flag为真时，显示弹框，否则关闭
					$('.alertDiv').fadeIn()
					$('.alertDiv ul').slideUp(0)
					$('.alertDiv ul').slideDown()
				}else{
					$('.alertDiv ul').slideUp()
					$('.alertDiv').fadeOut()
				}
			}
			//============
			// 点击编辑小区
				var phonem;
				var blockArea= 1  //显示页面 1是默认页 2是编辑页 3是添加设备页
				var title= $('header h3').text().trim() //获取小区标题
				$('#edit').click(function(){
					phonem = $("#phone").val();
					blockArea= 2
					$('.areaBelongInfo1').slideUp()
					$('.areaBelongInfo3').slideUp()
					$('.areaBelongInfo2').slideDown()
					changeTitle()
				})
				// 点击模板
				$('#alertBtn').click(function(){
					openOrClose(true)
					handleTem()
				})
				$('.alertDiv').click(function(){
					openOrClose()
				})
				$('.alertDiv ul').click(function(e){
					e= e || window.event
					e.stopPropagation()
				})
				$('.cancelBtn').click(function(){ //点击取消
					openOrClose()
				})
				// 点击添加设备
				 // 模拟数据 data是传过来的数据
				 var inputCode= $('.listCode')
				 var data= []
				 inputCode.each(function(i,item){
					 data.push({text: $(item).val().trim()}) 
				 })
					var picker = new mui.PopPicker();
						 picker.setData(data);
				$('#addBtn').click(function(){
					blockArea= 3
					changeTitle()
					
					
					$('.selectDeviceNum').click(function(){ //点击选择设备编号
						 picker.show(function (selectItems) {
						    console.log(selectItems[0].text);//智子
						    $('#deviceInp').val(selectItems[0].text)
						  })

					})
					// =============//
					$('.areaBelongInfo1').slideUp()
					$('.areaBelongInfo2').slideUp()
					$('.areaBelongInfo3').slideDown()
				})

				// 点击删除设备
					$('.deleteSpan').click(function(){
						var codeval= $(this).val().trim()
						mui.confirm('确认删除此设备','提示',function(e){
							if(e.index ==1 ){
								// 获取设备编号
									$.ajax({
							        type : "POST",
									url : "/merchant/delAreaCode",
									data : {
										code : codeval
									},
								    success:function(e){
								    	if (e == 1) {
								    		mui.alert("删除成功");
								    		location.reload();
								    	} else if (e == 0) {
											mui.alert("删除失败，请重新提交");
										}
								    },
								    error:function(){//异常处理；
								    	 mui.alert('系统错误!');
								    }
								})
							}
						})
					})

				// 点击编辑小区提交
				$('.areaBelongInfo2 .mui-btn-primary').click(function(){
					var aid= $("#aid").val()
					/* 获取小区名称及地址 */
					var name= $('#name').val().trim()
					var address= $('#address').val().trim()				
						$.ajax({
					        type : "POST",
							url : "/merchant/editAreaInfo",
							data : {
								aid: aid,
								name: name,
								address: address
								},
						    success:function(res){
						    	if(res.code == 200){
						    		location.reload();
						    	}else{
						    		mui.alert('修改失败，请重试!');
						    	}
						    	
						    },
						    error:function(){
						    	mui.alert('修改失败，请重试!');
						    }
						})
				})
				$('.areaBelongInfo2 .mui-btn-danger').click(function(){
					// 清空input所有值
					//	$('.areaBelongInfo2 input').val('')
					blockIndexPage()
				})

				// 点击添加设备提交
				$('.areaBelongInfo3 .mui-btn-primary').click(function(){
					// 这里判断有没有输入内容
					// 添加之后成功与否提示框，告诉客户添加成功或失败
					// 这里发送完之后，清空input所有值
					
						$.ajax({
					        type : "POST",
							url : "/merchant/addEquArea",
							data : {
								code :  $('#deviceInp').val().trim(),
								aid : $("#aid").val(),
							},
						    success:function(e){
						    	if (e == 1) {
						    		mui.alert("添加成功","小区添加设备");
						    		location.reload();
						    	} else if (e == 2) {
						    		mui.alert("设备存在有误，可能设备已解除绑定，请查看是否有此设备！","小区添加设备");
								} else if (e == 0) {
									mui.alert("小区信息有误，请重新提交","小区添加设备");
								}
						    },
						    error:function(){
						    	 mui.alert('系统错误!');
						    }
						})
					
					blockIndexPage()
				})
				$('.areaBelongInfo3 .mui-btn-danger').click(function(){
					// 清空input所有值
					$('.areaBelongInfo3 input').val('')
					blockIndexPage()
				})

				$('#deleteBtn').click(function(){
					mui.confirm('确认删除此小区','提示',function(e){
						if(e.index === 1){
							// 发送请求删除小区
							$.ajax({
						        type : "POST",
								url : "/merchant/delArea",
								data : {
									aid : $("#aid").val()
								},
							    success:function(res){
							    	if (res.code == 200) {
							    		mui.alert("删除成功");
							    		location.replace("/merchant/areaManage");
							    	} else {
										mui.alert("删除失败，请重新提交");
									}
							    },
							    error:function(){
							    	 mui.alert('系统错误!');
							    }
							})
						}
					})
				})

				function blockIndexPage(){ //显示默认页
					blockArea=1
					$('.areaBelongInfo2').slideUp()
					$('.areaBelongInfo3').slideUp()
					$('.areaBelongInfo1').slideDown()
					changeTitle()
				}

				function changeTitle(){
					if(blockArea==1){
						$('header h3').text(title)
					}else if(blockArea == 2){
						$('header h3').text('编辑'+title)
					}else if(blockArea== 3){
						$('header h3').text('添加设备到'+title)
					}

				}

			//====================
			$(".phone").on('keyup',function(){
				var mobile = $(this).val().trim();
				var selfMerUser= ${meruser.phoneNum} //商户自己的手机号
				var parentEle= $(this).parent().parent()
				isCanCommitAddInfo= false 
				if(mobile.length == 11){
					if(selfMerUser == mobile){ //判断商户输入的手机号是不是自己的，如果是，则阻止商户输入
						$('.dangerTip').text('不能输入自己的手机号')
						isCanCommitAddInfo= false //输入自己手机号时不能提交
						return
					}
					$.ajax({
				        type : "POST",
						url : "${hdpath}/merchant/checkAccount", //处理页面的路径
						data : {
							mobile : mobile,
						},
					    success:function(e){ //服务器返回响应，根据响应结果，分析是否登录成功；
					    	if (e != null && e!="") {
					    		parentEle.find('.nick').val(e.username);
					    		parentEle.find('.realName').val(e.realname);
					    		parentEle.attr('data-id',e.id);
					    		$('.dangerTip').text('');
					    		isCanCommitAddInfo= true //手机号正常，可以提交
					    	} else {
					    		parentEle.find('.realName').val('');
					    		parentEle.find('.nick').val('');
					    		parentEle.attr('data-id','');
					    		$('.dangerTip').text('输入的手机号非商户');
					    		$('.addInfo_con').fadeIn();
					    		isCanCommitAddInfo= false //手机号非商户，不能提交;
					    	}
					    },
					    error:function(){//异常处理；
					    	 mui.toast('错误!');
					    	 isCanCommitAddInfo= false //请求失败，不能提交;
					    }
					})
				}else{
					/* 手机号不到11位 的时候，input置空，阻止往下执行 */
					parentEle.find('.realName').val('');
		    		parentEle.find('.nick').val('');
		    		parentEle.attr('data-id','')
		    		$('.dangerTip').text('')
				}
			})
			/* 添加合伙人的时候提示，合伙人操作信息 */
			/* var isFirst= true
			$('.addManAndPart .phone').click(function(){
				if(isFirst){ //第一次执行
					$('.addInfo_con').fadeIn()
					isFirst= false
				}
			}) */
			$('.addInfo_con').click(function(){
				$('.addInfo_con').fadeOut()	
			})
		})
	</script>
</body>
</html>
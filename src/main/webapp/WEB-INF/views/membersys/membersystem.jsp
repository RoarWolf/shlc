<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>会员管理</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/art-template.js"></script>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>

<style type="text/css">
* {
	padding: 0;
	margin: 0;
} 
body {
	background-color: #efeff4;
}
a {
	text-decoration: none;
	
}
.member {
	font-size: 12px;
	padding: 15px 10px;
	color: #333;
}

/* header开始 */
.member header {
	padding-bottom: 5px;
	/* border-bottom: 1px solid #aaa; */
	position: fixed;
	left: 15px;
	top: 10px;
	right:10px;
	background-color: #efeff4;
}
.member .tb {
	position: fixed;
	 top: 138px;
	 left: 10px;
	 right: 10px;
	 bottom: 0;
	 overflow: auto;
/* 	 padding-top: 36px; */
}

.member header span{
	display: inline-block;
	/* width: 17%; */
	font-size: 14px;
	min-width: 56px;
}
.member header input{
	/*float: left;*/
	height: 36px;
	width: 60%;
	max-width: calc(80% - 65px);
	margin-bottom: 0;
	background-color: #f7f8f9;
	border: 1px solid #BFBFBF;
	color: #666;
	font-size: 12px;

}
.member header input::-webkit-input-placeholder{
	font-size: 12px;
	}    /* 娴ｈ法鏁ebkit閸愬懏鐗抽惃鍕セ鐟欏牆娅� */
.member header input:-moz-placeholder{
	font-size: 12px;
	}                  /* Firefox閻楀牊婀�4-18 */
.member header input::-moz-placeholder{
	font-size: 12px;
	}                  /* Firefox閻楀牊婀�19+ */
.member header input:-ms-input-placeholder{
	font-size: 12px;
	}           /* IE濞村繗顫嶉崳锟� */

.member header button{
	float: right;
	width: 20%;
	margin-top: 1px;
}

.member .area_con {
		margin-top: 5px;
		display: flex;
		align-items: center;
	}
	.member .area_con .area_info {
		height: 36px;
		display: flex;
		justify-content: space-around;
	}
	.member .area_con .area_info>div {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-content: center;
	}
	.member .area_con .area_info>div>span {
		/* display: flex;
		flex-direction: column;
		align-content: center; */
		color: #666;
		font-size: 12px;
		text-align: center;
		overflow: hidden;
    	white-space: nowrap;
	}
	.member .area_con> span , .icPage .ic_title{
	    /* width: 17%; */
	    width: 56px;
	    font-size: 14px;
	    display: inline-flex;
	    justify-content: space-between;
	}
	.member  span i{
		font-style: normal;
	}
	.member .area_con .area_info {
		width: calc(60% - 58px);
		display: flex;
		justify-content: space-around;
	}
	.member .area_con select {
		width: 40%;
		max-width: calc(100% - 65px);
		margin: 0;
		height: 36px;
		background-color: #f7f8f9;
    	border: 1px solid #BFBFBF !important;
    	border-radius: 6px;
    	padding: 8px 15px;
    	font-size: 12px;
    	color: #666;
    	margin-left: 3px;
    	text-align-last: center
	}

/* header结束 */



/* .member .top {
	display: flex;
	padding-bottom: 20px;
	border-bottom: 1px solid #ccc;

}
.member .top div:nth-child(1){
	flex: 3;
	display: flex;
	align-items: center;
	justify-content: center;
}
.member .top div:nth-child(2){
	flex: 8;
}
.member .top div:nth-child(3){
	flex: 2;
	display: flex;
	align-items: center;
	justify-content: center;
}
.member .top input {
	width: 90%;
	height: 34px;
	border: 1px solid #ccc;
	border-radius: 6px;
	display: block;
	margin: 0 auto;
	background-color: #f7f8f9;
	outline: none;
	text-align: center;
}
.member select {
	width: 100%;
	height: 30px;
	border: none;
	outline: none;
	background-color: transparent;
	border: 1px solid #D9D9D9 !important;
	border-radius: 5px;
	font-size: 12px;
	margin-bottom: 0;
	appearance: normal !important;
	-webkit-appearance: normal !important;
	padding: 0;
}

.member .top div:nth-child(3) span {
	display: block;
	width: 100%;
	height: 90%;
	background-color: #4cd964;
	color: #fff;
	line-height: 30px;
	text-align: center;
	border-radius: 3px;
}


.member .area_con {
	margin-top: 5px;
	display: flex;
	align-items: center;
}
.member .area_con .area_info {
	height: 36px;
	display: flex;
	justify-content: space-around;
}
.member .area_con .area_info>div {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-content: center;
}
.member .area_con .area_info>div>span {
	display: flex;
	flex-direction: column;
	align-content: center;
	color: #666;
	font-size: 12px;
	text-align: center;
	width: 17%;
    min-width: 56px;
}
.member .area_con> span , .icPage .ic_title{
    width: 56px;
    font-size: 14px;
    display: inline-flex;
    justify-content: space-between;
}
.member  span i{
	font-style: normal;
}
.member .area_con .area_info {

}
.member .area_con select {
	width: 50%;
	max-width: calc(100% - 175px);
	margin: 0;
	height: 36px;
	background-color: #f7f8f9;
   	border: 1px solid #BFBFBF !important;
   	border-radius: 6px;
   	padding: 8px 15px;
   	font-size: 12px;
   	color: #666;
   	margin-left: 3px;
   	text-align-last: center
}
 */
.member table {
	table-layout:fixed;
	width: 100%;
	border-left: 1px solid #add9c0;
	border-top: 1px solid #add9c0;
/* 	overflow: hidden; */
	border-collapse: inherit;
}
.member table td {
	text-align: center;
	line-height: 35px;
	border-bottom: 1px solid #add9c0;
	border-right: 1px solid #add9c0;
}
.member table thead{
	background-color: #C8EFD4;
	color: #333;
	font-weight: 600;
}
.member table thead td {
	line-height: 1.1;
	padding: 12px 0;
}
.member table tbody {
	background-color: #f5f7fa;
}
.member table tr td:nth-child(1) {
	width: 64px;
}
.member table tr td:nth-child(2){
	width: 60px;
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
}
.member table tr td .moneyHeight {
	line-height: 1.2em;
}

.member table tr td:nth-child(4){
	width: 80px;
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
}
.member table tr td:nth-child(5){
	width: 55px;
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
}
.member table tr td:last-child button{
	padding: 3px 12px;
	vertical-align: middle;
}

.QRCodeAlert {
  position: fixed;
  width: 270px;
  position: absolute;
  left: 50%;
  top: 50%;
  animation: showAni .3s;
  transform-origin:center center;
  transform: translate(-50%,-50%);
  -ms-transform: translate(-50%,-50%);
  -moz-transform: translate(-50%,-50%);
  -webkit-transform: translate(-50%,-50%);
  -o-transform: translate(-50%,-50%);
  z-index: 99;
  display: none;
}
.QRCodeAlert .top {
  position: relative;
    padding: 15px;
    border-radius: 13px 13px 0 0;
    background: rgba(255,255,255,.95);
}
.QRCodeAlert .top::after {
    position: absolute;
    top: auto;
    right: auto;
    bottom: 0;
    left: 0;
    z-index: 15;
    display: block;
    width: 100%;
    height: 1px;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleY(.5);
    transform: scaleY(.5);
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
}
.QRCodeAlert .top h3 {
  font-size: 18px;
  font-weight: 500;
  text-align: center;
  color: #000;
}
.QRCodeAlert .top p{
    margin: 5px 0 0;
    font-family: inherit;
    font-size: 14px;
    text-align: center;
    color: #666;
}
.QRCodeAlert .top input {
    width: 100%;
    height: 30px;
    padding: 0 35px 0 5px;
    margin: 0;
    font-size: 14px;
    background: #fff;
    border: 1px solid rgba(0,0,0,.3);
    border-radius: 0;
}

.QRCodeAlert  .inputDiv>div,
.QRCodeAlert  .inputDiv>div>label {
    display: flex;
    justify-content:center;
    font-size: 14px;
    color: #333;
    font-weight: 400;
    }
.QRCodeAlert .bottom {
  position: relative;
    display: -webkit-box;
    display: -webkit-flex;
    display: flex;
    height: 44px;
    -webkit-box-pack: center;
    -webkit-justify-content: center;
    justify-content: center;
}
.QRCodeAlert .bottom span {
  position: relative;
    display: block;
    width: 100%;
    height: 44px;
    box-sizing: border-box;
    padding: 0 5px;
    overflow: hidden;
    font-size: 17px;
    line-height: 44px;
    color: #007aff;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    background: rgba(255,255,255,.95);
    -webkit-box-flex: 1;
}
.QRCodeAlert .bottom span:first-child {
   border-radius: 0 0 0 13px;
}
.QRCodeAlert .bottom span:first-child::after {
  position: absolute;
    top: 0;
    right: 0;
    bottom: auto;
    left: auto;
    z-index: 15;
    display: block;
    width: 1px;
    height: 100%;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleX(.5);
    transform: scaleX(.5);
    -webkit-transform-origin: 100% 50%;
    transform-origin: 100% 50%;
    }
    .QRCodeAlert .bottom span:last-child{
        border-radius: 0 0 13px;
        font-weight: 600;
    }
    .QRCodeAlert  .inputDiv {
      position: relative;
      margin: 15px 0 0;
    }
    .QRCodeAlert h6 {
      font-size: 15px;
      color: #333;
      margin-bottom: 8px;
      text-align: center;
    }
    .copymark {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      top: 0;
      background-color: rgba(0,0,0,.4);
      z-index: 98;
      display: none;
    }

.equGrad .top input {
    width: 100%;
    height: 30px;
    padding: 0 35px 0 5px;
    margin: 0;
    font-size: 14px;
    background: #fff;
    border: none;
    border-radius: 0;
    background-color: transparent;
}
.equGrad .top h3 {
  margin-bottom: 10px;
}
.equGrad .top form {
  padding-top: 15px;
  background-color: transparent;
}
.equGrad .top form:after {
  background-color: transparent;
}
.equGrad .top form:before {
    position: absolute;
    top: 0;
    right: -15px;
    left: -15px;
    z-index: 15;
    display: block;
    height: 1px;
    content: '';
    background-color: rgba(0,0,0,.2);
    -webkit-transform: scaleY(.5);
    transform: scaleY(.5);
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
}



 .equGrad .mui-checkbox.mui-left label {
  padding-left: 45px;
}
.equGrad .mui-checkbox.mui-left label>div:first-child {
  float: left;
}
.equGrad .mui-checkbox.mui-left label>div:last-child {
  padding-left: 10px;
  text-align: right;
  overflow: hidden;
  text-overflow:ellipsis;
  white-space: nowrap;
}
.equGrad .topFa label>div {
	text-align: center; 
	font-size: 14px;
}
.table_thead {
	position: fixed;
    left: 10px;
    top: 106px;
    right: 10px;
    width: calc(100% - 20px) !important;
    background-color: #add9c0 !important;
    z-index: 50;
}
 .table_body .tip_info {
 	background-color: #efeff4;
 	position: relative;
 }
 
 .table_body .tip_info td{
 	border: none;
 }
 .table_body .tip_info td>div {
 	transform: translateX(-1px);
 	background-color: #efeff4;
 	color: #999;
 }
 
.mer_info {
	padding-top: 5px;
	color: #666;
	overflow: hidden;
	display: flex;
	justify-content: space-between;
}
.mer_info .title {
    font-size: 14px;
	padding-right: 5px;
}
.mer_info ul {
	display: flex;
	justify-content: space-between;
}
.mer_info ul li {
	list-style: none;
	margin-left: 5px;
	margin-right: 5px;
}
.mer_info ul li>span {
	width: 16px;
	height: 16px;
	min-width: auto;
	display: inline-block;
	font-size: 14px;
	box-shadow: 0px 0px 5px #808080;
	border-radius: 3px;
	vertical-align: middle;
}
.top_tip {
	font-size:14px;
	color: #666;
}
			
			
.grad1 {
	color: #6BD089 !important;
}
.grad2 {
	color: #FFB27D !important;
}
.grad3 {
	color: #F47378 !important;
}

.grad1_bg {
	background-color: #6BD089 !important;
}
.grad2_bg {
	background-color: #FFB27D !important;
}
.grad3_bg {
	background-color: #F47378 !important;
}
</style>
</head>
<body>
<div class="member">
	<header class="clearfix">
			<div>
				<span class="ic_title">
					搜索条件
				</span>
				
				<input type="search" name="icCode" placeholder="请输入会员号/昵称/电话">
				<button type="button" class="mui-btn mui-btn-success" id="searchBut">查询</button>
			</div>
			<div class="area_con">
				<span>小区名称</span>
				<select>
						<option data-id="" value="">所有</option>
						<!-- <option data-id="-1" value="-1" data-name="未选择">未绑定小区 </option> -->
						<c:forEach items="${areaData}" var="item">
							<option 
							data-id="${item.id}" 
							value="${item.id}" 
							data-name="${item.name}" 
							>${item.name}</option>
		                </c:forEach>
	                
				</select>
				<div class="area_info">
					<div>
						<span>会员数 / 充值  / 赠送</span>
						<span>
						<i class="card_num" data-num="${datasize}">${datasize}</i> /
						<i class="card_money" data-money="${datamoney}"><fmt:formatNumber value="${datamoney}" pattern="0.00"/></i> /
						<i class="card_sendmoney" data-sendmoney="${datasendmoney}"><fmt:formatNumber value="${datasendmoney}" pattern="0.00"/></i>
						</span>
					</div>
					<%-- <div>
						<span>金额</span>
						<span class="total_card_money" data-money="${datamoney}">${datamoney}元</span>
					</div> --%>
				</div>
			</div>
		<div class="mer_info clearfix">
			<div class="title">会员等级</div>
			<ul>
				<li>等级1 <span class="grad1_bg"></span></li>
				<li>等级2 <span class="grad2_bg"></span></li>
				<li>等级3 <span class="grad3_bg"></span></li>
			</ul>
		</div>
		<div class="top_tip"><strong>注：</strong>点击会员名称可修改会员等级</div>
		</header>
	<div class="tb">
<!-- 		<table cellspacing="0" class="table_thead">
				<thead>
					<tr>
						<td>会员号</td>
						<td>昵称</td>
						<td>金额</td>
						<td>电话</td>
						<td>小区名称</td>
						<td>等级</td>
					</tr>
				</thead>
		</table> -->
		<table cellspacing="0" class="table_body">
			 <thead>
				<tr>
					<td>会员号</td>
					<td>昵称</td>
					<td>充值/赠送</td>
					<td>电话</td>
					<td>小区名称</td>
				</tr>
			<tbody id="content"> 
				<c:forEach items="${memberdata}" var="member">
					<tr>
						<td>
						  <a href="/merchant/mercVirtualPayMoney?type=1&id=${member.id}">
							<font><fmt:formatNumber value="${member.id}" pattern="00000000"/></font>
						  </a>
						</td>
						<td  data-grad="${member.role}" data-num="<fmt:formatNumber value="${member.id}" pattern="00000000"/>" class="user <c:choose><c:when test="${member.role == 5}">grad1</c:when><c:when test="${member.role == 6}">grad2</c:when><c:when test="${member.role == 7}">grad3</c:when></c:choose>">${member.username !=null ? member.username : "--"}</td>
						<td><div class='moneyHeight'><a href="/general/touristmoneychange?uid=${member.id}"><fmt:formatNumber value="${member.balance}" pattern="0.00"/> / <fmt:formatNumber value="${member.sendmoney}" pattern="0.00"/></a></div></td>
						<td><font>${member.phone_num !=null ? member.phone_num : "--"}</font></td>
						<td><font>${member.arename !=null ? member.arename : "--"}</font></td>
						<%-- <td><button type="button" class="mui-btn mui-btn-outlined" data-grad="${member.role}">${member.role-4}级</button></td> --%>
					</tr>
				</c:forEach>
				<tr class="tip_info">
					<td colspan="5" >
						<div>
							<c:if test="${fn:length(memberdata) < 35 }">
								暂无更多数据
							</c:if>
							<c:if test="${fn:length(memberdata) >= 35 }">
								正在加载更多
							</c:if>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="copymark"></div>
<div class="QRCodeAlert equGrad">
    <div class="popur">
     <div class="topFa">
      <div class="top">
        <h3>设置00000001会员等级</h3>
        <form class="mui-input-group"></form>
          
          <div class="mui-input-row mui-radio mui-left">
            <label><div>1级</div></label>
             <input name="radio1" type="radio" value="5">
          </div>
          <div class="mui-input-row mui-radio mui-left">
            <label><div>2级</div></label>
            <input name="radio1" type="radio" value="6">
          </div>
          <div class="mui-input-row mui-radio mui-left">
            <label><div>3级</div></label>
            <input name="radio1" type="radio" value="7">
          </div>
        
      </div>
     </div>
      <div class="bottom">
        <span class="closeBtn">取消</span>
        <span class="sureBtn">确认</span>
      </div>
    </div>
  </div>
<!-- <table cellspacing="0" class="table_thead">
				<thead>
					<tr>
						<td>会员号</td>
						<td>昵称</td>
						<td>金额</td>
						<td>电话</td>
						<td>小区名称</td>

					</tr>
				</thead>
		</table>  -->
<script id="data-tpl" type="text/html">

<table cellspacing="0" class="table_body">
<thead>
					<tr>
						<td>会员号</td>
						<td>昵称</td>
						<td>充值/赠送</td>
						<td>电话</td>
						<td>小区名称</td>
					</tr>
				</thead>
<tbody id="content">
{{ each result }}
<tr>
	<td>
		<a href="/merchant/mercVirtualPayMoney?type=1&id={{$value.id}}">
			<font>{{ ('00000000'+$value.id).substr(-8) }}</font>
		</a>
	</td>
	<td data-grad="{{ $value.role }}" data-num="{{ ('00000000'+$value.id).substr(-8) }}" class="user {{ $value.role ==5 ? 'grad1' : $value.role ==6 ? 'grad2' : $value.role ==7 ? 'grad3' : ''}}">{{ $value.username !=null ? $value.username : "--" }}</td>
	<td>
		<div class='moneyHeight'>
		<a href="/general/touristmoneychange?uid={{$value.id}}">
			{{ $value.balance.toFixed(2) }} / {{ $value.sendmoney.toFixed(2) }}
		</a>
		</div>
	</td>
	<td><font>{{ $value.phone_num !=null ? $value.phone_num : "--" }}</font></td>
	<td><font>{{ $value.arename !=null ? $value.arename : "--" }}</font></td>
</tr>
{{ /each }}
<tr class="tip_info">
	<td colspan="5" >
		<div>
			{{ result.length < 25 ? '暂无更多数据' : '正在加载更多' }}
		</div>
	</td>
</tr>
</tbody>
</table>
</script>

<script id="data-tpl2" type="text/html">
{{ each result }}
<tr>
	<td>
		<a href="/merchant/mercVirtualPayMoney?type=1&id={{$value.id}}">
			<font>{{ ('00000000'+$value.id).substr(-8) }}</font>
		</a>
	</td>
	<td data-grad="{{ $value.role }}" data-num="{{ ('00000000'+$value.id).substr(-8) }}" class="user {{ $value.role ==5 ? 'grad1' : $value.role ==6 ? 'grad2' : $value.role ==7 ? 'grad3' : ''}}">{{ $value.username !=null ? $value.username : "--" }}</td>
	<td>
		<div class='moneyHeight'>
		<a href="/general/touristmoneychange?uid={{$value.id}}">
			{{ $value.balance.toFixed(2) }} / {{ $value.sendmoney.toFixed(2) }}
		</a>
	</div>
	</td>
	<td><font>{{ $value.phone_num !=null ? $value.phone_num : "--" }}</font></td>
	<td><font>{{ $value.arename !=null ? $value.arename : "--" }}</font></td>
</tr>
{{ /each }}
</script>

<script>
$(function(){
	$buttonEle= null
	
	$('.tb').on('click','.user',function(){
		var merNum= $(this).attr('data-num').trim()
		var grad= $(this).attr('data-grad').trim()
		$buttonEle= $(this)
		$('.equGrad h3').text('设置'+merNum+'会员等级')
		$('.equGrad input[type="radio"]').eq(grad-5).prop('checked',true)
		$('.copymark').fadeIn(100)
	     $('.equGrad').fadeIn()
	})
	
/* 	$('table td button').click(function(){
		 var merNum= $(this).parent().parent(.find('td').eq(0).find('a font').text()
		 $buttonEle= $(this)
		$('.equGrad h3').text('设置'+merNum+'会员等级')
		var grad= parseInt($(this).attr('data-grad'))//获取会员等级
		$('.equGrad input[type="radio"]').eq(grad-5).prop('checked',true)
		$('.copymark').fadeIn(100)
	      $('.equGrad').fadeIn()
	      
	}) */
	$('.equGrad .closeBtn').click(function(){
		$('.copymark').fadeOut(100)
	    $('.equGrad').fadeOut()
	})
	$('.equGrad .sureBtn').click(function(){
		var grad= $('.equGrad input[type="radio"]:checked').val().trim() //获取选择的等级会员
		var merNum=  $buttonEle.attr('data-num').trim()
		$.ajax({
			url: '/merchant/memberrole',
			data: {member:merNum,role:grad},
			type: 'post',
			success: function(res){
				var gradStr= '1级'
				switch(parseInt(grad)){
				case 5 : gradStr= '1级' ; break;
				case 6 : gradStr= '2级' ; break;
				case 7 : gradStr= '3级' ; break;
				}
				$buttonEle.attr('data-grad',grad)
				$buttonEle.removeClass('grad1')
				$buttonEle.removeClass('grad2')
				$buttonEle.removeClass('grad3')
				$buttonEle.addClass('grad'+(grad-4))
				 mui.toast('修改成功',{ duration:'1500', type:'div' })
			},
			error: function(err){
				console.log(err)
				 mui.toast('修改失败，请稍后重试！',{ duration:'1500', type:'div' })
			}
		})
		$('.copymark').fadeOut(100)
	    $('.equGrad').fadeOut()
	})
})
</script>

<script type="text/javascript">
var flag= false
var currentPage= 1 //页码
var limit= 35 //每次取的条数
var totalPages //总页数
	window.onpageshow = function(event) {
		if (event.persisted || window.performance &&
				window.performance.navigation.type == 2)
				{
				location.reload();
				}
		//window.location.reload();
	};
	/* 通过会员号/昵称/小区搜索 */
	$('.area_con select').change(function(){
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		sendAjaxAndRender(true)
	})
	$('#searchBut').click(function(){
		 flag= true
		 totalPages= undefined
		 currentPage= 1
		sendAjaxAndRender(true)
	})
	function sendAjaxAndRender(from){
		//var from= typeof currentPage == 'undefined' ? true : false //from是判断哪里调用这个函数的，按条件搜索为true ,上拉加载未false
		currentPage= currentPage || 1
		limit= limit || 35
		var keywords= $('input[name="icCode"]').val().trim()
		var areaId= $('.area_con select').val().trim()
		var type= 1 // 1是会员号   2是会员名称  3是手机号
		if(/^\d{9,11}$/.test(keywords)){
			type= 3
		}else if(/^\d+$/.test(keywords)){
			type= 1
		}else{
			type= 2
		}
		
		var data= {type: type,keywords: keywords,areaId: areaId,currentPage: currentPage,limit: limit}
		$.ajax({
			url: '/merchant/ajaxMemberCentre',
			type: 'post',
			data:data,
			success: function(res){
			  if(res.code == 200){
				 totalPages= res.totalPages
				 if(from){
					 $('.tb')[0].scrollTop= 0
					 $('.tb').html('')
					 var html = template('data-tpl', {result: res.datalist});
					/*  document.getElementById('content').innerHTML = html; */
					$('.tb').html(html)
					 /* 每次搜索之后都置空 */
				 }else{
					 var fragment= document.createDocumentFragment()
					 for(var i= 0; i< res.datalist.length; i++){
						 var html2= template('data-tpl2',{result: [res.datalist[i]]}) 
						 $(fragment).append($(html2))
					 }
					 var text= currentPage <= totalPages ? '正在加载更多' : '暂无更多数据'
					 $(fragment).append($('<tr class="tip_info"><td colspan="5"><div>'+text+'</div></td></tr>'))
					 $('.tip_info').remove()
					 $('.table_body tbody').append($(fragment))
				 }
				 var datasize= res.datasize || 0
				 var tatolmoney= res.datamoney || 0
				 var sendmoney= res.datasendmoney || 0
				 $('.card_num').attr('data-num',datasize).text(datasize)
				 $('.card_money').attr('data-money',tatolmoney).text(tatolmoney.toFixed(2))
				 $('.card_sendmoney').attr('data-sendmoney',sendmoney).text(sendmoney.toFixed(2))
			  }else {
				  mui.toast(res.message)
			  }
			},
			error: function(err){
				mui.toast('请求出错！')
			},
			complete: function(){
				flag= false
			}
		})
	}
	
	/* 上啦加载 */
	$('.member .tb').scroll(function(e){
		e= e || window.event
		var target= e.target || e.currentTarget
		var tbHeight= $(this).height()
		var tbodyHeight= $('.table_body').height()
		if((tbodyHeight - target.scrollTop)*0.9 <= tbHeight && !flag){
			flag= true
			//请求数据
			currentPage++ //页码加一
			if(typeof totalPages == 'undefined' || currentPage <= totalPages){
				sendAjaxAndRender()
			}else{
				$('.tip_info div').text('暂无更多数据')
			}
		}
		
	})
	
</script>
</body>
</html>
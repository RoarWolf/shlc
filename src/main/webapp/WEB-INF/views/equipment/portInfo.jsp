<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath}/css/base.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/mui/js/mui.min.js"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script> 
<title>端口设置</title>
  <style>
    .appFa {
      width: 100%;
      height: 100%;
      padding: 59px 10px 15px;
      overflow: auto;
    }
    .app {
      border-radius: 5px;
      overflow: hidden;
      border: 1px solid #add9c0;
      box-sizing: border-box; 
      font-size: 14px;  
      color: #666;
    }

     .app table {
        width: 100%;    
        border-radius: 8px;
      }
      .app table td {
        text-align: center;
        line-height: 35px;
        border-bottom: 1px solid #add9c0;
        border-right: 1px solid #add9c0;
        padding:0 5px;
        font-size: 14px;
      }
      .app table tr td:last-child{
      	 padding:5px 5px;
      }
      .app table tr td:first-child {
        width: 20%;
      }
      .app table tbody tr td:first-child {
        font-size: 18px;
      }
      .app table tr td:nth-child(2) {
         max-width: 30%;
         word-break: break-all;
         padding-left: 0;
         padding-right: 0;
      }
      .app table tr td:nth-child(3) {
          padding-left: 0;
          padding-right: 0;
      }
      .app table tr td:nth-child(2) p{
        height: 35px;
        overflow:hidden;
        text-overflow:ellipsis;
        padding-left: 5px;
        padding-right: 5px;
        border-bottom: 1px solid #add9c0;
      }
      .app table tr td:nth-child(3) p {
      	padding-left: 5px;
        padding-right: 5px;
        height: 35px;
        border-bottom: 1px solid #add9c0;
      }
      .app table tr td p{
      	color: #000;
      }
      .app table tr td p:last-child{
      	border: none;
      }
      .app table tr td:last-child {
        border-right: none;
        width: 25%;
      }
      .app table tr td:last-child .mui-btn-success{
      	margin-bottom: 5px;
      }
      .app table tbody tr:last-child td{
        border-bottom: none;
      }
      .app table thead{
        background-color: #C8EFD4;
        color: #333;
        font-weight: 600;
      }
      .app table tbody {
         background-color: #f5f7fa;
      }
      .app table tbody button {
        font-size: 14px;
        padding: 4px 10px;
        vertical-align: middle;
      }
      /*弹框，遮挡层*/

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

  .deleteMer .top input {
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
.deleteMer .top h3 {
  margin-bottom: 10px;
}
.deleteMer .top form {
  padding-top: 15px;
  background-color: transparent;
}
.deleteMer .top form:after {
  background-color: transparent;
}
.deleteMer .top form:before {
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



 .deleteMer .mui-checkbox.mui-left label {
  padding-left: 45px;
}
.deleteMer .mui-checkbox.mui-left label>div:first-child {
  float: left;
}
.deleteMer .mui-checkbox.mui-left label>div:last-child {
  padding-left: 10px;
  text-align: right;
  overflow: hidden;
  text-overflow:ellipsis;
  white-space: nowrap;
}
 .deleteMer .mui-checkbox.mui-left input[type=checkbox] {
  left: 0;
}

  </style>
</head>
<body>
<div class="appFa">
<header class="mui-bar mui-bar-nav">
      <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
      <h1 class="mui-title" data-equip="${code}">设备  ${code} 端口绑定</h1>
  </header>
  <div class="app">
    <div class="tb">
            <table cellspacing="0">
              <thead>
                <tr>
                  <td>端口号</td>
                  <td>昵称</td>
                  <td>会员号</td>
                  <td>操作</td>
                </tr>
              </thead>
                <tbody>
                  <tr>
                      <td> 1 </td>
                     
                          <td>
                          	<c:if test="${fn:length(portInfo1) == 0}">
	                          	— —
	                        </c:if>
	                        <c:if test="${fn:length(portInfo1)!= 0}">
		                        <c:forEach items="${portInfo1}" var="portInfo">
		                          <p>${portInfo.username}</p>
		                        </c:forEach>
	                         </c:if>
                          </td>
  				           <td>
  				            <c:if test="${fn:length(portInfo1) == 0}">
	                           	— —
	                        </c:if>
	                         <c:if test="${fn:length(portInfo1)!= 0}">
                          	<c:forEach items="${portInfo1}" var="portInfo">
	                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
	                        </c:forEach>
	                        </c:if>
                          </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>2</td>
                      <td>
                         <c:if test="${fn:length(portInfo2) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo2)!= 0}">
	                        <c:forEach items="${portInfo2}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo2) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo2)!= 0}">
                         	<c:forEach items="${portInfo2}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>3</td>
                       <td>
                         <c:if test="${fn:length(portInfo3) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo3)!= 0}">
	                        <c:forEach items="${portInfo3}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo3) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo3)!= 0}">
                         	<c:forEach items="${portInfo3}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>

                    <tr>
                      <td>4</td>
                      <td>
                         <c:if test="${fn:length(portInfo4) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo4)!= 0}">
	                        <c:forEach items="${portInfo4}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo4) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo4)!= 0}">
                         	<c:forEach items="${portInfo4}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>

                     <tr>
                      <td>5</td>
                       <td>
                         <c:if test="${fn:length(portInfo5) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo5)!= 0}">
	                        <c:forEach items="${portInfo5}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo5) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo5)!= 0}">
                         	<c:forEach items="${portInfo5}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>

                     <tr>
                      <td>6</td>
                       <td>
                         <c:if test="${fn:length(portInfo6) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo6)!= 0}">
	                        <c:forEach items="${portInfo6}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo6) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo6)!= 0}">
                         	<c:forEach items="${portInfo6}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>7</td>
                       <td>
                         <c:if test="${fn:length(portInfo7) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo7)!= 0}">
	                        <c:forEach items="${portInfo7}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo7) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo7)!= 0}">
                         	<c:forEach items="${portInfo7}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>8</td>
                       <td>
                         <c:if test="${fn:length(portInfo8) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo8)!= 0}">
	                        <c:forEach items="${portInfo8}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo8) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo8)!= 0}">
                         	<c:forEach items="${portInfo8}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>9</td>
                       <td>
                         <c:if test="${fn:length(portInfo9) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo9)!= 0}">
	                        <c:forEach items="${portInfo9}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo9) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo9)!= 0}">
                         	<c:forEach items="${portInfo9}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                    
                    <tr>
                      <td>10</td>
                       <td>
                         <c:if test="${fn:length(portInfo10) == 0}">
                         	— —
                        </c:if>
                        <c:if test="${fn:length(portInfo10)!= 0}">
	                        <c:forEach items="${portInfo10}" var="portInfo">
	                          <p>${portInfo.username}</p>
	                        </c:forEach>
                         </c:if>
                         </td>
 				           <td>
 				            <c:if test="${fn:length(portInfo10) == 0}">
                         	 — —	
                        </c:if>
                         <c:if test="${fn:length(portInfo10)!= 0}">
                         	<c:forEach items="${portInfo10}" var="portInfo">
                          <p data-id="${portInfo.id}"><fmt:formatNumber value="${portInfo.uid}" pattern="00000000"/></p>
                        </c:forEach>
                        </c:if>
                      </td>
                      <td>
                        <button class="mui-btn mui-btn-success">添加</button>
                        <button class="mui-btn mui-btn-danger">删除</button>
                      </td>
                    </tr>
                </tbody> 
            </thead>
       </div>
    </div>
  <!-- 遮挡层 弹框-->
    <div class="copymark"></div>
    <div class="QRCodeAlert addMer">
    <div class="popur">
      <div class="top">
        <h3>端口1绑定会员</h3>
        <!-- <p>是否修改该IC卡：4206BF49</p> -->
        <div class="inputDiv">
          <h6>请输入会员号</h6>
          <input type="text" name="inpName" placeholder="会员号">
        </div>
        
      </div>
      <div class="bottom">
        <span class="closeBtn">取消</span>
        <span class="sureBtn">确认</span>
      </div>
    </div>
  </div>

    <div class="QRCodeAlert deleteMer">
    <div class="popur">
     <div class="topFa">
      <div class="top">
        <h3>端口1解绑会员</h3>
        <form class="mui-input-group">
          <div class="mui-input-row mui-checkbox mui-left">
            <label><div>012345</div><div>永夜</div></label>
            <input name="checkbox" value="Item 1" type="checkbox">
          </div>
          <div class="mui-input-row mui-checkbox mui-left">
            <label><div>012345</div><div>呃呃呃呃呃呃</div></label>
            <input name="checkbox" value="Item 2" type="checkbox" >
          </div>
          <div class="mui-input-row mui-checkbox mui-left">
            <label><div>012345</div><div>sadasdqwe</div></label>
            <input name="checkbox" type="checkbox">
          </div>
        </form>
      </div>
     </div>
      <div class="bottom">
        <span class="closeBtn">取消</span>
        <span class="sureBtn">确认</span>
      </div>
    </div>
  </div>
</div>  
<script> 
$(function(){
	$targetDelete= null
	$targetAdd= null
  $('.tb table .mui-btn-success').click(function(){
      var PLen= $(this).parent().parent().find('td').eq(2).find('p').length
      $targetAdd= $(this)
      if(PLen>=10){
          mui.toast('此端口绑定人数不能超过10人')
          return
      }
      var portNum= $(this).parent().parent().find('td').eq(0).text().trim() //端口号
	  $('.addMer .top h3').text('端口'+portNum+'绑定会员')
      $('.copymark').fadeIn(100)
      $('.addMer').fadeIn()

  })
   $('.tb table .mui-btn-danger').click(function(){
	   var PLen= $(this).parent().parent().find('td').eq(2).find('p').length
	   $targetDelete= $(this)
	      if(PLen <=0){
	          mui.toast('此端口暂未绑定会员')
	          return
	      }
	  var pName= $(this).parent().parent().find('td').eq(1).find('p')
	  var PNum= $(this).parent().parent().find('td').eq(2).find('p')
	    var portNum= $(this).parent().parent().find('td').eq(0).text().trim() //端口号
	    var str1='<h3>端口'+portNum+'解绑会员</h3>'
	    var divStr= ''
	  PNum.each(function(i,item){
		  divStr+= '<div class="mui-input-row mui-checkbox mui-left"><label><div>'+$(item).text().trim()+'</div><div>'+$(pName[i]).text().trim()+'</div></label><input name="checkbox" value="'+$(item).attr('data-id').trim()+'" type="checkbox"</div>'
	  })
	  var str= str1+'<form class="mui-input-group"></form>'+divStr
	  var $divEle= $('<div class="top"></div>')
	  $divEle.append($(str))
	  console.log($divEle)
	  $('.deleteMer .topFa').html($divEle)
      $('.copymark').fadeIn()
       $('.deleteMer').fadeIn()
  })
  $('.copymark').click(function(){
      $(this).fadeOut()
      $('.addMer').fadeOut()
      $('.deleteMer').fadeOut()
  })
  $('.addMer .sureBtn').click(function(){ //添加确定
    $('.copymark').fadeOut()
    $('.addMer').fadeOut()
    //获取设备号，端口号，会员号
    var equip= $('header h1').attr('data-equip').trim()
    var port= $targetAdd.parent().parent().find('td').eq(0).text().trim() //端口号
    var member= $('.addMer .inputDiv input').val().trim()
    $.ajax({
      url: '/equipment/addspecifiedport',
      type: 'POST',
      data: {equip:equip,port:port,member:member},
      success: function(res){
    	  console.log(res)
    	  if(res.code == 401){
    		  mui.toast('您输入的会员不存在！',{ duration:1500, type:'div' })
    	  }else if(res.code == 403){
    		  mui.toast('添加失败，请稍后重试！',{ duration:1500, type:'div' })
    	  }else if(res.code == 407){
    		  mui.toast('您输入的会员号不在您的名下！',{ duration:1500, type:'div' })
    	  }else if(res.code == 408){
    		  mui.toast('408！',{ duration:1500, type:'div' })
    	  }else if(res.code == 200){
    		  var uidStr= '00000000'+res.data.uid
    		  uidStr= uidStr.substr(-8)
    		 if($targetAdd.parent().parent().find('td').eq(2).find('p').length <=0){
    			 $targetAdd.parent().parent().find('td').eq(1).text('')
    			 $targetAdd.parent().parent().find('td').eq(2).text('')
    		 }
    		 var nume= res.data.username || ''
    		 $targetAdd.parent().parent().find('td').eq(1).append($('<p>'+nume+'</p>'))
    		 $targetAdd.parent().parent().find('td').eq(2).append($('<p data-id='+res.data.id+'>'+uidStr+'</p>'))
    		 mui.toast('添加成功！',{ duration:1500, type:'div' })
    	  }
      },
      error:function(err){
    	  console.log(err)
      }
    })
    //获取，发送ajax请求
  })
  $('.addMer .closeBtn').click(function(){ //添加取消
    $('.copymark').fadeOut()
    $('.addMer').fadeOut()
  })
  $('.deleteMer .closeBtn').click(function(){ //选择取消
    $('.copymark').fadeOut()
    $('.deleteMer').fadeOut()
  })
  $('.deleteMer .sureBtn').click(function(){ //删除确定
	  //获取用户id 集合
	  var memberList= []
	  var inputList= $('.deleteMer input[name="checkbox"]:checked')
	  inputList.each(function(i,item){
		  memberList.push(parseInt($(item).val().trim()))
	  })
	  if(memberList.length<=0){
		  mui.toast('请选择删除的会员',{ duration:1500, type:'div' })
		  return
	  }
  		console.log("memberList===" + memberList);
  		console.log("memberListJSON===" + JSON.stringify(memberList));
  	  $.ajax({
  		  url: '/equipment/deletespecifiedport',
  		  data: {
  			memberList: JSON.stringify(memberList)
  		  },
  		  type:	"POST",
  		  dataType: 'json',
  		  success: function(res){
  			  console.log(res)
  			  if(res.code == 403){
  				mui.toast('删除失败请重试',{ duration:1500, type:'div' })
  				  return 
  			  }
  			  if(res.code== 200){
  				var $name= $targetDelete.parent().parent().find('td').eq(1).find('p')
  				var $num= $targetDelete.parent().parent().find('td').eq(2).find('p')
  				$num.each(function(i,item){
  					if(memberList.indexOf(parseInt($(item).attr('data-id')))!= -1){
  						$(this).remove()
  						$name.eq(i).remove()
  					}
  				})
  				if($targetDelete.parent().parent().find('td').eq(2).find('p').length <=0){
  					$targetDelete.parent().parent().find('td').eq(1).text('— —')
  					$targetDelete.parent().parent().find('td').eq(2).text('— —')
  				}
  				mui.toast('删除成功',{ duration:1500, type:'div' })
  				
  			  }
  		  },
  		  error: function(err){
  			  console.log(err)
  		  }
  	  })
    $('.copymark').fadeOut()
    $('.deleteMer').fadeOut()
  })

})
</script>
</body>
</html>
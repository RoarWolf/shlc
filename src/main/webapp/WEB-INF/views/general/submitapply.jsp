<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="hdpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>申请进度查询</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!--标准mui.css-->
<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/icons-extra.css" />
<!--App自定义的css-->
<link rel="stylesheet" type="text/css" href="${hdpath }/mui/css/app.css" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
<script type="text/javascript" src="${hdpath}/mui/js/mui.js"></script> 
<style>
html,body,.mui-scroll-wrapper{width:100%; height: 100%;}
.markright{ float: right; margin-right: 8%;}
#submit{width: 80%; margin-left: 10%; line-height: 2;}
.content{height: 80%; overflow:auto;}
/*toast信息提示*/
.mui-toast-container {bottom: 50% !important;}
/* .mui-toast-message {background: url(/app/themes/default/images/toast.png) no-repeat center 10px #000; opacity: 0.6; color: #fff; width: 180px; padding: 70px 5px 10px 5px;}
 */
</style>
</head>
<body>
<div id="pullrefresh" class="mui-scroll-wrapper">
  <div> <span style="line-height: 2; font-size: 18px; margin-left: 5%;">申请进度查询</span> </div>
  <div class="mui-scroll">
    <div  class="content">
	 <ul class="mui-table-view">
		 <c:forEach items="${submitApply}" var="submit">
		    <li class="mui-table-view-cell">
		        <a href="javascript:void(0);" >
				   <span class="markleft">卡&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：&nbsp;${submit.cardID}</span>
				   <span class="markright"> 申请状态：
					  <c:choose>
						<c:when test="${submit.status == 3}"><font color="green">解挂</font></c:when>
						<c:when test="${submit.status == 6}"><font color="green">金额转移</font></c:when>
					  </c:choose>
				   </span><br>
				  <span class="markleft">处理状态：
					  <c:choose>
						<c:when test="${submit.type == 0}"><font color="green">已处理</font></c:when>
						<c:when test="${submit.type == 1}"><font color="green">被拒绝</font></c:when>
						<c:when test="${submit.type == 2}"><font color="green">待处理</font></c:when>
					  </c:choose>
				   </span><br>
				   <span class="markleft">时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：&nbsp;<fmt:formatDate value="${submit.recordTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
				
				</a>
		    </li>
		 </c:forEach>
	 </ul>
	 </div>
  <!-- <a href="javascript:window.history.back(-1)">返回上一页</a> -->
	 <a href="/general/operation?uid=${user.id}"><button type="button" id="submit" style="margin-top: 1rem;" class="mui-btn mui-btn-success">返回</button></a>
  
  </div>
</div>
</body>
</html>
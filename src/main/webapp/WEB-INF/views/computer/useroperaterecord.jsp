<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>用户操作</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<link rel="stylesheet" href="${hdpath}/css/jquery-ui.css">
<script type="text/javascript" src="${hdpath}/js/jquery-ui.js"></script>
<style type="text/css">
.dialog-htm{ padding: 18px;}
.dialog .dialog-htm label{float: left; width: 15%; margin:0;line-height: 2; font-size: 16px;}
.dialog .dialog-htm input{line-height: 2; width: 75%;}
.dialog .dialog-htm select{padding: 8px 0; width: 75%;}
</style>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
	<%@ include file="/WEB-INF/views/navigation.jsp"%>
</div>
<div class="leftnav" id="lefeMenu">
	<%@ include file="/WEB-INF/views/menu.jsp"%>
</div>
<div>
	<ul class="bread">
	  <li><a href="javascript:void(0)" target="right" class="icon-home">操作记录</a></li>
	</ul>
</div>
<div class="admin">
   <div class="panel admin-panel" id="adminPanel">
     <div class="conditionsd">
	   <form method="post" action="${hdpath}/pcoperate/useroperatelog" id="listform">
		   <div class="searchdiv">
		      <ul class="search" style="padding-left:10px;">
				<li>操作人&nbsp;&nbsp; 
					<input type="text" placeholder="输入操作人" name="dealer" class="frame2"  value="${dealer}"/>
				</li>
				<li>操作对象&nbsp;&nbsp; 
					<input type="text" placeholder="输入操作对象" name="openick" class="frame2"  value="${openick}"/>
				</li>
				<li>操作内容&nbsp;&nbsp; 
					<input type="text" placeholder="输入操作内容" name="realname" class="frame2"  value="${realname}"/>
				</li>
	     		<li>时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
	     							onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
					    &nbsp;&nbsp; —
						&nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
	     							onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
				</li>
				<li class="cmbquery"><input type="submit" style="width: 80px;" value="搜索">
				</li>
			</ul>
		</div>
	</form>
   </div>
 <div class="table table-div">
  <table class="table table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>操作时间</th>
			<th>操作内容</th>
			<th>操作人</th>
			<th>操作对象</th>
			<!-- <th>类型</th> -->
			<th>备注</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${operatetrcon}" var="order" varStatus="as">
			<tr>
				<td>${as.count+current}</td>
				<td><fmt:formatDate value="${order.operate_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${order.name}</td>
				<td>${order.openick}</td>
				<td>${order.objnick}</td>
				<%-- <td>${order.type == 1 ? "用户记录" : "其它"}</td> --%>
				<td>${order.remark != null ? order.remark : "— —"}</td>
			</tr>
		</c:forEach>
		<c:if test="${operatetrcon==null}"><tr><td colspan="7">暂无信息</td></tr></c:if>
	</tbody>
  </table>
	<div align="center">
	<%-- 构建分页导航 --%>
		共有${pageBean.totalRows}条数据，共${pageBean.totalPages}页，当前为${pageBean.currentPage}页
		<br/>
		 <span class="btn btn-success" onclick="currentPage(0)">首页</span>
		 <c:if test="${pageBean.currentPage >1}">
		 	<span class="btn btn-success" onclick="currentPage(2)">上一页</span>
		 </c:if>
		 
		 <c:forEach begin="${pageBean.start}" end="${pageBean.end}" step="1" var="i">
	              	<span onclick="currentPagenum(${i})">${i} </span>
		 </c:forEach>
		 
		 <c:if test="${pageBean.currentPage < pageBean.totalPages}">
		 	<span class="btn btn-success" onclick="currentPage(3)">下一页</span>
		 </c:if>
		<span class="btn btn-success" onclick="currentPage(1)">尾页</span>
		<form style="display: inline;">
			<input type="text" name="pageNumber" style="width: 50px"> 
			<input class="btn btn-info" type="button" onclick="currentPage(4)" value="go">
		</form>
	</div>
	 <!-- ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ -->
 </div>
</div>
</div>
</body>
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(23); })
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcoperate/useroperatelog?"+arguments; 
}
function  currentPage(mark){
	var arguments = $("#listform").serialize();
	if(mark==0){//首页
		arguments += "&currentPage=1"; 
	}else if(mark==1){//末页
		arguments += "&currentPage="+${pageBean.totalPages}; 
	}else if(mark==2){//上一页
		arguments += "&currentPage="+${pageBean.currentPage-1};
	}else if(mark==3){//下一页
		arguments += "&currentPage="+${pageBean.currentPage+1}; 
	}else if(mark==4){//go跳转页
		var currentPage = $("input[name='pageNumber']").val();
		if(currentPage==null ||currentPage==""){
			alert("请输入页码。");
			return;
		}
		arguments += "&currentPage="+currentPage; 
	}
	window.location.href="${pageContext.request.contextPath}/pcoperate/useroperatelog?"+arguments; 
}


</script>
</html>
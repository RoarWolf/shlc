<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备操作日志</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<style type="text/css">
html,body,table{width:100%;}
td {
	text-align: center;
}
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备操作日志</a></li>
	</ul>
</div>
<div class="admin">
<div class="conditionsd">
  <form method="post" action="${hdpath}/pcequipment/selectcodeoperatelog" id="searchform">
    <div class="searchdiv">
      <ul class="search" style="padding-left:10px;">
      		<li>
     		设备号&nbsp;&nbsp;
     		<input type="text" placeholder="输入设备号" name="code" class="frame1" value="${code}"/>
     		</li>
     		<li>
     		商户名&nbsp;&nbsp;
     		<input type="text" placeholder="输入商户名" name="realname" class="frame4" value="${realname}"/>
     		</li>
       		<li>
     		商户电话&nbsp;&nbsp;
     		<input type="text" placeholder="输入商户电话" name="phone" class="frame4" value="${phone}"/>
     		</li>
       		<li>
     		操作人&nbsp;&nbsp;
     		<input type="text" placeholder="输入操作人" name="username" class="frame4" value="${username}"/>
     		</li>
     		<li>类型&nbsp;&nbsp;
     		<select name="sort">
				<option value="0">请选择</option>
				<option value="1" <c:if test="${sort == 1}"> selected="selected"</c:if> >上线</option>
				<option value="2" <c:if test="${sort == 2}"> selected="selected"</c:if> >下线</option>
				<option value="3" <c:if test="${sort == 3}"> selected="selected"</c:if> >绑定</option>
				<option value="4" <c:if test="${sort == 4}"> selected="selected"</c:if> >解绑</option>
				<option value="5" <c:if test="${sort == 5}"> selected="selected"</c:if> >版本号修改</option>
				<option value="6" <c:if test="${sort == 6}"> selected="selected"</c:if> >小区修改</option>
			</select>
     		</li>
     		<%-- <li>类型&nbsp;&nbsp;
     		<select name="sort">
				<!-- <option value="0">请选择</option> -->
				<option value="1" <c:if test="${sort == 1}"> selected="selected"</c:if> >上下线</option>
				<option value="2" <c:if test="${sort == 2}"> selected="selected"</c:if> >绑定与解绑</option>
				<option value="3" <c:if test="${sort == 3}"> selected="selected"</c:if> >设备版本号修改</option>
				<option value="4" <c:if test="${sort == 4}"> selected="selected"</c:if> >小区修改</option>
			</select>
     		</li>
     		<li>状态&nbsp;&nbsp;
     		<select name="type">
				<option value="0">请选择</option>
				<option value="1" <c:if test="${type == 1}"> selected="selected"</c:if> >空闲</option>
				<option value="2" <c:if test="${type == 2}"> selected="selected"</c:if> >使用</option>
				<option value="3" <c:if test="${type == 3}"> selected="selected"</c:if> >禁用</option>
				<option value="4" <c:if test="${type == 4}"> selected="selected"</c:if> >故障</option>
			</select>
     		</li> --%>
     		<li>起始时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
     							onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
				    &nbsp;&nbsp; —
					&nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
     							onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
			</li>
       		<li class="cmbquery">
            	<input type="submit" style="width: 80px;" value="搜索">
      	 	</li>
        </ul>
     </div>
   </form>
</div>
<div class="table table-div">
	<table class="table table-hover" >
	    <thead>
			<tr>
			   <th>记录时间</th>
			   <th>设备号</th>
			   <th>归属商户</th>
			   <th>商户电话</th>
			   <th>操作人</th>
			   <th>状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${operateLog}" var="record" varStatus="as">
				<tr>
					<td><fmt:formatDate value="${record.operate_time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${record.code}</td>
					<td>${record.dealer}</td>
					<td>${record.dealerphone}</td>
					<td>${record.opername}</td>
					<td>
						<c:choose>
							<c:when test="${record.sort == 1}">${record.type == 1 ? "上线" : record.type == 2 ? "下线" : "--"}</c:when>
							<c:when test="${record.sort == 2}">${record.type == 1 ? "绑定" : record.type == 2 ? "解绑" : "--"}</c:when>
							<c:when test="${record.sort == 3}">${record.type == 1 ? "修改版本号" : record.type == 2 ? "修改收费模板" : "--"}</c:when>
							<c:when test="${record.sort == 4}">${record.type == 1 ? "绑定小区" : record.type == 2 ? "解绑小区" : "--"}</c:when>
							<c:otherwise>— —</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
   <!-- 页码内容显示 -->
   <div align="center">
	<%@ include file="/WEB-INF/views/public/pagearea.jsp"%>
   </div>
  </div>
 </div>
</body>
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(43); })
var pathurl = window.location.pathname;
function currentPagenum(mark){//点击页码指定页
	var arguments = $("#searchform").serialize()+"&currentPage="+mark;
	window.location.href=pathurl+"?"+arguments; 
}
function  currentPage(mark){
	var arguments = $("#searchform").serialize();
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
	window.location.href=pathurl+"?"+arguments; 
}
</script>
</html>
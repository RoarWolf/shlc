<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>IC卡查询</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<link rel="stylesheet" href="${hdpath}/css/jquery-ui.css">
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>  
<script type="text/javascript" src="${hdpath}/js/jquery-ui.js"></script>
<style type="text/css">
.addcard{float: right; margin-right:92px;}
.dialog-htm{ padding: 18px;}
.dialog .dialog-htm label{float: left; width: 15%; margin:0;line-height: 2; font-size: 16px;}
.dialog .dialog-htm input{line-height: 2; width: 75%;}
.dialog .dialog-htm select{padding: 8px 0; width: 75%;}
</style>
<script type="text/javascript">
/* function relievebinding(Id){
	var statu = confirm("确认解除用户绑定？");
	if(!statu){
	  return false;
	}
	$.ajax({
		data:{Id:Id},
        type : "POST",
        url : "${hdpath}/pccardrecord/relievebinding",
        timeout : 3000, //超时时间设置，单位毫秒
        success : function(res) {
        	window.location.reload();
        }
    });
}
function deletecard(Id){
	var statu = confirm("确认注销？");
	if(!statu){
	  return false;
	}
	$.ajax({
		data:{Id:Id},
        type : "POST",
        url : "${hdpath}/pccardrecord/deletecardbyId",
        timeout : 3000, //超时时间设置，单位毫秒
        success : function(res) {
        	window.location.reload();
        }
    });
}
function removecard(Id){//物理删除
	var statu = confirm("确认删除？");
	if(!statu){
	  return false;
	}
	$.ajax({
		data:{Id:Id},
        type : "POST",
        url : "${hdpath}/pccardrecord/removecardbyId",
        timeout : 3000, //超时时间设置，单位毫秒
        success : function(res) {
        	window.location.reload();
        }
    });
}

//激活、挂失、解挂
function editcard( genre, Id){
	var statu = confirm("是否确认该操作？");
	if(!statu){
	  return false;
	}
	$.ajax({
		data:{genre:genre,Id:Id,},
        type : "POST",
        url : "${hdpath}/pccardrecord/updateonlinecard",
        timeout : 3000, //超时时间设置，单位毫秒
        success : function(res) {
        	window.location.reload();
        }
    });
}

//绑定、解绑
function onbinding( genre, Id){
	var statu = confirm("是否确认该操作？");
	if(!statu){
	  return false;
	}
	$.ajax({
		data:{genre:genre,Id:Id,},
        type : "POST",
        url : "${hdpath}/pccardrecord/onbinding",
        timeout : 3000, 
        success : function(res) {
        	window.location.reload();
        }
    });
}
  */
</script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">IC卡查询</a></li>
	</ul>
</div>
<div class="admin">
	<div class="panel admin-panel" id="adminPanel">
    	<div class="conditionsd">
		 <form method="post" action="${hdpath}/pccardrecord/selectonlinecard" id="listform">
		    <div class="searchdiv">
		      <ul class="search" style="padding-left:10px;">
				<li>十六进制卡号&nbsp;&nbsp; 
					<input type="text" placeholder="输入卡号" name="cardnumber" class="input"  value="${cardnumber}"/>
				</li>
				<li>原始卡号&nbsp;&nbsp; 
					<input type="text" placeholder="输入卡号" name="figure" class="input"  value="${figure}"/>
				</li>
				<li>用户&nbsp;&nbsp; 
					<input type="text" placeholder="输入用户名" name="nickname" class="input"  value="${nickname}"/>
				</li>
				<li>所属商户&nbsp;&nbsp; 
					<input type="text" placeholder="输入商户名" name="dealer" class="input"  value="${dealer}"/>
				</li>
				<li>小区名&nbsp;&nbsp; 
					<input type="text" placeholder="输入小区名" name="areaname" class="input"  value="${areaname}"/>
				</li>
				<li>状态&nbsp;&nbsp; 
					<select name="status">
						<option value="-1" <c:if test="${status == -1 }">selected="selected"</c:if>>请选择</option>
						<option value="0" <c:if test="${status == 0 }">selected="selected"</c:if>>未激活</option>
						<option value="1" <c:if test="${status == 1 }">selected="selected"</c:if>>正常</option>
						<option value="2" <c:if test="${status == 2 }">selected="selected"</c:if>>挂失</option>
					</select>
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
				<th>用户</th>
				<th>十六进制卡号</th>
				<th>原始卡号</th>
				<th>所属商户</th>
				<th>金额</th>
				<th>状态</th>
				<th>关联钱包</th>
				<th>归属小区</th>
				<th>备注</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${onlinecard}" var="order" varStatus="as">
				<c:if test="${order!=null}">
				<tr>
					<td >${as.count+current}</td>
					<td>${order.touristnick !=null ? order.touristnick : "— —"}</td>
					<td><a href="/pccardrecord/onlineoperate?cardID=${order.cardID}">${order.cardID}</a></td>
					<td>${order.figure}</td>
					<td>${order.dealernick !=null ? order.dealernick : "— —"}</td>
					<td>
					<c:choose>
						<c:when test="${order.relevawalt==1}"><a href="/pcadminiStrator/selecwalletdetail?uid=${order.uid}">${order.touristbalance}</a></c:when>
						<c:when test="${order.relevawalt==2}"><a href="/pccardrecord/selectonlineconsume?cardID=${order.cardID}">${order.money}</a></c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					
					
					</td>
					<td>${order.status == 0 ? "未激活" : order.status == 1 ? "<font color='#5cb85c'>正常</font>" : order.status == 2 ? "<font color='#ea2e2e'>挂失</font>" : order.status == 3 ? "注销" : "其它"}</td>
					<td>${order.relevawalt ==1 ? "是" : order.relevawalt ==2 ? "否" : "否"}</td>
					<td>${order.areaname !=null ? order.areaname : "— —"}</td>
					<td>${order.remark !=null ? order.remark : "— —"}</td>
					<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				</c:if>
				<c:if test="${order==null}"><tr><td colspan="9">暂无信息</td></tr></c:if>
			</c:forEach>
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
<script type="text/javascript">
$(document).ready(function(){	
	$('#51'+' a').addClass('at');
	$('#51').parent().parent().parent().prev().removeClass("collapsed");
	$('#51').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#51').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#51').parent().parent().parent().addClass("in");
	$('#51').parent().parent().parent().prev().attr("aria-expanded",true)
})
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectonlinecard?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectonlinecard?"+arguments; 
}
</script>
</html>
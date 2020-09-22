<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>包月记录</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">

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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">包月记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcorder/selectPackageMonth" id="listform">
			    <div class="searchdiv">
			     <ul class="search" style="padding-left:10px;">
 					<li>
		     		订单:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入订单" name="ordernum" class="frame6" value="${ordernum}"  />
		     		</li>
		            <li>
		     		昵称:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入昵称" name="nickname" class="frame2" value="${nickname}"  />
		     		</li>
		     		<li>时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
		     							onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
						    &nbsp;&nbsp; —
							&nbsp;&nbsp;<input type="text" name="endTime" id="endTime" placeholder="请选择时间" value="${endTime}"
		     							onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})" readonly="readonly">
					</li>
		       		<li>
		            	<input type="submit" style="width: 80px;" value="搜索">
		      	 	</li>
      			</ul>
			   </div>
			</form>
  		 </div>
  		 <hr>
		 <div class="table table-div">
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>订单号</th>
					<th>昵称</th>
					<th>金额</th>
					<th>来源</th>
					<th>状态</th>
					<th>每日次数</th>
					<th>变动次数</th>
					<th>剩余次数</th>
					<th>记录时间</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${order}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<td><a href="/pcorder/detailsRestsOrderinfo?paysource=6&ordernum=${order.ordernum}&status=${order.status}">${order.ordernum}</a></td>
						<%-- <td><a href="/pcorder/TraderecordDetails?paysource=6&ordernum=${order.ordernum}&status=${order.status}">${order.ordernum}</a></td> --%>
						<td>${order.nickname}</td>
						<td>${order.money}</td>
						<td>${order.paysource==1 ? "开通包月" : order.paysource==2 ? "包月充电" : "--"}</td>
						<td>${order.status==1 ? "正常" : order.status==2 ? "退回" : "--"}</td>
						<td>${order.everydaynum}</td>
						<td>${order.changenum}</td>
						<td>${order.surpnum}</td>
						<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td>
					      				    
					    </td>
					   </tr>
					</c:forEach>
				 </tbody>
			   </table>
			   <!-- 页码内容显示 -->
			   <div align="center">
				<%@ include file="/WEB-INF/views/public/pagearea.jsp"%>
			   </div>
			  <!-- ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ -->
			   </div>
   			</div>
</div>
</body>
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){menulocation(37);})
</script>
</html>
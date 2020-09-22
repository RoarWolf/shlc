<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>在线卡消费</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">在线卡消费</a></li>
	</ul>
</div>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
   <div class="conditionsd">
	 <form method="post" action="${hdpath}/pccardrecord/selectonlineconsume" id="listform">
	   <div class="searchdiv">
	      <ul class="search" style="padding-left:10px;">
			<li>订单号&nbsp;&nbsp; 
				<input type="text" placeholder="输入订单号" name="ordernum" class="frame7" value="${ordernum}"/>
			</li>
			<li>持卡人&nbsp;&nbsp; 
				<input type="text" placeholder="输入姓名" name="nickname" class="frame2" value="${nickname}"/>
			</li>
			<li>卡号&nbsp;&nbsp; 
				<input type="text" placeholder="输入卡号" name="cardID" class="frame2" value="${cardID}"/>
			</li>
			<li>商户名&nbsp;&nbsp; 
				<input type="text" placeholder="输入商户名" name="dealer" class="frame2" value="${dealer}"/>
			</li>
			<li>设备号&nbsp;&nbsp; 
				<input type="text" placeholder="输入设备号" name="code" class="frame2" value="${code}"/>
			</li>
			<li>类型&nbsp;&nbsp; 
				<select name="type" class="frame1">
					<option value="">请选择</option>
					<option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>消费</option>
					<option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>余额回收</option>
					<option value="3" <c:if test="${type == 3}">selected="selected"</c:if>>微信充值</option>
					<%-- <option value="4" <c:if test="${status == 4 }"> </c:if>>卡操作</option> --%>
					<option value="5" <c:if test="${type == 5}">selected="selected"</c:if>>退费</option>
					<option value="6" <c:if test="${type == 6}">selected="selected"</c:if>>支付宝充值</option>
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
				<th>单号</th>
				<th>持卡人</th>
				<th>卡号</th>
				<th>商户名</th>
				<!-- <th>操作人</th> -->
				<th>操作金额</th>
				<th>到账金额</th>
				<th>余额</th>
				<th>类型</th>
				<th>设备号</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cardconsume}" var="order" varStatus="as">
				<tr>
					<td>${as.count+current}</td>
					<td><a href="/pcorder/TraderecordDetails?paysource=5&ordernum=${order.ordernum}&orderid=${order.id}">${order.ordernum}</a></td>
					<td>${order.nickname != null ? order.nickname : "— —"}</td>
					<td>${order.cardID}</td>
					<td>${order.dealer != null ? order.dealer : "— —"}</td>
				<%-- 	<td>${order.operanick != null ? order.operanick : "— —"}</td> --%>
					<td>${order.money}</td>
					<td>
						<c:choose>
							<c:when test="${order.type == 1||order.type == 5||order.type == 7}"><font color='#ea0d3f'> -${order.accountmoney} </font></c:when>
							<c:when test="${order.type == 2||order.type == 3||order.type == 6}"><font color='#00CC99'> +${order.accountmoney} </font></c:when>
							<c:otherwise><font color='#ada98b'>  ${order.accountmoney} </font></c:otherwise>
						</c:choose>
					</td>
					<td>${order.balance}</td>
					<td>
						<c:choose>
							<c:when test="${order.type == 1||order.type == 5||order.type == 7}"><font color='#ea0d3f'>${order.type == 1 ? "消费" : order.type == 5 ? "微信退费": order.type == 7 ? "支付宝退费" : "— —"}</font></c:when>
							<c:when test="${order.type == 2||order.type == 3||order.type == 6}"><font color='#00CC99'>${order.type == 2 ? "余额回收" : order.type == 3 ? "微信充值": order.type == 6 ? "支付宝充值" : "— —"}</font></c:when>
							<c:otherwise><font color='#ada98b'>卡操作</font></c:otherwise>
						</c:choose>
					</td>
					<td>${order.code != null ? order.code : "— —"}</td>
					<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</c:forEach>
			<c:if test="${cardconsume==null}"><tr><td colspan="11">暂无信息</td></tr></c:if>
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
	$('#53'+' a').addClass('at');
	$('#53').parent().parent().parent().prev().removeClass("collapsed");
	$('#53').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#53').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#53').parent().parent().parent().addClass("in");
	$('#53').parent().parent().parent().prev().attr("aria-expanded",true)
})
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectonlineconsume?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pccardrecord/selectonlineconsume?"+arguments; 
}
</script>
</html>
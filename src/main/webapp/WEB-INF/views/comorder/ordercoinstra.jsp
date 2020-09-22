<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>投币记录</title>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">投币记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcorder/selectCoinsInfoByTr" id="searchform">
			    <div class="searchdiv">
			     <ul class="search" style="padding-left:10px;">
 					<li>
		     		订单:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入订单" name="ordernum" class="frame7" value="${ordernum}"  />
		     		</li>
		            <li>
		     		用户昵称:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入用户昵称" name="username" class="frame2" value="${username}"  />
		     		</li>
		       		<li>
		     		商户名:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入商户名" name="dealer" class="frame2" value="${dealer}"  />
		     		</li>
		       		<li>
		     		商户电话:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入商户电话" name="mobile" class="frame3" value="${mobile}"  />
		     		</li>
		       		<li>
		     		卡号:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入卡号" name="cardnum" class="frame2" value="${cardnum}"  />
		     		</li>
		       		<li>
		     		设备号:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入设备号" name="code" class="frame2" value="${code}"  />
		     		</li>
		     		<li>起始时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
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
		 <div class="table table-div">
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>订单号</th>
					<th>用户名</th>
					<th>商户名</th>
					<th>商户电话</th>
					<th>设备号</th>
					<th>卡号</th>
					<th>设备类型</th>
					<th>交易金额</th>
					<th>交易时间</th>
					<th>详情</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${order}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<td>
						<c:choose>
				          	<c:when test="${order.paysource == 1}">
				          		<a href="/pcorder/selectChargeRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 2}">
				          		<a href="/pcorder/selectIncoinsRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 3}">
				          		<a href="/pcorder/selectOfflineRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 4}">
				          		<a href="/pcorder/selectWalletRecord?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
				          	<c:when test="${order.paysource == 5}">
				          		<a href="/pccardrecord/selectonlineconsume?ordernum=${order.ordernum}">${order.ordernum}</a>
					       	</c:when>
						</c:choose>
						</td>
						<td>${order.uusername}</td>
						<td>${order.dealer !=null ? order.dealer:"— —"}</td>
						<td>${order.muphonenum !=null ? order.muphonenum:"— —"}</td>
						<td>${order.eqcode !=null ? order.eqcode:"— —"}</td>
						<td>${order.code !=null ? order.code:"— —"}</td>
						<td>
						   <c:if test="${order.paysource == 5}">— —</c:if>
						   <c:if test="${order.paysource != 5}">${order.hardver=='00' ? "十路智慧款" : order.hardver=='01' ? "十路智慧款" : order.hardver=='02' ? "电轿款" : order.hardver=='03' ? "脉冲板子" :  order.hardver=='04' ? "离线充值机" : "— —"}</c:if>
						</td>
						<td>
						  <c:if test="${order.status == 1}"><font style="color: #00CC99;">+${order.money !=null ? order.money:"— —"}</font></c:if>
						  <c:if test="${order.status == 2}"><font style="color: #ea0d3f;">-${order.money !=null ? order.money:"— —"}</font></c:if>
						</td>
						<td><fmt:formatDate value="${order.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><a href="/pcorder/TraderecordDetails?paysource=${order.paysource}&ordernum=${order.ordernum}&orderid=${order.id}"><button class="btn btn-success">详情</button></a></td>
					   </tr>
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
<script src="${hdpath}/js/paging.js"></script>
<script type="text/javascript">
$(document).ready(function(){menulocation(37);});
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
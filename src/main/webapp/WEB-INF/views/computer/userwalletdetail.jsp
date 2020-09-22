<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
<title>钱包明细</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">
	  	<span style="color: #e66161;">${admin.realname != null ? admin.realname : admin.username}</span>钱包明细
	  	</a></li>
	</ul>
</div>

 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcadminiStrator/selecwalletdetail" id="listform">
			    <div class="searchdiv">
			      <ul class="search" style="padding-left:10px;">
			            <li>
			     		订单号&nbsp;&nbsp;
			     		<input type="text" placeholder="请输入订单号" name="ordernum" class="frame7" value="${ordernum}"/>
			     		<input type="hidden" name="uid" class="input" value="${admin.id}">
			     		</li>
			     		<li>类型&nbsp;&nbsp;
			     		<select name="paysource">
							<option value="0" <c:if test="${paysource == 0}"> selected="selected"</c:if> >请选择</option>
							<option value="1" <c:if test="${paysource == 1}"> selected="selected"</c:if> >充值</option>
							<option value="2" <c:if test="${paysource == 2}"> selected="selected"</c:if> >消费</option>
							<option value="5" <c:if test="${paysource == 5}"> selected="selected"</c:if> >退款到钱包</option>
							<option value="6" <c:if test="${paysource == 6}"> selected="selected"</c:if> >钱包退款</option>
						</select>
			     		</li>
			     		<li>时间:&nbsp;&nbsp;<input type="text" name="startTime" id="startTime" placeholder="请选择时间" value="${startTime}"
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
					<th>序号</th>
					<th>订单号</th>
					<th>金额</th>
					<th>余额</th>
					<th>操作类型</th>
					<th>时间</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${walletdetail}" var="wallet"  varStatus="as">
					  <tr id="name${wallet.id}">
						<td >${as.count+current}</td>
						<td><a href="/pcadminiStrator/genwalletdetail?genre=1&ordernum=${wallet.ordernum}&paysource=${wallet.paysource}">${wallet.ordernum}</a></td>
						<td>
							<c:if test="${wallet.paysource == 1 || wallet.paysource == 5 ||wallet.paysource == 7 }"><font color='#00CC99'>+${wallet.money}</font></c:if>
							<c:if test="${wallet.paysource == 2 || wallet.paysource == 3 ||wallet.paysource == 4 ||wallet.paysource == 6 ||wallet.paysource == 8}"><font color='#ef1111'>-${wallet.money}</font></c:if>
						</td>
						<td>${wallet.balance}</td>
						<td>${wallet.paysource == 1 ? "充值" : wallet.paysource == 2 ? "消费" : wallet.paysource == 3 ? "消费" : wallet.paysource == 4 ? "消费" : 
						wallet.paysource == 5 ? "退款到钱包" : wallet.paysource == 6 ? "钱包退款"  : wallet.paysource == 7 ? "虚拟充值" : wallet.paysource == 8 ? "虚拟退款" : "其它"}</td>
						<!-- 1、充值-2、充电-3、投币-4、离线卡充值-5、退款', -->
						<td><fmt:formatDate value="${wallet.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					   </tr>
					</c:forEach>
				 </tbody>
			   </table>
			 	<div align="center">
				<!-- 构建分页导航 -->
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
   			</div>
         </div>
      </div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#22'+' a').addClass('at');
	$('#22').parent().parent().parent().prev().removeClass("collapsed");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#22').parent().parent().parent().addClass("in");
	$('#22').parent().parent().parent().prev().attr("aria-expanded",true)
	})
</script>
<script type="text/javascript">
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selecwalletdetail?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcadminiStrator/selecwalletdetail?"+arguments; 
}
</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>用户银行卡信息</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath }/js/md5.js"></script>
<script type="text/javascript" src="${hdpath}/js/calendar.js"></script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">用户银行信息</a></li>
	</ul>
</div>
 <div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd"> </div>
		 <div class="table table-div">
			<table class="table table-hover" >
			  <thead>
				<tr>
					<th>序号</th>
					<th>昵称</th>
					<th>姓名</th>
					<th>电话</th>
					<th>账户与开户行</th>
					<th>类型</th>
					<th>费率(‰)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${userbank}" var="bank"  varStatus="as">
					<tr id="name${bank.id}" >
						<td >${as.count}</td>
						<%-- <td>${as.index + 1}</td> --%>
						<td>${bank.username}</td>
						<td>${bank.realname !=null ? bank.realname:"— —"}</td>
						<td>${bank.phone_num !=null ? bank.phone_num:"— —"}</td>
						<td >
							<div style="text-align: left; display: inline-block;">
								  <c:if test="${bank.company!=null}">公司名称：${bank.company}<br></c:if> 
						  银行名称：${bank.bankname !=null ? bank.bankname:"— —"}<br>
						  银行账号： <span class="bankNumSpan">${bank.bankcardnum !=null ? bank.bankcardnum:"— —"}</span>
							</div>
						</td>
						<td>${bank.type==1 ? "个人银行卡" : bank.type==2 ? "对公账户" : "— —"}</td>
						<td>${bank.rate !=null ? bank.rate:"— —"}&nbsp;‰</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){	
	$('#21'+' a').addClass('at');
	$('#21').parent().parent().parent().prev().removeClass("collapsed");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#21').parent().parent().parent().addClass("in");
	$('#21').parent().parent().parent().prev().attr("aria-expanded",true)
	
	//处理银行卡的
	var reg= /(\d{4})(?=\d)/g
	$('.bankNumSpan').each(function(i,item){
		var bankNum= $(item).text().trim()
		bankNum= bankNum.replace(reg,function(str){
		    return str+ ' '
		})
		$(item).text(bankNum)
	})
})
</script>
</html>
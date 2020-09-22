<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>卡包充值记录</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<script src="${hdpath }/js/md5.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">卡包充值记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">                      
			  <form method="post" action="${hdpath}/pcorder/selectWalletRecord" id="listform">
			    <div class="searchdiv">
			     	<ul class="search" style="padding-left:10px;">
			       		<li>
			     		订单&nbsp;&nbsp;
			     		<input type="text" placeholder="请输入订单" name="ordernum" value="${ordernum}" class="frame7"/>
			     		</li>
			           <li>
			     		昵称&nbsp;&nbsp;
			     		<input type="text" placeholder="输入姓名" name="username" value="${username}" class="frame2"/>
			     		</li>
			       		<li>
			     		姓名&nbsp;&nbsp;
			     		<input type="text" placeholder="输入商户名" name="realname" value="${realname}" class="frame2"/>
			     		</li>
			     		<li>
			     		类型&nbsp;&nbsp;
			     		<select name="paytype" class="frame1">
							<option value="0">请选择</option>
							<option value="1" <c:if test="${paytype == 1}"> selected="selected"</c:if> >充值</option>
							<option value="2" <c:if test="${paytype == 2}"> selected="selected"</c:if> >退款</option>
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
  		 <hr>
		 <div class="table table-div">
			<table class="table table-hover" >
			    <thead>
				   <tr>
					<th>序号</th>
					<th>订单号</th>
					<th>昵称</th>
					<th>姓名</th>
					<th>充值金额</th>
					<th>余额</th>
					<th>类型</th>
					<th>卡包</th>
					<th>操作时间</th>
					<c:if test="${admin.rank==0}"><th>退款</th></c:if>
					
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${orderWalletPay}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<%-- <td><a href="/pcorder/TraderecordDetails?paysource=4&ordernum=${order.ordernum}">${order.ordernum}</a></td> --%>
						<td><a href="/pcorder/detailsRestsOrderinfo?paysource=4&ordernum=${order.ordernum}&status=${order.paytype}">${order.ordernum}</a></td>
						<td>${order.username}</td>
						<td>${order.realname !=null ? order.realname:"— —"}</td>
						<td>${order.money}</td>
						<td>${order.balance !=null ? order.balance:"— —"}</td>
						<td>
						<c:choose>
							<c:when test="${order.paytype == 0}"><font color='#00CC99'>微信充值钱包</font></c:when>
							<c:when test="${order.paytype == 1}"><font color='#00CC99'>虚拟充值钱包</font></c:when>
							<c:when test="${order.paytype == 7}"><font color='#00CC99'>支付宝充值钱包</font></c:when>
							<c:when test="${order.paytype == 2}"><font color='#00CC99'>钱包微信退款</font></c:when>
							<c:when test="${order.paytype == 6}"><font color='#00CC99'>钱包虚拟退款</font></c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}"><font color='#00CC99'>退款到钱包</font></c:when>
						</c:choose>
						</td>
						<td>${order.remark != "wallet" ? order.remark:"钱包"}</td>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <c:if test="${admin.rank==0}">
					    <td>
					    	<c:choose>
								<c:when test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7}">
								  <button class="btn btn-danger" onclick="refundMoney('${order.id}','${order.paytype}','${order.uid}','${order.money}')"  value="${order.id }">退款</button>
								</c:when>
								<c:when test="${order.paytype == 2 || order.paytype == 6 || order.paytype == 8}">
								  <button disabled="disabled" class="btn" style="background-color: gray;border-color: gray;">退款</button>
								</c:when>
								<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}">
								  <button disabled="disabled" class="btn" style="background-color: gray;border-color: gray;">退款</button>
								</c:when>
							</c:choose>
					    </td>
					  	</c:if>
					    </tr>
					</c:forEach>
				 </tbody>
			   </table>
			    <!-- 弹框 -->
			   <div class="modal fade" id="refundModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			        <div class="modal-dialog" role="document">
			            <div class="modal-content">
			                <div class="modal-header">
			                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			                    <h4 class="modal-title" id="Modaltitle">退款确认</h4>
			                 </div>
			                 <div class="modal-body">
			                    <div class="form-group">
			                        <input type="password" name="refundpwd" id="refundpwd" class="form-control" placeholder="请输入密码">
			                        <input type="hidden" id="datainfo" data-orderId="" data-paytype="" data-uid="" >
			                    </div>
			               	  </div>
			                  <div class="modal-footer">
			                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal">确认</button>
			                  </div>
			            </div>
			        </div>
			    </div>
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
function refundMoney(orderId,paytype,uid,money){
	$.ajax({
		url : "${hdpath}/pcadminiStrator/getaccountbyId",
		data : {
			uid : uid,
		},
		type : "POST",
		cache : false,
		success : function(e) {
			if (e.balance < money) {
				alert("用户钱包金额不足"+money);
				return false;
			} else {
				$('#datainfo').attr('data-orderId',orderId)
				$('#datainfo').attr('data-paytype',paytype)
				$('#datainfo').attr('data-uid',uid)
				$("#refundModal").modal();
			}
		}
	});
}
$("#btn_submit").click(function (){
	var refundpwd = $("#refundModal").find("input[name='refundpwd']").val().trim();
	if (refundpwd == null || refundpwd == "") {
		alert("请输入密码");
		return false;
	}
	var orderId = $('#datainfo').attr('data-orderId').trim();
	var paytype = $('#datainfo').attr('data-paytype').trim();
	var uid = $('#datainfo').attr('data-uid').trim();
	var url;
	var data;
	if(paytype==0){
		url = "${hdpath}/wxpay/doRefund";
		data ={id : orderId,refundState : 4,pwd : refundpwd,utype : 1}
	}else if(paytype==1){
		url = "${hdpath}/wxpay/mercVirtualReturn";
		data ={id : orderId,type : 1}
	}else if(paytype==7){
		url = "${hdpath}/alipay/alipayRefund";
		data ={id : orderId,refundState : 4,pwd : refundpwd,utype : 1}
	}
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : data,
		type : "POST",
		cache : false,
		success : function(data) {
			result(data);
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
});

function result(data){
	if (data.ok == 'error') {
		alert("退款失败");
		window.location.reload();
	} else if (data.ok == 'pwderror') {
		alert("退款密码错误");
		window.location.reload();
	} else if (data.ok == 'usererror') {
		alert("用户金额不足");
		window.location.reload();
	} else if (data.ok == 'moneyerror') {
		alert("商户或合伙人金额不足");
		window.location.reload();
	} else if (data.ok == 'ok') {
		alert("退款成功");
		window.location.reload();
	}else{
		window.location.reload();
		alert("退款异常失败");
	}
}

</script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(36); })
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
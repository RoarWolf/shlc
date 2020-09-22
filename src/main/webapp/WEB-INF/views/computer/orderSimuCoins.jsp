<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>投币信息记录</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">投币信息记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcorder/selectIncoinsRecord" id="listform">
			    <div class="searchdiv">
			     <ul class="search" style="padding-left:10px;">
 					<li>
		     		订单:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入订单" name="ordernum" class="frame7" value="${ordernum}"  />
		     		</li>
		            <li>
		     		姓名:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入姓名" name="username" class="frame2" value="${username}"  />
		     		</li>
		            <%-- <li>
		     		手机号:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入手机号" name="phoneNum" class="input" value="${phoneNum}"  />
		     		</li> --%>
		       		<li>
		     		商户:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入商户名" name="dealer" class="frame2" value="${dealer}"  />
		     		</li>
		       		<li>
		     		设备号:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入设备号" name="equipmentnum" class="frame1" value="${equipmentnum}"  />
		     		</li>
		       		<%-- <li>
		     		端口号:&nbsp;&nbsp;
		     		<input type="text" placeholder="请输入设备号" name="port" class="input" value="${port}"  />
		     		</li> --%>
		       		<li>
		     		订单状态:&nbsp;&nbsp;
		     		<select name="orderstatu" class="frame1">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${orderstatu == 1}"> selected="selected"</c:if> >正常</option>
						<option value="2" <c:if test="${orderstatu == 2}"> selected="selected"</c:if> >已退款</option>
					</select>
		     		</li>
		     		<li>
		     		消费类型:&nbsp;&nbsp;
		     		<select name="type" class="frame1">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${type == 1}"> selected="selected"</c:if> >微信</option>
						<option value="2" <c:if test="${type == 2}"> selected="selected"</c:if> >支付宝</option>
						<option value="3" <c:if test="${type == 3}"> selected="selected"</c:if> >投币器</option>
					</select>
		     		</li>
		       		<li>
		     		是否为预订单:&nbsp;&nbsp;
		     		<select name="paystatus" class="frame1">
						<option value="1" <c:if test="${paystatus == 1}"> selected="selected"</c:if> >否</option>
						<option value="0" <c:if test="${paystatus == 0}"> selected="selected"</c:if> >是</option>
					</select>
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
					<th>姓名</th>
					<th>商户名</th>
					<th>设备号</th>
					<!-- <th>端口号</th> -->
					<!-- <th>商户电话</th> -->
					<th>消费额</th>
					<th>投币数</th>
					<th>订单状态</th>
					<th>支付方式</th>
					<th>回复状态</th>
					<th>记录时间</th>
					<th>退款时间</th>
					<th>退款操作</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${orderSimuCoins}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<td><a href="/pcorder/detailsRestsOrderinfo?paysource=2&ordernum=${order.ordernum}&status=${order.handletype}">${order.ordernum}</a></td>
						<%-- <td><a href="/pcorder/TraderecordDetails?paysource=2&ordernum=${order.ordernum}">${order.ordernum}</a></td> --%>
						<td>${order.username}</td>
						<td>${order.dealer}</td>
						<td><a href="/pcequipment/selectEquipmentLog?equipmentnum=${order.equipmentnum}&port=${order.port}">${order.equipmentnum}</a></td>
						<%-- <td><a href="/pcequipment/selectEquipmentLog?equipmentnum=${order.equipmentnum}&port=${order.port}">${order.port}</a></td> --%>
						<%-- <td>${order.deaphone}</td> --%>
						<td>${order.money}</td>
						<td>${order.handletype == 3 ? "— —" : order.coin_num}</td>
						<td><font color='#00CC99'>${order.handletype == 4 ? "退款" : order.handletype == 5 ? "退款" :"正常"}</font></td>
						<td><font color='#00CC99'>
						  <c:choose>
							<c:when test="${order.handletype == 1 || order.handletype == 4}">微信</c:when>
							<c:when test="${order.handletype == 2 || order.handletype == 5}">支付宝</c:when>
							<c:when test="${order.handletype == 3}">投币</c:when>
							<c:when test="${order.handletype == 6 || order.handletype == 7}">钱包</c:when>
							<c:when test="${order.handletype == 8 || order.handletype == 9}">微信小程序</c:when>
							<c:when test="${order.handletype == 10 || order.handletype == 11}">支付宝小程序</c:when>
						  </c:choose>
						</font></td>
						<td><font>${order.recycletype == 0 ? "未回复" : "回复成功"}</font></td>
						<td><fmt:formatDate value="${order.begin_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><fmt:formatDate value="${order.refund_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td>
					      <c:choose>
					    	 <c:when test="${order.handletype == 3 || order.handletype == 4 || order.handletype == 5 || order.handletype == 7 || order.handletype == 9 || order.handletype == 11}">
					      		<button class="btn btn-danger" disabled="disabled" style="background-color: gray;border-color: gray;" data-toggle="modal">退款</button>
					      	 </c:when>
					    	 <c:when test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">
								<button class="btn btn-danger" onclick="refundMoney('${order.id}','${order.handletype}')"  style="color: #fff;background-color: #d9534f;border-color: #d43f3a;">退款</button>
					      	 </c:when>
					      </c:choose>					    
					    </td>
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
			                        <!-- <label for="txt_parentdepartment">请输入验证码</label> -->
			                        <input type="text" name="refundpwd" id="refundpwd" class="form-control" placeholder="请输入验证码" style=" width: 60%;">
			                        <div style="<c:if test="${admin.rank==0}">display: none;</c:if>">
			                        <canvas id="canvas" width="120" height="34"></canvas>
			                        <a href="#" id="changeImg"><span onclick="refresh()"  style="font-size: 16px; margin-left: 1.5rem;">看不清，换一张</span></a>                   		
			                    	</div> 
			                    	<input type="hidden" name="orderId" class="form-control">
			                    	<input type="hidden" name="paytype" class="form-control">
			                    	<input type="hidden" name="securitycode" id="securitycode" class="form-control">
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
<script src="${hdpath}/js/authcode.js"></script>
<script type="text/javascript">
function refresh(){
	drawPic();
}
function refundMoney(orderId,paytype){
	var rank = ${admin.rank};
	if(rank==0){
		$(".Modaltitle").text("请输入密码");
		$("#refundModal").find("input[name='refundpwd']").attr('type','password');
		$("#refundModal").find("input[name='refundpwd']").attr('placeholder','请输入密码');
	}else{
		$("#Modaltitle").text("请输入验证码");
		$("#refundModal").find("input[name='refundpwd']").attr('type','text');
		$("#refundModal").find("input[name='refundpwd']").attr('placeholder','请输入验证码');
	}
	drawPic();
	$("#refundModal").find("input[name='orderId']").val(orderId);
	$("#refundModal").find("input[name='paytype']").val(paytype);
	$("#refundModal").modal();
} 
$("#btn_submit").click(function (){
	var url;
	var utype;
	var wolfkey = 0;
	var orderId = $("#refundModal").find("input[name='orderId']").val();
	var paytype = $("#refundModal").find("input[name='paytype']").val();
	var securitycode =  $("#refundModal").find("input[name='securitycode']").val();
	var refundpwd = $("#refundModal").find("input[name='refundpwd']").val().trim();
	var rank = ${admin.rank};
	if(rank==0){
		if (refundpwd == null || refundpwd == "") {
			alert("请输入密码");
			return false;
		}
		utype =1;
	}else{
		if(refundpwd!=securitycode){
			alert("验证码不正确！")
			return false;
		}
		utype =2;
	}
	if(paytype==1){//微信
		url = "${hdpath}/wxpay/doRefund";
	}else if(paytype==2){//支付宝
		url = "${hdpath}/alipay/alipayRefund";
	}else if(paytype==6){
		url = "${hdpath}/wxpay/doWalletReturn";
	}else if(paytype==8){
		url = "${hdpath}/wxpay/doRefund";
		wolfkey = 3;
	}else if(paytype==10){
		url = "${hdpath}/alipay/alipayRefund";
		wolfkey = 4;
	}
 	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : {
			id : orderId,
			refundState : 3,
			pwd : refundpwd,
			utype : utype,
			wolfkey : wolfkey,
		},
		type : "POST",
		cache : false,
		success : function(e){
			if(e==1){
				alert("密码不正确！")
			} else if (e.ok=="ok") {
				alert("退款成功！")
			} else if (e.ok=="error") {
				alert("退款失败！")
			}else{
				alert("退款异常失败");
			}
			location.reload();
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        },
        error: function() {
        	alert("退款异常");
        }
	});  
	
});
</script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(35); })
function currentPagenum(mark){//指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcorder/selectIncoinsRecord?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcorder/selectIncoinsRecord?"+arguments; 
}
</script>


</html>

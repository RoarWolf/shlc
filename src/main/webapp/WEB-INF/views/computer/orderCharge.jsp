<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>充电记录</title>
<script type="text/javascript" src="${hdpath}/jedate/jedate.js"></script>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<script src="${hdpath }/js/my.js"></script>
<style type="text/css">
.modal-backdrop {
    position: relative; 
}
.table>tbody>tr>td {
	vertical-align: middle;
}
.table .chargeInfoTd {
	text-align: center;
}
.table .chargeInfoTd >div {
	text-align: left;
	display: inline-block;
}
.table .chargeInfoTd p {
	padding-left: 5px;
	margin: 0;
	line-height: 1.6em;
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">充电记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">
			  <form method="post" action="${hdpath}/pcorder/selectChargeRecord" id="searchform">
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
		       		<li>
		     		商户:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入商户名" name="dealer" class="frame2" value="${dealer}"  />
		     		</li>
		       		<li>
		     		设备号:&nbsp;&nbsp;
		     		<input type="text" placeholder="输入设备号" name="equipmentnum" class="frame1" value="${equipmentnum}"  />
		     		</li>
		       		<li>
		     		订单状态:&nbsp;&nbsp;
		     		<select name="number" class="frame1">
						<option value="-1">请选择</option>
						<option value="0" <c:if test="${number == 0}"> selected="selected"</c:if> >正常</option>
						<option value="1" <c:if test="${number == 1}"> selected="selected"</c:if> >已退款</option>
					</select>
		     		</li>
		     		<li>
		     		消费类型:&nbsp;&nbsp;
		     		<select name="paytype">
						<option value="0">请选择</option>
						<option value="1" <c:if test="${paytype == 1}"> selected="selected"</c:if> >钱包</option>
						<option value="2" <c:if test="${paytype == 2}"> selected="selected"</c:if> >微信</option>
						<option value="3" <c:if test="${paytype == 3}"> selected="selected"</c:if> >支付宝</option>
					</select>
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
					<th>端口号</th>
					<th>消费额</th>
					<th>订单状态</th>
					<th>支付方式</th>
					<th>充电信息</th>
					<th>是否续充冲</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>结束原因</th>
					<th>退款时间</th>
					<th>详情</th>
					<th>退款操作</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${orderCharge}" var="order"  varStatus="as">
					  <tr id="name${order.id}" >
						<td >${as.count+current}</td>
						<%-- <td><a href="/pcorder/TraderecordDetails?paysource=1&ordernum=${order.ordernum}">${order.ordernum}</a></td> --%>
						<td><a href="/pcorder/detailsRestsOrderinfo?paysource=1&ordernum=${order.ordernum}&status=${order.number}">${order.ordernum}</a></td>
						<td>${order.username !=null ? order.username:"— —"}</td>
						<td>${order.dealer !=null ? order.dealer:"— —"}</td>
						<td><a href="/pcequipment/selectEquipmentLog?equipmentnum=${order.equipmentnum}&port=${order.port}">${order.equipmentnum}</a></td>
						<td><a href="/pcequipment/selectEquipmentLog?equipmentnum=${order.equipmentnum}&port=${order.port}">${order.port}</a></td>
						<td>${order.expenditure !=null ? order.expenditure:"— —"}</td>
						<td style="color: #00CC99;">${order.number == 0 ? "正常" : order.number == 1 ? "全额退款" : order.number == 2 ? "部分退款" : "其它"}</td>
						<td> 
							<font color='#00CC99' id="paytype${order.id }" data-mypaytype="${order.paytype}">${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝": order.paytype == 4 ? "包月" : "— —"}</font>
						</td>
						<td class="chargeInfoTd">
							<div>
							 <p><span>${order.consume_quantity/100}</span>度</p>
							 <p><span>${order.consume_time}</span>分钟</p>
							</div>
						</td>
						<td>
							<c:if test="${order.ifcontinue!=null}"><a href="/pcorder/selectChargeRecord?continue=1&orderId=${order.ifcontinue}">是</a></c:if>
					    	<c:if test="${order.ifcontinue==null}">否</c:if>
						</td>
						<td><fmt:formatDate value="${order.begintime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><fmt:formatDate value="${order.endtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td>${order.resultinfo==0?"时间或者电量用完":order.resultinfo==1?"手动停止":order.resultinfo==2?"充电满了":
					    order.resultinfo==3?"超功率自停":order.resultinfo==4?"远程断电":order.resultinfo==11?"被迫停止":order.resultinfo==255?"日志结束":"— —"}</td>
					    
					    <td><fmt:formatDate value="${order.refund_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><a href="/pcequipment/powerBrokenLine?orderId=${order.id}"><button class="btn btn-success">详情</button></a></td>
					    <td>
					      <c:choose>
					      	<c:when test="${order.number == 0}">
					      		 <button class="btn btn-danger" onclick="refundMoney('${order.id}','${order.paytype}')"  value="${order.id }">退款</button>
					      	</c:when>
					      	<c:when test="${order.number == 2}">
					      		<button class="btn btn-danger" onclick="withdraw('${order.id}')">撤回</button>
					      	</c:when>
					        <c:otherwise>
					        	<button disabled="disabled" style="background-color: gray;border-color: gray;" class="btn btn-danger" >退款</button>
					        </c:otherwise>
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
			    <!--  -->
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
function withdraw(id){
	$.ajax({
		url : "/wxpay/withdrawWalletRefund",
		data : { id : id },
		type : "POST",
		cache : false,
		success : function(e) {
			if (e.ok == "ok") {
				alert(e.messg);
				window.location.reload();
			} else if (e.ok == "error") {
				alert(e.messg);
				window.location.reload();
			} else{
				window.location.reload();
			}
		}
	});
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
	if(paytype==1){//充电模块 '1:钱包  2:微信  3:支付宝  4:微信小程序'
		url = "${hdpath}/wxpay/doWalletReturn";
	}else if(paytype==2){
		url = "${hdpath}/wxpay/doRefund";
	}else if(paytype==3){
		url = "${hdpath}/alipay/aliDoRefund";
		wolfkey = 4;
	} 
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : {
			id : orderId,
			refundState : 1,
			pwd : refundpwd,
			utype : utype,
		},
		type : "POST",
		cache : false,
		success : function(data) {
			if (data.ok == 'error') {
				alert("退款失败");
				window.location.reload();
			} else if (data.ok == 'pwderror') {
				alert("退款密码错误");
				window.location.reload();
			} else if (data.ok == 'moneyerror') {
				alert("商户或合伙人金额不足");
				window.location.reload();
			} else if (data.ok == 'ok') {
				alert("退款成功");
				window.location.reload();
			}else{
				window.location.reload();
				alert("退款异常");
			}
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
});
</script>
<script type="text/javascript">
$(document).ready(function(){ menulocation(33); })
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

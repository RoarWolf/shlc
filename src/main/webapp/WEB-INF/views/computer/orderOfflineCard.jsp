<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>离线卡记录</title>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">离线卡记录</a></li>
	</ul>
</div>
<div class="admin">
	 <div class="panel admin-panel" id="adminPanel">
    		<div class="conditionsd">                      
			  <form method="post" action="${hdpath}/pcorder/selectOfflineRecord" id="listform">
			    <div class="searchdiv">
			     	<ul class="search" style="padding-left:10px;">
			       		<li>
			     		订单&nbsp;&nbsp;
			     		<input type="text" placeholder="请输入订单" name="ordernum" value="${ordernum}" class="frame7" />
			     		</li>
			           <li>
			     		姓名&nbsp;&nbsp;
			     		<input type="text" placeholder="请输入姓名" name="username" value="${username}" class="frame2"/>
			     		</li>
			       		<li>
			     		商户&nbsp;&nbsp;
			     		<input type="text" placeholder="请输入商户名" name="dealer" value="${dealer}" class="frame2"/>
			     		</li>
			       		<li>
			     		设备号&nbsp;&nbsp;
			     		<input type="text" placeholder="输入设备号" name="equipmentnum" value="${equipmentnum}" class="frame1"/>
			     		</li>
			       		<li>
			     		卡号&nbsp;&nbsp;
			     		<input type="text" placeholder="输入卡号" name="cardID" value="${cardID}" class="frame1"/>
			     		</li>
			     		<li>
			     		操作类型&nbsp;&nbsp;
			     		<select name="handletype" class="frame1">
							<option value="-1">请选择</option>
							<option value="0" <c:if test="${handletype == 0}"> selected="selected"</c:if> >扣费</option>
							<option value="1" <c:if test="${handletype == 1}"> selected="selected"</c:if> >充值</option>
							<option value="2" <c:if test="${handletype == 2}"> selected="selected"</c:if> >查询</option>
						</select>
			     		</li>
			     		<li>
			     		回复类型&nbsp;&nbsp;
			     		<select name="recycletype" class="frame2">
							<option value="-1">请选择</option>
							<option value="0" <c:if test="${recycletype == 0}"> selected="selected"</c:if> >操作成功</option>
							<option value="1" <c:if test="${recycletype == 1}"> selected="selected"</c:if> >余额不足</option>
							<option value="2" <c:if test="${recycletype == 2}"> selected="selected"</c:if> >无卡</option>
							<option value="3" <c:if test="${recycletype == 3}"> selected="selected"</c:if> >卡号不统一</option>
							<option value="4" <c:if test="${recycletype == 3}"> selected="selected"</c:if> >其他错误</option>
						</select>
			     		</li>
			     		<li>
			     		支付类型&nbsp;&nbsp;
			     		<select name="paytype" class="frame2">
							<option value="0">请选择</option>
							<option value="1" <c:if test="${paytype == 1}"> selected="selected"</c:if> >微信</option>
							<option value="2" <c:if test="${paytype == 2}"> selected="selected"</c:if> >支付宝</option>
							<option value="3" <c:if test="${paytype == 3}"> selected="selected"</c:if> >微信退费</option>
							<option value="4" <c:if test="${paytype == 4}"> selected="selected"</c:if> >支付宝退费</option>
							<option value="5" <c:if test="${paytype == 5}"> selected="selected"</c:if> >钱包</option>
							<option value="5" <c:if test="${paytype == 6}"> selected="selected"</c:if> >钱包退费</option>
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
					<th>姓名</th>
					<th>商户名</th>
					<th>设备号</th>
					<th>卡号</th>
					<th>余额</th>
					<th>充值金额</th>
					<th>到账金额</th>
					<th>优惠金额</th>
					<th>支付类型</th>
					<th>操作类型</th>
					<th>回复类型</th>
					<th>操作时间</th>
					<th>退款时间</th>
					<th>退款</th>
				  </tr>
				</thead>
				<tbody>
				   <c:forEach items="${orderOffline}" var="offline"  varStatus="as">
					  <tr id="name${offline.id}" >
						<td >${as.count+current}</td>
						<%-- <td><a href="/pcorder/TraderecordDetails?paysource=3&ordernum=${offline.ordernum}">${offline.ordernum}</a></td> --%>
						<td><a href="/pcorder/detailsRestsOrderinfo?paysource=3&ordernum=${offline.ordernum}&status=${offline.paytype}">${offline.ordernum}</a></td>
						<td>${offline.username}</td>
						<td>${offline.dealer !=null ? offline.dealer:"— —"}</td>
						<td>${offline.equipmentnum}</td>
						<td>${offline.cardID}</td>
						<c:if test="${offline.handletype == 0}">
							<td><font color='#00CC99'>${offline.balance !=null ? offline.balance:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.chargemoney !=null ? offline.chargemoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.accountmoney !=null ? offline.accountmoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.discountmoney !=null ? offline.discountmoney:"0.00"}</font></td>
						</c:if>
						<c:if test="${offline.handletype == 1}">
							<td><font color='#00CC99'>${offline.balance !=null ? offline.balance:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.chargemoney !=null ? offline.chargemoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.accountmoney !=null ? offline.accountmoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.discountmoney !=null ? offline.discountmoney:"0.00"}</font></td>
						</c:if>
						<c:if test="${offline.handletype == 2}">
							<td><font color='#00CC99'>${offline.balance !=null ? offline.balance:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.chargemoney !=null ? offline.chargemoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.accountmoney !=null ? offline.accountmoney:"0.00"}</font></td>
							<td><font color='#00CC99'>${offline.discountmoney !=null ? offline.discountmoney:"0.00"}</font></td>
						</c:if>
						
						<td id="paytype${offline.id }" data-mypaytype="${offline.paytype}">${offline.paytype==1 ? "微信" : offline.paytype==2 ? "支付宝" : offline.paytype==3 ? "微信退款" : offline.paytype==4?"支付宝退款":"— —"}</td>
						<td>
						<c:choose>
							<c:when test="${offline.handletype == 0}">扣费</c:when>
							<c:when test="${offline.handletype == 1}">充值</c:when>
							<c:when test="${offline.handletype == 2}">查询</c:when>
						</c:choose>
						</td>
						<td>
						<c:choose>
							<c:when test="${offline.recycletype == 0}">操作成功</c:when>
							<c:when test="${offline.recycletype == 1}">余额不足</c:when>
							<c:when test="${offline.recycletype == 2}">无卡</c:when>
							<c:when test="${offline.recycletype == 3}">卡号不统一</c:when>
							<c:when test="${offline.recycletype == 4}">其他错误</c:when>
						</c:choose>
						</td>
						<td><fmt:formatDate value="${offline.begin_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><fmt:formatDate value="${offline.refund_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						 <td>
						 	<c:choose>
						 		<c:when test="${offline.handletype==1}">
						 			<button <c:if test="${offline.paytype == 3 || offline.paytype == 4}">disabled="disabled" style="background-color: gray;border-color: gray;"</c:if>
					      class="btn btn-danger" data-toggle="modal" data-target=".${offline.id }"  onclick="refundMoney('${offline.id}','${offline.paytype}')" value="${offline.id }">退款</button>
						 		</c:when>
						 		<c:otherwise>
						 			— —
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
				<div align="center">
				<!-- 页码内容显示 -->
			   <div align="center">
				<%@ include file="/WEB-INF/views/public/pagearea.jsp"%>
			   </div>
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
	}else if(paytype==5){
		url = "${hdpath}/wxpay/doWalletReturn";
	}
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : {
			id : orderId,
			refundState : 2,
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
$(document).ready(function(){ menulocation(34); })
function currentPagenum(mark){//点击页码指定页
	var arguments = $("#listform").serialize()+"&currentPage="+mark;
	window.location.href="${pageContext.request.contextPath}/pcorder/selectOfflineRecord?"+arguments; 
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
	window.location.href="${pageContext.request.contextPath}/pcorder/selectOfflineRecord?"+arguments; 
}
</script>
</html>
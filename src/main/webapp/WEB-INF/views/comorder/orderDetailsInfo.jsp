<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>订单详情</title>
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
<ul class="bread">
  <li><a href="javascript:void(0)" target="right" class="icon-home">订单详情</a></li>
</ul>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
    <div class="conditionsd"></div>
  	<div class="table table-div">
		<table class="table table-hover">
			<tbody>
			    <c:choose>
				<c:when test="${paysource == 1}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.number==0}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></span></c:if> 
						<c:if test="${order.number==1 || order.number==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
					<tr align="center">
						<th>充电时间（分钟）</th>
						<td>${order.durationtime}</td>
					</tr>
					<tr align="center">
						<th>充电电量（度）</th>
						<td>${order.quantity/100}</td>
					</tr>
					<tr align="center">
						<th>付款方式</th>
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : order.paytype == 4 ? "包月" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>充电时间</th>
						<td><fmt:formatDate value="${order.begintime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.number == 1}">
								<font color="gray">全额退款</font>
							</c:if>
							<c:if test="${order.number == 0}">
								<font color="#5cb85c">正常</font>
							</c:if>
							<c:if test="${order.number == 2}">
								<font color="gray">部分退款</font>(<font color="red">${order.refundMoney}</font>)
							</c:if>
					</tr>
					<c:choose>
						<c:when test="${order.number == 0}">
						  <c:if test="${order.paytype == 2 || order.paytype == 3}">
						    <tr>
							 <th>操作</th>
							 <td><button onclick="refundMoney('${order.id}','${paysource}','${order.paytype}')" class="btn btn-danger">退款</button> </td>
						    </tr>
						   </c:if>
						</c:when>
					</c:choose>
				</c:when>
				<c:when test="${paysource == 2 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.money}" pattern="0.00" /></span></c:if> 
						<c:if test="${order.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.money}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<tr align="center">
						<th>投币个数</th>
						<td>${order.coinNum}</td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:choose>
								<c:when test="${order.handletype == 1 || order.handletype == 4}">微信</c:when>
								<c:when test="${order.handletype == 2 || order.handletype == 5}">支付宝</c:when>
								<c:when test="${order.handletype == 3}">投币</c:when>
								<c:when test="${order.handletype == 6 || order.handletype == 7}">钱包</c:when>
								<c:when test="${order.handletype == 8 || order.handletype == 9}">微信小程序</c:when>
								<c:when test="${order.handletype == 10 || order.handletype == 11}">支付宝小程序</c:when>
							</c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.beginTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:choose>
								<c:when test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 3 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">正常</c:when>
								<c:when test="${order.handletype == 4}"><font style="color: #ec2a2a;">微信已退费</font></c:when>
								<c:when test="${order.handletype == 5}"><font style="color: #ec2a2a;">支付宝已退费</font></c:when>
								<c:when test="${order.handletype == 7}"><font style="color: #ec2a2a;">已退款到钱包</font></c:when>
								<c:when test="${order.handletype == 9}"><font style="color: #ec2a2a;">微信小程序已退费</font></c:when>
								<c:when test="${order.handletype == 11}"><font style="color: #ec2a2a;">支付宝小程序已退费</font></c:when>
							</c:choose>
						</td>
					</tr>
					<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 6 || order.handletype == 8 || order.handletype == 10}">
					  <tr>
						<th>操作</th>
						 <td><button onclick="refundMoney('${order.id}','${paysource}','${order.handletype}')" class="btn btn-danger">退款</button></td>
					   </tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 3 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<th>卡余额</th>
						<td><fmt:formatNumber value="${order.balance }" pattern="0.00" /></td>
					</tr>
					<c:if test="${source==1}">
					<tr align="center">
						<th>付款金额（元）</th>
						<td>
						<c:if test="${order.paytype==1 || order.paytype==2 || order.paytype==5}">
						<span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${order.chargemoney}" pattern="0.00" /></span>
						</c:if> 
						<c:if test="${order.paytype==3 || order.paytype==4 || order.paytype==6}">
						<span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${order.chargemoney}" pattern="0.00" /></span>
						</c:if>
						</td>
					</tr>
					<tr align="center">
						<th>充卡金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>商户收入（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.mermoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					<c:if test="${partrecord.manid!=0}">
					<tr align="center">
						<th>合伙人（元）</th>
						<td>
						<c:if test="${partrecord.status==1}"><span style="color: #37d825;">+&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if> 
						<c:if test="${partrecord.status==2}"><span style="color: #d20e0e;">-&nbsp;<fmt:formatNumber value="${partrecord.manmoney}" pattern="0.00" /></span></c:if>
						</td>
					</tr>
					</c:if>
					</c:if>
					<tr align="center">
						<th>支付方式</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 1 || order.paytype == 3}"><font>微信</font></c:when>
							<c:when test="${order.paytype == 2 || order.paytype == 4}"><font>支付宝</font></c:when>
						  	<c:otherwise>查询</c:otherwise>
						  </c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>充值时间</th>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
						 <c:choose>
							<c:when test="${order.paytype == 1 || order.paytype == 2  || order.paytype == 5}"><font>正常</font></c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 6 }"><font>退款</font></c:when>
						  	<c:otherwise>查询</c:otherwise>
						  </c:choose>
						</td>
					</tr>
					<c:if test="${order.paytype == 1 || order.paytype == 2}">
					  <tr>
						<th>操作</th>
						 <td><button onclick="refundMoney('${order.id}','${paysource}','${order.paytype}')" class="btn btn-danger">退款</button></td>
					   </tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 4 }">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额</th>
						<td>
							<fmt:formatNumber value="${order.money}" pattern="0.00"/>
						</td>
					</tr>
					<tr align="center">
						<th>钱包金额（元）</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
						  <c:choose>
							<c:when test="${order.paytype == 0 || order.paytype == 2}">微信</c:when>
							<c:when test="${order.paytype == 1 || order.paytype == 6}">虚拟操作</c:when>
							<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}">自动退费</c:when>
						    <c:when test="${order.paytype == 7 || order.paytype == 8}">支付宝</c:when>
						  </c:choose>
						</td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.paytype == 0 ? "微信钱包充值" : order.paytype == 1 ? "虚拟充值钱包" : order.paytype == 2 ? "钱包退款" : order.paytype == 3 ? "充电记录退费" : 
						order.paytype == 4 ? "离线卡充值退费" : order.paytype == 5 ? "模拟投币退费" : order.paytype == 6 ? "虚拟钱包退费" : 
						order.paytype == 7 ? "支付宝钱包充值" : order.paytype == 8? "钱包退款" : "— —" }</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:choose>
								<c:when test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7}">正常</c:when>
								<c:when test="${order.paytype == 2 || order.paytype == 6 || order.paytype == 8}"><font style="color: #ec2a2a;">钱包退款</font></c:when>
								<c:when test="${order.paytype == 3 || order.paytype == 4 || order.paytype == 5}"><font style="color: #ec2a2a;">退费到钱包</font></c:when>
							</c:choose>
						</td>
					</tr>
					<c:if test="${order.paytype == 0 || order.paytype == 1 || order.paytype == 7}">
					<tr align="center">
						<th>退款</th>
						 <td><button onclick="refundMoney('${order.id}','${paysource}','${order.paytype}')" class="btn btn-danger">退款</button></td>
					</tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 5 }"> 
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>卡号</th>
						<td>${order.cardID}</td>
					</tr>
					<tr align="center">
						<th>余额</th>
						<td><fmt:formatNumber value="${order.balance}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充值金额</th>
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>到账金额</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.type == 1 ? "消费" : order.type == 2 ? "余额回收": order.type == 3 ? "微信充值" : order.type == 4 ? "卡操作" : order.type == 5 ? "微信退款" : order.type == 6 ? "支付宝充值" : order.type == 7 ? "支付宝退款" : order.type == 8 ? "虚拟充值" : order.type == 9 ? "虚拟充值退款" : "其它"}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<c:choose>
						   <c:when test="${order.flag==1}">
							<td><font>${order.status==1 ? "正常" : order.status==2 ? "激活" : order.status==3 ? "绑定" : 
							order.type==4 ? "删除" : order.status==5 ? "挂失" : order.status==6 ? "解挂" : order.status==7 ? "卡号修改" : "修改备注"}</font></td>
						   </c:when>
						   <c:when test="${order.flag==2}"><td style="color: #ef2222;">已退款</td></c:when>
						</c:choose>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<c:if test="${order.flag == 1}">
					  <tr>
						<th>操作</th>
						 <td><button onclick="refundMoney('${order.id}','${paysource}','${order.type}')" class="btn btn-danger">退款</button></td>
					   </tr>
					</c:if>
				</c:when>
				<c:when test="${paysource == 6 }"> 
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>金额</th>
						<td>${order.money}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.ifrefund == 1 }">
								<font color="#ef2222">已退款</font>
							</c:if>
							<c:if test="${order.ifrefund == 0 }">
								<font color="#ef2222">${order.status == 2 ? '已退款' : '正常' }</font> 
							</c:if>
						</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<c:if test="${order.status != 2 && order.ifrefund == 0}">
					<tr align="center">
						<td>退费</td>
						<td><button class="btn btn-danger" onclick="refundMoney('${order.id}','${paysource}','2')">退款</button></td>
					</tr>
					</c:if>
				</c:when>
			</c:choose>
			</tbody>
		</table>
	</div>
   <!-- 弹框 -->
   <div class="modal fade ${order.id}" id="detailsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="detailsModalLabel">退款确认</h4>
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
                    	<input type="hidden" name="paysource" class="form-control">
                    	<input type="hidden" name="securitycode" class="form-control">
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
  </div>
  </div>
</body>
<script src="${hdpath}/js/paging.js"></script>

<script type="text/javascript">
$(document).ready(function(){menulocation(31);})
function refresh(){
	drawPic();
}

function refundMoney(orderId,paysource,paytype){
	var rank = ${admin.rank};
	if(rank==0){
		$("#detailsModalLabel").text("请输入密码");
		$("#detailsModal").find("input[name='refundpwd']").attr('type','password');
		$("#detailsModal").find("input[name='refundpwd']").attr('placeholder','请输入密码');
	}else{
		$("#detailsModalLabel").text("请输入验证码");
		$("#detailsModal").find("input[name='refundpwd']").attr('type','text');
		$("#detailsModal").find("input[name='refundpwd']").attr('placeholder','请输入验证码');
	}
	drawPic();
	$("#detailsModal").find("input[name='orderId']").val(orderId);
	$("#detailsModal").find("input[name='paysource']").val(paysource);
	$("#detailsModal").find("input[name='paytype']").val(paytype);
	$("#detailsModal").modal();
}
$("#btn_submit").click(function (){
	var url;
	var utype;
	var refundState;//1:充电、 2:脉冲、 3:离线 
	var wolfkey = 0;
	var data;
	var orderId = $("#detailsModal").find("input[name='orderId']").val();
	var paysource = $("#detailsModal").find("input[name='paysource']").val();
	var paytype = $("#detailsModal").find("input[name='paytype']").val();
	var securitycode =  $("#detailsModal").find("input[name='securitycode']").val();
	var refundpwd = $("#detailsModal").find("input[name='refundpwd']").val();
	var rank = ${admin.rank};
	if(rank==0){
		utype =1;
	}else{
		if(refundpwd!=securitycode){
			alert("验证码不正确！")
			return false;
		}
		utype =2;
	}
	///wxDoRefund  Integer id, Integer refundState, Integer wolfkey
	if(paysource==1){//充电模块 '1:钱包  2:微信  3:支付宝  4:微信小程序'
		refundState = 1;
		if(paytype==1){
			url = "${hdpath}/wxpay/doWalletReturn";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==2){
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==3){
			url = "${hdpath}/alipay/alipayRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==4){
			url = "${hdpath}/wxpay/wxDoRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}
	}else if(paysource==2){//脉冲模块 1微信、2支付宝、3投币、4 微信退款、5 支付宝退款、6 钱包、7钱包退款、8微信小程序、9微信小程序退款、10支付宝小程序、11支付宝小程序退款*/
		refundState = 3;
		if(paytype==1){
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==2){
			url = "${hdpath}/alipay/alipayRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==6){
			url = "${hdpath}/wxpay/doWalletReturn";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==8){
			url = "${hdpath}/wxpay/wxDoRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype,wolfkey:3};
		}else if(paytype==10){
			url = "${hdpath}/alipay/aliDoRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype,wolfkey:4};
		}
	}else if(paysource==3){//离线充值机 1-wx、2-alipay、3-wx refund、4-alipay refund、5-wallet、6-wallet refund*/
		refundState = 2;
		if(paytype==1){
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==2){
			url = "${hdpath}/alipay/alipayRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==5){
			url = "${hdpath}/wxpay/doWalletReturn";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}
	}else if(paysource==4){//钱包
		refundState = 4;
		if(paytype==0){
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==1){
			url = "${hdpath}/wxpay/mercVirtualReturn";
			data ={id : orderId,type : 1}
		}else if(paytype==7){
			url = "${hdpath}/alipay/alipayRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}
	}else if(paysource==5){//在线卡  onlineCard
		refundState = 5;
		if(paytype==3){//微信
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==6){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
		}else if(paytype==8){
			url = "${hdpath}/wxpay/mercVirtualReturn";
			data ={id : orderId,type : 2}
		}
	}else if(paysource==6){//包月
		refundState = 6;
			url = "${hdpath}/wxpay/doRefund";
			data ={id : orderId,refundState : refundState,pwd : refundpwd,utype : utype}
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

/**绘制验证码图片**/
function drawPic(){
  var canvas=document.getElementById("canvas");
  var width=canvas.width;
  var height=canvas.height;
  var ctx = canvas.getContext('2d');
  ctx.textBaseline = 'bottom';

  /**绘制背景色**/
  ctx.fillStyle = randomColor(180,240); //颜色若太深可能导致看不清
  ctx.fillRect(0,0,width,height);
  /**绘制文字**/
  var str = 'ABCEFGHJKLMNPQRSTWXY123456789';
  var securitycode = "";
  for(var i=0; i<4; i++){
    var txt = str[randomNum(0,str.length)];
    securitycode = securitycode + txt;
    ctx.fillStyle = randomColor(50,160);  //随机生成字体颜色
    ctx.font = randomNum(16,40)+'px SimHei'; //随机生成字体大小
    var x = 10+i*25;
    var y = randomNum(25,35);
    var deg = randomNum(-45, 45);
    //修改坐标原点和旋转角度
    ctx.translate(x,y);
    ctx.rotate(deg*Math.PI/180);
    ctx.fillText(txt, 0,0);
    //恢复坐标原点和旋转角度
    ctx.rotate(-deg*Math.PI/180);
    ctx.translate(-x,-y);
  }
  $("#detailsModal").find("input[name='securitycode']").val(securitycode);
}
  
/**生成一个随机数**/
function randomNum(min,max){
  return Math.floor( Math.random()*(max-min)+min);
}
/**生成一个随机色**/
function randomColor(min,max){
  var r = randomNum(min,max);
  var g = randomNum(min,max);
  var b = randomNum(min,max);
  return "rgb("+r+","+g+","+b+")";
}           
</script>
</html>
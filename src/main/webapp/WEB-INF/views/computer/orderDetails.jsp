<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>订单详情查看</title>
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
  <li><a href="javascript:void(0)" target="right" class="icon-home">订单详情查看</a></li>
</ul>
<div class="admin">
  <div class="panel admin-panel" id="adminPanel">
    <div class="conditionsd"></div>
  	<div class="table table-div">
		<table class="table table-hover">
			<tbody>
				<!-- //商户 paysource  来源 1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包', -->
			    <c:choose>
				<c:when test="${paysource == 1}">
					<tr>
						<th>订单号</th><td>${order.ordernum}</td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.expenditure}" pattern="0.00" /></td>
					</tr>
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
						<td>${order.paytype == 1 ? "钱包" : order.paytype == 2 ? "微信" : order.paytype == 3 ? "支付宝" : "— —" }</td>
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
						<td><fmt:formatNumber value="${order.money}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>投币个数</th>
						<td>${order.coinNum}</td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:if test="${order.handletype == 1}"><font>微信</font></c:if>
							<c:if test="${order.handletype == 2}"><font>支付宝</font></c:if>
							<c:if test="${order.handletype == 3}"><font>投币</font></c:if>
						</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.beginTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.handletype == 1 || order.handletype == 2 || order.handletype == 3}"><font>正常</font></c:if>
							<c:if test="${order.handletype == 4 || order.handletype == 5}"><font>退款</font></c:if>
						</td>
					</tr>
					<c:if test="${order.handletype == 1 || order.handletype == 2}">
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
						<td>${order.cardID}"</td>
					</tr>
					<tr align="center">
						<th>卡余额</th>
						<td><fmt:formatNumber value="${order.balance }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>付款金额（元）</th>
						<td><fmt:formatNumber value="${order.accountmoney}" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>充卡金额（元）</th>
						<td><fmt:formatNumber value="${order.chargemoney }" pattern="0.00" /></td>
					</tr>
					<tr align="center">
						<th>支付方式</th>
						<td>
							<c:if test="${order.paytype == 1 || order.paytype == 3}"><font>微信</font></c:if>
							<c:if test="${order.paytype == 2 || order.paytype == 4}"><font>支付宝</font></c:if>
						</td>
					</tr>
					<tr align="center">
						<th>充值时间</th>
						<td><fmt:formatDate value="${order.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.paytype == 1 || order.paytype == 2 }"><font>正常</font></c:if>
							<c:if test="${order.paytype == 3 || order.paytype == 4 }"><font>退款</font></c:if>
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
							<font>微信</font>
						</td>
					</tr>
					<tr align="center">
						<th>操作类型</th>
						<td>${order.paytype == 0 ? "钱包充值" : order.paytype == 1 ? "充值卡" : order.paytype == 2 ? "卡退费" : order.paytype == 3 ? "充电记录退费" : order.paytype == 4 ? "离线卡充值退费" : order.paytype == 5 ? "模拟投币退费"
						 : "— —" }</td>
					</tr>
					<tr align="center">
						<th>交易时间</th>
						<td><fmt:formatDate value="${order.paytime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
							<c:if test="${order.paytype == 0 || order.paytype == 1 }"><font>正常</font></c:if>
							<c:if test="${order.paytype == 2 || order.paytype == 3 || order.paytype == 4 || order.paytype == 5 }"><font>退款</font></c:if>
						</td>
					</tr>
					 <c:if test="${admin.rank==0 && order.paytype == 0}">
					  <tr>
						<th>操作</th>
						 <td><button onclick="refundwallet('${order.id}', '${order.uid}','${order.money}','${paysource}')" class="btn btn-danger">退款</button></td>
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
						<th>类型</th>
						<td>${order.type == 1 ? "消费" : order.type == 2 ? "退款": order.type == 3 ? "充值" : order.type == 4 ? "卡操作"  : "其它"}</td>
					</tr>
					<tr align="center">
						<th>订单状态</th>
						<td>
						   ${order.status == 0 ? "<font color='gray'>未成功</font>" : order.status == 1 ? "<font color='#5cb85c'>成功</font>" : order.status == 2 ? 
						   "激活": order.status == 3 ? "绑定" : order.status == 4 ? "解绑" : order.status == 5 ? "挂失" : order.status == 6 ? "解挂"  : "注销"}
						</td>
					</tr>
					<tr align="center">
						<th>创建时间</th>
						<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
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
function refundwallet(orderId,uid,money,paysource){
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
				$("#detailsModalLabel").text("请输入密码");
				$("#detailsModal").find("input[name='refundpwd']").attr('type','password');
				$("#detailsModal").find("input[name='refundpwd']").attr('placeholder','请输入密码');
				
				$("#detailsModal").find("input[name='orderId']").val(orderId);
				$("#detailsModal").find("input[name='paysource']").val(paysource);
				//$("#detailsModal").find("input[name='paytype']").val(paytype);
				$("#detailsModal").modal();
			}
		}
	});
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
	var refundState;//1:充电、 2:离线、 3:投币  4:钱包
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
	
	if(paysource==1){//充电模块  chargeRecord
		if(paytype==2){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==3){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}
		refundState = 1;
	}else if(paysource==2){//脉冲模块 inCoinsService
		if(paytype==1){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==2){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}
		refundState = 3;
	}else if(paysource==3){//离线充值机 offlineCardService
		if(paytype==1){//微信
			url = "${hdpath}/wxpay/doRefund";
		}else if(paytype==2){//支付宝
			url = "${hdpath}/alipay/alipayRefund";
		}
		refundState = 2;
	}else if(paysource==4){//用户充值钱包  moneyService
		url = "${hdpath}/wxpay/doRefund";
		refundState = 4;
	}
	$.bootstrapLoading.start({ loadingTips: "正在退款，请稍后..." });
	$.ajax({
		url : url,
		data : {
			id : orderId,
			refundState : refundState,
			pwd : refundpwd,
			utype : utype,
			wolfkey : wolfkey,
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
				alert("退款异常失败");
			}
		},//返回数据填充
		complete: function () {
            $.bootstrapLoading.end();
        }
	});
});


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
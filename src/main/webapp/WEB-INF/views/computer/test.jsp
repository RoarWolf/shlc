<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>详细信息记录</title>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script> 
<script type="text/javascript" src="${hdpath}/js/jquery-ui.js"></script>
<link rel="stylesheet" href="${hdpath}/css/admin.css">
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link rel="stylesheet" href="${hdpath}/css/bootstrap.min.css">
<link rel="stylesheet" href="${hdpath}/hdfile/css/toastr.min.css">
<script src="${hdpath}/hdfile/js/toastr.min.js"></script>

<style>
	ol {
		padding: 0;
		margin: 0;
	}
	html, html>body{
	width: 100%;
	height: 100%;
	}
	body {
	 position:relative;
	}
	.admin {
	    /*background: #f8f8f8;*/
	    background-color: #fff;
	    position: absolute;
	    border-left: solid 1px #b5cfd9;
	    right: 0;
	    bottom: 0;
	    top: 110px;
	    left: 180px;
	    padding: 15px;
	    padding-right: 0px;
	    padding-bottom: 0px;
	    overflow: auto;
	    border-top: 1px solid #b5cfd9;
	    padding-right: 15px;
	    font-size: 13px;
	    color: #666;
	}
	#time {
		display: inline-block;
		float: right;
		line-height:40px;
	}
	.table {
		margin-bottom: 0;
	}
	.table td {
		vertical-align: middle !important;
	}
	.tableDiv1::after , .tableDiv2::after ,.tableMidDiv::after,
	.arguDiv::after,.modelDiv::after,.defaultTemDiv::after,.modelDiv::after{
		content: '';
		display: block;
		height: 65px;
		width: 100%;
		background-color: transparent;
	}
	.admin table td {
		background-color: #F5F7FA;
	}
	.admin thead td{
		background-color: #C8EFD4;
	}
	
	.admin button {
		padding: 4px 12px;
		font-size: 13px;
	}
	.divTop .portNum {
		padding: 10px 0;
		border-bottom: 1px solid #ddd;
		border-top: 1px solid #ddd;
	}
	.divTop .portNum >span {
		margin-left: 5%;
	}
	.divTop .portNum >span>span {
		color: #6BD089;
	}
	.table-striped tbody tr:nth-of-type(odd){
		background-color: rgba(226,233,229,.5);
	}
	#tableTop {
		color: #666;
		/*border: 1px solid #ccc;*/
		/*margin: 25px 0;*/
	}
	.admin thead td{
		height: 40px;
		text-align: center;
	}
	.tableMidDiv .left tbody td,#arguTable tbody td {
		height: 40px;
	}
	.admin td {
		padding: 5px;
		vertical-align: middle;
		text-align: center;
	}
	#tableTop button {
		font-size: 13px;
		padding: 4px 12px;
	}
	/*#tableMid {
		margin: 25px 0;
	}*/
	#tableMid td {
		text-align: center;
	
	}
	#tableMid input {
		background-color: transparent;
		border: 1px solid #999;
		border-radius: 5px;
		text-align: center;
	}
	#tableMid .firstTd td {
		background-color: #f5f7fa;
	}
	.tableMidDiv {
		width: 100%;

	}
	.tableMidDiv .left {
		float: left;
		width: 45%;
	}
	.tableMidDiv .right {
		float: right;
		width: 45%;
	}
	.form-control {
		display: inline-block;
		width: 50%;
		background-color: transparent;
		height: 30px;
		font-size: 13px;

	}

	.modelDiv h3 , .arguDiv h3,.tableDiv2 h3, .tableDiv1 h3, .defaultTemDiv h3{
		font-size: 18px;
		color: #333;
		text-align: center;
		line-height: 50px;
		margin-bottom: 0;
	}
	.arguDiv td {
		height: 42px;
	}
	.arguDiv input {
		background-color: transparent;
	    border: 1px solid #999;
	    border-radius: 5px;
	    text-align: center;
	}
	.modelDiv::after {
		height: 25px !important;
	}
	
	/*===============*/
	
	.defaultTemDiv .temList {
		background-color: #f8f8f8;
		/*border: 2px solid #35c5dc52;*/
		border: 1px solid #ccc;
	}
	.defaultTemDiv  button{
		font-size: 13px !important;
   		padding: 4px 12px !important;
	}
	.defaultTemDiv .temDiv table {
		width: 100%;
		margin: 0;
	}
	.defaultTemDiv .temList .faTem {
		border-top: 1px solid #ccc;
	}
	.defaultTemDiv .temList .faTem td {
		background: #f5f7fa !important;
	}
	.defaultTemDiv .temList table td {
		text-align: center;
		vertical-align: middle !important;
	}
	.defaultTemDiv .temList table td.left_td{
		text-align: left;
		font-weight: 400;
		line-height: 28px;
	}
	.defaultTemDiv .temList table td.left_td>span {
		display: inline-block;
		min-width: 30%;
		padding-right: 3%;
	}
	.defaultTemDiv .temList table td.left_td>span>span {
		margin-left: 10px;
	}
	.defaultTemDiv .temList table.faTem  {
		background-color: rgba(0,211,255,0.06);
		border-bottom: transparent;
	}
	.defaultTemDiv .temList table.faTem td {
		border: none;
	}
	.defaultTemDiv .temList table .title {
		font-weight: 600;
	}
	.defaultTemDiv .temList table .setWidth{
		width: 150px;
	}
	.defaultTemDiv .temList  .chTem thead {
		background-color: #f5f7fa;
	}
	.defaultTemDiv .temList .lastTd {
		position: relative;
	}
	.defaultTemDiv .temList .lastTd button.goToTemPage {
		display: block;
		float: left;
		margin-left: 25%;
	}
	.defaultTemDiv .temList .lastTd button.temForMoreDevice {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		-webkit-transform: translateX(-50%);
		-moz-transform: translateX(-50%);
		-o-transform: translateX(-50%);
		-ms-transform: translateX(-50%);
	}
	.defaultTemDiv .temList .lastTd button.addBut {
		float: right;
		margin-right: 25%;
	}
	.defaultTemDiv .temList .lastTd {
		position: relative;
	}
	
	/*=================*/
	
	.span-green {
		color: #22B14C !important;
	}
	.span-red {
		color: #F47378 !important;
	}

	/*弹框*/

.tem-mask1,.tem-mask2 {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, .3);
    z-index: 99;
    display: none;
}

.list-center1 ,.list-center2 {
    z-index: 9;
    /*width: 30%;*/
    width: 410px;
    background-color: #fff;
    border-radius: 10px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
}
.list-center1,.list-center2 {
    padding: 15px;
}
.list-center1 h3 ,.list-center2 h3 {
    margin: 0 auto;
    font-size: 15px;
    font-weight: 600;
    text-align: center;
    margin-top: 15px;
    padding-bottom: 15px;
    border-bottom: 1px solid #DCDFE6;
}
.list-center1 form,.list-center2 form {
    font-size: 14px;
    color: #666;
    padding-top: 10px;
}
.list-center1 form .inp ,.list-center2 form .inp {
    margin-bottom: 15px;
}
.list-center1 form input , .list-center2 form input {
    margin-top: 5px;
    padding-left: 20px;
    height: 38px;
    border-radius: 6px;
    width: 80%;
    margin-bottom: 0;
    font-size: 12px;
    box-shadow: transparent;
}
.list-center1 form input::-webkit-input-placeholder , .list-center2 form input::-webkit-input-placeholder{
    color: #c0c0c0;
    font-size: 12px;
}    /* 娴ｈ法鏁ebkit閸愬懏鐗抽惃鍕セ鐟欏牆娅� */
.list-center1 form input:-moz-placeholder , .list-center2 form input:-moz-placeholder{
    color: #c0c0c0;
    font-size: 12px;
}                  /* Firefox閻楀牊婀�4-18 */
.list-center1 form input::-moz-placeholder , .list-center2 form input::-moz-placeholder{
    color: #c0c0c0;
    font-size: 12px;
}                  /* Firefox閻楀牊婀�19+ */
.list-center1 form input:-ms-input-placeholder , .list-center2 form input:-ms-input-placeholder{
    color: #c0c0c0;
    font-size: 12px;
}           /* IE濞村繗顫嶉崳锟� */
.list-center1 form .inp , .list-center2 form .inp{
    position: relative;
}
.list-center1 form .btn , .list-center2 form .btn {
    position: absolute;
    right: 7%;
    bottom: 5px;
    color: #333;
}
.list-center1 .bottom , .list-center2 .bottom{
    width: 100%;
    margin: 20px 0 0 0;
    position: relative;
   	height: 30px;
}
.list-center1 .bottom .mui-btn-success , .list-center2 .bottom .mui-btn-success {
    font-size: 14px;
    padding: 3px 6px;
    margin-right: 5px;
}
.list-center1 .close2 , .list-center2 .close2 {
    position: absolute;
    right: 70px;
}
.list-center1 .submit , .list-center2 .submit{
	position: absolute;
	 right: 0px;
}
.list-center2 form .inp.radio-inp {
    display: flex;
    /*justify-content: space-between;*/
}
.list-center2 form .inp.radio-inp h5 {
    color: #666;
    margin-right: 18px;

}
.list-center2 form .inp.radio-inp label {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #666;
}
.list-center2 form .inp input[name="isRef"],
.list-center2 form .inp input[name="isWalletPay"]{
    width: 18px;
    height: 18px;
    margin: 0;
    margin-left: 10px;
}
.list-center2 h5 {
	font-size: 14px;
}



.popover {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, .6);
    z-index: 100;
    max-width: none;
    display: none; 
}
.popover ul li label {
    display: flex;
    align-items: center;
}
.popover ul {
    overflow: hidden;
    width:280px;
    z-index: 12;
    background-color: #fff;
    border-radius: 10px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    font-size: 14px;
    padding-left: 20px;
}
.popover ul li {
	/*padding: */
	line-height: 40px;
	border-bottom: 1px solid #ccc;
}
.popover ul input {
    width: 18px;
    height: 18px;
    margin-right: 10px;
}
.popover label {
  color: #666;
}
.popover .firstLi{
	height: 50px;
	line-height: 50px
}
.popover .lastButton {
	height: 50px;
	padding-top: 10px;
	padding-bottom: 10px;
}
.popover .lastButton button:first-child{
    float: left;
    position: initial;
    -webkit-transform: none;
    transform:none;
    padding: 4px 6px;
    font-size: 12px;
    margin-left: 15%;
}
.popover .lastButton button:last-child{
    float: right;
    position: initial;
    -webkit-transform: none;
    transform:none;
    padding: 4px 6px;
    font-size: 12px;
    margin-right: 15%;
}
.defaultTemDiv .temList .temList {
	border: none !important;
}
.defaultTemDiv .temList.gradTem {
	border-width: 2px !important;
}
.defaultTemDiv  .gradTem .gradTitle {
    border-bottom: 1px solid #ddd !important;
    font-weight: 700;
}

.alertTemForMoreDevice {
	position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, .3);
    z-index: 99;
    display: none;
}
.alertTemForMoreDevice .alertDiv {
	width: 600px;
	max-height: 800px;
	background: #fff;
	border-radius: 6px;
	padding: 20px;
	position: absolute;
	left: 50%;
	top: 50%;
	transform: translate(-50%,-50%);
	-webkit-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	overflow-y: auto;
}
.alertTemForMoreDevice .alertDiv input {
	zoom: 140%;
}
.alertTemForMoreDevice .alertDiv .table>tbody>tr>td,
.alertTemForMoreDevice .alertDiv .table>thead>tr>td
{
	border: none;
	height: 48px;
	line-heoght: 48px;
	font-size: 14px;
}
.alertTemForMoreDevice .alertDiv h3 {
	font-size: 20px;
	text-align: center;
	margin: 5px 0 30px;
}
.alertTemForMoreDevice .bottom {
	margin-top : 30px;
}
.alertTemForMoreDevice .bottom .moDeClose {
	float: left;
	margin-left: 20%;
	padding: 6px 20px;
}
.alertTemForMoreDevice .bottom .moDeConfirm {
	float: right;
	margin-right: 20%;
	padding: 6px 20px;
}
/* 设置滚动条的样式 */.alertTemForMoreDevice .alertDiv::-webkit-scrollbar {    
width: 12px;
}/* 滚动槽 */.alertTemForMoreDevice .alertDiv::-webkit-scrollbar-track {    
-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);    
border-radius: 10px;
}/* 滚动条滑块 */.alertTemForMoreDevice .alertDiv::-webkit-scrollbar-thumb {    
border-radius: 10px;    
background: rgba(0,0,0,0.1);    
-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5);
}
.alertTemForMoreDevice .alertDiv::-webkit-scrollbar-thumb:window-inactive {    
background: rgba(255,0,0,0.4);
}

</style>
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
	  <li><a href="javascript:void(0)" target="right" class="icon-home">设备详细信息记录</a></li>
	</ul>
</div>
 <div class="admin">
 	<div class="panel admin-panel" id="adminPanel">
  	<div class="container-fulid" >
	<input type="hidden" value="${code}" id="code">
		<!--  新添加的  -->
		<div class="divTop">
			<div class="portNum" style="margin-top: 10px;">
				<c:if test="${ username == '0' }">
					<button type="button" class="btn btn-danger"
					  style="margin-left: 20px;"
					  onclick="pwdbinding('${code}','0')"
					  >
					强制绑定进行选择
					</button>
					<%-- <a href="${hdpath }/equipment/pcbindequ?code=${code}" class="btn btn-danger">强制绑定进行选择</a> --%>
				</c:if>
				<c:if test="${ username != '0' }">
					<button type="button" class="btn btn-danger" style="margin-left: 20px;"
					onclick="pwdbinding('${code}','1')"
					>强制解绑</button>
					<%-- <a href="${hdpath }/equipment/pcunbound?code=${code}" class="btn btn-danger">强制解绑</a> --%>
				</c:if>
				
				<span>绑定状态： 
					<span>${username == '0' ? "未绑定" : "已绑定"}</span>
				</span>
				<span>绑定用户： 
					<span>${username == '0' ? "无" : username}</span>
				</span>
				<span>设备名：
					<span>${code == null ? '---' : code }</span>
				</span>
				<span>所属小区：
					<span>---</span>
				</span>
				<span>电话：
					<span>---</span>
				</span>
				<span style="display: none;"><a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${merid}"><button>模板查看</button></a></span>
			</div>
			<div class="modelDiv">
				<h3>模块信息（硬件，软件，IMEI, +CCID）</h3>
				<table class="table table-striped table-hover table-bordered" id="arguTable">
					<thead>
						<tr>
							<td>设备编号</td>
							<td>ccid</td>
							<td>imei</td>
							<td>硬件版本</td>
							<td>硬件版本号</td>
							<td>软件版本号</td>
							<td>信号强度</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${equipment.code }</td>
							<td>${equipment.ccid }</td>
							<td>${equipment.imei }</td>
							<td>${equipment.hardversion }</td>
							<td>${equipment.hardversionnum }</td>
							<td>${equipment.softversionnum }</td>
							<td>${equipment.csq }</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="modelDiv">
				<h3>位置信息</h3>
				<table class="table table-striped table-hover table-bordered" id="arguTable">
					<thead>
						<tr>
							<td>经度</td>
							<td>纬度</td>
							<td>查看</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${equipment.lon == null ? '---' : equipment.lon }</td>
							<td>${equipment.lat == null ? '---' : equipment.lat }</td>
							<td><a style="display:inline-block;" href="${hdpath}/pcequipment/allowlookmapinfo?lon=${equipment.lon}&lat=${equipment.lat}"><button type="button" class="btn btn-info">查看位置</button></a></td>
							<td><button type="button" class="btn btn-info getPosition">获取</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		
		</div>
		<c:if test="${equipment.hardversion != '03' && equipment.hardversion != '04'}" >
		<!-- 充电模板 tem1 开始 -->
		<div class="defaultTemDiv tem1">
			<h3>充电选中模板</h3>
			<c:if test="${temp != null}">
				<div class="temList">
					<table class="table  faTem">
							<thead>
								<tr class="title">
									<td class="left_td">
										<span><b>模板名称:</b> <span class="temNmae">${temp.name }</span></span>
										<span><b>品牌名称:</b> <span class="brandName">${temp.remark }</span></span>
										<span><b>售后电话:</b> <span class="telephone">${temp.common1 }</span></span>
									</td>
									
									<td >操作</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="left_td">		
										<span>
											<b>是否支持退费:</b>
											<c:if test="${temp.permit==1}"><span class="isRef span-green">是</span> 
			                    				<c:if test="${temp.common2==1}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
			                    				<c:if test="${temp.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
			                    				<c:if test="${temp.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
			                    			</c:if> 
			                    			<c:if test="${temp.permit==2}"><span class="isRef span-red">否</span><span data-val=''></span></c:if>
										</span>
									
										<span>
											<b>是否钱包强制支付:</b> 
											<c:if test="${temp.walletpay==1}"><span class="isWalletPay span-green">是</span></c:if>
                    						<c:if test="${temp.walletpay==2}"><span class="isWalletPay span-red">否</span></c:if>
											<span></span>
										</span>
									</td>
									<td class="setWidth">
										<button type="button" class="btn btn-info tem-title-edit" data-id="${temp.id }" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
									
								</tr>
							</tbody>
						
					</table>
					<table class="table table-bordered chTem">
						<thead>
							<tr class="title">
								<td>显示名称</td>
								<td>充电价格</td>
								<td>充电时间</td>
								<td>消耗电量</td>
								<td>操作</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${temp.gather}" var="tempson">
								<tr>
									<td>${tempson.name}</td>
									<td><span>${tempson.money}</span>元</td>
									<td><span>${tempson.chargeTime}</span>分钟</td>
									<td><span>${tempson.chargeQuantity/100}</span>度</td>
									<td class="setWidth">
			                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
								</tr>
		                    </c:forEach>
							<tr>
								<td colspan="6" class="lastTd">
									<c:if test="${uid == null}" >
										<a href="javascript:void(0);"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${uid != null}" >
										<a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${uid}"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${merid !=0 && merid !=null}">
										<button type="button" class="btn btn-info temForMoreDevice"  
										data-uid="${uid}" 
										data-hardversion="${equipment.hardversion}" 
										data-merid="${merid}"
										data-id="${temp.id }"
										>此模板是否复用更多设备</button>
									</c:if>
									<button type="button" class="btn btn-info addBut" data-id="${temp.id }"  <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >添加模板</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</c:if>
			<c:if test="${temp == null}">
				 <c:forEach items="${templist}" var="temggather">
					 <c:if test="${temggather.rank == 1 }">
		   				<div class="temList gradTem">
		   			 </c:if>
		   			 <table class="table  faTem">
							<thead>
								<tr>
									<td class="gradTitle" colspan="3">
										<c:if test="${temggather.rank == 1 }">	
											<div class="temGradName">分等级模板一</div>
										</c:if>
										<c:if test="${temggather.rank == 2 }">	
											<div class="temGradName">分等级模板二</div>
										</c:if>
										<c:if test="${temggather.rank == 3 }">	
											<div class="temGradName">分等级模板三</div>
										</c:if>
									</td>
								</tr>
								<tr class="title">
									<td class="left_td">
										<span><b>模板名称:</b> <span class="temNmae">${temggather.name}</span></span>
										<span><b>品牌名称:</b> <span class="brandName">${temggather.remark}</span></span>
										<span><b>售后电话：</b> <span class="telephone">${temggather.common1}</span></span>
									</td>
									
									<td>操作</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="left_td">
										<span>
										<b>是否支持退费:</b> 
											<c:if test="${temggather.permit==1}">
											<span class="isRef span-green">是</span>
												<c:if test="${temggather.common2==1}"><span data-val="1">(退费标准：时间和电量最小)</span></c:if>
			                    				<c:if test="${temggather.common2==2}"><span data-val="2">(退费标准：根据时间)</span></c:if>
			                    				<c:if test="${temggather.common2==3}"><span data-val="3">(退费标准：根据电量)</span></c:if>
											</c:if>
											 <c:if test="${temggather.permit==2}"><span class="isRef span-red">否</span><span data-val=""></span></c:if>
										<!--<span class="isRef span-green">是</span> <span data-val="2">(退费标准：根据时间)</span>  -->
										</span>
										<span><b>是否钱包强制支付:</b> 
											<c:if test="${temggather.walletpay==1}"><span class="isWalletPay span-green">是</span></c:if>
			                    			<c:if test="${temggather.walletpay==2}"><span class="isWalletPay span-red">否</span></c:if></p>
										</span>
									</td>
									<td class="setWidth">
										<button type="button" class="btn btn-info tem-title-edit" data-id="${temggather.id}">编辑</button>
									</td>
								</tr>
							</tbody>
						
					</table>
					<table class="table table-bordered chTem">
						<thead>
							<tr class="title">
								<td>显示名称</td>
								<td>充电价格</td>
								<td>充电时间</td>
								<td>消耗电量</td>
								<td>操作</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${temggather.gather}" var="gardChildTem">
				                <tr>
									<td>${gardChildTem.name}</td>
									<td><span>${gardChildTem.money}</span>元</td>
									<td><span>${gardChildTem.chargeTime}</span>分钟</td>
									<td><span>${gardChildTem.chargeQuantity/100}</span>度</td>
									<td class="setWidth">
										<button type="button" class="btn btn-info tem-edit" data-id="${gardChildTem.id}">编辑</button>
									</td>
								</tr>
				             </c:forEach>
							<tr>	
								<td colspan="6" class="lastTd">
								<c:if test="${temggather.rank == 3 }">
									<c:if test="${uid == null}" >
										<a href="javascript:void(0);"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${uid != null}" >
										<a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${uid}"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
								</c:if>
									<button type="button" class="btn btn-info addBut" data-id="${temggather.id}">添加模板</button>
								</td>
							</tr>
						</tbody>
					</table>
				 </c:forEach>
			</c:if>
			
		</div>
		<!-- 充电模板 tem1 结束 -->
		</c:if>
		<c:if test="${equipment.hardversion == '04' }" >
		<!-- 离线卡模板 tem2 开始-->
		<div class="defaultTemDiv tem2">
			<h3>离线卡选中模板</h3>
			<div class="temList">
					<table class="table  faTem">
							<thead>
								<tr class="title">
									<td class="left_td">
										<span><b>模板名称:</b> <span class="temNmae">${temp.name }</span></span>
										<span><b>品牌名称:</b> <span class="brandName">${temp.remark }</span></span>
									</td>
									<td >操作</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="left_td">		
										<span><b>售后电话：</b> <span class="telephone">${temp.common1 }</span></span>	
									</td>
									<td class="setWidth">
										<button type="button" class="btn btn-info tem-title-edit" data-id="${temp.id }" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
									
								</tr>
							</tbody>
						
					</table>
					<table class="table table-bordered chTem">
						<thead>
							<tr class="title">
								<td>显示名称</td>
								<td>付款金额</td>
								<td>充卡金额</td>
								<td>操作</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${temp.gather}" var="tempson">
								<tr>
									<td>${tempson.name}</td>
									<td><span>${tempson.money}</span>元</td>
									<td><span>${tempson.remark}</span>元</td>
									<td class="setWidth">
			                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
								</tr>
		                    </c:forEach>
							<tr>
								<td colspan="6" class="lastTd">
									<c:if test="${uid == null}" >
										<a href="javascript:void(0);"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${uid != null}" >
										<a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${uid}"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${merid !=0 && merid !=null}">
										<button type="button" class="btn btn-info temForMoreDevice"  
										data-uid="${uid}" 
										data-hardversion="${equipment.hardversion}" 
										data-merid="${merid}"
										data-id="${temp.id }"
										>此模板是否复用更多设备</button>
									</c:if>
									
									<button type="button" class="btn btn-info addBut" data-id="${temp.id }"  <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >添加模板</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
		</div>
		
		<!-- 离线卡模板 tem2 结束-->
		</c:if>
		<c:if test="${equipment.hardversion == '03' }" >
		<!-- 模拟投币模板 tem3 开始-->
		<div class="defaultTemDiv tem3">
			<h3>模拟投币选中模板</h3>
			<div class="temList">
					<table class="table  faTem">
							<thead>
								<tr class="title">
									<td class="left_td">
										<span><b>模板名称:</b> <span class="temNmae">${temp.name }</span></span>
										<span><b>品牌名称:</b> <span class="brandName">${temp.remark }</span></span>
									</td>
									<td >操作</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="left_td">		
										<span><b>售后电话：</b> <span class="telephone">${temp.common1 }</span></span>	
									</td>
									<td class="setWidth">
										<button type="button" class="btn btn-info tem-title-edit" data-id="${temp.id }" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
									
								</tr>
							</tbody>
						
					</table>
					<table class="table table-bordered chTem">
						<thead>
							<tr class="title">
								<td>显示名称</td>
								<td>投币个数</td>
								<td>付款金额</td>
								<td>操作</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${temp.gather}" var="tempson">
								<tr>
									<td>${tempson.name}</td>
									<td><span><fmt:formatNumber value="${tempson.remark}" pattern="0" /></span>个</td>
									<td><span>${tempson.money}</span>元</td>
									<td class="setWidth">
			                            <button type="button" class="btn btn-info tem-edit" data-id="${tempson.id}" <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >编辑</button>
									</td>
								</tr>
		                    </c:forEach>
							<tr>
								<td colspan="6" class="lastTd">
									<c:if test="${uid == null}" >
										<a href="javascript:void(0);"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<c:if test="${uid != null}" >
										<a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${uid}"   style="color: #fff; text-decoration: none;">
											<button type="button" class="btn btn-info goToTemPage"  data-uid="${uid}" >查看更多</button>
										</a>
									</c:if>
									<!-- 
									<a href="${hdpath}/pcequipment/looktemp?code=${code}&merid=${uid}"  style="color: #fff; text-decoration: none;">
										<button type="button" class="btn btn-info goToTemPage" data-uid="${uid}">查看更多</button>
									</a>
									 -->
									<c:if test="${merid !=0 && merid !=null}">
										<button type="button" class="btn btn-info temForMoreDevice"  
										data-uid="${uid}" 
										data-hardversion="${equipment.hardversion}" 
										data-merid="${merid}"
										data-id="${temp.id }"
										>此模板是否复用更多设备</button>
									</c:if>
									
									<button type="button" class="btn btn-info addBut" data-id="${temp.id }"  <c:if test="${merid==0 || merid==null}">disabled="disabled"</c:if> >添加模板</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
		</div>
		<!-- 离线卡模板 tem3 结束-->
		</c:if>
		<div class="tableDiv1">
			<h3>端口状态</h3>
			<table class="table table-striped table-hover table-bordered" id="tableTop">
				 <thead>
				 	<tr>
				 		<td>端口号</td>
				 		<td>端口状态	</td>
				 		<td>充电时间（分钟）	</td>
				 		<td>充电功率（W）	</td>
				 		<td>剩余电量（度）</td>
				 		<td>可回收余额</td>
				 		<td>实时更新</td>
				 		<td>锁定</td>
				 		<td>解锁</td>
				 		<td>日志</td>
				 	</tr>
				 </thead>
				  <tbody>
					<c:forEach items="${allPortStatusList }" var="codeport">
					 	<tr>
					 		<td>${codeport.port }</td>
					 		<td id="portstate${codeport.port }" >${codeport.portStatus == 2 ? "使用" : codeport.portStatus == 1 ? "空闲" : "故障" }</td>
					 		<td id="porttime${codeport.port }">${codeport.time }</td>
					 		<td id="portpower${codeport.port }">${codeport.power }</td>
					 		<td id="portelec${codeport.port }">${codeport.elec / 100 }</td>
					 		<td id="recylce${codeport.port }">${codeport.surp }</td>
					 		<td><button type="button" class="btn btn-success" id="queryport${codeport.port }" value="${codeport.port }">更新</button></td>
					 		<td><button type="button" class="btn btn-info" id="lock${codeport.port }"  value="0" >锁定</button></td>
					 		<td><button type="button" class="btn btn-info" id="debloack${codeport.port }" value="1" >解锁</button></td>
					 		<td><a href="${hdpath}/pcequipment/selectEquipmentLog?parm=0&equipmentnum=${code}&port=${codeport.port}">查看</a></td>
					 	</tr>
					 </c:forEach>
				 </tbody>
			</table>
		</div>
		 <!-- endding!!! -->
		
		 <div class="tableDiv2">
		 <h3>远程充电</h3>
			<table class="table table-striped table-hover table-bordered" id="tableMid">
				<thead>
					<tr class="firstTd">
						<td colspan="3">
							<button type="button" class="btn btn-info" id="queryfree" value="2">查询空闲端口</button>
						</td>
						<td colspan="4">
							<button type="button" class="btn btn-info" id="readall" value="15">查询端口状态</button>
						</td>
					</tr>
					<tr>
						<td>端口号</td>
						<td>端口状态</td>
						<td>是否空闲</td>
						<td>充电时间（分钟）</td>
						<td>充电电量 （度）</td>
						<td>操作</td>
						<td>远程断电</td>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${allPortStatusList }" var="codeport">
					<tr>
						<td>${codeport.port }</td>
						<td id="payportstate${codeport.port }">${codeport.portStatus == 2 ? "使用" : codeport.portStatus == 1 ? "空闲" : "故障" }</td>
						<td id="isfreeport${codeport.port }">${codeport.portStatus == 1 ? "是" : "否" }</td>
						<td><input type="text" value="240" id="chargetime${codeport.port }" /></td>
						<td><input type="text" value="1.0" id="chargeelec${codeport.port }" /></td>
						<td><button type="button" class="btn btn-info" id="payport${codeport.port }">开始</button></td>
						<td><button type="button" class="btn btn-info" id="stopport${codeport.port }">断电</button></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
			
		 <!-- endding!!! -->
		 <div class="tableMidDiv clearfix">
			<div class="left">
				<table class="table table-striped table-hover table-bordered" id="tableMid">
					<thead>
						<tr class="firstTd">
							<td colspan="2">
								<button type="button" id="querytakemoney" class="btn btn-info" value="7">查询消费总金额</button>
							</td>
						</tr>
						<tr>
							<td>类型</td>
							<td>金额</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>刷卡总金额</td>
							<td id="coinmoney"></td>
						</tr>
						<tr>
							<td>投币总金额</td>
							<td id="icmoney"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="right">
				<table class="table table-striped table-hover table-bordered" id="tableMid">
					<thead>
						<tr class="firstTd">
							<td colspan="3">
								设置IC卡、投币器是否可用
							</td>
						</tr>
						<tr>
							<td>类型</td>
							<td>解锁</td>
							<td>锁定</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>投币器</td>
							<td colspan="2">
								<select class="form-control input-sm" id="coinstate">
									<option value="0">锁定</option>
									<option value="1">解锁</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>IC卡</td>
							<td colspan="2">
								<select class="form-control input-sm"  id="icstate">
									<option value="0">锁定</option>
									<option value="1">解锁</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><button class="btn btn-info" id="seticandcoin">设置</button></td>
							<td colspan="2" id="seticcoininfo">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="arguDiv"><!-- 系统参数 -->
			<h3>系统参数表</h3>
			<form id="sysParamForm">
			<input type="hidden" id="elec_time_first"
						name="elecTimeFirst" value="${sysparam.elecTimeFirst == null ? 0 : sysparam.elecTimeFirst}">
			<input type="hidden" id="code" name="code" value="${code }">
			<table class="table table-striped table-hover table-bordered" id="arguTable">
				<thead>
					<tr>
						<td>含义</td>
						<td>数值</td>
						<td>单位</td>
						<td>最大值</td>
						<td>最小值</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>设置投币充电时间(单位为分钟)</td>
						<td><input type="text" id="coin_min" name="coinMin" value="${sysparam.coinMin == null ? 240 : sysparam.coinMin}"></td>
						<td>分钟</td>
						<td>999</td>
						<td>0</td>
					</tr>
					<tr>
						<td>设置刷卡充电时间 (单位为分钟)</td>
						<td><input type="text" id="card_min" name="cardMin" value="${sysparam.cardMin == null ? 240 : sysparam.cardMin}"></td>
						<td>分钟</td>
						<td>999</td>
						<td>0</td>
					</tr>
					<tr>
						<td>设置单次投币最大用电量(单位为度,KWH)</td>
						<td><input type="text" id="coin_elec" name="coinElec" value="${sysparam.coinElec == null ? 1 : sysparam.coinElec}"></td>
						<td>0.1度</td>
						<td>15</td>
						<td>0.1</td>
					</tr>
					<tr>
						<td>设置单次刷卡最大用电量(单位为度,KWH)</td>
						<td><input  type="text" id="card_elec" name="cardElec" value="${sysparam.cardElec == null ? 1 : sysparam.cardElec}"></td>
						<td>0.1度</td>
						<td>15</td>
						<td>0.1</td>
					</tr>
					<tr>
						<td>设置刷卡扣费金额(单位为元)</td>
						<td><input  type="text" id="cst" name="cst" value="${sysparam.cst == null ? 1 : sysparam.cst}"></td>
						<td>角</td>
						<td>15</td>
						<td>0.1</td>
					</tr>
					<tr>
						<td>设置第一档最大充电功率（最大功率以机器支持为准）</td>
						<td><input  type="text" id="power_max_1" name="powerMax1" value="${sysparam.powerMax1 == null ? 200 : sysparam.powerMax1}"></td>
						<td>瓦</td>
						<td>3500</td>
						<td>50</td>
					</tr>
					<tr>
						<td>设置第二档最大充电功率（最大功率以机器支持为准）</td>
						<td><input  type="text" id="power_max_2"
					name="powerMax2" value="${sysparam.powerMax2 == null ? 400 : sysparam.powerMax2}"></td>
						<td>瓦</td>
						<td>3500</td>
						<td>50</td>
					</tr>
					<tr>
						<td>设置第三档最大充电功率（最大功率以机器支持为准）</td>
						<td><input  type="text" id="power_max_3"
					name="powerMax3" value="${sysparam.powerMax3 == null ? 600 : sysparam.powerMax3}"></td>
						<td>瓦</td>
						<td>3500</td>
						<td>50</td>
					</tr>
					<tr>
						<td>设置第四档最大充电功率（最大功率以机器支持为准）</td>
						<td><input  type="text" id="power_max_4"
					name="powerMax4" value="${sysparam.powerMax4 == null ? 800 : sysparam.powerMax4}"></td>
						<td>瓦</td>
						<td>3500</td>
						<td>50</td>
					</tr>
					<tr>
						<td>设置第二档充电时间百分比</td>
						<td><input type="text" id="power_2_tim"
					name="powerTim2" value="${sysparam.powerTim2 == null ? 75 : sysparam.powerTim2}"></td>
						<td>%</td>
						<td>100</td>
						<td>1</td>
					</tr>
					<tr>
						<td>设置第三档充电时间百分比</td>
						<td><input type="text" id="power_3_tim"
					name="powerTim3" value="${sysparam.powerTim3 == null ? 50 : sysparam.powerTim3}"></td>
						<td>%</td>
						<td>100</td>
						<td>1</td>
					</tr>
					<tr>
						<td>设置第四档充电时间百分比</td>
						<td><input type="text" id="power_4_tim"
					name="powerTim4" value="${sysparam.powerTim4 == null ? 25 : sysparam.powerTim4}"></td>
						<td>%</td>
						<td>100</td>
						<td>1</td>
					</tr>
					<tr>
						<td>是否支持余额回收（1为支持 0为不支持)</td>
						<td><input type="text" id="sp_rec_mon"
					name="spRecMon" value="${sysparam.spRecMon == null ? 1 : sysparam.spRecMon}"></td>
						<td>无</td>
						<td>1</td>
						<td>0</td>
					</tr>
					<tr>
						<td>是否支持断电自停（1为支持 0为不支持)</td>
						<td><input type="text" id="sp_full_empty"
					name="spFullEmpty" value="${sysparam.spFullEmpty == null ? 1 : sysparam.spFullEmpty}"></td>
						<td>无</td>
						<td>1</td>
						<td>0</td>
					</tr>
					<tr>
						<td>设置充电器最大浮充功率 （功率为瓦，当充电器功率低于这个值的话，可视为充电器已充满)</td>
						<td><input type="text" id="full_power_min"
					name="fullPowerMin" value="${sysparam.fullPowerMin == null ? 30 : sysparam.fullPowerMin}"></td>
						<td>瓦</td>
						<td>200</td>
						<td>0</td>
					</tr>
					<tr>
						<td>设置浮充时间 （充电器充满变绿灯之后的，继续浮充时间，单位为分钟）</td>
						<td><input type="text" id="full_charge_time"
					name="fullChargeTime" value="${sysparam.fullChargeTime == null ? 120 : sysparam.fullChargeTime}"></td>
						<td>分钟</td>
						<td>240</td>
						<td>30</td>
					</tr>
					<tr>
						<td>是否初始显示电量 （此功能是否支持和设备相关）</td>
						<td>
							<input type="text" id="elec_time_first1"
						name="elecTimeFirst1" value="${sysparam.elecTimeFirst == null ? 0 : sysparam.elecTimeFirst == -1 ? '--' : sysparam.elecTimeFirst}">
						</td>
						<td>无</td>
						<td>1</td>
						<td>0</td>
					</tr>
					<tr>
						<td>
							<button class="btn btn-info" id="readsystem" value="30">读取参数</button>
						</td>
						<td colspan="4">
							<button class="btn btn-info" id="submitbtn">保存参数</button>
							<span id="isaccess"></span>
						</td>
					</tr>
				</tbody>
			</table>
			</form>
		</div>
		
		<!-- 弹框 -->
	   <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	        <div class="modal-dialog" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="myModalLabel"></h4>
	                </div>
	                <div class="modal-body">
	                    <div class="form-group">
	                        <label for="txt_departmentname">密码</label>
	                        <input type="hidden" name="testnum" class="form-control" Readonly="Readonly">
	                        <input type="hidden" name="code" class="form-control" Readonly="Readonly">
	                        <input type="password" name="pwdbinding" class="form-control" placeholder="输入密码">
	                    </div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
	                    <button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    <!--  -->
	</div>
  </div>
</div>
<!-- 复用更多设备弹框 -->
<div class="alertTemForMoreDevice">
	<div class="alertDiv">
		<h3>选中设备使用此模板</h3>
		<table class="table table-striped">
			<thead>
				<tr>
					<td><input type="checkbox" id="checkAll" data-code="000123" /></td>
					<td>设备号</td>
					<td>设备名称</td>
				</tr>
			</thead>
			<tbody class="mfdBody">
				<!-- <tr>
					<td><input type="checkbox" data-code="000124" /></td>
					<td>000365</td>
					<td>测试名称</td>
				</tr> -->
			</<tbody>
		</table>
		<div class="bottom">
			<button type="button" class="btn moDeClose">取消</button>
			<button type="button" class="btn btn-info moDeConfirm">确认</button>
		</div>
	</div>
</div>

<!-- 复用更多设备弹框 -->
<div class="tem-mask1 tem1" >
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close1"></span>
            <h3>修改电子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name1">显示名称</label>
                    <br>
                    <input type="text" id="name1" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="parse1">充电价格</label>
                    <br>
                    <input type="text" id="parse1" name="parse" placeholder="请填写充电价格">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="time1">充电时间</label>
                    <br>
                    <input type="text" id="time1" name="time" placeholder="请填写充电时间">
                    <div class="btn">分钟</div>
                </div>
                <div class="inp">
                    <label for="power1">消耗电量</label>
                    <br>
                    <input type="text" id="power1" name="power" placeholder="请填写消耗电量">
                    <div class="btn">度</div>
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>

    <!-- 修改充电模板 -->

    <div class="tem-mask2 tem1" >
	    <div class="list-center2">
	            <span class="mui-icon mui-icon-closeempty close"></span>
	            <h3>修改充电模板</h3>
	            <form action="#">
	                <div class="inp">
	                    <label for="temNmae1">模板名称</label>
	                    <br>
	                    <input type="text" id="temNmae1" name="temNmae" placeholder="请填写模板名称">
	                </div>
	                <div class="inp">
	                    <label for="brandName1">品牌名称</label>
	                    <br>
	                    <input type="text" id="brandName1" name="brandName" placeholder="请填写品牌名称">
	                </div>
	                <div class="inp">
	                    <label for="telephone1">售后电话</label>
	                    <br>
	                    <input type="text" id="telephone1" name="telephone" placeholder="请填写售后电话">
	                </div>
	                <div class="inp radio-inp">
	                    <h5>是否支持退费</h5>
	                    <br>
	                    <label>
	                        <input type="radio" name="isRef" value="1" id="isRefInp">是
	                    </label>
	                     <span style="font-size: 12px; line-height: 24px; margin-left: 5px; color: #54C1F0;" id="spanList">(时间)</span>
	                    <label>
	                        <input type="radio" name="isRef" value="0">否
	                    </label>
	                </div>
	                <div class="inp radio-inp">
	                    <h5>是否钱包强制支付</h5>
	                    <br>
	                    <label>
	                        <input type="radio" name="isWalletPay" value="1">是
	                    </label>
	                    <label>
	                        <input type="radio" name="isWalletPay" value="0">否
	                    </label>
	                </div>
	            </form>
	            <div class="bottom">
	                <button type="button" class="btn btn-info close2">关闭</button>
	                <button type="button" class="btn btn-info submit">提交</button>
	            </div>
	            </div>
	        </div>
	     </div>


	<div  class="tem1 popover">
      <ul class="mui-table-view">
        <li class="mui-table-view-cell firstLi">
                   退费标准
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="1" id="refReg1" checked="checked">时间和电量最小
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="2" id="refReg2">根据时间
            </label>
        </li>
        <li class="mui-table-view-cell">
            <label>
                 <input type="radio" name="refReg" value="3" id="refReg3">根据电量
            </label>
        </li>
         <li class="mui-table-view-cell lastButton">
           <button type="button" class="btn btn-info" id="exitBut">关闭</button>
           <button type="button" class="btn btn-info" id="confirmBut">确定</button>
        </li>
      </ul>
    </div>
	<!-- 离线卡模板弹框开始 -->
	<div class="tem-mask1 tem2" >
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改离线子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name2">显示名称</label>
                    <br>
                    <input type="text" id="name2" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="parse2">付款金额</label>
                    <br>
                    <input type="text" id="parse2" name="parse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <label for="totalParse">充卡金额</label>
                    <br>
                    <input type="text" id="totalParse" name="totalParse" placeholder="请填写充卡金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>充卡金额：</span> 该金额为向卡内实际充值的金额（即用户卡中增加的金额）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info  submit">提交</button>
            </div>
        </div>
    </div>
	
	<div class="tem-mask2 tem2" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改离线模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temNmae2">模板名称</label>
                    <br>
                    <input type="text" id="temNmae2" name="temNmae" placeholder="请填写模板名称">
                </div>
                <div class="inp">
                    <label for="brandName2">品牌名称</label>
                    <br>
                    <input type="text" id="brandName2" name="brandName" placeholder="请填写品牌名称">
                </div>
                <div class="inp">
                    <label for="telephone2">售后电话</label>
                    <br>
                    <input type="text" id="telephone2" name="telephone" placeholder="请填写售后电话">
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info  close2">关闭</button>
                <button type="button" class="btn btn-info  submit">提交</button>
            </div>
        </div>
    </div>
	<!-- 离线卡弹框结束 -->
	<!-- tem3 投币模板开始 -->
	
		<div class="tem-mask2 tem3" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>新增投币模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temNmae">模板名称</label>
                    <br>
                    <input type="text" id="temNmae" name="temNmae" placeholder="请填写模板名称">
                </div>
                <div class="inp">
                    <label for="brandName">品牌名称</label>
                    <br>
                    <input type="text" id="brandName" name="brandName" placeholder="请填写品牌名称">
                </div>
                <div class="inp">
                    <label for="telephone">售后电话</label>
                    <br>
                    <input type="text" id="telephone" name="telephone" placeholder="请填写售后电话">
                </div>
            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>
    
    <div class="tem-mask1 tem3" style="display: none;">
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改投币子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name3">显示名称</label>
                    <br>
                    <input type="text" id="name3" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="coinNum3">投币个数</label>
                    <br>
                    <input type="text" id="coinNum3" name="coinNum" placeholder="请填写投币个数">
                    <div class="btn">个</div>
                </div>
                <div class="inp">
                    <label for="totalParse3">付款金额</label>
                    <br>
                    <input type="text" id="totalParse3" name="totalParse" placeholder="请填写付款金额">
                    <div class="btn">元</div>
                </div>
                <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>投币个数：</span> 指投币的数量（单位为元）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div>

            </form>
            <div class="bottom">
                <button type="button" class="btn btn-info close2">关闭</button>
                <button type="button" class="btn btn-info submit">提交</button>
            </div>
        </div>
    </div>
	
	<!-- tem3 投币模板结束 -->
	
</body>

<script type="text/javascript">
$(document).ready(function(){	
	$('#41'+' a').addClass('at');
	$('#41').parent().parent().parent().prev().removeClass("collapsed");
	$('#41').parent().parent().parent().prev().find('span').css('class', 'pull-right glyphicon glyphicon-chevron-toggle glyphicon-minus');
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	$('#41').parent().parent().parent().addClass("in");
	$('#41').parent().parent().parent().prev().attr("aria-expanded",true)
	
	/* 地图点击事件 */
	$(".getPosition").click(function() {
		var code = $('#code').val().trim();
		$.ajax({
			type : "post",
			url : '${hdpath }/getLocationBySendData',
			dataType : "json",
			data : {code:code},
			success : function(data) {
				if (data == 1) {
					alert("获取成功");
				}
			}
		})
	})
	/* 点击选择设备复用模板 */
	if($('.temForMoreDevice').length > 0){
		var merid= $('.temForMoreDevice').attr('data-merid').trim()
		var type=$('.temForMoreDevice').attr('data-hardversion').trim()
		var temid= $('.temForMoreDevice').attr('data-id').trim()
		
		$('.temForMoreDevice').click(function(){
		$.ajax({
			url: '/pcequipment/inquireequipm',
			type: 'post',
			data: {merid:merid,type:type},
			success: function(res){
				$('.mfdBody').html('')
				var frament= document.createDocumentFragment()
				$(res).each(function(i,item){
					item.devicename= item.devicename== null || item.devicename== '' ? '— —' : item.devicename
					var str= '<tr><td><input type="checkbox" data-code="'+item.code+'"></td><td>'+item.code+'</td><td>'+item.devicename+'</td></tr>';
					frament.appendChild($(str).get(0));
				})
				$('.mfdBody').get(0).appendChild(frament);
			},
			error: function(err){
				console.log(err)
			}
		})
		
		$('.alertTemForMoreDevice').fadeIn()
	})
	$('.alertTemForMoreDevice').click(function(){
		$(this).fadeOut()
	})
	$('.alertDiv').click(function(e){
		e= e || windoe.event
		e.stopPropagation()
	})
	$('#checkAll').click(function(){
		if($(this).prop('checked')){
			$('.mfdBody input[type="checkbox"]').prop('checked',true)
		}else{
			$('.mfdBody input[type="checkbox"]').prop('checked',false)
		}
		
	})
	$('.mfdBody').on('click','input[type="checkbox"]',function(){
		var isAllCheck= true
		$('.mfdBody input[type="checkbox"]').each(function(i,item){
			if($(item).prop('checked') === false){
				isAllCheck= false
				return
			}
		})
		if(isAllCheck){
			$('#checkAll').prop('checked',true)
		}else {
			$('#checkAll').prop('checked',false)
		}
	})
	$('.moDeClose').click(function(){
		$('.alertTemForMoreDevice').fadeOut()
		$('.alertTemForMoreDevice input[type="checkbox"]').prop('checked',false)
	})
	$('.moDeConfirm').click(function(){
		$('.alertTemForMoreDevice').fadeOut()
		var deviceList= []
		$('.mfdBody input[type="checkbox"]').each(function(i,item){
			if($(item).prop('checked')){
				deviceList.push($(item).attr('data-code').trim())	
			}
		})
		/* 发送ajax */
		$.ajax({
			url: '/pcequipment/updateequitemp',
			type: 'post',
			dataType: 'json',
			data: {
					temid:temid,
					codeList: JSON.stringify(deviceList)
				},
			success: function(res){
				console.log(res)
				if(res.code === 200){
					toastr.success('此模板复用成功')
				}
			},
			error: function(err){
				console.log(err)
			}
		})
	})
	
	}
	
	
	})
</script>
<script type="text/javascript">
$("#btn_submit").click(function () {
	var num = $("#myModal").find("input[ name='testnum']").val();
	var code = $("#myModal").find("input[ name='code']").val();
	var pwdbinding = $("#myModal").find("input[ name='pwdbinding']").val();
	var url;
	if(num==0){
		url = "${hdpath }/pcequipment/pcbindequ?code="+code;
	}else{
		url = "${hdpath}/pcequipment/disbinding?code="+code;
	}
	$.ajax({
		data:{password:pwdbinding,},
		url : "${hdpath}/pcequipment/pwdjudge",
		type : "POST",
		cache : false,
		success : function(e){
			if(e==1){
				alert("密码不正确")
			}else if(e==0){
				window.location.href =url;
			}
		},
	});
});
function pwdbinding(code,num){
	$("#myModalLabel").text("请输入密码");
	$("#myModal").find("input[ name='code']").attr('value',code);
	$("#myModal").find("input[ name='testnum']").attr('value',num);
	$('#myModal').modal();
}
</script>
<!-- <script src="${hdpath}/hdfile/js/testJs.js"></script> -->
<script>

$(function(){
			toastr.options = {
				  "closeButton": false,
				  "debug": false,
				  "positionClass": "toast-top-right",
				  "onclick": null,
				  "showDuration": "300",
				  "hideDuration": "1000",
				  "timeOut": "1500",
				  "extendedTimeOut": "1000",
				  "showEasing": "swing",
				  "hideEasing": "linear",
				  "showMethod": "fadeIn",
				  "hideMethod": "fadeOut"
				}
				
			$('.goToTemPage').click(function(e){
				var uid= $(this).attr('data-uid').trim()
				if(!uid){
					toastr.warning('此设备未绑定商户！')
				}
			})
			
				function tem1(){

				   var targetEle= null
				    $('.tem1 #isRefInp').click(function(){
				    	var common2= parseInt($(targetEle).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
				    	$('.tem1.popover input').removeAttr('checked')
				    	if(common2 === 2){
				    		$('.tem1 #refReg2').prop('checked',true)
				    	}else if(common2 === 3){
				    		$('.tem1 #refReg3').prop('checked',true)
				    	}else {
				    		$('.tem1 #refReg1').prop('checked',true)
				    	}

				        $('.tem1.popover').fadeIn()
				     })
				     $('.tem1 #exitBut').click(function(){
				         $('.tem1.popover').fadeOut()
				     })
				      $('.tem1 #confirmBut').click(function(){
				    	  var regReg= parseInt($('.tem1 input[name="refReg"]:checked').val()) //退费标准
				          var str= ''
				          switch(regReg){
				                 case 1: str= '(默认)'; break;
				                 case 2: str= '(时间)'; break;
				                 case 3: str= '(电量)'; break;
				             }
				              $('.tem1 #spanList').text(str)
				         $('.tem1.popover').fadeOut()
				     })
			       
						     
					$('.defaultTemDiv.tem1').click(function(e){
						e= e || window.event
						var target= e.target || e.srcElement
						targetEle= target
						if($(target).hasClass('tem-title-edit')){ //编辑主模板
							var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
				            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
				            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
				            var isRef= $(target).parent().parent().parent().parent().find('.isRef').html().trim() === '是' ? true : false
				            var isWalletPay= $(target).parent().parent().parent().parent().find('.isWalletPay').html().trim() === '是' ? true : false
				            var regVal= ''
				            if(isRef){
				                regVal=  parseInt($(target).parent().parent().parent().parent().find('.isRef').next().attr('data-val'))
				             }
							console.log(temNmae,brandName,telephone,isRef,isWalletPay,regVal)
							/*=========== 将原来的值赋给input框*/
							$('.tem1 .list-center2 h3').html('修改充电模板')
					        $('.tem1 .list-center2 input[name=temNmae]').val(temNmae)
					        $('.tem1 .list-center2 input[name=brandName]').val(brandName)
					        $('.tem1 .list-center2 input[name=telephone]').val(telephone)
					        if(isRef){
					            $('.tem1 .list-center2 input[name=isRef]').eq(0).prop('checked',true)
					            var str= ''
					                switch(regVal){
					                    case 1: str= '(默认)'; break;
					                    case 2: str= '(时间)'; break;
					                    case 3: str= '(电量)'; break;
					                }
					                $('.tem1 #spanList').text(str)
					        }else{
					        	 $('.tem1 #spanList').text('')
					            $('.tem1 .list-center2 input[name=isRef]').eq(1).prop('checked',true)
					        }
					        if(isWalletPay){
					            $('.tem1 .list-center2 input[name=isWalletPay]').eq(0).prop('checked',true)
					        }else{
					            $('.tem1 .list-center2 input[name=isWalletPay]').eq(1).prop('checked',true)
					        }
					        $('.tem1.tem-mask2').fadeIn()
					         
						}
						if($(target).hasClass('tem-edit')){ //编辑子模板
							 	var name= $(target).parent().parent().find('td').eq(0).html().trim()
					            var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
					            var time= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
					            var power= $(target).parent().parent().find('td').eq(3).find('span').html().trim()
		
					            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
					                title: '修改电子模板',
					                name: name,
					                parse: parse,
					                time: time,
					                power: power,
					            }
								console.log(obj)
					            renderList(obj)
							
						}
						if($(target).hasClass('addBut')){ //添加子模板
							 	var $list= $(target).parent().parent().prev()
					            if($list.length <= 0){ //没有子节点
					                //这里是默认设置
					                var nextParse= 1
					                var nextTime= 60
					                var nextPower= 1
					                var rate1= Math.round(time / parse)  //得到的比例是1元充电多久
					                var rate2= Math.round(time / power)  //得到的比例是消耗1度电充电多久
					                var houer= (nextTime / 60) % 1 === 0 ? (nextTime / 60) : (nextTime / 60).toFixed(2)
					                var nextName= nextParse+'元'+houer+'小时'
		
					            }else { //找到上一个子节点
					                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
					                var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
					                var time= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
					                var power= $list.find('td').eq(3).find('span').text().match(reg)[0]-0
					                var rate1= Math.round(time / parse)  //得到的比例是1元充电多久
					                var rate2= Math.round(time / power)  //得到的比例是消耗1度电充电多久
					                var nextParse= parse+1
					                var nextTime= (nextParse * rate1) % 1 === 0 ? (nextParse * rate1) : (nextParse * rate1).toFixed(2)
					                var nextPower= (nextTime / rate2) % 1 === 0 ? (nextTime / rate2) : (nextTime / rate2).toFixed(2)
					                var houer= (nextTime / 60) % 1 === 0 ? (nextTime / 60) : (nextTime / 60).toFixed(2)
					                var nextName= nextParse+'元'+houer+'小时'
					            }
								
							 	var id= parseInt($(targetEle).attr('data-id'))
							 	console.log(id,nextName,nextParse,nextTime,nextPower)
							 	 $.ajax({  //添加子模板
						                data:{
						                    id: id,
						                    name: nextName,
						                    money: nextParse, 
						                    chargeTime: nextTime, 
						                    chargeQuantity: nextPower
						                },
						                url : "/wctemplate/allowaddsubclasscharge",
						                type : "POST",
						                cache : false,
						                success : function(e){
						                	if(e.code=== 100){
						                         toastr.warning('登录过期，请重新登录')
						                	}else if(e.code=== 101){
						                        toastr.warning('显示名称重复，请修改后重试')
						                	}else if(e.code=== 102){
						                        toastr.warning('金额过大，请修改后重试')
						                	}else if(e.code === 200){
						                		var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTime+'</span>分钟</td><td><span>'+nextPower+'</span>度</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td>'
						                       var list= $('<tr></tr>')
						                       list.html(str)
						                       $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
						                        toastr.success('添加成功')
						                	}
		
						                },//返回数据填充
						            });
						}
					})
					
					//主模板编辑部分开始
			        /*================== 点击关闭弹框*/
			        $('.tem1 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
				        e= e || window.event
				        e.stopPropagation()
				    })
				    $('.tem1.tem-mask2').mousedown(close2)
				    $('.tem1 .list-center2 .close').mousedown(close2)
				    $('.tem1 .list-center2 .close2').mousedown(close2)
				    function close2 (e) {
				        e= e || window.event
				        e.stopPropagation()
				        $('.tem1.tem-mask2').fadeOut()
				    }
			       /*================= 编辑主模板*/
			      $('.tem1 .list-center2 .submit').click(function(e){ 
				        e =e || window.event
				        e.stopPropagation()
				        var temNmaeVal= $('.tem1 .list-center2 input[name=temNmae]').val().trim()
				        var brandNameseVal= $('.tem1 .list-center2 input[name=brandName]').val().trim()
				        var telephoneVal= $('.tem1 .list-center2 input[name=telephone]').val().trim()
				        var isRefVal= $('.tem1 .list-center2 input[name="isRef"]:checked').val()
				        var isWalletPayVal= $('.tem1 .list-center2 input[name="isWalletPay"]:checked').val()
				        
				        var refReg= $('.tem1.popover input[name="refReg"]:checked').val() //退费标准
				        var str1= ''
				            switch(parseInt(refReg)){
				                case 1: str1= '(退费标准：时间和电量最小)'; break;
				                case 2: str1= '(退费标准：根据时间)'; break;
				                case 3: str1= '(退费标准：根据电量)'; break;
				            }
				        var permit= parseInt(isRefVal) === 0 ? 2 :  parseInt(isRefVal)
				        var walletpay = parseInt(isWalletPayVal) === 0 ? 2 :  parseInt(isWalletPayVal)
				        if(temNmaeVal.length <= 0){
				            toastr.warning('请输入模板名称')
				            return false
				        }
				        var id= parseInt($(targetEle).attr('data-id'))
			          $.ajax({
			               data:{
			                   id: id,
			                   name:temNmaeVal,
			                   remark: brandNameseVal,
			                   permit: permit,
			                   walletpay: walletpay,
			                   common1: telephoneVal,
			                   common2	: refReg
			               },
			               url : "/wctemplate/allowupdatestaircharge",
			               type : "POST",
			               cache : false,
			               success : function(e){
			                  handleEditTitle()
			               },//返回数据填充
			           });
				        
				        function handleEditTitle(){
			               var parentEle= $(targetEle).parent().parent().parent().parent()	          
			               parentEle.find('.temNmae').html(temNmaeVal)
			               parentEle.find('.brandName').html(brandNameseVal)
			               parentEle.find('.telephone').html(telephoneVal)
			               var isRefHtml= parseInt(isRefVal) ? '是' : '否'
			               parentEle.find('.isRef').html(isRefHtml)
			             
			               parentEle.find('.isRef').next().html(str1)
			               parentEle.find('.isRef').next().attr('data-val',refReg)
			              
			                if(!parseInt(isRefVal)){
			                   parentEle.find('.isRef').next().fadeOut()
			               }else{
			                   parentEle.find('.isRef').next().fadeIn()
			               }
		
			               parentEle.find('.isRef').removeClass('span-green span-red')
			               if(parseInt(isRefVal)){
			                    parentEle.find('.isRef').addClass('span-green')
			               }else {
			                   parentEle.find('.isRef').addClass('span-red')
			               }
		
			               var isWalletPayHtml= parseInt(isWalletPayVal) ? '是' : '否'
			               parentEle.find('.isWalletPay').html(isWalletPayHtml)
			               parentEle.find('.isWalletPay').removeClass('span-green span-red')
			               if(parseInt(isWalletPayVal)){
			                   parentEle.find('.isWalletPay').addClass('span-green')
			               }else {
			                   parentEle.find('.isWalletPay').addClass('span-red')
			               }
			               toastr.success('主模板编辑成功！')
			  	       		 $('.tem1.tem-mask2').fadeOut()
				        }
			      }) 
			      //主模板编辑部分结束
			      //编辑子模板开始
			      function renderList(obj){ //渲染list-content
			          $('.tem1 .list-center1 h3').html(obj.title)
			          $('.tem1 .list-center1 input[name=name]').val(obj.name)
			          $('.tem1 .list-center1 input[name=parse]').val(obj.parse)
			          $('.tem1 .list-center1 input[name=time]').val(obj.time)
			          $('.tem1 .list-center1 input[name=power]').val(obj.power)
			          $('.tem1.tem-mask1').fadeIn()
			      }
			       
			      $('.tem1 .list-center1').mousedown(function(e){
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem1.tem-mask1').mousedown(close)
			      $('.tem1 .list-center1 .close').mousedown(close)
			      $('.tem1 .list-center1 .close2').mousedown(close)
			      function close (e) {
			          e= e || window.event
			          e.stopPropagation()
			          console.log('点击了')
			          $('.tem1.tem-mask1').fadeOut()
			      }
		
			      $('.tem1 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem1.tem-mask2').mousedown(close2)
			      $('.tem1 .list-center2 .close').mousedown(close2)
			      $('.tem1 .list-center2 .close2').mousedown(close2)
			      
			       $('.tem1 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
			        e =e || window.event
			        e.stopPropagation()
			        var reg= /^\d+(\.\d+)?$/
			        var nameVal= $('.tem1 .list-center1 input[name=name]').val().trim()
			        var parseVal= $('.tem1 .list-center1 input[name=parse]').val().trim()
			        var timeVal= $('.tem1 .list-center1 input[name=time]').val().trim()
			        var powerVal= $('.tem1 .list-center1 input[name=power]').val().trim()
			        if(nameVal.length <= 0){
			        	toastr.warning("请输入模板名称")
			            return false
			        }
			        if(parseVal.length <= 0){
			        	toastr.warning("请输入充电价格")
			            return false
			        }
			        if(!reg.test(parseVal)) {
			        	toastr.warning("充电价格请输入数字")
			            return false
			        }
			        if(timeVal.length <= 0){
			        	toastr.warning("请输入充电时间")
			            return false
			        }
			        if(!reg.test(timeVal)) {
			        	toastr.warning("充电时间请输入数字")
			            return false
			        }
			        if(powerVal.length <= 0){
			        	toastr.warning("请输入消耗电量")
			            return false
			        }
			        if(!reg.test(powerVal)) {
			        	toastr.warning("消耗电量请输入数字")
			            return false
			        }
			        //修改子模板
			            //发送ajax讲修改之后的数据传输到服务器=====================
		
			            var id= parseInt($(targetEle).attr('data-id'))
			            $.ajax({
			                data:{
			                    id: id,
			                    name:nameVal,
			                    money: parseVal,
			                    chargeTime: timeVal,
			                    chargeQuantity: powerVal
			                },
			                url : "/wctemplate/allowupdatesubclasscharge",
			                type : "POST",
			                cache : false,
			                success : function(e){
			                    var parentEle= $(targetEle).parent().parent()
					            console.log(parentEle)
					            parentEle.find('td').eq(0).html(nameVal)
					            parentEle.find('td').eq(1).find('span').html(parseVal)
					            parentEle.find('td').eq(2).find('span').html(timeVal)
					            parentEle.find('td').eq(3).find('span').html(powerVal)
					        	$('.tem1.tem-mask1').fadeOut()
					        	 toastr.success("修改成功")
		
			                },//返回数据填充
			            });
		
			    })     
			      //编辑子模板结束
			}
			tem1()
		/*tem2开始=============================*/
			function tem2(){

				   var targetEle= null
					$('.defaultTemDiv.tem2').click(function(e){
						e= e || window.event
						var target= e.target || e.srcElement
						targetEle= target
						if($(target).hasClass('tem-title-edit')){ //编辑主模板
							var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
				            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
				            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
							/*=========== 将原来的值赋给input框*/
							$('.tem2 .list-center2 h3').html('修改充电模板')
					        $('.tem2 .list-center2 input[name=temNmae]').val(temNmae)
					        $('.tem2 .list-center2 input[name=brandName]').val(brandName)
					        $('.tem2 .list-center2 input[name=telephone]').val(telephone)
					        $('.tem2.tem-mask2').fadeIn()
						}
						if($(target).hasClass('tem-edit')){ //编辑子模板
							 	var name= $(target).parent().parent().find('td').eq(0).html().trim()
					            var parse= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
					            var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
					            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
							 		 title: '修改离线子模板',
						             name: name,
						             parse: parse,
						             totalParse: totalParse
					            }
								console.log(obj)
					            renderList(obj)
							
						}
						if($(target).hasClass('addBut')){ //添加子模板
							var $list= $(target).parent().parent().prev()
							 if($list.length <= 0){ //没有子节点
					                //这里是默认设置
								 	var nextParse= 30
					                var nextTotalParse= 31
					                var nextSendParse= nextTotalParse- nextParse
					                var nextName= nextParse+'元送'+nextSendParse
		
					            }else { //找到上一个子节点
					            	 	var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
					                     var parse= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
					                     var TotalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
					                    
					                     var nextParse= parse * 2
					                     var nextSendParse= (TotalParse- parse) * 2
					                     var nextTotalParse= nextParse+ nextSendParse
					                     var nextName= nextParse+'元送'+nextSendParse
					            }
							
							 	var id= parseInt($(targetEle).attr('data-id'))
							 	 $.ajax({  //添加子模板
						                data:{
						                	id: id,
						                    name: nextName,
						                    money: nextParse,
						                    remark: nextTotalParse
						                },
						                url : "/wctemplate/allowaddsubclassoffline",
						                type : "POST",
						                cache : false,
						                success : function(e){
						                	if(e.code=== 100){
						                         toastr.warning('登录过期，请重新登录')
						                	}else if(e.code=== 101){
						                        toastr.warning('显示名称重复，请修改后重试')
						                	}else if(e.code=== 102){
						                        toastr.warning('金额过大，请修改后重试')
						                	}else if(e.code === 200){
						                		var str= '<td>'+nextName+'</td><td><span>'+nextParse+'</span>元</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td>'
						                       var list= $('<tr></tr>')
						                       list.html(str)
						                       $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
						                        toastr.success('添加成功')
						                	}
		
						                },//返回数据填充
						            });
						}
					})
					
					//主模板编辑部分开始
			        /*================== 点击关闭弹框*/
			        $('.tem2 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
				        e= e || window.event
				        e.stopPropagation()
				    })
				    $('.tem2.tem-mask2').mousedown(close2)
				    $('.tem2 .list-center2 .close').mousedown(close2)
				    $('.tem2 .list-center2 .close2').mousedown(close2)
				    function close2 (e) {
				        e= e || window.event
				        e.stopPropagation()
				        $('.tem2.tem-mask2').fadeOut()
				    }
			       /*================= 编辑主模板*/
			      $('.tem2 .list-center2 .submit').click(function(e){ 
				        e =e || window.event
				        e.stopPropagation()
				       	var temNmaeVal= $('.tem2 .list-center2 input[name=temNmae]').val().trim()
				        var brandNameseVal= $('.tem2 .list-center2 input[name=brandName]').val().trim()
				        var telephoneVal= $('.tem2 .list-center2 input[name=telephone]').val().trim()
				        if(temNmaeVal.length <= 0){
				            toastr.warning('请输入模板名称')
				            return false
				        }
				        var id= parseInt($(targetEle).attr('data-id'))
			          $.ajax({
			               data:{
			            	   	id: id,
			                    name:temNmaeVal,
			                    remark: brandNameseVal,
			                    common1: telephoneVal
			               },
			               url : "/wctemplate/allowupdatestairoffline",
			               type : "POST",
			               cache : false,
			               success : function(e){
			                  handleEditTitle()
			               },//返回数据填充
			           });
				        
				        function handleEditTitle(){
			               var parentEle= $(targetEle).parent().parent().parent().parent()	          
			               parentEle.find('.temNmae').html(temNmaeVal)
			               parentEle.find('.brandName').html(brandNameseVal)
			               parentEle.find('.telephone').html(telephoneVal)
			               toastr.success('主模板编辑成功！')
			  	       		 $('.tem2.tem-mask2').fadeOut()
				        }
			      }) 
			      //主模板编辑部分结束
			      //编辑子模板开始
			      function renderList(obj){ //渲染list-content
			    	  $('.tem2 .list-center1 h3').html(obj.title)
			          $('.tem2 .list-center1 input[name=name]').val(obj.name)
			          $('.tem2 .list-center1 input[name=parse]').val(obj.parse)
			          $('.tem2 .list-center1 input[name=totalParse]').val(obj.totalParse)
			          $('.tem2.tem-mask1').fadeIn()
			      }
			       
			      $('.tem2 .list-center1').mousedown(function(e){
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem2.tem-mask1').mousedown(close)
			      $('.tem2 .list-center1 .close').mousedown(close)
			      $('.tem2 .list-center1 .close2').mousedown(close)
			      function close (e) {
			          e= e || window.event
			          e.stopPropagation()
			          console.log('点击了')
			          $('.tem2.tem-mask1').fadeOut()
			      }
		
			      $('.tem2 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem2.tem-mask2').mousedown(close2)
			      $('.tem2 .list-center2 .close').mousedown(close2)
			      $('.tem2 .list-center2 .close2').mousedown(close2)
			      
			       $('.tem2 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
			        e =e || window.event
			        e.stopPropagation()
			        var reg= /^\d+(\.\d+)?$/
			        var nameVal= $('.tem2 .list-center1 input[name=name]').val().trim()
			        var parseVal= $('.tem2 .list-center1 input[name=parse]').val().trim()
			        var totalParseVal= $('.tem2 .list-center1 input[name=totalParse]').val().trim()
			        if(nameVal.length <= 0){
			        		toastr.warning('请输入显示名称')
			                return false
			        }
			         if(parseVal.length <= 0){
			        	 	toastr.warning('请输入付款金额')
			                return false
			         }
			         if(!reg.test(parseVal)){
			        	 toastr.warning('付款金额请输入数字')
			              return false
			         }
			         if(totalParseVal.length <= 0){
			        	 toastr.warning('请输入充卡金额')
			                return false
			         }
			         if(!reg.test(totalParseVal)){
			        	 toastr.warning('充卡金额请输入数字')
			                return false
			         }
			         var flag= $('.tem2 .list-center1 h3').html().trim() === '新增离线子模板' ? true : false
			            //修改离线子模板
			        
			        //修改子模板
			            //发送ajax讲修改之后的数据传输到服务器=====================
		
			            var id= parseInt($(targetEle).attr('data-id'))
			            $.ajax({
			                data:{
			                	 id: id,
			                     name:nameVal,
			                     money: parseVal,
			                     remark: totalParseVal
			                },
			                url : "/wctemplate/allowupdatesubclassoffline",
			                type : "POST",
			                cache : false,
			                success : function(e){
			                	var parentEle= $(targetEle).parent().parent()
			                    console.log(parentEle)
			                    parentEle.find('td').eq(0).html(nameVal)
					            parentEle.find('td').eq(1).find('span').html(parseVal)
					            parentEle.find('td').eq(2).find('span').html(totalParseVal)
			                    $('.tem2.tem-mask1').fadeOut()
					        	 toastr.success("修改成功")
		
			                },//返回数据填充
			            });
		
			    })     
			      //编辑子模板结束
			}
			tem2()
			
			/*tem3开始=============================*/
			function tem3(){

				   var targetEle= null
					$('.defaultTemDiv.tem3').click(function(e){
						e= e || window.event
						var target= e.target || e.srcElement
						targetEle= target
						if($(target).hasClass('tem-title-edit')){ //编辑主模板
							var temNmae= $(target).parent().parent().parent().parent().find('.temNmae').html().trim()
				            var brandName= $(target).parent().parent().parent().parent().find('.brandName').html().trim()
				            var telephone= $(target).parent().parent().parent().parent().find('.telephone').html().trim()
							/*=========== 将原来的值赋给input框*/
							$('.tem3 .list-center2 h3').html('修改投币模板')
					        $('.tem3 .list-center2 input[name=temNmae]').val(temNmae)
					        $('.tem3 .list-center2 input[name=brandName]').val(brandName)
					        $('.tem3 .list-center2 input[name=telephone]').val(telephone)
					        $('.tem3.tem-mask2').fadeIn()
						}
						if($(target).hasClass('tem-edit')){ //编辑子模板
							   var name= $(target).parent().parent().find('td').eq(0).html().trim()
					           var coinNum= $(target).parent().parent().find('td').eq(1).find('span').html().trim()
					           var totalParse= $(target).parent().parent().find('td').eq(2).find('span').html().trim()
							 	
					            var obj= {  //这里是从后台获取的数据或者从元素上获取的（这里模拟后台数据）
							 		 title: '修改投币子模板',
						             name: name,
						             coinNum: coinNum,
						             totalParse: totalParse
					            }
					            renderList(obj)
							
						}
						if($(target).hasClass('addBut')){ //添加子模板
							var $list= $(target).parent().parent().prev()
							 var $list= $(target).parent().parent().prev()
					    	   if($list.length <= 0){ //没有子节点
					                //这里是默认设置
					                var nextCoinNum= 1
					                var nextTotalParse= 1
					                var rate= nextCoinNum / nextTotalParse  //得到的是一元几个币
					                var nextName= nextCoinNum+'元'+nextTotalParse+'个币'

					            }else { //找到上一个子节点
					                var reg= /(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}/g
					                var coinNum= $list.find('td').eq(1).find('span').text().match(reg)[0]-0
					                var totalParse= $list.find('td').eq(2).find('span').text().match(reg)[0]-0
					                var rate= totalParse/coinNum  //得到的是一个币几元
					                var nextCoinNum= coinNum+1
					                var nextTotalParse= nextCoinNum * rate
					                var nextName= nextTotalParse+'元'+nextCoinNum+'个币'
					            }
							
							 	var id= parseInt($(targetEle).attr('data-id'))
							 	 $.ajax({  //添加子模板
						                data:{
						                   id: id,
						                   name: nextName,
						                   remark: nextCoinNum,
						                   money: nextTotalParse
						                },
						                url : "/wctemplate/allowaddsubclassincoins",
						                type : "POST",
						                cache : false,
						                success : function(e){
						                	if(e.code=== 100){
						                         toastr.warning('登录过期，请重新登录')
						                	}else if(e.code=== 101){
						                        toastr.warning('显示名称重复，请修改后重试')
						                	}else if(e.code=== 102){
						                        toastr.warning('金额过大，请修改后重试')
						                	}else if(e.code === 200){
						                		var str= '<td>'+nextName+'</td> <td><span>'+nextCoinNum+'</span>个</td><td><span>'+nextTotalParse+'</span>元</td><td class="setWidth"><button type="button" class="btn btn-info tem-edit" data-id="'+e.ctemid+'">编辑</button></td>'
						                       var list= $('<tr></tr>')
						                       list.html(str)
						                       $(targetEle).parent().parent().parent()[0].insertBefore(list[0],$(targetEle).parent().parent()[0])
						                        toastr.success('添加成功')
						                	}
		
						                },//返回数据填充
						            });
						}
					})
					
					//主模板编辑部分开始
			        /*================== 点击关闭弹框*/
			        $('.tem3 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
				        e= e || window.event
				        e.stopPropagation()
				    })
				    $('.tem3.tem-mask2').mousedown(close2)
				    $('.tem3 .list-center2 .close').mousedown(close2)
				    $('.tem3 .list-center2 .close2').mousedown(close2)
				    function close2 (e) {
				        e= e || window.event
				        e.stopPropagation()
				        $('.tem3.tem-mask2').fadeOut()
				    }
			       /*================= 编辑主模板*/
			      $('.tem3 .list-center2 .submit').click(function(e){ 
				        e =e || window.event
				        e.stopPropagation()
				       	var temNmaeVal= $('.tem3 .list-center2 input[name=temNmae]').val().trim()
				        var brandNameseVal= $('.tem3 .list-center2 input[name=brandName]').val().trim()
				        var telephoneVal= $('.tem3 .list-center2 input[name=telephone]').val().trim()
				        if(temNmaeVal.length <= 0){
				            toastr.warning('请输入模板名称')
				            return false
				        }
				        var id= parseInt($(targetEle).attr('data-id'))
			          $.ajax({
			               data:{
			            	   	id: id,
			                    name:temNmaeVal,
			                    remark: brandNameseVal,
			                    common1: telephoneVal
			               },
			               url : "/wctemplate/allowupdatestairincoins",
			               type : "POST",
			               cache : false,
			               success : function(e){
			                  handleEditTitle()
			               },//返回数据填充
			           });
				        
				        function handleEditTitle(){
			               var parentEle= $(targetEle).parent().parent().parent().parent()	          
			               parentEle.find('.temNmae').html(temNmaeVal)
			               parentEle.find('.brandName').html(brandNameseVal)
			               parentEle.find('.telephone').html(telephoneVal)
			               toastr.success('主模板编辑成功！')
			  	       		 $('.tem3.tem-mask2').fadeOut()
				        }
			      }) 
			      //主模板编辑部分结束
			      //编辑子模板开始
			      function renderList(obj){ //渲染list-content
				        $('.tem3 .list-center1 h3').html(obj.title)
				        $('.tem3 .list-center1 input[name=name]').val(obj.name)
				        $('.tem3 .list-center1 input[name=coinNum]').val(obj.coinNum)
				        $('.tem3 .list-center1 input[name=totalParse]').val(obj.totalParse)
				        $('.tem3.tem-mask1').fadeIn()
				    }
			       
			      $('.tem3 .list-center1').mousedown(function(e){
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem3.tem-mask1').mousedown(close)
			      $('.tem3 .list-center1 .close').mousedown(close)
			      $('.tem3 .list-center1 .close2').mousedown(close)
			      function close (e) {
			          e= e || window.event
			          e.stopPropagation()
			          console.log('点击了')
			          $('.tem3.tem-mask1').fadeOut()
			      }
		
			      $('.tem3 .list-center2').mousedown(function(e){ //组织阻止冒泡，防止点击了自身隐藏
			          e= e || window.event
			          e.stopPropagation()
			      })
			      $('.tem3.tem-mask2').mousedown(close2)
			      $('.tem3 .list-center2 .close').mousedown(close2)
			      $('.tem3 .list-center2 .close2').mousedown(close2)
			      
			       $('.tem3 .list-center1 .submit').click(function (e) { //点击修改电子模板提交
			        e =e || window.event
			        e.stopPropagation()
			        var reg= /^\d+(\.\d+)?$/
			        var nameVal= $('.tem3 .list-center1 input[name=name]').val().trim()
			        var coinNumVal= $('.tem3 .list-center1 input[name=coinNum]').val().trim()
			        var totalParseVal= $('.tem3 .list-center1 input[name=totalParse]').val().trim()
			        if(nameVal.length <= 0){
			        	  toastr.warning("请输入显示名称")
			            return false
			        }
			        if(coinNumVal.length <= 0){
			        	toastr.warning("请输入投币个数")
			            return false
			        }
			        if(!reg.test(coinNumVal)){
			        	toastr.warning("投币个数请输入数字")
			            return false
			        }
			        if(totalParseVal.length <= 0){
			        	toastr.warning("请输入付款金额")
			            return false
			        }
			        if(!reg.test(totalParseVal)){
			        	toastr.warning("付款金额请输入数字")
			            return false
			        }
			        //修改子模板
			            //发送ajax讲修改之后的数据传输到服务器=====================
		
			            var id= parseInt($(targetEle).attr('data-id'))
			            $.ajax({
			                data:{
			                	id: id,
			                    name:nameVal,
			                    remark: coinNumVal,
			                    money: totalParseVal
			                },
			                url : "/wctemplate/allowupdatesubclassincoins",
			                type : "POST",
			                cache : false,
			                success : function(e){
			                	 var parentEle= $(targetEle).parent().parent()
			                     parentEle.find('td').eq(0).html(nameVal)
			                     parentEle.find('td').eq(1).find('span').html(coinNumVal)
			                     parentEle.find('td').eq(2).find('span').html(totalParseVal)
			                     $('.tem3.tem-mask1').fadeOut()
			                      toastr.success("修改成功")
			                },//返回数据填充
			            });
		
			    })     
			      //编辑子模板结束
			}
			tem3()
		
	})
</script>
<script>
	
	$(function() {
		//setInterval(aa, 1000); //每1秒刷新一次
		//debugger;
		function aa() {
			$.ajax({
				url : "${hdpath}/getinfo",
				data : {
					code : $("#code").val()
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					$('#queryallspan1').html(data.param1);
					$('#queryallspan2').html(data.param2);
				}
			});
		}
		$("#readsystem").click(function(e) {
			e= e || window.event
			e.preventDefault()
			$.bootstrapLoading.start({ loadingTips: "正在获取数据..." });
			$.ajax({
				url : '${hdpath}/readsysteminfo',
				data : {
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.status == "0") {
						alert("读取参数失败");
					} else {
						$("#coin_min").val(data.param1);
						$("#card_min").val(data.param2);
						$("#coin_elec").val(data.param3/10);
						$("#card_elec").val(data.param4/10);
						$("#cst").val(data.param5/10);
						$("#power_max_1").val(data.param6);
						$("#power_max_2").val(data.param7);
						$("#power_max_3").val(data.param8);
						$("#power_max_4").val(data.param9);
						$("#power_2_tim").val(data.param10);
						$("#power_3_tim").val(data.param11);
						$("#power_4_tim").val(data.param12);
						$("#sp_rec_mon").val(data.param13);
						$("#sp_full_empty").val(data.param14);
						$("#full_power_min").val(data.param15);
						$("#full_charge_time").val(data.param16);
						$("#elec_time_first").val(data.param17);
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("#submitbtn").click(function(e) {
			e= e || window.event
			e.preventDefault()
			var reg=/^[0-9]+$/;
			var firstreg=/[0-9]+$/;
			var elecreg=/^\d+(\.\d)?$/;
			var coin_minval = $("#coin_min").val();
			var card_minval = $("#card_min").val();
			var coin_elecval = $("#coin_elec").val();
			var card_elecval = $("#card_elec").val();
			var cstval = $("#cst").val();
			var power_max_1val = $("#power_max_1").val();
			var power_max_2val = $("#power_max_2").val();
			var power_max_3val = $("#power_max_3").val();
			var power_max_4val = $("#power_max_4").val();
			var power_2_timval = $("#power_2_tim").val();
			var power_3_timval = $("#power_3_tim").val();
			var power_4_timval = $("#power_4_tim").val();
			var sp_rec_monval = $("#sp_rec_mon").val();
			var sp_full_emptyval = $("#sp_full_empty").val();
			var full_power_minval = $("#full_power_min").val();
			var full_charge_timeval = $("#full_charge_time").val();
			var elec_time_firstval = $("#elec_time_first1").val();
			console.log('--' + elec_time_firstval);
			if (coin_minval == null || coin_minval == "" || !reg.test(coin_minval)) {
				alert("设置投币充电时间参数不可为空且不可为非法字符")
			} else if (card_minval == null || card_minval == "" || !reg.test(card_minval)) {
				alert("设置刷卡充电时间参数不可为空且不可为非法字符")
			} else if (coin_elecval == null || coin_elecval == "" || !elecreg.test(coin_elecval)) {
				alert("设置单次投币最大用电量参数不可为空且不可为非法字符")
			} else if (card_elecval == null || card_elecval == "" || !elecreg.test(card_elecval)) {
				alert("设置单次刷卡最大用电量参数不可为空且不可为非法字符")
			} else if (cstval == null || cstval == "" || !elecreg.test(cstval)) {
				alert("设置刷卡扣费金额参数不可为空且不可为非法字符")
			} else if (power_max_1val == null || power_max_1val == "" || !reg.test(power_max_1val)) {
				alert("设置第一档最大充电功率参数不可为空且不可为非法字符")
			} else if (power_max_2val == null || power_max_2val == "" || !reg.test(power_max_2val)) {
				alert("设置第二档最大充电功率参数不可为空且不可为非法字符")
			} else if (power_max_3val == null || power_max_3val == "" || !reg.test(power_max_3val)) {
				alert("设置第三档最大充电功率参数不可为空且不可为非法字符")
			} else if (power_max_4val == null || power_max_4val == "" || !reg.test(power_max_4val)) {
				alert("设置四档最大充电功率参数不可为空且不可为非法字符")
			} else if (power_2_timval == null || power_2_timval == "" || !reg.test(power_2_timval)) {
				alert("设置第二档充电时间百分比参数不可为空且不可为非法字符")
			} else if (power_3_timval == null || power_3_timval == "" || !reg.test(power_3_timval)) {
				alert("设置第三档充电时间百分比参数不可为空且不可为非法字符")
			} else if (power_4_timval == null || power_4_timval == "" || !reg.test(power_4_timval)) {
				alert("设置第四档充电时间百分比参数不可为空且不可为非法字符")
			} else if (sp_rec_monval == null || sp_rec_monval == "" || !reg.test(sp_rec_monval)) {
				alert("设置是否支持余额回收参数不可为空且不可为非法字符")
			} else if (sp_full_emptyval == null || sp_full_emptyval == "" || !reg.test(sp_full_emptyval)) {
				alert("设置是否支持断电自停参数不可为空且不可为非法字符")
			} else if (full_power_minval == null || full_power_minval == "" || !reg.test(full_power_minval)) {
				alert("设置充电器最大浮充功率参数不可为空且不可为非法字符")
			} else if (full_charge_timeval == null || full_charge_timeval == "" || !reg.test(full_charge_timeval)) {
				alert("设置浮充时间参数不可为空且不可为非法字符")
			} else if (elec_time_firstval == null || elec_time_firstval == "" || (parseInt(elec_time_firstval) > 1 || parseInt(elec_time_firstval) < -1)) {
				alert("设置是否初始显示电量 参数不可为空且不可为非法字符")
			} else {
				if (elec_time_firstval == 1 || elec_time_firstval == 0 || elec_time_firstval == -1) {
					$("#elec_time_first").val(elec_time_firstval);
				}
				$.bootstrapLoading.start({ loadingTips: "正在设置..." });
				$.ajax({
					url : '/equipment/setSysParam',
					data : $("#sysParamForm").serialize(),
					type : "POST",
					cache : false,
					success : function(data) {
						if (data.status == "0") {
							alert("设置失败");
						} else {
							alert("设置成功");
						}
					},//返回数据填充
					complete: function () {
			            $.bootstrapLoading.end();
			        }
				});
			}
			/* var coin_minval = $("#coin_min").val() == null ? 0 : $("#coin_min").val();
			var card_minval = $("#card_min").val() == null ? 0 : $("#card_min").val();
			var coin_elecval = $("#coin_elec").val() == null ? 0 : $("#coin_elec").val();
			var card_elecval = $("#card_elec").val() == null ? 0 : $("#card_elec").val();
			var cstval = $("#cst").val() == null ? 0 : $("#cst").val();
			var power_max_1val = $("#power_max_1").val() == null ? 0 : $("#power_max_1").val();
			var power_max_2val = $("#power_max_2").val() == null ? 0 : $("#power_max_2").val();
			var power_max_3val = $("#power_max_3").val() == null ? 0 : $("#power_max_3").val();
			var power_max_4val = $("#power_max_4").val() == null ? 0 : $("#power_max_4").val();
			var power_2_timval = $("#power_2_tim").val() == null ? 0 : $("#power_2_tim").val();
			var power_3_timval = $("#power_3_tim").val() == null ? 0 : $("#power_3_tim").val();
			var power_4_timval = $("#power_4_tim").val() == null ? 0 : $("#power_4_tim").val();
			var sp_rec_monval = $("#sp_rec_mon").val() == null ? 0 : $("#sp_rec_mon").val();
			var sp_full_emptyval = $("#sp_full_empty").val() == null ? 0 : $("#sp_full_empty").val();
			var full_power_minval = $("#full_power_min").val() == null ? 0 : $("#full_power_min").val();
			var full_charge_timeval = $("#full_charge_time").val() == null ? 0 : $("#full_charge_time").val();
			var elec_time_firstval = $("#elec_time_first").val() == null ? 0 : $("#elec_time_first").val();
			$.ajax({
				url : '${hdpath}/setsystem',
				data : {
					coin_min : coin_minval,
					card_min : card_minval,
					coin_elec : coin_elecval,
					card_elec : card_elecval,
					cst : cstval,
					power_max_1 : power_max_1val,
					power_max_2 : power_max_2val,
					power_max_3 : power_max_3val,
					power_max_4 : power_max_4val,
					power_2_tim : power_2_timval,
					power_3_tim : power_3_timval,
					power_4_tim : power_4_timval,
					sp_rec_mon : sp_rec_monval,
					sp_full_empty : sp_full_emptyval,
					full_power_min : full_power_minval,
					full_charge_time : full_charge_timeval,
					elec_time_first : elec_time_firstval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.status == "0") {
						alert("设置失败");
					} else {
						alert("设置成功");
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			}); */
		})
		$("#queryall").click(function() {
			$.ajax({
				url : '${hdpath}/testmutual',
				data : {
					param : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					/* $('#queryallspan1').html(data.param1);
					$('#queryallspan2').html(data.param2); */
				},//返回数据填充
			});
		})
		$("#queryfree").click(function() {
			$.ajax({
				url : '${hdpath}/testmutual',
				data : {
					param : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				async : false,
				success : function(data) {
				},//返回数据填充
			});
		})
		$("button[id^='payport']").click(function() {
			var payportval = $(this).parent().prev().prev().prev().prev().prev().text();
			var timeval = $(this).parent().prev().prev().children().val();
			var elecval = $(this).parent().prev().children().val();
			$.ajax({
				url : '${hdpath}/testpaytoport',
				data : {
					payport : payportval,
					time : timeval,
					elec : elecval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
				},//返回数据填充
			});
		})
		$("#querytakemoney").click(function() {
			$.ajax({
				url : '${hdpath}/testmutual',
				data : {
					param : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
				},//返回数据填充
			});
		})
		//setInterval(querytakemoney, 1000); //每1秒刷新一次
		//debugger;
		function querytakemoney() {
			$.ajax({
				url : "${hdpath}/getinfo",
				data : {
					code : $("#code").val()
				},
				type : "post",
				dataType : "json",
				success : function(data) {
					$('#coinmoney').html(data.param6);
					$('#icmoney').html(data.param7);
				}
			});
		}
		$("#seticandcoin").click(function() {
			var setcoinval = $("#coinstate").find("option:selected").val();
			var seticval = $("#icstate").find("option:selected").val();
			$.ajax({
				url : '${hdpath}/seticandcoin',
				data : {
					setcoin : setcoinval,
					setic : seticval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					$("#seticcoininfo").html(data);
				},//返回数据填充
			});
		})
		$("button[id^='queryport']").click(function() {
			var portval = $(this).val();
			$.bootstrapLoading.start({ loadingTips: "正在获取数据，请稍后..." });
			$.ajax({
				url : '${hdpath}/querystate',
				data : {
					port : portval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.err == "0") {
						alert("获取数据失败");
					} else {
						var portStatusStr = '';
						if (data.portstatus == 1) {
							portStatusStr = "空闲";
						} else if (data.portstatus == 2) {
							portStatusStr = "使用";
						} else {
							portStatusStr = "故障";
						}
						$("#portstate" + portval).html(portStatusStr);
						$("#porttime" + portval).html(data.time);
						$("#portpower" + portval).html(data.power);
						$("#portelec" + portval).html(data.elec / 100);
						$("#recylce" + portval).html(data.surp);
					}
				},//返回数据填充
		        complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("button[id^='lock']").click(function() {
			var wolf = $(this).parent().prev().children().val();
			$.bootstrapLoading.start({ loadingTips: "正在锁定端口，请稍后..." });
			$.ajax({
				url : '${hdpath}/lock',
				data : {
					port : wolf,
					status : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.err == "0") {
						alert(data.errinfo);
					} else if (data.err == "1") {
						alert(data.errinfo);
					} else {
						var portStatusStr = "";
						var portStatusHtml = $("#portstate" + wolf).text();
						if(data.status == "0") {
							portStatusStr = "锁定";
						} else if (portStatusHtml == "锁定") {
							portStatusStr = "空闲";
						}
						$("#portstate" + wolf).html(portStatusStr);
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("button[id^='debloack']").click(function() {
			var wolf = $(this).parent().prev().prev().children().val();
			$.bootstrapLoading.start({ loadingTips: "正在锁定端口，请稍后..." });
			$.ajax({
				url : '${hdpath}/lock',
				data : {
					port : wolf,
					status : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
					if (data.err == "0") {
						alert(data.errinfo);
					} else if (data.err == "1") {
						alert(data.errinfo);
					} else {
						var portStatusStr = "";
						var portStatusHtml = $("#portstate" + wolf).text();
						if(data.status == "1") {
							portStatusStr = "空闲";
						} else if (portStatusHtml == "锁定") {
							portStatusStr = "空闲";
						}
						$("#portstate" + wolf).html(portStatusStr);
					}
				},//返回数据填充
				complete: function () {
		            $.bootstrapLoading.end();
		        }
			});
		})
		$("button[id^='stopport']").click(function() {
			var stopportval = $(this).parent().prev().prev().prev().prev().prev().prev().text();
			$.ajax({
				url : '${hdpath}/stopRechargeByPort',
				data : {
					port : stopportval,
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
				},//返回数据填充
			});
		})
		$("#readall").click(function() {
			$.ajax({
				url : '${hdpath}/testmutual',
				data : {
					param : $(this).val(),
					code : $("#code").val()
				},
				type : "POST",
				cache : false,
				success : function(data) {
				},//返回数据填充
			});
		})
	})
</script>
</html>
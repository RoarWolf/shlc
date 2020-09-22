<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title></title>
<meta name="description" content="简介">
<meta name="keywords" content="关键字">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
</head>	
<style>
.side-nav-item {
  display: block;
  padding: 10px 15px 10px 15px;
  background-color: #FFFFFF;
  cursor: pointer;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
  -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.item-title {
  background-color: #dcf0f9;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  border-bottom: 1px solid #dcf0f9;
}

.panel-heading {
  margin-top: 5px;
  padding: 0;
  border-radius: 3px;
  border: 1px solid transparent;
  border-color: #DDDDDD;
}

.item-body {
  padding: 10px 15px 5px 15px;
  border-bottom: 1px solid #DDDDDD;
}

.item-second {
  margin-top: 5px;
  cursor: pointer;
}

.item-second a {
  display: block;
  height: 100%;
  width: 100%;
}
.at{ color:red;}
</style>
<body>
<div class="col-md-2 side-nav" style="width: 180px;">
  <div class="panel-group" id="accordion">
    <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-shuju" id="headshuju" class="side-nav-item item-title">
      	数据汇总
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-shuju" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
          	<c:if test="${admin.rank==0}">
          	   <li class="item-second" id="10"><a href="${hdpath}/pcstatistics/graphindex">数据监控</a></li>
               <li class="item-second" id="11"><a href="${hdpath}/pcstatistics/collectinfo">数据汇总</a></li>
               <li class="item-second" id="12"><a href="${hdpath}/pcstatistics/statisticsinfo">历史统计</a></li>
    		</c:if>
          	<c:if test="${admin.rank==2}">
               <li class="item-second" id="14"><a href="${hdpath}/pcstatistics/agentdatacollect">数据汇总</a></li>
    		</c:if>
               <li class="item-second" id="13"><a href="${hdpath}/pcstatistics/codeearningscollect">设备收益</a></li>
               <li class="item-second" id="15"><a href="${hdpath}/pcstatistics/agentcollectinfo">商户收益</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-admini" id="headadmini" class="side-nav-item item-title">
      	用户管理
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-admini" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="21"><a href="${hdpath}/pcadminiStrator/selectDealerUserInfo">商户信息</a></li>
            <li class="item-second" id="22"><a href="${hdpath}/pcadminiStrator/selectGeneralUserInfo">用户信息</a></li>
            <li class="item-second" id="23"><a href="${hdpath}/pcoperate/useroperatelog">操作信息</a></li>
          </ul>
        </div>
      </div>
    </div>

    <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-charge" id="headcharge" class="side-nav-item item-title collapsed">
      	交易记录
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-charge" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="31"><a href="${hdpath}/pcorder/selectTradeRecord">交易记录</a></li>
            <li class="item-second" id="33"><a href="${hdpath}/pcorder/selectChargeRecord">充电记录</a></li>
            <li class="item-second" id="34"><a href="${hdpath}/pcorder/selectOfflineRecord">离线卡记录</a></li>
            <li class="item-second" id="35"><a href="${hdpath}/pcorder/selectIncoinsRecord">投币记录</a></li>
            <li class="item-second" id="37"><a href="${hdpath}/pcorder/selectPackageMonth">包月记录</a></li>
            <c:if test="${admin.rank==0}">
            <li class="item-second" id="32"><a href="${hdpath}/pcorder/selectWithdrawRecord">提现管理</a></li>
            <li class="item-second" id="36"><a href="${hdpath}/pcorder/selectWalletRecord">钱包记录</a></li>
            </c:if>
          </ul>
        </div>
      </div>
    </div>
    
     <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-code" id="headcode" class="side-nav-item item-title collapsed">
      	设备管理
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-code" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="41"><a href="${hdpath}/pcequipment/selectEquList">设备列表</a></li>
            <li class="item-second" id="411"><a href="${hdpath}/pcequipment/selectBluetoothList">蓝牙设备</a></li>
            <li class="item-second" id="42"><a href="${hdpath}/pcequipment/selectEquipmentLog">设备日志</a></li>
           	<li class="item-second" id="43"><a href="${hdpath}/pcequipment/selectcodeoperatelog">操作日志</a></li>
          </ul>
        </div>
      </div>
    </div>
     <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-ICcard" id="headICcard" class="side-nav-item item-title collapsed">
      	IC卡管理
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-ICcard" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="51"><a href="${hdpath}/pccardrecord/selectonlinecard">在线卡查询</a></li>
            <li class="item-second" id="52"><a href="${hdpath}/pccardrecord/onlineoperate">在线卡操作</a></li>
            <li class="item-second" id="53"><a href="${hdpath}/pccardrecord/selectonlineconsume">在线卡消费</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-estate" id="headestate" class="side-nav-item item-title collapsed">
      	小区管理
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-estate" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="61"><a href="${hdpath}/pcHousing/housingAdress">小区管理</a></li>
          </ul>
        </div>
      </div>
    </div>
    <c:if test="${admin.rank==0}">
    <div class="panel-heading panel">
      <a data-toggle="collapse" data-parent="#accordion" href="#item-system" id="headsystem" class="side-nav-item item-title collapsed">
      	系统设置
        <span class="pull-right glyphicon glyphicon-chevron-toggle glyphicon-plus"></span>
      </a>
      <div id="item-system" class="panel-collapse collapse">
        <div class="item-body">
          <ul class="list-unstyled">
            <li class="item-second" id="71"><a href="${hdpath}/pcequipment/lookSystemTemp">系统模板</a></li>
            <li class="item-second" id="72"><a href="${hdpath}/system/computedata">结算</a></li>
          </ul>
        </div>
      </div>
    </div>
    </c:if>
  </div>
</div>
   <div style="position: fixed; left: 0px; bottom:10px; width: 180px; text-align: center;">
  	<span>版本号：1.30</span></br>
  	<span>2019-10-17</span>
   </div>
</body>
</html>

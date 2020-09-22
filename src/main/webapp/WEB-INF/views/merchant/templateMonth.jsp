<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="${hdpath}/hdfile/css/base.css">
<link rel="stylesheet" href="${hdpath}/mui/css/mui.min.css">
<link rel="stylesheet" href="${hdpath}/hdfile/css/monthlyTem.css">
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath }/js/jquery.js"></script>
<script src="${hdpath }/mui/js/mui.min.js"></script>
<script src="${hdpath }/hdfile/js/monthlyTem.js"></script>
<title>包月模板</title>
</head>
<body>
	 <div class="tem">
	 <c:if test="${templatelist == null}">
	 	<div class="noData">暂无数据！</div>
	 </c:if>
	 <c:if test="${templatelist != null}">
	 <c:forEach items="${templatelist}" var="tempparent">
        <div class="list-div">
            <li>
                <div class="title">
                    <p>包月模板名称：<span class="temName">${tempparent.name}</span></p>
                    <p>每月最大充电次数：<span ><span class="monthChargeTimes">${tempparent.common1}</span><strong>次</strong></span></p>
                    <p>每日最大充电次数<span><span class="dayChargeTimes">${tempparent.remark}</span><strong>次</strong></span></p>
                    <p>每次最大用电量：<span><span class="maxPower">${tempparent.common3}</span><strong>度</strong></span></p>
                    <p>每次最长充电时间:<span><span class="longestTime">${tempparent.common2}</span><strong>分钟</strong></span></p>
                      <p>是否开通包月功能：
	                    <c:if test="${tempparent.ifmonth==1}"><span class="span-green">是</span></c:if>
	                    <c:if test="${tempparent.ifmonth==2}"><span class="span-red">否</span></c:if>
                    </p>
                    <div>
                        <button type="button" class="mui-btn mui-btn-success tem-title-edit" data-id="${tempparent.id}" >编辑</button>
                    </div>
                </div>
                <ul class="mui-table-view">
                 <c:forEach items="${tempparent.gather}" var="tempson">
	                    <li class="mui-table-view-cell">
	                        <p>显示名称：
	                            <span class="name">${tempson.name}</span></p>
	                        <p>
	                           	 充值金额：
	                            <span>
	                                <span class="chargeParse">${tempson.money}</span><b>元</b>
	                            </span>
	                        </p>
	                        <p>包月时间：
	                            <span>
	                                <span class="time">${tempson.common1}</span><b>月</b>
	                            </span>
	                        </p>
	                        <div>
	                            <button type="button" class="mui-btn mui-btn-success tem-edit" data-id="${tempson.id}">编辑</button>
	                            <button type="button" class="mui-btn mui-btn-success tem-delete" data-id="${tempson.id}">删除</button>
	                        </div>
	                    </li>
                    </c:forEach>
                    <li class="mui-table-view-cell bottom"><button type="button" class="mui-btn mui-btn-success mui-btn-outlined addBut" data-id="${tempparent.id}">添加</button></li>
                   </ul> 
        </div>
        </c:forEach>
        </c:if>
      
        <nav class='mui-bar mui-bar-tab  <c:if test="${templatelist != null}">removeTem</c:if>'>
           <div>
               <a href="/merchant/index"><button type="button" class="mui-btn mui-btn-success">首页</button></a>
               <c:if test="${templatelist == null}">
               <button type="button" class="mui-btn mui-btn-success addTemplate">添加模板</button>
               </c:if>
           </div>
      </nav>
    </div>
    <div class="tem-mask1">
        <div class="list-center1">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改包月子模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="name">显示名称</label>
                    <br>
                    <input type="text" id="name" name="name" placeholder="请填写显示名称">
                </div>
                <div class="inp">
                    <label for="chargeParse">充值金额</label>
                    <br>
                    <input type="text" id="chargeParse" name="chargeParse" placeholder="请填写充值金额"><strong>元</strong>
                </div>
                <div class="inp">
                    <label for="time">包月时间</label>
                    <br>
                    <input type="text" id="time" name="time" placeholder="请填写包月时间"><strong>月</strong>
                </div>
               <!--  <div class="inp">
                    <ol class="ol-red">注:</ol>
                    <ol><span>投币个数：</span> 指投币的数量（单位为元）</ol>
                    <ol><span>付款金额：</span> 该金额为用户实际支付的金额（即商户收到的金额）</ol>
                </div> -->

            </form>
            <div class="bottom">
                <button type="button" class="mui-btn mui-btn-success close2">关闭</button>
                <button type="button" class="mui-btn mui-btn-success submit">提交</button>
            </div>
        </div>
    </div>

    <div class="tem-mask2" >
        <div class="list-center2">
            <span class="mui-icon mui-icon-closeempty close"></span>
            <h3>修改包月主模板</h3>
            <form action="#">
                <div class="inp">
                    <label for="temName">包月模板名称</label>
                    <br>
                    <input type="text" id="temName" name="temName" placeholder="请填写包月模板名称">
                </div>
                <div class="inp">
                    <label for="monthChargeTimes">每月最大充电次数</label>
                    <br>
                    <input type="text" id="monthChargeTimes" name="monthChargeTimes" placeholder="请填写每月最大充电次数"><strong>次</strong>
                </div>
                <div class="inp">
                    <label for="dayChargeTimes">每日最大充电次数</label>
                    <br>
                    <input type="text" id="dayChargeTimes" name="dayChargeTimes" placeholder="请填写每日充电次数"><strong>次</strong>
                </div>
                <div class="inp">
                    <label for="maxPower">每次最大用电量</label>
                    <br>
                    <input type="text" id="maxPower" name="maxPower" placeholder="请填写每次最大用电量"><strong>度</strong>
                </div>
                 <div class="inp">
                    <label for="longestTime">每次最长充电时间</label>
                    <br>
                    <input type="text" id="longestTime" name="longestTime" placeholder="请填写每次最长充电时间"><strong>分钟</strong>
                </div>
                <div class="inp radio-inp">
                    <h5>是否开通包月功能</h5>
                    <br/>
                    <label>
                        <input type="radio" name="isMonthly" value="1" />是
                    </label>
                    <label>
                        <input type="radio" name="isMonthly" value="2" />否
                    </label>
                </div>
                <div class="inp ol">
                    <ol class="ol-red">注:</ol>
                    <ol><span>每日充电次数说明：</span>当模板每日次数每月次数为0的时候，就是不限制次数，用户随意使用</ol>
                </div>

            </form>
            <div class= "bottom">
                <button type="button" class="mui-btn mui-btn-success close2">关闭</button>
                <button type="button" class="mui-btn mui-btn-success submit">提交</button>
            </div>
        </div>
    </div>
    
    
</body>
</html>
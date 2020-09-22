<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充电预览</title>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
<link href="${hdpath}/css/ui-choose.css" rel="stylesheet" />
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<script src="${hdpath}/js/jquery.js"></script>
<script src="${hdpath}/js/ui-choose.js"></script>
<style type="text/css">
#notarize{float: right;  margin-right: 2rem; margin-top: 2px;}
</style>
</head>
<body>
<div class="demo-box">
  <div style="margin-top: 2%;">
	<c:if test="${template.remark!=null}">
		<span style="margin-left: 3%;font-size: 16px;">欢迎使用${template.remark }电动车充电设备</span>
	</c:if>
	
  </div>
  <table class="demo-table">
    <caption></caption>
      <tr>
      <td>
        <ul class="chargechoose"><li>请选择端口</li></ul>
        <ul class="ui-choose" id="uc_01">
          <li>1号</li>
          <li>2号</li>
          <li>3号</li>
          <li>4号</li>
          <li>5号</li>
          <li>6号</li>
          <li>7号</li>
          <li>8号</li>
          <li>9号</li>
          <li>10号</li>
        </ul>
     </td>
    </tr>
    <tr><td style="height:25px;"></td></tr>
    <tr>
      <td>
        <ul class="chargechoose"><li>请选择充电时间</li></ul>
        <ul class="ui-choose" id="uc_02">
        <c:forEach items="${templatelist}" var="temp">
          <li class="equtemp">${temp.name}<span style="display:none;">_${temp.id}</span></li>
         </c:forEach>
        </ul>
      </td>
    </tr>
  </table>
</div>
<script>
// 将所有.ui-choose实例化
$('.ui-choose').ui_choose();

// uc_01 ul 单选
var uc_01 = $('#uc_01').data('ui-choose'); 
uc_01.click = function(index, item) {
	//var name = item.text().replace(/号端口/g, '');
	var name = item.text().replace('号端口', '');
	$('input[name="portchoose"]').val(name)
}
/* uc_01.change = function(index, item) {
    console.log('change', index, item.text())
} */
var uc_02 = $('#uc_02').data('ui-choose'); 
uc_02.click = function(index, item){
	var id = item.text().split("_")[1];
	$('input[name="chargeparam"]').val(id)
    //console.log('click', index, item.text())
}
/* uc_02.change = function(index, item) {
    console.log('change', index, item.text())
} */
</script>
<div class="container">
	<form  method="get">
		<input type="hidden" id="code" name="code" value="${code}">
		<input type="hidden" id="wxpaycode" name="wxpaycode" value="${wxpaycode}">
		<input type="hidden" id="portchoose" name="portchoose" value="">
		<input type="hidden" id="chargeparam" name="chargeparam" value="">
		<div>
			<!-- <button type="submit" class="btn btn-success" onClick="submitAction();return false;">确认提交</button> -->
			<input id="notarize" disabled="disabled" class="btn btn-success" type="button" value="确认提交" >
		</div>
	</form>
	<div style="padding-top:20px"></div>
	<hr>
	<p style="color: red">
		注意：<br/>
			绿色端口为空闲端口，可使用。<br/>
			红色端口为正在使用，可加钱。<br/>
			灰色端口为端口故障，不可用。<br/>
	</p>
	<div>
		<c:if test="${template.remark!=null}">
		<span>如用疑问，请联系：${template.common1}<c:if test="${template.common1==null}">${user.phoneNum}</c:if></span>
		</c:if>
	</div>
	<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
		<div align="center"><a href="${hdpath }/merchant/manage" class="btn btn-success">回管理页面</a></div>
	</nav>
</div>
</body>
</html>
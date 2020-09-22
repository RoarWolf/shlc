<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>信息对比</title>
</head>
<body>
<!-- 	List<Map<String, Object>> -->
	<table>
	    <thead>
		   <tr>
			<th>序号</th>
			<th>订单ID</th>
			<th>订单号</th>
			<th>商户名</th>
			<th>设备号</th>
			<th>端口号</th>
			<th>消费金额</th>
			<th>投币数</th>
			<th>订单状态</th>
			<th>支付方式</th>
			<th>回复状态</th>
			<th>记录时间</th>
		  </tr>
		</thead>
		<tbody>
				<tr><td colspan="12">总计：{size}</td></tr>
			<c:forEach items="${result}" var="order"  varStatus="as">
				<tr id="name${order.id}" >
					<td >${as.count+current}</td>
					<td>${order.id}</td>
					<td>${order.ordernum}</td>
					<td>${order.dealer}</td>
					<td>${order.equipmentnum}</td>
					<td>${order.port}</td>
					<td>${order.money}</td>
					<td>${order.handletype == 3 ? "— —" : order.coin_num}</td>
					<td>
					  <c:choose>
						<c:when test="${order.status == 0}">未支付</c:when>
						<c:when test="${order.status == 1}">支付成功</c:when>
					  </c:choose>
					</td>
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
					<td><font>${order.recycletype == 0 ? "未回复" : "回复成功"}</font></td>
					<td><fmt:formatDate value="${order.begin_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
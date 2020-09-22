<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择充电机器</title>
<%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
<%@ include file="/WEB-INF/views/public/commons.jspf"%>
</head>
<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<table class="table table-hover">
					<thead>
						<tr align="center">
							<td><strong>设备编号</strong></td>
							<td><strong>状态</strong></td>
							<td><strong>操作</strong></td>
							<td><strong>端口状态</strong></td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${equipmentList }" var="equ">
							<tr align="center">
								<td>${equ.code }</td>
								<td>${equ.state == 1 ? "在线" : "不在线" }</td>
								<td><a <c:if test="${equ.state == 1 }">href="${hdpath }/merchant/remotechargechoose?code=${equ.code}"</c:if>
								    <c:if test="${equ.state == 0}">href="javascript:void(0);"  onclick="this.setAttribute('disabled','disabled')"</c:if> class="btn btn-success"  >远程充电</a></td>
								<td>
									<c:choose>
										<c:when test="${equ.hardversion != '03' && equ.hardversion != '04' }">
											<a href="${hdpath }/merchant/charge?code=${equ.code}" class="btn btn-success">状态</a>
										</c:when>
										<c:otherwise>
											<button disabled="disabled" class="btn btn-default">状态</button>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
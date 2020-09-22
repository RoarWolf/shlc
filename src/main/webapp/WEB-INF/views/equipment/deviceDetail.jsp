<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title>设备详情</title>
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<link rel="stylesheet" href="${hdpath}/css/base.css">
	<link rel="stylesheet" href="${hdpath }/mui/css/mui.min.css">
	<script src="${hdpath}/mui/js/mui.min.js"></script>
	<script type="text/javascript" src="${hdpath }/js/jquery-2.1.0.js"></script>
	<style>
		.app {
			font-size: 14px;
			padding-top: 15px;
		}
		h5 {
			margin-left: 10px;
			margin-bottom: 8px;
		}
		.module-info ul {
			margin: 0 10px;
			border: 1px solid #44b549;
			background-color: #f5f7fa;
			padding: 10px 10px;
			border-radius: 5px;
			box-shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
		}
		.module-info ul li{
			display: flex;
			justify-content: space-between;
			padding: 5px 0;
		}
		.module-info ul li .right {
			color: #666;
		}
	</style>
</head>
<body>
	<div class="app">
		<h5>模块信息</h5>
		<div class="module-info">
			<ul>
				<%-- <li>
					<div class="left">硬件版本</div>
					<div class="right">${equipment.hardversion}-${ equipment.hardversion == '00' ? '出厂默认设置' : equipment.hardversion == '01' ? '十路智慧款': equipment.hardversion == '02' ? '电轿款' : equipment.hardversion == '03' ? '脉冲板子' :  equipment.hardversion == '04' ? '离线充值机' :  equipment.hardversion == '05' ? '十六路智慧款' :  equipment.hardversion == '06' ? '二十路智慧款' :  equipment.hardversion == '07' ? '单路交流桩' :  equipment.hardversion == '08' ? 'V3十路智慧版' : ''}</div>
				</li>
				<li>
					<div class="left">硬件版本号</div>
					<div class="right">${equipment.hardversionnum}-${equipment.hardversionnum == '00' ? '2G模块' : equipment.hardversionnum == '01' ? '4G模块': equipment.hardversionnum == '02' ? '蓝牙模块' : equipment.hardversionnum == '03' ? '合宙2G模块' : ''}</div>
				</li> --%>
				<li>
					<div class="left">设备编号</div>
					<div class="right">${equipmentinfo.code}</div>
				</li>
				<li>
					<div class="left">设备名称</div>
					<div class="right">${equipmentinfo.remark == null ? '— —' : equipmentinfo.remark}</div>
				</li>
				<li>
					<div class="left">小区名称</div>
					<div class="right">${equipmentinfo.areaname == null ? '— —' : equipmentinfo.areaname}</div>
				</li>
				<li>
					<div class="left">设备CCID</div>
					<div class="right">${equipmentinfo.ccid}</div>
				</li>
				<li>
					<div class="left">设备IMEI</div>
					<div class="right">${equipmentinfo.imei}</div>
				</li>
				<li>
					<div class="left">信号强度</div>
					<div class="right">${equipmentinfo.csq}</div>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
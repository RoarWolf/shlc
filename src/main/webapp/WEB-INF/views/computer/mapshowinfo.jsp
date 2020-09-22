<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
<link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.15&key=2c85f8508cc8c9c37829d4609cd8cad1"></script>
<script type="text/javascript" src="${hdpath}/js/jquery.js"></script>
    <style>
        html, body, #container {
            height: 100%;
            width: 100%;
        }

        .content-window-card {
            position: relative;
            box-shadow: none;
            bottom: 0;
            left: 0;
            width: auto;
            padding: 0;
        }

        .content-window-card p {
            height: 2rem;
        }

        .custom-info {
            border: solid 1px silver;
        }

        div.info-top {
            position: relative;
            background: none repeat scroll 0 0 #F9F9F9;
            border-bottom: 1px solid #CCC;
            border-radius: 5px 5px 0 0;
        }

        div.info-top div {
            display: inline-block;
            color: #333333;
            font-size: 14px;
            font-weight: bold;
            line-height: 31px;
            padding: 0 10px;
        }

        div.info-top img {
            position: absolute;
            top: 10px;
            right: 10px;
            transition-duration: 0.25s;
        }

        div.info-top img:hover {
            box-shadow: 0px 0px 5px #000;
        }

        div.info-middle {
            font-size: 12px;
            padding: 10px 6px;
            line-height: 20px;
        }

        div.info-bottom {
            height: 0px;
            width: 100%;
            clear: both;
            text-align: center;
        }

        div.info-bottom img {
            position: relative;
            z-index: 104;
        }

        span {
            margin-left: 5px;
            font-size: 11px;
        }

        .info-middle img {
            float: left;
            margin-right: 6px;
        }
    </style>
<title>地图信息</title>
</head>

<body data-lon=${lon } data-lat=${lat }>

<div id="container"></div>
  
</body>
<script>
var lon,lat;
var lonnum=$('body').attr('data-lon')-0
var latnum= $('body').attr('data-lat')-0
var list= new Array(lonnum,latnum)
var map = new AMap.Map("container", {
    resizeEnable: true,
    center: list,
    zoom: 15
});
addMarker();

//添加marker标记
function addMarker() {
    map.clearMap();
    var marker = new AMap.Marker({
        map: map,
        position: list
    });
    //鼠标点击marker弹出自定义的信息窗体
    AMap.event.addListener(marker, 'click', function () {
        infoWindow.open(map, marker.getPosition());
    });
}

//实例化信息窗体
var title = '方恒假日酒店<span style="font-size:11px;color:#F00;">价格:318</span>',
    content = [];
content.push("<img src='http://tpc.googlesyndication.com/simgad/5843493769827749134'>地址：北京市朝阳区阜通东大街6号院3号楼东北8.3公里");
content.push("电话：010-64733333");
content.push("<a href='https://ditu.amap.com/detail/B000A8URXB?citycode=110105'>详细信息</a>");
var infoWindow = new AMap.InfoWindow({
    isCustom: true,  //使用自定义窗体
    content: createInfoWindow(title, content.join("<br/>")),
    offset: new AMap.Pixel(16, -45)
});

//构建自定义信息窗体
function createInfoWindow(title, content) {
    var info = document.createElement("div");
    info.className = "custom-info input-card content-window-card";

    //可以通过下面的方式修改自定义窗体的宽高
    //info.style.width = "400px";
    // 定义顶部标题
    var top = document.createElement("div");
    var titleD = document.createElement("div");
    var closeX = document.createElement("img");
    top.className = "info-top";
    titleD.innerHTML = title;
    closeX.src = "https://webapi.amap.com/images/close2.gif";
    closeX.onclick = closeInfoWindow;

    top.appendChild(titleD);
    top.appendChild(closeX);
    info.appendChild(top);

    // 定义中部内容
    var middle = document.createElement("div");
    middle.className = "info-middle";
    middle.style.backgroundColor = 'white';
    middle.innerHTML = content;
    info.appendChild(middle);

    // 定义底部内容
    var bottom = document.createElement("div");
    bottom.className = "info-bottom";
    bottom.style.position = 'relative';
    bottom.style.top = '0px';
    bottom.style.margin = '0 auto';
    var sharp = document.createElement("img");
    sharp.src = "https://webapi.amap.com/images/sharp.png";
    bottom.appendChild(sharp);
    info.appendChild(bottom);
    return info;
}

//关闭信息窗体
function closeInfoWindow() {
    map.clearInfoWindow();
}
</script>
</html>
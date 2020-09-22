<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <%@ include file="/WEB-INF/views/public/compWechatFont.jspf"%>
    <title>Document</title>

    <style>
        #main {
            margin-top: 50px;
            border: 1px solid black;
            cursor: pointer;
        }

        #qrcode img {
            position: absolute;
            top: 50%;
            left: 50%;
            width: 100px;
            height: 100px;
            display: block;
        }

        #qrcode {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.6);
            z-index: 9999;
            display: none;
        }
    </style>

    <script src="http://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
</head>
<body>
    <button id="main">你是想要这种效果吗？？？</button>
    
    <div id="qrcode">
        <img id="image" width="100%" height="100%" src="https://jkooll.github.io/src/images/wechat_qrcode.jpg">
    </div>

    <script>
        $(function() {
            $("#main").click(function() {
                $("#qrcode").fadeIn("slow");
            });

            $("#qrcode").click(function() {
                $("#qrcode").fadeOut("slow");
            })
        });
    </script>
</body>
</html>
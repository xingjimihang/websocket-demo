<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>websocket通讯</title>
</head>
<script>
    var socket;
    function openSocket() {
        var userId = document.getElementById("userId");
        var replyText = document.getElementById("relpyText");
        if(typeof WebSocket != 'undefined') {
            alert("您的浏览器支持WebSocket");
            //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
            //等同于socket = new WebSocket("ws://localhost:8888/xxxx/im/25");
            var socketUrl="http://localhost:8080/ws/"+userId.value;
            socketUrl=socketUrl.replace("https","ws").replace("http","ws");
            if(socket!=null){
                socket.close();
                socket=null;
            }
            socket = new WebSocket(socketUrl);
            //打开事件
            socket.onopen = function() {
                alert("websocket已打开");
                //socket.send("这是来自客户端的消息" + location.href + new Date());
            };
            //获得消息事件
            socket.onmessage = function(msg) {
                replyText.innerText = msg.data;
                //发现消息进入    开始处理前端触发逻辑
            };
            //关闭事件
            socket.onclose = function() {
                alert("websocket已关闭");
            };
            //发生了错误事件
            socket.onerror = function() {
                alert("websocket发生了错误");
            }
        }else{
            alert("您的浏览器不支持WebSocket");
        }
    }
    function sendMessage() {
        var toUserId = document.getElementById("toUserId");
        var content = document.getElementById("contentText");
        if(typeof(WebSocket) == "undefined") {
            alert("您的浏览器不支持WebSocket");
        }else {
            alert("您的浏览器支持WebSocket");
            socket.send('{"toUserId":"'+toUserId.value+'","content":"'+content.value+'"}');
        }
    }
</script>
<body>
    <p>【userId】：
    <div><input id="userId" name="userId" type="text" value="10"></div>
    <p>【toUserId】：
    <div><input id="toUserId" name="toUserId" type="text" value="20"></div>
    <p>【toUserId】：
    <div><input id="contentText" name="contentText" type="text" value="hello websocket"></div>
    <p>【操作】：
    <div><a onclick="openSocket()">开启socket</a></div>
    <p>【操作】：
    <div><a onclick="sendMessage()">发送消息</a></div>
    <div id="relpyText"></div>
</body>
</html>
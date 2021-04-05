package com.example.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketServer {

    /**
     * 用来存放每个客户端的websocket服务
     */
    private static ConcurrentHashMap<String, WebSocketServer> servers = new ConcurrentHashMap<>();

    /**
     * 与客户端的连接会话，需要通过他对客户端发送消息
     */
    private Session session;

    /**
     * 接收到的当前用的id
     */
    private String userId;

    /**
     * 开启连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (servers.containsKey(userId)) {
            servers.remove(userId);
            servers.put(userId, this);
        } else {
            servers.put(userId, this);
        }
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        if (servers.containsKey(userId)) {
            servers.remove(userId);
        }
        System.out.println("用户：" + userId + "退出");
    }

    /**
     * 接受客户端消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("用户消息：" + userId + "===报文：" + message);
        //可以群发消息
        //可以将消息保存到数据库或者保存到redis中
        if (StringUtils.isNotBlank(message)) {
            try {
                JSONObject jsonObject = (JSONObject) JSON.parse(message);
                jsonObject.put("fromUserId", userId);
                String toUserId = jsonObject.getString("toUserId");
                if (StringUtils.isNotBlank(toUserId) && servers.containsKey(toUserId)) {
                    servers.get(toUserId).sendMessage(jsonObject.getString("content"));
                } else {
                    System.out.println("请求的用户：" + toUserId + "不在线");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给客户端推送消息
     */
    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws Exception {
        if (StringUtils.isNotBlank(userId) && servers.containsKey(userId)) {
            servers.get(userId).sendMessage(message);
        } else {
            System.out.println("用户" + userId + ",不在线！");
        }
    }
}

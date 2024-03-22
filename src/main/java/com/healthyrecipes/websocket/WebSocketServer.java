package com.healthyrecipes.websocket;

import com.healthyrecipes.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:86198
 * @DATE:2024/3/19 10:38
 * @DESCRIPTION: WebSocket
 * @VERSION:1.0
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{userid}")
public class WebSocketServer {


    //存放会话对象
    private static Map<Integer, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userid") Integer userid) {
        log.info("{} 与服务器进行连接.",userid);
        sessionMap.put(userid , session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userid") String userid) {
        log.info("用户：{}，发送信息：{}",userid,message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param userid
     */
    @OnClose
    public void onClose(@PathParam("userid") Integer userid) {
        log.info("{} :关闭连接" , userid);
        sessionMap.remove(userid);
    }

    /**
     * 给用户发送信息
     *
     * @param message
     */
    public void sendAIResultToUser(Integer userid,String message) {

        //获得对应的session
        Session session = sessionMap.get(userid);

        try {
            //服务器向客户端发送消息
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 通过userId获得一个session
     * @param: [java.lang.Integer]
     * @return: javax.websocket.Session
     */
    public Session getSessionByUserId(Integer userid){
        return sessionMap.get(userid);
    }
}

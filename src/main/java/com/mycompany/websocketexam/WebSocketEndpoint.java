package com.mycompany.websocketexam;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/websocket")
public class WebSocketEndpoint implements Serializable {

    private static final Logger logger 
            = LoggerFactory.getLogger(WebSocketEndpoint.class);

    @EJB
    private QueueSenderSessionBean senderBean;
    
    private static final Set<Session> sessions 
            = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        logger.info("Session Open {}", session.getId());
        session.getAsyncRemote().sendText("Session Opened");
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(final String message, final Session client) {
        logger.info("Call QueueSenderSessionBean");
        senderBean.sendMessage(message);
    }

    @OnClose
    public void onClose(final Session session) {
        logger.info("Session Close {}", session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("Error", t);
    }

    public void onJMSMessage(@Observes @WSJMSMessage Message msg) {
        logger.info("Receive CDI Event {}", msg);

        sessions.forEach(s -> {
            try {
                s.getBasicRemote().sendText(msg.getBody(String.class));
            } catch (IOException | JMSException ex) {
                logger.error("Error", ex);
            }
        });

    }
}

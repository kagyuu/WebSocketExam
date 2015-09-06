package com.mycompany.websocketexam;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(mappedName = "jms/myQueue")
public class WebSocketMDB implements MessageListener {
    
    private static final Logger logger 
            = LoggerFactory.getLogger(WebSocketMDB.class);

    @Inject
    @WSJMSMessage
    private Event<Message> jmsEvent;

    @Override
    public void onMessage(Message msg) {
        logger.info("Fire CDI Event");
        jmsEvent.fire(msg);
    }
}

package com.mycompany.websocketexam;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocalBean
@Stateless
public class QueueSenderSessionBean {

    private static final Logger logger 
            = LoggerFactory.getLogger(QueueSenderSessionBean.class);

    @Resource(mappedName = "jms/myQueue")    
    private Queue myQueue;

    @Inject
    private JMSContext jmsContext;

    public void sendMessage(String message) {
        logger.info("Enqueu Message to JMS");        
        jmsContext.createProducer().send(myQueue, message);
    }

}

package com.mycompany.nats.java.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.nats.client.Nats;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.jboss.logging.Logger;

//@ApplicationScoped
@Singleton
@Startup
public class MessageRecieverQueue {

    private final Logger log = Logger.getLogger(MessageRecieverQueue.class);
    private Connection natsConnection;
    private Dispatcher dispatcher;
    
    @PostConstruct
    public void init() {
        log.info("MessageRecieverQueue in init()");
        dispatcher = null;
        natsConnection = null;
        
        try {
            String url = "nats://nats:4222";
            natsConnection = Nats.connect(url); // localhost on port 4222
            
            // Create a message dispatcher for handling messages in a separate thread
            dispatcher = natsConnection.createDispatcher((msg) -> {
                log.infof("%s on subject %s\n",
                    new String(msg.getData(), StandardCharsets.UTF_8),
                    msg.getSubject());
            });

            dispatcher.subscribe("foo.bar");
            
        } catch (Exception e) {
            log.errorf("MessageRecieverQueue caught exception: %s\n", e.getMessage());
        } finally {

        }
        log.info("MessageRecieverQueue in init() END");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("MessageRecieverQueue in preDestroy()");
        
        if(dispatcher!=null) {
            dispatcher.unsubscribe("foo.bar");
        }
        
        if(natsConnection!=null) {
            try {
                natsConnection.close();
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            }
        }
    }

}

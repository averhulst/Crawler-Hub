package service.messaging;

import application.hub.Config;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.*;

public class MessengerImpl implements Messenger{
    private ConnectionFactory connectionFactory;
    private Map<String, Queue> queues;

    public MessengerImpl(){
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Config.ENVIRONMENT.MESSAGING_SERVICE_ADDRESS);
        connectionFactory.setUsername(Config.ENVIRONMENT.MESSAGING_SERVICE_USER_NAME);
        connectionFactory.setPassword(Config.ENVIRONMENT.MESSAGING_SERVICE_PASS);
        queues = new HashMap<String, Queue>();

        try {
            Connection connection = connectionFactory.newConnection();

            queues.put("freshDomains",
                    new QueueImpl(connection.createChannel(), "freshDomains")
            );

            queues.put("discoveredDomains",
                    new QueueImpl(connection.createChannel(), "discoveredDomains")
            );

            queues.put("crawlResults",
                    new QueueImpl(connection.createChannel(), "crawlResults")
            );


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Queue getQueue(String queueName){
        return queues.get(queueName);
    }

}


package messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.*;

public class MessagingFacade implements Messenger{
    private ConnectionFactory connectionFactory;
    private Map<String, Consumer> consumers;
    private Map<String, Publisher> publishers;

    public MessagingFacade(){
        this.consumers = new HashMap<>();
        this.publishers = new HashMap<>();

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            Connection connection = connectionFactory.newConnection();

            publishers.put("freshDomains",
                    new Publisher(connection.createChannel(), "freshDomains")
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void publishFreshDomains(List domains){
        String freshDomains = String.join(";", domains);
        publishers.get("freshDomains").publishMessage(freshDomains);
    }

    public String fetchCrawlResult(){
        return consumers.get("crawlResults").getMessage();
    }
    public String fetchDiscoveredDomains(){
        return consumers.get("discoveredDomains").getMessage();
    }
}


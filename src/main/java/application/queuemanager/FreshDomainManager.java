package application.queuemanager;

import application.dao.DomainStoreDAO;
import application.hub.Config;
import com.mongodb.*;
import com.rabbitmq.client.AMQP;
import service.messaging.Queue;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class FreshDomainManager extends QueueManager {
    private DBCollection persistentDomainQueueStore;
    private DB db;
    private final static Logger LOGGER = Logger.getLogger(FreshDomainManager.class.getName());
    private int desiredQueueSize = Config.FRESH_DOMAIN_QUEUE_DESIRED_SIZE;
    private Mongo mongo;

    public FreshDomainManager(Queue queue, DomainStoreDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.threadPool = threadPool;

        try {
            mongo = new MongoClient(
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_ADDRESS,
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_PORT
            );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.db = mongo.getDB("tomato");

        this.persistentDomainQueueStore=  db.getCollection("domainQueue");
        LOGGER.info("FreshDomainManager running!");

        if(queue.getQueueSize() == 0){
            queue.publishMessages(produceDomainSeeds());
        }
    }

    public void tick(){
        produceFreshDomains();
    }

    private void produceFreshDomains(){
        while(queue.getQueueSize() < desiredQueueSize){
            DBObject query = new BasicDBObject();
            DBObject domain = persistentDomainQueueStore.findAndRemove(query);
            String newDomain = domain.get("url").toString();

            if(newDomain.length() > 0){
                queue.publishMessage(newDomain);
            }else{
                LOGGER.warning("Domain store empty, unable to supply crawlable domains to the job queue! \n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private List produceDomainSeeds(){
        return new ArrayList<String>(){{
            add("http://animagraffs.com/");
            add("http://jgrapht.org/");
            add("http://www.pixijs.com/");
            add("http://www.draw2d.org/");
            add("http://www.reddit.com/");
            add("http://www.cnn.com/");
            add("http://www.stackoverflow.com/");
            add("http://www.theguardian.com/");
            add("http://www.newsweek.com/");
            add("http://www.usatoday.com/");
            add("http://www.digg.com/");
            add("http://www.anandtech.com/");
            add("http://www.tomshardware.com/");
        }};
    }
}

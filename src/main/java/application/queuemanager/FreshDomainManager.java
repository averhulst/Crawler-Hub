package application.queuemanager;

import application.dao.DomainStoreDAO;
import application.hub.Config;
import service.messaging.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class FreshDomainManager extends QueueManager {
    private DomainStoreDAO domainStore;
    private final static Logger LOGGER = Logger.getLogger(DiscoveredDomainManager.class.getName());
    private int desiredQueueSize = Config.FRESH_DOMAIN_QUEUE_DESIRED_SIZE;

    public FreshDomainManager(Queue queue, DomainStoreDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.threadPool = threadPool;
        this.domainStore = dao;
        this.threadPool = threadPool;
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
            if(domainStore.getSize() > 0 ){
                queue.publishMessage(domainStore.getNextDomain());
            }else{
                LOGGER.warning("Domain store empty!");
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

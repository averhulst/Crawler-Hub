package application.queuemanager;

import Util.Util;
import application.dao.DomainStoreDAO;
import application.hub.Config;
import com.mongodb.*;
import org.json.JSONArray;
import service.messaging.Queue;

import javax.sound.midi.SysexMessage;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class DiscoveredDomainManager extends QueueManager {
    private DomainStoreDAO domainStore;
    private final static Logger LOGGER = Logger.getLogger(DiscoveredDomainManager.class.getName());
    private DBCollection persistentDomainQueueStore;
    private DBCollection crawledDomains;
    private DB db;
    private Mongo mongo;

    public DiscoveredDomainManager(Queue queue, DomainStoreDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        try {
            mongo = new MongoClient(
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_ADDRESS,
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_PORT
            );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.db = mongo.getDB("tomato");
        this.threadPool = threadPool;
        this.domainStore = dao;
        this.persistentDomainQueueStore =  db.getCollection("domainQueue");
        this.crawledDomains =  db.getCollection("crawledDomains");
        LOGGER.info("DiscoveredDomainManager running!");

    }

    public void tick(){
        pollDiscoveredDomainQueue();
    }

    private void pollDiscoveredDomainQueue() {
        String message = queue.getMessage();

        if (message.length() == 0) {
            try {
                LOGGER.warning("Discovered Domain job queue returned empty message. probably empty! \n");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        String[] discoveredDomains = message.split(";");

        for(String domain : discoveredDomains) {
            DBObject crawledWhere  = new BasicDBObject("url", domain);
            DBCursor crawledQueryResult =  crawledDomains.find(crawledWhere);

            if(crawledQueryResult.size() == 0) {
                DBObject queuedWhere  = new BasicDBObject("url", domain);
                DBCursor queuedQueryResult =  persistentDomainQueueStore.find(queuedWhere);

                if(queuedQueryResult.size() == 0){
                    persistentDomainQueueStore.insert(new BasicDBObject("url", domain));
                }
            }
        }

    }

}

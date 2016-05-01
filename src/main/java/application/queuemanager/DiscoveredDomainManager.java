package application.queuemanager;

import application.dao.DomainStoreDAO;
import application.hub.Config;
import service.messaging.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class DiscoveredDomainManager extends QueueManager {
    private DomainStoreDAO domainStore;
    private final static Logger LOGGER = Logger.getLogger(DiscoveredDomainManager.class.getName());

    public DiscoveredDomainManager(Queue queue, DomainStoreDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.threadPool = threadPool;
        this.domainStore = dao;
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

        LOGGER.warning("Got a message! \n" + message.length());

    }

}

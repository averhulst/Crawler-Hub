package application.hub;

import application.dao.CrawlResultsImpl;
import application.dao.DomainStoreImpl;
import application.queuemanager.DiscoveredDomainManager;
import application.queuemanager.FreshDomainProcessor;
import application.queuemanager.QueueManager;
import service.messaging.MessengerImpl;
import service.messaging.Messenger;
import application.queuemanager.CrawlResultManager;

import java.util.concurrent.Executors;

public class Hub {
    private Messenger messenger;
    private QueueManager crawlResultManager;
    private QueueManager discoveredDomainManager;
    private QueueManager freshDomainManager;

    public Hub() {
        messenger = new MessengerImpl();

        crawlResultManager = new CrawlResultManager(
                messenger.getQueue("crawlResults"),
                CrawlResultsImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );

        discoveredDomainManager =  new DiscoveredDomainManager(
                messenger.getQueue("discoveredDomains"),
                DomainStoreImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );

        freshDomainManager =  new FreshDomainProcessor(
                messenger.getQueue("freshDomains"),
                DomainStoreImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );
    }

    public void run(){
        crawlResultManager.run();
        discoveredDomainManager.run();
        freshDomainManager.run();
    }

}

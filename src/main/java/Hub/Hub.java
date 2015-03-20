package Hub;

import dao.CrawledDomainDAO;
import dao.CrawledDomainImpl;
import dao.DomainQueueDAO;
import dao.DomainQueueImpl;
import messaging.MessagingFacade;
import messaging.Messenger;
import processor.CrawlResultProcessor;
import processor.DiscoveredDomainProcessor;
import processor.Processor;

import java.util.concurrent.Executors;

public class Hub {
    private Messenger messenger;
    private Processor crawlResultProcessor;
    private Processor discoveredDomainProcessor;

    public Hub() {
        messenger = new MessagingFacade();

        crawlResultProcessor = new CrawlResultProcessor(
                messenger,
                CrawledDomainImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );

        discoveredDomainProcessor =  new DiscoveredDomainProcessor(
                messenger,
                DomainQueueImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );
    }

    public void run(){
        crawlResultProcessor.run();
        discoveredDomainProcessor.run();
    }

}

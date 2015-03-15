package Hub;

import messaging.MessagingFacade;
import messaging.Messenger;
import processor.CrawlResultProcessor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Hub {
    private Messenger messenger;
    private DAO crawledDomainDao;
    private DAO discoveredDomainDao;
    private CrawlResultProcessor crawlResultProcessor;
    private CrawlResultProcessor discoveredDomainProcessor;

    public Hub() {
        crawledDomainDao = new DAO("crawledDomains");
        messenger = new MessagingFacade();
        crawlResultProcessor =  new CrawlResultProcessor(messenger, crawledDomainDao, Executors.newSingleThreadExecutor());

        discoveredDomainDao = new DAO("crawledDomainIndex");
        discoveredDomainProcessor =  new CrawlResultProcessor(messenger, discoveredDomainDao, Executors.newSingleThreadExecutor());
    }

    public void run(){
        crawlResultProcessor.run();
        discoveredDomainProcessor.run();

    }

}

package application.hub;

import application.dao.CrawlResultsImpl;
import application.dao.DomainQueueImpl;
import service.messaging.MessengerImpl;
import service.messaging.Messenger;
import application.processor.CrawlResultProcessor;
import application.processor.DiscoveredDomainProcessor;
import application.processor.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Hub {
    private Messenger messenger;
    private Processor crawlResultProcessor;
    private Processor discoveredDomainProcessor;

    public Hub() {
        messenger = new MessengerImpl();

        if(messenger.getQueue("freshDomains").getQueueSize() == 0){
            messenger.getQueue("freshDomains").publishMessages(produceDomainSeeds());
        }

        crawlResultProcessor = new CrawlResultProcessor(
                messenger.getQueue("crawlResults"),
                CrawlResultsImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );

        discoveredDomainProcessor =  new DiscoveredDomainProcessor(
                messenger.getQueue("discoveredDomains"),
                DomainQueueImpl.getInstance(),
                Executors.newSingleThreadExecutor()
        );
    }

    public void run(){
        crawlResultProcessor.run();
        discoveredDomainProcessor.run();
    }

    public List produceDomainSeeds(){
        List<String> domainSeed = new ArrayList<>();

        domainSeed.add("http://animagraffs.com/");
        domainSeed.add("http://jgrapht.org/");
        domainSeed.add("http://www.pixijs.com/resources/");
        domainSeed.add("http://www.draw2d.org/draw2d/");

        return domainSeed;
    }

}

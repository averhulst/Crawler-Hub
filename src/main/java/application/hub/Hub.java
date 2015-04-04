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

        if(messenger.getQueue("freshDomains").getQueueSize() < 4){
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
        //crawlResultProcessor.run();
        //discoveredDomainProcessor.run();
    }

    public List produceDomainSeeds(){
        List<String> domainSeed = new ArrayList<>();

        domainSeed.add("http://animagraffs.com/");
        domainSeed.add("http://jgrapht.org/");
        domainSeed.add("http://www.pixijs.com/");
        domainSeed.add("http://www.draw2d.org/");
        domainSeed.add("http://www.reddit.com/");
        domainSeed.add("http://www.cnn.com/");
        domainSeed.add("http://www.stackoverflow.com/");
        domainSeed.add("http://www.theguardian.com/");
        domainSeed.add("http://www.newsweek.com/");
        domainSeed.add("http://www.usatoday.com/");
        domainSeed.add("http://www.digg.com/");
        domainSeed.add("http://www.anandtech.com/");
        domainSeed.add("http://www.tomshardware.com/");

        return domainSeed;
    }

}

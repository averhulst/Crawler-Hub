package application.processor;

import Util.Util;
import application.dao.CrawlResultsImpl;
import application.dao.CrawledResultsDAO;
import application.dao.DomainQueueDAO;
import service.messaging.Queue;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class DiscoveredDomainProcessor extends Processor {
    private DomainQueueDAO domainQueue;
    private CrawledResultsDAO crawlResults;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DiscoveredDomainProcessor.class);

    public DiscoveredDomainProcessor(Queue queue, DomainQueueDAO dao,  ExecutorService threadPool) {
        this.queue = queue;
        this.threadPool = threadPool;
        this.domainQueue = dao;
        crawlResults = CrawlResultsImpl.getInstance();
    }

    public void run(){
        running = true;
        Runnable r = () -> {
            String message;
            int insertedCount = 0;
            while(running){
                List<String> discoveredDomains = parseDiscoveredDomains(queue.getMessage());
                for(String domain : discoveredDomains){
                    if(!crawlResults.domainHasBeenCrawled(Util.toSha256(domain))){
                        domainQueue.enqueueDomain(domain);
                        insertedCount++;
                    }
                }
                //TODO handle disocvered domains
                log.info("enqueued " + insertedCount + " domains to the fresh domain queue, discarded " + (discoveredDomains.size() - insertedCount));
            }
        };

        super.threadPool.execute(r);
    }

    private List<String> parseDiscoveredDomains(String domains){
        List<String> discoveredDomains = new ArrayList();
        discoveredDomains.addAll(Arrays.asList(domains.split(";")));

        Map domainHash = new HashMap<String, String>();
        for(String domain : discoveredDomains){
            domainHash.put(Util.toSha256(domain), domain);
        }

        return discoveredDomains;
    }
}

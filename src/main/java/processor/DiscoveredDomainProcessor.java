package processor;

import dao.CrawledDomainDAO;
import dao.CrawledDomainImpl;
import Util.Util;
import dao.DomainQueueDAO;
import messaging.Messenger;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class DiscoveredDomainProcessor extends Processor{
    private DomainQueueDAO dao;

    public DiscoveredDomainProcessor(Messenger messenger, DomainQueueDAO dao,  ExecutorService threadPool) {
        this.messenger = messenger;
        this.threadPool = threadPool;
        this.dao = dao;
    }

    public void run(){
        running = true;
        Runnable r = () -> {
            while(running){
                List discoveredDomains = parseDiscoveredDomains(messenger.fetchDiscoveredDomains());

            }
        };

        super.threadPool.execute(r);
    }

    private List parseDiscoveredDomains(String domains){
        List<String> discoveredDomains = new ArrayList();
        discoveredDomains.addAll(Arrays.asList(domains.split(";")));

        Map domainHash = new HashMap<String, String>();
        for(String domain : discoveredDomains){
            domainHash.put(Util.toSha256(domain), domain);
        }

        return discoveredDomains;
    }
}

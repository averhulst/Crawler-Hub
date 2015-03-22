package processor;

import Util.Util;
import dao.DomainQueueDAO;
import messaging.Queue;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class DiscoveredDomainProcessor extends Processor{
    private DomainQueueDAO dao;

    public DiscoveredDomainProcessor(Queue queue, DomainQueueDAO dao,  ExecutorService threadPool) {
        this.queue = queue;
        this.threadPool = threadPool;
        this.dao = dao;
    }

    public void run(){
        running = true;
        Runnable r = () -> {
            String message;
            while(running){
                List discoveredDomains = parseDiscoveredDomains(queue.getMessage());
                //TODO handle disocvered domains
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

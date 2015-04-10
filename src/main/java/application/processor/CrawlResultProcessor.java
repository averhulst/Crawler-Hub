package application.processor;

import application.dao.CrawledResultsDAO;
import Util.Util;
import service.messaging.Queue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class CrawlResultProcessor extends Processor {
    private CrawledResultsDAO dao;
    private final static Logger LOGGER = Logger.getLogger(DiscoveredDomainProcessor.class.getName());

    public CrawlResultProcessor(Queue queue, CrawledResultsDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.dao = dao;
        this.threadPool = threadPool;
        LOGGER.info("CrawlResultProcessor running!");
    }

    public void tick(){
        String message = queue.getMessage();
        if(message != null && message.length() > 0){
            processCrawlResult(message);
        }
    }

    private void processCrawlResult(String message){
        JSONObject crawledDomain = new JSONObject(message);
        if(isValidResult(crawledDomain)){
            String hash = Util.toSha256(crawledDomain.getString("url"));
            crawledDomain.put(hash, message);
            dao.insertCrawlResult(crawledDomain);
            LOGGER.info("Crawl result inserted for domain: " + crawledDomain.get("url").toString());
        }
    }

    private boolean isValidResult(JSONObject crawledDomain){
        String URL;
        JSONArray pages;

        try{
            URL = crawledDomain.get("url").toString();
            pages = crawledDomain.getJSONArray("pages");
        }catch(JSONException e){
            e.printStackTrace();
            //throws on lookups for keys that don't exist, not valid
            return false;
        }

        return URL.length() > 0 && pages.length() > 0;
    }


}

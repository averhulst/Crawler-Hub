package application.processor;

import application.dao.CrawledResultsDAO;
import Util.Util;
import service.messaging.Queue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;

public class CrawlResultProcessor extends Processor {
    private CrawledResultsDAO dao;

    public CrawlResultProcessor(Queue queue, CrawledResultsDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.dao = dao;
        this.threadPool = threadPool;
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
            String hash = Util.toSha256(crawledDomain.get("url").toString());
            crawledDomain.put(hash, crawledDomain);
            dao.insertCrawlResult(crawledDomain);
        }
    }

    private boolean isValidResult(JSONObject crawledDomain){
        String URL;
        JSONArray pages;

        try{
            URL = crawledDomain.get("url").toString();
            pages = crawledDomain.getJSONArray("pages");
        }catch(JSONException e){
            //throws on lookups for keys that don't exist, not valid
            return false;
        }

        return URL.length() > 0 && pages.length() > 0;
    }


}

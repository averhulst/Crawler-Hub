package processor;

import dao.CrawledDomainDAO;
import dao.CrawledDomainImpl;
import Util.Util;
import messaging.Messenger;
import messaging.Queue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;

public class CrawlResultProcessor extends Processor{
    private CrawledDomainDAO dao;

    public CrawlResultProcessor(Queue queue, CrawledDomainDAO dao, ExecutorService threadPool) {
        this.queue = queue;
        this.dao = dao;
        this.threadPool = threadPool;
    }

    public void run(){
        running = true;

        Runnable r = () -> {
            String message;
            while(running){
                if((message = queue.getMessage()) != null){
                    processMessage(message);
                }

            }
        };

        super.threadPool.execute(r);
    }

    private void processMessage(String message){
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

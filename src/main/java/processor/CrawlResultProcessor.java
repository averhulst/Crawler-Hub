package processor;

import dao.CrawledDomainDAO;
import dao.CrawledDomainImpl;
import Util.Util;
import messaging.Messenger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;

public class CrawlResultProcessor extends Processor{
    private CrawledDomainDAO dao;

    public CrawlResultProcessor(Messenger messenger, CrawledDomainDAO dao, ExecutorService threadPool) {
        this.messenger = messenger;
        this.dao = dao;
        this.threadPool = threadPool;
    }

    public void run(){
        running = true;
        Runnable r = () -> {
            while(running){
                JSONObject crawledDomain = new JSONObject(messenger.fetchCrawlResult());
                if(isValidResult(crawledDomain)){
                    String hash = Util.toSha256(crawledDomain.get("url").toString());
                    crawledDomain.put(hash, crawledDomain);
                    dao.insertCrawlResult(crawledDomain);
                }
            }
        };

        super.threadPool.execute(r);
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

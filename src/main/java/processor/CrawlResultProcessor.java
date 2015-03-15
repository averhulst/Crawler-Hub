package processor;

import Hub.DAO;
import Util.Util;
import messaging.Messenger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

public class CrawlResultProcessor extends Processor{

    public CrawlResultProcessor(Messenger messenger, DAO dao, ExecutorService threadPool) {
        super(threadPool, dao, messenger);
    }

    public void run(){
        running = true;
        Runnable r = () -> {
            while(running){
                JSONObject crawledDomain = new JSONObject(messenger.fetchCrawlResult());
                if(isValidResult(crawledDomain)){
                    String hash = Util.toSha256(crawledDomain.get("url").toString());
                    crawledDomain.put(hash, crawledDomain);
                    dao.save(crawledDomain);
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

package application.dao;

import application.hub.Config;
import com.mongodb.*;
import com.mongodb.util.JSON;
import Util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Logger;

public class CrawlResultsImpl implements CrawledResultsDAO {
    private Mongo mongo;
    private DB db;
    private DBCollection crawledDomainsCollection;


    private static CrawledResultsDAO instance = new CrawlResultsImpl();

    public static CrawledResultsDAO getInstance(){
        //TODO watch for likely thread WAIT problems?
        return instance;
    }

    private CrawlResultsImpl() {
        try {
            mongo = new MongoClient(
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_ADDRESS,
                    Config.ENVIRONMENT.CRAWL_RESULTS_DB_PORT
            );
            db = mongo.getDB("tomato");
            crawledDomainsCollection =  db.getCollection("crawledDomains");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(555);
        }
    }

    public synchronized void insertCrawlResult(JSONObject domain){
        BasicDBObject databaseDocument = new BasicDBObject((BasicDBObject)JSON.parse(domain.toString()));

        if(databaseDocument.size() > 0){
            try{
                crawledDomainsCollection.save(databaseDocument);
            }catch(com.mongodb.MongoInternalException e){
                System.err.println("BSON size exceeded");
            }catch (Exception f) {
                f.printStackTrace();
            }
        }
    }

    public synchronized List get(Map where){
        List<String> result = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.putAll(where);

        DBCursor cursor = crawledDomainsCollection.find(query);

        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        return result;
    }

    public synchronized boolean domainHasBeenCrawled(String domainHash){
        BasicDBObject query = new BasicDBObject("_id", domainHash);
        DBCursor cursor = crawledDomainsCollection.find(query);
        return cursor.hasNext();
    }
}

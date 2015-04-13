package application.dao;

import application.hub.Config;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.*;

public class CrawlResultsImpl implements CrawledResultsDAO {
    Mongo mongo;
    DB db;
    DBCollection collection;
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
            collection =  db.getCollection("crawledDomains");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertCrawlResult(JSONObject domain){
        BasicDBObject databaseDocument = new BasicDBObject((BasicDBObject)JSON.parse(domain.toString()));

        if(databaseDocument.size() > 0){
            collection.save(databaseDocument);
        }

    }

    public synchronized List get(Map where){
        List<String> result = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.putAll(where);

        DBCursor cursor = collection.find(query);

        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        return result;
    }

    public synchronized boolean domainHasBeenCrawled(String domainHash){
        BasicDBObject query = new BasicDBObject("_id", domainHash);
        DBCursor cursor = collection.find(query);
        return cursor.hasNext();
    }
}

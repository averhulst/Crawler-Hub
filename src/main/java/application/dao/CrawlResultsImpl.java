package application.dao;

import Util.Util;
import com.mongodb.*;
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
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDB("tomato");
            collection =  db.getCollection("crawledDomains");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertCrawlResult(JSONObject domain){
        BasicDBObject databaseDocument = new BasicDBObject();
        Iterator<String> iterator = domain.keys();
        Map<String, String> outMap = new HashMap<String, String>();

        while(iterator.hasNext()) {
            String name = iterator.next();
            databaseDocument.put(name, domain.getString(name));
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

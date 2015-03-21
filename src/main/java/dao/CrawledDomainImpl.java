package dao;

import com.mongodb.*;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.*;

public class CrawledDomainImpl implements CrawledDomainDAO{
    Mongo mongo;
    DB db;
    DBCollection collection;
    private static CrawledDomainDAO instance = new CrawledDomainImpl();

    public static CrawledDomainDAO getInstance(){
        return instance;
    }

    private CrawledDomainImpl() {
        try {
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDB("tomato");
            collection =  db.getCollection("crawledDomains");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void insertCrawlResult(JSONObject domain){
        BasicDBObject databaseDocument = new BasicDBObject();
        Iterator<String> iterator = domain.keys();
        Map<String, String> outMap = new HashMap<String, String>();

        while(iterator.hasNext()) {
            String name = iterator.next();
            databaseDocument.put(name, domain.getString(name));
        }
    }

    public List get(Map where){
        List<String> result = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.putAll(where);

        DBCursor cursor = collection.find(query);

        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        return result;
    }
}

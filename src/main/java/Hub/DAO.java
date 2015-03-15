package Hub;

import com.mongodb.*;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: buttes
 * Date: 3/10/15
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class DAO {
    Mongo mongo;
    DB db;
    DBCollection collection;

    public DAO(String collectionName) {
        try {
            mongo = new MongoClient("localhost", 27017);
            db = mongo.getDB("tomato");
            collection =  db.getCollection(collectionName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void save(JSONObject domain){
        BasicDBObject databaseDocument = new BasicDBObject();
        Iterator<String> iterator = domain.keys();
        Map<String, String> outMap = new HashMap<String, String>();

        while(iterator.hasNext()) {
            String name = iterator.next();
            databaseDocument.put(name, domain.getString(name));
        }
    }

    private synchronized void save(BasicDBObject databaseDocument){
        //only synchronize private methods for DB operations so a thread doesn't have to wait for other threads to prepare their data
        collection.save(databaseDocument);
    }
}

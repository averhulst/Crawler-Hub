package application.hub;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    public static final Environment ENVIRONMENT;
    public static final int QUEUE_MANAGER_TICK_INTERVAL;
    public static final int FRESH_DOMAIN_QUEUE_DESIRED_SIZE;

    static{
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/config.txt"));
            String line = null;
            sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject configJSON = new JSONObject(sb.toString());

        QUEUE_MANAGER_TICK_INTERVAL     = configJSON.getInt("QUEUE_MANAGER_TICK_RATE");
        FRESH_DOMAIN_QUEUE_DESIRED_SIZE = configJSON.getInt("FRESH_DOMAIN_QUEUE_DESIRED_SIZE");
        ENVIRONMENT                     = new Environment(configJSON.getJSONObject("ENVIRONMENT"));
    }

    public static class Environment {
        public final String CRAWL_RESULTS_DB_ADDRESS;
        public final int    CRAWL_RESULTS_DB_PORT;
        public final String DOMAIN_QUEUE_DB_ADDRESS;
        public final int    DOMAIN_QUEUE_DB_PORT;
        public final String MESSAGING_SERVICE_ADDRESS;
        public final int    MESSAGING_SERVICE_PORT;
        public final String MESSAGING_SERVICE_USER_NAME;
        public final String MESSAGING_SERVICE_PASS;

        public Environment(JSONObject envJSON){
            CRAWL_RESULTS_DB_ADDRESS    =  envJSON.getJSONObject("CRAWL_RESULTS_DB").getString("Address");
            CRAWL_RESULTS_DB_PORT       =  envJSON.getJSONObject("CRAWL_RESULTS_DB").getInt("Port");
            DOMAIN_QUEUE_DB_ADDRESS     =  envJSON.getJSONObject("DOMAIN_QUEUE_DB").getString("Address");
            DOMAIN_QUEUE_DB_PORT        =  envJSON.getJSONObject("DOMAIN_QUEUE_DB").getInt("Port");
            MESSAGING_SERVICE_ADDRESS   =  envJSON.getJSONObject("MESSAGING_SERVICE").getString("Address");
            MESSAGING_SERVICE_PORT      =  envJSON.getJSONObject("MESSAGING_SERVICE").getInt("Port");
            MESSAGING_SERVICE_USER_NAME =  envJSON.getJSONObject("MESSAGING_SERVICE").getString("User_name");
            MESSAGING_SERVICE_PASS      =  envJSON.getJSONObject("MESSAGING_SERVICE").getString("Pass");
        }

    }
}

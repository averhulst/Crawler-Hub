package Util;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Environment {
    public static String CRAWL_RESULTS_DB_ADDRESS;
    public static int CRAWL_RESULTS_DB_PORT;
    public static String DOMAIN_QUEUE_DB_ADDRESS;
    public static int DOMAIN_QUEUE_DB_PORT;
    public static String MESSAGING_SERVICE_ADDRESS;
    public static int MESSAGING_SERVICE_PORT;

    public Environment() {
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("/environment_config.txt"));
            String line = null;
            sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject environmentJSON = new JSONObject(sb.toString());

        CRAWL_RESULTS_DB_ADDRESS    =  environmentJSON.getString("CRAWL_RESULTS_DB_ADDRESS");
        CRAWL_RESULTS_DB_PORT       =  (int)environmentJSON.get("CRAWL_RESULTS_DB_PORT");
        DOMAIN_QUEUE_DB_ADDRESS     =  environmentJSON.getString("DOMAIN_QUEUE_DB_ADDRESS");
        DOMAIN_QUEUE_DB_PORT        =  (int)environmentJSON.get("DOMAIN_QUEUE_DB_PORT");
        MESSAGING_SERVICE_ADDRESS   =  environmentJSON.getString("MESSAGING_SERVICE_ADDRESS");
        MESSAGING_SERVICE_PORT      =  (int)environmentJSON.get("MESSAGING_SERVICE_PORT");

    }

}

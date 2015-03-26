package application.dao;

import org.json.JSONObject;

public interface CrawledResultsDAO {
    public void insertCrawlResult(JSONObject domain);
    public boolean domainHasBeenCrawled(String domainHash);
}

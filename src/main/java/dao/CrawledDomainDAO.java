package dao;

import org.json.JSONObject;

public interface CrawledDomainDAO {
    public void insertCrawlResult(JSONObject domain);
}

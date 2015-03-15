package messaging;

import java.util.List;

public interface Messenger {

    public void publishDiscoveredDomains(List discoveredDomains);

    public void publishFreshDomains(List discoveredDomains);

    public List<String> fetchFreshDomains();

    public String fetchCrawlResult();
}

package messaging;

import java.util.List;

public interface Messenger {

    public void publishFreshDomains(List discoveredDomains);

    public String fetchCrawlResult();

    public String fetchDiscoveredDomains();
}

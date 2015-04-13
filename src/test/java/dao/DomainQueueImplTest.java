package dao;

import application.dao.DomainStoreImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainQueueImplTest {
    private DomainStoreImpl domainStoreImpl;

    @Before
    public void setUp() throws Exception {
        domainStoreImpl = DomainStoreImpl.getInstance();
        domainStoreImpl.flushDb();
        assert(domainStoreImpl.getNextDomain() == null);
    }

    @Test
    public void testEnqueueDomain() throws Exception {
        String[] domains = new String[] {
                "http://www.google.com",
                "http://www.reddit.com",
                "http://www.youtube.com"
        };
        List<String> insertedDomains = new ArrayList(Arrays.asList(domains));
        List<String> retrievedDomains = new ArrayList<>();

        for(String domain : insertedDomains){
            domainStoreImpl.enqueueDomain(domain);
        }

        for(int i = 0 ; i < domains.length ; i++){
            retrievedDomains.add(domainStoreImpl.getNextDomain());
        }

        assertEquals(retrievedDomains,insertedDomains);
    }
}

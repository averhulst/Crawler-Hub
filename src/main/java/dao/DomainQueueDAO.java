package dao;

public interface DomainQueueDAO {
    public void enqueueDomain(String domain);
    public String getNextDomain();
}

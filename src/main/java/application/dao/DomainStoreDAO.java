package application.dao;

public interface DomainStoreDAO {
    public void enqueueDomain(String domain);
    public String getNextDomain();
    public long getSize();
}

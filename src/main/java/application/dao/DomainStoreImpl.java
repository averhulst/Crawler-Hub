package application.dao;

import application.hub.Config;
import application.queuemanager.DiscoveredDomainManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Logger;

public class DomainStoreImpl implements DomainStoreDAO {
    private JedisPool jedisPool;
    private String jedisQueueName;
    private static DomainStoreImpl instance = new DomainStoreImpl();
    private final static Logger LOGGER = Logger.getLogger(DiscoveredDomainManager.class.getName());

    private DomainStoreImpl() {
        jedisQueueName = "domainQueue";
        connect();
    }

    public static DomainStoreImpl getInstance(){
        return instance;
    }

    public String getNextDomain(){
        Jedis jedis = jedisPool.getResource();
        String nextdomain = null;
        try{
            nextdomain = jedis.spop(jedisQueueName);
            System.out.println(getSize());
            LOGGER.warning("size: " + getSize());
            //spop will return a random item from the set, not going to care about order for now
        }finally{
            jedisPool.returnResource(jedis);
        }

        return nextdomain;
    }


    public void enqueueDomain(String domain){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.sadd(jedisQueueName, domain);
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    public long getSize(){
        Jedis jedis = jedisPool.getResource();
        long size = jedis.scard(jedisQueueName);
        jedisPool.returnResource(jedis);
        return size;
    }

    private void connect(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(4);
        jedisPool = new JedisPool(
                poolConfig,
                Config.ENVIRONMENT.DOMAIN_QUEUE_DB_ADDRESS,
                Config.ENVIRONMENT.DOMAIN_QUEUE_DB_PORT
        );
    }
    public void flushDb(){
        Jedis jedis = jedisPool.getResource();

        try{
            jedis.flushDB();
        }finally{
            jedisPool.returnResource(jedis);
        }
    }
}

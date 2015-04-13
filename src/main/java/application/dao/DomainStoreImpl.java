package application.dao;

import application.hub.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class DomainStoreImpl implements DomainStoreDAO {
    private JedisPool jedisPool;
    private String jedisQueueName;
    private static DomainStoreImpl instance = new DomainStoreImpl();

    private DomainStoreImpl() {
        jedisQueueName = "domainQueue";
        connect();
    }
    public static DomainStoreImpl getInstance(){
        return instance;
    }

    public String getNextDomain(){
        Jedis jedis = jedisPool.getResource();
        String nextdomain;

        try{
            nextdomain = jedis.spop(jedisQueueName);
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
        return jedisPool.getResource().scard(jedisQueueName);
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

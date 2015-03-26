package application.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class DomainQueueImpl implements DomainQueueDAO{
    private JedisPool jedisPool;
    private String jedisQueueName;
    private static DomainQueueImpl instance = new DomainQueueImpl();

    private DomainQueueImpl() {
        jedisQueueName = "domainQueue";
        connect();
    }
    public static DomainQueueImpl getInstance(){
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
        return jedisPool.getResource().llen(jedisQueueName);
    }

    private void connect(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(4);
        jedisPool = new JedisPool(poolConfig, "localhost");
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

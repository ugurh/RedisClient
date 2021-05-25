package io.hrnugr.config;

import io.hrnugr.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static io.hrnugr.util.Messages.HOST_IS_EMPTY;
import static io.hrnugr.util.Messages.PORT_IS_EMPTY;

@Slf4j
public class RedisClient {
    private static final RedisClient instance = new RedisClient();
    private static String host = null;
    private static Integer port = null;
    private static JedisPool pool = null;

    private RedisClient() {
    }

    public static RedisClient getInstance() {
        setConnection();
        getInstance(host, port);
        return instance;
    }

    /**
     *
     * @param redisHost Redis host address
     * @param redisPort Redis post name
     * @return Create RedisClient instance
     */
    private static RedisClient getInstance(String redisHost, Integer redisPort) {
        if (StringUtils.isBlank(redisHost)) {
            throw new RuntimeException(HOST_IS_EMPTY);
        }
        if (redisPort == null) {
            throw new RuntimeException(PORT_IS_EMPTY);
        }
        host = redisHost;
        port = redisPort;
        if (pool == null) {
            createConnection();
        }
        return instance;
    }

    /**
     * Set redis connection config
     */
    private static void setConnection() {
        host = Constants.HOST;
        port = Constants.PORT;
    }

    /**
     * Create Redis Connection
     */
    private static void createConnection() {
        if (pool == null) {
            synchronized (RedisClient.class) {
                if (null == pool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    // Maximum active connections to Redis instance
                    poolConfig.setMaxTotal(128);
                    // Number of connections to Redis that just sit there and do nothing
                    poolConfig.setMaxIdle(200);
                    // Minimum number of idle connections to Redis - these can be seen as always open and ready to serve
                    poolConfig.setMinIdle(50);
                    // Maximum number of connections to test in each idle check
                    poolConfig.setNumTestsPerEvictionRun(10);
                    // Idle connection checking period
                    poolConfig.setTimeBetweenEvictionRunsMillis(5000);
                    //Tests whether connection is dead when connection retrieval method is called
                    poolConfig.setTestOnBorrow(true);
                    // Tests whether connection is dead when returning a connection to the pool
                    poolConfig.setTestOnReturn(true);
                    // Tests whether connections are dead during idle periods
                    poolConfig.setTestWhileIdle(true);
                    pool = new JedisPool(poolConfig, host, port, 100000);
                    /**
                     *
                    pool = new JedisPool(poolConfig, host, port, connectTimeout,operationTimeout,password, db,
                       clientName, useSsl, sslSocketFactory, sslParameters, hostnameVerifier);
                    */
                }
            }
        }
    }

    /**
     * @return boolean is connect to Redis
     */
    public static boolean isConnected() {
        Jedis jedis = getJedis();
        return jedis.isConnected();
    }

    /**
     * Destroy connection
     */
    public void destroyConnection() {
        pool.destroy();
        pool = null;
    }

    /**
     * Get Jedis
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    public void returnJedis(Jedis jedis) {
        if (jedis != null){
            pool.returnResource(jedis);
        }
    }

    /**
     *
     * @param jedis
     * Close jedis
     */
    public void closeJedis(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            closeBrokenResource(jedis);
        }
    }

    /**
     * Return jedis connection to the pool,
     * Call different return methods depends on whether the connection is broken
     */
    public void closeBrokenResource( Jedis jedis ) {
        try {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
        } catch ( Exception e ) {
            destroyJedis(jedis);
        }
    }

    /**
     * Forcibly destroy Jedis outside Jedis Pool
     */
    public static void destroyJedis( Jedis jedis ) {
        if ( jedis != null ) {
            try {
                jedis.quit();
            } catch ( Exception e ) {
                log.error(" >>> RedisUtil-jedis.quit() : " + e );
            }

            try {
                jedis.disconnect();
            } catch ( Exception e ) {
                log.error( ">>> RedisUtil-jedis.disconnect() : " + e );
            }
        }
    }

}

package io.hrnugr.cache;

import com.google.gson.Gson;
import io.hrnugr.config.RedisClient;
import io.hrnugr.entity.GenUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

import static io.hrnugr.util.Messages.KEY_IS_EMPTY;
import static io.hrnugr.util.Messages.VALUE_IS_EMPTY;

@Slf4j
public class GenUnitCache {
    private static GenUnitCache instance = null;
    RedisClient redisClient = RedisClient.getInstance();
    private final String groupName = "GenUnitCache:";

    Gson gson;

    private GenUnitCache() {
        super();
        gson = new Gson();
    }

    public static GenUnitCache getInstance() {
        if (instance == null) {
            instance = new GenUnitCache();
        }
        return instance;
    }

    public GenUnit getUnit(String unitId) {
        GenUnit result = null;
        if (StringUtils.isBlank(unitId)) {
            throw new RuntimeException(KEY_IS_EMPTY);
        }
        Jedis jedis = RedisClient.getJedis();
        try {
            String genUnit = jedis.get(groupName + unitId);
            result = gson.fromJson(genUnit, GenUnit.class);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        } finally {
            redisClient.closeJedis(jedis);
        }

        return result;
    }

    /**
     *
     */
    public void save(List<GenUnit> unitList) {
        String code = null;

        if (unitList == null || unitList.size() == 0)
            throw new RuntimeException(VALUE_IS_EMPTY);

        Jedis jedis = RedisClient.getJedis();

        try {
            for (int i = 0; i < unitList.size(); i++) {
                GenUnit item =  unitList.get(i);
                code = jedis.set((groupName + item.getUnitId()), gson.toJson(item));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            redisClient.closeJedis(jedis);
        }
    }

    public void delete(String genUnitId) {
        if (genUnitId == null) {
            throw new RuntimeException(KEY_IS_EMPTY);
        }
        Jedis jedis = RedisClient.getJedis();
        try {
            jedis.del(groupName + genUnitId);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        } finally {
            redisClient.closeJedis(jedis);
        }
    }
}

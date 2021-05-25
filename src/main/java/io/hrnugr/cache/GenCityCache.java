package io.hrnugr.cache;

import io.hrnugr.config.RedisClient;
import io.hrnugr.entity.GenCity;
import io.hrnugr.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.hrnugr.util.Messages.KEY_IS_EMPTY;
import static io.hrnugr.util.Messages.VALUE_IS_EMPTY;

@Slf4j
public class GenCityCache {
    private final String groupName = "GenCityCache:";
    RedisClient redisClient = RedisClient.getInstance();
    private static GenCityCache instance = null;

    private GenCityCache() {
        super();
    }

    public static GenCityCache getInstance() {
        if (instance == null) {
            instance = new GenCityCache();
        }
        return instance;
    }

    public GenCity getCity(String cityId) {
        GenCity result = null;

        if (StringUtils.isBlank(cityId)) {
            throw new RuntimeException(KEY_IS_EMPTY);
        }
        Jedis jedis = redisClient.getJedis();
        try {
            byte[]  genCity=  jedis.get((groupName + cityId).getBytes(StandardCharsets.UTF_8));
            result = (GenCity) SerializeUtil.deserialize(genCity);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        } finally {
            redisClient.closeJedis(jedis);
        }
        return result;
    }


    public   String save(List<GenCity> cityList) {
        String code = null;

        if (cityList == null || cityList.size() == 0)
            throw new RuntimeException(VALUE_IS_EMPTY);

        Jedis jedis = redisClient.getJedis();

        try {
            for (int i = 0; i < cityList.size(); i++) {
                GenCity item = (GenCity) cityList.get(i);
                code = jedis.set((groupName + item.getId()).getBytes(StandardCharsets.UTF_8), SerializeUtil.serialize(item));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            redisClient.closeJedis(jedis);
        }
        return code;
    }

}

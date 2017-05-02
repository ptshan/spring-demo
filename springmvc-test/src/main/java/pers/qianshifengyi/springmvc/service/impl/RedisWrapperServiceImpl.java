package pers.qianshifengyi.springmvc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import pers.qianshifengyi.springmvc.service.RedisWrapperService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangshan on 17/4/21.
 */
public class RedisWrapperServiceImpl implements RedisWrapperService {

    private static final Logger logger = LoggerFactory.getLogger(RedisWrapperServiceImpl.class);

    // key序列化可以不要,但redisTemplate需要byte[]所以要加上
    private final StringRedisSerializer keySerializer = new StringRedisSerializer();

    private final JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long del(final String... keys) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keySerializer.serialize(keys[i]));
                }
                return result;
            }
        });
    }

    @Override
    public Boolean expire(final String key,final long timeout,final TimeUnit unit) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.expire(keySerializer.serialize(key),unit.toSeconds(timeout));
            }
        });
    }


    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key,value);
                if(liveTime > 0){
                    connection.expire(key,liveTime);
                }
                return 0L;
            }
        });
    }

    @Override
    public void set(String key, Object value, long liveTime) {
        this.set(keySerializer.serialize(key),valueSerializer.serialize(value),liveTime);
    }

    @Override
    public void set(String key, Object value) {
        this.set(key,value,0);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return null;
            }
        });
    }

    @Override
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean exists(final String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(keySerializer.serialize(key));
            }
        });
    }

    @Override
    public String flushDB() {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "OK";
            }
        });
    }

    @Override
    public Long dbSize() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    @Override
    public String ping() {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    /**
     * 返回到期时间的毫秒
     * @param key
     * @return
     */
    @Override
    public Long getExpire(final String key) {
        // redis取到的时间是秒
        Long expireSeconds = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // 查看键的生存时间用TTL命令
                return connection.ttl(keySerializer.serialize(key));
            }
        });
        if(expireSeconds == null || expireSeconds <=0 ){
            return expireSeconds;
        }
        Calendar currCal = Calendar.getInstance();
        currCal.add(Calendar.SECOND,expireSeconds.intValue());
        return currCal.getTimeInMillis();
    }

}

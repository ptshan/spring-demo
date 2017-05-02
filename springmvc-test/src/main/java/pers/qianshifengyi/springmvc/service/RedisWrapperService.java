package pers.qianshifengyi.springmvc.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangshan on 17/4/21.
 */
public interface RedisWrapperService {

        Long del(String... keys);

        Boolean expire(String key, long timeout, TimeUnit unit);

        void set(final byte[] key, final byte[] value, final long liveTime);

        void set(String key, Object value, long liveTime);

        void set(String key, Object value);

        Object get(String key);

        Set keys(String pattern);

        Boolean exists(String key);

        String flushDB();

        Long dbSize();

        String ping();

        Long getExpire(String key);

}

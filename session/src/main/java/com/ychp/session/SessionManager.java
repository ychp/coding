package com.ychp.session;

import com.google.common.collect.Maps;
import com.ychp.coding.common.util.JsonMapper;
import com.ychp.redis.dao.JedisTemplate;
import com.ychp.session.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 2017/6/26
 */
@Slf4j
public class SessionManager {

    private final JedisTemplate jedisTemplate;

    private final Integer maxAge;

    @Autowired
    public SessionManager(Integer maxAge, JedisTemplate jedisTemplate) {
        this.maxAge = maxAge;
        this.jedisTemplate = jedisTemplate;
    }

    public void save(String sessionId, Long userId) {
        Map<String, Object> sessionContent = Maps.newHashMap();
        sessionContent.put("userId", userId);
        save(sessionId, sessionContent, null);
    }

    public void save(String sessionId, Long userId, Integer expired) {
        Map<String, Object> sessionContent = Maps.newHashMap();
        sessionContent.put("userId", userId);
        save(sessionId, sessionContent, expired);
    }

    public void save(String sessionId, Map<String, Object> sessionContent, Integer expired) {
        jedisTemplate.excute(jedis -> {
            String value = JsonMapper.JSON_NON_DEFAULT_MAPPER.toJson(sessionContent);
            Integer finalExpire = expired == null ? maxAge : expired;
            jedis.setex(SessionUtils.getSessionKey(sessionId), finalExpire, value);
            return true;
        });
    }

    public Boolean checkLogin(String sessionId) {
        return jedisTemplate.excute(jedis -> {
            String sessionContent = jedis.get(SessionUtils.getSessionKey(sessionId));
            return StringUtils.isNotBlank(sessionContent);
        });
    }

    public void refreshValid(String sessionId, Integer expired) {
        jedisTemplate.excute(jedis -> {
            Integer finalExpire = expired == null ? maxAge : expired;
            jedis.expire(SessionUtils.getSessionKey(sessionId), finalExpire);
            return true;
        });
    }

    public void inValid(String sessionId) {
        jedisTemplate.excute(jedis -> {
            jedis.del(SessionUtils.getSessionKey(sessionId));
            return true;
        });
    }


}

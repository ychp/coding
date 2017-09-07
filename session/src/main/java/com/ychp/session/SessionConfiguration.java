package com.ychp.session;

import com.ychp.redis.configuration.RedisAutoConfiguration;
import com.ychp.redis.dao.JedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 2017/6/26
 */
@Import(RedisAutoConfiguration.class)
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(SessionProperties.class)
public class SessionConfiguration {

    @Autowired
    private SessionProperties sessionProperties;

    @Bean
    @ConditionalOnMissingBean(JedisPoolConfig.class)
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        if(!StringUtils.isEmpty(sessionProperties.getRedis().getPool().getMaxActive())) {
            config.setMaxTotal(Integer.valueOf(sessionProperties.getRedis().getPool().getMaxActive()));
        }
        if(!StringUtils.isEmpty(sessionProperties.getRedis().getPool().getMaxIdle())) {
            config.setMaxIdle(Integer.valueOf(sessionProperties.getRedis().getPool().getMaxIdle()));
        }
        if(!StringUtils.isEmpty(sessionProperties.getMinIdle())) {
            config.setMinIdle(Integer.valueOf(sessionProperties.getMaxIdle()));
        }
        if(!StringUtils.isEmpty(sessionProperties.getMaxWait())) {
            config.setMaxWaitMillis(Integer.valueOf(sessionProperties.getMaxWait()));
        }
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(JedisPool.class)
    public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig){
        Integer port = StringUtils.isEmpty(sessionProperties.getRedisPort()) ? 6379 : Integer.valueOf(sessionProperties.getRedisPort());
        Integer timeOut = StringUtils.isEmpty(sessionProperties.getTimeout()) ? 1000 : Integer.valueOf(sessionProperties.getTimeout());
        Integer database = StringUtils.isEmpty(sessionProperties.getDatabase()) ? 0 : Integer.valueOf(sessionProperties.getDatabase());
        return StringUtils.isEmpty(sessionProperties.getPassword()) ?
                new JedisPool(jedisPoolConfig, sessionProperties.getRedisHost(), port, timeOut, null, database) :
                new JedisPool(jedisPoolConfig, sessionProperties.getRedisHost(), port, timeOut, sessionProperties.getPassword(), database);
    }

    @Bean
    @ConditionalOnMissingBean(JedisTemplate.class)
    public JedisTemplate getJedisTemplate(Pool<Jedis> jedisPool){
        Integer database = StringUtils.isEmpty(sessionProperties.getDatabase()) ? 0 : Integer.valueOf(sessionProperties.getDatabase());
        return new JedisTemplate(jedisPool, database);
    }

    @Bean
    public SessionManager sessionManager(JedisTemplate jedisTemplate) {
        return new SessionManager(sessionProperties.getMaxAge(), jedisTemplate);
    }
}

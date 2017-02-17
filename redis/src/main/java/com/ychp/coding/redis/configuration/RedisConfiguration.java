package com.ychp.coding.redis.configuration;

import com.ychp.coding.redis.dao.JedisTemplate;
import com.ychp.coding.redis.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/25
 */
@Primary
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        if(!StringUtils.isEmpty(redisProperties.getMaxActive())) {
            config.setMaxTotal(Integer.valueOf(redisProperties.getMaxActive()));
        }
        if(!StringUtils.isEmpty(redisProperties.getMaxIdle())) {
            config.setMaxIdle(Integer.valueOf(redisProperties.getMaxIdle()));
        }
        if(!StringUtils.isEmpty(redisProperties.getMinIdle())) {
            config.setMinIdle(Integer.valueOf(redisProperties.getMaxIdle()));
        }
        if(!StringUtils.isEmpty(redisProperties.getMaxWait())) {
            config.setMaxWaitMillis(Integer.valueOf(redisProperties.getMaxWait()));
        }
        return config;
    }

    @Bean
    public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig){
        Integer port = StringUtils.isEmpty(redisProperties.getPort()) ? 6379 : Integer.valueOf(redisProperties.getPort());
        Integer timeOut = StringUtils.isEmpty(redisProperties.getTimeout()) ? 1000 : Integer.valueOf(redisProperties.getTimeout());
        Integer database = StringUtils.isEmpty(redisProperties.getDatabase()) ? 0 : Integer.valueOf(redisProperties.getDatabase());
        return StringUtils.isEmpty(redisProperties.getPassword()) ?
                new JedisPool(jedisPoolConfig, redisProperties.getHost(), port, timeOut, null, database) :
                new JedisPool(jedisPoolConfig, redisProperties.getHost(), port, timeOut, redisProperties.getPassword(), database);
    }

    @Bean
    public JedisTemplate getJedisTemplate(Pool<Jedis> jedisPool){
        Integer database = StringUtils.isEmpty(redisProperties.getDatabase()) ? 0 : Integer.valueOf(redisProperties.getDatabase());
        return new JedisTemplate(jedisPool, database);
    }

}

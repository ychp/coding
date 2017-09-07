package com.ychp.redis.configuration;

import com.ychp.redis.dao.JedisTemplate;
import com.ychp.redis.properties.RedisProperties;
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
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        if(!StringUtils.isEmpty(redisProperties.getPool().getMaxActive())) {
            config.setMaxTotal(redisProperties.getPool().getMaxActive());
        }
        if(!StringUtils.isEmpty(redisProperties.getPool().getMaxIdle())) {
            config.setMaxIdle(redisProperties.getPool().getMaxIdle());
        }
        if(!StringUtils.isEmpty(redisProperties.getPool().getMinIdle())) {
            config.setMinIdle(redisProperties.getPool().getMaxIdle());
        }
        if(!StringUtils.isEmpty(redisProperties.getPool().getMaxWait())) {
            config.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        }
        return config;
    }

    @Bean
    public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig){
        Integer port = StringUtils.isEmpty(redisProperties.getPort()) ? 6379 : redisProperties.getPort();
        Integer timeOut = StringUtils.isEmpty(redisProperties.getTimeout()) ? 1000 : redisProperties.getTimeout();
        Integer database = StringUtils.isEmpty(redisProperties.getDatabase()) ? 0 : redisProperties.getDatabase();
        return StringUtils.isEmpty(redisProperties.getPassword()) ?
                new JedisPool(jedisPoolConfig, redisProperties.getHost(), port, timeOut, null, database) :
                new JedisPool(jedisPoolConfig, redisProperties.getHost(), port, timeOut, redisProperties.getPassword(), database);
    }

    @Bean
    public JedisTemplate getJedisTemplate(Pool<Jedis> jedisPool){
        Integer database = StringUtils.isEmpty(redisProperties.getDatabase()) ? 0 : redisProperties.getDatabase();
        return new JedisTemplate(jedisPool, database);
    }

}

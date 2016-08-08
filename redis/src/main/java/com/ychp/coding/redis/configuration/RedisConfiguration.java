package com.ychp.coding.redis.configuration;

import com.ychp.coding.redis.dao.JedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@EnableAutoConfiguration
public class RedisConfiguration{

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisProperties.getPool().getMaxActive());
        config.setMaxIdle(redisProperties.getPool().getMaxIdle());
        config.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        return config;
    }

    @Bean
    public Pool<Jedis> getPool(JedisPoolConfig config){

        return StringUtils.isEmpty(redisProperties.getPassword()) ? new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeout(), null, redisProperties.getDatabase()) : new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeout(), redisProperties.getPassword(), redisProperties.getDatabase());
    }

    @Bean
    public JedisTemplate getJedisTemplate(Pool<Jedis> pool){
        return new JedisTemplate(pool, redisProperties.getDatabase());
    }



}

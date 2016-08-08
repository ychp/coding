package com.ychp.coding.redis.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/25
 */
public class JedisTemplate {

    private Pool<Jedis> jedisPool;

    private int indb;

    public JedisTemplate(Pool<Jedis> jedisPool, int indb){
        this.jedisPool = jedisPool;
        this.indb = indb;
    }

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public <T> T excute(JedisAction<T> action){
        Jedis jedis = null;
        boolean broken = false;
        try{
            jedis = jedisPool.getResource();
            jedis.select(indb);
            return action.action(jedis);
        }catch (JedisConnectionException je){
            broken = true;
            throw je;
        } finally {
            if(jedis != null){
                if(broken){
                    jedisPool.returnBrokenResource(jedis);
                }else {
                    jedisPool.returnResource(jedis);
                }
            }
        }
    }

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

}

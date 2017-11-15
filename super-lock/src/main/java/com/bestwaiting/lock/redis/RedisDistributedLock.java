package com.bestwaiting.lock.redis;

import com.bestwaiting.lock.distribute.DistributedLock;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by bestwaiting on 17/11/2.
 */
public class RedisDistributedLock implements DistributedLock {

    private String redisHost = "127.0.0.1";
    private int redisPort = 6379;
    private long lockTimeOut;
    private Jedis jedis;

    public RedisDistributedLock() {
        jedis = new Jedis(redisHost, redisPort);
    }

    public RedisDistributedLock(String redisHost, int redisPort) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        jedis = new Jedis(redisHost, redisPort);
    }

    public RedisDistributedLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public long tryLock(String key) {

        return tryLock(key, this.lockTimeOut);
    }

    public long tryLock(String key, long lockTimeOut) {
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = System.currentTimeMillis() + lockTimeOut + 1;

        if (jedis.setnx(key, String.valueOf(expireTime)) == 1) {
            //得到了锁返回
            return expireTime;
        } else {
            String curLockTimeStr = jedis.get(key);
            //判断是否过期
            if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {

                expireTime = System.currentTimeMillis() + lockTimeOut + 1;

                curLockTimeStr = jedis.getSet(key, String.valueOf(expireTime));
                //仍然过期,则得到锁
                if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                    return expireTime;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    public long lock(String key) throws InterruptedException {
        return lock(key, this.lockTimeOut, 0);
    }

    public long lock(String key, long lockTimeOut, long sleepTime) throws InterruptedException {
        long starttime = System.currentTimeMillis();
        long sleep = (sleepTime == 0 ? lockTimeOut / 10 : sleepTime);
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = 0;
        while (true) {
            expireTime = System.currentTimeMillis() + lockTimeOut + 1;
            if (jedis.setnx(key, String.valueOf(expireTime)) == 1) {
                return expireTime;
            } else {
                String curLockTimeStr = jedis.get(key);
                // 检查当前key是否过期
                if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                    expireTime = System.currentTimeMillis() + lockTimeOut + 1;
                    curLockTimeStr = jedis.getSet(key, String.valueOf(expireTime));
                    // 仍然过期,则得到锁
                    if (StringUtils.isBlank(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr)) {
                        return expireTime;
                    } else {
                        Thread.sleep(sleep);
                    }
                } else {
                    Thread.sleep(sleep);
                }
            }
            if (lockTimeOut > 0 && ((System.currentTimeMillis() - starttime) >= lockTimeOut)) {
                expireTime = 0;
                return expireTime;
            }
        }
    }

    public void releaseLock(String key, long expireTime) {
        if (System.currentTimeMillis() - expireTime > 0) {
            return;
        }
        String curLockTimeStr = jedis.get(key);
        if (StringUtils.isNotBlank(curLockTimeStr) && Long.valueOf(curLockTimeStr) > System.currentTimeMillis()) {
            jedis.del(key);
        }
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public long getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }
}

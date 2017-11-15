package com.bestwaiting.lock.distribute;

/**
 * Created by bestwaiting on 17/11/2.
 */
public interface DistributedLock {

    /**
     * 尝试获取key资源锁
     *
     * @param key 资源
     * @return
     */
    long tryLock(String key);

    /**
     * 尝试获取key资源锁
     *
     * @param key         资源
     * @param lockTimeOut 超时时间
     * @return
     */
    long tryLock(String key, long lockTimeOut);

    /**
     * 尝试时间内等待获取key资源锁
     *
     * @param key
     * @return
     * @throws InterruptedException
     */
    long lock(String key) throws InterruptedException;

    /**
     * 尝试时间内等待获取key资源锁
     *
     * @param key
     * @param lockTimeOut
     * @param sleepTime
     * @return
     * @throws InterruptedException
     */
    long lock(String key, long lockTimeOut, long sleepTime) throws InterruptedException;

    /**
     * 释放key资源锁
     *
     * @param key
     * @param lockTimeOut
     */
    void releaseLock(String key, long lockTimeOut);

}

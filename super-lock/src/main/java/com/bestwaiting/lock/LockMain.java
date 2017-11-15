package com.bestwaiting.lock;

import com.bestwaiting.lock.zookeeper.ZkDistributedLock;

import java.io.IOException;

/**
 * Created by bestwaiting on 17/11/2.
 */
public class LockMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        //ZkPandaLock zkPandaLock = new ZkPandaLock();
        //zkPandaLock.connectZooKeeper("127.0.0.1:2181", "jason");
    /*	boolean tryLockResult = ZkPandaLock.tryLock();
		System.out.println("tryLockResult:"+tryLockResult);
		boolean releaseLockResult = ZkPandaLock.releaseLock();
		System.out.println("releaseLockResult:"+releaseLockResult);*/
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    try {
                        ZkDistributedLock zkPandaLock = new ZkDistributedLock();
                        zkPandaLock.connectZooKeeper("127.0.0.1:2181", "jason");
                        zkPandaLock.lock("");
                        System.out.println(Thread.currentThread().getName() + "在做事，做完就释放锁");
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "我做完事情了");
                        zkPandaLock.releaseLock("", 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}

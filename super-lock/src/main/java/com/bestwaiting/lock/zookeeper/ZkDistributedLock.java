package com.bestwaiting.lock.zookeeper;

import com.bestwaiting.lock.distribute.DistributedLock;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bestwaiting on 17/11/2.
 */
public class ZkDistributedLock implements DistributedLock {
    /**
     * zookeeper节点的默认分隔符
     */
    private final static String SEPARATOR = "/";
    /**
     * pandaLock在zk中的根节点
     */
    private final static String lockRootNode = SEPARATOR + "parndaLock";// 熊猫锁的zk根节点
    /**
     * pandaLock默认的EPHEMERAL节点的超时时间，单位毫秒
     */
    private static final int defaultLockTimeout = 5000;
    /**
     * 竞争者节点，每个想要尝试获得锁的节点都会获得一个竞争者节点
     */
    private static final String COMPETITOR_NODE = "competitorNode";
    /**
     * 统一的zooKeeper连接，在Init的时候初始化
     */
    private static ZooKeeper pandaZk = null;

    /**
     * 与zk连接成功后消除围栏
     */
    private CountDownLatch latch = new CountDownLatch(1);
    private CountDownLatch getTheLocklatch = new CountDownLatch(1);

    private String lockName = null;

    private String rootPath = null;

    private String lockPath = null;

    private String competitorPath = null;

    private String thisCompetitorPath = null;

    private String waitCompetitorPath = null;


    public long tryLock(String key) {
        if (StringUtils.isBlank(rootPath) || StringUtils.isBlank(lockName) || pandaZk == null) {
            System.out.println("you can not tryLock anyone before you dit not initial connectZookeeper");
        }
        List<String> allCompetitorList = null;
        try {
            createComPrtitorNode();
            allCompetitorList = pandaZk.getChildren(lockPath, false);
        } catch (KeeperException e) {
            System.out.println("zookeeper connect error");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(allCompetitorList);
        int index = allCompetitorList.indexOf(thisCompetitorPath.substring((lockPath + SEPARATOR).length()));
        if (index == -1) {
            System.out.println("competitorPath not exit after create");
        } else if (index == 0) {// 如果发现自己就是最小节点,那么说明本人获得了锁
//            return true;
        } else {// 说明自己不是最小节点
//            return false;
        }
        return 0;
    }

    public long tryLock(String key, long lockTimeOut) {
        return 0;
    }

    public long lock(String key) throws InterruptedException {
        if (StringUtils.isBlank(rootPath) || StringUtils.isBlank(lockName) || pandaZk == null) {
            System.out.println("you can not lock anyone before you dit not connectZookeeper");
        }
        List<String> allCompetitorList = null;
        try {
            createComPrtitorNode();
            allCompetitorList = pandaZk.getChildren(lockPath, false);
        } catch (KeeperException e) {
            System.out.println("zookeeper connect error");
        } catch (InterruptedException e) {
            System.out.println("the lock has  been Interrupted");
        }
        Collections.sort(allCompetitorList);
        int index = allCompetitorList.indexOf(thisCompetitorPath.substring((lockPath + SEPARATOR).length()));
        if (index == -1) {
            System.out.println("competitorPath not exit after create");
        } else if (index == 0) {// 如果发现自己就是最小节点,那么说明本人获得了锁
            return 0;
        } else {// 说明自己不是最小节点
            waitCompetitorPath = lockPath + SEPARATOR + allCompetitorList.get(index - 1);
            // 在waitPath上注册监听器, 当waitPath被删除时, zookeeper会回调监听器的process方法
            Stat waitNodeStat;
            try {
                waitNodeStat = pandaZk.exists(waitCompetitorPath, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getType().equals(Event.EventType.NodeDeleted) && event.getPath().equals(waitCompetitorPath)) {
                            getTheLocklatch.countDown();
                        }
                    }
                });
                if (waitNodeStat == null) {//如果运行到此处发现前面一个节点已经不存在了。说明前面的进程已经释放了锁
                    return 0;
                } else {
                    getTheLocklatch.await();
                    return 0;
                }
            } catch (KeeperException e) {
                System.out.println("zookeeper connect error");
            } catch (InterruptedException e) {
                System.out.println("the lock has  been Interrupted");
            }


        }
        return 0;
    }

    public long lock(String key, long lockTimeOut, long sleepTime) throws InterruptedException {
        return 0;
    }

    public void releaseLock(String key, long lockTimeOut) {
        if (StringUtils.isBlank(rootPath) || StringUtils.isBlank(lockName) || pandaZk == null) {
            System.out.println("you can not release anyLock before you dit not initial connectZookeeper");
        }
        try {
            pandaZk.delete(thisCompetitorPath, -1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.out.println("the release lock has been Interrupted ");
        } catch (KeeperException e) {
            System.out.println("zookeeper connect error");
        }
    }

    /**
     * 创建竞争者节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void createComPrtitorNode() throws KeeperException, InterruptedException {
        competitorPath = lockPath + SEPARATOR + COMPETITOR_NODE;
        thisCompetitorPath = pandaZk.create(competitorPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void connectZooKeeper(String zkhosts, String lockName) throws Exception {
        Stat rootStat = null;
        Stat lockStat = null;
        if (StringUtils.isBlank(zkhosts)) {
            System.out.println("zookeeper hosts can not be blank");
        }
        if (StringUtils.isBlank(lockName)) {
            System.out.println("lockName can not be blank");
        }
        if (pandaZk == null) {
            pandaZk = new ZooKeeper(zkhosts, defaultLockTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState().equals(Event.KeeperState.SyncConnected)) {
                        latch.countDown();
                    }
                }
            });
        }
        latch.await();
        rootStat = pandaZk.exists(lockRootNode, false);
        if (rootStat == null) {
            rootPath = pandaZk.create(lockRootNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            rootPath = lockRootNode;
        }
        String lockNodePathString = lockRootNode + SEPARATOR + lockName;
        lockStat = pandaZk.exists(lockNodePathString, false);
        if (lockStat != null) {// 说明此锁已经存在
            lockPath = lockNodePathString;
            // throw new
            // PandaLockException("the lockName is already exits in zookeeper, please check the lockName :"
            // +lockName);
        } else {
            // 创建相对应的锁节点
            lockPath = pandaZk.create(lockNodePathString, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        this.lockName = lockName;
    }
}

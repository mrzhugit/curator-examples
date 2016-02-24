package locking.multiSharedLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 存钱时锁定张三账户
 */
public class LisiLockExample {
    private static final int QTY = 5;
    private static final int REPETITIONS = 100;

    private static final String PATH = "/examples/lisi_locks";

    public static void main(String[] args) throws Exception {


        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        try {
            client.start();

            InterProcessMutex lock = new InterProcessMutex(client, PATH);
            System.out.println("请求write lock");
            lock.acquire();
            System.out.println("获取write lock成功");
            Thread.sleep(1000 * 30);
            lock.release();
            System.out.println("释放write lock");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }


    }
}

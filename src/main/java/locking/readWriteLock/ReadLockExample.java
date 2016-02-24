package locking.readWriteLock;

import locking.ExampleClientThatLocks;
import locking.FakeLimitedResource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 资源被读时,可以被其他进程读,不可被写
 */
public class ReadLockExample {
    private static final int QTY = 5;
    private static final int REPETITIONS = 100;

    private static final String PATH = "/examples/reade_write_locks";

    public static void main(String[] args) throws Exception {


        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        try {
            client.start();

            InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client,PATH);
            for (int j = 0; j < REPETITIONS; ++j) {
                InterProcessMutex mutex = lock.readLock();
                System.out.println("请求read lock");
                mutex.acquire();
                System.out.println("获取read lock 成功");
                Thread.sleep(1000 * 10);
                mutex.release();
                System.out.println("释放read lock");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

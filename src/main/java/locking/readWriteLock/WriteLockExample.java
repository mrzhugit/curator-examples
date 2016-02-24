package locking.readWriteLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 资源被写时其他进程不可读
 */
public class WriteLockExample {
    private static final int QTY = 5;
    private static final int REPETITIONS = 100;

    private static final String PATH = "/examples/reade_write_locks";

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        try {
            client.start();

            InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client,PATH);
            for (int j = 0; j < REPETITIONS; ++j) {
                InterProcessMutex mutex = lock.writeLock();
                System.out.println("请求write lock");
                mutex.acquire();
                System.out.println("获取write lock成功");
                Thread.sleep(1000 * 100);
                mutex.release();
                System.out.println("释放write lock");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

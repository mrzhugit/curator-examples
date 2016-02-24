package locking.sharedSemaphore;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 资源同时最多被2个进程锁定
 */
public class LockExample {

    private static final String PATH = "/examples/shared_semaphore_locks";
    private static final int scount = 2;

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        try {
            client.start();

            InterProcessSemaphoreV2 lock = new InterProcessSemaphoreV2(client, PATH, scount);

            System.out.println("请求lock");
            org.apache.curator.framework.recipes.locks.Lease lease = lock.acquire();
            System.out.println("获取 lock成功");
            Thread.sleep(1000 * 20);
            lock.returnLease(lease);
            System.out.println("释放 lock");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

package locking.multiSharedLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 张三给李四转账,锁定两个账户
 */
public class MultiLockExample {
    private static final int REPETITIONS = 100;


    public static void main(String[] args) throws Exception {


        List<String> pathList = new ArrayList<String>();
        pathList.add("/examples/zhangsan_locks");
        pathList.add("/examples/lisi_locks");

        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        try {
            client.start();

            InterProcessMultiLock lock = new InterProcessMultiLock(client, pathList);
            System.out.println("请求write lock");
            lock.acquire();
            System.out.println("获取write lock成功");
            Thread.sleep(1000 * 10);
            lock.release();
            System.out.println("释放write lock");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }


    }
}

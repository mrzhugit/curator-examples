package barrier.barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 等待领导发送指示再干活
 */
public class Worker {

    private static final String PATH = "/examples/barrier";
    private static final String zkUrl = "localhost:2181";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, new ExponentialBackoffRetry(1000, 3));

        try {
            client.start();
            DistributedBarrier barrier = new DistributedBarrier(client, PATH);
            barrier.setBarrier();
            System.out.println("等待master信号");
            barrier.waitOnBarrier();
            System.out.println("开始执行任务");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

}

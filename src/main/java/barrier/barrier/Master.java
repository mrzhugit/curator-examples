package barrier.barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 领导人发话后,下面工人开始干活
 */
public class Master {
    //协调节点
    private static final String PATH = "/examples/barrier";
    private static final String zkUrl = "localhost:2181";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, new ExponentialBackoffRetry(1000, 3));

        try {
            client.start();
            DistributedBarrier barrier = new DistributedBarrier(client, PATH);
            barrier.setBarrier();
            System.out.println("发送信号");
            barrier.removeBarrier();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

}

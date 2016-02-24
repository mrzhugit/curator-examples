package barrier.doubleBarrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;

/**
 * 3个进程一起开始,一起结束
 */
public class Worker {

    private static final String PATH = "/examples/barrier";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));

        try {
            client.start();
            DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, PATH,3);
            System.out.println("等待全部就绪");
            barrier.enter();
            System.out.println("全部就绪,开始执行任务");
            Thread.sleep(1000 * (new Random().nextInt(10)));
            System.out.println("执行完毕,等待全部完毕");
            barrier.leave();
            System.out.println("全部完毕");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

}

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package locking;

import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多进程之间的重入锁示例
 *
 * 多个应用对同一资源进行锁定
 *
 */
public class LockingExample {
    private static final int QTY = 5;
    private static final int REPETITIONS = 100;

    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception {
        String procId = String.valueOf(new Random().nextInt(1000));

        // FakeLimitedResource simulates some external resource that can only be access by one process at a time
        final FakeLimitedResource resource = new FakeLimitedResource();

            CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
            try {
                client.start();

                ExampleClientThatLocks example = new ExampleClientThatLocks(client, PATH, resource, "Client "+procId);
                for (int j = 0; j < REPETITIONS; ++j) {
                    example.doWork(10, TimeUnit.SECONDS);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                CloseableUtils.closeQuietly(client);
            }
    }
}

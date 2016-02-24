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
package leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 主节点选举示例
 * 多进程仅有一个为主节点.其余进程为备用节点.
 * 主节点关闭后备用节点中会选举出另外一个作为主节点.
 * <p>
 * 在候选人的控制台回车后开始中断
 */
public class LeaderSelectorExample {

    private static final String PATH = "/examples/leader";

    public static void main(String[] args) throws Exception {
        String userName = String.valueOf(new Random().nextInt(1000));
        System.out.println("候选人:" + userName);
        System.out.println("输入回车开始调休\n");
        CuratorFramework client = null;
        ExampleClient example = null;
        try {
            client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            example = new ExampleClient(client, PATH, userName);
            client.start();
            example.start();
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            System.out.println("回家...");
            CloseableUtils.closeQuietly(example);
            CloseableUtils.closeQuietly(client);
        }
    }
}

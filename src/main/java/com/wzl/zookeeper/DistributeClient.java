package com.wzl.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端
 */
public class DistributeClient {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeClient client = new DistributeClient();
        //1.连接zookeeper集群
        client.getConnect();
        //2.注册监听
        client.getChildren();
        //3.业务逻辑处理
        client.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/servers", true);

        ArrayList<String> hosts = new ArrayList<>();
        children.forEach(child->{
            try {
                byte[] data = zkClient.getData("/servers/" + child, false, null);
                hosts.add(new String(data));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //将所有在线主机名打印到控制台
        System.out.println(hosts);
    }

    private String connectString = "master:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    private void getConnect() throws IOException {
       zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("======================================satrt===========================");
                try {
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("======================================end===========================");
            }
        });
    }
}

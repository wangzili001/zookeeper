package com.wzl.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestZookeeper {
    private String connectString="master:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    //创建连接
    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("==============================start=====================");
                List<String> children;
                try {
                    children = zkClient.getChildren("/",true);
                    children.forEach(child->{
                        System.out.println(child);
                    });
                    System.out.println("==============================end=====================");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //创建节点
    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String path = zkClient.create("/sanguo", "caocao".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
    }

    //获取子节点并监听节点变化
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/", true);

        children.forEach(child->{
            System.out.println(child);
        });
        Thread.sleep(Long.MAX_VALUE);
    }
    //判断节点是否存在
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat exists = zkClient.exists("/sanguo", false);
        System.out.println(exists == null?"not exist":exists);
    }
}

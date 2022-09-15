package com.lagou;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * redis集群客户端应用(由于本地ip与docker虚拟ip不一致，导致JedisCluster不能连接，以此需把项目发布到docker内部并加入redis集群的网段才能正常运行)
 */
public class JedisClusterApplication {

    public static void main(String[] args) {

        //  集群节点添加(因为集群采用的是docker形成的集群，因此此处的ip地址皆是docker内部虚拟地址)
        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort("192.168.0.36", 6379));
		jedisClusterNode.add(new HostAndPort("192.168.0.37", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.38", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.39", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.40", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.41", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.42", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.0.43", 6379));

        //  集群设置
        int connectionTimeout = 3000;
        int soTimeout = 3000;
        int maxAttempts = 5;
        String password = "123456";
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(100);
		poolConfig.setMaxWait(Duration.ofMillis(10000));	//4.0.0-SNAPSHOT版本
        poolConfig.setTestOnBorrow(true);

        //  获取集群客户端并开始查询以及添加数据
        JedisCluster myCluster = new JedisCluster(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);


        //  位于节点7
        String name = myCluster.get("name");
        System.out.println("name======>"+name);

        //  位于节点7
        myCluster.set("name","李四");
        String name1 = myCluster.get("name");
        System.out.println("name======>"+name1);

        System.out.println("=============================");

        String redisNode1 = myCluster.get("redis-node1");
        System.out.println("位于节点1的redis-node1======>"+redisNode1);

        String test = myCluster.get("test");
        System.out.println("位于节点2的test======>"+test);

        String k1 = myCluster.get("k1");
        System.out.println("位于节点3的k1======>"+k1);

    }
}

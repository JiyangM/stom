package cn.itcast.storm.kafkastormredis;

import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by maoxiangyi on 2016/8/16.
 */
public class TestRedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Map<String, String> wordcount = jedis.hgetAll("wordcount");
        System.out.println(wordcount);
    }
}

package cn.itcast.storm.kafkastormredis;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maoxiangyi on 2016/8/16.
 */
public class MyWordCountAndPrintBolt extends BaseBasicBolt {
    private Jedis jedis;
    private Map<String, String> wordCountMap = new HashMap<String, String>();

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //连接redis---代表可以连接任何事物
        jedis = new Jedis("127.0.0.1", 6379);
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = (String) input.getValueByField("word");
        Integer num = (Integer) input.getValueByField("num");
        //1、查看单词对应的value是否存在
        Integer integer = wordCountMap.get(word)==null?0:Integer.parseInt(wordCountMap.get(word));
        if (integer == null || integer.intValue() == 0) {
            wordCountMap.put(word, num+"");
        } else {
            wordCountMap.put(word, (integer.intValue() + num)+"");
        }
        //2、保存数据到redis
        System.out.println(wordCountMap);
        // redis key wordcount:->Map
//        jedis.hmset("wordcount",wordCountMap);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //todo 不需要定义输出的字段
    }
}

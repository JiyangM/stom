package cn.itcast.storm.kafkastormredis;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Map --->word,1
 */
public class MySplitBolt  extends BaseBasicBolt{
    public void execute(Tuple input, BasicOutputCollector collector) {
        //1、数据如何获取
        byte[] juzi = (byte[])input.getValueByField("bytes");
        //2、进行切割
        String[] strings = new String(juzi).split(" ");
        //3、发送数据
        for (String word:strings){
            //Values 对象帮我们生成一个list
             collector.emit(new Values(word,1));
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word","num"));
    }
}

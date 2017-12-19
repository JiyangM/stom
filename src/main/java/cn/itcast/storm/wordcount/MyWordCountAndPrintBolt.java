package cn.itcast.storm.wordcount;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class MyWordCountAndPrintBolt extends BaseBasicBolt {
    private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = (String) input.getValueByField("word");
        Integer num = (Integer) input.getValueByField("num");
        //1、查看单词对应的value是否存在
        Integer integer = wordCountMap.get(word);
        if (integer == null || integer.intValue() == 0) {
            wordCountMap.put(word,num);
        }else {
            wordCountMap.put(word,integer.intValue()+num);
        }
        //2、打印数据
        System.out.println(wordCountMap);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //todo 不需要定义输出的字段
    }
}

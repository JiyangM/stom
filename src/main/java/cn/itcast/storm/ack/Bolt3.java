package cn.itcast.storm.ack;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by maoxiangyi on 2016/8/18.
 */
public class Bolt3 extends BaseRichBolt {
    private OutputCollector collector;

    //初始化方法 只调用一次
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    //被循环调用
    public void execute(Tuple input) {
        collector.emit(input,new Values(input.getString(0)));
        System.out.println("bolt3的execute方法被调用一次" + input.getString(0));
        collector.fail(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("uuid"));
    }
}

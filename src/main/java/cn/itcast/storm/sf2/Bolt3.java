package cn.itcast.storm.sf2;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * Created by maoxiangyi on 2016/8/18.
 */
public class Bolt3 extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        System.out.println("bolt3被调用"+input.getString(0));
       throw new FailedException("xxx");
//        collector.emit(new Values(input.getString(0)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}

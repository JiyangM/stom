package cn.itcast.storm.sf2;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Created by maoxiangyi on 2016/8/18.
 */
public class Bolt4 extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        System.out.println("bolt4被调用"+input.getString(0));
        //执行业务逻辑
        collector.emit(new Values(input.getString(0)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}

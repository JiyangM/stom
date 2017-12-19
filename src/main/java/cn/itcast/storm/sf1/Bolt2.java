package cn.itcast.storm.sf1;

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
public class Bolt2 extends BaseRichBolt {
    private OutputCollector collector;

    //初始化方法
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    //执行业务逻辑的方法，被循环调用
    public void execute(Tuple input) {
        System.out.println("bolt2被调用"+input.getString(0));
        //执行业务逻辑
        collector.emit(input, new Values(input.getString(0)));
        //业务逻辑正常执行之后，告诉storm框架，在bolt1这个环节处理成功啦
        collector.ack(input);
    }

    //输出字段
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}

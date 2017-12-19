package cn.itcast.storm.ack;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by maoxiangyi on 2016/8/18.
 */
public class AckSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;

    //初始化方法
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    //上帝之手，循环调用，每调用过一次就发送一条消息
    public void nextTuple() {
        //生产一条数据
        String uuid = UUID.randomUUID().toString().replace("_", "");
        collector.emit(new Values(uuid),new Values(uuid));
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //定义发送的字段
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("uuid"));
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功" + msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败" + msgId);
        collector.emit((List)msgId,msgId );
    }
}

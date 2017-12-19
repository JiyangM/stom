package cn.itcast.storm.sf2;

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
 * Storm框架不会重新发送数据
 * 需要我们自定义一个成员变量Map保存数据
 * 如果数据处理成功，就需要从成员变量Map中删除数据
 * 如果数据处理失败，需要从成员变量中Map获取数据并重新发送。
 * ---------------------------
 * Storm框架需要告知spout是哪条信息处理成功或失败了，
 * 所以需要我们发送数据的时候，为每条消息指定一个msgid。
 * 尽可能保证msgid不重复，推荐使用uuid
 */
public class MyAckSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    //初始化方法
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    //被循环调用，每调用一次，发一条数据出去
    public void nextTuple() {
        //每次调用nextTuple，就发送字符串
        String msgId = UUID.randomUUID().toString().replace("_", "");
        collector.emit(new Values(msgId), new Values(msgId));
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //定义输出的字段
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("string"));
    }

    //当一条消息被成功处理之后，Storm框架会调用spout的ack方法
    //确定是哪条消息成功，storm会传入一个msgId
    public void ack(Object msgId) {
        System.out.println("消息处理成功：" + msgId);
    }

    //当一条消息被处理失败之后，Storm框架会调用spout的fail方法
//    确定是哪条消息失败，storm会传入一个msgId
    public void fail(Object msgId) {
//        //消息要重发，从哪里获取数据
        collector.emit((List)msgId, msgId);
        System.out.println("消息处理失败：重发" + msgId);
    }
}

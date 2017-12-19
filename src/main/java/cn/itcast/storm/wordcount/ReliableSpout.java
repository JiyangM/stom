package cn.itcast.storm.wordcount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.util.*;

/**
 *  参考自：
 *  https://www.cnblogs.com/intsmaze/p/5918087.html
 *  http://www.aboutyun.com/thread-9526-1-1.html
 */
public class ReliableSpout extends BaseRichSpout {
    // key:messageId,Data
    private HashMap<String, String> waitAck = new HashMap<String, String>();

    public static final String FILE_PATH = "D:\\1.log";
    private SpoutOutputCollector collector;
    private BufferedReader bufferedReader;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            this.bufferedReader = new BufferedReader(new FileReader(new File(FILE_PATH)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void nextTuple() {
        try {
            String line = bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)) {
                List<Object> arrayList = new ArrayList<Object>();
                arrayList.add(line);
//                collector.emit(arrayList);
                // 指定messageId，开启ackfail机制,指定messageid acker 数量至少为1 config.setNumAckers(1);
                String messageId = UUID.randomUUID().toString().replaceAll("-", "");
                collector.emit(arrayList, messageId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //消息源可以发射多条消息流stream。多条消息流可以理解为多中类型的数据。
        outputFieldsDeclarer.declare(new Fields("juzi"));
    }

    public void ack(Object msgId) {
        System.out.println("消息处理成功:" + msgId);
        System.out.println("删除缓存中的数据...");
        waitAck.remove(msgId);
    }

    public void fail(Object msgId) {
        System.out.println("消息处理失败:" + msgId);
        System.out.println("重新发送失败的信息...");
        //重发如果不开启ackfail机制，那么spout的map对象中的该数据不会被删除的。
        collector.emit(new Values(waitAck.get(msgId)),msgId);
    }
}

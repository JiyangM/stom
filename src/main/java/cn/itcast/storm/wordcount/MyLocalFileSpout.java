package cn.itcast.storm.wordcount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MyLocalFileSpout extends BaseRichSpout {
//    public static final String FILE_PATH = "/root/1.log";
    public static final String FILE_PATH = "D:\\1.log";
    private SpoutOutputCollector collector;
    private BufferedReader bufferedReader;

    //初始化方法
    //该方法只会被调用一次，用来初始化
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            this.bufferedReader = new BufferedReader(new FileReader(new File(FILE_PATH)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Storm实时计算的特性就是对数据一条一条的处理
    //while(true){
    // this.nextTuple()
    // }
    public void nextTuple() {
        // 每被调用一次就会发送一条数据出去
        try {
            String line = bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)) {
                List<Object> arrayList = new ArrayList<Object>();
                arrayList.add(line);
//                collector.emit(arrayList);
                String messageId = UUID.randomUUID().toString().replaceAll("-", "");
                collector.emit(arrayList, messageId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //消息源可以发射多条消息流stream。多条消息流可以理解为多中类型的数据。
        declarer.declare(new Fields("juzi"));
    }

}

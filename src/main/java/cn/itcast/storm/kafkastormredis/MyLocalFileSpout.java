package cn.itcast.storm.kafkastormredis;

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

/**
 * Created by maoxiangyi on 2016/8/16.
 */
public class MyLocalFileSpout extends BaseRichSpout {
    private  SpoutOutputCollector collector;
    private BufferedReader bufferedReader;
    //初始化方法
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            this.bufferedReader = new BufferedReader(new FileReader(new File("/root/1.log")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Storm实时计算的特性就是对数据一条一条的处理
    //while(true){
    // this.nextTuple()
    // }
    public void nextTuple() {
        //每被调用一次就会发送一条数据出去
        try {
            String line = bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)){
                List<Object> arrayList = new ArrayList<Object>();
                arrayList.add(line);
                collector.emit(arrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("juzi"));
    }
}

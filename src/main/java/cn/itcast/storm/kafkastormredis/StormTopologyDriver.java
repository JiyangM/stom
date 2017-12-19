package cn.itcast.storm.kafkastormredis;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 * Created by maoxiangyi on 2016/8/16.
 */
public class StormTopologyDriver {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //1、准备任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("KafkaSpout",new KafkaSpout(new SpoutConfig(new ZkHosts("zk01:2181"),"kafkaTest1","/kakfa","kafkaTest1")),2);
        topologyBuilder.setBolt("bolt1", new MySplitBolt(),4).shuffleGrouping("KafkaSpout");
        topologyBuilder.setBolt("bolt2", new MyWordCountAndPrintBolt(),2).shuffleGrouping("bolt1");

        //2、任务提交
        //提交给谁？提交什么内容？
        Config config = new Config();
        config.setNumWorkers(2);
        StormTopology stormTopology = topologyBuilder.createTopology();
//        //本地模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount", config, stormTopology);
        //集群模式
//        StormSubmitter.submitTopology("wordcount1", config, stormTopology);
    }
}

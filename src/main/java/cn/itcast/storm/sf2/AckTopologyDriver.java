package cn.itcast.storm.sf2;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by maoxiangyi on 2016/8/16.
 */
public class AckTopologyDriver {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //1、准备任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MyAckSpout(), 1);
        topologyBuilder.setBolt("bolt1", new Bolt1(), 1).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("bolt2", new Bolt2(), 1).shuffleGrouping("bolt1");
        topologyBuilder.setBolt("bolt3", new Bolt3(), 1).shuffleGrouping("bolt2");
        topologyBuilder.setBolt("bolt4", new Bolt4(), 1).shuffleGrouping("bolt3");

        //2、任务提交
        //提交给谁？提交什么内容？
        Config config = new Config();
        config.setNumWorkers(2);
        StormTopology stormTopology = topologyBuilder.createTopology();
        //本地模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount", config, stormTopology);
    }
}

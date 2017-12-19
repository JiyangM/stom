package cn.itcast.storm.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class StormTopologyDriver {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //1、准备任务信息
        //Storm框架支持多语言，在JAVA环境下创建一个拓扑，需要使用TopologyBuilder进行构建
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        /* MyLocalFileSpout类，主要是将文本内容读成一行一行的模式
         * 消息源spout是Storm里面一个topology里面的消息生产者。
         * 一般来说消息源会从一个外部源读取数据并且向topology里面发出消息：tuple。
         * Spout可以是可靠的也可以是不可靠的。
         * 如果这个tuple没有被storm成功处理，可靠的消息源spouts可以重新发射一个tuple，但是不可靠的消息源spouts一旦发出一个tuple就不能重发了。
         *
         * 消息源可以发射多条消息流stream。多条消息流可以理解为多中类型的数据。
         * 使用OutputFieldsDeclarer.declareStream来定义多个stream，然后使用SpoutOutputCollector来发射指定的stream。
         *
         * Spout类里面最重要的方法是nextTuple。要么发射一个新的tuple到topology里面或者简单的返回如果已经没有新的tuple。
         * 要注意的是nextTuple方法不能阻塞，因为storm在同一个线程上面调用所有消息源spout的方法。
         *
         * 另外两个比较重要的spout方法是ack和fail。storm在检测到一个tuple被整个topology成功处理的时候调用ack，否则调用fail。storm只对可靠的spout调用ack和fail。
         */

        topologyBuilder.setSpout("mySpout", new MyLocalFileSpout(), 2);
//        topologyBuilder.setSpout("mySpout", new ReliableSpout(), 2);
        topologyBuilder.setBolt("bolt1", new MySplitBolt(), 4).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("bolt2", new MyWordCountAndPrintBolt(), 2).shuffleGrouping("bolt1");

        //2、任务提交
        //提交给谁？提交什么内容？
        Config config = new Config();
        //定义你希望集群分配多少个工作进程给你来执行这个topology
        config.setNumWorkers(2);
        StormTopology stormTopology = topologyBuilder.createTopology();

        //这在本地环境调试topology很有用， 但是在线上这么做的话会影响性能的。
        config.setDebug(false);

        // Acker的数量为0时  storm会在spout发射一个tuple之后马上调用spout的ack方法。也就是说这个tuple树不会被跟踪。
        config.setNumAckers(2);
        //storm的运行有两种模式: 本地模式和分布式模式.
        //本地模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount", config, stormTopology);
//        指定本地模式运行多长时间之后停止，如果不显式的关系程序将一直运行下去
//        Utils.sleep(10000);
//        localCluster.shutdown();

        // 集群模式
//        StormSubmitter.submitTopology("wordcount1", config, stormTopology);
    }
}

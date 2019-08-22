import com.hazelcast.config.*;

import java.util.Map;


public class ExampleWANReplicationDiscoveryConfiguration {

    public static void main(String[] args) throws Exception{
//tag::wrdc[]
        Config config = new Config();

        WanReplicationConfig wrConfig = new WanReplicationConfig();
        wrConfig.setName("my-wan-cluster-batch");

        WanBatchReplicationPublisherConfig publisherConfig = new WanBatchReplicationPublisherConfig()
        			.setGroupName("london")
        			.setClassName("com.hazelcast.enterprise.wan.replication.WanBatchReplication")
        			.setQueueCapacity(1000)
        			.setQueueFullBehavior(WANQueueFullBehavior.THROW_EXCEPTION);

        Map<String, Comparable> props = publisherConfig.getProperties();
        props.put("batch.size", 500);
        props.put("batch.max.delay.millis", 1000);
        props.put("snapshot.enabled", false);
        props.put("response.timeout.millis", 60000);
        props.put("ack.type", WanAcknowledgeType.ACK_ON_OPERATION_COMPLETE.toString());
        props.put("group.password", "london-pass");
        props.put("discovery.period", "20");
        props.put("executorThreadCount", "2");

        DiscoveryConfig discoveryConfig = new DiscoveryConfig();

        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig("com.hazelcast.aws.AwsDiscoveryStrategy");
        discoveryStrategyConfig.addProperty("access-key","test-access-key");
        discoveryStrategyConfig.addProperty("secret-key","test-secret-key");
        discoveryStrategyConfig.addProperty("region","test-region");
        discoveryStrategyConfig.addProperty("iam-role","test-iam-role");
        discoveryStrategyConfig.addProperty("host-header","ec2.test-host-header");
        discoveryStrategyConfig.addProperty("security-group-name","test-security-group-name");
        discoveryStrategyConfig.addProperty("tag-key","test-tag-key");
        discoveryStrategyConfig.addProperty("tag-value","test-tag-value");
        discoveryStrategyConfig.addProperty("hz-port",5702);

        discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);
        publisherConfig.setDiscoveryConfig(discoveryConfig);
        wrConfig.addWanBatchReplicationPublisherConfig(publisherConfig);
        config.addWanReplicationConfig(wrConfig);
//end::wrdc[]
    }
}

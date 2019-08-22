import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientReliableTopicConfig;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.TopicOverloadPolicy;
import com.hazelcast.config.Config;
import com.hazelcast.config.RingbufferConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


public class ExampleRTClient {

    public static void main(String[] args) throws Exception{
        //tag::rtclient[]
        Config config = new Config();
        RingbufferConfig ringbufferConfig = new RingbufferConfig("default");
        ringbufferConfig.setCapacity(10000000)
                .setTimeToLiveSeconds(5);
        config.addRingBufferConfig(ringbufferConfig);

        ClientConfig clientConfig = new ClientConfig();
        ClientReliableTopicConfig topicConfig = new ClientReliableTopicConfig("default");
        topicConfig.setTopicOverloadPolicy( TopicOverloadPolicy.BLOCK )
                            .setReadBatchSize( 10 );
        clientConfig.addReliableTopicConfig(topicConfig);

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        ITopic topic = client.getReliableTopic(topicConfig.getName());
        //end::rtclient[]
    }
}

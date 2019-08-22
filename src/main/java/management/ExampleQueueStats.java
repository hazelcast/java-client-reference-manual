import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.monitor.LocalQueueStats;

public class ExampleQueueStats {

    public static void main(String[] args) throws Exception{
        //tag::qs[]
        HazelcastInstance node = Hazelcast.newHazelcastInstance();
        IQueue<Integer> orders = node.getQueue( "orders" );
        LocalQueueStats queueStatistics = orders.getLocalQueueStats();
        System.out.println( "average age of items = "
                + queueStatistics.getAvgAge() );
        //end::qs[]
    }
}
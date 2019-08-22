import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;

public class ExampleIAtomicLong {

    public static void main( String[] args ) {
        //tag::ial[]
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong( "counter" );
        for ( int k = 0; k < 1000 * 1000; k++ ) {
            if ( k % 500000 == 0 ) {
                System.out.println( "At: " + k );
            }
            counter.incrementAndGet();
        }
        System.out.printf( "Count is %s\n", counter.get() );
        //end::ial[]
    }
}

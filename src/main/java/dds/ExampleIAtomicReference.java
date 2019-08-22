import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicReference;

public class ExampleIAtomicReference {
    public static void main(String[] args){
        //tag::iar[]
        Config config = new Config();

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        IAtomicReference<String> ref = hz.getCPSubsystem().getAtomicReference("reference");
        ref.set("foo");
        System.out.println(ref.get());
        System.exit(0);
        //end::iar[]
    }
}

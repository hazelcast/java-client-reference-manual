import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.hazelcast.nio.IOUtil.closeResource;

public class ExampleCardinalityEstimator {
    public static void main(String[] args) throws Exception {
        //tag::ces[]
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        CardinalityEstimator visitorsEstimator = hz.getCardinalityEstimator("visitors");

        InputStreamReader isr = new InputStreamReader(ExampleCardinalityEstimator.class.getResourceAsStream("visitors.txt"));
        BufferedReader br = new BufferedReader(isr);
        try {
            String visitor = br.readLine();
            while (visitor != null) {
                visitorsEstimator.add(visitor);
                visitor = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResource(br);
            closeResource(isr);
        }

        System.out.printf("Estimated unique visitors seen so far: %d%n", visitorsEstimator.estimate());

        Hazelcast.shutdownAll();
//end::ces[]
    }
}

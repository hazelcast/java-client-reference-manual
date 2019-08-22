import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.locks.Lock;

public class ExampleLock {
    public static void main(String[] args) throws Exception{
        //tag::lock[]
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        Lock lock = hazelcastInstance.getCPSubsystem().getLock("myLock");
        lock.lock();
        try {
            // do something here
        } finally {
            lock.unlock();
        }
        //end::lock[]
    }
}

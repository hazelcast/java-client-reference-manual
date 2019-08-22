import com.hazelcast.partition.PartitionAware;

import java.io.Serializable;

//tag::orderkey[]
final class OrderKey implements PartitionAware, Serializable {

    private final long orderId;
    private final long customerId;

    OrderKey(long orderId, long customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    @Override
    public Object getPartitionKey() {
        return customerId;
    }

    @Override
    public String toString() {
        return "OrderKey{"
                + "orderId=" + orderId
                + ", customerId=" + customerId
                + '}';
//end::orderkey[]
    }
}
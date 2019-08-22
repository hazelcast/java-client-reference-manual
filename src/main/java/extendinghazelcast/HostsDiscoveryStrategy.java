import com.hazelcast.config.NetworkConfig;
import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//tag::hds[]
public class HostsDiscoveryStrategy extends AbstractDiscoveryStrategy {

    private static final String HOSTS_NIX = "/etc/hosts";
    private static final String HOSTS_WINDOWS = "%SystemRoot%\\system32\\drivers\\etc\\hosts";

    private final String siteDomain;

    HostsDiscoveryStrategy(ILogger logger, Map<String, Comparable> properties) {
        super(logger, properties);

        this.siteDomain = getOrNull("discovery.hosts", HostsDiscoveryConfiguration.DOMAIN);
    }

    @Override
    public Iterable<DiscoveryNode> discoverNodes() {
        List<String> assignments = filterHosts();
        return mapToDiscoveryNodes(assignments);
    }

    private List<String> filterHosts() {
        String os = System.getProperty("os.name");

        String hostsPath;
        if (os.contains("Windows")) {
            hostsPath = HOSTS_WINDOWS;
        } else {
            hostsPath = HOSTS_NIX;
        }

        File hosts = new File(hostsPath);


        List<String> lines = readLines(hosts);

        List<String> assignments = new ArrayList<String>();
        for (String line : lines) {

            if (matchesDomain(line)) {
                assignments.add(line);
            }
        }
        return assignments;
    }

    private Iterable<DiscoveryNode> mapToDiscoveryNodes(List<String> assignments) {
        Collection<DiscoveryNode> discoveredNodes = new ArrayList<DiscoveryNode>();

        for (String assignment : assignments) {
            String address = sliceAddress(assignment);
            String hostname = sliceHostname(assignment);

            Map<String, String> attributes = Collections.singletonMap("hostname", hostname);

            InetAddress inetAddress = mapToInetAddress(address);
            Address addr = new Address(inetAddress, NetworkConfig.DEFAULT_PORT);

            discoveredNodes.add(new SimpleDiscoveryNode(addr, attributes));
        }
        return discoveredNodes;
    }

    private List<String> readLines(File hosts) {
        try {
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader(hosts));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    lines.add(line.trim());
                }
            }

            return lines;
        } catch (IOException e) {
            throw new RuntimeException("Could not read hosts file", e);
        }
    }

    private boolean matchesDomain(String line) {
        if (line.isEmpty()) {
            return false;
        }
        String hostname = sliceHostname(line);
        return hostname.endsWith("." + siteDomain);
    }

    private String sliceAddress(String assignment) {
        String[] tokens = assignment.split("\\p{javaSpaceChar}+");
        if (tokens.length < 1) {
            throw new RuntimeException("Could not find ip address in " + assignment);
        }
        return tokens[0];
    }

    private static String sliceHostname(String assignment) {
        String[] tokens = assignment.split("(\\p{javaSpaceChar}+|\t+)+");
        if (tokens.length < 2) {
            throw new RuntimeException("Could not find hostname in " + assignment);
        }
        return tokens[1];
    }

    private InetAddress mapToInetAddress(String address) {
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not resolve ip address", e);
        }
    }
}
//end::hds[]
package com.hazelcast.client.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.core.HazelcastInstance;

public class ExampleClientConfiguration {

    public static void main(String[] args) throws Exception{
        //tag::scc[]
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress("10.1.1.21", "10.1.1.22:5703")
                .setSmartRouting(true)
                .addOutboundPortDefinition("34700-34710")
                .setRedoOperation(true)
                .setConnectionTimeout(5000)
                .setConnectionAttemptLimit(5);

        AwsConfig clientAwsConfig = new AwsConfig();
        clientAwsConfig.setAccessKey( "my-access-key" )
                .setSecretKey( "my-secret-key" )
                .setRegion( "us-west-1" )
                .setHostHeader( "ec2.amazonaws.com" )
                .setSecurityGroupName( ">hazelcast-sg" )
                .setTagKey( "type" )
                .setTagValue( "hz-members" )
                .setIamRole( "s3access" )
                .setEnabled( true );
        clientConfig.getNetworkConfig().setAwsConfig( clientAwsConfig );
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        //end::scc[]
    }
}

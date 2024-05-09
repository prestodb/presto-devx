package presto.infra;

import com.pulumi.awsx.ec2.Vpc;
import com.pulumi.awsx.ec2.VpcArgs;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author linsong
 */
public class InfraVpc {

    private final Vpc vpc;

    public InfraVpc() {
        String name = "infra-vpc";
        Map<String, String> tags = new HashMap<>(App.TAGS);
        tags.put("Name", name);
        vpc = new Vpc(name, VpcArgs.builder()
                .cidrBlock(App.CIDR_BLOCK)
                .enableDnsHostnames(true)
                .numberOfAvailabilityZones(3)
                .tags(tags)
                .build()
        );
    }

    public Vpc getVpc() {
        return vpc;
    }
}

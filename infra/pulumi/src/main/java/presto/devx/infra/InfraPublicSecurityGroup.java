package presto.devx.infra;

import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupArgs;
import com.pulumi.aws.ec2.inputs.SecurityGroupEgressArgs;
import com.pulumi.aws.ec2.inputs.SecurityGroupIngressArgs;
import com.pulumi.awsx.ec2.Vpc;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author linsong
 */
public class InfraPublicSecurityGroup {

    private final SecurityGroup instance;

    public InfraPublicSecurityGroup(Vpc vpc) {
        String sgName = "presto-devx-infra-security-group-public-access";
        Map<String, String> tags = new HashMap<>(App.TAGS);
        tags.put("Name", sgName);
        instance = new SecurityGroup(sgName, SecurityGroupArgs.builder()
                .description("Enable HTTP/HTTPS/SSH Access")
                .egress(
                        SecurityGroupEgressArgs.builder()
                                .cidrBlocks("0.0.0.0/0")
                                .protocol("-1")
                                .fromPort(0)
                                .toPort(0)
                                .build())
                .ingress(
                        SecurityGroupIngressArgs.builder()
                                .cidrBlocks("0.0.0.0/0")
                                .protocol("tcp")
                                .fromPort(22)
                                .toPort(22)
                                .build(),
                        SecurityGroupIngressArgs.builder()
                                .cidrBlocks("0.0.0.0/0")
                                .protocol("tcp")
                                .fromPort(80)
                                .toPort(80)
                                .build(),
                        SecurityGroupIngressArgs.builder()
                                .cidrBlocks("0.0.0.0/0")
                                .protocol("tcp")
                                .fromPort(443)
                                .toPort(443)
                                .build(),
                        SecurityGroupIngressArgs.builder()
                                .self(Boolean.TRUE)
                                .protocol("-1")
                                .fromPort(0)
                                .toPort(0)
                                .build())
                .name(sgName)
                .tags(tags)
                .vpcId(vpc.vpcId())
                .build());
    }

    public SecurityGroup getInstance() {
        return instance;
    }
}

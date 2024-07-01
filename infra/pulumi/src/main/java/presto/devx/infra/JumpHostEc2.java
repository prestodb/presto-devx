package presto.devx.infra;

import com.pulumi.aws.ec2.Instance;
import com.pulumi.aws.ec2.InstanceArgs;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.enums.InstanceType;
import com.pulumi.awsx.ec2.Vpc;
import com.pulumi.core.Output;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author linsong
 */
public class JumpHostEc2 {

    private final Instance instance;

    public JumpHostEc2(Vpc vpc, SecurityGroup sg, String amiId, Output<String> ec2KeyPairName) {
        Map<String, String> tags = new HashMap<>(App.TAGS);
        tags.put("Name", "presto-devx-infra-jump-host");
        this.instance = new Instance("presto-devx-infra-jump-host", InstanceArgs.builder()
                .ami(amiId)
                .instanceType(InstanceType.T3_2XLarge)
                .keyName(ec2KeyPairName)
                .subnetId(vpc.publicSubnetIds().applyValue(x -> x.get(0)))
                .tags(tags)
                .vpcSecurityGroupIds(Output.all(sg.id()))
                .build());
    }

    public Instance getInstance() {
        return instance;
    }
}

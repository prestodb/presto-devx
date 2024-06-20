package presto.infra;

import com.pulumi.awsnative.route53.HostedZone;
import com.pulumi.core.Output;
import com.pulumi.resources.CustomResourceOptions;

/**
 *
 * @author linsong
 */
public class InfraHostedZone {

    private final HostedZone hostedZone;

    public InfraHostedZone(String hostedZoneId) {
        hostedZone = HostedZone.get("presto-infra-hosted-zone", Output.of(hostedZoneId),
                CustomResourceOptions.builder().build());
    }

    public HostedZone getHostedZone() {
        return hostedZone;
    }
}

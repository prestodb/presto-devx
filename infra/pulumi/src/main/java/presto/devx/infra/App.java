package presto.devx.infra;

import com.pulumi.Config;
import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.aws.ec2.Eip;
import com.pulumi.aws.ec2.EipArgs;
import com.pulumi.core.Output;
import java.util.Map;

public class App {

    public static final String CIDR_BLOCK = "10.178.0.0/16";

    public static final Map<String, String> TAGS = Map.of("team", "presto-dx", "project", "presto-infra");

    public static void main(String[] args) {
        Pulumi.run(App::provisionStack);
    }

    private static void provisionStack(Context context) {
        PrestoInfraS3 s3 = new PrestoInfraS3();
        context.export("presto-devx-infras3-bucket-id", s3.getBucket().id());

        InfraVpc vpc = new InfraVpc();
        context.export("presto-devx-infravpc-id", vpc.getVpc().vpcId());
        context.export("presto-devx-infrapublic-subnet-ids", vpc.getVpc().publicSubnetIds());
        context.export("presto-devx-infraprivate-subnet-ids", vpc.getVpc().privateSubnetIds());

        InfraPublicSecurityGroup sg = new InfraPublicSecurityGroup(vpc.getVpc());
        context.export("presto-devx-inframain-sg-id", sg.getInstance().id());

        Config config = context.config();
        String dockerAmi = config.require("centos-stream-9-docker-ami");
        JumpHostEc2 jumpHostEc2 = new JumpHostEc2(vpc.getVpc(), sg.getInstance(), dockerAmi);
        context.export("presto-devx-infracentos-stream-9-docker-ami", Output.of(dockerAmi));

        Eip eip = new Eip("presto-devx-infrajump-host-elastic-ip", EipArgs.builder()
                .instance(jumpHostEc2.getInstance().id())
                .tags(App.TAGS)
                .build());
        context.export("presto-devx-infrajump-host-elastic-ip", eip.publicIp());

        String sshEd25519PubKey = config.require("ssh-ed25519-pubkey");
        InfraEc2KeyPair ec2KeyPair = new InfraEc2KeyPair(sshEd25519PubKey);
        context.export("presto-devx-infracluster-ec2-keypair-name", ec2KeyPair.getKeyPair().keyName());

        ClusterIntanceProfile clusterIntanceProfile = new ClusterIntanceProfile();
        context.export("presto-devx-infrainstance-profile-name", clusterIntanceProfile.getInstanceProfile().name());
        context.export("presto-devx-infras3-access-role-arn", clusterIntanceProfile.getS3AccessRole().arn());

        Output<String> password = config.requireSecret("rds-mysql-password");
        InfraRdsMysql mysql = new InfraRdsMysql(password, vpc.getVpc(), sg.getInstance());
        context.export("presto-devx-infra-rds-mysql-address", mysql.getInstance().address());
        context.export("presto-devx-infra-rds-mysql-password", mysql.getInstance().password());

        InfraEksCluster eks = new InfraEksCluster(vpc.getVpc());
        context.export("presto-devx-infraeks-kubeconfig", eks.getCluster().kubeconfig());

        String hostedZoneId = config.require("hosted-zone-id");
        InfraHostedZone hostedZone = new InfraHostedZone(hostedZoneId);
        context.export("presto-devx-infrahosted-zone", hostedZone.getHostedZone().name());

        PrestoEcrRepos engProdEcrRepos = new PrestoEcrRepos();
        context.export("presto-devx-infraecr-repos", Output.ofList(engProdEcrRepos.getEcrRepos()));

        InfraWildSslCertificate infraWildSslCertificate = new InfraWildSslCertificate("oss.prestodb.dev");
        context.export("presto-devx-infrainfra-wild-cert", infraWildSslCertificate.getCertificate().arn());

        InfraEmail prestodbDevEmail= new InfraEmail("oss");
        context.export("presto-devx-infrainfra-prestodb-dev-email", prestodbDevEmail.getPrestodbEmail().arn());
    }
}

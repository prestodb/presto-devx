package presto.infra;

import com.pulumi.eks.Cluster;
import com.pulumi.eks.ClusterArgs;
import com.pulumi.eks.ManagedNodeGroup;
import com.pulumi.eks.ManagedNodeGroupArgs;
import com.pulumi.aws.eks.inputs.NodeGroupScalingConfigArgs;
import com.pulumi.aws.iam.Role;
import com.pulumi.aws.iam.RoleArgs;
import com.pulumi.aws.iam.RolePolicyAttachment;
import com.pulumi.aws.iam.RolePolicyAttachmentArgs;
import com.pulumi.awsx.ec2.Vpc;
import com.pulumi.core.Output;
import java.util.List;

/**
 *
 * @author linsong
 */
public class InfraEksCluster {

    private final Cluster cluster;

    public InfraEksCluster(Vpc vpc) {
        Role eksRole = new Role("presto-infra-eks-admin-role", RoleArgs.builder()
                .assumeRolePolicy(
                        """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                            {
                              "Effect": "Allow",
                              "Principal": {
                                "Service": "eks.amazonaws.com"
                              },
                              "Action": "sts:AssumeRole"
                            }
                          ]
                        }
                        """
                )
                .build());

        RolePolicyAttachment eksPolicyAttachment = new RolePolicyAttachment("presto-infra-eks-admin-role-pa",
                RolePolicyAttachmentArgs.builder()
                        .policyArn("arn:aws:iam::aws:policy/AmazonEKSClusterPolicy")
                        .role(eksRole.name())
                        .build());

        Role instanceRole = new Role("presto-infra-eks-node-group-instance-role", RoleArgs.builder()
                .assumeRolePolicy(
                        """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                            {
                              "Effect": "Allow",
                              "Principal": {
                                "Service": "ec2.amazonaws.com"
                              },
                              "Action": "sts:AssumeRole"
                            }
                          ]
                        }
                        """
                )
                .build());

        List<String> policies = List.of(
                "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly",
                "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy",
                "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy",
                "arn:aws:iam::aws:policy/AmazonS3FullAccess");
        for (int i = 0; i < policies.size(); i++) {
            RolePolicyAttachment policyAttachment = new RolePolicyAttachment("presto-infra-eks-node-group-instance-role-pa-" + (i + 1),
                    RolePolicyAttachmentArgs.builder()
                            .policyArn(policies.get(i))
                            .role(instanceRole.name())
                            .build());
        }

        cluster = new Cluster("presto-infra-eks", ClusterArgs.builder()
                .instanceRole(instanceRole)
                .serviceRole(eksRole)
                .skipDefaultNodeGroup(true)
                .subnetIds(vpc.publicSubnetIds())
                .tags(App.TAGS)
                .version("1.28")
                .vpcId(vpc.vpcId())
                .build());

        String ng = "presto-infra-eks-managed-node-group";
        ManagedNodeGroup nodeGroup = new ManagedNodeGroup(ng, ManagedNodeGroupArgs.builder()
                .cluster(cluster)
                .diskSize(100)
                .instanceTypes("m7i.4xlarge")
                .nodeGroupName(ng)
                .nodeRole(instanceRole)
                .scalingConfig(NodeGroupScalingConfigArgs.builder()
                        .desiredSize(2)
                        .maxSize(12)
                        .minSize(2)
                        .build())
                .subnetIds(Output.all(vpc.privateSubnetIds().applyValue(ids -> ids.get(0))))
                .tags(App.TAGS)
                .build());
    }

    public Cluster getCluster() {
        return cluster;
    }
}

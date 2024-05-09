package presto.infra;

import com.pulumi.aws.iam.InstanceProfile;
import com.pulumi.aws.iam.InstanceProfileArgs;
import com.pulumi.aws.iam.Role;
import com.pulumi.aws.iam.RoleArgs;
import com.pulumi.aws.iam.RolePolicyAttachment;
import com.pulumi.aws.iam.RolePolicyAttachmentArgs;

/**
 *
 * @author linsong
 */
public class ClusterIntanceProfile {

    private final Role s3AccessRole;
    private final InstanceProfile instanceProfile;

    public ClusterIntanceProfile() {
        String roleName = "cluster-instance-s3-access-role";
        s3AccessRole = new Role(roleName, RoleArgs.builder()
                .assumeRolePolicy("""
                    {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Effect": "Allow",
                                "Principal": {
                                    "Service": "ec2.amazonaws.com"
                                },
                                "Action": "sts:AssumeRole"
                            },
                            {
                                "Effect": "Allow",
                                "Principal": {
                                    "AWS": "*"
                                },
                                "Action": "sts:AssumeRole"
                            }
                        ]
                    }""")
                .name(roleName)
                .build());

        // Attach S3 full access policy to the role
        RolePolicyAttachment s3FullAccessPolicyAttachment = new RolePolicyAttachment("cluster-instance-s3-full-access-policy-attachment",
                new RolePolicyAttachmentArgs.Builder()
                        .policyArn("arn:aws:iam::aws:policy/AmazonS3FullAccess")
                        .role(s3AccessRole.name())
                        .build());

        // Create an instance profile and associate the role with it
        String profileName = "cluster-instance-profile";
        instanceProfile = new InstanceProfile(profileName, InstanceProfileArgs.builder()
                .name(profileName)
                .role(s3AccessRole.name())
                .tags(App.TAGS)
                .build());
    }

    public Role getS3AccessRole() {
        return s3AccessRole;
    }

    public InstanceProfile getInstanceProfile() {
        return instanceProfile;
    }
}

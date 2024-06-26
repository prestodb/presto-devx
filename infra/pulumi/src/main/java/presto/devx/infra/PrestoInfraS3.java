package presto.devx.infra;

import com.pulumi.aws.s3.Bucket;
import com.pulumi.aws.s3.BucketArgs;
import com.pulumi.aws.s3.BucketObject;
import com.pulumi.aws.s3.BucketObjectArgs;
import com.pulumi.aws.s3.inputs.BucketLifecycleRuleArgs;
import com.pulumi.aws.s3.inputs.BucketLifecycleRuleExpirationArgs;

/**
 *
 * @author linsong
 */
public class PrestoInfraS3 {

    private final Bucket bucket;

    public PrestoInfraS3() {
        String name = "presto-devx-infras3";
        this.bucket = new Bucket(name, BucketArgs.builder()
                .lifecycleRules(
                        BucketLifecycleRuleArgs.builder()
                                .enabled(true)
                                .expiration(BucketLifecycleRuleExpirationArgs.builder()
                                        .days(180)
                                        .build())
                                .prefix("clusters")
                                .build())
                .bucket(name)
                .tags(App.TAGS)
                .build());
        BucketObject bucketObject = new BucketObject("clusters", BucketObjectArgs.builder()
                .bucket(bucket.bucket())
                .content("")
                .key("clusters/")
                .build());
        BucketObject bucketObject2 = new BucketObject("infra", BucketObjectArgs.builder()
                .bucket(bucket.bucket())
                .content("")
                .key("infra/")
                .build());
    }

    public Bucket getBucket() {
        return bucket;
    }
}

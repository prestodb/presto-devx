package presto.infra;

import com.pulumi.aws.ses.EmailIdentity;
import com.pulumi.aws.ses.EmailIdentityArgs;

/**
 *
 * @author linsong
 */
public class InfraEmail {

    private final EmailIdentity prestodbEmail;

    public InfraEmail(String email) {
        this.prestodbEmail = new EmailIdentity("infra-email", EmailIdentityArgs.builder()
                .email(email + "@prestodb.dev").build());
    }

    public EmailIdentity getPrestodbEmail() {
        return prestodbEmail;
    }
}

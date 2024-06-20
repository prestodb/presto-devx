package presto.infra;

import com.pulumi.aws.ec2.KeyPair;
import com.pulumi.aws.ec2.KeyPairArgs;

/**
 *
 * @author linsong
 */
public class Ec2KeyPairInfra {

    private final KeyPair keyPair;

    public Ec2KeyPairInfra() {
        this.keyPair = new KeyPair("presto-infra-infra-ec2-keypair", KeyPairArgs.builder()
                .keyName("presto-infra-infra-ec2-keypair")
                .publicKey("ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIJnAI1iY/NWlpbsWM9zVt/E15tJeA2kxB1f3tbvOVoZC linsong.wang@ibm.com")
                .tags(App.TAGS)
                .build());
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}

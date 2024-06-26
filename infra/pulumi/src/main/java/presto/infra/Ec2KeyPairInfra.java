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
        String name = "presto-devx-infrainfra-ec2-keypai";
        this.keyPair = new KeyPair(name, KeyPairArgs.builder()
                .keyName(name)
                .publicKey("ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIJnAI1iY/NWlpbsWM9zVt/E15tJeA2kxB1f3tbvOVoZC linsong.wang@ibm.com")
                .tags(App.TAGS)
                .build());
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}

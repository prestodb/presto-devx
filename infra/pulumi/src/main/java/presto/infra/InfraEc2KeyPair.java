package presto.infra;

import com.pulumi.aws.ec2.KeyPair;
import com.pulumi.aws.ec2.KeyPairArgs;

/**
 *
 * @author wanglinsong
 */
public class InfraEc2KeyPair {

    private final KeyPair keyPair;

    public InfraEc2KeyPair(String sshEd25519Pubkey) {
        String name = "presto-devx-infraec2-keypair";
        this.keyPair = new KeyPair(name, KeyPairArgs.builder()
                .keyName(name)
                .publicKey("ssh-ed25519 " + sshEd25519Pubkey)
                .tags(App.TAGS)
                .build());
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}

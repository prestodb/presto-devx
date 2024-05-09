package presto.infra;

import com.pulumi.aws.acm.Certificate;
import com.pulumi.aws.acm.CertificateArgs;

/**
 *
 * @author linsong
 */
public class InfraWildSslCertificate {

    private final Certificate certificate;

    public InfraWildSslCertificate(String domainName) {
        
        certificate = new Certificate("infra-wild-ssl-cert", CertificateArgs.builder()
                .domainName("*." + domainName)
                .tags(App.TAGS)
                .validationMethod("DNS")
                .build());
    }

    public Certificate getCertificate() {
        return certificate;
    }
}

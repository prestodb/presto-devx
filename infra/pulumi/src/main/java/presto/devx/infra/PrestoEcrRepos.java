package presto.devx.infra;

import com.pulumi.aws.ecr.Repository;
import com.pulumi.aws.ecr.RepositoryArgs;
import com.pulumi.aws.ecr.inputs.RepositoryImageScanningConfigurationArgs;
import com.pulumi.core.Output;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author linsong
 */
public class PrestoEcrRepos {

    private final List<Repository> ecrRepos = new ArrayList<>();

    private final List<String> repos = List.of(
            "jenkins-controller",
            "presto",
            "presto-native",
            "presto-native-dependency"
    );

    public PrestoEcrRepos() {
        for (String r : repos) {
            String name = "devx/" + r;
            Repository repo = new Repository(name, RepositoryArgs.builder()
                    .imageScanningConfiguration(RepositoryImageScanningConfigurationArgs.builder()
                            .scanOnPush(true)
                            .build())
                    .imageTagMutability("MUTABLE")
                    .name(name)
                    .tags(App.TAGS)
                    .build());
            ecrRepos.add(repo);
        }
    }

    public List<Output<String>> getEcrRepos() {
        return ecrRepos.stream().map(repo -> repo.repositoryUrl()).toList();
    }
}

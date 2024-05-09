package presto.infra;

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
            "jenkins-controller"
    );

    public PrestoEcrRepos() {
        for (String r : repos) {
            String name = "devex/" + r;
            Repository repo = new Repository(name, RepositoryArgs.builder()
                    .imageScanningConfiguration(RepositoryImageScanningConfigurationArgs.builder()
                            .scanOnPush(true)
                            .build())
                    .imageTagMutability("IMMUTABLE")
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

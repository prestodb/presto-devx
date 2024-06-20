FROM amazonlinux:2022

ARG HADOOP_VERSION=3.3.3
ARG HIVE_METASTORE_VERSION=3.1.3
ARG HADOOP_AWS_DOWNLOAD_LINK=https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.3.3/hadoop-aws-3.3.3.jar
ARG AWS_SDK_BUNDLE_LINK=https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.12.433/aws-java-sdk-bundle-1.12.433.jar

WORKDIR /opt

ENV HADOOP_HOME=/opt/hadoop
ENV HMS_HOME=/opt/hms
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64/jre/

RUN yum install gzip java-1.8.0-amazon-corretto tar which -y && \
    curl -L https://repo1.maven.org/maven2/org/apache/hive/hive-standalone-metastore/${HIVE_METASTORE_VERSION}/hive-standalone-metastore-${HIVE_METASTORE_VERSION}-bin.tar.gz | tar zxf - && \
    curl -L http://archive.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz | tar zxf - && \
    curl -SLO ${HADOOP_AWS_DOWNLOAD_LINK} && \
    curl -SLO ${AWS_SDK_BUNDLE_LINK} && \
    curl -SLO https://repo1.maven.org/maven2/org/apache/hive/hive-metastore/${HIVE_METASTORE_VERSION}/hive-metastore-${HIVE_METASTORE_VERSION}.jar && \
    curl -SLO https://repo1.maven.org/maven2/org/apache/hive/hive-common/${HIVE_METASTORE_VERSION}/hive-common-${HIVE_METASTORE_VERSION}.jar && \
    curl -SLO https://repo1.maven.org/maven2/org/apache/hive/hive-serde/${HIVE_METASTORE_VERSION}/hive-serde-${HIVE_METASTORE_VERSION}.jar && \
    mv /opt/apache-hive-metastore-$HIVE_METASTORE_VERSION-bin $HMS_HOME && \
    mkdir -p $HMS_HOME/lib && mv /opt/hive-metastore-*.jar /opt/hive-common-*.jar /opt/hive-serde-*.jar $HMS_HOME/lib/ && \
    mv /opt/hadoop-$HADOOP_VERSION $HADOOP_HOME && \
    mv /opt/hadoop-aws-*.jar /opt/aws-java-sdk-bundle-*.jar $HADOOP_HOME/share/hadoop/common/ && \
    curl -L https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.32/mysql-connector-j-8.0.32.jar -o $HMS_HOME/lib/mysql-connector-j-8.0.32.jar

WORKDIR $HMS_HOME
EXPOSE 9083

COPY entrypoint.sh /opt/entrypoint.sh
ENTRYPOINT ["/opt/entrypoint.sh"]
## Startup command from Kubernetes or for local use for a built <image_name_or_id>
# AWS_ACCESS_KEY,AWS_SECRET_ACCESS_KEY  = set to an account that has write access to the s3 bucket of your choice
# Note also that this will start a HMS against the MySQL database whose creds have been passed in
#docker run -it --rm -e AWS_ACCESS_KEY=foo -e AWS_SECRET_ACCESS_KEY=bar <image_name_or_id> sh -c  "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64/jre/; cd /opt/hms/bin/; ./start-metastore  -v --hiveconf javax.jdo.option.ConnectionDriverName='com.mysql.jdbc.Driver' --hiveconf javax.jdo.option.ConnectionURL='jdbc:mysql://<host-name>/<database-name>'  --hiveconf javax.jdo.option.ConnectionUserName='<username>' --hiveconf javax.jdo.option.ConnectionPassword='<password>'"

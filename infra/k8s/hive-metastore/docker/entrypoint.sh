#!/bin/bash -ex

export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64/jre/

/opt/hms/bin/start-metastore -v \
    --hiveconf javax.jdo.option.ConnectionDriverName=com.mysql.cj.jdbc.Driver \
    --hiveconf metastore.storage.schema.reader.impl=org.apache.hadoop.hive.metastore.SerDeStorageSchemaReader \
    --hiveconf javax.jdo.option.ConnectionURL=${JDBC_MYSQL} \
    --hiveconf javax.jdo.option.ConnectionUserName=${DB_USER} \
    --hiveconf javax.jdo.option.ConnectionPassword=${DB_PASS} \
    --hiveconf fs.s3a.access.key=${AWS_ACCESS_KEY_ID} \
    --hiveconf fs.s3a.secret.key=${AWS_SECRET_ACCESS_KEY}

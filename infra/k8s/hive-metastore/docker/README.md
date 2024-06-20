## Docker setup for HMS 3+
Contains Dockerfile to build a HMS with relevant libs

Steps
1. Build a docker image `docker build <parent-folder>`
1. Start image with your container orchestrator. Samples below :
(Note that you need to set  `AWS_ACCESS_KEY` and `AWS_SECRET_ACCESS_KEY` to an account that has write access to the s3 bucket of your choice)  

With an external relational database

```
 docker run -it --rm -e AWS_ACCESS_KEY=foo -e AWS_SECRET_ACCESS_KEY=bar <image_name> sh -c \
  "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64/jre/; cd /opt/hms/bin/; ./start-metastore  -v --hiveconf javax.jdo.option.ConnectionDriverName='com.mysql.jdbc.Driver' --hiveconf javax.jdo.option.ConnectionURL='jdbc:mysql://<host-name>/<database-name>'  --hiveconf javax.jdo.option.ConnectionUserName='<username>' --hiveconf javax.jdo.option.ConnectionPassword='<password>'"
```  

Use a derby embedded database generated on the fly :
```
 docker run -it --rm -e AWS_ACCESS_KEY=foo -e AWS_SECRET_ACCESS_KEY=bar <image_name> sh -c \
  "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64/jre/; cd /opt/hms/bin/; ./schematool -initSchema -dbType derby; ./start-metastore  -v"
```


1. The instantiated container will expose port 9083 that can be connected to

## Creating 

### HMS relational database
First, create an empty database on your server
```
CREATE DATABASE <dbname>
```

Then, using Hive SchemaTool, create the tables/indexes on the target databases (example with Postgres below)

```
docker run -it --rm  <image_name> bash

cd /opt/hms/bin/

./schematool -initSchema -dbType postgres -driver 'org.postgresql.Driver' -url 'jdbc:postgresql://localhost:5432/<dbname>' -userName '<username>' -passWord '<password>'
```

Ouput would look like below :
```
1210/1216    CREATE INDEX IDX_RUNTIME_STATS_CREATE_TIME ON RUNTIME_STATS(CREATE_TIME);
No rows affected (0.01 seconds)
1211/1216    
1212/1216    
1213/1216    -- -----------------------------------------------------------------
1214/1216    -- Record schema version. Should be the last step in the init script
1215/1216    -- -----------------------------------------------------------------
1216/1216    INSERT INTO "VERSION" ("VER_ID", "SCHEMA_VERSION", "VERSION_COMMENT") VALUES (1, '3.1.0', 'Hive release version 3.1.0');
1 row affected (0.004 seconds)
Closing: org.postgresql.jdbc.PgConnection
sqlline version 1.3.0
Initialization script completed
schemaTool completed
```


### Sample HMS catalog file for Presto

```
hive.properties: |
    hive.metastore.uri=thrift://hive-metastore.hive-metastore:9083
    hive.s3.iam-role=arn:aws:iam::932483864676:role/cloud-datasource-s3-role
    connector.name=hive-hadoop2
    # metastore
    hive.metastore-cache-ttl=10m
    hive.metastore-refresh-interval=2m
    hive.metastore-timeout=3m
    # writer properties
    hive.max-partitions-per-writers=5000
    hive.collect-column-statistics-on-write=true
    # split optimization
    hive.max-outstanding-splits=1800
    hive.max-initial-splits=600
    hive.max-initial-split-size=128MB
    hive.max-split-size=256MB
    hive.split-loader-concurrency=32
    # dml permission
    hive.non-managed-table-writes-enabled=true
    hive.non-managed-table-creates-enabled=true
    # S3
    hive.s3.max-error-retries=50
    hive.s3.connect-timeout=1m
    hive.s3.socket-timeout=2m
    # pushdown
    hive.pushdown-filter-enabled=true
    # hive.parquet.pushdown-filter-enabled=true
    # Data content specific
    hive.recursive-directories=true
    # affinity scheduling
    hive.node-selection-strategy=SOFT_AFFINITY
    hive.orc.use-column-names=true
    hive.orc.file-tail-cache-enabled=true
    hive.orc.file-tail-cache-size=114MB
    hive.orc.file-tail-cache-ttl-since-last-access=6h
    hive.orc.stripe-metadata-cache-enabled=true
    hive.orc.stripe-footer-cache-size=160MB
    hive.orc.stripe-footer-cache-ttl-since-last-access=6h
    hive.orc.stripe-stream-cache-size=300MB
    hive.orc.stripe-stream-cache-ttl-since-last-access=6h
    hive.parquet.use-column-names=true
    hive.parquet-batch-read-optimization-enabled=true
    hive.parquet.metadata-cache-enabled=true
    hive.parquet.metadata-cache-size=300MB
    hive.parquet.metadata-cache-ttl-since-last-access=6h
    # hive legacy config
    hive.allow-drop-table=true
    hive.allow-rename-table=true
    hive.allow-add-column=true
    hive.allow-drop-column=true
    hive.allow-rename-column=true
```

Once you have a Presto setup with above config, you can create a new table like :  
**Note that you need to use `s3a://` as the filesystem for any s3 based tables. `s3://` or `s3n://` do NOT work** [(See why)](https://hadoop.apache.org/docs/stable/hadoop-aws/tools/hadoop-aws/index.html#Overview:~:text=Apache%E2%80%99s%20Hadoop%E2%80%99s%20original%20s3%3A//%20client.%20This%20is%20no%20longer%20included%20in%20Hadoop.)  
This has *no relation* to the Presto S3 FS impl.

```

 CREATE TABLE t0_hms (        
    "a0" integer,
    "b0" integer,
    "c0" integer,
    "d0" integer,
    "e0" integer,
    "f0" integer,
    "g0" integer,
    "h0" integer,
    "i0" integer,
    "j0" integer,
    "k0" integer,
    "l0" integer,
    "m0" integer,
    "n0" integer,
    "o0" integer,
    "p0" integer,
    "q0" integer,
    "r0" integer,
    "t0" integer,
    "u0" integer,
    "unn0" integer,
    "group0" integer
 )
 WITH (
    bucket_count = 2,
    bucketed_by = ARRAY['a0'],
    format = 'PARQUET',
    partitioned_by = ARRAY['group0'],
    sorted_by = ARRAY[],
    external_location = 's3a://ahana-oss-tpch-benchmark/simple/t0'
 )
 ```

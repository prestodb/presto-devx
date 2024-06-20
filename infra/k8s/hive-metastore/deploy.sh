#!/bin/bash -ex

AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens hive-metastore

DEPLOY_MYSQL_HOST=$(pulumi stack output -s ibm-data-ai/presto-deploy-infra/deploy-infra --json | jq -r '."presto-deploy-mysql-address"')
DB_PASS=$(pulumi stack output -s ibm-data-ai/presto-deploy-infra/deploy-infra --json --show-secrets | jq -r '."presto-deploy-mysql-password"')
AWS_ACCESS_KEY_ID=$(op item get "IBM AWS Access Key" --fields label="access key id")
AWS_SECRET_ACCESS_KEY=$(op item get "IBM AWS Access Key" --fields label="secret access key")

envsubst < hive-metastore-service.yaml | kubectl apply -f -

kubectl get all

#!/usr/bin/env bash
set -e

export PRESTO_MYSQL_PASSWORD=$(pulumi stack output -s ibm-data-ai/presto-deploy-infra/deploy-infra --json --show-secrets | jq -r '."presto-deploy-mysql-password"')

kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens benchto

envsubst < benchto-service.yaml | kubectl apply -f -

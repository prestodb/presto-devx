#!/bin/bash -ex

export PRESTO_MYSQL_PASSWORD=$(pulumi stack output -s ibm-data-ai/presto-deploy-infra/deploy-infra --json --show-secrets | jq -r '."presto-deploy-mysql-password"')
export PULUMI_API_TOKEN=$(op item get "Pulumi API Token" --fields label=credential)
export S3_PROXY_AUTH=$(op item get "S3 Proxy Web Server Auth" --fields label="basic-auth")

kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens presto-dashboard-staging

## https://kubernetes.github.io/ingress-nginx/examples/auth/basic/
# htpasswd -c auth presto
#  password: **********
# kubectl create secret generic ingress-basic-auth --from-file=auth

envsubst < pd-service-staging.yaml | kubectl apply -f -

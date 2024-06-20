#!/usr/bin/env bash -ex

export CLUSTERS_IBM_PRESTODB_DEV_PASSWORD=$(op item get "S3 Proxy Web Server Auth" --fields label=password)

AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens s3-proxy

envsubst < s3-presto-deploy-infra-and-cluster.yaml | kubectl apply -f -
open https://clusters.ibm.prestodb.dev

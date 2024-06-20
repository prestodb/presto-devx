#!/bin/bash -ex

export BUILD_VERSION=${BUILD_VERSION:?error}

export AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens diffto

envsubst < diffto.yaml | kubectl apply -f -

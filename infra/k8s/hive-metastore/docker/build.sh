#!/bin/bash -ex

AWS_PROFILE=ibm-aws
AWS_REGION="us-east-1"

ACCOUNT_ID=$(aws sts get-caller-identity --profile ${AWS_PROFILE} --output text | awk '{print $1}')
AWS_ECR="${ACCOUNT_ID}.dkr.ecr.$AWS_REGION.amazonaws.com"
DOCKER_REPO="${AWS_ECR}/engprod/hms"
DOCKER_TAG="3.1.3-$(TZ=UTC date +%Y%m%d)H$(git rev-parse --short HEAD)"
DOCKER_IMAGE="${DOCKER_REPO}:${DOCKER_TAG}"
printenv | sort

docker buildx build --platform=linux/amd64 -t ${DOCKER_IMAGE} --load .
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ECR}
docker image ls | grep ${DOCKER_REPO}
docker push ${DOCKER_IMAGE}

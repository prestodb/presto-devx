#!/bin/sh -ex

export JENKINS_VERSION="2.467-jdk17"

export AWS_PROFILE="prestodb-aws"
export AWS_REGION="us-east-1"

ACCOUNT_ID=$(aws sts get-caller-identity --output text | awk '{print $1}')
AWS_ECR="${ACCOUNT_ID}.dkr.ecr.$AWS_REGION.amazonaws.com"
DOCKER_REPO="${AWS_ECR}/devx/jenkins-controller"
DOCKER_TAG="$JENKINS_VERSION-$(TZ=UTC date +%Y%m%d)H$(git rev-parse --short HEAD)"
JENKINS_IMAGE="${DOCKER_REPO}:${DOCKER_TAG}"

printenv | sort

docker buildx build --platform=linux/amd64 --build-arg JENKINS_VERSION -t ${JENKINS_IMAGE} .
aws ecr get-login-password --region ${AWS_REGION} --profile ${AWS_PROFILE} | docker login --username AWS --password-stdin ${AWS_ECR}
docker push ${JENKINS_IMAGE}

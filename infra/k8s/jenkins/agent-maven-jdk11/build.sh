#!/bin/bash -ex

AWS_PROFILE=prestodb-aws
AWS_REGION="us-east-1"

ACCOUNT_ID=$(aws sts get-caller-identity --profile ${AWS_PROFILE} --output text | awk '{print $1}')
AWS_ECR="${ACCOUNT_ID}.dkr.ecr.us-east-1.amazonaws.com"
DOCKER_IMAGE_TAG="$(TZ=UTC date +%Y%m%d)H$(git rev-parse --short=7 HEAD)"
DOCKER_IMAGE_VERSIONED="${AWS_ECR}/devx/jenkins-agent-maven-jdk11:${DOCKER_IMAGE_TAG}"
DOCKER_IMAGE_LATEST="${AWS_ECR}/devx/jenkins-agent-maven-jdk11:latest"

printenv | sort

docker buildx build --load --platform=linux/amd64 -t ${DOCKER_IMAGE_VERSIONED} .
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ECR}
docker tag ${DOCKER_IMAGE_VERSIONED} ${DOCKER_IMAGE_LATEST}
docker push ${DOCKER_IMAGE_VERSIONED}
docker push ${DOCKER_IMAGE_LATEST}

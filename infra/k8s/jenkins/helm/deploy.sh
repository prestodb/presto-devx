#!/bin/sh -ex

AWS_PROFILE=prestodb-aws

ACCOUNT_ID=$(aws sts get-caller-identity --output text | awk '{print $1}')
kubectx arn:aws:eks:us-east-1:${ACCOUNT_ID}:cluster/presto-devx-infra-eks
kubens jenkins-controller

helm repo add jenkins https://charts.jenkins.io || OK
helm repo update

kubectl apply -f service-account.yaml
kubectl apply -f pvc.yaml
helm upgrade -i jenkins-controller -f jenkins-values.yaml --version 5.3.0 jenkins/jenkins

jsonpath="{.data.jenkins-admin-password}"
secret=$(kubectl get secret -n jenkins-controller jenkins-controller -o jsonpath=$jsonpath)
echo $(echo $secret | base64 --decode)

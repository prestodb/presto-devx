#!/bin/sh -ex

export AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens jenkins-controller

helm repo add jenkins https://charts.jenkins.io || OK
helm repo update

kubectl apply -f service-account.yaml
kubectl apply -f pvc.yaml
helm upgrade -i jenkins-controller -f jenkins-values.yaml --version 4.9.1 jenkins/jenkins

jsonpath="{.data.jenkins-admin-password}"
secret=$(kubectl get secret -n jenkins-controller jenkins-controller -o jsonpath=$jsonpath)
echo $(echo $secret | base64 --decode)

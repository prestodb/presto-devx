#!/bin/bash -ex

# https://kubernetes.github.io/ingress-nginx/deploy/#aws
# https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/aws/nlb-with-tls-termination/deploy.yaml

AWS_PROFILE=prestodb-aws
kubectx arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-infra-eks
kubens ingress-nginx

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm upgrade -i ingress-nginx -f values.yaml --version 4.10.1 ingress-nginx/ingress-nginx 

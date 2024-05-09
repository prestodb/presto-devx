#!/bin/sh -ex

export AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens prometheus-grafana

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
# helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

helm upgrade -i prometheus-grafana -f prometheus-grafana-values.yaml --version 55.5.1 prometheus-community/kube-prometheus-stack

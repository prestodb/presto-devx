#!/bin/sh -ex

export AWS_PROFILE=ibm-aws
kubectx arn:aws:eks:us-east-1:093347738777:cluster/deploy-infra-eksCluster-c1c221f
kubens influxdb2

helm repo add influxdata https://helm.influxdata.com/
helm repo update
helm upgrade --install influxdb2 -f influxdb2-values.yaml --version 2.1.2 influxdata/influxdb2

echo "To retrieve the password for the 'admin' user:"
echo $(kubectl get secret influxdb2-auth -o "jsonpath={.data['admin-password']}" --namespace influxdb2 | base64 --decode)

#!/bin/bash -ex

# https://weaveworks-gitops.awsworkshop.io/25_workshop_2_ha-dr/50_add_yamls/10_alb_ingress.html
# https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-0.32.0/deploy/static/provider/aws/deploy.yaml

export AWS_PROFILE=prestodb-aws
kubectx arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-infra-eks
kubens ingress-nginx

kubectl apply -f ingress.yaml


## output
# Warning: resource namespaces/ingress-nginx is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
# namespace/ingress-nginx configured
# serviceaccount/ingress-nginx created
# configmap/ingress-nginx-controller created
# clusterrole.rbac.authorization.k8s.io/ingress-nginx created
# clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx created
# role.rbac.authorization.k8s.io/ingress-nginx created
# rolebinding.rbac.authorization.k8s.io/ingress-nginx created
# service/ingress-nginx-controller-admission created
# service/ingress-nginx-controller created
# deployment.apps/ingress-nginx-controller created
# clusterrole.rbac.authorization.k8s.io/ingress-nginx-admission created
# clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
# job.batch/ingress-nginx-admission-create created
# job.batch/ingress-nginx-admission-patch created
# role.rbac.authorization.k8s.io/ingress-nginx-admission created
# rolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
# serviceaccount/ingress-nginx-admission created
# error: resource mapping not found for name: "ingress-nginx-admission" namespace: "ingress-nginx" from "ingress.yaml": no matches for kind "ValidatingWebhookConfiguration" in version "admissionregistration.k8s.io/v1beta1"
# ensure CRDs are installed first

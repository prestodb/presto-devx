
## eks auto scaler
https://github.com/kubernetes/autoscaler/blob/master/cluster-autoscaler/cloudprovider/aws/README.md

export AWS_PROFILE=prestodb-aws



1.
cluster-autoscaler-policy.json
'''
aws iam create-policy \
    --policy-name arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-eks-auto-scaling-policy \
    --policy-document file://cluster-autoscaler-policy.json
'''
output:
'''
{
    "Policy": {
        "PolicyName": "arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-eks-auto-scaling-policy",
        "PolicyId": "ANPARLO7VOSM3NCGMQD44",
        "Arn": "arn:aws:iam::932483864676:policy/arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-eks-auto-scaling-policy",
        "Path": "/",
        "DefaultVersionId": "v1",
        "AttachmentCount": 0,
        "PermissionsBoundaryUsageCount": 0,
        "IsAttachable": true,
        "CreateDate": "2023-10-20T18:50:37+00:00",
        "UpdateDate": "2023-10-20T18:50:37+00:00"
    }
}
'''

2.
eksctl utils associate-iam-oidc-provider --region=us-east-1 --cluster=arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-eksCluster-c1c221f --approve
2023-10-20 11:18:48 [ℹ]  IAM Open ID Connect provider is already associated with cluster "arn:aws:eks:us-east-1:932483864676:cluster/presto-devx-eksCluster-c1c221f" in "us-east-1"



3
curl -O https://raw.githubusercontent.com/kubernetes/autoscaler/master/cluster-autoscaler/cloudprovider/aws/examples/cluster-autoscaler-autodiscover.yaml
update cluster-autoscaler-autodiscover.yaml
kubectl apply -f cluster-autoscaler-autodiscover.yaml

'''
Warning: resource serviceaccounts/cluster-autoscaler is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
serviceaccount/cluster-autoscaler configured
clusterrole.rbac.authorization.k8s.io/cluster-autoscaler created
role.rbac.authorization.k8s.io/cluster-autoscaler created
clusterrolebinding.rbac.authorization.k8s.io/cluster-autoscaler created
rolebinding.rbac.authorization.k8s.io/cluster-autoscaler created
deployment.apps/cluster-autoscaler created
'''


4.
kubectl annotate serviceaccount cluster-autoscaler \
  -n kube-system \
  eks.amazonaws.com/role-arn=arn:aws:iam::932483864676:role/presto-devx-eks-auto-scaling-role \
  --overwrite

'''
serviceaccount/cluster-autoscaler annotated
'''

kubectl patch deployment cluster-autoscaler \
  -n kube-system \
  -p '{"spec":{"template":{"metadata":{"annotations":{"cluster-autoscaler.kubernetes.io/safe-to-evict": "false"}}}}}'

'''
deployment.apps/cluster-autoscaler patched
'''

kubectl -n kube-system edit deployment.apps/cluster-autoscaler



5.
kubectl -n kube-system logs -f deployment.apps/cluster-autoscaler

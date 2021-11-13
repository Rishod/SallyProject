gcloud container clusters update sally-cluster  \
  --workload-pool=vlad-proj-test.svc.id.goog
  --zone=us-central1-c

kubectl create serviceaccount --namespace staging cloud-sql-sa

gcloud iam service-accounts add-iam-policy-binding --role roles/iam.workloadIdentityUser --member "serviceAccount:vlad-proj-test.svc.id.goog[staging/cloud-sql-sa]" sally-cloud-sql-sq@vlad-proj-test.iam.gserviceaccount.com

kubectl annotate serviceaccount --namespace staging cloud-sql-sa iam.gke.io/gcp-service-account=sally-cloud-sql-sq@vlad-proj-test.iam.gserviceaccount.com
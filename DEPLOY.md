# Deploying route-service-demo to Cloud Run

Run these commands in **Google Cloud Shell** (bash), not on your Windows machine.
Replace `YOUR_PROJECT_ID` with your own project id. Billing must be enabled on the project.

Before you start, push your local commits so the repo on GitHub has the code:
`git push origin main` (run this on your PC).

## 1. Clone the repo
```bash
git clone https://github.com/dbrmar/route-service-demo.git
cd route-service-demo
```

## 2. Select the project and region
```bash
gcloud config set project YOUR_PROJECT_ID
gcloud config set run/region europe-west1
```

## 3. Enable the APIs
```bash
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com
```

## 4. Deploy from source
The project has a Dockerfile, so Cloud Build builds the image from it (not with buildpacks).
```bash
gcloud run deploy route-service-demo \
  --source . \
  --region europe-west1 \
  --allow-unauthenticated
```
The first time, gcloud asks to create the Artifact Registry repository `cloud-run-source-deploy`. Answer `Y`.

## 5. Check it with curl
```bash
URL=$(gcloud run services describe route-service-demo --region europe-west1 --format='value(status.url)')
echo $URL

curl -s $URL/actuator/health
curl -s -i -X POST $URL/routes -H "Content-Type: application/json" -d '{"name":"Cluj - Brasov"}'
curl -s $URL/routes
curl -s "$URL/routes?status=PLANNED"
curl -s $URL/routes/1
curl -s -i $URL/routes/99
curl -s -i -X POST $URL/routes -H "Content-Type: application/json" -d '{"name":""}'
```
Expected results: health returns `{"status":"UP"}`, the create returns `201` with a `Location` header,
`/routes/99` returns a `404` problem+json, and a blank name returns a `400`.

## 6. Cleanup (to stop all costs)
```bash
gcloud run services delete route-service-demo --region europe-west1 --quiet
gcloud artifacts repositories delete cloud-run-source-deploy --location europe-west1 --quiet
gcloud storage rm -r gs://YOUR_PROJECT_ID_cloudbuild
```
The last bucket holds the uploaded source archives. Check its exact name with `gcloud storage ls`
before deleting it. If the project only exists for this demo, deleting the whole project in the
Console removes everything at once.

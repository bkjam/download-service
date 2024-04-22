# File Download URL Proxy

This section covers how to implement dynamic download URL proxy with Nginx + Lua + Redis.

## Usage

```bash
##################################################
# Step 1. Build File Download Service as a Docker Image
##################################################
cd FileDownloadService
./gradlew jibDockerBuild

##################################################
# Step 2. Start Minio, Redis, Nginx, FileDownload Service
##################################################
cd ..
docker-compose up -d

##################################################
# Step 3. Upload File to S3
##################################################
# Open Web Browser and go to the url below
# Login to Minio (username = admin, password = password)
# Create bucket named "storage"
# Upload a file into bucket
http://localhost:9001

##################################################
# Step 4. Test Download Url Proxy
##################################################
# Generate Download Url
curl localhost:8000/download-service/api/v1/download/url?fileId=token.txt
# Example output
# > http://localhost:8000/download/e5a203358b532db46c5e1f865a57b870

# Test Download Url (should proxy to Presigned url)
curl http://localhost:8000/download/e5a203358b532db46c5e1f865a57b870      # returns something
curl http://localhost:8000/download/e5a203358b532db46c5e1f865a57b870      # download url no longer valid
```

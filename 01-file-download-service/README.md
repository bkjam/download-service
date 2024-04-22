# File Download Service

This section covers how to implement file download endpoints using Spring Boot and Minio Client.

## Usage

```bash
##################################################
# Step 1. Start Minio
##################################################
cd Minio
docker-compose up -d

##################################################
# Step 2. Start File Download Service
##################################################
cd FileDownloadService
./gradlew bootrun

##################################################
# Step 3. Upload File to S3
##################################################
# Open Web Browser and go to the url below
# Login to Minio (username = admin, password = password)
# Create bucket named "storage"
# Upload a file into bucket
http://localhost:9001

##################################################
# Step 4. Test Download Endpoints (Enter URL in Web Browser)
##################################################
# Single File Download
http://localhost:8080/api/v1/download?fileId=yourfile

# Multiple File Download
http://localhost:8080/api/v1/download/zip?fileIds=yourfile,yourfile2
http://localhost:8080/api/v1/download/zip?fileIds=yourfile&fileIds=yourfile2

# Presigned Url
http://localhost:8080/api/v1/download/presigned?fileId=yourfile
```

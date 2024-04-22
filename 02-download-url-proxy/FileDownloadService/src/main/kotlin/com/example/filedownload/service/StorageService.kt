package com.example.filedownload.service

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class StorageService(
    private val minioClient: MinioClient,
    @Value("\${object-storage.bucket}") private val bucket: String
) {
    fun generatePresignedUrl(fileId: String): String? {
        val args = GetPresignedObjectUrlArgs.builder()
            .bucket(bucket)
            .`object`(fileId)
            .method(Method.GET)
            .expiry(5, TimeUnit.MINUTES)
            .build()
        return minioClient.getPresignedObjectUrl(args)
    }
}

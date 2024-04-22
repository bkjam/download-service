package com.example.filedownload.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioClientConfig(
    @Value("\${object-storage.endpoint}") private val s3Endpoint: String,
    @Value("\${object-storage.access-key}") private val accessKey: String,
    @Value("\${object-storage.access-secret}") private val accessSecret: String
) {
    @Bean
    fun minioClient(): MinioClient {
        try {
            return MinioClient.builder()
                .endpoint(s3Endpoint)
                .credentials(accessKey, accessSecret)
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }
}

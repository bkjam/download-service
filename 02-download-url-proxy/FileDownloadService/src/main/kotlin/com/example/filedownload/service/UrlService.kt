package com.example.filedownload.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class UrlService(
    private val redisTemplate: StringRedisTemplate,
    private val uniqueIdGenerator: UniqueIdGenerator,
    @Value("\${download.url}") private val downloadUrl: String
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UrlService::class.java)
    }

    fun generateDownloadUrl(presignedUrl: String): String {
        val downloadId = uniqueIdGenerator.generateUniqueId() ?: throw Exception("Unable to generate unique download Id")
        if (!redisTemplate.opsForValue().get(downloadId).isNullOrEmpty()) {
            throw Exception("Download Id already exists!")
        }

        logger.info("Storing to Redis :  $downloadId = $presignedUrl")
        redisTemplate.opsForValue()[downloadId] = presignedUrl
        return "$downloadUrl/$downloadId"
    }
}

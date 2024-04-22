package com.example.filedownload.controller

import com.example.filedownload.service.StorageService
import com.example.filedownload.service.UrlService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/download")
class DownloadController(
    private val storageService: StorageService,
    private val urlService: UrlService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(DownloadController::class.java)
    }

    @GetMapping("/presigned")
    fun getFilePresignedUrl(@RequestParam fileId: String): ResponseEntity<String> {
        logger.info("Received request to generate presigned url for file - $fileId")
        val presignedUrl = storageService.generatePresignedUrl(fileId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(presignedUrl)
    }

    @GetMapping("/url")
    fun getFileDownloadUrl(@RequestParam fileId: String): ResponseEntity<String> {
        logger.info("Received request to generate download url for file - $fileId")
        val presignedUrl = storageService.generatePresignedUrl(fileId) ?: return ResponseEntity.notFound().build()
        val downloadUrl = urlService.generateDownloadUrl(presignedUrl)
        return ResponseEntity.ok(downloadUrl)
    }
}

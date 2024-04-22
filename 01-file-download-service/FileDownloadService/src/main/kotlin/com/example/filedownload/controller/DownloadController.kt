package com.example.filedownload.controller

import com.example.filedownload.service.StorageService
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/download")
class DownloadController(
    private val storageService: StorageService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(DownloadController::class.java)
    }

    @GetMapping
    fun downloadFile(@RequestParam fileId: String, response: HttpServletResponse) {
        logger.info("Received request to download file - $fileId")
        storageService.downloadFile(fileId, response)
    }

    @GetMapping("/presigned")
    fun getFilePresignedUrl(@RequestParam fileId: String): ResponseEntity<String> {
        logger.info("Received request to generate presigned url for file - $fileId")
        val presignedUrl = storageService.generatePresignedUrl(fileId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(presignedUrl)
    }

    @GetMapping("/zip")
    fun downloadMultipleFiles(@RequestParam fileIds: List<String>, response: HttpServletResponse) {
        logger.info("Received request to download files - ${fileIds.joinToString(",")}")
        storageService.downloadMultipleFiles(fileIds, response)
    }
}

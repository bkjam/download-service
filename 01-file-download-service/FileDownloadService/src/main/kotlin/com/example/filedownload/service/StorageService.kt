package com.example.filedownload.service

import io.minio.GetObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.StatObjectArgs
import io.minio.http.Method
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.io.InputStream
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class StorageService(
    private val minioClient: MinioClient,
    @Value("\${object-storage.bucket}") private val bucket: String
) {
    fun downloadFile(fileId: String, response: HttpServletResponse) {
        // Set File Content
        val statArgs = StatObjectArgs.builder().bucket(bucket).`object`(fileId).build()
        val stat = minioClient.statObject(statArgs)

        // Set Response Header
        response.contentType = stat.contentType()
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileId\"")

        // Download File
        val getArgs = GetObjectArgs.builder().bucket(bucket).`object`(fileId).build()
        val inputStream: InputStream = minioClient.getObject(getArgs)
        IOUtils.copyLarge(inputStream, response.outputStream)
    }

    fun downloadMultipleFiles(fileIds: List<String>, response: HttpServletResponse) {
        // Set Response Header
        response.contentType = "application/zip";
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")

        // Create Zip File and write to Output Stream
        val zipOut = ZipOutputStream(response.outputStream)
        fileIds.forEach { fileId ->
            val args = GetObjectArgs.builder().bucket(bucket).`object`(fileId).build()
            val inputStream = minioClient.getObject(args)
            val zipEntry = ZipEntry(fileId)
            zipOut.putNextEntry(zipEntry)
            StreamUtils.copy(inputStream, zipOut)
            zipOut.closeEntry()
        }
        zipOut.finish()
        zipOut.close()
    }

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

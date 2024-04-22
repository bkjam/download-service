package com.example.filedownload.service

import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Random

@Component
class UniqueIdGenerator {
    fun generateUniqueId(): String? {
        val timestamp = System.currentTimeMillis() // Get current timestamp
        val randomId = generateRandomString(8) // Generate random identifier
        val combinedData = timestamp.toString() + randomId // Concatenate timestamp and random identifier
        return try {
            // Hash the combined data using MD5 hashing algorithm
            val digest: MessageDigest = MessageDigest.getInstance("MD5")
            val hashBytes: ByteArray = digest.digest(combinedData.toByteArray(StandardCharsets.UTF_8))
            bytesToHex(hashBytes) // Convert hashed bytes to hexadecimal string
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            // Handle exception
            null
        }
    }

    private fun generateRandomString(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder(length)
        val random = Random()
        for (i in 0 until length) {
            val index: Int = random.nextInt(characters.length)
            sb.append(characters[index])
        }
        return sb.toString()
    }

    private fun bytesToHex(bytes: ByteArray): String? {
        val hexString = StringBuilder()
        for (b in bytes) {
            val hex = Integer.toHexString(0xff and b.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }
}

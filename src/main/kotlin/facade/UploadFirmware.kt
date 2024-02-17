package org.example.facade

import kotlinx.coroutines.delay
import kotlin.random.Random

class UploadFirmware {
    suspend fun uploadFirmware(
        data: ByteArray,
    ): Boolean {
        delay(Random.nextLong(1000, 5000))
        try {
            SerialBLE.shared.send(data)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }

    suspend fun uploadFirmware(
        data: ByteArray,
        timeout: Long,
    ): Boolean {
        delay(Random.nextLong(1000, 5000))
        try {
            SerialBLE.shared.send(data, timeout)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }

    suspend fun uploadFirmware(
        data: ByteArray,
        timeout: Long,
        retries: Int,
    ): Boolean {
        delay(Random.nextLong(1000, 5000))
        try {
            SerialBLE.shared.send(data, timeout, retries)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}
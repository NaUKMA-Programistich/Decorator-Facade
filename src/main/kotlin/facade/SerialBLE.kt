package org.example.facade

import kotlinx.coroutines.withTimeoutOrNull

class SerialBLE {
    companion object {
        const val SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"
        const val CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"

        const val REQUEST_TIMEOUT = 1000L

        const val REQUEST_CONNECT = 0x01
        const val REQUEST_DISCONNECT = 0x02
        const val REQUEST_SEND = 0x03

        val shared = SerialBLE()
    }

    fun send(data: ByteArray): ByteArray {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        } else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        // Відправити дані по Bluetooth та отримати відповідь
        return byteArrayOf(0x00)
    }

    suspend fun send(data: ByteArray, timeout: Long): ByteArray {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        } else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        withTimeoutOrNull(timeout) {
            // Відправити дані по Bluetooth та отримати відповідь

        }

        // Відправити дані по Bluetooth та отримати відповідь
        return byteArrayOf(0x00)
    }

    suspend fun send(data: ByteArray, timeout: Long, retries: Int): ByteArray {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        } else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        for (i in 0 until retries) {
            try {
                return send(data, timeout)
            } catch (e: IllegalArgumentException) {
                continue
            }
        }

        // Відправити дані по Bluetooth та отримати відповідь
        return byteArrayOf(0x00)
    }
}
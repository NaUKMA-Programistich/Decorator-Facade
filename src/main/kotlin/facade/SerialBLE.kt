package org.example.facade

import kotlinx.coroutines.withTimeoutOrNull

class SerialBLE {
    companion object {
        // UUID для сервісу Bluetooth LE, який використовується для з'єднання
        const val SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"
        // UUID для характеристики Bluetooth LE, через яку передаються дані
        const val CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"

        // Тайм-аут для запитів, в мілісекундах
        const val REQUEST_TIMEOUT = 1000L

        // Константи для різних типів запитів
        const val REQUEST_CONNECT = 0x01
        const val REQUEST_DISCONNECT = 0x02
        const val REQUEST_SEND = 0x03

        // Створення єдиного екземпляру (singleton) класу для глобального доступу
        val shared = SerialBLE()
    }

    // Метод для відправки даних без використання корутин
    fun send(data: ByteArray): ByteArray {
        // Перевірка на пустоту даних
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        }
        // Перевірка на довжину даних (не більше 20 байт)
        else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        // Відправлення даних по Bluetooth і повернення відповіді (мок)
        return byteArrayOf(0x00)
    }

    // Метод для відправки даних з використанням корутин та тайм-ауту
    suspend fun send(data: ByteArray, timeout: Long): ByteArray {
        // Перевірка на пустоту даних
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        }
        // Перевірка на довжину даних
        else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        // Використання withTimeoutOrNull для обмеження часу відправлення
        withTimeoutOrNull(timeout) {
            // Логіка відправлення даних по Bluetooth (мок)
        }

        // Повернення мокованої відповіді
        return byteArrayOf(0x00)
    }

    // Метод для відправки даних з використанням корутин, тайм-ауту та повторень при помилці
    suspend fun send(data: ByteArray, timeout: Long, retries: Int): ByteArray {
        // Перевірка на пустоту та розмір даних
        if (data.isEmpty()) {
            throw IllegalArgumentException("Data is empty")
        } else if (data.size > 20) {
            throw IllegalArgumentException("Data is too long")
        }

        // Цикл для повторення відправлення даних у разі помилки
        for (i in 0 until retries) {
            try {
                // Спроба відправити дані з урахуванням тайм-ауту
                return send(data, timeout)
            } catch (e: IllegalArgumentException) {
                // Ігнорування помилки та спроба повторного відправлення
                continue
            }
        }

        // Повернення мокованої відповіді, якщо всі спроби були безуспішні
        return byteArrayOf(0x00)
    }
}

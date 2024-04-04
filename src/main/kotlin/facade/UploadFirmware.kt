package org.example.facade

import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Клас `UploadFirmware` відповідає за завантаження прошивки через BLE (Bluetooth Low Energy) інтерфейс.
 * Використовує корутини для асинхронного виконання завдань, що дозволяє виконувати завантаження без блокування основного потоку.
 */
class UploadFirmware {

    /**
     * Завантажує дані прошивки.
     *
     * @param data Масив байтів даних прошивки.
     * @return Повертає `true`, якщо завантаження пройшло успішно, інакше `false`.
     */
    suspend fun uploadFirmware(data: ByteArray): Boolean {
        // Симуляція тривалості завантаження випадковим часом.
        delay(Random.nextLong(1000, 5000))
        try {
            // Відправлення даних через SerialBLE інтерфейс.
            SerialBLE.shared.send(data)
            return true
        } catch (e: IllegalArgumentException) {
            // Повертаємо `false` у випадку помилки відправлення.
            return false
        }
    }

    /**
     * Завантажує дані прошивки з визначеним таймаутом.
     *
     * @param data Масив байтів даних прошивки.
     * @param timeout Час у мілісекундах, після якого спроба відправлення буде вважатись невдалою.
     * @return Повертає `true`, якщо завантаження пройшло успішно, інакше `false`.
     */
    suspend fun uploadFirmware(data: ByteArray, timeout: Long): Boolean {
        delay(Random.nextLong(1000, 5000))
        try {
            SerialBLE.shared.send(data, timeout)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }

    /**
     * Завантажує дані прошивки з визначеним таймаутом і кількістю спроб.
     *
     * @param data Масив байтів даних прошивки.
     * @param timeout Час у мілісекундах, після якого спроба відправлення буде вважатись невдалою.
     * @param retries Кількість спроб відправлення перед визнанням невдачі.
     * @return Повертає `true`, якщо завантаження пройшло успішно, інакше `false`.
     */
    suspend fun uploadFirmware(data: ByteArray, timeout: Long, retries: Int): Boolean {
        delay(Random.nextLong(1000, 5000))
        try {
            SerialBLE.shared.send(data, timeout, retries)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}

package org.example

import kotlinx.coroutines.runBlocking
import org.example.decorator.*
import org.example.facade.UpdateChannel
import org.example.facade.UpdateFacade

fun main() = runBlocking {
    // testFacade()
    testDecorator()
}

// Функція suspend вказує, що вона є асинхронною та може бути призупинена.
// Це означає, що вона може виконувати тривалі операції, такі як запити до мережі або баз даних,
// без блокування основного потоку виконання. Ця функція призначена для тестування фасаду оновлення.
suspend fun testFacade() {
    // Виведення повідомлення у консоль для ідентифікації початку тестування фасаду.
    println("Test facade:")

    // Створення екземпляру фасаду UpdateFacade та виклик методу updateDevice для ініціації процесу оновлення.
    // Метод приймає параметри: поточний канал оновлення (currentChannel), поточну версію програмного забезпечення ("1.0.0"),
    // новий канал оновлення (newChannel) та нову версію програмного забезпечення ("2.0.1").
    UpdateFacade().updateDevice(
        currentChannel = UpdateChannel.RELEASE, // Поточний канал оновлення встановлено як RELEASE.
        "1.0.0", // Поточна версія програмного забезпечення.
        newChannel = UpdateChannel.DEV, // Новий канал оновлення встановлено як DEV.
        "2.0.1" // Нова версія програмного забезпечення.
    )

    // Виведення роздільної лінії у консоль після завершення тестування для кращої візуальної організації.
    println("---------------------")
}


/**
 * Функція для демонстрації роботи з декораторами.
 * Декоратор дозволяє динамічно додавати нову поведінку до об'єктів.
 */
fun testDecorator() {
    // Прапорець для умовного декорування
    val isNeedAuth = true
    val isNeedThrottler = true
    val isNeedDuplicate = true
    println("Test decorator:")

    // Створення базового пристрою
    val device = Device("Device")
    // Ініціалізація базового API для взаємодії з пристроєм
    var serialApi: SerialApi = SerialApiImpl(device)
    println("---------------------")

    // Умовне декорування з додаванням функціоналу авторизації
    if (isNeedAuth) {
        // Декорування serialApi для додавання функціоналу авторизації
        serialApi = SerialAuthDecorator(serialApi)
        println("---------------------")
    }

    if (isNeedDuplicate) {
        // Декорування serialApi для додавання функціоналу дублювання
        serialApi = SerialDuplicateDecorator(serialApi)
        println("---------------------")
    }

    if (isNeedThrottler) {
        // Декорування serialApi для додавання функціоналу обмеження потоку
        serialApi = SerialOverflowThrottlerDecorator(serialApi)
        println("---------------------")
    }

    serialApi.receive()
    println("---------------------")

    serialApi.send(byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05))
    println("---------------------")
}

fun testRawDecorator() {
    val device = Device("Device")
    // Ініціалізація базового API для взаємодії з пристроєм
    val serialApi: SerialApi = SerialApiImpl(device)
    println("---------------------")

    // Декорування serialApi для додавання функціоналу авторизації
    val authAndDuplicate = object : SerialApi {
        override fun send(data: ByteArray) {
            println("AuthAndDuplicate: Send data")

            // Виклик методу send() базового API
            val duplicateData = data + data
            val authData = duplicateData + duplicateData.calculateCrc()

            serialApi.send(authData)
        }

        override fun receive(): ByteArray {
            println("AuthAndDuplicate: Receive data")

            val data = serialApi.receive()
            val halfData = data.copyOfRange(0, data.size / 2)
            val crc = data.copyOfRange(data.size / 2, data.size)

            if (halfData.calculateCrc() != crc[0]) {
                throw Exception("CRC check failed")
            }

            return halfData
        }

        override fun getSpeed(): Int {
            return serialApi.getSpeed()
        }
    }
    authAndDuplicate.receive()
}

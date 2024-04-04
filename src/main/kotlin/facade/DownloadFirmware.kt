package org.example.facade

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.random.Random

// Клас DownloadFirmware відповідає за завантаження прошивки з зовнішнього ресурсу.
class DownloadFirmware {
    // Функція завантаження прошивки, яка приймає канал оновлення та версію. Використовує стандартного HTTP клієнта.
    suspend fun download(channel: UpdateChannel, version: String): ByteArray {
        // Побудова HTTP запиту з використанням URI, який формується на основі каналу та версії.
        val request = HttpRequest.newBuilder()
            .uri(URI("https://download.example.com/$channel/$version"))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        // Створення нового екземпляру HTTP клієнта.
        val client = HttpClient.newHttpClient()
        // Виконання HTTP запиту в окремому потоці I/O для асинхронності.
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        // Штучна затримка для імітації часу завантаження.
        delay(Random.nextLong(1000, 5000))
        // Повернення мокованих даних замість реальної відповіді сервера.
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }

    // Перевантажена версія функції завантаження, яка додатково приймає екземпляр HttpClient.
    suspend fun download(channel: UpdateChannel, version: String, client: HttpClient): ByteArray {
        // Аналогічне створення HTTP запиту.
        val request = HttpRequest.newBuilder()
            .uri(URI("https://download.example.com/$channel/$version"))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        // Виконання запиту з використанням переданого клієнта.
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        // Повернення мокованих даних.
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }

    // Перевантажена версія функції, що дозволяє вказати конкретний URI для завантаження.
    suspend fun download(channel: UpdateChannel, version: String, client: HttpClient, uri: URI): ByteArray {
        // Створення запиту з конкретним URI.
        val request = HttpRequest.newBuilder()
            .uri(uri)
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        // Виконання запиту.
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        // Повернення мокованих даних.
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }
}

package org.example.facade

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.random.Random

// Клас для завантаження прошивки
class DownloadFirmware {
    suspend fun download(channel: UpdateChannel, version: String): ByteArray {
        val request = HttpRequest.newBuilder()
            .uri(URI("https://download.example.com/$channel/$version"))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        val client = HttpClient.newHttpClient()
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        delay(Random.nextLong(1000, 5000))
        // return lambda.invoke().body() мок результата
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }

    suspend fun download(channel: UpdateChannel, version: String, client: HttpClient): ByteArray {
        val request = HttpRequest.newBuilder()
            .uri(URI("https://download.example.com/$channel/$version"))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        // return lambda.invoke().body() мок результата
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }

    suspend fun download(channel: UpdateChannel, version: String, client: HttpClient, uri: URI): ByteArray {
        val request = HttpRequest.newBuilder()
            .uri(uri)
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()
        val lambda = withContext(Dispatchers.IO) {
            { client.send(request, HttpResponse.BodyHandlers.ofByteArray()) }
        }
        // return lambda.invoke().body() мок результата
        return byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
    }
}
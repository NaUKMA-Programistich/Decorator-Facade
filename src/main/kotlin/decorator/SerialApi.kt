package org.example.decorator

// Звичайний інтерфейс для роботи з пристроєм
interface SerialApi {
    fun send(data: ByteArray)
    fun receive(): ByteArray
    fun getSpeed(): Int
}

package org.example.facade

import kotlinx.coroutines.delay
import kotlin.random.Random

class ControlDevice {
    suspend fun turnOn() {
        delay(Random.nextLong(1000, 5000))
        SerialBLE.shared.send(byteArrayOf(0x01))
    }

    suspend fun turnOff() {
        delay(Random.nextLong(1000, 5000))
        SerialBLE.shared.send(byteArrayOf(0x00))
    }

    suspend fun setMode(mode: Int) {
        delay(Random.nextLong(1000, 5000))
        SerialBLE.shared.send(byteArrayOf(0x02, mode.toByte()))
    }
}
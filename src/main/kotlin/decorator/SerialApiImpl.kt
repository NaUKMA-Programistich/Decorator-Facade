package org.example.decorator

// Реалізація інтерфейсу для роботи з пристроєм
class SerialApiImpl(private val device: Device): SerialApi {
    override fun send(data: ByteArray) {
        println("Sending data to ${device.name}")
    }

    override fun receive(): ByteArray {
        println("Receiving data from ${device.name}")
        return ByteArray(0x01)
    }

    override fun getSpeed(): Int {
        return 9600
    }
}

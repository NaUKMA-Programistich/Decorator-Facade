package org.example.decorator

// Декоратор для розширення функціоналу класу SerialApi
open class SerialApiDecorator(private val wrapper : SerialApi): SerialApi {
    override fun send(data: ByteArray) {
        wrapper.send(data)
    }

    override fun receive(): ByteArray {
        return wrapper.receive()
    }

    override fun getSpeed(): Int {
        return wrapper.getSpeed()
    }
}
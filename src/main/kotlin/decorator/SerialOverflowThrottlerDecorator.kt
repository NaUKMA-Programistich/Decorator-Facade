package org.example.decorator

// Декоратор для обмеження розміру буфера
class SerialOverflowThrottlerDecorator(private val source: SerialApi): SerialApiDecorator(source) {
    private val buffer = mutableListOf<Byte>()

    companion object {
        private const val MAX_BUFFER_SIZE = 1024
    }

    override fun send(data: ByteArray) {
        if (buffer.size + data.size > MAX_BUFFER_SIZE) {
            throw IllegalStateException("Buffer overflow")
        }
        buffer.addAll(data.toList())
        println("Buffer size: ${buffer.size}")
        source.send(data)
    }

    override fun receive(): ByteArray {
        val data = source.receive()
        buffer.removeAll(data.toList())
        println("Buffer size: ${buffer.size}")
        return data
    }
}
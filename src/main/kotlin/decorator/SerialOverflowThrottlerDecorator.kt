package org.example.decorator

/**
 * Decorator class for limiting the size of the buffer in a serial communication API.
 * This class implements the SerialApi interface and uses a decorator pattern to add new functionality
 * (buffer size control) on top of the existing serial API functionality.
 *
 * @param source The SerialApi instance that this decorator wraps. All calls are forwarded to this source
 * after the additional logic implemented by this decorator is executed.
 */
class SerialOverflowThrottlerDecorator(private val source: SerialApi) : SerialApiDecorator(source) {
    // Internal buffer to temporarily store the data before forwarding it to the source SerialApi.
    private val buffer = mutableListOf<Byte>()

    companion object {
        // Maximum allowed size of the buffer. Attempts to add data beyond this limit will result in an exception.
        private const val MAX_BUFFER_SIZE = 1024
    }

    /**
     * Sends data through the serial API while enforcing the buffer size limit.
     * If adding the new data would exceed the maximum buffer size, an IllegalStateException is thrown.
     * Otherwise, the data is added to the buffer and then sent through the source SerialApi.
     *
     * @param data The data to be sent.
     * @throws IllegalStateException if adding the data would exceed the maximum buffer size.
     */
    override fun send(data: ByteArray) {
        if (buffer.size + data.size > MAX_BUFFER_SIZE) {
            throw IllegalStateException("Buffer overflow")
        }
        buffer.addAll(data.toList())
        println("Buffer size: ${buffer.size}")
        source.send(data)
    }

    /**
     * Receives data from the serial API and removes it from the internal buffer.
     * This method calls the receive method of the source SerialApi, removes the received data from the buffer,
     * and then returns the data.
     *
     * @return The data received from the serial API.
     */
    override fun receive(): ByteArray {
        val data = source.receive()
        buffer.removeAll(data.toList())
        println("Buffer size: ${buffer.size}")
        return data
    }
}

package org.example.decorator

// Декоратор для додавання CRC розміру буфера
class SerialAuthDecorator(private val source: SerialApi): SerialApiDecorator(source) {

    override fun send(data: ByteArray) {
        val buffer = ByteArray(data.size + 1)
        data.copyInto(buffer)
        buffer[buffer.size - 1] = calculateCrc(data)
        super.send(buffer)
    }

    private fun calculateCrc(data: ByteArray): Byte {
        var crc: Byte = 0x00
        data.forEach { byte ->
            var b = byte
            for (i in 0 until 8) {
                val mix = (crc.toInt() xor b.toInt()) and 0x80
                crc = (crc.toInt() shl 1).toByte()
                if (mix != 0) crc = (crc.toInt() xor 0x07).toByte()
                b = (b.toInt() shl 1).toByte()
            }
        }
        return crc
    }
}
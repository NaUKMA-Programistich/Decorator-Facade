package org.example.decorator

/**
 * Декоратор, який додає до даних контрольну суму CRC перед відправленням.
 * Він розширює можливості базового компонента SerialApi, дозволяючи забезпечити додаткову обробку даних.
 *
 * @property source Посилання на об'єкт SerialApi, до якого застосовується декоратор.
 */
class SerialAuthDecorator(private val source: SerialApi): SerialApiDecorator(source) {

    init {
        println("SerialAuthDecorator created")
    }

    /**
     * Перевизначена функція для відправлення даних.
     * Додає до масиву даних контрольну суму CRC на останню позицію, а потім відправляє змінений масив.
     *
     * @param data Масив байтів даних, який потрібно відправити.
     */
    override fun send(data: ByteArray) {
        println("Auth send")
        // Створення нового буфера для даних з додатковим місцем для CRC.
        val buffer = ByteArray(data.size + 1)
        // Копіювання існуючих даних у новий буфер.
        data.copyInto(buffer)
        // Обчислення та додавання CRC до кінця буфера.
        buffer[buffer.size - 1] = data.calculateCrc()
        // Відправлення буфера з даними та CRC через базовий метод.
        source.send(buffer)
    }

    override fun receive(): ByteArray {
        println("Auth receive")
        return source.receive()
    }
}

/**
 * Функція для обчислення контрольної суми CRC.
 * Використовується для забезпечення цілісності даних.
 *
 * @param data Масив байтів даних, для якого потрібно обчислити CRC.
 * @return Контрольна сума CRC як байт.
 */
fun ByteArray.calculateCrc(): Byte {
    var crc: Byte = 0x00
    this.forEach { byte ->
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
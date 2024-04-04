package org.example.decorator

/**
 * Декоратор, який додає до даних контрольну суму CRC перед відправленням.
 * Він розширює можливості базового компонента SerialApi, дозволяючи забезпечити додаткову обробку даних.
 *
 * @property source Посилання на об'єкт SerialApi, до якого застосовується декоратор.
 */
class SerialAuthDecorator(private val source: SerialApi): SerialApiDecorator(source) {

    /**
     * Перевизначена функція для відправлення даних.
     * Додає до масиву даних контрольну суму CRC на останню позицію, а потім відправляє змінений масив.
     *
     * @param data Масив байтів даних, який потрібно відправити.
     */
    override fun send(data: ByteArray) {
        // Створення нового буфера для даних з додатковим місцем для CRC.
        val buffer = ByteArray(data.size + 1)
        // Копіювання існуючих даних у новий буфер.
        data.copyInto(buffer)
        // Обчислення та додавання CRC до кінця буфера.
        buffer[buffer.size - 1] = calculateCrc(data)
        // Виведення обчисленого CRC для перевірки.
        println("CRC: ${buffer[buffer.size - 1]}")
        // Відправлення буфера з даними та CRC через базовий метод.
        super.send(buffer)
    }

    /**
     * Функція для обчислення контрольної суми CRC.
     * Використовується для забезпечення цілісності даних.
     *
     * @param data Масив байтів даних, для якого потрібно обчислити CRC.
     * @return Контрольна сума CRC як байт.
     */
    private fun calculateCrc(data: ByteArray): Byte {
        // Ініціалізація CRC нульовим значенням.
        var crc: Byte = 0x00
        // Ітерація по кожному байту даних.
        data.forEach { byte ->
            var b = byte
            // Обробка кожного біта в байті.
            for (i in 0 until 8) {
                // XOR операція між CRC та бітом даних, враховуючи тільки старший біт.
                val mix = (crc.toInt() xor b.toInt()) and 0x80
                // Зсув CRC вліво.
                crc = (crc.toInt() shl 1).toByte()
                // Якщо в результаті mix вийшло ненульове значення, виконуємо XOR з 0x07.
                if (mix != 0) crc = (crc.toInt() xor 0x07).toByte()
                // Зсув поточного байта даних вліво.
                b = (b.toInt() shl 1).toByte()
            }
        }
        // Повернення обчисленого CRC.
        return crc
    }
}

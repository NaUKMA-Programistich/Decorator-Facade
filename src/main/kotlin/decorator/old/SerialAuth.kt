package decorator.old

import org.example.decorator.SerialApi

/**
 * Клас, який реалізує інтерфейс SerialApi та додає до даних контрольну суму CRC перед відправленням.
 * Забезпечує додаткову обробку даних для забезпечення їхньої цілісності.
 */
class SerialAuth: SerialApi {
    init {
        println("SerialAuth created")
    }


    /**
     * Функція для відправлення даних.
     * Додає до масиву даних контрольну суму CRC на останню позицію, а потім відправляє змінений масив.
     *
     * @param data Масив байтів даних, який потрібно відправити.
     */
    override fun send(data: ByteArray) {
        println("Auth send")
        // Створення нового буфера для даних з додатковим місцем для CRC.
        val buffer = ByteArray(data.size + 1)
        // Копіювання існуючих даних у новий буфер.
        System.arraycopy(data, 0, buffer, 0, data.size)
        // Обчислення та додавання CRC до кінця буфера.
        buffer[data.size] = calculateCrc(data)
        // Виведення обчисленого CRC для перевірки.
        println("CRC: ${buffer[data.size]}")
        // Тут має бути ваш код для відправлення буфера з даними та CRC через Serial API.
        // Наприклад, використання зовнішньої бібліотеки або API для відправлення даних.
    }

    /**
     * Припустимо, що ця функція імплементована для отримання даних, але для прикладу вона не реалізована.
     *
     * @return Масив байтів даних, отриманих з серійного API.
     */
    override fun receive(): ByteArray {
        println("Auth receive")
        // Тут має бути ваш код для отримання даних через Serial API.
        // Для прикладу, цей метод не реалізований.
        return byteArrayOf()
    }

    override fun getSpeed(): Int {
        // Тут має бути ваш код для отримання швидкості передачі даних через Serial API.
        // Для прикладу, цей метод не реалізований.
        return 0
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
                // Якщо в результаті mix вийшло ненульове значення, виконуємо XOR з поліномом 0x07.
                if (mix != 0) crc = (crc.toInt() xor 0x07).toByte()
                // Зсув поточного байта даних вліво.
                b = (b.toInt() shl 1).toByte()
            }
        }
        // Повернення обчисленого CRC.
        return crc
    }
}

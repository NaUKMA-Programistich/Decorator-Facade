package decorator.old

import org.example.decorator.SerialApi

/**
 * Клас для обмеження розміру буфера в API серійної комунікації.
 * Цей клас реалізує інтерфейс SerialApi і використовує власну логіку для контролю розміру буфера,
 * не вдаючись до механізму декоратора.
 */
class SerialOverflowThrottler: SerialApi {
    init {
        println("SerialOverflowThrottler created")
    }

    // Внутрішній буфер для тимчасового зберігання даних.
    private val buffer = mutableListOf<Byte>()

    companion object {
        // Максимально допустимий розмір буфера.
        private const val MAX_BUFFER_SIZE = 1024
    }

    /**
     * Відправляє дані через серійний API, контролюючи при цьому ліміт розміру буфера.
     * Якщо додавання нових даних перевищує максимальний розмір буфера, кидається IllegalStateException.
     * В іншому випадку, дані додаються до буфера та відправляються.
     *
     * @param data Дані для відправлення.
     * @throws IllegalStateException якщо додавання даних перевищить максимальний розмір буфера.
     */
    override fun send(data: ByteArray) {
        println("OverflowThrottler send")
        if (buffer.size + data.size > MAX_BUFFER_SIZE) {
            throw IllegalStateException("Переповнення буфера")
        }
        buffer.addAll(data.toList())
    }

    /**
     * Отримує дані з серійного API і видаляє їх з внутрішнього буфера.
     * Цей метод передбачає видалення отриманих даних з буфера та повернення їх.
     *
     * @return Дані, отримані з серійного API.
     */
    override fun receive(): ByteArray {
        println("OverflowThrottler receive")
        // Тут додається ваш код для отримання даних через серійний API
        val data = byteArrayOf() // Припустимо, це дані, отримані з API
        buffer.removeAll(data.toList())
        return data
    }

    override fun getSpeed(): Int {
        return 0
    }
}

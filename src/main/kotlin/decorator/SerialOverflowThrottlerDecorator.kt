package org.example.decorator

/**
 * Декоратор для обмеження розміру буфера в API серійної комунікації.
 * Цей клас реалізує інтерфейс SerialApi і використовує патерн декоратора для додавання нової функціональності
 * (контроль розміру буфера) до існуючої функціональності серійного API.
 *
 * @param source Екземпляр SerialApi, який цей декоратор обгортає. Всі виклики перенаправляються до цього джерела
 * після виконання додаткової логіки, реалізованої цим декоратором.
 */
class SerialOverflowThrottlerDecorator(private val source: SerialApi) : SerialApiDecorator(source) {
    init {
        println("SerialOverflowThrottlerDecorator created")
    }

    // Внутрішній буфер для тимчасового зберігання даних перед їхнім перенаправленням до джерела SerialApi.
    private val buffer = mutableListOf<Byte>()

    companion object {
        // Максимально допустимий розмір буфера. Спроби додати дані понад цей ліміт призведуть до винятку.
        private const val MAX_BUFFER_SIZE = 1024
    }

    /**
     * Відправляє дані через серійний API, контролюючи при цьому ліміт розміру буфера.
     * Якщо додавання нових даних перевищує максимальний розмір буфера, кидається IllegalStateException.
     * В іншому випадку, дані додаються до буфера та відправляються через джерело SerialApi.
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
        source.send(data)
    }

    /**
     * Отримує дані з серійного API і видаляє їх з внутрішнього буфера.
     * Цей метод викликає метод прийому даних від джерела SerialApi, видаляє отримані дані з буфера,
     * а потім повертає дані.
     *
     * @return Дані, отримані з серійного API.
     */
    override fun receive(): ByteArray {
        val data = source.receive()
        buffer.removeAll(data.toList())
        println("OverflowThrottler receive")
        return data
    }
}

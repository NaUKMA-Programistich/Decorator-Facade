package org.example.decorator

/**
 * Декоратор для дублювання даних при відправленні через серійний порт.
 * При відправленні кожен байт даних дублюється, а при отриманні - видаляється кожен другий байт,
 * щоб відновити оригінальні дані.
 *
 * @property source Об'єкт, що реалізує інтерфейс SerialApi, до якого застосовується декоратор.
 */
class SerialDuplicateDecorator(private val source: SerialApi) : SerialApiDecorator(source) {
    init {
        println("SerialDuplicateDecorator created")
    }

    /**
     * Відправляє дані, дублюючи кожен байт.
     * Кожен байт у масиві даних дублюється, щоб забезпечити додаткову редундантність переданих даних.
     *
     * @param data Масив байтів даних, який потрібно відправити.
     */
    override fun send(data: ByteArray) {
        println("Duplicate send")
        // Створення нового масиву для дубльованих даних
        val duplicatedData = ByteArray(data.size * 2)
        data.forEachIndexed { index, byte ->
            // Дублювання кожного байта
            duplicatedData[index * 2] = byte
            duplicatedData[index * 2 + 1] = byte
        }

        // Відправлення дубльованих даних через джерело
        source.send(duplicatedData)
    }

    /**
     * Отримує дані, видаляючи кожен другий байт.
     * При отриманні даних кожен другий байт видаляється для відновлення оригінального масиву даних.
     *
     * @return Масив байтів даних, отриманих з серійного порту після видалення кожного другого байта.
     */
    override fun receive(): ByteArray {
        println("Duplicate receive")
        // Отримання дубльованих даних
        var receivedData = source.receive()
        val data = if (receivedData.size % 2 == 0) {
            receivedData
        } else {
            // Якщо кількість байтів непарна, видалення останнього байта
            receivedData = receivedData.copyOf(receivedData.size - 1)
            receivedData
        }
        receivedData.forEachIndexed { index, byte ->
            // Копіювання кожного першого з дубльованих байтів
            if (index % 2 == 0) {
                data[index / 2] = byte
            }
        }

        // Повернення відновленого масиву даних
        return receivedData
    }
}

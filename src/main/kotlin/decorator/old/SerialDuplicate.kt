package org.example.decorator.old

import org.example.decorator.SerialApi

class SerialDuplicate: SerialApi {
    init {
        println("SerialDuplicate created")
    }

    /**
     * Відправляє дані через серійний порт, дублюючи кожен байт.
     *
     * @param data Масив байтів даних для відправлення.
     */
    override fun send(data: ByteArray) {
        println("Duplicate send")
        val duplicatedData = ByteArray(data.size * 2)
        data.forEachIndexed { index, byte ->
            duplicatedData[index * 2] = byte
            duplicatedData[index * 2 + 1] = byte // Дублювання байта
        }
    }

    /**
     * Отримує дані з серійного порту. Реалізація в цьому прикладі є умовною.
     *
     * @return Масив байтів даних, отриманих з серійного порту.
     */
    override fun receive(): ByteArray {
        println("Duplicate receive")
        // Умовна реалізація: повернення заздалегідь визначених даних
        val receivedData = byteArrayOf(1, 1, 2, 2, 3, 3) // Приклад отриманих дубльованих даних
        return receivedData
    }

    /**
     * Повертає швидкість серійного з'єднання.
     *
     * @return Швидкість з'єднання у бодах.
     */
    override fun getSpeed(): Int {
        return 9600 // Швидкість з'єднання у бодах
    }
}

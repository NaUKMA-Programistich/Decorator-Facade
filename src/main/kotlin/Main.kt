package org.example

import kotlinx.coroutines.runBlocking
import org.example.decorator.*
import org.example.facade.UpdateChannel
import org.example.facade.UpdateFacade

fun main() = runBlocking {
    // testFacade()
    testDecorator()
}

// Функція suspend вказує, що вона є асинхронною та може бути призупинена.
// Це означає, що вона може виконувати тривалі операції, такі як запити до мережі або баз даних,
// без блокування основного потоку виконання. Ця функція призначена для тестування фасаду оновлення.
suspend fun testFacade() {
    // Виведення повідомлення у консоль для ідентифікації початку тестування фасаду.
    println("Test facade:")

    // Створення екземпляру фасаду UpdateFacade та виклик методу updateDevice для ініціації процесу оновлення.
    // Метод приймає параметри: поточний канал оновлення (currentChannel), поточну версію програмного забезпечення ("1.0.0"),
    // новий канал оновлення (newChannel) та нову версію програмного забезпечення ("2.0.1").
    UpdateFacade().updateDevice(
        currentChannel = UpdateChannel.RELEASE, // Поточний канал оновлення встановлено як RELEASE.
        "1.0.0", // Поточна версія програмного забезпечення.
        newChannel = UpdateChannel.DEV, // Новий канал оновлення встановлено як DEV.
        "2.0.1" // Нова версія програмного забезпечення.
    )

    // Виведення роздільної лінії у консоль після завершення тестування для кращої візуальної організації.
    println("---------------------")
}


/**
 * Функція для демонстрації роботи з декораторами.
 * Декоратор дозволяє динамічно додавати нову поведінку до об'єктів.
 */
fun testDecorator() {
    // Прапорець для умовного декорування
    val flag = true
    println("Test decorator:")

    // Створення базового пристрою
    val device = Device("Device")
    // Ініціалізація базового API для взаємодії з пристроєм
    var serialApi: SerialApi = SerialApiImpl(device)
    // Відправка даних через базове API
    serialApi.send(byteArrayOf(1, 2, 3, 4, 5))

    // Умовне декорування з додаванням функціоналу авторизації
    if (flag) {
        // Декорування serialApi для додавання функціоналу авторизації
        serialApi = SerialAuthDecorator(serialApi)
        // Відправка даних через декорований API з авторизацією
        serialApi.send(byteArrayOf(1, 2, 3, 4, 5))
        // Отримання швидкості з'єднання
        serialApi.getSpeed()
    }

    // Додаткове декорування для контролю перевищення потоку даних
    serialApi = SerialOverflowThrottlerDecorator(serialApi)
    // Отримання даних через декорований API з контролем перевищення
    serialApi.receive()
    // Відправка даних через декорований API з контролем перевищення
    serialApi.send(byteArrayOf(1, 2, 3, 4, 5))
    // Отримання швидкості з'єднання після декорування
    serialApi.getSpeed()

    println("---------------------")
}

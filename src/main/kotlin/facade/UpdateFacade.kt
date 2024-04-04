package org.example.facade

import kotlinx.coroutines.withTimeoutOrNull

// Фасадний клас для організації процесу оновлення програмного забезпечення пристрою.
// Фасад приховує складність взаємодії між різними компонентами системи оновлення.
class UpdateFacade {

    // Ініціалізація компонентів, необхідних для процесу оновлення.
    // DI (Dependency Injection) використовується для зменшення залежності між класами.
    private val compareVersion = CompareFirmwareVersion()
    private val downloadFirmware = DownloadFirmware()
    private val uploadFirmware = UploadFirmware()
    private val controlDevice = ControlDevice()

    // Основна функція для оновлення пристрою.
    // Вона координує процес перевірки версії, завантаження та встановлення прошивки.
    suspend fun updateDevice(
        currentChannel: UpdateChannel, // Поточний канал оновлення
        currentVersion: String, // Поточна версія прошивки
        newChannel: UpdateChannel, // Новий канал оновлення
        newVersion: String // Нова версія прошивки
    ) {
        // Перевірка можливості оновлення за допомогою порівняння версій.
        val compareResult = compareVersion.compare(
            currentChannel, currentVersion, newChannel, newVersion
        )
        if (compareResult) {
            println("Device will be updated") // Оновлення можливе
        } else {
            println("Device will not be updated") // Оновлення неможливе
            return
        }

        // Виконання процесу оновлення
        val firmware: ByteArray
        try {
            // Завантаження нової прошивки з вказаного каналу
            firmware = downloadFirmware.download(newChannel, newVersion)
        } catch (e: Exception) {
            // У випадку помилки при завантаженні прошивки, виводимо повідомлення та завершуємо оновлення
            println("Error while downloading firmware ${e}")
            return
        }
        println("Firmware downloaded") // Прошивка успішно завантажена

        // Встановлення оновлення на пристрій
        try {
            val result = uploadFirmware.uploadFirmware(firmware)
            if (result) {
                println("Firmware uploaded") // Прошивка успішно встановлена
            } else {
                println("Error while uploading firmware") // Помилка при встановленні прошивки
            }
        } catch (e: Exception) {
            println("Error while uploading firmware") // Помилка при встановленні прошивки
        }

        // Зміна режиму роботи пристрою після оновлення
        try {
            controlDevice.setMode(1) // Налаштування нового режиму роботи
            println("Device mode set") // Режим роботи встановлено
        } catch (e: Exception) {
            println("Error while setting device mode") // Помилка при встановленні режиму роботи
        }

        // Перезавантаження пристрою для застосування оновлень
        try {
            controlDevice.turnOff() // Вимкнення пристрою
            println("Device turned off")
        } catch (e: Exception) {
            println("Error while turning off device") // Помилка при вимкненні пристрою
        }

        // Вмикання пристрою після оновлення
        try {
            withTimeoutOrNull(5000) { // О
                controlDevice.turnOn() // Увімкнення пристрою
                println("Device turned on")
            }
        } catch (e: Exception) {
            println("Error while turning on device") // Помилка при увімкненні пристрою
        }
    }
}
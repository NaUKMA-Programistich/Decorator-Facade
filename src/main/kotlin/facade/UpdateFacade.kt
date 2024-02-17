package org.example.facade

import kotlinx.coroutines.withTimeoutOrNull

// Фасад для оновлення пристрою
class UpdateFacade {

    // DI
    private val compareVersion = CompareFirmwareVersion()
    private val downloadFirmware = DownloadFirmware()
    private val uploadFirmware = UploadFirmware()
    private val controlDevice = ControlDevice()

    suspend fun updateDevice(
        currentChannel: UpdateChannel,
        currentVersion: String,
        newChannel: UpdateChannel,
        newVersion: String
    ) {
        // Перевірка можливості оновлення
        val compareResult = compareVersion.compare(
            currentChannel, currentVersion, newChannel, newVersion
        )
        if (compareResult) {
            println("Device will be updated")
        } else {
            println("Device will not be updated")
            return
        }

        // Виконання оновлення
        val firmware: ByteArray
        try {
            firmware = downloadFirmware.download(newChannel, newVersion)
        } catch (e: Exception) {
            println("Error while downloading firmware ${e}")
            return
        }
        println("Firmware downloaded")

        // Встановлення оновлення
        try {
            val result = uploadFirmware.uploadFirmware(firmware)
            if (result) {
                println("Firmware uploaded")
            } else {
                println("Error while uploading firmware")
            }
        } catch (e: Exception) {
            println("Error while uploading firmware")
        }

        // Перемикання режиму
        try {
            controlDevice.setMode(1)
            println("Device mode set")
        } catch (e: Exception) {
            println("Error while setting device mode")
        }

        // Перезавантаження пристрою
        try {
            controlDevice.turnOff()
            println("Device turned off")
        } catch (e: Exception) {
            println("Error while turning off device")
        }

        // Вмикання пристрою
        try {
            withTimeoutOrNull(5000) {
                controlDevice.turnOn()
            }
            println("Device turned on")
        } catch (e: Exception) {
            println("Error while turning on device")
        }
    }
}
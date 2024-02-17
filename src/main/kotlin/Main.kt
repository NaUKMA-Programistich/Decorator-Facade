package org.example

import kotlinx.coroutines.runBlocking
import org.example.decorator.*
import org.example.facade.UpdateChannel
import org.example.facade.UpdateFacade

fun main() = runBlocking {
    testFacade()
    testDecorator()
}

suspend fun testFacade() {
    println("Test facade:")
    UpdateFacade().updateDevice(
        currentChannel = UpdateChannel.RELEASE, "1.0.0",
        newChannel = UpdateChannel.DEV, "2.0.1"
    )
    println("---------------------")
}

fun testDecorator() {
    println("Test decorator:")

    val device = Device("Device")
    var serialApi: SerialApi = SerialApiImpl(device)
    serialApi.send(byteArrayOf(1, 2, 3, 4, 5))

    serialApi = SerialOverflowThrottlerDecorator(serialApi)
    serialApi.receive()
    serialApi.send(byteArrayOf(1, 2, 3, 4, 5))
    serialApi.getSpeed()

    serialApi = SerialAuthDecorator(serialApi)
    serialApi.send(byteArrayOf(1, 2, 3, 4, 5))
    serialApi.getSpeed()

    println("---------------------")
}

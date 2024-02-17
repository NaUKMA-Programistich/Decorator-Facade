package org.example.facade

// Проста бізнес-логіка для порівняння версій
class CompareFirmwareVersion {
    fun compare(
        channel: UpdateChannel,
        currentVersion: String,
        newChannel: UpdateChannel,
        newVersion: String
    ): Boolean {
        if (channel == UpdateChannel.DEV) {
            return false
        }
        if (channel == UpdateChannel.RC && newChannel == UpdateChannel.RELEASE) {
            return false
        }
        if (compareVersion(currentVersion, newVersion) > 0) {
            return false
        }
        return true
    }

    private fun compareVersion(
        currentVersion: String,
        newVersion: String
    ): Int {
        val current = currentVersion.split(".").map { it.toInt() }
        val new = newVersion.split(".").map { it.toInt() }
        val max = maxOf(current.size, new.size)
        for (i in 0 until max) {
            val currentPart = current.getOrNull(i) ?: 0
            val newPart = new.getOrNull(i) ?: 0
            if (currentPart < newPart) return -1
            if (currentPart > newPart) return 1
        }
        return 0
    }

    private fun maxOf(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    private fun <T> List<T>.getOrNull(index: Int): T? {
        return if (index in indices) get(index) else null
    }
}
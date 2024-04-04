package org.example.facade

/**
 * Клас, призначений для порівняння версій прошивок.
 * Він дозволяє визначити, чи слід оновлювати поточну версію прошивки до нової версії,
 * з урахуванням каналу оновлення.
 */
class CompareFirmwareVersion {
    /**
     * Порівнює поточну версію прошивки з новою версією, враховуючи канали оновлень.
     *
     * @param channel Поточний канал оновлення.
     * @param currentVersion Строкове представлення поточної версії прошивки.
     * @param newChannel Канал оновлення нової версії.
     * @param newVersion Строкове представлення нової версії прошивки.
     * @return true, якщо оновлення до нової версії рекомендовано, інакше false.
     */
    fun compare(
        channel: UpdateChannel,
        currentVersion: String,
        newChannel: UpdateChannel,
        newVersion: String
    ): Boolean {
        // DEV-канал завжди вважається нестабільним, оновлення ігноруються.
        if (channel == UpdateChannel.DEV) {
            return false
        }

        // Оновлення з RC (Release Candidate) на RELEASE ігноруються, щоб уникнути зайвого оновлення.
        if (channel == UpdateChannel.RC && newChannel == UpdateChannel.RELEASE) {
            return false
        }

        // Використовується для порівняння версій; якщо поточна версія новіша, оновлення не потрібне.
        if (compareVersion(currentVersion, newVersion) > 0) {
            return false
        }

        return true
    }

    /**
     * Порівнює дві версії прошивок.
     *
     * @param currentVersion Строкове представлення поточної версії прошивки.
     * @param newVersion Строкове представлення нової версії прошивки.
     * @return -1, 0, або 1, якщо поточна версія відповідно менша, еквівалентна, або більша за нову версію.
     */
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

    /**
     * Повертає більше з двох значень.
     *
     * @param a Перше число для порівняння.
     * @param b Друге число для порівняння.
     * @return Більше з двох чисел.
     */
    private fun maxOf(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    /**
     * Повертає елемент зі списку за індексом, або null, якщо індекс виходить за межі.
     *
     * @param index Індекс елемента, який потрібно отримати.
     * @return Елемент за вказаним індексом
        */
    private fun <T> List<T>.getOrNull(index: Int): T? {
        return if (index in indices) get(index) else null
    }
}
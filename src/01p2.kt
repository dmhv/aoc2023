fun main() {
    val lines = readInput("01")
    lines.map { extractDigits(it) }.sumOf { "${it.first()}${it.last()}".toInt() }.println()
}

private fun extractDigits(it: String): List<String> {
    val t2d = mapOf(
        "one" to "1", "two" to "2", "three" to "3", "four" to "4",
        "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9",
    )
    val idxToDigit = mutableMapOf<Int, String>()

    // process proper digits
    for (target in t2d.values) {
        it.allIndicesOf(target).forEach { idxToDigit[it] = target }
    }

    // process digits as text
    for (key in t2d.keys) {
        it.allIndicesOf(key).forEach { idxToDigit[it] = t2d[key]!! }
    }

    return idxToDigit.toSortedMap().values.toList()
}

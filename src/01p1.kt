fun main() {
    val lines = readInput("01")
    lines.map { extractDigits(it) }.sumOf { "${it.first()}${it.last()}".toInt() }.println()
}

private fun extractDigits(it: String): List<Char> {
    return it.filter { it.isDigit() }.toList()
}

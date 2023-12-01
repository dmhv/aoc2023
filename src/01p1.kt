fun main() {
    val lines = readInput("01")
    var out = 0
    lines.forEach {
        val digits = extractDigits(it)
        val firstAndLast = "${digits.first()}${digits.last()}"
        out += firstAndLast.toInt()
    }
    println(out)
}

private fun extractDigits(it: String): List<Char> {
    return it.filter { it.isDigit() }.toList()
}
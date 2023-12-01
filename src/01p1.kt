fun main() {
    val lines = readInput("01")
    var out = 0
    lines.forEach {
        val ints = extractInts(it)
        val firstAndLast = "${ints.first()}${ints.last()}"
        out += firstAndLast.toInt()
    }
    println(out)
}

private fun extractInts(it: String): List<Char> {
    return it.filter { it.isDigit() }.toList()
}
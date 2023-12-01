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

private fun extractInts(it: String): List<String> {
    val t2d = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )
    val idxToDigit = mutableMapOf<Int, String>()

    // process proper digits
    for (target in t2d.values) {
        val indices = findAllIndices(it, target)
        indices.forEach { idxToDigit[it] = target }
    }

    // process digits as text
    for (key in t2d.keys) {
        val indices = findAllIndices(it, key)
        indices.forEach { idxToDigit[it] = t2d[key]!! }
    }

    return idxToDigit.toSortedMap().values.toList()
}

private fun findAllIndices(inp: String, target: String): List<Int> {
    val out = mutableListOf<Int>()
    var idx = inp.indexOf(target)
    while (idx >= 0) {
        out.add(idx)
        idx = inp.indexOf(target, idx+1)
    }
    return out.toList()
}

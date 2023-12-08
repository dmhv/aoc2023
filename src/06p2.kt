fun main() {
    val lines = readInput("06")
    val regexNum = Regex("(\\d+)")

    val time = regexNum.findAll(lines[0]).toList().map { it.value }.joinToString("").toLong()
    val record = regexNum.findAll(lines[1]).toList().map { it.value }.joinToString("").toLong()

    var numWaysToLoseFromLeft = 0L
    for (i in (0..time)) {
        if (i * (time - i) <= record) numWaysToLoseFromLeft += 1
        else break
    }

    (time - numWaysToLoseFromLeft * 2 + 1).println()
}

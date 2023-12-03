fun main() {
    val lines = readInput("03")

    val regexNum = Regex("(\\d+)")
    val regexAnchor = Regex("([^0-9.])")

    data class Num(val row: Int, val colStart: Int, val colEnd: Int, val value: Int)
    data class Anchor(val row: Int, val col: Int, val value: String)

    fun Num.isAdjacentTo(anchor: Anchor) =
        (anchor.row in (this.row-1)..(this.row+1)) && (anchor.col in (this.colStart-1)..(this.colEnd+1))

    fun Anchor.countAdjacent(ns: List<Num>) = ns.filter { it.isAdjacentTo(this) }.size

    // parsing
    val numbers = mutableListOf<Num>()
    val anchors = mutableListOf<Anchor>()
    for ((r, line) in lines.withIndex()) {
        val numbersFound = regexNum.findAll(line)
        numbersFound.forEach { numbers.add(Num(r, it.range.first, it.range.last, it.value.toInt())) }

        val anchorsFound = regexAnchor.findAll(line)
        anchorsFound.forEach { anchors.add(Anchor(r, it.range.first, it.value)) }
    }

    anchors
        .filter { it.value == "*" && it.countAdjacent(numbers) == 2 }
        .map {gear -> numbers.filter { it.isAdjacentTo(gear) }
        .fold(1) { acc, it -> acc * it.value }}
        .sum()
        .println()
}


import kotlin.math.pow

fun main() {
    val lines = readInput("04")
    val regexSpace = Regex("\\s+")

    data class Card(val present: Set<Int>, val winning: Set<Int>)

    fun Card.score(): Int {
        val numMatched = present.intersect(winning).size
        if (numMatched == 0) return 0
        return 2.toDouble().pow(numMatched - 1).toInt()
    }

    val cards = mutableListOf<Card>()
    for (line in lines) {
        val (winningStr, presentStr) = line.split(": ")[1].trim().split("|")
        val winningNums = regexSpace.split(winningStr.trim()).map { it.toInt() }.toSet()
        val presentNums = regexSpace.split(presentStr.trim()).map { it.toInt() }.toSet()
        cards.add(Card(presentNums, winningNums))
    }
    cards.sumOf { it.score() }.println()
}

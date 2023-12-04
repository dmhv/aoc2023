fun main() {
    val lines = readInput("04")
    val regexSpace = Regex("\\s+")

    data class Card(val present: Set<Int>, val winning: Set<Int>)

    fun Card.numMatches() = present.intersect(winning).size

    val numCopies = mutableMapOf<Int, Int>()
    for (line in lines) {
        val cardId = line.removePrefix("Card").trim().split(":")[0].toInt()
        val (winningStr, presentStr) = line.split(": ")[1].trim().split("|")
        val winningNums = regexSpace.split(winningStr.trim()).map { it.toInt() }.toSet()
        val presentNums = regexSpace.split(presentStr.trim()).map { it.toInt() }.toSet()

        val cntThisCard = numCopies.getOrDefault(cardId, 0) + 1
        numCopies[cardId] = cntThisCard

        for (i in 1..Card(presentNums, winningNums).numMatches()) {
            val otherCardId = cardId + i
            val cntOtherCard = numCopies.getOrDefault(otherCardId, 0) + cntThisCard
            numCopies[otherCardId] = cntOtherCard
        }
    }
    numCopies.values.sum().println()
}


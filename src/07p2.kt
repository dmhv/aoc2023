import java.util.SortedMap

fun main() {
    val lines = readInput("07")

    data class Card(val card: Char) {
        override fun toString(): String = card.toString()
    }

    fun Card.rankOf() : Int {
        return when (this.card) {
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'T' -> 10
            'J' -> 1
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 0
        }
    }

    val cardComparator = Comparator<Card> { c1, c2 -> c1.rankOf() - c2.rankOf() }

    data class Hand(val cards: SortedMap<Card, Int>, val bid: Int, val cardsArray: List<Card>)

    fun Hand.typeOf() : Int {
        val counts = this.cardsArray.groupingBy { it }.eachCount().values
        return when {
            5 in counts -> 6
            4 in counts -> 5
            3 in counts && 2 in counts -> 4
            3 in counts -> 3
            2 in counts && 2 in (counts - 2) -> 2
            2 in counts -> 1
            else -> 0
        }
    }

    fun Hand.rankOf() : Int {
        var offSet = 1
        var cardsScore = 0
        for ((i, c) in this.cardsArray.reversed().withIndex()) {
            if (i > 0) offSet *= 15
            cardsScore += offSet * c.rankOf()
        }
        offSet *= 15
        return this.typeOf() * offSet + cardsScore
    }

    fun Hand.replaceJokers(): Hand {
        if (!this.cards.contains(Card('J'))) return this
        if (this.cards.keys.toList() == listOf(Card('J'))) return this
        val mostOccurrences = this.cards.filter { it.key != Card('J') }.maxOf { it.value }
        val mostFrequentCard = this.cards.filter { it.value == mostOccurrences }.keys.last()

        val cardCounts = mutableMapOf<Card, Int>()
        for (c in this.cards.keys) {
            if (c == Card('J')) continue
            if (c == mostFrequentCard) {
                cardCounts[c] =
                    this.cards.getOrDefault(c, 0) +
                            this.cards.getOrDefault(Card('J'), 0)
            }
            else cardCounts[c] = this.cards.getOrDefault(c, 0)
        }
        return Hand(cardCounts.toSortedMap(cardComparator), this.bid, this.cardsArray)
    }

    val hands = mutableListOf<Hand>()
    for (line in lines) {
        val (cards, bid) = line.split(" ")
        val cardsArray = mutableListOf<Card>()
        val cardCounts = mutableMapOf<Card, Int>()
        for (c in cards) {
            val card = Card(c)
            cardsArray.add(card)
            cardCounts[card] = cardCounts.getOrDefault(card, 0) + 1
        }
        val hand = Hand(cardCounts.toSortedMap(cardComparator), bid.toInt(), cardsArray)
        val handReplaced = hand.replaceJokers()
        hands.add(handReplaced)
    }

    hands
        .sortedBy { it.rankOf() }
        .mapIndexed { i, h -> (i + 1) * h.bid }
        .sum()
        .println()
}

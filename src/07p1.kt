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
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 1
        }
    }

    val cardComparator = Comparator<Card> { c1, c2 -> c1.rankOf() - c2.rankOf() }

    data class Hand(val cards: SortedMap<Card, Int>, val bid: Int, val cardsArray: List<Card>)

    fun Hand.isFiveOfAKind() = this.cards.values.size == 1

    fun Hand.isFourOfAKind(): Boolean {
        if (this.cards.values.size != 2) return false
        return this.cards.values.maxOf { it } == 4
    }

    fun Hand.isFullHouse(): Boolean {
        if (this.cards.values.size != 2) return false
        return this.cards.values.sorted() == listOf(2, 3)
    }

    fun Hand.isThreeOfAKind(): Boolean {
        if (this.cards.values.size != 3) return false
        return this.cards.values.maxOf { it } == 3
    }

    fun Hand.isTwoPair(): Boolean {
        if (this.cards.values.size != 3) return false
        return this.cards.values.maxOf { it } == 2
    }

    fun Hand.isOnePair(): Boolean {
        if (this.cards.values.size != 4) return false
        return this.cards.values.maxOf { it } == 2
    }

    fun Hand.rankOf() : Int {
        var offSet = 1
        var cardsScore = 0
        for ((i, c) in this.cardsArray.reversed().withIndex()) {
            if (i > 0) offSet *= 15
            cardsScore += offSet * c.rankOf()
        }
        offSet *= 15

        if (this.isFiveOfAKind()) return 7 * offSet + cardsScore
        if (this.isFourOfAKind()) return 6 * offSet + cardsScore
        if (this.isFullHouse()) return 5 * offSet + cardsScore
        if (this.isThreeOfAKind()) return 4 * offSet + cardsScore
        if (this.isTwoPair()) return 3 * offSet + cardsScore
        if (this.isOnePair()) return 2 * offSet + cardsScore
        return 1 * offSet + cardsScore
    }

    val handComparator = Comparator<Hand> { h1, h2 -> h1.rankOf() - h2.rankOf() }

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
        hands.add(Hand(cardCounts.toSortedMap(cardComparator), bid.toInt(), cardsArray))
    }

    var out = 0
    for ((i, h) in hands.sortedWith(handComparator).withIndex()) {
        out += (i + 1) * h.bid
    }
    out.println()
}





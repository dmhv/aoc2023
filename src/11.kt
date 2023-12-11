import kotlin.math.abs

fun main() {
    val lines = readInput("11")
    val expansionFactor = 1000000

    data class Galaxy(val coords: List<Long>)

    fun Galaxy.distanceTo(other: Galaxy): Long {
        var out = 0L
        for (i in 0..<this.coords.size) {
            out += abs(this.coords[i] - other.coords[i])
        }
        return out
    }

    fun adjustCoords(l: List<Galaxy>, component: Int): MutableList<Galaxy> {
        val sorted = l.sortedBy { it.coords[component] }.toMutableList()
        val coords = sorted.map { it.coords[component] }.toList()
        val d = coords.zipWithNext { a, b -> b - a }.toMutableList()
        d.add(0, coords[0])
        d.replaceAll { if (it < 2) it else expansionFactor * (it - 1) + 1 }
        val adjustedCoords = d.runningFold(0) { sum: Long, num -> sum + num }.drop(1)

        val out = mutableListOf<Galaxy>()
        for ((i, adjCoord) in adjustedCoords.withIndex()) {
            val thisCoords = sorted[i].coords.toMutableList()
            thisCoords[component] = adjCoord
            out.add(Galaxy(thisCoords))
        }
        return out
    }

    var galaxies = mutableListOf<Galaxy>()
    for ((row, line) in lines.withIndex()) {
        for ((col, c) in line.withIndex()) {
            if (c == '#') galaxies.add(Galaxy(listOf(row.toLong(), col.toLong())))
        }
    }

    for (i in 0..<galaxies[0].coords.size) galaxies = adjustCoords(galaxies, i)

    var sumOfDistances = 0L
    for ((i, g1) in galaxies.withIndex()) {
        for (g2 in galaxies.drop(i + 1)) {
            sumOfDistances += g1.distanceTo(g2)
        }
    }
    sumOfDistances.println()
}

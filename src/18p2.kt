import kotlin.math.abs

fun main() {
    val lines = readInput("18")

    data class Point(val r: Long, val c: Long)

    var r = 0L
    var c = 0L
    var cntBoundaryPoints = 0L
    val points = mutableListOf<Point>()

    lines.forEach {
        val hexStr = it.split(" ").last().substring(2, 8)
        val d = hexStr.last()
        val cnt = hexStr.substring(0, 5).toLong(radix = 16)
        when (d) {
            '0' -> c += cnt
            '2' -> c -= cnt
            '1' -> r += cnt
            '3' -> r -= cnt
            else -> throw Exception("Unexpected direction $d")
        }
        points.add(Point(r, c))
        cntBoundaryPoints += cnt
    }

    val area = abs(points.zipWithNext().sumOf { (it.first.r + it.second.r) * (it.first.c - it.second.c) }) / 2
    val cntInteriorPoints = area - cntBoundaryPoints / 2 + 1
    (cntBoundaryPoints + cntInteriorPoints).println()
}
import kotlin.math.abs

fun main() {
    val lines = readInput("18")

    data class Point(val r: Int, val c: Int)

    var r = 0
    var c = 0
    var cntBoundaryPoints = 0
    val points = mutableListOf<Point>()

    lines.forEach {
        val (d, cntStr) = it.split(" ").take(2)
        val cnt = cntStr.toInt()
        when (d) {
            "R" -> c += cnt
            "L" -> c -= cnt
            "D" -> r += cnt
            "U" -> r -= cnt
            else -> throw Exception("Unexpected direction $d")
        }
        points.add(Point(r, c))
        cntBoundaryPoints += cnt
    }

    val area = abs(points.zipWithNext().sumOf { (it.first.r + it.second.r) * (it.first.c - it.second.c) }) / 2
    val cntInteriorPoints = area - cntBoundaryPoints / 2 + 1
    (cntBoundaryPoints + cntInteriorPoints).println()
}
fun main() {
    val lines = readInput("21")
    val numSteps = 64

    data class Point(val r: Int, val c: Int)

    val m = mutableMapOf<Point, Char>()
    for ((r, row) in lines.withIndex()) {
        for ((c, ch) in row.withIndex()) {
            m[Point(r, c)] = ch
        }
    }
    val mr = m.keys.maxOf { it.r }
    val mc = m.keys.maxOf { it.c }

    fun printMap(ms: Map<Point, Char>, maxRow: Int, maxCol: Int) {
        val out = StringBuilder()
        for (r in 0..maxRow) {
            for (c in 0..maxCol) {
                out.append(ms[Point(r, c)])
            }
            out.append("\n")
        }
        out.dropLast(1).toString().println()
    }
//    printMap(m, mr, mc)

    fun getNeighbours(p: Point, ms: Map<Point, Char>, maxRow: Int, maxCol: Int): List<Point> {
        val out = mutableListOf<Point>()
        if (p.r > 0 && ms[Point(p.r - 1, p.c)] == '.') out.add(Point(p.r - 1, p.c))
        if (p.c > 0 && ms[Point(p.r, p.c - 1)] == '.') out.add(Point(p.r, p.c - 1))
        if (p.r < maxRow && ms[Point(p.r + 1, p.c)] == '.') out.add(Point(p.r + 1, p.c))
        if (p.c < maxCol && ms[Point(p.r, p.c + 1)] == '.') out.add(Point(p.r, p.c + 1))
        return out
    }

    data class QueueElement(val stepNum: Int, val point: Point)

    val startingPoint = m.filter { it.value == 'S' }.keys.first()
    m[startingPoint] = '.'
    var queue = listOf(QueueElement(0, startingPoint))

    for (step in 1..numSteps) {
        val previous = queue.map { it.point }
        val options = mutableSetOf<Point>()
        for (p in previous) {
            val neighbours = getNeighbours(p, m, mr, mc)
            options.addAll(neighbours)
        }
        val next = options.map { QueueElement(step, it) }
        queue = next
    }
    queue.size.println()
}
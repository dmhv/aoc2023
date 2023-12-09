fun main() {
    val lines = readInput("09")
    val ls = lines.map { l -> l.split(" ").map { it.toInt() }}

    var out = 0
    for (l in ls) {
        val diffs = mutableListOf<List<Int>>()
        var thisLine = l
        diffs.add(thisLine)
        while (!thisLine.all { it == 0 }) {
            val nextLine = thisLine.zipWithNext { a, b -> b - a }
            diffs.add(nextLine)
            thisLine = nextLine
        }

        out += diffs.sumOf { it.last() }
    }
    out.println()
}

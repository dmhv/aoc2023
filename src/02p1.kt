fun main() {
    val lines = readInput("02")
    var out = 0
    lines.forEachIndexed { i, s ->
        val chunks = s.substringAfter(": ").split(";")
        val anyImpossible = chunks.map { isPossible(it) }.filter { !it }
        if (anyImpossible.isEmpty()) out += i+1
    }
    out.println()
}

private fun isPossible(inp: String): Boolean {
    val counts = mapOf("red" to 12, "green" to 13, "blue" to 14)
    val pairs = inp.trim().split(", ")
    pairs.forEach {
        val numAndColor = it.split(" ")
        val num = numAndColor[0].toInt()
        val color = numAndColor[1]
        if (counts.getOrDefault(color, 0) < num) return false
    }
    return true
}

fun main() {
    val lines = readInput("02")
    lines.sumOf { minSetPower(it) }.println()
}

private fun minSetPower(inp: String): Int {
    val chunks = inp.substringAfter(":").split(";")
    val counts = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
    chunks.forEach { chunk ->
        val pairs = chunk.trim().split(", ")
        pairs.forEach {
            val numAndColor = it.split(" ")
            val num = numAndColor[0].toInt()
            val color = numAndColor[1]
            if (counts.getOrDefault(color, 0) < num) counts[color] = num
        }
    }
    return counts.values.fold(1) { total, it -> total * it }
}

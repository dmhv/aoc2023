fun main() {
    val lines = readInput("15")[0].split(",")
    lines.map { s -> s.map { it.code }.fold(0) { acc, i -> (acc + i) * 17 % 256 } }.sum().println()
}

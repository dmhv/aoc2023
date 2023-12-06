fun main() {
    val lines = readInput("06")
    val regexNum = Regex("(\\d+)")

    data class Race(val time: Int, val record: Int)

    fun Race.numWaysToWin() = (1..<this.time).map { it * (this.time - it) }.count { it > this.record }

    val times = regexNum.findAll(lines[0]).toList().map { it.value.toInt() }
    val records = regexNum.findAll(lines[1]).toList().map { it.value.toInt() }
    val races = times.indices.map { Race(times[it], records[it]) }
    races.map { it.numWaysToWin() }.reduce { acc, i -> acc * i }.println()
}




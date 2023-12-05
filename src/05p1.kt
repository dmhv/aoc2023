import java.lang.Exception

fun main() {
    val lines = readInput("05")

    data class Range(val start: Long, val end: Long, val offset: Long)
    data class MapCat(val ranges: List<Range>, val from: String, val to: String)

    fun Range.convert(num: Long) = num + this.offset

    fun MapCat.convert(num: Long) : Long {
        val foundRange = ranges.filter { num in it.start..it.end }
        if (foundRange.size > 1) throw Exception("Expected to find zero or one conversion range!")
        if (foundRange.isNotEmpty()) return foundRange.first().convert(num)
        return num
    }

    val seeds = Regex("\\s+").split(lines[0].split(": ")[1]).map { it.toLong() }.toList()

    var fillingMap = false
    val mappings = mutableListOf<MapCat>()
    var ranges = mutableListOf<Range>()
    var fromCat = ""
    var toCat = ""

    for (line in lines.drop(2)) {
        if (!fillingMap) {
            ranges = mutableListOf()
            val categories = line.split(" ")[0].split("-to-")
            fromCat = categories[0].also { toCat = categories[1] }
            fillingMap = true
            continue
        }
        if (line.isBlank()) {
            mappings.add(MapCat(ranges, fromCat, toCat))
            fillingMap = false
            continue
        }
        val (destRangeStart, sourceRangeStart, length) = Regex("\\s+").split(line).map { it.toLong() }.toList()
        val sourceRangeEnd = sourceRangeStart + length - 1
        val offset = destRangeStart - sourceRangeStart
        ranges.add(Range(sourceRangeStart, sourceRangeEnd, offset))
    }
    mappings.add(MapCat(ranges, fromCat, toCat))

    var minLocation = Long.MAX_VALUE
    for (seed in seeds) {
        var currentCategory = "seed"
        var currentValue = seed
        while (currentCategory != "location") {
            val mapping = mappings.first { it.from == currentCategory }
            currentValue = mapping.convert(currentValue)
            currentCategory = mapping.to
        }
        if (currentValue < minLocation) minLocation = currentValue
    }
    minLocation.println()
}




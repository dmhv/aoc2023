fun main() {
    val lines = readInput("12")

    fun generateOptions(n: Int, m: Int): List<List<Int>> {
        if (m == 0) {
            return listOf(buildList {
                (1..n).forEach { _ -> add(1) }
            })
        }
        if (n == m) {
            return listOf(buildList {
                (1..n).forEach { _ -> add(-1) }
            })
        }
        if (n == 1) {
            return listOf(listOf(-1), listOf(1))
        }
        val out = mutableListOf<List<Int>>()
        var first: Int
        var next: List<List<Int>>

        if (n > m) {
            first = 1
            next = generateOptions(n - 1, m)
            next.forEach {
                out.add(buildList {
                    add(first)
                    addAll(it)
                })
            }
        }

        first = -1
        next = generateOptions(n - 1, m - 1)
        next.forEach {
            out.add(buildList {
                add(first)
                addAll(it)
            })
        }

        return out
    }

    fun mapCharToInt(c: Char) = when (c) {
        '.' -> 1
        '#' -> -1
        else -> 0
    }

    fun isValid(vs: List<Int>, groupCounts: List<Int>): Boolean {
        var thisGroupSize = 0
        var groupNum = 0
        for (v in vs) {
            if (v != -1) {
                if (thisGroupSize > 0) {
                    if (groupNum >= groupCounts.size) return false
                    if (thisGroupSize != groupCounts[groupNum]) return false
                    groupNum += 1
                    thisGroupSize = 0
                }
            } else {
                thisGroupSize += 1
            }
        }
        if (thisGroupSize > 0) {
            if (groupNum >= groupCounts.size) return false
            if (thisGroupSize != groupCounts[groupNum]) return false
            groupNum += 1
        }
        return groupNum == groupCounts.size
    }

    data class Record(val vs: List<Int>, val cntUnknown: Int, val groups: List<Int>, val targetDamaged: Int)

    val records = mutableListOf<Record>()
    for (line in lines) {
        val (vs, groups) = line.split(" ")
        val vsParsed = vs.map {mapCharToInt(it)}
        val groupsParsed = groups.split(",").map { it.toInt() }.toList()
        val cntUnknown = vsParsed.count { it == 0 }
        val targetDamaged = groupsParsed.sum()
        records.add(Record(vsParsed, cntUnknown, groupsParsed, targetDamaged))
    }

    var cntOptions = 0
    for (r in records) {
        var thisRecordOptions = 0
        val optionsDamaged = r.vs.count {it == 0}
        if (optionsDamaged <= 0) {
            cntOptions++
            continue
        }
        val existingDamaged = r.vs.count { it == -1 }
        val remainingDamaged = r.targetDamaged - existingDamaged
        val options = generateOptions(optionsDamaged, remainingDamaged)

        val filteredOptions = options.filter { o ->  o.count { it == -1 } + existingDamaged == r.targetDamaged }
        for (option in filteredOptions) {
            val toCheck = mutableListOf<Int>()
            var j = 0
            for (el in r.vs) {
                if (el != 0) toCheck.add(el)
                else {
                    toCheck.add(option[j])
                    j++
                }
            }
            if (isValid(toCheck, r.groups)) {
                thisRecordOptions += 1
            }
        }
        cntOptions += thisRecordOptions
    }
    cntOptions.println()
}

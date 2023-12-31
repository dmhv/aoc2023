import java.lang.Exception

fun main() {
    val lines = readInput("19")

    data class Rule(val label: String, val threshold: Int, val op: String, val res: String)

    data class Interval(val start: Int, val end: Int)  // [start, end)

    fun Interval.contains(v: Int) = v >= this.start && v < this.end

    fun Interval.length() = (this.end - this.start).toLong()

    data class Cube(val vs: Map<String, Interval>) {
        override fun toString(): String {
            val out = StringBuilder()
            for (k in this.vs.keys) {
                val thisInterval = this.vs[k]!!
                out.append("$k: ${thisInterval.start}..${thisInterval.end - 1} ")
            }
            return out.dropLast(1).toString()
        }
    }

    fun Cube.volume() = this.vs.values.map { it.length() }.reduce { acc, i -> acc * i }

    data class SplitResult(val satisfied: Cube?, val notSatisfied: Cube?)

    fun Cube.split(key: String, value: Int, op: String): SplitResult {
        val thisInterval = this.vs[key]!!
        if (!thisInterval.contains(value)) {
            return if (op == "<") {
                if (thisInterval.end <= value) {
                    SplitResult(satisfied = this, notSatisfied = null)
                } else {
                    SplitResult(satisfied = null, notSatisfied = this)
                }
            } else {
                if (value < thisInterval.start) {
                    SplitResult(satisfied = this, notSatisfied = null)
                } else {
                    SplitResult(satisfied = null, notSatisfied = this)
                }
            }
        }

        val delta = if (op == "<") 0 else 1
        val left = Interval(thisInterval.start, value + delta)
        val right = Interval(value + delta, thisInterval.end)
        val leftVs = this.vs.toMutableMap()
        leftVs[key] = left
        val rightVs = this.vs.toMutableMap()
        rightVs[key] = right

        return if (op == "<") {
            SplitResult(satisfied = Cube(leftVs), notSatisfied = Cube(rightVs))
        } else {
            SplitResult(satisfied = Cube(rightVs), notSatisfied = Cube(leftVs))
        }
    }

    data class Workflow(val label: String, val rules: List<Rule>, val default: String)

    val workflows = mutableMapOf<String, Workflow>()

    for (line in lines) {
        if (line.isBlank()) break

        val (label, rest) = line.dropLast(1).split("{")
        val rulesString = rest.split(",")
        val default = rulesString.takeLast(1)[0]
        val rules = mutableListOf<Rule>()
        for (r in rulesString.dropLast(1)) {
            var ruleOp = ""
            if (r.contains(">")) ruleOp = ">"
            if (r.contains("<")) ruleOp = "<"
            if (ruleOp == "") throw Exception("Unknown rule in $r")

            val (ruleLabel, other) = r.split(Regex("[><]"))
            val (ruleThreshold, ruleRes) = other.split(":")
            val rule = Rule(
                label = ruleLabel,
                threshold = ruleThreshold.toInt(),
                op = ruleOp,
                res = ruleRes
            )
            rules.add(rule)
        }
        workflows[label] = Workflow(label = label, rules = rules, default = default)
    }

    val startCube = Cube(buildMap {
        // end is not included
        set("x", Interval(1, 4001))
        set("m", Interval(1, 4001))
        set("a", Interval(1, 4001))
        set("s", Interval(1, 4001))
    })

    // first element - workflow label
    val toProcess = mutableListOf<Pair<String, Cube>>()
    toProcess.add(Pair("in", startCube))
    val accepted = mutableListOf<Cube>()

    while (toProcess.isNotEmpty()) {
        val (label, cube) = toProcess.removeFirst()
        val wf = workflows[label]!!
        // first element - index of the rule that should be applied
        val thisToProcess = mutableListOf(Pair(0, cube))

        while (thisToProcess.isNotEmpty()) {
            val (ruleIdx, thisCube) = thisToProcess.removeFirst()
            val rule = wf.rules[ruleIdx]

            val splitRes = thisCube.split(
                key = rule.label,
                value = rule.threshold,
                op = rule.op
            )

            if (splitRes.satisfied != null) {
                if (rule.res !in listOf("A", "R")) {
                    // not done yet, move it to the next workflow
                    toProcess.add(Pair(rule.res, splitRes.satisfied))
                } else if (rule.res == "A") {
                    // done, we only care about those that end in "A"
                    accepted.add(splitRes.satisfied)
                }
            }
            if (splitRes.notSatisfied != null) {
                if (ruleIdx < wf.rules.size - 1) {
                    // not done, and there are more rules in this workflow
                    thisToProcess.add(Pair(ruleIdx + 1, splitRes.notSatisfied))
                } else {
                    if (wf.default !in listOf("A", "R")) {
                        // not done, and there are no rules in this workflow, so move to next
                        toProcess.add(Pair(wf.default, splitRes.notSatisfied))
                    } else if (wf.default == "A") {
                        // done
                        accepted.add(splitRes.notSatisfied)
                    }
                }
            }
        }
    }
    accepted.sumOf { it.volume() }.println()
}

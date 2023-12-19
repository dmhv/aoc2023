import java.lang.Exception

fun main() {
    val lines = readInput("19")

    data class Rule(val label: String, val threshold: Int, val op: String, val res: String)

    data class Part(val vs: Map<String, Int>)

    fun Rule.evaluate(part: Part): String? {
        return when (op) {
            ">" -> if (part.vs[label]!! > threshold) res else null
            "<" -> if (part.vs[label]!! < threshold) res else null
            else -> throw Exception("Operation $op not implemented")
        }
    }

    data class Workflow(val label: String, val rules: List<Rule>, val default: String)

    fun Workflow.evaluate(part: Part): String {
        return rules.firstNotNullOfOrNull { it.evaluate(part) } ?: this.default
    }

    val workflows = mutableMapOf<String, Workflow>()
    val parts = mutableListOf<Part>()
    var isParsingWorkflows = true

    for (line in lines) {
        if (line.isBlank()) {
            isParsingWorkflows = false
            continue
        }

        if (isParsingWorkflows) {
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

        if (!isParsingWorkflows) {
            val kvs = line.drop(1).dropLast(1).split(",")
            val vs = buildMap {
                kvs.forEach {
                    val (k, v) = it.split("=")
                    set(k, v.toInt())
                }
            }
            parts.add(Part(vs))
        }
    }

    val acceptedParts = mutableListOf<Part>()
    for (part in parts) {
        var step = "in"
        while (step !in listOf("A", "R")) {
            step = workflows[step]!!.evaluate(part)
        }
        if (step == "A") acceptedParts.add(part)
    }

    acceptedParts.sumOf { it.vs.values.reduce { acc, i -> acc + i } }.println()
}
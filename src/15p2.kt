import kotlin.streams.toList

fun main() {
    val lines = readInput("15")[0].split(",")

    data class Lens(val label: String, var strength: Int)

    fun hash(s: String) = s.chars().toList().fold(0) { acc, i -> (acc + i) * 17 % 256 }

    val boxToLenses = buildMap<Int, MutableList<Lens>> { (0..255).forEach { put(it, mutableListOf()) } }.toMutableMap()

    for (line in lines) {
        if (line.contains('=')) {
            val (label, strength) = line.split("=")
            val hashedLabel = hash(label)
            if (boxToLenses[hashedLabel]!!.any { it.label == label} ) {
                val foundLens = boxToLenses[hashedLabel]!!.first { it.label == label }
                foundLens.strength = strength.toInt()
            } else {
                boxToLenses[hashedLabel]!!.add(Lens(label, strength.toInt()))
            }
        }
        else {
            val label = line.split("-")[0]
            val hashedLabel = hash(label)
            boxToLenses[hashedLabel] = boxToLenses[hashedLabel]!!.filter { it.label != label }.toMutableList()
        }
    }

    var out = 0
    for ((boxNum, lenses) in boxToLenses.values.withIndex()) {
        for ((lensNum, lens) in lenses.withIndex()) {
            out += (boxNum + 1) * (lensNum + 1) * lens.strength
        }
    }
    out.println()
}

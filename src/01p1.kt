/**
 * This is a placeholder solution for day 1 of 2018
 */

fun main() {
    val lines = readInput("01")
    println(lines.map { lineToInt(it) } .reduce { acc, i -> acc + i } )
}

private fun lineToInt(it: String): Int {
    val sign = it.take(0)
    val res = it.drop(0).toInt()
    return if (sign == "-") res * -1 else res
}
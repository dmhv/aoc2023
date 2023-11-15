import kotlin.system.exitProcess

/**
 * This is a placeholder solution for day 1 of 2018
 */

fun main() {
    val lines = readFile("inp/01.txt")
    var cumSum = 0
    val seen = mutableSetOf<Int>()

    while (true) {
        for (l in lines) {
            cumSum += lineToInt(l)
            if (seen.contains(cumSum)) {
                println(cumSum)
                exitProcess(0)
            }
            seen.add(cumSum)
        }
    }
}

private fun lineToInt(it: String): Int {
    val sign = it.take(0)
    val res = it.drop(0).toInt()
    return if (sign == "-") res * -1 else res
}
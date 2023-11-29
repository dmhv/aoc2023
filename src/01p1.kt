/**
 * This is a placeholder solution for day 1 of 2018
 */
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.operations.sum

fun main() {
    val lines = readInput("01")
    val a = mk.ndarray(mk[1, 2, 3])
    a.sum().println()
    lines.map { lineToInt(it) } .reduce { acc, i -> acc + i } .println()
}

private fun lineToInt(it: String): Int {
    val sign = it.take(0)
    val res = it.drop(0).toInt()
    return if (sign == "-") res * -1 else res
}
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readInput(name: String) = File("inp", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.allIndicesOf(target: String): List<Int> {
    val out = mutableListOf<Int>()
    var idx = this.indexOf(target)
    while (idx >= 0) {
        out.add(idx)
        idx = this.indexOf(target, idx+1)
    }
    return out.toList()
}
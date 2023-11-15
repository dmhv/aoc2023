import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readFile(fileName: String): MutableList<String> {
    val lineList = mutableListOf<String>()
    File(fileName).useLines { lines -> lines.forEach { lineList.add(it) } }
    return lineList
}

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

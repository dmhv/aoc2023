import kotlin.math.max

fun main() {
    val lines = readInput("08")

    data class Node(val loc: String, val left: String, val right: String)

    val moves = lines[0].trim()
    val nodes = mutableMapOf<String, Node>()
    lines.drop(2).forEach {
        val (thisNode, leftRight) = it.split(" = ")
        val (left, right) = leftRight.drop(1).dropLast(1).split(", ")
        nodes[thisNode] = Node(thisNode, left, right)
    }

    var i = 0
    var thisNodes = nodes.filter { it.value.loc.endsWith('A') }.values
    val nodeToLoopLength = mutableMapOf<Node, Int>()

    while (true) {
        thisNodes.filter { it.loc.endsWith('Z') }.forEach { nodeToLoopLength[it] = i }
        if (nodeToLoopLength.size == thisNodes.size) break

        val direction = moves[i % moves.length]
        thisNodes = thisNodes.map { if (direction == 'L') nodes[it.left]!! else nodes[it.right]!! }
        i++
    }

    fun gcd(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return max(a, b)
        return if (a > b) gcd(a % b, b) else gcd(a, b % a)
    }

    fun lcm(a: Long, b: Long) = a * b / gcd(a, b)

    nodeToLoopLength.values.map { it.toLong() }.reduce { acc, l -> lcm(acc, l) }.println()
}

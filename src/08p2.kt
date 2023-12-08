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

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    nodeToLoopLength.values.map { it.toLong() }.reduce { acc, l -> findLCM(acc, l) }.println()
}





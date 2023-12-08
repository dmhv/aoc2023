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
    var thisNode = nodes["AAA"]
    val targetNode = nodes["ZZZ"]
    while (true) {
        if (thisNode == targetNode) {
            i.println()
            break
        }
        val direction = moves[i % moves.length]
        thisNode = if (direction == 'L') nodes[thisNode!!.left] else nodes[thisNode!!.right]
        i++
    }
}

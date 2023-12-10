fun main() {
    val lines = readInput("10")

    data class Tile(val r: Int, val c: Int)

    val grid = mutableMapOf<Tile, String>()
    for ((r, line) in lines.withIndex()) {
        for ((c, char) in line.withIndex()) grid[Tile(r, c)] = char.toString()
    }

    val start = grid.filter { it.value == "S" }.keys.first()
    val maxRow = grid.keys.maxOf { it.r }
    val maxCol = grid.keys.maxOf { it.c }

    fun Tile.getNeighboursOf(): List<Tile> {
        val out = mutableListOf<Tile>()
        val v = grid.getOrDefault(this, "err")
        if (v == "err") throw Exception("$this not found in the grid!")

        if (this.r > 0 && v in "S|LJ" && grid[Tile(r - 1, c)]!! in "|7F") out.add(Tile(r - 1, c))
        if (this.r < maxRow && v in "S|7F" && grid[Tile(r + 1, c)]!! in "|LJ") out.add(Tile(r + 1, c))
        if (this.c > 0 && v in "S-7J" && grid[Tile(r, c - 1)]!! in "-LF") out.add(Tile(r , c - 1))
        if (this.c < maxCol && v in "S-LF" && grid[Tile(r, c + 1)]!! in "-7J") out.add(Tile(r, c + 1))

        return out.filter { grid[it] != "." }
    }

    val distanceToTile = mutableMapOf<Tile, Int>()
    distanceToTile[start] = 0

    val toVisit = mutableListOf<Tile>()
    val previous = mutableMapOf<Tile, Tile>()
    start.getNeighboursOf().forEach {
        toVisit.add(it)
        previous[it] = start
    }

    while (toVisit.isNotEmpty()) {
        val thisTile = toVisit.removeAt(0)
        if (distanceToTile.contains(thisTile)) continue

        val previousTile = previous[thisTile]!!
        distanceToTile[thisTile] = distanceToTile[previousTile]!! + 1
        thisTile.getNeighboursOf().forEach {
            toVisit.add(it)
            previous[it] = thisTile
        }
    }

    grid[start] = "|" // I know, could've figured it out based on neighbours
    val enclosedTiles = mutableListOf<Tile>()
    for (r in 0..maxRow) {
        for (c in 0..maxCol) {
            val thisTile = Tile(r, c)
            if (!distanceToTile.containsKey(thisTile)) {
                val fromLeft = distanceToTile.filter { it.key.c < c && it.key.r == r && grid[it.key]!! != "-" }
                var cntWall = fromLeft.count { grid[it.key]!! == "|" }
                var nL = fromLeft.count { grid[it.key]!! == "L" }
                var nF = fromLeft.count { grid[it.key]!! == "F" }
                var n7 = fromLeft.count { grid[it.key]!! == "7" }
                var nJ = fromLeft.count { grid[it.key]!! == "J" }

                while (nL > 0 && n7 > 0) nL--.also { n7-- }.also { cntWall++ }
                while (nF > 0 && nJ > 0) nF--.also { nJ-- }.also { cntWall++ }

                if (cntWall % 2 == 1) enclosedTiles.add(thisTile)
            }
        }
    }
    enclosedTiles.size.println()
}

object Day17 {
    // val targetArea = 20..30 to -10..-5
    val targetArea = 248..285 to -85..-56

    data class Coord(val x: Int, val y: Int) {
        constructor(coord: Pair<Int, Int>) : this(coord.first, coord.second)
    }

    private fun Pair<IntRange, IntRange>.toCoords(): List<Coord> {
        val (xs, ys) = this
        return xs.flatMap { row -> ys.map { col -> Coord(row, col) } }
    }

    private fun Pair<IntRange, IntRange>.draw(path: List<Coord>) {
        val targetArea = this.toCoords()
        println("\n")
        val maxCols = (path + targetArea).maxOf { it.x } + 2
        val maxRows = (path + targetArea).maxOf { it.y } + 2
        val minRows = (path + targetArea).minOf { it.y } - 2

        (minRows..maxRows).reversed().forEach { row ->
            print(row.toString().padStart(3, ' ') + "  ")
            (0..maxCols).forEach { col ->
                print(
                    when {
                        path.contains(Coord(col, row)) -> "#"
                        targetArea.contains(Coord(col, row)) -> "T"
                        else -> "."
                    }
                )
            }
            println()
        }
    }

    private fun Pair<IntRange, IntRange>.outOfBounds(point: Coord): Boolean {
        val (xs, ys) = this
        val (x, y) = point
        return x > xs.last || y < ys.first
    }

    fun Pair<IntRange, IntRange>.within(point: Coord): Boolean {
        val (xs, ys) = this
        val (x, y) = point
        return xs.contains(x) && ys.contains(y)
    }

    private fun fire(x: Int, y: Int): List<Coord> {
        val velocity = generateSequence(x to y) { (x, y) ->
            (when {
                x > 0 -> x - 1
                x < 0 -> x + 1
                else -> 0
            }) to y - 1
        }.iterator()

        return generateSequence(Coord(0 to 0)) { (x, y) ->
            val (x2, y2) = velocity.next()
            Coord((x + x2) to (y + y2))
        }
            .takeWhile { targetArea.within(it) || !targetArea.outOfBounds(it) }
            .toList()
    }

    object Part1 {
        fun run() {
            val paths = (0..100).flatMap { x ->
                (0..100).map { y ->
                    (x to y) to fire(x, y)
                }
            }
                .filter { (_, path) -> targetArea.within(path.last()) }
                .sortedBy { (_, path) -> path.maxByOrNull { it.y }!!.y }

            println(paths.maxOf { (_, path) -> path.maxOf{ it.y } })

            targetArea.draw(fire(23, 84))
        }
    }

    object Part2 {
        fun run() {
            val paths = (20..300).flatMap { x ->
                (-100..100).map { y ->
                    (x to y) to fire(x, y)
                }
            }
                .filter { (_, path) -> targetArea.within(path.last()) }
                .map {(velocity, _) ->  velocity}

            println(paths.size)
        }
    }
}

fun main() {
    Day17.Part1.run()
    Day17.Part2.run()
}
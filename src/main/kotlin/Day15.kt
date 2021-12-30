import java.io.File
import java.nio.charset.Charset.defaultCharset
import kotlin.streams.toList

typealias ChitonGrid = Map<Pair<Int, Int>, Day15.Square>

object Day15 {
    private fun toCoordinates(row: Int, line: String) = line.chars()
        .map(Character::getNumericValue)
        .toList()
        .mapIndexed { col, value -> (col to row) to Square((col to row), value) }

    private fun draw(coordinates: Collection<Square>) {
        println("\n")
        val maxCols = coordinates.maxOf { it.coord.first }
        val maxRows = coordinates.maxOf { it.coord.second }

        (0..maxRows).forEach { row ->
            (0..maxCols).forEach { col ->
                print(coordinates.find { it.coord == Pair(col, row) }?.let { it.risk } ?: ".")
            }
            println()
        }
    }

    data class Square(
        var coord: Pair<Int, Int>,
        var risk: Int,
        var distance: Int = Int.MAX_VALUE,
        var parent: Square? = null
    ): Comparable<Square> {
        fun getNeighbours(
            validCells: ChitonGrid,
            remaining: Collection<Square>,
            deltas: List<Pair<Int, Int>> = listOf(
                -1 to 0, 0 to -1,
                1 to 0, 0 to 1,
            )
        ) = deltas.asSequence()
            .map { (x2, y2) -> coord.first + x2 to coord.second + y2 }
            .mapNotNull { validCells[it] }
            .filter { remaining.contains(it) }


        override fun compareTo(other: Square): Int {
            return this.distance.compareTo(other.distance)
        }
    }

    object Part1 {
        val chitonGrid = {
            File("day15/input-real.txt")
                .readLines(defaultCharset())
                .mapIndexed(::toCoordinates)
                .flatten()
                .associate { it }
        }

        fun run() {
            val grid = chitonGrid()
            grid.values.first().distance = 0

            val queue = grid.values.toMutableList()

            while (queue.isNotEmpty()) {
                val next = queue.minOrNull()!!
                queue.remove(next)

                val neighbours = next.getNeighbours(grid, queue)
                neighbours.forEach {
                    if (next.distance + next.risk < it.distance) {
                        it.distance = next.distance + next.risk
                        it.parent = next
                    }
                }
            }

            var end = grid.values.last()
            val parents = mutableListOf(end)
            while(end.parent != null) {
                end = end.parent!!
                parents.add(end)
            }
            draw(parents)
            parents.reversed().forEach { println("${it.coord} ${it.risk}   ${it.distance}")  }
            parents.sumOf { it.risk }.also { println(it - grid.values.first().risk) }
        }
    }

    object Part2 {
        private fun toCoordinates(row: Int, line: List<Int>) = line
            .mapIndexed { col, value -> (col to row) to Square((col to row), value) }

        fun run() {
            val grid = File("day15/input-real.txt")
                .readLines(defaultCharset())
                .map { it.map (Character::getNumericValue) }
                .map { vals -> (0..4).flatMap{ i -> vals.map { round(it, i) }}}.let{
                    grid -> (0..4).flatMap{ i -> grid.map { row -> row.map { round(it, i) } }}
                }.mapIndexed { i, it -> toCoordinates(i, it) }
                .flatten()
                .associate { it }

            grid.values.first().distance = 0

            val queue = grid.values.toMutableList()
            val total = queue.size
            var count = 0

            while (queue.isNotEmpty()) {
                if (count++ % 500 == 0) {
                    println("$count/$total (${queue.size})")
                }
                val next = queue.minOrNull()!!
                queue.remove(next)

                val neighbours = next.getNeighbours(grid, queue)
                neighbours.forEach {
                    if (next.distance + next.risk < it.distance) {
                        it.distance = next.distance + next.risk
                        it.parent = next
                    }
                }
            }

            var end = grid.values.last()
            val parents = mutableListOf(end)
            while(end.parent != null) {
                end = end.parent!!
                parents.add(end)
            }
            draw(parents)
            parents.reversed().forEach { println("${it.coord} ${it.risk}   ${it.distance}")  }
            parents.sumOf { it.risk }.also { println(it - grid.values.first().risk) }
        }
    }

    private fun round(it: Int, i: Int): Int {
        val x = it + i
        val y = x - 9
        return if (y > 0) y else x
    }
}

fun main() {
    Day15.Part1.run()
    Day15.Part2.run() // 21 mins!
}
import java.io.File
import java.nio.charset.Charset.defaultCharset
import kotlin.streams.toList

typealias OctopusGrid = Map<Pair<Int, Int>, Day11.Octopus>

object Day11 {
    private fun toCoordinates(row: Int, line: String) = line.chars()
        .map(Character::getNumericValue)
        .toList()
        .mapIndexed { col, value -> (row to col) to Octopus((row to col), value) }

    val octopusGrid = {
        File("day11/input-Day11.kt.txt")
            .readLines(defaultCharset())
            .mapIndexed(::toCoordinates)
            .flatten()
            .associate { it }
    }

    data class Octopus(var coord: Pair<Int, Int>, var score: Int, var hasFlashed: Boolean = false) {
        fun increment() = score++
        private fun needsToFlash() = score > 9 && !hasFlashed

        private fun flash() {
            hasFlashed = true
        }

        fun reset() = if (hasFlashed) {
            hasFlashed = false; score = 0; 1
        } else 0

        private fun getNeighbours(
            validCells: OctopusGrid,
            deltas: List<Pair<Int, Int>> = listOf(
                -1 to 0, 0 to -1,
                1 to 0, 0 to 1,
                -1 to -1, 1 to 1,
                -1 to 1, 1 to -1
            )
        ) = deltas.asSequence()
            .map { (x2, y2) -> coord.first + x2 to coord.second + y2 }
            .mapNotNull { validCells[it] }

        fun check(grid: OctopusGrid) {
            if (this.needsToFlash()) {
                this.flash()
                this.getNeighbours(grid).forEach { it.increment(); it.check(grid) }
            }
        }
    }

    private fun OctopusGrid.step(): Int {
        this.values.forEach { it.increment() }
        this.values.forEach { oct -> oct.check(this) }
        return this.values.sumOf { it.reset() }
    }

    object Part1 {
        fun run() {
            val grid = octopusGrid()
            val count = (0..99).map { grid.step() }.sum()
            println(count)
        }
    }

    object Part2 {
        private fun OctopusGrid.isNotComplete(): Boolean {
            val flashCount = this.step()
            return flashCount != this.size
        }

        fun run() {
            val grid = octopusGrid()
            var count = 0
            do {
                count++
            } while (grid.isNotComplete())

            println(count)
        }
    }
}

fun main() {
    Day11.Part1.run()
    Day11.Part2.run()
}
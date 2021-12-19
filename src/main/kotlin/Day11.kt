import java.io.File
import java.nio.charset.Charset.defaultCharset
import kotlin.streams.toList

object Day11 {
    object Part1 {
        fun toCoordinates(row: Int, line: String) = line.chars()
            .map(Character::getNumericValue)
            .toList()
            .mapIndexed { col, value -> (row to col) to value }

        fun getSurroundingCoordinates(
            validCells: ValidCells,
            coord: Coordinate,
            deltas: List<Pair<Int, Int>> = listOf(
                -1 to 0,  0 to -1,
                1 to 0, 0 to 1,
                -1 to -1, 1 to 1,
                -1 to 1, 1 to -1
                )
        ) = deltas.asSequence()
            .map { (x2, y2) -> coord.first + x2 to coord.second + y2 }
            .filter { validCells.contains(it) }

        fun run() {
            File("day11/input-test.txt")
                .readLines(defaultCharset())
                .mapIndexed(::toCoordinates)
                .flatten()
                .also { println(it) }
        }
    }
}

fun main() {
    Day11.Part1.run()
}
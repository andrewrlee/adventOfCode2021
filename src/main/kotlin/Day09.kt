import java.io.File
import java.nio.charset.Charset.defaultCharset
import kotlin.streams.toList

typealias Coordinate = Pair<Int, Int>
typealias Basin = Set<Coordinate>
typealias ValidCells = Set<Coordinate>

object Day09 {
    fun toCoordinates(row: Int, line: String) = line.chars()
        .map(Character::getNumericValue)
        .toList()
        .mapIndexed { col, value -> (row to col) to value }

    fun getSurroundingCoordinates(
        validCells: ValidCells,
        coord: Coordinate,
        seenCoordinates: HashSet<Coordinate>,
        deltas: List<Pair<Int, Int>> = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
    ) = deltas.asSequence()
        .map { (x2, y2) -> coord.first + x2 to coord.second + y2 }
        .filter { validCells.contains(it) }
        .filter { !seenCoordinates.contains(it) }

    fun findBasin(validCells: ValidCells, coordinate: Coordinate, seenCoordinates: HashSet<Coordinate>): Basin? {
        if (seenCoordinates.contains(coordinate)) return null
        seenCoordinates.add(coordinate)
        val basin = mutableSetOf(coordinate)

        getSurroundingCoordinates(validCells, coordinate, seenCoordinates)
            .filter { !seenCoordinates.contains(it) }
            .map { findBasin(validCells, it, seenCoordinates) }
            .filterNotNull()
            .forEach { basin.addAll(it) }

        return basin
    }

    fun run() {
        val file = File("day09/input-real.txt")

        val cells = file.readLines(defaultCharset())
            .mapIndexed(::toCoordinates)
            .flatten()

        val (highs, unseen) = cells.partition { it.second == 9 }
        val validCells = cells.map { it.first }.toSet()

        val seenCoordinates = highs.map { it.first }.toHashSet()
        val toCheck = unseen.map { it.first }

        val basins = toCheck.fold(mutableListOf<Basin>()) { basins, coordinate ->
            findBasin(validCells, coordinate, seenCoordinates)?.let { basins.add(it) }
            basins
        }

        val result = basins.map { it.size }.sortedDescending().take(3).reduce(Int::times)
        println(result)
    }
}

fun main() {
    Day09.run()
}
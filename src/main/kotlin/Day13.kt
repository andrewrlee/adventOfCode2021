import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

object Day13 {

    data class Coord(val x: Int, val y: Int) {
        constructor(coord: Pair<Int, Int>) : this(coord.first, coord.second)
    }

    enum class Direction {
        x {
            override fun extract(c: Coord) = c.x
            override fun adjust(c: Coord, value: Int) = Coord((c.x - (c.x - value) * 2) to c.y)
        },
        y {
            override fun extract(c: Coord) = c.y
            override fun adjust(c: Coord, value: Int) = Coord(c.x to (c.y - (c.y - value) * 2))
        };

        abstract fun extract(coord: Coord): Int
        abstract fun adjust(coord: Coord, value: Int): Coord
    }

    private fun toCoordinates(line: String) = line
        .split(",".toRegex())
        .map(Integer::parseInt)
        .let { Coord(it[0] to it[1]) }

    private fun toInstructions(line: String): Pair<Direction, Int> {
        val (axis, value) = "fold along (.)=(.+)".toRegex().find(line)!!.destructured
        return Direction.valueOf(axis) to Integer.parseInt(value)
    }

    private fun fold(instruction: Pair<Direction, Int>, coordinates: Collection<Coord>): Set<Coord> {
        val (top, bottom) = coordinates.partition { instruction.first.extract(it) > instruction.second }
        val adjusted = top.map { instruction.first.adjust(it, instruction.second) }
        return bottom.toMutableSet() + adjusted
    }

    private fun draw(coordinates: Collection<Coord>) {
        println("\n")
        println("Contains: ${coordinates.size}")
        val maxCols = coordinates.maxOf { it.x }
        val maxRows = coordinates.maxOf { it.y }

        (0..maxRows).forEach { row ->
            (0..maxCols).forEach { col ->
                print(if (coordinates.contains(Coord(col, row))) "#" else ".")
            }
            println()
        }
    }

    object Part1 {

        fun run() {
            val (instr, coords) = File("day13/input-real.txt")
                .readLines(UTF_8)
                .partition { it.startsWith("fold along") }

            val coordinates = coords.filter { it.isNotBlank() }.map(::toCoordinates)
            val instructions = instr.map(::toInstructions)
            val first = fold(instructions.first(), coordinates)

            println(first.size)
        }
    }

    object Part2 {

        fun run() {
            val (instr, coords) = File("day13/input-real.txt")
                .readLines(UTF_8)
                .partition { it.startsWith("fold along") }

            val coordinates: Collection<Coord> = coords.filter { it.isNotBlank() }.map(::toCoordinates)
            val instructions = instr.map(::toInstructions)

            val result = instructions.fold(coordinates) { acc, i  -> fold(i, acc) }

            draw(result)
            // L R G P R E C B
        }
    }
}

fun main() {
    Day13.Part1.run()
    Day13.Part2.run()
}

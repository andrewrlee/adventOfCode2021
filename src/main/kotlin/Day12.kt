import java.io.File
import java.nio.charset.Charset.defaultCharset

@OptIn(ExperimentalStdlibApi::class)
object Day12 {

    val maze = File("day12/input-real.txt")
        .readLines(defaultCharset())
        .flatMap { row -> row.split("-").let { listOf(it[0] to it[1], it[1] to it[0]) } }
        .groupBy({ it.first }, { it.second })

    object Part1 {
        fun run() {
            val paths = mutableSetOf<List<String>>()
            countPaths(maze, paths, listOf("start"))
            paths.forEach { println(it) }
            println(paths.size)
        }

        private fun countPaths(
            maze: Map<String, List<String>>,
            paths: MutableSet<List<String>>,
            currentPath: List<String>
        ) {
            val here = currentPath.last()
            if (here == "end") {
                paths.add(currentPath)
            }
            val destinations = maze[here] ?: emptyList()

            destinations.forEach {
                val nextPath = buildList { addAll(currentPath); add(it) }
                if (!currentPath.contains(it) || it.chars().allMatch { it.toChar().isUpperCase() }) {
                    countPaths(maze, paths, nextPath)
                }
            }
        }
    }

    object Part2 {
        fun run() {
            val paths = mutableSetOf<List<String>>()
            countPaths(maze, paths, listOf("start"), false)
            paths.forEach { println(it) }
            println(paths.size)
        }

        private fun countPaths(
            maze: Map<String, List<String>>,
            paths: MutableSet<List<String>>,
            currentPath: List<String>,
            twiceVisited: Boolean
        ) {
            val here = currentPath.last()
            if (here == "end") {
                paths.add(currentPath)
                return
            }
            val destinations = maze[here] ?: emptyList()

            destinations.forEach {
                val isNotStart = it != "start"
                val isSmallCave = isNotStart && it.chars().allMatch { c -> c.toChar().isLowerCase() }
                val isLargeCave = isNotStart && it.chars().allMatch { c -> c.toChar().isUpperCase() }

                val nextPath = buildList { addAll(currentPath); add(it) }

                when {
                    isLargeCave -> countPaths(maze, paths, nextPath, twiceVisited)
                    isSmallCave && !currentPath.contains(it) -> countPaths(maze, paths, nextPath, twiceVisited)
                    isSmallCave && !twiceVisited -> countPaths(maze, paths, nextPath, true)
                    else -> {}
                }
            }
        }
    }
}

fun main() {
    Day12.Part1.run()
    Day12.Part2.run()
}
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.collections.Map.Entry

object Day14 {

    object Part1 {

        private fun toRule(s: String): Pair<List<Char>, Char> {
            val (pair, value) = s.split(" -> ")
            return pair.toCharArray().let { listOf(it[0], it[1]) } to value[0]
        }

        private fun step(
            input: List<Char>,
            rules: Map<List<Char>, Char>
        ) = input.windowed(2, 1)
            .flatMap { p ->
                rules[p]?.let { listOf(p[0], it) } ?: listOf(p[0])
            } + input.last()

        fun run() {
            val lines = File("day14/input-real.txt").readLines(UTF_8)

            val input = lines.first().toCharArray().toList()
            val rules = lines.drop(2).associate(::toRule)

            val result = generateSequence(input) { step(it, rules) }.drop(10).first()

            val freqs = result.groupingBy { it }.eachCount().entries
            val max = freqs.maxOf { it.value }
            val min = freqs.minOf { it.value }

            println(max - min)
        }
    }

    object Part2 {

        private fun toRule(s: String): Pair<List<Char>, Char> {
            val (pair, value) = s.split(" -> ")
            return pair.toCharArray().let { listOf(it[0], it[1]) } to value[0]
        }

        private fun toTransitions() = { (k, v): Entry<List<Char>, Char> ->
            (k[0] to k[1]) to listOf(k[0] to v, v to k[1]) }

        private fun step(
            values: MutableMap<Pair<Char, Char>, Long>,
            transitions: Map<Pair<Char, Char>, List<Pair<Char, Char>>>
        ): MutableMap<Pair<Char, Char>, Long> {
            val newValues = values.flatMap { (k, v) ->
                val new = transitions[k]!!
                new.map { it to v }
            }.groupBy({ it.first }, { it.second }).mapValues { it.value.sum() }

            return newValues.toMutableMap();
        }

        fun run() {
            val lines = File("day14/input-real.txt").readLines(UTF_8)

            val input = lines.first().toCharArray().toList()

            val rules = lines.drop(2).associate(::toRule)
            val transitions = rules.map(toTransitions()).toMap()

            val initialFreqs = input
                .windowed(2, 1)
                .map { it[0] to it[1] }
                .groupingBy { it }
                .eachCount()
                .mapValues { it.value.toLong() }
                .toMutableMap()

            val result = generateSequence(initialFreqs) { step(it, transitions) }.drop(40).first()

            val freqs = result.entries
                .groupBy({ it.key.first }, { it.value })
                .mapValues { it.value.sum() }
                .toMutableMap().also {
                    // Add single last character in
                    it[input.last()] = 1L + it[input.last()]!!
                }

            val max = freqs.maxOf { it.value }
            val min = freqs.minOf { it.value }

            println(max - min)
        }
    }
}

fun main() {
    Day14.Part1.run()
    Day14.Part2.run()
}

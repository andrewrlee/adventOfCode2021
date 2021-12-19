import Day10.Type.CLOSE
import Day10.Type.OPEN
import java.io.File
import java.nio.charset.Charset.defaultCharset
import java.util.Stack

object Day10 {
    data class Bracket(val bracketType: BracketType, val type: Type)

    enum class Type { OPEN, CLOSE }

    enum class BracketType(val open: Char, val close: Char, val points: Int, val autoCompleteScore: Int) {
        SQUARE('[', ']', 57, 2),
        ROUND('(', ')', 3, 1),
        BRACE('{', '}', 1197, 3),
        ANGLE('<', '>', 25137, 4);

        fun match(c: Char): Type? = when (c) {
            open -> OPEN
            close -> CLOSE
            else -> null
        }
    }

    fun String.toBrackets() = this.map { c ->
        BracketType.values().firstNotNullOf { b ->
            b.match(c)?.let { Bracket(b, it) }
        }
    }

    object Part1 {
        private fun findInvalid(brackets: List<Bracket>): BracketType? {
            val stack = Stack<Bracket>()
            brackets.forEach {
                if (it.type == CLOSE) {
                    val opening = stack.pop()
                    if (it.bracketType != opening.bracketType) {
                        return it.bracketType
                    }
                } else {
                    stack.push(it)
                }
            }
            return null
        }

        fun run() {
            File("day10/input-real.txt")
                .readLines(defaultCharset())
                .mapNotNull { findInvalid(it.toBrackets()) }
                .map { it.points }
                .reduce(Int::plus)
                .also { println(it) }
        }
    }

    object Part2 {
        private fun complete(brackets: List<Bracket>): Long? {
            val stack = Stack<Bracket>()
            brackets.forEach {
                if (it.type == CLOSE) {
                    val opening = stack.pop()
                    if (it.bracketType != opening.bracketType) {
                        return null
                    }
                } else {
                    stack.push(it)
                }
            }
            if (stack.isEmpty()) {
                return null
            }

            return stack.reversed().fold(0L) { acc, b -> (acc * 5) + b.bracketType.autoCompleteScore }
        }

        fun run() {
            File("day10/input-real.txt").readLines(defaultCharset())
                .mapNotNull { complete(it.toBrackets()) }
                .sorted()
                .also { println(it[it.size/2]) }
        }
    }
}

fun main() {
    Day10.Part1.run()
    Day10.Part2.run()
}
import java.io.File
import java.lang.Exception
import java.math.BigInteger
import java.nio.charset.Charset.defaultCharset


object Day16 {

    fun toBinary(s: String) = BigInteger(s, 16).toString(2).let {
        String.format("%4s", it).replace(" ", "0").toCharArray().toList()
    }

    data class Packet(
        val version: Int,
        val type: Int,
        val literal: Long? = null,
        val payload: List<Packet>? = emptyList()
    ) {
        fun getTotalVersion(): Int = version + (payload?.sumOf { it.getTotalVersion() } ?: 0)
    }


    data class BinaryString(var input: List<Char>) {

        fun isNotEmpty() = input.isNotEmpty() && input.size > 3

        fun readString(length: Int): String {
            val chars = mutableListOf<Char>()
            var index = 0
            var realCount = 0

            do {
                when (val c = input[index]) {
                    '%' -> {}
                    else -> {
                        chars.add(c)
                        realCount++
                    }
                }
                index++
            } while (realCount != length)
            input = input.subList(index, input.size)
            return chars.joinToString("")
        }

        fun readSection(length: Int): BinaryString {
            val chars = mutableListOf<Char>()
            var index = 0
            var realCount = 0

            do {
                val c = input[index]
                when (c) {
                    '%' -> {}
                    else -> {
                        realCount++
                    }
                }
                chars.add(c)
                index++
            } while (realCount != length)
            input = input.subList(index, input.size)
            return BinaryString(chars)
        }

        fun readInt(length: Int): Int {
            val string = readString(length)
            return Integer.parseInt(string, 2)
        }

        fun finishHexChar() {
            val index = input.indexOf('%')
            input = input.subList(index + 1, input.size)
        }
    }

    object Part1 {
        fun run() {
            var inline =
                File("day16/input-real.txt").readText(defaultCharset()).map { toBinary(it.toString()) }.toList()

            val input = BinaryString(inline.flatMap { it + '%' })

            println(readPacket(input).getTotalVersion())
        }

        private fun readPackets(input: BinaryString): MutableList<Day16.Packet> {
            val packets = mutableListOf<Day16.Packet>()
            while (input.isNotEmpty()) {
                val packet = readPacket(input, false)
                packets.add(packet)
            }
            return packets
        }


        private fun readPacket(input: BinaryString, trim: Boolean = false): Packet {
            val version = input.readInt(3)
            val type = input.readInt(3)
            val payload = when (type) {
                4 -> null
                else -> readOperator(input)
            }
            val literal = when (type) {
                4 -> readLiteral(input)
                else -> null
            }
            if (trim) input.finishHexChar()
            return Packet(version, type, literal, payload)
        }

        private fun readOperator(input: BinaryString): List<Packet> {
            val type = input.readString(1)

            return when (type) {
                "0" -> {
                    val size = input.readInt(15)
                    readPackets(input.readSection(size))
                }
                "1" -> {
                    val numberOfPackets = input.readInt(11)
                    (0..numberOfPackets - 1).map {
                        readPacket(input)
                    }
                }
                else -> throw RuntimeException("unexpected val")
            }
        }

        private fun readLiteral(input: BinaryString): Long {
            val parts = mutableListOf<Char>()
            var finish: Boolean
            do {
                val (a, b, c, d, e) = input.readString(5).toList()
                parts.addAll(listOf(b, c, d, e))
                finish = a == '0'
            } while (!finish)
                return BigInteger(parts.joinToString(""), 2).longValueExact()

        }
    }
}

fun main() {
    Day16.Part1.run()
}
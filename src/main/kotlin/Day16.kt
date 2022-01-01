import Day16.Type.LITERAL
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset.defaultCharset


object Day16 {

    enum class Type(val code: Int) {
        SUM(0),
        PRODUCT(1),
        MIN(2),
        MAX(3),
        LITERAL(4),
        G_THAN(5),
        L_THAN(6),
        EQUAL_TO(7),
        ;

        companion object {
            fun fromCode(code: Int) = Type.values().first { it.code == code }
        }
    }

    fun toBinary(s: String) = BigInteger(s, 16).toString(2).let {
        String.format("%4s", it).replace(" ", "0").toCharArray().toList()
    }

    sealed class Packet(
        open val version: Int,
        open val type: Type,
    ) {
        abstract fun getTotalVersion(): Int
        abstract fun evaluate(): Long
    }

    data class Literal(
        override val version: Int,
        val literal: Long
    ) : Packet(version, LITERAL) {
        override fun getTotalVersion() = version
        override fun evaluate(): Long = literal
    }

    data class Operator(
        override val version: Int,
        override val type: Type,
        val payload: List<Packet>
    ) : Packet(version, type) {
        override fun getTotalVersion() = version + (payload.sumOf { it.getTotalVersion() })
        override fun evaluate() = when (type) {
            Type.SUM -> payload.sumOf { it.evaluate() }
            Type.PRODUCT -> payload.fold(1L) { acc, i -> acc * i.evaluate() }
            Type.MIN -> payload.minOf { it.evaluate() }
            Type.MAX -> payload.maxOf { it.evaluate() }
            Type.G_THAN -> if (payload[0].evaluate() > payload[1].evaluate()) 1 else 0
            Type.L_THAN ->  if (payload[0].evaluate() < payload[1].evaluate()) 1 else 0
            Type.EQUAL_TO -> if (payload[0].evaluate() == payload[1].evaluate()) 1 else 0
            else -> throw RuntimeException("unexpected type")
        }
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


    private fun readPackets(input: BinaryString): MutableList<Day16.Packet> {
        val packets = mutableListOf<Packet>()
        while (input.isNotEmpty()) {
            val packet = readPacket(input, false)
            packets.add(packet)
        }
        return packets
    }


    private fun readPacket(input: BinaryString, trim: Boolean = false): Packet {
        val version = input.readInt(3)
        val packet = when (val type = Type.fromCode(input.readInt(3))) {
            LITERAL -> readLiteral(version, input)
            else -> readOperator(version, type, input)
        }
        if (trim) input.finishHexChar()
        return packet
    }

    private fun readOperator(version: Int, type: Type, input: BinaryString): Packet {
        val packets = when (input.readString(1)) {
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
        return Operator(version, type, packets)
    }

    private fun readLiteral(version: Int, input: BinaryString): Packet {
        val parts = mutableListOf<Char>()
        var finish: Boolean
        do {
            val (a, b, c, d, e) = input.readString(5).toList()
            parts.addAll(listOf(b, c, d, e))
            finish = a == '0'
        } while (!finish)
        val value = BigInteger(parts.joinToString(""), 2).longValueExact()
        return Literal(version, value)
    }

    object Part1 {
        fun run() {
            val inline =
                File("day16/input-real.txt").readText(defaultCharset()).map { toBinary(it.toString()) }.toList()

            val input = BinaryString(inline.flatMap { it + '%' })

            println(readPacket(input).getTotalVersion())
        }
    }

    object Part2 {
        fun run() {
            val inline =
                File("day16/input-real.txt").readText(defaultCharset()).map { toBinary(it.toString()) }.toList()

            val input = BinaryString(inline.flatMap { it + '%' })

            val packet = readPacket(input)
            println(packet.evaluate())
        }
    }
}

fun main() {
    Day16.Part2.run()
}
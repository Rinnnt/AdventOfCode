package aoc2015

import java.io.File

class Day07(filename: String) {
    private val wires = File(filename).bufferedReader().readLines().associate {
        val words = it.split(" -> ")
        words[1] to words[0]
    }

    private val memo = mutableMapOf<String, UShort>()

    private val opMap: Map<String, ((UShort, UShort) -> UShort)> = mapOf(
        "AND" to { a, b -> a and b },
        "OR" to { a, b -> a or b },
        "LSHIFT" to { a, b -> (a.toUInt() shl b.toInt()).toUShort() },
        "RSHIFT" to { a, b -> (a.toUInt() shr b.toInt()).toUShort() }
    )

    private fun eval(wire: String): UShort {
        if ("""(\d+)""".toRegex().matches(wire)) {
            return wire.toUShort()
        }

        if (!memo.contains(wire)) {
            val words = wires[wire]!!.split(" ")
            memo[wire] = when (words.size) {
                1 -> eval(words.first())
                2 -> eval(words[1]).inv()
                3 -> opMap[words[1]]!!(eval(words[0]), eval(words[2]))
                else -> throw Exception("Unknown wiring")
            }
        }

        return memo[wire]!!
    }

    fun part1(): UShort = eval("a")

    fun part2(): UShort {
        memo.clear()
        memo["b"] = 956u
        return eval("a")
    }
}

fun main() {
    val sol = Day07("src/main/resources/2015/Day07Input.txt")
    println(sol.part1())
    println(sol.part2())
}

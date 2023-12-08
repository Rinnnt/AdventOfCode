package aoc2023

import java.io.File

class Day08(val filename: String) {
    private val lines = File(filename).bufferedReader().readLines()

    private val moves = lines.first()
    private val nodeRegex = Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)")
    private val nodes = nodeRegex.findAll(lines.joinToString("$")).associate {
        val (c, l, r) = it.destructured
        c to (l to r)
    }

    private fun movesUntil(from: String, finished: (cur: String) -> Boolean): Long {
        return generateSequence(0 to from) {
            when (moves[it.first % moves.length]) {
                'L' -> (it.first + 1) to nodes[it.second]!!.first
                'R' -> (it.first + 1) to nodes[it.second]!!.second
                else -> -1 to "$$$"
            }
        }.takeWhile { !finished(it.second) }.count().toLong()
    }

    fun part1(): Long = movesUntil("AAA") { it == "ZZZ" }

    private fun gcd(a: Long, b: Long): Long {
        if (b == 0L) {
            return a
        }
        return gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

    fun part2(): Long {
        return nodes.keys
            .filter { it.endsWith('A') }
            .map { start -> movesUntil(start) { it.endsWith('Z') } }
            .reduce(::lcm)
    }
}

fun main() {
    val sol = Day08("src/main/resources/2023/Day08Input.txt")
    println(sol.part1())
    println(sol.part2())
}

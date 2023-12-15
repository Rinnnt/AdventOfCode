package aoc2023

import java.io.File

class Day15(val filename: String) {
    private val sequence = File(filename).bufferedReader().readLine().split(",")

    private fun hash(str: String): Int = str.fold(0) { acc, c -> (17 * (acc + c.code)) % 256 }

    fun part1(): Int = sequence.sumOf(::hash)

    fun part2(): Int {
        val m = mutableMapOf<String, Pair<Int, Int>>()
        val f = MutableList(256) { 0 }

        sequence.forEach { seq ->
            if (seq.contains("=")) {
                val str = seq.substringBefore("=")
                val len = seq.substringAfter("=").toInt()
                val pos = if (m.contains(str)) m[str]!!.first else f[hash(str)]++
                m[str] = pos to len
            } else {
                val str = seq.substringBefore("-")
                m.remove(str)
            }
        }

        return IntRange(0, 255).sumOf { h ->
            m.filterKeys { hash(it) == h }.values.sortedBy { it.first }.withIndex()
                .sumOf { (h + 1) * (it.index + 1) * (it.value.second) }
        }
    }
}

fun main() {
    val sol = Day15("src/main/resources/2023/Day15Input.txt")
    println(sol.part1())
    println(sol.part2())
}

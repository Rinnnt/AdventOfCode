package aoc2023

import java.io.File
import java.lang.Math.pow

class Day04(val filename: String) {
    private val lines = File(filename).bufferedReader().readLines()

    private val cardRegex = Regex("Card\\s+\\d+:([\\d\\s]*)\\|([\\d\\s]*)")
    private val numRegex = Regex("\\d+")

    private val winCounts = cardRegex.findAll(lines.joinToString("$")).map { match ->
        numRegex.findAll(match.groupValues[1])
            .count { num -> numRegex.findAll(match.groupValues[2]).map { it.value }.contains(num.value) }
    }

    fun part1(): Int {
        return winCounts.sumOf { pow(2.0, it.toDouble()).toInt() / 2 }
    }

    fun part2(): Int {
        val counts = MutableList(lines.size) { 1 }
        winCounts.withIndex().forEach { cnt ->
            IntRange(cnt.index + 1, cnt.index + cnt.value).forEach { counts[it] += counts[cnt.index] }
        }
        return counts.sum()
    }

}

fun main() {
    val sol = Day04("src/main/resources/2023/Day04Input.txt")
    println(sol.part1())
    println(sol.part2())
}

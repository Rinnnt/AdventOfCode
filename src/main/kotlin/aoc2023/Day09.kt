package aoc2023

import java.io.File

class Day09(val filename: String) {
    private val sequences =
        File(filename).bufferedReader().readLines().map { line -> line.split(" ").map { it.toLong() } }

    private fun extrapolate(sequence: List<Long>, backwards: Boolean): Long {
        if (sequence.all { it == 0L }) return 0L
        return when (backwards) {
            false -> sequence.last() + extrapolate(sequence.windowed(2).map { it[1] - it[0] }, backwards)
            true -> sequence.first() - extrapolate(sequence.windowed(2).map { it[1] - it[0] }, backwards)
        }
    }

    fun part1(): Long = sequences.sumOf { extrapolate(it, false) }

    fun part2(): Long = sequences.sumOf { extrapolate(it, true) }
}

fun main() {
    val sol = Day09("src/main/resources/2023/Day09Input.txt")
    println(sol.part1())
    println(sol.part2())
}

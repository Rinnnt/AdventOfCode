package AoC

import java.io.File

class Day6(val filename: String) {
    val lines = File(filename).bufferedReader().readLines()

    private fun marker(packet: String, n: Int): Int = packet.withIndex().windowed(n, 1)
        .first { it.distinctBy { (i, v) -> v }.size == n }.last().index + 1

    fun part1(): Int = lines.map { marker(it, 4) }.first()

    fun part2(): Int = lines.map { marker(it, 14) }.first()
}

fun main() {
    val sol = Day6("src/2022/main/resources/Day6Input.txt")
    println(sol.part1())
    println(sol.part2())
}
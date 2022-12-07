package aoc2022

import java.io.File

class Day4 {

    private fun contain(range: List<Int>): Boolean =
        (range[0] <= range[2] && range[1] >= range[3]) ||
                (range[2] <= range[0] && range[3] >= range[1])

    fun part1(filename: String): Int {
        val pairs = File(filename).bufferedReader().readLines()
        val ranges = pairs.map { it.split(",", "-").map { s -> s.toInt() } }
        return ranges.filter { contain(it) }.size
    }

    private fun exclusive(range: List<Int>): Boolean =
        (range[1] < range[2]) || (range[3] < range[0])

    fun part2(filename: String): Int {
        val pairs = File(filename).bufferedReader().readLines()
        val ranges = pairs.map { it.split(",", "-").map { s -> s.toInt() } }
        return ranges.size - ranges.filter { exclusive(it) }.size
    }
}

fun main() {
    val sol = Day4()
    println(sol.part1("src/main/resources/2022/Day4Input.txt"))
    println(sol.part2("src/main/resources/2022/Day4Input.txt"))
}
package aoc2015

import java.io.File

class Day01(filename : String) {
    private val line: String = File(filename).bufferedReader().readLine()

    fun part1(): Int = line.count { it == '(' } - line.count { it == ')' }

    fun part2(): Int {
        var level = 0
        for ((idx, v) in line.withIndex()) {
            when (v) {
                '(' -> level++
                ')' -> level--
            }
            if (level == -1) return idx + 1
        }
        return -1
    }
}

fun main() {
    val sol = Day01("src/main/resources/2015/Day01Input.txt")
    println(sol.part1())
    println(sol.part2())
}

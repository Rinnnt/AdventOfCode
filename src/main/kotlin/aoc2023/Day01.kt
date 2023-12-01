package aoc2023

import java.io.File

class Day01(filename: String) {
    private val cals = File(filename).bufferedReader().readLines()

    fun part1(): Int {
        return cals.sumOf { it -> 10 * it.first { it.isDigit() }.digitToInt() + it.last { it.isDigit() }.digitToInt() }
    }

    private val digitMap = mapOf(
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    fun part2(): Int {
        return cals.sumOf { line ->
            10 * digitMap.entries
                .filter { entry -> line.indexOf(entry.key) != -1 }
                .minBy { entry -> line.indexOf(entry.key) }
                .value
            +digitMap.entries
                .maxBy { entry -> line.lastIndexOf(entry.key) }
                .value
        }
    }
}

fun main() {
    val sol = Day01("src/main/resources/2023/Day01Input.txt")
    println(sol.part1())
    println(sol.part2())
}

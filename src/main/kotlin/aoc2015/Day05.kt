package aoc2015

import java.io.File

class Day05(filename: String) {
    private val strings = File(filename).bufferedReader().readLines()

    private val badStrings = """(ab|cd|pq|xy)""".toRegex()
    private val repeated = """(\w)\1""".toRegex()
    private val vowels = """[aeiou]""".toRegex()

    private fun nice(s: String): Boolean =
        !badStrings.containsMatchIn(s) && repeated.containsMatchIn(s) && vowels.findAll(s).toList().size > 2

    fun part1(): Int = strings.filter { nice(it) }.size

    private val repeatPair = """(\w\w).*\1""".toRegex()
    private val repeatSep = """(\w)\w\1""".toRegex()

    private fun nice2(s: String): Boolean = repeatPair.containsMatchIn(s) && repeatSep.containsMatchIn(s)

    fun part2(): Int = strings.filter { nice2(it) }.size
}

fun main() {
    val sol = Day05("src/main/resources/2015/Day05Input.txt")
    println(sol.part1())
    println(sol.part2())
}

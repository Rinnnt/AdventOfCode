package AoC

import java.io.File

class Day3 {
    private fun priority(c: Char): Int =
        when (c) {
            in 'a'..'z' -> c.code - 'a'.code + 1
            in 'A'..'Z' -> c.code - 'A'.code + 27
            else -> 0
        }

    // turn each half of the string to a set of characters
    // then take the intersection to get the duplicate item
    // return with .elementAt(0)
    // Pre - There is only one duplicate item
    private fun findDuplicate(s: String): Char =
        s.substring(0, s.length / 2).toCharArray().toSet()
            .intersect(s.substring(s.length / 2).toSet()).elementAt(0)

    fun part1(filename: String): Int {
        val lines = File(filename).useLines { it.toList() }
        return lines.sumOf { priority(findDuplicate(it)) }
    }

    // turn each of the three strings to sets
    // and intersect to find duplicate
    // return with .elementAt(0)
    // Pre - there is only one badge
    private fun findBadge(t: List<String>): Char =
        t[0].toSet().intersect(t[1].toSet().intersect(t[2].toSet())).elementAt(0)

    fun part2(filename: String): Int {
        val lines = File(filename).bufferedReader().readLines()
//        val grouped = lines.withIndex().groupBy { it.index / 3 }.map { (k, v) -> v.map { it.value } }
        val grouped = lines.chunked(3)
        return grouped.sumOf { priority(findBadge(it)) }
    }
}

fun main() {
    val sol = Day3()
    println(sol.part1("src/2022/main/resources/Day3Input.txt"))
    println(sol.part2("src/2022/main/resources/Day3Input.txt"))
}

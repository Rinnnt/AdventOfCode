package aoc2023

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day11(val filename: String) {
    private val universe = File(filename).bufferedReader().readLines()
    private val galaxies = universe.withIndex()
        .flatMap { row -> row.value.indices.filter { row.value[it] == '#' }.map { row.index to it } }

    private fun transpose(universe: List<String>): List<String> =
        IntRange(0, universe[0].length - 1).map { idx -> universe.map { it[idx] }.joinToString("") }

    private val emptyRows = universe.scan(0L) { acc, row -> acc + if (row.all { it == '.' }) 1 else 0 }
    private val emptyCols = transpose(universe).scan(0L) { acc, row -> acc + if (row.all { it == '.' }) 1 else 0 }

    private fun totalDistance(n: Long): Long {
        return galaxies.sumOf { galaxy ->
            galaxies.sumOf {
                val er = emptyRows[max(galaxy.first, it.first) + 1] - emptyRows[min(galaxy.first, it.first) + 1]
                val ec = emptyCols[max(galaxy.second, it.second) + 1] - emptyCols[min(galaxy.second, it.second) + 1]
                abs(galaxy.first - it.first) + (n - 1) * er + abs(galaxy.second - it.second) + (n - 1) * ec
            }
        } / 2
    }

    fun part1(): Long = totalDistance(2)

    fun part2(): Long = totalDistance(1000000)
}

fun main() {
    val sol = Day11("src/main/resources/2023/Day11Input.txt")
    println(sol.part1())
    println(sol.part2())
}

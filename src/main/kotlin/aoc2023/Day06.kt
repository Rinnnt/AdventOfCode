package aoc2023

import java.io.File

class Day06(val filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val numRegex = Regex("\\d+")

    private val times = numRegex.findAll(lines.first()).map { it.value }
    private val dists = numRegex.findAll(lines.last()).map { it.value }

    fun part1(): Int {
        return times.map { it.toLong() }
            .zip(dists.map { it.toLong() })
            .map { (time, dist) ->
                LongRange(0, time).count { t -> t * (time - t) > dist }
            }.reduce(Int::times)
    }

    fun part2(): Int {
        val time = times.joinToString("").toLong()
        val dist = dists.joinToString("").toLong()
        return LongRange(0, time).count { t -> t * (time - t) > dist }
    }
}

fun main() {
    val sol = Day06("src/main/resources/2023/Day06Input.txt")
    println(sol.part1())
    println(sol.part2())
}

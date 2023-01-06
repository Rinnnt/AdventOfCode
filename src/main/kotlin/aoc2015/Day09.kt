package aoc2015

import java.io.File
import kotlin.system.measureTimeMillis

class Day09(filename: String) {
    private val cities = mutableSetOf<String>()
    private val distances = File(filename).bufferedReader().readLines().associate {
        val regex = """(\w+) to (\w+) = (\d+)""".toRegex()
        val (city1, city2, dist) = regex.find(it)!!.destructured
        cities.add(city1)
        cities.add(city2)
        if (city1 < city2) Pair(city1, city2) to dist.toInt() else Pair(city2, city1) to dist.toInt()
    }
    private val paths = permutePaths(listOf(), cities)
    private val pathDistances = paths.map { findPathDistance(it) }

    private fun permutePaths(visited: List<String>, left: Set<String>): List<List<String>> {
        val res = mutableListOf<List<String>>()
        if (left.isEmpty()) {
            res.add(visited)
        } else {
            for (l in left) {
                res.addAll(permutePaths(visited.plus(l), left.subtract(setOf(l))))
            }
        }
        return res
    }

    private fun findDistance(city1: String, city2: String): Int =
        if (city1 < city2) distances[Pair(city1, city2)]!! else distances[Pair(city2, city1)]!!

    private fun findPathDistance(path: List<String>): Int = path.windowed(2).sumOf { findDistance(it[0], it[1]) }

    fun part1(): Int = pathDistances.min()

    fun part2(): Int = pathDistances.max()
}

fun main() {
    val sol = Day09("src/main/resources/2015/Day09Input.txt")
    val time = measureTimeMillis {

        println(sol.part1())
        println(sol.part2())
    }
    println("time ${time / 1000.0} seconds")
}

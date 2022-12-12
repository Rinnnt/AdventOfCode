package aoc2022

import java.io.File

class Day12(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val heights = lines.map { it.chunked(1).map { it.single() }.toMutableList() }
    private val yRange = heights.indices
    private val xRange = heights.first().indices
    private val start = heights.withIndex().first { it.value.contains('S') }.index to heights.first { it.contains('S') }.indexOf('S')
    private val end = heights.withIndex().first { it.value.contains('E') }.index to heights.first { it.contains('E') }.indexOf('E')
    private val starts = heights.asSequence().withIndex().filter { it.value.contains('a') }
        .map{ it.index to it.value.withIndex().filter { it.value == 'a'}.map { it.index } }
        .map { p -> p.second.map { p.first to it }}.flatten().toList()

    init {
        heights[start.first][start.second] = 'a'
        heights[end.first][end.second] = 'z'
    }

    private fun bfs(s: Pair<Int, Int> = start): Int {
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf(s)
        val toVisit: ArrayDeque<Pair<Pair<Int, Int>, Int>> = ArrayDeque()
        var distance = Int.MAX_VALUE

        toVisit.add(s to 0)

        while (toVisit.isNotEmpty()) {
            val (p, n) = toVisit.removeFirst()
            val maxCode = heights[p.first][p.second].code + 1
            if (p == end) {
                return n
            }
            if (p.first + 1 in yRange && p.second in xRange && !visited.contains(p.first + 1 to p.second) && heights[p.first + 1][p.second].code <= maxCode) {
                toVisit.addLast((p.first + 1 to p.second) to n + 1)
                visited.add(p.first + 1 to p.second)
            }
            if (p.first - 1 in yRange && p.second in xRange && !visited.contains(p.first - 1 to p.second) && heights[p.first - 1][p.second].code <= maxCode) {
                toVisit.addLast((p.first - 1 to p.second) to n + 1)
                visited.add(p.first - 1 to p.second)
            }
            if (p.first in yRange && p.second + 1 in xRange && !visited.contains(p.first to p.second + 1) && heights[p.first][p.second + 1].code <= maxCode) {
                toVisit.addLast((p.first to p.second + 1) to n + 1)
                visited.add(p.first to p.second + 1)
            }
            if (p.first in yRange && p.second - 1 in xRange && !visited.contains(p.first to p.second - 1) && heights[p.first][p.second - 1].code <= maxCode) {
                toVisit.addLast((p.first to p.second - 1) to n + 1)
                visited.add(p.first to p.second - 1)
            }
        }
        return distance
    }

    fun part1(): Int = bfs()

    fun part2(): Int = starts.minOf { bfs(it) }
}

fun main() {
    val sol = Day12("src/main/resources/2022/Day12Input.txt")
    println(sol.part1())
    println(sol.part2())
}

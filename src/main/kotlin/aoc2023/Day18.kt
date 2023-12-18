package aoc2023

import java.io.File
import kotlin.math.abs

class Day18(val filename: String) {
    private val digPlan = File(filename).bufferedReader().readLines()
    private val originalPlanRegex = Regex("([UDRL]) (\\d+) .*")
    private val colorPlanRegex = Regex("\\w \\d+ \\(#(\\w{5})(\\w)\\)")
    private val dxys = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    private val direction = mapOf(
        "U" to 0, "D" to 1, "L" to 2, "R" to 3,
        "3" to 0, "1" to 1, "2" to 2, "0" to 3,
    )

    private val originalPlan = digPlan.map {
        val (dir, num) = originalPlanRegex.matchEntire(it)!!.destructured
        direction[dir]!! to num.toLong()
    }

    private val colorPlan = digPlan.map {
        val (num, dir) = colorPlanRegex.matchEntire(it)!!.destructured
        direction[dir]!! to num.toLong(16)
    }

    private fun shoelace(ps: List<Pair<Long, Long>>): Long {
        // shoelace formula
        val (xs, ys) = ps.unzip()
        return abs((xs.zip(ys.drop(1) + ys.take(1)).sumOf { it.first * it.second }
                - (xs.drop(1) + xs.take(1)).zip(ys).sumOf { it.first * it.second }) / 2)
    }

    private fun area(plan: List<Pair<Int, Long>>): Long {
        val points = plan.scan(0L to 0L) { acc, p ->
            val (dx, dy) = dxys[p.first]
            acc.first + dx * p.second to acc.second + dy * p.second
        }
        val numBoundaryPoints = points.windowed(2)
            .sumOf { abs(it[1].first - it[0].first) + abs(it[1].second - it[0].second) }
        // modified pick's theorem to include boundary coordinates
        return shoelace(points.drop(1)) + numBoundaryPoints / 2 + 1
    }

    fun part1(): Long = area(originalPlan)

    fun part2(): Long = area(colorPlan)
}

fun main() {
    val sol = Day18("src/main/resources/2023/Day18Input.txt")
    println(sol.part1())
    println(sol.part2())
}

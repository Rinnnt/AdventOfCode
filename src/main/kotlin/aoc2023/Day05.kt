package aoc2023

import java.io.File

class Day05(val filename: String) {
    private val lines = File(filename).bufferedReader().readLines()

    private val MAXRANGE = 1000000000000L

    private val seedRegex = Regex("seeds: ([\\d\\s]*)")
    private val mapRegex = Regex(".*map:\\n([\\d\\s]*)")
    private val rangeRegex = Regex("(\\d*) (\\d*) (\\d*)")
    private val numRegex = Regex("\\d+")

    private val seeds =
        numRegex.findAll(seedRegex.find(lines.first())!!.groups[0]!!.value).map { it.value.toLong() }.toList()
    private val maps = mapRegex.findAll(lines.joinToString("\n")).map { match ->
        rangeRegex.findAll(match.groups[1]!!.value).map {
            val (dst, src, rng) = it.destructured
            Triple(dst.toLong(), src.toLong(), rng.toLong())
        }.sortedBy { it.second }.toList()
    }.toList()

    private fun pass(seed: Long): Pair<Long, Long> {
        var num = seed
        var same = MAXRANGE
        maps.forEach { map ->
            map.firstOrNull { num < it.second + it.third }?.let { range ->
                if (num < range.second) {
                    same = same.coerceAtMost(range.second - num)
                } else {
                    same = same.coerceAtMost(range.second + range.third - num)
                    num = range.first + num - range.second
                }
            }
        }
        return num to same
    }

    fun part1(): Long {
        return seeds.minOf { pass(it).first }
    }

    fun part2(): Long {
        var min = MAXRANGE
        seeds.windowed(2, 2).forEach { rng ->
            var seed: Long = rng.first()
            while (seed < rng.first() + rng.last()) {
                val (num, inc) = pass(seed)
                min = min.coerceAtMost(num)
                seed += inc
            }
        }
        return min
    }

}

fun main() {
    val sol = Day05("src/main/resources/2023/Day05Input.txt")
    println(sol.part1())
    println(sol.part2())
}

package aoc2023

import java.io.File

class Day03(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()

    private val dxs = listOf(-1, 0, 1)
    private val dys = listOf(-1, 0, 1)
    private val dxys = dxs.flatMap { dx -> dys.map { dy -> dx to dy } }
    private val gearRegex = Regex("\\*")
    private val symbolRegex = Regex("[^0-9.]")
    private val numberRegex = Regex("\\d+")

    private fun inBounds(x: Int, y: Int): Boolean = lines.indices.contains(x) && lines[x].indices.contains(y)

    fun part1(): Int {
        return lines.withIndex().sumOf { line ->
            numberRegex.findAll(line.value).filter { match ->
                match.range.any { idx ->
                    dxys.map { line.index + it.first to idx + it.second }.any { (i, j) ->
                        inBounds(i, j) && symbolRegex.matches(lines[i][j].toString())
                    }
                }
            }.sumOf { match -> match.value.toInt() }
        }
    }

    fun part2(): Int {
        return lines.withIndex().sumOf { line ->
            gearRegex.findAll(line.value).map { gearMatch ->
                dxs.map { line.index + it }.filter { inBounds(it, 0) }.flatMap { numberRegex.findAll(lines[it]) }
                    .filter { numberMatch ->
                        dys.map { gearMatch.range.first + it }.filter { inBounds(0, it) }
                            .any { numberMatch.range.contains(it) }
                    }
            }.filter { it.size == 2 }.sumOf { matches ->
                matches.map { it.value.toInt() }.reduce(Int::times)
            }
        }
    }

}

fun main() {
    val sol = Day03("src/main/resources/2023/Day03Input.txt")
    println(sol.part1())
    println(sol.part2())
}

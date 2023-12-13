package aoc2023

import java.io.File

class Day13(val filename: String) {
    private val patterns =
        File(filename).bufferedReader().readLines().joinToString("\n").split("\n\n").map { rows -> rows.split("\n") }

    private fun transpose(pattern: List<String>): List<String> =
        IntRange(0, pattern[0].length - 1).map { idx -> pattern.map { it[idx] }.joinToString("") }

    private fun hash(row: String): Long = row.replace('.', '0').replace('#', '1').toLong(2)

    private val hashed = patterns.map { it.map(::hash) to transpose(it).map(::hash) }

    private fun mirrorPoints(rows: List<Long>): List<Long> {
        return LongRange(1, (rows.size - 1).toLong()).filter {
            rows.subList(0, it.toInt()).reversed().zip(rows.subList(it.toInt(), rows.size))
                .all { p -> p.first == p.second }
        }
    }

    private fun mirrorPoint(rows: List<Long>): Long = mirrorPoints(rows).elementAtOrElse(0) { 0 }

    fun part1(): Long = hashed.sumOf {
        100 * mirrorPoint(it.first) + mirrorPoint(it.second)
    }

    private fun smudge(pattern: List<String>, i: Int, j: Int): List<String> {
        val newPattern = pattern.toList().map { it.toMutableList() }
        newPattern[i][j] = when (newPattern[i][j]) {
            '.' -> '#'
            '#' -> '.'
            else -> '$'
        }
        return newPattern.map { it.joinToString("") }
    }

    fun part2(): Long {
        return patterns.zip(hashed).sumOf { (pattern, hash) ->
            val originalMirror = 100 * mirrorPoint(hash.first) + mirrorPoint(hash.second)

            IntRange(0, pattern.size - 1).flatMap { row ->
                IntRange(0, pattern[row].length - 1)
                    .map { col -> smudge(pattern, row, col) }
            }
                .map { it.map(::hash) to transpose(it).map(::hash) }
                .flatMap { mirrorPoints(it.first).map { p -> 100 * p } + mirrorPoints(it.second) }
                .first { it != 0L && it != originalMirror }
        }
    }
}

fun main() {
    val sol = Day13("src/main/resources/2023/Day13Input.txt")
    println(sol.part1())
    println(sol.part2())
}

package aoc2023

import java.io.File

class Day02(filename: String) {
    private val games = File(filename).bufferedReader().readLines()
    private val colours = listOf("red", "green", "blue")
    private val handRegex = Regex("(\\d+) (${colours.joinToString("|")})")
    private val gameRegex = Regex("Game (\\d+)")

    fun part1(): Int {
        val limits = listOf(12, 13, 14)
        return games.filter { game ->
            handRegex.findAll(game).all { match ->
                match.groupValues[1].toInt() <= limits[colours.indexOf(match.groupValues[2])]
            }
        }.sumOf { gameRegex.find(it)?.groupValues?.get(1)?.toInt() ?: 0 }
    }

    fun part2(): Int {
        return games.sumOf { game ->
            colours.map { colour ->
                handRegex.findAll(game)
                    .filter { match -> match.groupValues[2] == colour }
                    .maxOf { match -> match.groupValues[1].toInt() }
            }.reduce(Int::times)
        }
    }
}

fun main() {
    val sol = Day02("src/main/resources/2023/Day02Input.txt")
    println(sol.part1())
    println(sol.part2())
}

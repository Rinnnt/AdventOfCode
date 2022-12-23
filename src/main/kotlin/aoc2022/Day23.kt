package aoc2022

import java.io.File

data class Position(val x: Int, val y: Int)

class Day23(filename: String) {
    private val ELVES = File(filename).bufferedReader().readLines().withIndex().map { row ->
        row.value.withIndex().filter { it.value == '#' }.map { Position(row.index, it.index) }
    }.flatten().toSet()
    private var elves = ELVES.toMutableSet()

    private val directions = listOf(
        listOf(Position(-1, -1), Position(-1, 0), Position(-1, 1)) to Position(-1, 0),
        listOf(Position(1, -1), Position(1, 0), Position(1, 1)) to Position(1, 0),
        listOf(Position(-1, -1), Position(0, -1), Position(1, -1)) to Position(0, -1),
        listOf(Position(-1, 1), Position(0, 1), Position(1, 1)) to Position(0, 1),
    )

    private val adjacent = listOf(
        Position(-1, -1),
        Position(-1, 0),
        Position(-1, 1),
        Position(0, -1),
        Position(0, 1),
        Position(1, -1),
        Position(1, 0),
        Position(1, 1)
    )

    private fun simulateRounds(n: Int): Int {
        elves = ELVES.toMutableSet()
        var dirIndex = 0
        var round = 1
        for (i in 1..n) {
            val goTo = mutableMapOf<Position, Position>()
            val dest = mutableMapOf<Position, Int>()
            elves.forEach { e ->
                if (!adjacent.all { !elves.contains(Position(e.x + it.x, e.y + it.y)) }) {
                    for (j in 0 until 4) {
                        val idx = (dirIndex + j) % directions.size
                        if (directions[idx].first.all { !elves.contains(Position(e.x + it.x, e.y + it.y)) }) {
                            val p = directions[idx].second
                            goTo[e] = Position(e.x + p.x, e.y + p.y)
                            dest[goTo[e]!!] = dest.getOrDefault(goTo[e]!!, 0) + 1
                            break
                        }
                    }
                }
            }
            if (goTo.isEmpty()) {
                return round
            }
            goTo.forEach { (old, new) ->
                if (dest[new] == 1) {
                    elves.remove(old)
                    elves.add(new)
                }
            }
            dirIndex = (dirIndex + 1) % directions.size
            round++
        }
        return round
    }

    private fun emptyTiles(): Int {
        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }
        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    fun part1(): Int {
        simulateRounds(10)
        return emptyTiles()
    }

    fun part2(): Int = simulateRounds(10000)
}

fun main() {
    val sol = Day23("src/main/resources/2022/Day23Input.txt")
    println(sol.part1())
    println(sol.part2())
}

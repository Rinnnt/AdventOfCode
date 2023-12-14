package aoc2023

import java.io.File

class Day14(val filename: String) {
    private val rocks = File(filename).bufferedReader().readLines().map { it.toList() }

    enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    private fun tiltVert(rocks: List<List<Char>>, dir: Direction): List<List<Char>> {
        val tilted = List(rocks.size) { MutableList(rocks[0].size) { '.' } }

        val range = if (dir == Direction.NORTH) rocks.indices else rocks.size - 1 downTo 0
        val dx = if (dir == Direction.NORTH) 1 else -1

        for (j in 0 until rocks[0].size) {
            var free = range.first
            for (i in range) {
                when (rocks[i][j]) {
                    'O' -> {
                        tilted[free][j] = 'O'
                        free += dx
                    }

                    '#' -> {
                        tilted[i][j] = '#'
                        free = i + dx
                    }
                }
            }
        }
        return tilted.map { it.toList() }
    }

    private fun tiltHori(rocks: List<List<Char>>, dir: Direction): List<List<Char>> {
        val tilted = List(rocks.size) { MutableList(rocks[0].size) { '.' } }

        val range = if (dir == Direction.WEST) rocks[0].indices else rocks[0].size - 1 downTo 0
        val dy = if (dir == Direction.WEST) 1 else -1

        for (i in rocks.indices) {
            var free = range.first
            for (j in range) {
                when (rocks[i][j]) {
                    'O' -> {
                        tilted[i][free] = 'O'
                        free += dy
                    }

                    '#' -> {
                        tilted[i][j] = '#'
                        free = j + dy
                    }
                }
            }
        }
        return tilted.map { it.toList() }
    }

    private fun northLoad(rocks: List<List<Char>>): Int =
        rocks.withIndex().sumOf { (rocks.size - it.index) * it.value.count { c -> c == 'O' } }

    fun part1(): Int {
        return northLoad(tiltVert(rocks, Direction.NORTH))
    }

    private fun tiltAll(rocks: List<List<Char>>): List<List<Char>> {
        var tilted = rocks
        tilted = tiltVert(tilted, Direction.NORTH)
        tilted = tiltHori(tilted, Direction.WEST)
        tilted = tiltVert(tilted, Direction.SOUTH)
        tilted = tiltHori(tilted, Direction.EAST)
        return tilted
    }

    fun part2(): Int {
        var tilted = rocks
        for (i in 1..1000) {
            tilted = tiltAll(tilted)
        }
        val loads = mutableListOf<Int>()
        var cycle = 0
        for (i in 1..1000) {
            tilted = tiltAll(tilted)
            loads.add(northLoad(tilted))
            if (i % 3 == 0) {
                cycle = i / 3
                if (IntRange(0, cycle - 1)
                        .all { loads[it] == loads[it + cycle] && loads[it] == loads[it + 2 * cycle] }
                ) break
            }
        }
        val index = (1000000000 - 1001) % cycle
        return loads[index]
    }
}

fun main() {
    val sol = Day14("src/main/resources/2023/Day14Input2.txt")
    println(sol.part1())
    println(sol.part2())
}

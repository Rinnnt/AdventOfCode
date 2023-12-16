package aoc2023

import java.io.File
import java.util.*

class Day16(val filename: String) {
    private val grid = File(filename).bufferedReader().readLines()

    private fun inBounds(pos: Pair<Int, Int>): Boolean =
        pos.first >= 0 && pos.first < grid.size && pos.second >= 0 && pos.second < grid[0].length

    private fun getDir(dir: Pair<Int, Int>, tile: Char): List<Pair<Int, Int>> {
        when (tile) {
            '.' -> return listOf(dir)

            '/' -> return if (dir.first == 0) {
                listOf(-dir.second to 0)
            } else {
                listOf(0 to -dir.first)
            }

            '\\' -> return if (dir.first == 0) {
                listOf(dir.second to 0)
            } else {
                listOf(0 to dir.first)
            }

            '|' -> return if (dir.first == 0) {
                listOf(-1 to 0, 1 to 0)
            } else {
                return listOf(dir)
            }

            '-' -> return if (dir.first == 0) {
                listOf(dir)
            } else {
                listOf(0 to -1, 0 to 1)
            }
        }
        return listOf()
    }

    private fun beamFrom(startPos: Pair<Int, Int>, startDir: Pair<Int, Int>): Int {
        val visited = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        val q = ArrayDeque<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        q.add(startPos to startDir)

        while (q.isNotEmpty()) {
            val (pos, dir) = q.removeFirst()
            if (!visited.contains(pos to dir)) {
                getDir(dir, grid[pos.first][pos.second]).forEach {
                    val newPos = pos.first + it.first to pos.second + it.second
                    if (inBounds(newPos)) {
                        q.addLast(newPos to it)
                    }
                }
                visited.add(pos to dir)
            }
        }

        return visited.map { it.first }.toSet().size
    }

    fun part1(): Int = beamFrom(0 to 0, 0 to 1)

    fun part2(): Int = IntRange(0, grid.size - 1).maxOf { beamFrom(it to 0, 0 to 1) }
        .coerceAtLeast(IntRange(0, grid.size - 1).maxOf { beamFrom(it to grid[0].length - 1, 0 to -1) })
        .coerceAtLeast(IntRange(0, grid[0].length - 1).maxOf { beamFrom(0 to it, 1 to 0) })
        .coerceAtLeast(IntRange(0, grid[0].length - 1).maxOf { beamFrom(grid.size - 1 to it, -1 to 0) })
}

fun main() {
    val sol = Day16("src/main/resources/2023/Day16Input.txt")
    println(sol.part1())
    println(sol.part2())
}

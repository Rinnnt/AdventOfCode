package aoc2023

import java.io.File

class Day21(val filename: String) {
    private val garden = File(filename).bufferedReader().readLines()
    private val start = garden.indices.map { it to garden[it].indexOf('S') }.first { it.second != -1 }

    private val dxys = listOf(
        0 to 1, 0 to -1, 1 to 0, -1 to 0
    )

    private fun inBounds(x: Int, y: Int): Boolean = garden.indices.contains(x) && garden[x].indices.contains(y)

    private fun count(start: Pair<Int, Int>, steps: Int): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val q = ArrayDeque<Pair<Int, Pair<Int, Int>>>()
        q.add(0 to start)
        while (!q.isEmpty()) {
            val (d, cur) = q.removeFirst()
            val (x, y) = cur
            if (d == steps) continue

            dxys.forEach {
                val nx = x + it.first
                val ny = y + it.second
                if (inBounds(nx, ny) && !visited.contains(nx to ny) && garden[nx][ny] != '#') {
                    visited.add(nx to ny)
                    q.addLast(d + 1 to (nx to ny))
                }
            }
        }

        return visited.filter { (it.first + it.second) % 2 == (start.first + start.second + steps) % 2 }.size
    }

    fun part1(): Int = count(start, 64)

    fun part2(): Long {
        // https://github.com/hyper-neutrino/advent-of-code/blob/main/2023/day21p2.py
        val steps = 26501365
        val size = garden.size

        val gridWidth = steps / size - 1

        val oddSide = gridWidth / 2 * 2 + 1
        val odd = oddSide * 1L * oddSide
        val evenSide = (gridWidth + 1) / 2 * 2
        val even = evenSide * 1L * evenSide

        val oddCount = count(start, size * 2 + 1)
        val evenCount = count(start, size * 2)

        val cornerT = count(size - 1 to start.second, size - 1)
        val cornerR = count(start.first to 0, size - 1)
        val cornerB = count(0 to start.second, size - 1)
        val cornerL = count(start.first to size - 1, size - 1)


        val smallTR = count(size - 1 to 0, size / 2 - 1)
        val smallTL = count(size - 1 to size - 1, size / 2 - 1)
        val smallBR = count(0 to 0, size / 2 - 1)
        val smallBL = count(0 to size - 1, size / 2 - 1)

        val largeTR = count(size - 1 to 0, size * 3 / 2 - 1)
        val largeTL = count(size - 1 to size - 1, size * 3 / 2 - 1)
        val largeBR = count(0 to 0, size * 3 / 2 - 1)
        val largeBL = count(0 to size - 1, size * 3 / 2 - 1)

        return (odd * 1L * oddCount
                + even * 1L * evenCount
                + cornerT + cornerR + cornerB + cornerL
                + (gridWidth + 1) * 1L * (smallTR + smallTL + smallBR + smallBL)
                + (gridWidth) * 1L * (largeTR + largeTL + largeBR + largeBL))
    }
}

fun main() {
    val sol = Day21("src/main/resources/2023/Day21Input.txt")
    println(sol.part1())
    println(sol.part2())
}

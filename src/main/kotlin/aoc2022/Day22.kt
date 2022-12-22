package aoc2022

import java.io.File

class Day22(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val path = lines.last().fold(mutableListOf(mutableListOf<Char>())) { acc, c ->
        acc.apply {
            if (c.isDigit()) {
                acc.last().add(c)
            } else {
                add(mutableListOf(c))
                add(mutableListOf())
            }
        }
    }.map { it.joinToString("") }

    private val columnSize = lines.subList(0, lines.size - 2).maxOf { it.length }
    private val rowSize = lines.size - 2
    val map = lines.subList(0, lines.size - 2).map {
        val padLength = columnSize
        it.padEnd(padLength, ' ').split("").subList(1, padLength + 1)
    }

    private val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    private fun moveForward(p: Pair<Int, Int>, d: Int): Pair<Int, Int> {
        val dir = directions[d]
        var newPos = (p.first + dir.first + rowSize) % rowSize to (p.second + dir.second + columnSize) % columnSize
        while (map[newPos.first][newPos.second] == " ") {
            newPos =
                (newPos.first + dir.first + rowSize) % rowSize to (newPos.second + dir.second + columnSize) % columnSize
        }
        return if (map[newPos.first][newPos.second] == "#") {
            p
        } else {
            newPos
        }
    }

    private fun moveForwardCube(p: Pair<Int, Int>, d: Int): Pair<Pair<Int, Int>, Int> {
        var dir = d
        var newPos = p.first + directions[dir].first to p.second + directions[dir].second
        if (newPos.first in 0..49 && newPos.second == 49 && d == 2) {
            newPos = 49 - newPos.first + 100 to 0
            dir = 0
        } else if (newPos.first in 50..99 && newPos.second == 49 && d == 2) {
            newPos = 100 to newPos.first - 50
            dir = 1
        } else if (newPos.first == 99 && newPos.second in 0..49 && d == 3) {
            newPos = 50 + newPos.second to 50
            dir = 0
        } else if (newPos.first in 100..149 && newPos.second == -1 && d == 2) {
            newPos = 149 - newPos.first to 50
            dir = 0
        } else if (newPos.first in 150..199 && newPos.second == -1 && d == 2) {
            newPos = 0 to 50 + newPos.first - 150
            dir = 1
        } else if (newPos.first == 200 && newPos.second in 0..49 && d == 1) {
            newPos = 100 + newPos.second to 0
            dir = 1
        } else if (newPos.first in 150..199 && newPos.second == 50 && d == 0) {
            newPos = 149 to 50 + newPos.first - 150
            dir = 3
        } else if (newPos.first == 150 && newPos.second in 50..99 && d == 1) {
            newPos = 150 + newPos.second - 50 to 49
            dir = 2
        } else if (newPos.first in 100..149 && newPos.second == 100 && d == 0) {
            newPos = 149 - newPos.first to 149
            dir = 2
        } else if (newPos.first in 50..99 && newPos.second == 100 && d == 0) {
            newPos = 49 to 100 + newPos.first - 50
            dir = 3
        } else if (newPos.first == 50 && newPos.second in 100..149 && d == 1) {
            newPos = 50 + newPos.second - 100 to 99
            dir = 2
        } else if (newPos.first in 0..49 && newPos.second == 150 && d == 0) {
            newPos = 49 - newPos.first + 100 to 99
            dir = 2
        } else if (newPos.first == -1 && newPos.second in 100..149 && d == 3) {
            newPos = 199 to newPos.second - 100
            dir = 3
        } else if (newPos.first == -1 && newPos.second in 50..99 && d == 3) {
            newPos = newPos.second - 50 + 150 to 0
            dir = 0
        }
        return if (map[newPos.first][newPos.second] == "#") {
            p to d
        } else {
            newPos to dir
        }
    }

    private fun simulatePath(cube: Boolean = false): Int {
        var pos = 0 to map[0].withIndex().first { it.value == "." }.index
        var dirIndex = 0
        for (p in path) {
            if (p == "R") {
                dirIndex = (dirIndex + 1) % directions.size
            } else if (p == "L") {
                dirIndex = (dirIndex - 1 + directions.size) % directions.size
            } else {
                for (i in 1..p.toInt()) {
                    if (cube) {
                        val tmp = moveForwardCube(pos, dirIndex)
                        pos = tmp.first
                        dirIndex = tmp.second
                    } else {
                        pos = moveForward(pos, dirIndex)
                    }
                }
            }
        }
        return 1000 * (pos.first + 1) + 4 * (pos.second + 1) + dirIndex
    }

    fun part1(): Int =
        simulatePath()

    fun part2(): Int =
        simulatePath(true)
}

fun main() {
    val sol = Day22("src/main/resources/2022/Day22Input.txt")
    println(sol.part1())
    println(sol.part2())
}

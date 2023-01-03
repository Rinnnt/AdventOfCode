package aoc2015

import java.io.File

class Day03(filename: String) {
    private val directions = File(filename).bufferedReader().readLine()
    private val dirMap = mapOf(
        '^' to (0 to 1),
        '>' to (1 to 0),
        'v' to (0 to -1),
        '<' to (-1 to 0)
    )

    fun part1(): Int {
        val visited = mutableSetOf(0 to 0)
        var current = 0 to 0
        directions.forEach {
            val direction = dirMap[it]!!
            current = current.first + direction.first to current.second + direction.second
            visited.add(current)

        }
        return visited.size
    }

    fun part2(): Int {
        val visited = mutableSetOf(0 to 0)
        var santa = 0 to 0
        var robot = 0 to 0
        directions.chunked(2).forEach {
            val santaDir = dirMap[it[0]]!!
            val robotDir = dirMap[it[1]]!!
            santa = santa.first + santaDir.first to santa.second + santaDir.second
            robot = robot.first + robotDir.first to robot.second + robotDir.second
            visited.add(santa)
            visited.add(robot)
        }
        return visited.size
    }
}

fun main() {
    val sol = Day03("src/main/resources/2015/Day03Input.txt")
    println(sol.part1())
    println(sol.part2())
}

package aoc2015

import java.io.File

class Day06(filename: String) {
    private val lines = File(filename).bufferedReader().readLines().map {
        val action = """(on|off|toggle)""".toRegex()
        val range = """(\d+)""".toRegex()
        action.find(it)!!.value to (range.findAll(it).toList().map { it.value.toInt() })
    }

    private fun simulate(part1: Boolean = true): Int {
        val lights = List<MutableList<Int>>(1000) { MutableList<Int>(1000) { 0 } }

        fun on(x: Int, y: Int) {
            if (part1) {
                lights[x][y] = 1
            } else {
                lights[x][y] += 1
            }
        }

        fun off(x: Int, y: Int) {
            if (part1) {
                lights[x][y] = 0
            } else {
                lights[x][y] = (lights[x][y] - 1).coerceAtLeast(0)
            }
        }

        fun toggle(x: Int, y: Int) {
            if (part1) {
                lights[x][y] = 1 - lights[x][y]
            } else {
                lights[x][y] += 2
            }
        }

        lines.forEach { (action, range) ->
            val func = when (action) {
                "on" -> ::on
                "off" -> ::off
                "toggle" -> ::toggle
                else -> throw Exception("Input Regex Malfunction")
            }
            val xRange = range[0].coerceAtMost(range[2])..range[0].coerceAtLeast(range[2])
            val yRange = range[1].coerceAtMost(range[3])..range[1].coerceAtLeast(range[3])
            for (x in xRange) {
                for (y in yRange) {
                    func(x, y)
                }
            }
        }
        return lights.sumOf { it.sum() }
    }

    fun part1(): Int = simulate()

    fun part2(): Int = simulate(false)
}

fun main() {
    val sol = Day06("src/main/resources/2015/Day06Input.txt")
    println(sol.part1())
    println(sol.part2())
}

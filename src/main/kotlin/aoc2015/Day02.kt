package aoc2015

import java.io.File

class Day02(filename: String) {
    private val regex = """(\d+)x(\d+)x(\d+)""".toRegex()
    private val dimensions = File(filename).bufferedReader().readLines().map {
        val (x, y, z) = regex.find(it)!!.destructured
        Triple(x.toInt(), y.toInt(), z.toInt())
    }

    fun part1(): Int =
        dimensions.sumOf { (x, y, z) ->
            val side1 = x * y
            val side2 = y * z
            val side3 = z * x
            2 * (side1 + side2 + side3) + side1.coerceAtMost(side2.coerceAtMost(side3))
        }

    fun part2(): Int =
        dimensions.sumOf { (x, y, z) ->
            var res = 0
            res += if (x < y) {
                if (y < z) {
                    2 * (x + y)
                } else {
                    2 * (x + z)
                }
            } else {
                if (x < z) {
                    2 * (x + y)
                } else {
                    2 * (y + z)
                }
            }
            res + (x * y * z)
        }
}

fun main() {
    val sol = Day02("src/main/resources/2015/Day02Input.txt")
    println(sol.part1())
    println(sol.part2())
}
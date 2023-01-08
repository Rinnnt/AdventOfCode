package aoc2015

import java.io.File

class Day10(filename: String) {
    private val start = File(filename).bufferedReader().readLine()

    private fun simulate(s: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < s.length) {
            val c = s[i]
            var j = i
            while (j < s.length && s[j] == c)
                j++
            sb.append((j - i).toString())
            sb.append(c)
            i = j
        }
        return sb.toString()
    }

    private fun simulateNTimes(s: String, n: Int): String {
        var string = s
        for (i in 1..n) {
            string = simulate(string)
        }
        return string
    }

    fun part1(): Int = simulateNTimes(start, 40).length

    fun part2(): Int = simulateNTimes(start, 50).length
}

fun main() {
    val sol = Day10("src/main/resources/2015/Day10Input.txt")
    println(sol.part1())
    println(sol.part2())
}

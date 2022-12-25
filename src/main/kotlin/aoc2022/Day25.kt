package aoc2022

import java.io.File
import kotlin.math.pow

class Day25(filename: String) {
    val snafus = File(filename).bufferedReader().readLines()
    val maxDigits = snafus.maxOf { it.length }
    val powersOfFive = List<Long>(maxDigits) { i -> 5.0.pow(i.toDouble()).toLong() }

    val snafuDigits: Map<Char, Long> = mapOf(
        '2' to 2,
        '1' to 1,
        '0' to 0,
        '-' to -1,
        '=' to -2
    )

    val digitsSnafu: Map<Long, Char> = mapOf(
        4.toLong() to '-',
        3.toLong() to '=',
        2.toLong() to '2',
        1.toLong() to '1',
        0.toLong() to '0'
    )

    fun snafuToDecimal(s: String): Long =
        (s.toCharArray().reversed() zip powersOfFive).sumOf { snafuDigits[it.first]!! * it.second }

    fun decimalToSnafu(n: Long): String {
        var num = n
        var res = mutableListOf<Char>()
        while (num > 0) {
            var (q, r) = num / 5 to num % 5
            if (r >= 3) q++
            res.add(digitsSnafu[r]!!)
            num = q
        }
        return res.joinToString("").reversed()
    }

    fun part1(): String =
        decimalToSnafu(snafus.sumOf { snafuToDecimal(it) })
}

fun main() {
    val sol = Day25("src/main/resources/2022/Day25Input.txt")
    println(sol.part1())
}

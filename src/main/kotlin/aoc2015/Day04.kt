package aoc2015

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class Day04(filename: String) {
    private val key = File(filename).bufferedReader().readLine()

    private fun findHash(n: Int): Int {
        var ret = 1
        while (true) {
            val md = MessageDigest.getInstance("MD5")
            if (BigInteger(1, md.digest(key.plus(ret.toString()).toByteArray())).toString(16).padStart(32, '0').startsWith("0".repeat(n))) {
                return ret
            } else {
                ret++
            }
        }
    }

    fun part1() = findHash(5)

    fun part2() = findHash(6)
}

fun main() {
    val sol = Day04("src/main/resources/2015/Day04Input.txt")
    println(sol.part1())
    println(sol.part2())
}

package aoc2015

import java.io.File

class Day08(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val codeLength = lines.sumOf { it.length }
    private val specialChars = setOf('\\', '"')

    private fun stringLength(s: String): Int {
        var length = 0
        var idx = 1
        while (idx < s.length - 1) {
            length++
            if (s[idx] == '\\') {
                idx += if (s[idx + 1] == 'x') 3 else 1
            }
            idx++
        }
        return length
    }

    private fun encodingLength(s: String): Int = s.sumOf { if (specialChars.contains(it)) 2 else 1.toInt() } + 2

    fun part1(): Int = codeLength - lines.sumOf { stringLength(it) }

    fun part2(): Int = lines.sumOf { encodingLength(it) } - codeLength
}

fun main() {
    val sol = Day08("src/main/resources/2015/Day08Input.txt")
    println(sol.part1())
    println(sol.part2())
}

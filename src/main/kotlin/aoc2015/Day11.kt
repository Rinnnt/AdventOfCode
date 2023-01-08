package aoc2015

import java.io.File

class Day11(filename: String) {
    val oldPassword = File(filename).bufferedReader().readLine().toCharArray()

    fun increment(a: CharArray) {
        var i = a.size - 1
        a[i] = ((a[i].code - 96) % 26 + 97).toChar()
        while (i > 0 && a[i] == 'a') {
            i -= 1
            a[i] = ((a[i].code - 96) % 26 + 97).toChar()
        }
    }

    fun nextValid(a: CharArray) {
        while (a.contains('i') || a.contains('o') || a.contains('l')) {
            increment(a)
        }
    }

    fun validPassword(a: CharArray): Boolean {
        if (a.contains('i') || a.contains('o') || a.contains('l')) return false
        if (!a.toList().windowed(3).any { it[2].code == it[1].code + 1 && it[1].code == it[0].code + 1 }) return false

        val regex = """(\w)\1.*(\w)\2""".toRegex()
        if (!regex.containsMatchIn(a.toString())) return false
        return true
    }

    fun part1(): String {
        while (!validPassword(oldPassword)) {
            increment(oldPassword)
        }
        return oldPassword.toString()
    }

}

fun main() {
    val sol = Day11("src/main/resources/2015/Day11Input.txt")
    println(sol.part1())
}

package aoc2015

import java.io.File

class Day11(filename: String) {
    private val oldPassword = File(filename).bufferedReader().readLine().toCharArray()

    private fun increment(a: CharArray, from: Int) {
        var i = a.size - from
        var newChar = ((a[i].code - 96) % 26 + 97).toChar()
        if (newChar == 'i' || newChar == 'o' || newChar == 'l') {
            newChar = ((newChar.code + 1).toChar())
        }
        a[i] = newChar
        while (i > 0 && a[i] == 'a') {
            i -= 1
            var newChar = ((a[i].code - 96) % 26 + 97).toChar()
            if (newChar == 'i' || newChar == 'o' || newChar == 'l') {
                newChar = ((newChar.code + 1).toChar())
            }
            a[i] = newChar
        }
    }


    private fun nextValidPassword(a: CharArray) {
        if (a.contains('i') || a.contains('o') || a.contains('l')) {
            val idx = a.indexOfFirst { "iol".contains(it) }
            a[idx] = (a[idx].code + 1).toChar()
            for (j in idx until a.size) {
                a[j] = 'a'
            }
        }
        val i = a.size - 5
        val expected = listOf(a[i], a[i], (a[i].code + 1).toChar(), (a[i].code + 2).toChar(), (a[i].code + 2).toChar())
        if (a[i] == 'y' || a[i] == 'z') {
            a[i] = 'a'
            a[i + 1] = 'a'
            a[i + 2] = 'b'
            a[i + 3] = 'c'
            a[i + 4] = 'c'

            increment(a, 6)
        } else if (a.joinToString("").substring(i, a.size) > expected.joinToString("")) {
            a[i] = (a[i].code + 1).toChar()
            nextValidPassword(a)
        } else if ("ghijklmno".contains(a[i])) {
            a[i] = 'p'
            a[i + 1] = 'p'
            a[i + 2] = 'q'
            a[i + 3] = 'r'
            a[i + 4] = 'r'
        } else {
            a[i + 1] = a[i]
            a[i + 2] = (a[i + 1].code + 1).toChar()
            a[i + 3] = (a[i + 2].code + 1).toChar()
            a[i + 4] = a[i + 3]
        }
    }

    fun part1(): String {
        nextValidPassword(oldPassword)
        return oldPassword.joinToString("")
    }

    fun part2(): String {
        increment(oldPassword, 1)
        nextValidPassword(oldPassword)
        return oldPassword.joinToString("")
    }

}

fun main() {
    val sol = Day11("src/main/resources/2015/Day11Input.txt")
    println(sol.part1())
    println(sol.part2())
}

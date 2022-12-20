package aoc2022

import java.io.File

class Day20(filename: String) {
    private val numbers = File(filename).bufferedReader().readLines().map { it.toLong() }

    private fun mix(l: List<Long>, n: Int): List<Long> {
        val numbersMoved = l.withIndex().map { it.value to it.index }.toMutableList()
        for (c in 1..n) {
            for (i in numbersMoved.indices) {
                val (idx, value) = numbersMoved.withIndex().first { it.value.second == i }
                numbersMoved.removeAt(idx)
                val newIdx = (((idx + value.first) % numbersMoved.size) + numbersMoved.size) % numbersMoved.size
                numbersMoved.add(newIdx.toInt(), value)
            }
        }
        return numbersMoved.map { it.first }
    }

    private fun giveCoord(l: List<Long>, n: Int): Long {
        val mixed = mix(l, n)
        val zero = mixed.indexOf(0)
        return mixed[(zero + 1000) % mixed.size] + mixed[(zero + 2000) % mixed.size] + mixed[(zero + 3000) % mixed.size]
    }

    fun part1(): Long =
        giveCoord(numbers, 1)

    fun part2(): Long =
        giveCoord(numbers.map { it * 811589153 }, 10)
}

fun main() {
    val sol = Day20("src/main/resources/2022/Day20Input.txt")
    println(sol.part1())
    println(sol.part2())
}

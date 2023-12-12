package aoc2023

import java.io.File

class Day12(val filename: String) {
    private val records = File(filename).bufferedReader().readLines()
        .map { it.split(" ") }
        .map { it.first() to it.last().split(",").map { c -> c.toInt() } }

    private val memo = mutableMapOf<Pair<String, List<Int>>, Long>()

    private fun arrangements(record: String, expected: List<Int>): Long {
        if (memo.contains(record to expected)) return memo[record to expected]!!

        var i = 0
        var li = 0
        var ans = 0L
        while (i < record.length && li < expected.size) {
            when (record[i]) {
                '.' -> i++
                '#' -> {
                    if (i + expected[li] <= record.length && record.substring(i, i + expected[li]).all { it != '.' }
                        && (i + expected[li] == record.length || record[i + expected[li]] != '#')) {
                        i += expected[li] + 1
                        li++
                    } else {
                        break
                    }
                }

                '?' -> {
                    if (i + expected[li] <= record.length && record.substring(i, i + expected[li]).all { it != '.' }
                        && (i + expected[li] == record.length || record[i + expected[li]] != '#')) {
                        ans += arrangements(
                            record.substring((i + expected[li] + 1).coerceAtMost(record.length)),
                            expected.subList(li + 1, expected.size)
                        )
                    }
                    i++
                }
            }
        }

        if (li >= expected.size) {
            if (i >= record.length || record.substring(i).all { it != '#' }) {
                ans += 1
            }
        }

        memo[record to expected] = ans

        return ans
    }

    fun part1(): Long {
        return records.sumOf { record ->
            arrangements(record.first, record.second)
        }
    }

    fun part2(): Long {
        return records.sumOf { record ->
            arrangements(
                IntRange(0, 4).joinToString("?") { record.first },
                IntRange(0, 4).flatMap { record.second })
        }
    }
}

fun main() {
    val sol = Day12("src/main/resources/2023/Day12Input.txt")
    println(sol.part1())
    println(sol.part2())
}


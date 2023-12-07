package aoc2023

import java.io.File

class Day07(val filename: String) {
    private val hands = File(filename).bufferedReader().readLines()
        .map { it.split(" ")[0] to it.split(" ")[1].toLong() }

    private val rankMap = mapOf(
        '2' to 0,
        '3' to 1,
        '4' to 2,
        '5' to 3,
        '6' to 4,
        '7' to 5,
        '8' to 6,
        '9' to 7,
        'T' to 8,
        'J' to 9,
        'Q' to 10,
        'K' to 11,
        'A' to 12,
    )

    private fun type(hand: String): Long = hand.map { c -> hand.count { c == it }.toLong() }.reduce(Long::times)

    private fun totalBets(cmp: Comparator<Pair<String, Long>>): Long {
        return hands.sortedWith(cmp).withIndex().sumOf {
            (it.index + 1) * it.value.second
        }
    }

    fun part1(): Long {
        return totalBets(compareBy<Pair<String, Long>> { type(it.first) }
            .thenBy { it.first.map { c -> '0' + rankMap[c]!! }.toString() }
        )
    }

    private val jokerRankMap = rankMap.toMutableMap() + ('J' to -1)

    private fun maxType(hand: String): Long {
        return jokerRankMap.keys.map {
            hand.replace('J', it)
        }.maxOf { type(it) }
    }

    fun part2(): Long {
        return totalBets(compareBy<Pair<String, Long>> { maxType(it.first) }
            .thenBy { it.first.map { c -> '0' + jokerRankMap[c]!! }.toString() }
        )
    }
}

fun main() {
    val sol = Day07("src/main/resources/2023/Day07Input.txt")
    println(sol.part1())
    println(sol.part2())
}

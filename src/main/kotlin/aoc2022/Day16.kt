package aoc2022

import java.io.File

class Day16(filename: String) {
    val lines = File(filename).bufferedReader().readLines()
    val flowRate = lines.associate {
        val words = it.split(" ")
        words[1] to words[4].substringAfter("=").substringBefore(";").toLong()
    }
    val leadsTo = lines.associate {
        val words = it.split(" ")
        it.split(" ")[1] to it.substringAfter("valves ").substringAfter("valve ").split(", ")
    }
    val memo = mutableMapOf<Triple<String, Long, Set<String>>, Long>()

    fun maxScore(valve: String, time: Long, opened: MutableSet<String>): Long {
        if (time <= 0.toLong()) {
            return 0
        }
        if (memo.contains(Triple(valve, time, opened.toSet()))) {
            return memo[Triple(valve, time, opened.toSet())]!!
        }

        var score: Long = 0
        if (!opened.contains(valve) && flowRate.getOrDefault(valve, Long.MIN_VALUE) != 0.toLong()) {
            opened.add(valve)
            score = score.coerceAtLeast(
                (time - 1) * flowRate.getOrDefault(valve, Long.MIN_VALUE) +
                        maxScore(valve, time - 1, opened)
            )
            opened.remove(valve)
        }
        for (nextValve in leadsTo.getOrDefault(valve, listOf())) {
            score = score.coerceAtLeast(
                maxScore(nextValve, time - 1, opened)
            )
        }
        memo[Triple(valve, time, opened.toSet())] = score
        return score
    }

    fun part1(): Long =
        maxScore("AA", 30, mutableSetOf())

    // works for example
    // timeout on real input
    val cmemo = mutableMapOf<Triple<String, Long, Set<String>>, Long>()

    fun maxScore3(valve: String, time: Long, opened: MutableSet<String>): Long {
        if (time <= 0.toLong()) {
            return maxScore("AA", 26, opened)
        }
        if (cmemo.contains(Triple(valve, time, opened.toSet()))) {
            return cmemo[Triple(valve, time, opened.toSet())]!!
        }

        var score: Long = 0
        if (!opened.contains(valve) && flowRate.getOrDefault(valve, Long.MIN_VALUE) != 0.toLong()) {
            opened.add(valve)
            score = score.coerceAtLeast(
                (time - 1) * flowRate.getOrDefault(valve, Long.MIN_VALUE) +
                        maxScore3(valve, time - 1, opened)
            )
            opened.remove(valve)
        }
        for (nextValve in leadsTo.getOrDefault(valve, listOf())) {
            score = score.coerceAtLeast(
                maxScore3(nextValve, time - 1, opened)
            )
        }
        cmemo[Triple(valve, time, opened.toSet())] = score
        return score
    }

    fun part2(): Long =
        maxScore3("AA", 26, mutableSetOf())
}

fun main() {
    val sol = Day16("src/main/resources/2022/Day16Input.txt")
    println(sol.part1())
    println(sol.part2())
}

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
        if (memo.contains(Triple(valve, time, opened))) {
            return memo[Triple(valve, time, opened)]!!
        }

        var score: Long = 0
        if (!opened.contains(valve) && flowRate.getOrDefault(valve, Long.MIN_VALUE) != 0.toLong()) {
            opened.add(valve)
            for (nextValve in leadsTo.getOrDefault(valve, listOf())) {
                score = score.coerceAtLeast(
                    (time - 1) * flowRate.getOrDefault(valve, Long.MIN_VALUE) +
                            maxScore(nextValve, time - 2, opened)
                )
            }
            opened.remove(valve)
        }
        for (nextValve in leadsTo.getOrDefault(valve, listOf())) {
                score = score.coerceAtLeast(
                    maxScore(nextValve, time - 1, opened)
                )
        }
        memo[Triple(valve, time, opened)] = score
        return score
    }

    fun part1(): Long {
        return maxScore("AA", 30, mutableSetOf<String>())
    }

}

fun main() {
    val sol = Day16("src/main/resources/2022/Day16Input.txt")
    println(sol.part1())
}

//    fun getValveScores(startValve: String, startTime: Long): Map<String, Pair<Long, List<String>>> {
//        val valveScores = mutableMapOf<String, Pair<Long, List<String>>>()
//        val visited = mutableSetOf<String>(startValve)
//        val queue = ArrayDeque<Triple<String, Long, List<String>>>()
//        queue.addLast(Triple(startValve, startTime, listOf<String>()))
//        while (queue.isNotEmpty()) {
//            val (valve, time, path) = queue.removeFirst()
//            val newPath = path.plus(valve)
//            val score = if (!opened.contains(valve)) (time- 1) * flowRate.getOrDefault(valve, Long.MIN_VALUE) else 0
//            valveScores[valve] = score to newPath
//
//            if (time > 1) {
//                for (nextValve in leadsTo.getOrDefault(valve, listOf())) {
//                    if (!visited.contains(nextValve)) {
//                        queue.addLast(Triple(nextValve, time - 1, newPath))
//                        visited.add(nextValve)
//                    }
//                }
//            }
//        }
//        return valveScores
//    }
//
//    fun simulate(startValve: String, startTime: Long): Long {
//        var total: Long = 0
//        var accumulated: Long = 0
//        var time = startTime
//        var valve = startValve
//        while (time > 0) {
//            total += accumulated
//            val scores = getValveScores(valve, time)
//            val best = scores.maxBy { it.value.first }
//            time -= 1
//            if (best.key == valve) {
//                accumulated += flowRate.getOrDefault(valve, Long.MIN_VALUE)
//                opened.add(valve)
//            }
//            else {
//                if (best.value.second.size > 1) {
//                    valve = best.value.second[1]
//                }
//            }
//        }
//        return total
//    }

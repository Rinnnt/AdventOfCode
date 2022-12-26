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

    val shortestDistance = getDistances()
    val leadsToUseful =
        leadsTo.filter { it.key == "AA" || flowRate[it.key] != 0.toLong() }
            .mapValues { (k, _) -> leadsTo.keys.filter { flowRate[it] != 0.toLong() && it != k } }

    fun getDistances(): Map<Pair<String, String>, Int> {
        val distances = mutableMapOf<Pair<String, String>, Int>()
        leadsTo.keys.filter { it == "AA" || flowRate[it] != 0.toLong() }.forEach { valve ->
            val queue = ArrayDeque<Pair<String, Int>>()
            queue.add(Pair(valve, 0))
            val visited = mutableSetOf(valve)

            while (queue.isNotEmpty()) {
                val (v, t) = queue.removeFirst()

                if (valve != v && flowRate[v] != 0.toLong()) {
                    distances[Pair(valve, v)] = t
                }

                for (nv in leadsTo[v]!!) {
                    if (!visited.contains(nv)) {
                        queue.addLast(Pair(nv, t + 1))
                        visited.add(nv)
                    }
                }
            }
        }
        return distances
    }

    val memo = mutableMapOf<Triple<String, Long, Set<String>>, Long>()
    fun maxScore(valve: String, time: Long, opened: MutableSet<String>): Long {
        if (memo.contains(Triple(valve, time, opened.toSet()))) {
            return memo[Triple(valve, time, opened.toSet())]!!
        }

        var score: Long = 0

        for (nextValve in leadsToUseful[valve]!!.filter { !opened.contains(it) }) {
            val requiredTime = shortestDistance[Pair(valve, nextValve)]!!
            if (requiredTime + 1 < time) {
                opened.add(nextValve)
                score = score.coerceAtLeast(
                    (time - requiredTime - 1) * flowRate[nextValve]!! +
                            maxScore(nextValve, time - requiredTime - 1, opened)
                )
                opened.remove(nextValve)
            }
        }
        memo[Triple(valve, time, opened.toSet())] = score
        return score
    }

    fun part1(): Long =
        maxScore("AA", 30, mutableSetOf())

    val cmemo = mutableMapOf<Triple<String, Long, Set<String>>, Long>()
    fun maxScore2(valve: String, time: Long, opened: MutableSet<String>): Long {
        if (cmemo.contains(Triple(valve, time, opened.toSet()))) {
            return cmemo[Triple(valve, time, opened.toSet())]!!
        }

        var score: Long = maxScore("AA", 26, opened)
        for (nextValve in leadsToUseful[valve]!!.filter { !opened.contains(it) }) {
            val requiredTime = shortestDistance[Pair(valve, nextValve)]!!
            if (requiredTime + 1 < time) {
                opened.add(nextValve)
                score = score.coerceAtLeast(
                    (time - requiredTime - 1) * flowRate[nextValve]!! +
                            maxScore2(nextValve, time - requiredTime - 1, opened)
                )
                opened.remove(nextValve)
            }
        }
        cmemo[Triple(valve, time, opened.toSet())] = score
        return score
    }

    fun part2(): Long =
        maxScore2("AA", 26, mutableSetOf())
}

fun main() {
    val sol = Day16("src/main/resources/2022/Day16Input.txt")
    println(sol.part1())
    println(sol.part2())
}

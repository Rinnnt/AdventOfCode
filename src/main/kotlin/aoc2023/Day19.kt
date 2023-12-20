package aoc2023

import java.io.File

class Day19(val filename: String) {
    private val rules = File(filename).bufferedReader().readLines().filter { !it.startsWith('{') }.associate {
        it.substringBefore('{') to it.substringAfter('{').substringBefore('}').split(',')
    }
    private val parts = File(filename).bufferedReader().readLines().filter { it.startsWith('{') }.map {
        it.substringAfter('{').substringBefore('}').split(",")
            .associate { it.substringBefore('=') to it.substringAfter('=').toInt() }
    }

    private fun applyRule(rule: String, part: Map<String, Int>): String {
        var cur = rule
        while (cur != "R" && cur != "A") {
            cur = rules[cur]!!.first {
                !it.contains(':')
                        || (it.contains('<') && part[it.substringBefore('<')]!! < it.substringAfter('<')
                    .substringBefore(':').toInt())
                        || (it.contains('>') && part[it.substringBefore('>')]!! > it.substringAfter('>')
                    .substringBefore(':').toInt())
            }.substringAfter(':')
        }
        return cur
    }

    fun part1(): Int {
        return parts.filter { applyRule("in", it) == "A" }.sumOf { it.values.sum() }
    }

    private fun count(rule: String, part: Map<String, Pair<Long, Long>>): Long {
        if (rule == "R") return 0
        if (rule == "A") {
            val ranges = part.values.map { it.second - it.first + 1 }
            if (ranges.any { it < 0 }) return 0
            return ranges.reduce(Long::times)
        }

        var cnt = 0L
        val filteredPart = part.toMutableMap()
        rules[rule]!!.forEach { cond ->
            if (!cond.contains(':')) {
                cnt += count(cond, filteredPart.toMap())
            }
            if (cond.contains('<')) {
                val acceptedPart = filteredPart.toMutableMap()
                acceptedPart[cond.substringBefore('<')] = acceptedPart[cond.substringBefore('<')]!!.first to
                        acceptedPart[cond.substringBefore('<')]!!.second.coerceAtMost(
                            cond.substringAfter('<').substringBefore(':').toLong() - 1
                        )
                cnt += count(cond.substringAfter(':'), acceptedPart.toMap())
                filteredPart[cond.substringBefore('<')] = filteredPart[cond.substringBefore('<')]!!.first.coerceAtLeast(
                    cond.substringAfter('<').substringBefore(':').toLong()
                ) to filteredPart[cond.substringBefore('<')]!!.second
            }
            if (cond.contains('>')) {
                val acceptedPart = filteredPart.toMutableMap()
                acceptedPart[cond.substringBefore('>')] = acceptedPart[cond.substringBefore('>')]!!.first.coerceAtLeast(
                    cond.substringAfter('>').substringBefore(':').toLong() + 1
                ) to acceptedPart[cond.substringBefore('>')]!!.second
                cnt += count(cond.substringAfter(':'), acceptedPart.toMap())
                filteredPart[cond.substringBefore('>')] =
                    filteredPart[cond.substringBefore('>')]!!.first to filteredPart[cond.substringBefore('>')]!!.second.coerceAtMost(
                        cond.substringAfter('>').substringBefore(':').toLong()
                    )
            }
        }
        return cnt
    }

    fun part2(): Long {
        return count("in", mapOf("x" to (1L to 4000), "m" to (1L to 4000), "a" to (1L to 4000), "s" to (1L to 4000)))
    }
}

fun main() {
    val sol = Day19("src/main/resources/2023/Day19Input.txt")
    println(sol.part1())
    println(sol.part2())
}

package aoc2022

import java.io.File
import kotlin.math.abs

class Day15(filename: String) {
    val lines = File(filename).bufferedReader().readLines()
    val sensorBeacons = lines.map {
        val words = it.split(" ").map { it.filter { it.isDigit() || it == '-' } }
        (words[2].toLong() to words[3].toLong()) to (words[8].toLong() to words[9].toLong())
    }
    val sensorDistances = sensorBeacons.map {
        it.first to (abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second))
    }

    fun cannotContainsRow(row: Long): Int {
        val points: MutableSet<Long> = mutableSetOf()
        sensorDistances.forEach {
            val dx = it.second - abs(row - it.first.second)
            for (x in -dx..dx) points.add(it.first.first + x)
        }
        points.removeIf {sensorBeacons.map {it.second}.contains(it to row)}
        return points.size
    }

    fun part1(): Int = cannotContainsRow(2_000_000)

    /**
     * Mind blowing solution
     * Since there is only one possible point that is not covered by the sensors
     * We just need to trace along the borders of the sensor's reach
     * and check if the point just out of reach of the sensor (distance + 1) is covered by any other sensor
     * if it is not covered by any other sensor, it must be the solution
     * * * Other points are redundant to search as it is contained within the sensor in consideration
     */
    fun cannotContains(): Long {
        for ((sensor, distance) in sensorDistances) {
            val newDist = distance + 1
            val rowStart = (sensor.first - newDist).coerceAtLeast(0)
            val rowEnd = (sensor.first + newDist).coerceAtMost(4_000_000)

            for (r in rowStart..rowEnd) {
                val left = (sensor.second - (newDist - abs(sensor.first - r))).coerceAtLeast(0)
                val right = (sensor.second + (newDist - abs(sensor.first - r))).coerceAtMost(4_000_000)
                if (sensorDistances.all { abs(left - it.first.first) + abs(r - it.first.second) > it.second })
                    return left * 4_000_000 + r
                if (sensorDistances.all { abs(right - it.first.first) + abs(r - it.first.second) > it.second })
                    return right * 4_000_000 + r
            }
        }
        return -1
    }

    fun part2(): Long = cannotContains()
}

fun main() {
    val sol = Day15("src/main/resources/2022/Day15Input.txt")
    println(sol.part1())
    println(sol.part2())
}

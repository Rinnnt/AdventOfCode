package aoc2023

import java.io.File

class Day24(val filename: String) {

    data class Hail(val px: Long, val py: Long, val pz: Long, val vx: Long, val vy: Long, val vz: Long)

    private val hailRegex = Regex("(-?\\d+),\\s+(-?\\d+),\\s+(-?\\d+) @\\s+(-?\\d+),\\s+(-?\\d+),\\s+(-?\\d+)")
    private val hails = File(filename).bufferedReader().readLines().map {
        val values = hailRegex.matchEntire(it)!!.groupValues.subList(1, 7).map(String::toLong)
        Hail(values[0], values[1], values[2], values[3], values[4], values[5])
    }

    fun part1(): Int {
        var intersections = 0
        for (i in hails.indices) {
            for (j in i + 1 until hails.size) {
                val ax = hails[i].px
                val ay = hails[i].py
                val am = hails[i].vy / hails[i].vx.toDouble()
                val bx = hails[j].px
                val by = hails[j].py
                val bm = hails[j].vy / hails[j].vx.toDouble()

                if (am == bm) continue

                val x = (am * ax - ay - bm * bx + by) / (am - bm)
                val y = (am * bm * bx - am * bm * ax + bm * ay - am * by) / (bm - am)

                if (x in 2.0E14..4.0E14 && y in 2.0E14..4.0E14) {
                    if ((x - ax) * hails[i].vx > 0 && (x - bx) * hails[j].vx > 0) { // check intersection time > 0
                        intersections++
                    }
                }
            }
        }
        return intersections
    }

    fun part2(): Int {
        // to run in z3 using python
        for (i in 0..2) {
            println("px + t$i * vx == ${hails[i].px} + t$i * ${hails[i].vx}")
            println("py + t$i * vy == ${hails[i].py} + t$i * ${hails[i].vy}")
            println("pz + t$i * vz == ${hails[i].pz} + t$i * ${hails[i].vz}")
        }
        return 0
    }
}

fun main() {
    val sol = Day24("src/main/resources/2023/Day24Input.txt")
    println(sol.part1())
    println(sol.part2())
}

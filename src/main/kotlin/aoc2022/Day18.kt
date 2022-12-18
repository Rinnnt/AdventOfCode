package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

data class Coord(val x: Int, val y: Int, val z: Int)

val adjacentCoords = listOf(
    Triple(-1, 0, 0),
    Triple(+1, 0, 0),
    Triple(0, -1, 0),
    Triple(0, +1, 0),
    Triple(0, 0, -1),
    Triple(0, 0, +1)
)

class Day18(filename: String) {
    private val cubes = File(filename).bufferedReader().readLines().map {
        it.split(",").map { it.toInt() }
    }.map {
        Coord(it[0], it[1], it[2])
    }.toSet()


    private fun findSurfaceArea(cubes: Set<Coord>): Int =
        cubes.sumOf { pos ->
            adjacentCoords.map { Coord(pos.x + it.first, pos.y + it.second, pos.z + it.third) }
                .filter { !cubes.contains(it) }.size
        }

    fun part1(): Int = findSurfaceArea(cubes)

    val xRange = cubes.minOf { it.x } - 1..cubes.maxOf { it.x } + 1
    val yRange = cubes.minOf { it.y } - 1..cubes.maxOf { it.y } + 1
    val zRange = cubes.minOf { it.z } - 1..cubes.maxOf { it.z } + 1

    private val invertedCubes = mutableSetOf<Coord>()

    fun inverseFloodFill(start: Coord) {
        val queue = ArrayDeque<Coord>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val cube = queue.removeFirst()
            adjacentCoords.map {
                Coord(cube.x + it.first, cube.y + it.second, cube.z + it.third)
            }.filter {
                it.x in xRange && it.y in yRange && it.z in zRange && !invertedCubes.contains(it) && !cubes.contains(it)
            }.forEach {
                invertedCubes.add(it)
                queue.add(it)
            }
        }
    }

    fun inverseCubeSurfaceArea(): Int {
        val xEdge = xRange.last - xRange.first + 1
        val yEdge = yRange.last - yRange.first + 1
        val zEdge = zRange.last - zRange.first + 1
        return 2 * (xEdge * yEdge + yEdge * zEdge + zEdge * xEdge)
    }

    fun part2(): Int {
        inverseFloodFill(Coord(xRange.first, yRange.first, zRange.first))
        return findSurfaceArea(invertedCubes) - inverseCubeSurfaceArea()
    }
}

fun main() {
    val sol = Day18("src/main/resources/2022/Day18Input.txt")
    println(sol.part1())
    println(sol.part2())
}

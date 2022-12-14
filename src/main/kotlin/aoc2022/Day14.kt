package aoc2022

import java.io.File

class Day14(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val rocks = lines.map { it.split(" -> ").map { it.split(",") }.map { it[0].toInt() to it[1].toInt() } }
    val minX = rocks.flatten().minOf {it.first}
    val maxX = rocks.flatten().maxOf {it.first}
    val minY = rocks.flatten().minOf {it.second}
    private val maxY = rocks.flatten().maxOf {it.second}
    private val cave1 = List<MutableList<Char>>(maxY + 2) { MutableList<Char>(550) {'.'} }

    private fun fillCave(cave: List<MutableList<Char>>) {
        for (rock in rocks) {
            for (i in 0 until rock.size - 1) {
                if (rock[i].first == rock[i+1].first) {
                    var y = rock[i].second
                    cave[y][rock[i].first] = '#'
                    val dir = if (rock[i].second < rock[i+1].second) 1 else -1
                    while (y != rock[i+1].second) {
                        y += dir
                        cave[y][rock[i].first] = '#'
                    }
                }
                else {
                    var x = rock[i].first
                    cave[rock[i].second][x] = '#'
                    val dir = if (rock[i].first < rock[i+1].first) 1 else -1
                    while (x != rock[i+1].first) {
                        x += dir
                        cave[rock[i].second][x] = '#'
                    }
                }
            }
        }
    }

    private fun simulateSand(cave: List<MutableList<Char>>): Int {
        var sand = 0
        var rest = true
        while (rest) {
            var newSand = 0 to 500
            var canMove = true
            while (canMove) {
                if (cave[newSand.first + 1][newSand.second] == '.') {
                    newSand = newSand.first + 1 to newSand.second
                    if (newSand.first > maxY) {
                        rest = false
                        break
                    }
                }
                else if (cave[newSand.first + 1][newSand.second - 1] == '.') {
                    newSand = newSand.first + 1 to newSand.second - 1
                }
                else if (cave[newSand.first + 1][newSand.second + 1] == '.') {
                    newSand = newSand.first + 1 to newSand.second + 1
                }
                else {
                    cave[newSand.first][newSand.second] = 'o'
                    canMove = false
                }
            }
            if (rest) sand++
        }
        return sand
    }

    fun part1(): Int {
        fillCave(cave1)
        return simulateSand(cave1)
    }

    private val cave2 = List<MutableList<Char>>(maxY + 3) { MutableList<Char>(1000) {'.'} }

    private fun fillCave2() {
        fillCave(cave2)
        cave2[maxY + 2].replaceAll { '#' }
    }

    private fun simulateSand2(cave: List<MutableList<Char>>): Int {
        var sand = 0
        var rest = true
        while (cave[0][500] == '.') {
            var newSand = 0 to 500
            var canMove = true
            while (canMove) {
                if (cave[newSand.first + 1][newSand.second] == '.') {
                    newSand = newSand.first + 1 to newSand.second
                }
                else if (cave[newSand.first + 1][newSand.second - 1] == '.') {
                    newSand = newSand.first + 1 to newSand.second - 1
                }
                else if (cave[newSand.first + 1][newSand.second + 1] == '.') {
                    newSand = newSand.first + 1 to newSand.second + 1
                }
                else {
                    cave[newSand.first][newSand.second] = 'o'
                    canMove = false
                }
            }
            sand++
        }
        return sand
    }
    fun part2(): Int {
        fillCave2()
        return simulateSand2(cave2)
    }

}

fun main() {
    val sol = Day14("src/main/resources/2022/Day14Input.txt")
    println(sol.part1())
    println(sol.part2())
}

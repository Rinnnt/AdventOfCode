package aoc2022

import java.io.File
import kotlin.math.abs

data class Pos(val x: Int, val y: Int) {
    fun translate(dx: Int, dy: Int): Pos =
        Pos(x + dx, y + dy)

    fun touching(other: Pos): Boolean =
        abs(x - other.x) <= 1 && abs(y - other.y) <= 1
}

enum class Dir(val dx: Int, val dy: Int) {
    U(0, 1),
    D(0, -1),
    L(-1, 0),
    R(1, 0)
}

class Day9(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private var head = Pos(0, 0)
    private var tail = Pos(0, 0)
    private val visited = mutableSetOf(Pos(0, 0))

    private fun parseMove(line: String): Pair<Dir, Int> {
        val words = line.split(" ")
        return Dir.valueOf(words[0]) to words[1].toInt()
    }

    private fun move(p: Pair<Dir, Int>) {
        for (i in 1..p.second) {
            val prev = head.copy()
            head = head.translate(p.first.dx, p.first.dy)
            if (!head.touching(tail)) {
                tail = prev
            }
            visited.add(tail)
        }
    }

    fun part1(): Int {
        lines.forEach { move(parseMove(it)) }
        return visited.size
    }

    private val knots = MutableList<Pos>(10) { Pos(0, 0) }
    private val endVisited = mutableSetOf(Pos(0, 0))

    private fun move2(p: Pair<Dir, Int>) {
        for (i in 1..p.second) {
            knots[0] = knots[0].translate(p.first.dx, p.first.dy)
            for (j in 0 until knots.size - 1) {
                if (!knots[j].touching(knots[j + 1])) {
                    val dx: Int = when {
                        knots[j].x > knots[j + 1].x -> 1
                        knots[j].x < knots[j + 1].x -> -1
                        else -> 0
                    }
                    val dy: Int = when {
                        knots[j].y > knots[j + 1].y -> 1
                        knots[j].y < knots[j + 1].y -> -1
                        else -> 0
                    }
                    knots[j + 1] = knots[j + 1].translate(dx, dy)
                }
            }
            endVisited.add(knots.last())
        }
    }

    fun printKnots() {
        val minX = knots.minOf { it.x }
        val maxX = knots.maxOf { it.x }
        val minY = knots.minOf { it.y }
        val maxY = knots.maxOf { it.y }

        for (i in maxY downTo minY) {
            for (j in minX..maxX) {
                print(knots.withIndex().firstOrNull { it.value.x == j && it.value.y == i }?.index ?: '.')
                print(' ')
            }
            print("\n")
        }
        println()
    }

    fun part2(): Int {
        lines.forEach { move2(parseMove(it)) }
        return endVisited.size
    }
}

fun main() {
    val sol = Day9("src/main/resources/2022/Day09Input.txt")
    println(sol.part1())
    println(sol.part2())
}

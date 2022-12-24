package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

class Day24(filename: String) {
    private val directions = listOf(Position(1, 0), Position(0, 1), Position(-1, 0), Position(0, -1), Position(0, 0))
    private val blizzardDirection = mapOf('v' to 0, '>' to 1, '^' to 2, '<' to 3)

    val lines = File(filename).bufferedReader().readLines()
    private val blizzards = lines.withIndex().map { row ->
        row.value.withIndex().filter { blizzardDirection.contains(it.value) }
            .map { Pair(Position(row.index - 1, it.index - 1), directions[blizzardDirection[it.value]!!]) }
    }.flatten().toSet()

    private val rowSize = lines.size - 2
    private val colSize = lines.first().length - 2
    private var start = Position(-1, lines.first().indexOf('.') - 1)
    private var end = Position(rowSize, lines.last().indexOf('.') - 1)

    private fun moveBlizzards(bs: Set<Pair<Position, Position>>): Set<Pair<Position, Position>> {
        val tmp = mutableSetOf<Pair<Position, Position>>()
        bs.forEach { b ->
            val newPos =
                Position((b.first.x + b.second.x + rowSize) % rowSize, (b.first.y + b.second.y + colSize) % colSize)
            tmp.add(Pair(newPos, b.second))
        }
        return tmp
    }

    /*
    bfs with each state as (position, blizzards, time) and check each direction and add new states is too slow
    Improve search with each state as (all reachable positions, blizzard state, time) still slow but better
     */
    private fun search(bzs: Set<Pair<Position, Position>>): Pair<Int, Set<Pair<Position, Position>>> {
        val memo = mutableMapOf<Set<Pair<Position, Position>>, Set<Pair<Position, Position>>>()
        var pos = setOf(start)
        var bs = bzs
        var step = 0

        while (!pos.contains(end)) {
            if (memo.contains(bs)) {
                bs = memo[bs]!!
            } else {
                val tmp = bs
                bs = moveBlizzards(bs)
                memo[tmp] = bs
            }

            val newPos = mutableSetOf<Position>()
            pos.forEach { p ->
                directions.forEach { d ->
                    val newP = Position(p.x + d.x, p.y + d.y)
                    if ((newP.x in 0 until rowSize && newP.y in 0 until colSize) || (newP == start) || (newP == end)) {
                        if (!bs.map { it.first }.contains(newP)) {
                            newPos.add(newP)
                        }
                    }
                }
            }

            pos = newPos
            step++
        }
        return step to bs
    }

    fun part1(): Int =
        search(blizzards).first

    private fun switchStartEnd() {
        val tmp = start
        start = end
        end = tmp
    }

    fun part2(): Int {
        val (trip1, b1) = search(blizzards)
        switchStartEnd()
        val (trip2, b2) = search(b1)
        switchStartEnd()
        val (trip3, b3) = search(b2)
        return trip1 + trip2 + trip3
    }
}

fun main() {
    val sol = Day24("src/main/resources/2022/Day24Input.txt")
    // 22.03 seconds
    val time = measureTimeMillis {
        println(sol.part1())
    }
    println("part1: ${time / 1000.0} seconds")
    // 60.572 seconds
    val time2 = measureTimeMillis {
        println(sol.part2())
    }
    println("part2: ${time2 / 1000.0} seconds")
}

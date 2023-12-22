package aoc2023

import java.io.File

class Day22(val filename: String) {

    data class Pos(val x: Int, val y: Int, val z: Int)

    private val brickRegex = Regex("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)")
    private val bricks = File(filename).bufferedReader().readLines().map {
        val groups = brickRegex.matchEntire(it)!!.groupValues.subList(1, 7).map(String::toInt)
        Pos(groups[0], groups[1], groups[2]) to Pos(groups[3], groups[4], groups[5])
    }

    private fun settle(bricks: List<Pair<Pos, Pos>>): Pair<Map<Pos, Int>, Map<Int, List<Pos>>> {
        val settled = mutableMapOf<Pos, Int>()
        val pos = mutableMapOf<Int, MutableList<Pos>>()
        bricks.withIndex().sortedBy { it.value.first.z }.forEach { v ->
            val idx = v.index
            val brick = v.value
            var z = brick.first.z

            while (z > 1 &&
                IntRange(brick.first.x, brick.second.x).all { x ->
                    IntRange(brick.first.y, brick.second.y).all { y ->
                        !settled.contains(Pos(x, y, z - 1))
                    }
                }
            ) z--

            pos += idx to mutableListOf()
            IntRange(brick.first.x, brick.second.x).forEach { x ->
                IntRange(brick.first.y, brick.second.y).forEach { y ->
                    IntRange(0, brick.second.z - brick.first.z).forEach { dz ->
                        settled += Pos(x, y, z + dz) to idx
                        pos[idx]!!.add(Pos(x, y, z + dz))
                    }
                }
            }
        }

        return settled to pos
    }

    private val settled = settle(bricks).first
    private val pos = settle(bricks).second

    private fun fallsIfRemoved(idx: Int, removedBricks: Set<Int>): Pair<Int, Set<Int>> {
        var removed = removedBricks.toMutableSet()
        var ans = 0
        removed.add(idx)

        pos[idx]!!.mapNotNull { settled[Pos(it.x, it.y, it.z + 1)] }
            .filter { !removed.contains(it) }
            .toSet()
            .filter { sup ->
                pos[sup]!!.mapNotNull { settled[Pos(it.x, it.y, it.z - 1)] }
                    .filter { it != sup && !removed.contains(it) }
                    .toSet()
                    .isEmpty()
            }.forEach { r ->
                val (cnt, rems) = fallsIfRemoved(r, removed)
                removed = rems.toMutableSet()
                ans += cnt + 1
            }

        return ans to removed
    }

    fun part1(): Int = pos.count { fallsIfRemoved(it.key, setOf()).first == 0 }

    fun part2(): Int = pos.keys.sumOf { fallsIfRemoved(it, setOf()).first }
}

fun main() {
    val sol = Day22("src/main/resources/2023/Day22Input.txt")
    println(sol.part1())
    println(sol.part2())
}

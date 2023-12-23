package aoc2023

import java.io.File
import kotlin.math.max

class Day23(val filename: String) {

    private val map = File(filename).bufferedReader().readLines()
    private val start = 0 to map[0].indexOf('.')
    private val end = map.size - 1 to map[map.size - 1].indexOf('.')

    private val dxys = listOf(
        0 to 1, 0 to -1, 1 to 0, -1 to 0
    )
    private val invert = listOf(1, 0, 3, 2)
    private val moves = mapOf(
        '.' to listOf(0, 1, 2, 3),
        '>' to listOf(0),
        '<' to listOf(1),
        'v' to listOf(2),
        '^' to listOf(3)
    )

    private fun inBounds(x: Int, y: Int): Boolean = map.indices.contains(x) && map[x].indices.contains(y)

    fun part1(): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        var longest = 0
        fun dfs(cur: Pair<Int, Int>, steps: Int) {
            if (cur == end) {
                longest = max(longest, steps)
                return
            }

            val x = cur.first
            val y = cur.second

            dxys.indices.filter { moves[map[x][y]]!!.contains(it) }.forEach {
                val nx = x + dxys[it].first
                val ny = y + dxys[it].second
                if (inBounds(nx, ny) && !visited.contains(nx to ny) && map[nx][ny] != '#') {
                    visited.add(nx to ny)
                    dfs(nx to ny, steps + 1)
                    visited.remove(nx to ny)
                }
            }
        }

        dfs(start, 0)
        return longest
    }

    private fun isNode(x: Int, y: Int): Boolean =
        x to y == start || x to y == end || dxys.map { x + it.first to y + it.second }
            .filter { inBounds(it.first, it.second) && map[it.first][it.second] != '#' }.size > 2

    private fun findNode(x: Int, y: Int, prev: Int, steps: Int): Pair<Pair<Int, Int>, Int> {
        if (isNode(x, y)) return (x to y) to steps

        val dxy = dxys.withIndex().first {
            val nx = x + it.value.first
            val ny = y + it.value.second
            it.index != prev && inBounds(nx, ny) && map[nx][ny] != '#'
        }
        return findNode(x + dxy.value.first, y + dxy.value.second, invert[dxy.index], steps + 1)
    }


    private fun compress(): Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>> {
        val compressed = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Pair<Int, Int>, Int>>>()
        map.indices.forEach { x ->
            map[x].indices.forEach { y ->
                if (isNode(x, y)) {
                    compressed[x to y] = mutableListOf()

                    dxys.withIndex().forEach {
                        val nx = x + it.value.first
                        val ny = y + it.value.second
                        if (inBounds(nx, ny) && map[nx][ny] != '#') {
                            compressed[x to y]!!.add(findNode(nx, ny, invert[it.index], 1))
                        }
                    }
                }
            }
        }
        return compressed
    }

    fun part2(): Int {
        val compressed = compress()
        val visited = mutableSetOf<Pair<Int, Int>>()
        var longest = 0
        fun dfs(cur: Pair<Int, Int>, steps: Int) {
            visited.add(cur)
            if (cur == end) {
                longest = max(longest, steps)
                visited.remove(cur)
                return
            }

            compressed[cur]!!.forEach {
                if (!visited.contains(it.first)) {
                    dfs(it.first, steps + it.second)
                }
            }

            visited.remove(cur)
        }

        dfs(start, 0)
        return longest
    }
}

fun main() {
    val sol = Day23("src/main/resources/2023/Day23Input.txt")
    println(sol.part1())
    println(sol.part2())
}

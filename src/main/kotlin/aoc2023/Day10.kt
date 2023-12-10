package aoc2023

import java.io.File

class Day10(val filename: String) {
    private val board = File(filename).bufferedReader().readLines()

    private val dxys = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    private val rev = listOf(1, 0, 3, 2)
    private val pipes = listOf(
        listOf('|', '7', 'F'),
        listOf('|', 'J', 'L'),
        listOf('-', 'F', 'L'),
        listOf('-', 'J', '7')
    )
    private val dirs = mutableMapOf(
        '|' to listOf(0, 1),
        '-' to listOf(2, 3),
        '7' to listOf(1, 2),
        'F' to listOf(1, 3),
        'J' to listOf(0, 2),
        'L' to listOf(0, 3),
    )

    private fun inBounds(x: Int, y: Int): Boolean = x >= 0 && x < board.size && y >= 0 && y < board[x].length

    private val start = findStart()

    private fun findStart(): Pair<Pair<Int, Int>, Char> {
        val startRow = board.withIndex().find { it.value.contains('S') }!!.index
        val startIdx = startRow to board[startRow].indexOf('S')
        val startSymbols = dxys.withIndex().filter { dxy ->
            val nx = startIdx.first + dxy.value.first
            val ny = startIdx.second + dxy.value.second
            inBounds(nx, ny) && pipes[dxy.index].contains(board[nx][ny])
        }.map { pipes[rev[it.index]] }
        val startSymbol = startSymbols[0].intersect(startSymbols[1].toSet()).first()
        dirs += 'S' to dirs[startSymbol]!!
        return startIdx to startSymbol
    }

    private val loopSet = findLoop()

    private fun findLoop(): MutableSet<Pair<Int, Int>> {
        val lset = mutableSetOf<Pair<Int, Int>>()
        var cur = start.first
        var prev = -1

        do {
            val dxy = dirs[board[cur.first][cur.second]]!!.find { it != prev }!!
            cur = cur.first + dxys[dxy].first to cur.second + dxys[dxy].second
            prev = rev[dxy]
            lset.add(cur)
        } while (cur != start.first)
        return lset
    }

    fun part1(): Int = loopSet.size / 2

    fun part2(): Int {
        var area = 0
        for (i in 0 until board.size) {
            for (j in 0 until board[i].length) {
                if (loopSet.contains(i to j)) continue
                var edges = 0
                var prev = -1
                for (k in j + 1 until board[i].length) {
                    if (!loopSet.contains(i to k)) continue
                    when (board[i][k]) {
                        '|' -> edges++
                        'L' -> prev = 0
                        'F' -> prev = 1
                        'J' -> if (prev == 1) edges++ else prev = -1
                        '7' -> if (prev == 0) edges++ else prev = -1
                    }
                }
                if (edges % 2 == 1) area++
            }
        }
        return area
    }
}

fun main() {
    val sol = Day10("src/main/resources/2023/Day10Input.txt")
    println(sol.part1())
    println(sol.part2())
}

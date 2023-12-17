package aoc2023

import java.io.File

class Day17(val filename: String) {
    private val grid = File(filename).bufferedReader().readLines().map { row -> row.map { it.toString().toInt() } }

    private val dxys = listOf(
        -1 to 0, 1 to 0, 0 to -1, 0 to 1
    )

    private fun inBounds(x: Int, y: Int): Boolean =
        x >= 0 && x < grid.size && y >= 0 && y < grid[0].size

    private fun solve(minMoves: Int, maxMoves: Int): Long {
        val q = ArrayDeque<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        val dist = List(grid.size) {
            List(grid[0].size) {
                List(4) { MutableList<Long>(maxMoves + 1) { 1000000000000000L } }
            }
        }
        q.addLast((0 to 0) to (1 to maxMoves))
        q.addLast((0 to 0) to (3 to maxMoves))
        dist[0][0][1][maxMoves] = 0
        dist[0][0][3][maxMoves] = 0

        while (!q.isEmpty()) {
            val (cur, moves) = q.removeFirst()
            val (x, y) = cur
            val (prev, left) = moves

            dxys.withIndex().forEach {
                val (idx, dxy) = it
                if (idx == prev && left > 0) {
                    val nx = x + dxy.first
                    val ny = y + dxy.second
                    if (inBounds(nx, ny) && dist[nx][ny][idx][left - 1] > dist[x][y][prev][left] + grid[nx][ny]) {
                        dist[nx][ny][idx][left - 1] = dist[x][y][prev][left] + grid[nx][ny]
                        q.addLast((nx to ny) to (idx to left - 1))
                    }
                }

                if (dxy.first != dxys[prev].first && dxy.second != dxys[prev].second && left <= maxMoves - minMoves) {
                    val nx = x + dxy.first
                    val ny = y + dxy.second
                    if (inBounds(nx, ny) && dist[nx][ny][idx][maxMoves - 1] > dist[x][y][prev][left] + grid[nx][ny]) {
                        dist[nx][ny][idx][maxMoves - 1] = dist[x][y][prev][left] + grid[nx][ny]
                        q.addLast((nx to ny) to (idx to maxMoves - 1))
                    }
                }
            }
        }

        return dist[grid.size - 1][grid[0].size - 1].minOf { ds -> ds.minOf { it } }
    }

    fun part1(): Long = solve(0, 3)

    fun part2(): Long = solve(4, 10)
}

fun main() {
    val sol = Day17("src/main/resources/2023/Day17Input.txt")
    println(sol.part1())
    println(sol.part2())
}

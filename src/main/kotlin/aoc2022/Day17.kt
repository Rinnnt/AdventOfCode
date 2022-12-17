package aoc2022

import java.io.File

/**
 * All possible rocks with their shape described by
 * a list of (dx, dy) pairs for each piece of their shape
 * that stores the separation of that piece from the bottom left corner
 */
enum class Rocks(val pieces: List<Pair<Int, Int>>) {
    HORIZONTAL(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)),
    CROSS(listOf(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2)),
    REVERSEL(listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2)),
    VERTICAL(listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3)),
    SQUARE(listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1))
}

class Day17(filename : String) {
    /**
     * jetStream contains the left and right pushes
     *
     * occupied is a set that stores the points on the grid that are
     * occupied by any settled rocks
     */
    private val jetStream: String = File(filename).bufferedReader().readLine()
//    private val jetStream: String = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    private val occupied = mutableSetOf<Pair<Long, Long>>()
    private val widthRange = 0 until 7

    /**
     * Check if the rock position is valid
     */
    private fun isNotValid(p: Pair<Long, Long>, rock: Rocks): Boolean {
        if (rock.pieces.all { it.first + p.first in widthRange }) {
            if (rock.pieces.all { !occupied.contains(it.first + p.first to it.second + p.second) }) {
                return false
            }
        }
        return true
    }

    /**
     * Reset the occupied positions for every new simulation
     */
    private fun resetOccupied() {
        occupied.clear()
        occupied.addAll(listOf(0.toLong() to 0.toLong(), 1.toLong() to 0.toLong(), 2.toLong() to 0.toLong(), 3.toLong() to 0.toLong(), 4.toLong() to 0.toLong(), 5.toLong() to 0.toLong(), 6.toLong() to 0.toLong()))
    }

    /**
     * Returns a pair of
     * * final height of the tower
     * * list of height increases in each step of rock
     */
    private fun simulateIncreases(n: Long): Pair<Long, MutableList<Long>> {
        resetOccupied()
        var height: Long = 0
        var prev: Long = 0
        var jet = 0
        val increases: MutableList<Long> = mutableListOf()

        for (i in 0 until n) {
            val rock = Rocks.values()[i.toInt() % Rocks.values().size]
            var bottomLeft = 2.toLong() to height + 4

            /**
             * First three steps down is guaranteed not to overlap
             */
            for (j in 1..3) {
                if (jetStream[jet] == '<') {
                    // and if it can be pushed left
                    if (!isNotValid(bottomLeft.first - 1 to bottomLeft.second, rock)) {
                        // push left
                        bottomLeft = bottomLeft.first - 1 to bottomLeft.second
                    }
                }
                // if jetStream is to the right
                if (jetStream[jet] == '>') {
                    // and if it can be pushed right
                    if (!isNotValid(bottomLeft.first + 1 to bottomLeft.second, rock)) {
                        // push right
                        bottomLeft = bottomLeft.first + 1 to bottomLeft.second
                    }
                }

                // drop down one height
                bottomLeft = bottomLeft.first to bottomLeft.second - 1
               // update jet
                jet = (jet + 1) % jetStream.length
            }

            /**
             * While it can continue to step down
             */
            while (!isNotValid(bottomLeft, rock)) {

                // if jetStream is to the left
                if (jetStream[jet] == '<') {
                    // and if it can be pushed left
                    if (!isNotValid(bottomLeft.first - 1 to bottomLeft.second, rock)) {
                        // push left
                        bottomLeft = bottomLeft.first - 1 to bottomLeft.second
                    }
                }
                // if jetStream is to the right
                if (jetStream[jet] == '>') {
                    // and if it can be pushed right
                    if (!isNotValid(bottomLeft.first + 1 to bottomLeft.second, rock)) {
                        // push right
                        bottomLeft = bottomLeft.first + 1 to bottomLeft.second
                    }
                }

                // drop down one height
                bottomLeft = bottomLeft.first to bottomLeft.second - 1
               // update jet
                jet = (jet + 1) % jetStream.length
            }

            // revert last drop down (since it is overlapping to break the while loop)
            bottomLeft = bottomLeft.first to bottomLeft.second + 1
            // update occupied positions
            rock.pieces.forEach { occupied.add(it.first + bottomLeft.first to it.second + bottomLeft.second) }
            // update new height
            height = height.coerceAtLeast(rock.pieces.maxOf { it.second + bottomLeft.second })
            increases.add(height - prev)
            prev = height
        }
        return height to increases
    }

    fun part1(): Long =
        simulateIncreases(2022).first

    /**
     *  Search for the repeating pattern in the increase of tower height
     *  the period is the number of rocks in the cycle
     *
     *  If such cycle exists, it does not have to be the same at the start
     *  since the search finds cycles that start at floor of the tower,
     *  whereas subsequent cycles start building on top of previous cycle's end (which may not be a flat floor)
     *  Also, the end might not be the same at end as period might not be a divisor of 10000
     *  hence we search for cycles that give 3 distinct chunks
     *
     *  Not guaranteed to be the shortest cycle.
     *  Assumes cycle period is much smaller than 100000
     */
    private fun findPattern(): Pair<Long, Long> {
        val increases: MutableList<Long> = simulateIncreases(100000).second

        for (period in 1..100000) {
            if (increases.chunked(period).distinct().size < 4) {
                println("cycle period (no. of rocks) is $period, height increase per cycle: ${simulateIncreases(period.toLong() * 2).first - simulateIncreases(period.toLong()).first}")
                println("Height increase for first cycle: ${simulateIncreases(period.toLong()).first}")
                return Pair(period.toLong(), simulateIncreases(period.toLong() * 2).first - simulateIncreases(period.toLong()).first)
            }
        }
        return Pair(-1, -1)
    }

    fun part2(): Long {
        val (period, increase) = findPattern()
        return (1_000_000_000_000 / period - 1) * increase + simulateIncreases(period + (1_000_000_000_000 % period)).first
    }
}

fun main() {
    val sol = Day17("src/main/resources/2022/Day17Input.txt")
    println(sol.part1())
    println(sol.part2())
}

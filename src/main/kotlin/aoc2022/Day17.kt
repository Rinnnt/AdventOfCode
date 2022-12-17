package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

/**
 * All possible rocks with their shape described by
 * a list of (dx, dy) pairs for each piece of their shape
 * that stores the separation of that piece from the bottom left corner
 */
enum class Rocks(val pieces: List<Pair<Int, Int>>) {
    HORIZONTAL(
        listOf(
            0 to 0,
            1 to 0,
            2 to 0,
            3 to 0
        )
    ),
    CROSS(
        listOf(
            1 to 0,
            0 to 1,
            1 to 1,
            2 to 1,
            1 to 2
        )
    ),
    REVERSEL(
        listOf(
            0 to 0,
            1 to 0,
            2 to 0,
            2 to 1,
            2 to 2
        )
    ),
    VERTICAL(
        listOf(
            0 to 0,
            0 to 1,
            0 to 2,
            0 to 3
        )
    ),
    SQUARE(
        listOf(
            0 to 0,
            1 to 0,
            0 to 1,
            1 to 1
        )
    )
}

class Day17(filename : String) {
    /**
     * line stores the left and right jet streams
     *
     * occupied is a map that stores the points on the grid that are
     * occupied by any settled rocks
     */
    val jetStream: String = File(filename).bufferedReader().readLine()
    private val occupied = mutableSetOf<Pair<Long, Long>>(
        0.toLong() to 0.toLong(),
        1.toLong() to 0.toLong(),
        2.toLong() to 0.toLong(),
        3.toLong() to 0.toLong(),
        4.toLong() to 0.toLong(),
        5.toLong() to 0.toLong(),
        6.toLong() to 0.toLong(),
    )
    private val widthRange = 0 until 7

    private fun isNotValid(p: Pair<Long, Long>, rock: Rocks): Boolean {
        if (rock.pieces.all { it.first + p.first in widthRange }) {
            if (rock.pieces.all { !occupied.contains(it.first + p.first to it.second + p.second) }) {
                return false
            }
        }
        return true
    }

    private fun simulate(n: Long): Long {
        var height: Long = 0
        var jet: Int = 0

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
        }

        return height
    }


    fun part1(): Long =
        simulate(2022)


    private val occupied2 = mutableMapOf<Long, MutableSet<Long>>(
        0.toLong() to mutableSetOf(0, 1, 2, 3, 4, 5, 6)
    )

    private fun isNotValid2(p: Pair<Long, Long>, rock: Rocks): Boolean {
        if (rock.pieces.all { it.first + p.first in widthRange }) {
            if (rock.pieces.all { !occupied2[it.second + p.second]!!.contains(it.first + p.first) }) {
                return false
            }
        }
        return true
    }

    private fun simulate2(n: Long): Long {
        var height: Long = 0
        var jet: Int = 0

        for (i in 0 until n) {
            if (i % 50455 == 0.toLong()) {
                for (row in height - 78500 downTo height - (78500*2)) {
                    occupied2.remove(row)
                }
            }
            val rock = Rocks.values()[i.toInt() % Rocks.values().size]
            var bottomLeft = 2.toLong() to height + 4
            for (row in height + 1..height + 7) {
                if (!occupied2.contains(row)) {
                    occupied2[row] = mutableSetOf()
                }
            }

            /**
             * First three steps down is guaranteed not to overlap
             */
            for (j in 1..3) {
                if (jetStream[jet] == '<') {
                    // and if it can be pushed left
                    if (!isNotValid2(bottomLeft.first - 1 to bottomLeft.second, rock)) {
                        // push left
                        bottomLeft = bottomLeft.first - 1 to bottomLeft.second
                    }
                }
                // if jetStream is to the right
                if (jetStream[jet] == '>') {
                    // and if it can be pushed right
                    if (!isNotValid2(bottomLeft.first + 1 to bottomLeft.second, rock)) {
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
            while (!isNotValid2(bottomLeft, rock)) {

                // if jetStream is to the left
                if (jetStream[jet] == '<') {
                    // and if it can be pushed left
                    if (!isNotValid2(bottomLeft.first - 1 to bottomLeft.second, rock)) {
                        // push left
                        bottomLeft = bottomLeft.first - 1 to bottomLeft.second
                    }
                }
                // if jetStream is to the right
                if (jetStream[jet] == '>') {
                    // and if it can be pushed right
                    if (!isNotValid2(bottomLeft.first + 1 to bottomLeft.second, rock)) {
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
            rock.pieces.forEach { occupied2[it.second + bottomLeft.second]!!.add(it.first + bottomLeft.first) }
            // update new height
            height = height.coerceAtLeast(rock.pieces.maxOf { it.second + bottomLeft.second })
        }

        return height
    }

    fun part2(): Long {
        occupied.clear()
        occupied.addAll(
            listOf(
                0.toLong() to 0.toLong(),
                1.toLong() to 0.toLong(),
                2.toLong() to 0.toLong(),
                3.toLong() to 0.toLong(),
                4.toLong() to 0.toLong(),
                5.toLong() to 0.toLong(),
                6.toLong() to 0.toLong()
            )
        )
        val height = simulate2(200000000)

//        for (row in height downTo height - 100) {
//            for (col in widthRange) {
//                if (occupied2[row]!!.contains(col.toLong())) {
//                    print("# ")
//                }
//                else {
//                    print(". ")
//                }
//            }
//            print("\n")
//        }
        return height
    }
}

fun main() {
    val sol = Day17("src/main/resources/2022/Day17Input.txt")
    println(sol.part1())
    val time = measureTimeMillis {
        println(sol.part2())
    }
    println("${time / 1000.toDouble()} seconds")
    println("Estimated time for part2: ${time * (5000).toDouble() / (1000 * 3600 * 24).toDouble()} Days")

    println("LCM: ${sol.jetStream.length * 5}")
}

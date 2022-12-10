package aoc2022

import java.io.File

interface Operation {
    val cycles: Int
}

class Noop(): Operation {
    override val cycles = 1
}

class Addx(val value: Int): Operation {
    override val cycles = 2
}

class Day10(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val importantCycles = listOf(20, 60, 100, 140, 180, 220)

    var cycle = 1
    var register = 1

    private fun parseOperation(line: String): Operation {
        val words = line.split(" ")
        return when (words[0]) {
            "addx" -> Addx(words[1].toInt())
            else -> Noop()
        }
    }

    private fun checkImportantCycle(): Int = if (cycle in importantCycles) cycle * register else 0

    private fun simulateCycles(operations: List<Operation>): Int {
        cycle = 1
        register = 1
        var sum = 0
        var add = 0
        for (operation in operations) {
            add = 0
            when (operation) {
                is Addx -> {
                    add = operation.value
                    sum += checkImportantCycle()
                    cycle += 1
                }
            }
            sum += checkImportantCycle()
            cycle += 1
            register += add
        }
        return sum
    }

    private fun drawPixel() {
        print(if ((cycle - 1) % 40 in register - 1.. register + 1) "#" else ".")
        print(" ")
        print(if (cycle % 40 == 0) "\n" else "")
    }

    private fun drawCycles(operations: List<Operation>) {
        cycle = 1
        register = 1
        var add = 0
        for (operation in operations) {
            add = 0
            when (operation) {
                is Addx -> {
                    add = operation.value
                    drawPixel()
                    cycle += 1
                }
            }
            drawPixel()
            cycle += 1
            register += add
        }
    }

    fun part1(): Int = simulateCycles(lines.map { parseOperation(it) })

    fun part2() {
        drawCycles(lines.map { parseOperation(it) })
    }
}

fun main() {
    val sol = Day10("src/main/resources/2022/Day10Input.txt")
    println(sol.part1())
    sol.part2()
}

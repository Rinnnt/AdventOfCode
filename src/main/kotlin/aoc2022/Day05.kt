package aoc2022

import java.io.File
import java.util.*

class Day5 {
    private fun parse(lines: List<String>): Pair<List<String>, List<String>> {
        val split = lines.indexOf("")
        val n = lines.size
        return lines.subList(0, split) to lines.subList(split + 1, n)
    }

    private fun parseContainers(initial: List<String>): List<Stack<Char>> {
        val n = (initial[0].length + 1) / 4
        val ret: MutableList<Stack<Char>> = mutableListOf()
        for (i in 1..n) {
            ret.add(Stack())
        }

        for (i in initial.size - 2 downTo 0) {
            for (j in 0 until n) {
                if (initial[i][(1 + j * 4)].isLetter()) {
                    ret[j].push(initial[i][(1 + j * 4)])
                }
            }
        }

        return ret.toList()
    }

    private fun parseMove(line: String): Triple<Int, Int, Int> {
        // move n from i to j
        val words = line.split(" ")
        return Triple(words[1].toInt(), words[3].toInt(), words[5].toInt())
    }

    private fun makeMoves(containers: List<Stack<Char>>, moves: List<String>): List<Stack<Char>> {
        moves.forEach {
            val (n, from, to) = parseMove(it)
            for (i in 1..n) {
                val x = containers[from - 1].pop()
                containers[to - 1].push(x)
            }
        }
        return containers
    }

    private fun topOfContainers(containers: List<Stack<Char>>): String {
        val sb = StringBuilder()
        containers.forEach { sb.append(it.peek()) }
        return sb.toString()
    }

    fun part1(filename: String): String {
        val lines = File(filename).bufferedReader().readLines()
        val (initial, moves) = parse(lines)
        val containers = parseContainers(initial)
        val newContainers = makeMoves(containers, moves)
        return topOfContainers(newContainers)
    }

    private fun makeMoves2(containers: List<Stack<Char>>, moves: List<String>): List<Stack<Char>> {
        moves.forEach {
            val (n, from, to) = parseMove(it)
            val tempStack: Stack<Char> = Stack()
            for (i in 1..n) {
                val x = containers[from - 1].pop()
                tempStack.push(x)
            }
            for (i in 1..n) {
                val x = tempStack.pop()
                containers[to - 1].push(x)
            }
        }
        return containers
    }

    fun part2(filename: String): String {
        val lines = File(filename).bufferedReader().readLines()
        val (initial, moves) = parse(lines)
        val containers = parseContainers(initial)
        val newContainers = makeMoves2(containers, moves)
        return topOfContainers(newContainers)
    }
}

fun main() {
    val sol = Day5()
    println(sol.part1("src/main/resources/2022/Day05Input.txt"))
    println(sol.part2("src/main/resources/2022/Day05Input.txt"))
}
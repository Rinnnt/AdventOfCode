package aoc2022

import java.io.File
import java.util.*


class Day13(filename: String) {
    val lines = File(filename).bufferedReader().readLines()

    fun parse(lines: List<String>): List<Pair<List<Any>, List<Any>>> =
        lines.filter { it.isNotEmpty() }.chunked(2).map { parseString(it[0]).first() as List<Any> to parseString(it[1]).first() as List<Any> }

    fun parse2(lines: List<String>): List<List<Any>> =
        lines.filter { it.isNotEmpty() }.map {parseString(it).first() as List<Any>}.plus(listOf(listOf(listOf(2)))).plus(listOf(listOf(listOf(6))))

    private fun parseString(line: String): List<Any> {
        if (line.isEmpty()) return listOf()
        if (line.all { it.isDigit() }) return listOf(line.toInt())

        val ret = mutableListOf<Any>()
        val (enclosed, elements) = checkEnclosed(line)

        if (enclosed) {
            ret.add(parseString(line.substring(1, line.length - 1)))
            return ret
        }
        for (element in elements) {
            ret.addAll(parseString(element))
        }
        return ret
    }

    private fun checkEnclosed(line: String): Pair<Boolean, MutableList<String>> {
        var enclosed = true
        var brackets = 0
        var prevComma = 0
        val elements: MutableList<String> = mutableListOf()
        for ((idx, c) in line.withIndex()) {
            if (c == '[') {
                brackets++
            }
            else if (c == ']') {
                brackets--
            }
            if (brackets == 0 && idx != line.length - 1) {
                enclosed = false
                if (c == ',') {
                    elements.add(line.substring(prevComma, idx))
                    prevComma = idx + 1
                }
            }
        }
        elements.add(line.substring(prevComma))

        return Pair(enclosed, elements)
    }


    // 1 is sorted (a is smaller)
    // -1 is not sorted (a is bigger)
    // 0 is for recursion
    private fun compare(a: List<Any>, b: List<Any>): Int {
        if (a.isEmpty() && b.isEmpty()) {
            return 0
        }
        if (a.isEmpty()) {
            return 1
        }
        if (b.isEmpty()) {
            return -1
        }

        if (a.first() is List<*> && b.first() is List<*>) {
            val c = compare((a.first() as List<Any>), (b.first() as List<Any>))
            if (c == 0) {
                return compare(a.subList(1, a.size), b.subList(1, b.size))
            }
            else {
                return c
            }
        }
        else if (a.first() is Int && b.first() is List<*>) {
            val aFirst = a.first() as Int
            val newA = mutableListOf(aFirst)
            val c = compare(newA, b.first() as List<Any>)
            if (c == 0) {
                return compare(a.subList(1, a.size), b.subList(1, b.size))
            }
            else {
                return c
            }
        }
        else if (a.first() is List<*> && b.first() is Int) {
            val bFirst = b.first() as Int
            val newb = mutableListOf(bFirst)
            val c = compare(a.first() as List<Any>, newb)
            if (c == 0) {
                return compare(a.subList(1, a.size), b.subList(1, b.size))
            }
            else {
                return c
            }
        }
        else {
            if (a.first() as Int == b.first() as Int) {
                return compare(a.subList(1, a.size), b.subList(1, b.size))
            }
            else if (a.first() as Int <= b.first() as Int) {
                return 1
            }
            else {
                return -1
            }
        }
    }

    fun part1(): Int =
        parse(lines).withIndex().filter { compare(it.value.first, it.value.second) == 1 }.sumOf {it.index + 1}

    private fun bubbleSort(lists: List<List<Any>>): List<List<Any>> {
        val mlists = lists.toMutableList()
        for (i in 0..mlists.size) {
            for (j in 0 until mlists.size - 1) {
                if (compare(mlists[j], mlists[j+1]) == -1) {
                    val temp = mlists[j]
                    mlists[j] = mlists[j+1]
                    mlists[j+1] = temp
                }
            }
        }
        return mlists
    }

    fun part2(): Int {
        val sorted = bubbleSort(parse2(lines))
        sorted.forEach {println(it)}
        return (sorted.indexOf(listOf(listOf(2))) + 1) * (sorted.indexOf(listOf(listOf(6))) + 1)
    }


}

fun main() {
    val sol = Day13("src/main/resources/2022/Day13Input.txt")
    println(sol.part1())
    println(sol.part2())
}



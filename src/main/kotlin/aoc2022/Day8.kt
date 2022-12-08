package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

class Day8(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val colSize = lines[0].length
    private val rowSize = lines.size
    private val trees: List<List<Pair<Char, Int>>> = lines.map { it.split("").subList(1, it.split("").size - 1) }.withIndex()
        .map { row -> row.value.withIndex().map { col -> col.value.single() to flatIndex(row.index, col.index) } }
    private val treeLines: List<List<Pair<Char, Int>>> = createTreeLines()
    private val visible = MutableList(rowSize * colSize) { false }
    private val visibility = MutableList(rowSize * colSize) {1}

    private fun flatIndex(row: Int, col: Int) = row * colSize + col

    private fun createTreeLines(): List<List<Pair<Char, Int>>> {
        val ret: MutableList<List<Pair<Char, Int>>> = mutableListOf()
        val flatTrees = trees.flatten()
        for (i in 0 until rowSize) {
            ret.add(flatTrees.filter { it.second / colSize == i })
        }
        for (i in 0 until colSize) {
            ret.add(flatTrees.filter { it.second % colSize == i })
        }
        return ret.map { listOf(it, it.reversed()) }.flatten()
    }

    private fun updateVisible(trees: List<Pair<Char, Int>>) {
        var tallest = 0
        for (tree in trees) {
            if (tree.first.code > tallest) {
                visible[tree.second] = true
                tallest = tree.first.code
            }
        }
    }

    private fun updateVisibility(trees: List<Pair<Char, Int>>) {
        for ((i, tree) in trees.withIndex()) {
            var n = 0
            for (idx in i-1 downTo 0) {
                n++
                if (trees[idx].first.code >= tree.first.code) break
            }
            visibility[tree.second] *= n
        }
    }

    init {
        treeLines.forEach {updateVisible(it)}
        treeLines.forEach {updateVisibility(it)}
    }

    fun part1(): Int = visible.map { if (it) 1 else 0}.sum()

    fun part2(): Int = visibility.max()
}

fun main() {
    val executionTime = measureTimeMillis {
        val sol = Day8("src/main/resources/2022/Day8Input.txt")
        println(sol.part1())
        println(sol.part2())
    }
    println("Execution took ${executionTime.toDouble() / 1000.0} seconds")
}

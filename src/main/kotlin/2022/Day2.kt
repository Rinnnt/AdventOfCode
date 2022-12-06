package `2022`

import java.io.File

class Day2 {
    // Opponent codes - A: Rock, B: Paper, C: Scissors
    // player codes   - X: Rock, Y: Paper, Z: Scissors
    private val defeats: Map<Char, Char> = mapOf(
        'X' to 'C',
        'Y' to 'A',
        'Z' to 'B'
    )

    private val equals: Map<Char, Char> = mapOf(
        'X' to 'A',
        'Y' to 'B',
        'Z' to 'C'
    )

    private val loses2: Map<Char, Char> = mapOf(
        'B' to 'X',
        'C' to 'Y',
        'A' to 'Z'
    )

    private val defeats2: Map<Char, Char> = mapOf(
        'C' to 'X',
        'A' to 'Y',
        'B' to 'Z'
    )

    private val equals2: Map<Char, Char> = mapOf(
        'A' to 'X',
        'B' to 'Y',
        'C' to 'Z'
    )

    private val shapeScore: Map<Char, Int> = mapOf(
        'X' to 1,
        'Y' to 2,
        'Z' to 3
    )

    private val winScore = 6
    private val drawScore = 3
    private val loseScore = 0

    private fun score(e: Char, p: Char) =
        when (e) {
            defeats[p] -> winScore
            equals[p] -> drawScore
            else -> loseScore
        }

    private fun newScore(e: Char, s: Char) =
        when (s) {
            'X' -> loseScore + shapeScore.getOrDefault(loses2.getOrDefault(e, 'X'), 0)
            'Y' -> drawScore + shapeScore.getOrDefault(equals2.getOrDefault(e, 'X'), 0)
            'Z' -> winScore + shapeScore.getOrDefault(defeats2.getOrDefault(e, 'X'), 0)
            else -> 0
        }

    fun part1(filename: String): Int {
        val rounds = File(filename).useLines { it.toList() }
        return rounds.sumOf { score(it[0], it[2]) + shapeScore.getOrDefault(it[2], 0) }
    }

    fun part2(filename: String): Int {
        val rounds = File(filename).useLines { it.toList() }
        return rounds.sumOf { newScore(it[0], it[2]) }
    }
}



fun main() {
    val sol = Day2()
    println(sol.part1("src/main/resources/2022/Day2Input.txt"))
    println(sol.part2("src/main/resources/2022/Day2Input.txt"))
}
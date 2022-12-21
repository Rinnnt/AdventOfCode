package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

class Day21(filename: String) {

    /**
     * Create a class for monkey with its name and its job
     * e.g. NumberMonkey("root", "jdqw + nrrs")
     */
    class NumberMonkey(val name: String, val job: String)

    /**
     * Create a map from the monkey's name to the monkey object
     */
    private val numberMonkeys = File(filename).bufferedReader().readLines().map {
        val words = it.split(": ")
        NumberMonkey(words[0], words[1])
    }.associateBy { it.name }

    /**
     * Memoization for the monkey's final output
     */
    private val monkeysNumber = numberMonkeys.mapValues { Long.MIN_VALUE }.toMutableMap()

    private val opMap: Map<String, ((Long, Long) -> Long)> = mapOf(
        "+" to { x, y -> x + y },
        "-" to { x, y -> x - y },
        "*" to { x, y -> x * y },
        "/" to { x, y -> x / y },
        "=" to { x, y -> if (x == y) 1 else 0 }
    )

    /**
     * Evaluate the final output of a monkey, with memoization
     */
    private fun eval(n: String): Long {
        if (monkeysNumber[n]!! != Long.MIN_VALUE) {
            return monkeysNumber[n]!!
        }

        val m = numberMonkeys[n]!!
        if (m.job.all { it.isDigit() }) {
            monkeysNumber[n] = m.job.toLong()
            return monkeysNumber[n]!!
        }

        val words = m.job.split(" ")
        monkeysNumber[n] = opMap[words[1]]!!(eval(words[0]), eval(words[2]))
        return monkeysNumber[n]!!
    }

    fun part1(): Long = eval("root")

    fun part2(): Long {

        /**
         * Create a second memoization table with the humn value set arbitrarily
         */
        val numberMonkeys2 = numberMonkeys.toMutableMap()
        var monkeysNumber2 = numberMonkeys2.mapValues { Long.MIN_VALUE }.toMutableMap()

        fun eval2(n: String): Long {
            val m = numberMonkeys2[n]!!
            if (m.job.all { it.isDigit() }) {
                monkeysNumber2[n] = m.job.toLong()
                return monkeysNumber2[n]!!
            }

            val words = m.job.split(" ")
            monkeysNumber2[n] = opMap[words[1]]!!(eval2(words[0]), eval2(words[2]))
            return monkeysNumber2[n]!!
        }

        numberMonkeys2["humn"] = NumberMonkey("root", "999999")
        eval2("root")

        /**
         * If the first and second memoization table contains same output for a monkey,
         * the monkey's output is not dependent on humn's value
         */
        monkeysNumber2 = monkeysNumber2.filter { it.value == monkeysNumber[it.key]!! }.toMutableMap()
        numberMonkeys2["root"] = NumberMonkey("root", numberMonkeys["root"]!!.job.replace('+', '='))

        /**
         * String representation of the equation to solve
         */
        fun jobToString(n: String): String {
            if (n == "humn") return "humn"
            if (monkeysNumber2.contains(n)) {
                return monkeysNumber2[n]!!.toString()
            }
            val m = numberMonkeys2[n]!!
            if (m.job.all { it.isDigit() }) {
                return m.job
            }
            val words = m.job.split(" ")
            return "(${jobToString(words[0])} ${words[1]} ${jobToString(words[2])})"
        }

        /**
         * Enforce the value a monkey should output
         * so that the previous monkey to output the needed value
         */
        fun needsToBe(n: String, value: Long): Long {
            if (n == "humn") {
                return value
            }
            val words = numberMonkeys2[n]!!.job.split(" ")
            if (monkeysNumber2.contains(words[0])) {
                when (words[1]) {
                    "+" -> return needsToBe(words[2], value - monkeysNumber2[words[0]]!!)
                    "-" -> return needsToBe(words[2], monkeysNumber2[words[0]]!! - value)
                    "*" -> return needsToBe(words[2], value / monkeysNumber2[words[0]]!!)
                    "/" -> return needsToBe(words[2], monkeysNumber2[words[0]]!! / value)
                    "=" -> return needsToBe(words[2], monkeysNumber2[words[0]]!!)
                }
            } else {
                when (words[1]) {
                    "+" -> return needsToBe(words[0], value - monkeysNumber2[words[2]]!!)
                    "-" -> return needsToBe(words[0], value + monkeysNumber2[words[2]]!!)
                    "*" -> return needsToBe(words[0], value / monkeysNumber2[words[2]]!!)
                    "/" -> return needsToBe(words[0], value * monkeysNumber2[words[2]]!!)
                    "=" -> return needsToBe(words[0], monkeysNumber2[words[2]]!!)
                }
            }
            return -1
        }

        println(jobToString("root"))
        return needsToBe("root", 1)
    }
}

fun main() {
    val sol = Day21("src/main/resources/2022/Day21Input.txt")
    println(sol.part1())
    println(sol.part2())
}

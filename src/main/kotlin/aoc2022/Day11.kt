package aoc2022

import java.io.File
import java.math.BigInteger

class Monkey(
    val id: Int,
    startingItems: List<BigInteger>,
    val operation: (BigInteger) -> BigInteger,
    val divTestNumber: Int,
    val trueMonkey: Int,
    val falseMonkey: Int
) {
    val items = ArrayDeque(startingItems)
    var inspected: BigInteger = 0.toBigInteger()
}

class Day11() {
    val monkeys = mutableListOf<Monkey>()

    fun initializeMonkeys() {
        monkeys.clear()
        monkeys.add(
            Monkey(
                0,
                listOf(83, 88, 96, 79, 86, 88, 70).map { it.toBigInteger() },
                { x -> x.times(5.toBigInteger()) },
                11,
                2,
                3
            )
        )
        monkeys.add(
            Monkey(
                1,
                listOf(59, 63, 98, 85, 68, 72).map { it.toBigInteger() },
                { x -> x.times(11.toBigInteger()) },
                5,
                4,
                0
            )
        )
        monkeys.add(
            Monkey(
                2,
                listOf(90, 79, 97, 52, 90, 94, 71, 70).map { it.toBigInteger() },
                { x -> x.plus(2.toBigInteger()) },
                19,
                5,
                6
            )
        )
        monkeys.add(
            Monkey(
                3,
                listOf(97, 55, 62).map { it.toBigInteger() },
                { x -> x.plus(5.toBigInteger())},
                13,
                2,
                6
            )
        )
        monkeys.add(
            Monkey(
                4,
                listOf(74, 54, 94, 76).map { it.toBigInteger() },
                { x -> x * x },
                7,
                0,
                3
            )
        )
        monkeys.add(
            Monkey(
                5,
                listOf(58).map { it.toBigInteger() },
                { x -> x.plus(4.toBigInteger()) },
                17,
                7,
                1
            )
        )
        monkeys.add(
            Monkey(
                6,
                listOf(66, 63).map { it.toBigInteger() },
                { x -> x.plus(6.toBigInteger()) },
                2,
                7,
                5
            )
        )
        monkeys.add(
            Monkey(
                7,
                listOf(56, 56, 90, 96, 68).map { it.toBigInteger() },
                { x -> x.plus(7.toBigInteger()) },
                3,
                4,
                1
            )
        )
    }

    fun takeRound(n: Int, relief: Boolean = true) {
        val zero: Long = 0
        for (i in 1..n) {
            for (monkey in monkeys) {
                monkey.inspected = monkey.inspected.plus(monkey.items.size.toBigInteger())
                while (monkey.items.isNotEmpty()) {
                    val item = if (relief) monkey.operation(monkey.items.removeFirst()).divide(3.toBigInteger()) else monkey.operation(monkey.items.removeFirst())
                    if (item.rem(monkey.divTestNumber.toBigInteger()) == 0.toBigInteger()) {
                        monkeys[monkey.trueMonkey].items.add(item.rem(9699690.toBigInteger()))//.divide(monkey.divTestNumber.toBigInteger()))
                    } else {
                        monkeys[monkey.falseMonkey].items.add(item.rem(9699690.toBigInteger()))
                    }
                }
            }
        }
    }

    fun part1(): BigInteger {
        initializeMonkeys()
        takeRound(20)
        val sortedMonkeys = monkeys.sortedByDescending { it.inspected }
        return sortedMonkeys[0].inspected.times(sortedMonkeys[1].inspected)
    }

    fun part2(): BigInteger {
        initializeMonkeys()
        takeRound(10000, false)
        val sortedMonkeys = monkeys.sortedByDescending { it.inspected }
        return sortedMonkeys[0].inspected.times(sortedMonkeys[1].inspected)

    }

}

fun main() {
    val sol = Day11()
    println(sol.part1())
    println(sol.part2())
}

package aoc2022

import java.io.File
import kotlin.system.measureTimeMillis

data class Resources(val ore: Int, val clay: Int, val obsidian: Int) {
    fun values(): List<Int> = listOf(ore, clay, obsidian)

    fun canBuy(other: Resources): Boolean =
        ore >= other.ore && clay >= other.clay && obsidian >= other.obsidian

    fun plus(other: Resources): Resources =
        Resources(ore + other.ore, clay + other.clay, obsidian + other.obsidian)

    fun minus(other: Resources): Resources =
        Resources(ore - other.ore, clay - other.clay, obsidian - other.obsidian)
}

val eachResource = listOf(
    Resources(1, 0, 0),
    Resources(0, 1, 0),
    Resources(0, 0, 1),
)

class Day19(filename: String) {
    /**
     * stores blueprints as a list of pairs of:
     * * blueprint number
     * * list of robot costs
     */
    private val blueprints = File(filename).bufferedReader().readLines().map {
        it.substringAfter("Blueprint ").substringBefore(":").toInt() to
                it.substringAfter(": ").substringBeforeLast(".").split(". ").map {
                    var prev = ""
                    var (ore, clay, obsidian) = Triple(0, 0, 0)
                    it.substringAfter("costs ").split(" ").forEach {
                        if (it == "ore") ore += prev.toInt()
                        if (it == "clay") clay += prev.toInt()
                        if (it == "obsidian") obsidian += prev.toInt()
                        prev = it
                    }
                    Resources(ore, clay, obsidian)
                }
    }

    private fun blueprintQuality(blueprint: Pair<Int, List<Resources>>): Int {
        val blueprintNumber = blueprint.first
        val maxNeeded = listOf(blueprint.second.maxOf { it.ore },
            blueprint.second.maxOf { it.clay },
            blueprint.second.maxOf { it.obsidian })
        val robotResources = blueprint.second.zip(eachResource)
        val memo = mutableMapOf<Triple<Resources, Resources, Int>, Int>()

        /**
         * Finds the maximum number of extra geodes it can open from the given state
         */
        fun findMaxGeodes(
            curResources: Resources,
            accResources: Resources,
            time: Int,
            couldBuild: List<Boolean> = listOf(false, false, false)
        ): Int {
            /**
             * Base case: out of time
             */
            if (time <= 0) return 0

            /**
             * Memoization
             */
            val state = Triple(curResources, accResources, time)
            if (memo.contains(state)) {
                return memo[state]!!
            }

            var geodes = 0
            /**
             * If building a geode robot is an option, it is the optimal option
             */
            if (curResources.canBuy(blueprint.second[3])) {
                geodes = time - 1 + findMaxGeodes(
                    curResources.plus(accResources).minus(blueprint.second[3]),
                    accResources,
                    time - 1
                )
            } else {
                var resourceCounter = 0
                val newCouldBuild = mutableListOf<Boolean>()
                for ((robot, resource) in robotResources) {
                    newCouldBuild.add(curResources.canBuy(robot))
                    /**
                     * If we could have built the robot in a previous state, don't build it now
                     * If we have enough robots for a resource, don't build more of those robots
                     */
                    if ((!couldBuild[resourceCounter]) &&
                        (accResources.values()[resourceCounter] < maxNeeded[resourceCounter]) &&
                        (curResources.canBuy(robot))
                    ) {
                        geodes = geodes.coerceAtLeast(
                            findMaxGeodes(
                                curResources.plus(accResources).minus(robot),
                                accResources.plus(resource),
                                time - 1
                            )
                        )
                    }
                    resourceCounter++
                }
                /**
                 * In the case of not building a robot, pass the robots we could have built at this state
                 */
                geodes = geodes.coerceAtLeast(
                    findMaxGeodes(curResources.plus(accResources), accResources, time - 1, newCouldBuild)
                )
            }

            memo[state] = geodes
            return geodes
        }

        val maxGeodes = findMaxGeodes(Resources(0, 0, 0), Resources(1, 0, 0), 24)
        return blueprintNumber * maxGeodes
    }

    fun part1(): Int =
        blueprints.withIndex().sumOf { blueprintQuality(it.value) }

    private fun blueprintGeodes(blueprint: Pair<Int, List<Resources>>): Int {
        val robotResources = blueprint.second.zip(eachResource)
        val maxNeeded = listOf(blueprint.second.maxOf { it.ore },
            blueprint.second.maxOf { it.clay },
            blueprint.second.maxOf { it.obsidian })
        val memo = mutableMapOf<Triple<Resources, Resources, Int>, Int>()

        /**
         * Finds the maximum number of extra geodes it can open from the given state
         */
        fun findMaxGeodes(
            curResources: Resources,
            accResources: Resources,
            time: Int,
            couldBuild: List<Boolean> = listOf(false, false, false)
        ): Int {
            /**
             * Base case: out of time
             */
            if (time <= 0) return 0

            /**
             * Memoization
             */
            val state = Triple(curResources, accResources, time)
            if (memo.contains(state)) {
                return memo[state]!!
            }

            var geodes = 0
            /**
             * If building a geode robot is an option, it is the optimal option
             */
            if (curResources.canBuy(blueprint.second[3])) {
                geodes = time - 1 + findMaxGeodes(
                    curResources.plus(accResources).minus(blueprint.second[3]),
                    accResources,
                    time - 1
                )
            } else {
                var resourceCounter = 0
                val newCouldBuild = mutableListOf<Boolean>()
                for ((robot, resource) in robotResources) {
                    newCouldBuild.add(curResources.canBuy(robot))
                    /**
                     * If we could have built the robot in a previous state, don't build it now
                     * If we have enough robots for a resource, don't build more of those robots
                     */
                    if ((!couldBuild[resourceCounter]) &&
                        (accResources.values()[resourceCounter] < maxNeeded[resourceCounter]) &&
                        (curResources.canBuy(robot))
                    ) {
                        geodes = geodes.coerceAtLeast(
                            findMaxGeodes(
                                curResources.plus(accResources).minus(robot),
                                accResources.plus(resource),
                                time - 1
                            )
                        )
                    }
                    resourceCounter++
                }
                /**
                 * In the case of not building a robot, pass the robots we could have built at this state
                 */
                geodes = geodes.coerceAtLeast(
                    findMaxGeodes(curResources.plus(accResources), accResources, time - 1, newCouldBuild)
                )
            }

            memo[state] = geodes
            return geodes
        }

        return findMaxGeodes(Resources(0, 0, 0), Resources(1, 0, 0), 32)
    }

    fun part2(): Int =
        blueprints.subList(0, 3).map { blueprintGeodes(it) }
            .fold(1) { acc, t -> acc * t }
}

fun main() {
    val sol = Day19("src/main/resources/2022/Day19Input.txt")

    val time = measureTimeMillis {
        println(sol.part1())
    }
    println("took ${time / 1000.0} seconds")
    val time2 = measureTimeMillis {
        println(sol.part2())
    }
    println("took ${time2 / 1000.0} seconds")
}

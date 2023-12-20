package aoc2023

import java.io.File

class Day20(val filename: String) {
    enum class ModuleType {
        BROADCASTER, FLIP_FLOP, CONJUNCTION
    }

    enum class Pulse {
        LOW {
            override fun invert(): Pulse = HIGH
        },
        HIGH {
            override fun invert(): Pulse = LOW
        };

        abstract fun invert(): Pulse
    }

    class Module(val name: String, val type: ModuleType, val destinations: List<String>) {
        private val memory = mutableMapOf<String, Pulse>()

        fun addSender(sender: String) {
            memory += sender to Pulse.LOW
        }

        fun clearMemory() {
            memory.clear()
        }

        fun process(sender: String, pulse: Pulse): List<Pair<String, Pulse>> {
            return when (type) {
                ModuleType.BROADCASTER -> destinations.map { it to pulse }

                ModuleType.FLIP_FLOP -> {
                    when (pulse) {
                        Pulse.LOW -> {
                            val newPulse = memory.getOrDefault(name, Pulse.LOW).invert()
                            memory += name to newPulse
                            destinations.map { it to newPulse }
                        }

                        Pulse.HIGH -> listOf()
                    }
                }

                ModuleType.CONJUNCTION -> {
                    memory += sender to pulse
                    destinations.map { d -> d to if (memory.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH }
                }
            }
        }
    }

    private val moduleRegex = Regex("([%&a-z]+) -> (([a-z]+)(,\\s*[a-z]+)*)")

    private val config = File(filename).bufferedReader().readLines().map {
        val (name, ds) = moduleRegex.matchEntire(it)!!.destructured
        name to ds.split(", ")
    }

    private val modules = config.associate {
        when (it.first[0]) {
            '%' -> it.first.drop(1) to Module(it.first.drop(1), ModuleType.FLIP_FLOP, it.second)
            '&' -> it.first.drop(1) to Module(it.first.drop(1), ModuleType.CONJUNCTION, it.second)
            'b' -> it.first to Module(it.first, ModuleType.BROADCASTER, it.second)
            else -> throw IllegalArgumentException("Unknown module type")
        }
    }

    private fun reset() {
        modules.values.forEach { it.clearMemory() }
        config.forEach { entry ->
            entry.second.forEach {
                if (modules.contains(it)) modules[it]!!.addSender(entry.first.drop(1))
            }
        }
    }

    fun part1(): Long {
        reset()
        var lows = 0L
        var highs = 0L
        for (i in 1..1000) {
            val q = ArrayDeque<Triple<String, String, Pulse>>()
            q.addLast(Triple("button", "broadcaster", Pulse.LOW))
            while (!q.isEmpty()) {
                val (from, to, pulse) = q.removeFirst()
                when (pulse) {
                    Pulse.LOW -> lows++
                    Pulse.HIGH -> highs++
                }
                if (modules.contains(to)) {
                    val next = modules[to]!!.process(from, pulse)
                    q.addAll(next.map { Triple(to, it.first, it.second) })
                }
            }
        }
        return lows * highs
    }

    private fun gcd(x: Long, y: Long): Long {
        if (y == 0L) return x
        return gcd(y, x % y)
    }

    private fun lcm(x: Long, y: Long): Long = (x * y) / gcd(x, y)

    fun part2(): Long {
        reset()
        // find cycle for modules that connect to nc
        // as nc is a conjunction that connects to rx
        val required = modules.filter { it.value.destinations.contains("nc") }.keys
        val cycles = mutableMapOf<String, Long>()

        var buttonPresses = 0L
        val q = ArrayDeque<Triple<String, String, Pulse>>()
        while (cycles.size < required.size) {
            buttonPresses++
            q.addLast(Triple("button", "broadcaster", Pulse.LOW))
            while (!q.isEmpty()) {
                val (from, to, pulse) = q.removeFirst()
                if (required.contains(to) && pulse == Pulse.LOW) cycles += to to buttonPresses
                if (modules.contains(to)) {
                    val next = modules[to]!!.process(from, pulse)
                    q.addAll(next.map { Triple(to, it.first, it.second) })
                }
            }
        }
        return cycles.values.reduce(::lcm)
    }
}

fun main() {
    val sol = Day20("src/main/resources/2023/Day20Input.txt")
    println(sol.part1())
    println(sol.part2())
}

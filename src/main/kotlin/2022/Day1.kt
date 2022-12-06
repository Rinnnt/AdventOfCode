package `2022`

class Day1Part1 {
     fun solution() {
        var max = 0
        var cur = 0
        var input = readln()
        while (input != "") {
            while (input != "") {
                cur += input.toInt()
                input = readln()
            }
            max = max.coerceAtLeast(cur)
            cur = 0
            input = readln()
        }

        println("Max Calories: $max")
    }
}

class Day1Part2 {
    private fun insert(list: MutableList<Int>, num: Int): MutableList<Int> {
        if (num <= list[2]) {
            return list
        }

        val insertIndex = if (num > list[0]) {
            0
        } else if (num > list[1]) {
            1
        } else {
            2
        }

        list.add(insertIndex, num)
        return list.take(3).toMutableList()
    }

    fun solution() {
        var topThree = mutableListOf(0, 0, 0)
        var cur = 0
        var input = readln()
        while (input != "") {
            while (input != "") {
                cur += input.toInt()
                input = readln()
            }
            topThree = insert(topThree, cur)
            cur = 0
            input = readln()
        }

        println("Total of top 3: ${topThree.sum()}")
    }
}

fun main() {
//    val sol1 = Day1Part1()
//    sol1.solution()

    val sol2 = Day1Part2()
    sol2.solution()

}

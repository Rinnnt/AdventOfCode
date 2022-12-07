package `2022`

import java.io.File

interface FileSystem

class FFile(val size: Int, val name: String): FileSystem

class Directory(val name: String, val files: MutableList<FileSystem> = mutableListOf(), val size: Int = 0): FileSystem

class Day7(filename: String) {
    val lines = File(filename).bufferedReader().readLines()

    fun buildFileSystem(): FileSystem {
        val root = Directory("/")
        val parents = ArrayDeque<Directory>()
        var cur = root

        for (line in lines.map { it.split(" ") }) {
            when (line[0]) {
                "$" -> if (line[1] == "cd") {
                    when (line[2]) {
                        "/" -> {
                            cur = root
                            parents.clear()
                        }
                        ".." -> cur = parents.removeLast()
                        else -> {
                            parents.addLast(cur)
                            cur = cur.files.filterIsInstance<Directory>().find { it.name == line[2] }!!
                        }
                    }
                }
                "dir" -> cur.files.add(Directory(line[1]))
                else -> cur.files.add(FFile(line[0].toInt(), line[1]))
            }
        }
        return root
    }



    fun part1(): Int {
        TODO()
    }
}

fun main() {
    val sol = Day7("src/main/resources/2022/Day7Input.txt")
    sol.buildFileSystem()
}

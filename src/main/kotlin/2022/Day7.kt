package `2022`

import java.io.File

interface FileSystem

class FFile(val size: Int, val name: String): FileSystem {
//    override fun toString(): String = "  - $name (file, size=${size})"
}

class Directory(val name: String, val files: MutableList<FileSystem> = mutableListOf(), var size: Int? = null): FileSystem {
//    override fun toString(): String {
//        val sb = StringBuilder()
//        sb.append("- $name (dir)")
//        for (file in files) {
//            sb.append("\n  $file")
//        }
//        return sb.toString()
//    }
}

class Day7(filename: String) {
    private val lines = File(filename).bufferedReader().readLines()
    private val root = buildFileSystem()
    private val fileSystemSize = getSize(root)
    private val directories = getDirs()

    private fun buildFileSystem(): Directory {
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

    private fun getSize(root: Directory): Int {
        if (root.size != null) {
            return root.size!!
        }
        else {
            var total = 0
            for (file in root.files) {
                when (file) {
                    is FFile -> {
                        total += file.size
                    }
                    is Directory -> {
                        val s = getSize(file)
                        file.size = s
                        total += s
                    }
                }
            }
            return total
        }
    }

    private fun getDirs(): List<Directory> {
        val ret: MutableList<Directory> = mutableListOf()
        val q = ArrayDeque<Directory>()
        q.addLast(root)
        while (q.isNotEmpty()) {
            val cur = q.removeFirst()
            ret.add(cur)
            for (file in cur.files) {
                if (file is Directory) {
                    q.addLast(file)
                }
            }
        }
        return ret
    }

    fun part1(): Int = directories.filter { (it.size ?: 0) <= 100000 }.sumOf { it.size ?: 0 }

    fun part2(): Int = directories.filter { fileSystemSize - (it.size ?: 0) <= 40_000_000 }.minOf { (it.size ?: 0) }
}

fun main() {
    val sol = Day7("src/main/resources/2022/Day7Input.txt")
    println(sol.part1())
    println(sol.part2())
}

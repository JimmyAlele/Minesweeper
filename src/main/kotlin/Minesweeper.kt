package minesweeper
import kotlin.random.Random
const val ROWS = 9
const val COLUMNS = 9

fun main() {

    val numberOfMines = readln().toInt()
    val mineField1 = MineField(ROWS, COLUMNS, numberOfMines)
    val mineLocations = mineField1.addMines()
    //println(mineLocations)
    mineField1.addHints()
    mineField1.hideMines()
    mineField1.takeGuesses(mineLocations)
}

class MineField (private val rows: Int, private val columns: Int, private val numberOfMines: Int) {
    private val field = MutableList(rows) {MutableList(columns) {"."} }
    private var n: Int = 0

    fun addMines (): MutableList<MutableList<Int>> {
        val mineLocations = mutableListOf<MutableList<Int>>()
        while (n != numberOfMines) {
            val x: Int = Random.nextInt(0, rows)
            val y: Int = Random.nextInt(0, columns)

            if (field[x][y] != "X") {
                field[x][y] = "X"
                mineLocations.add(mutableListOf(x, y))
                n++
            }
        }
        return mineLocations
    }

    fun addHints () {
        for (row in 0 until ROWS) {
            for (column in 0 until COLUMNS) {
                if (field[row][column] != "X") {
                    var countMines = 0
                    for (x in maxOf(row - 1, 0) .. minOf(row + 1, ROWS - 1)) {
                        for (y in maxOf(0, column - 1) .. minOf(column + 1, COLUMNS - 1)) {
                            if (field[x][y] == "X") { countMines ++ }
                        }
                    }
                    field[row][column] = if (countMines > 0) countMines.toString() else "."
                }
            }
        }
    }

    private fun printMineField() {
        field.forEach{
            println(it.joinToString(""))
        }
    }

    fun hideMines () {
        for (row in 0 until ROWS) {
            for (column in 0 until COLUMNS) {
                if (field[row][column] == "X") {
                    field[row][column] = "."
                }
            }
        }
    }

    fun takeGuesses (locations: MutableList<MutableList<Int>>) {
        val minesGuessedWrong = mutableListOf<MutableList<Int>>()
        minesGuessedWrong.addAll(locations)
        val mLocations = mutableListOf<MutableList<Int>>()
        mLocations.addAll(locations)

        while (mLocations.isNotEmpty() && minesGuessedWrong.isNotEmpty()) {
            printMineField()
            println("Set/delete mines marks (x and y coordinates):")
            val (x, y) = readln().split(" ")

            when (field[x.toInt()][y.toInt()]) {
                "." -> {
                    field[x.toInt()][y.toInt()] = "*"
                    if (mutableListOf(x.toInt(), y.toInt()) in mLocations) {
                        mLocations.remove(mutableListOf(x.toInt(), y.toInt()))
                        minesGuessedWrong.remove(mutableListOf(x.toInt(), y.toInt()))
                        printMineField()
                    } else {
                        minesGuessedWrong.add(mutableListOf(x.toInt(), y.toInt()))
                        printMineField()
                    }
                }
                "*" -> {
                    field[x.toInt()][y.toInt()] = "."
                    if (mutableListOf(x.toInt(), y.toInt()) in locations) {
                        mLocations.add(mutableListOf(x.toInt(), y.toInt()))
                        printMineField()
                    } else {
                        minesGuessedWrong.remove(mutableListOf(x.toInt(), y.toInt()))
                        printMineField()
                    }
                }
                else -> {
                    println("There is a number here!")
                }
            }
        }
        println("Congratulations! You found all the mines!")
    }

    fun beautifyField () {
        field.addAll(0, MutableList(1) {MutableList(columns) {"-"} })
        field.addAll(0, MutableList(1) {MutableList(columns) {"-"} })
        for (i in 0 until columns) {
            field[0][i] = (i + 1).toString()
        }
        field.addAll(field.lastIndex + 1, MutableList(1) {MutableList(columns) {"-"} })
        for (i in field.indices) {
            field[i].add(0,"|")
            field[i].add(field[i].lastIndex + 1, "|")
        }

        val stuff = listOf (" ", "-", 1, 2, 3, 4, 5, 6, 7, 8, 9, "-")
        for (i in field.indices) {
            field[i].add(0, stuff[i].toString())
        }
    }
}
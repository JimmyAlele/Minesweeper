package minesweeper
import kotlin.random.Random
const val ROWS = 9
const val COLUMNS = 9

fun main() {
    val numberOfMines = readln().toInt()
    val mineField1 = MineField(ROWS, COLUMNS, numberOfMines)
    mineField1.addMines()
    mineField1.addHints()
    mineField1.printMineField()
}

class MineField (private val rows: Int, private val columns: Int, private val numberOfMines: Int) {
    private val field = MutableList(rows) {MutableList(columns) {"."} }
    private var n: Int = 0

    fun addMines () {
        while (n != numberOfMines) {
            val x: Int = Random.nextInt(0, rows)
            val y: Int = Random.nextInt(0, columns)

            if (field[x][y] != "X") {
                field[x][y] = "X"
                n++
            }
        }
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

    fun printMineField() {
        field.forEach{
            println(it.joinToString(""))
        }
    }
}
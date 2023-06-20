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
    val field = MutableList(rows) {MutableList(columns) {"."} }
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

    private fun cellLocation (x: Int, y: Int): String =
        when {
            x == 0 && y ==0 ||  x == ROWS - 1 && y == 0 || x == ROWS - 1 && y == COLUMNS - 1 || x == 0 && y == COLUMNS - 1 -> "corner"
            x == 0 || x == ROWS -1 || y == 0 || y == COLUMNS - 1 -> "side"
            else -> "middle"
        }

    fun addHints () {
        for (row in 0 until ROWS) {
            for (column in 0 until COLUMNS) {
                val middle = mutableListOf(mutableListOf(row, column - 1),
                    mutableListOf(row - 1, column -1),
                    mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column + 1),
                    mutableListOf(row, column + 1),
                    mutableListOf(row + 1, column + 1),
                    mutableListOf(row + 1, column),
                    mutableListOf(row + 1, column - 1),
                )

                val side1 = mutableListOf(mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column + 1),
                    mutableListOf(row, column + 1),
                    mutableListOf(row + 1, column + 1),
                    mutableListOf(row + 1, column)
                )

                val side2 = mutableListOf(mutableListOf(row, column - 1),
                    mutableListOf(row - 1, column - 1),
                    mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column + 1),
                    mutableListOf(row, column + 1)
                )

                val side3 = mutableListOf(mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column - 1),
                    mutableListOf(row, column - 1),
                    mutableListOf(row + 1, column - 1),
                    mutableListOf(row + 1, column)
                )

                val side4 = mutableListOf(mutableListOf(row, column - 1),
                    mutableListOf(row + 1, column - 1),
                    mutableListOf(row + 1, column),
                    mutableListOf(row + 1, column + 1),
                    mutableListOf(row, column + 1)
                )

                val corner1 = mutableListOf(mutableListOf(row + 1, column),
                    mutableListOf(row + 1, column + 1),
                    mutableListOf(row, column + 1)
                )

                val corner2 = mutableListOf(mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column + 1),
                    mutableListOf(row, column + 1)
                )

                val corner3 = mutableListOf(mutableListOf(row - 1, column),
                    mutableListOf(row - 1, column - 1),
                    mutableListOf(row, column - 1)
                )

                val corner4 = mutableListOf(mutableListOf(row + 1, column),
                    mutableListOf(row + 1, column - 1),
                    mutableListOf(row, column - 1)
                )

                if (field[row][column] != "X") {
                    field[row][column] = when {
                        cellLocation(row, column) == "corner" -> when {
                            row == 0 && column == 0 -> countMines(corner1).toString()
                            row == 8 && column == 0 -> countMines(corner2).toString()
                            row == 8 && column == 8 -> countMines(corner3).toString()
                            else -> countMines(corner4).toString()
                        }
                        cellLocation(row, column) == "side" -> when {
                            column == 0 -> countMines(side1).toString()
                            row == 8 -> countMines(side2).toString()
                            column == 8 -> countMines(side3).toString()
                            else -> countMines(side4).toString()
                        }
                        else -> countMines(middle).toString()

                        }
                    }
                }
            }
        }
    private fun countMines (list: MutableList<MutableList<Int>>): Any {
        var sum = 0
        for (listItem in list) {
            if (field[listItem[0]][listItem[1]] == "X") sum++
        }
        return if(sum == 0) (".") else sum
    }

    fun printMineField() {
        field.forEach{
            println(it.joinToString(""))
        }
    }
}
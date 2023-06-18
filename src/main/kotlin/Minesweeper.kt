package minesweeper
import kotlin.random.Random
const val ROWS = 9
const val COLUMNS = 9

fun main() {
    val numberOfMines = readln().toInt()
    val mineField = MutableList(ROWS) {MutableList(COLUMNS) {"."} }
    var n = 0

    while (n != numberOfMines) {
        val x = Random.nextInt(0,ROWS)
        val y = Random.nextInt(0, COLUMNS)

        if (mineField[x][y] != "X") {
            mineField[x][y] = "X"
            n++
        }
    }
    for (row in mineField) {
        println(row.joinToString(""))
    }
}




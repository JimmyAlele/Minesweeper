package minesweeper
import kotlin.random.Random
const val ROWS = 9
const val COLUMNS = 9

fun main() {
    println("How many mines do you want on the field?")
    val numberOfMines = readln().toInt()
    val rowsWithMines = mutableListOf<Int>()
    repeat(numberOfMines) {
        rowsWithMines.add(Random.nextInt(1, ROWS + 1))
    }
}

    /* generate the mines using random numbers
    var rowPosition = Int
    var columnPosition: Int
    for (mine in 1..numberOfMines) {
        rowPosition = Random.nextInt(0, ROWS)
        columnPosition = Random.nextInt(0, COLUMNS)
    }*/


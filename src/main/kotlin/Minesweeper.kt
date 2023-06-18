package minesweeper
import kotlin.random.Random
const val ROWS = 9
const val COLUMNS = 9

fun main() {
    println("How many mines do you want on the field?")
    val numberOfMines = readln().toInt()
    val rowTemplate = MutableList(COLUMNS) { "." }
    val rowsWithMines = mutableListOf<Int>()
    val numberOfMinesPerRow: MutableList<Int> = mutableListOf<Int>()

    repeat(numberOfMines) {
        rowsWithMines.add(Random.nextInt(1, ROWS + 1))
    }

    for (row in 1..ROWS) {
        numberOfMinesPerRow.add(rowsWithMines.count { it == row })
    }

    // each row can only have as many mines as its columns
    // check that none of the rows have more mines than they can accommodate
    while (numberOfMinesPerRow.maxOrNull()!! > COLUMNS) {
        rowsWithMines.clear()
        numberOfMinesPerRow.clear()

        repeat(numberOfMines) {
            rowsWithMines.add(Random.nextInt(1, ROWS + 1))
        }

        for (row in 1..ROWS) {
            numberOfMinesPerRow.add(rowsWithMines.count { it == row })
        }
        println(numberOfMinesPerRow)
    }

    for (row in 1..ROWS) {
        if (row in rowsWithMines) {
            val predicate: (Int) -> Boolean = { it == row }
            val numberOfMinesInRow = rowsWithMines.count(predicate)

            if (numberOfMinesInRow == 1) {
                val rowTemplateCopy = rowTemplate.toMutableList()
                rowTemplateCopy[Random.nextInt(0, COLUMNS)] = "X"
                println(rowTemplateCopy.joinToString(""))
            } else {
                val rowTemplateCopy = rowTemplate.toMutableList()
                while (rowTemplateCopy.count { it == "X" } < numberOfMinesInRow) {
                    rowTemplateCopy[Random.nextInt(0, COLUMNS)] = "X"
                }

                println(rowTemplateCopy.joinToString(""))
            }
        } else {
            println(rowTemplate.joinToString(""))
        }
    }
}




package minesweeper
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9

fun main() {

    println("How many mines do you want on the field?")
    val numberOfMines = readln().toInt()
    val mineField1 = MineField(ROWS, COLUMNS, numberOfMines)
    mineField1.printMineField(mineField1.displayedField)
    val mineLocations = mineField1.addMines()
    mineField1.addHints()
    mineField1.printMineField(mineField1.field)
    mineField1.printMineField(mineField1.displayedField)
    println(mineLocations)
    //mineField1.takeGuesses(mineLocations)
}

class MineField (private val rows: Int, private val columns: Int, private val numberOfMines: Int) {
    val field = MutableList(rows) {MutableList(columns) {"."} }
    val displayedField = MutableList(rows) {MutableList(columns) {"."} }

    fun addMines (): MutableList<MutableList<Int>> {
        println("Set/unset mine marks or claim a cell as free:")
        val (y, x) = readln().split(" ").subList(0, 2).map {it.toInt() - 1}
        val mineLocations = mutableListOf<MutableList<Int>>()
        mineLocations.add(mutableListOf(x, y))
        field[x][y] = "X"
        var n: Int = 1 //starting with the first free mine location entered by the user

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

    fun takeGuesses (locations: MutableList<MutableList<Int>>) {
        val minesGuessedWrong = mutableListOf<MutableList<Int>>()
        minesGuessedWrong.addAll(locations)
        val mLocations = mutableListOf<MutableList<Int>>()
        mLocations.addAll(locations)

        while (mLocations.isNotEmpty() && minesGuessedWrong.isNotEmpty()) {
            println("Set/unset mine marks or claim a cell as free:")
            val (coordinateY, coordinateX, action) = readln().split(" ")
            val y = coordinateY.toInt() - 1
            val x = coordinateX.toInt() - 1

            when (action) {
                "mine" -> {
                    if (mutableListOf(x, y) in mLocations) {
                        mLocations.remove(mutableListOf(x, y))
                        minesGuessedWrong.remove(mutableListOf(x, y))
                        displayedField[x][y] = "*"
                        printMineField(displayedField)
                    } else {
                        minesGuessedWrong.add(mutableListOf(x, y))
                        displayedField[x][y] = "*"
                        printMineField(displayedField)
                    }
                }
                "free" -> {
                    if (mutableListOf(x, y) in locations) {
                        println("You stepped on a mine and failed!")
                        printMineField(displayedField)
                    } else {
                        minesGuessedWrong.remove(mutableListOf(x, y))
                    }
                }
                else -> {
                }
            }
        }

        println("Congratulations! You found all the mines!")
    }

    fun printMineField (sampleField: MutableList<MutableList<String>>): Unit {
        val printedField = sampleField.map { it. toMutableList() }.toMutableList()
        printedField.addAll(0, MutableList(1) {MutableList(sampleField[0].size) {"-"} })
        printedField.addAll(printedField.lastIndex + 1, MutableList(1) {MutableList(sampleField[0].size) {"-"} })
        printedField.addAll(0, MutableList(1) {MutableList(sampleField[0].size) {""} })
        for (i in 0 until sampleField[0].size) {
            printedField[0][i] = (i + 1).toString()
        }

        for (i in printedField.indices) {
            printedField[i].add(0, "|")
            printedField[i].add(printedField[i].lastIndex + 1, "|")
        }

        for (i in printedField.indices) {
            when (i) {
                0 -> printedField[i].add(0, " ")
                1 -> printedField[i].add(0, "-")
                printedField.lastIndex -> printedField[i].add(0, "-")
                else -> printedField[i].add(0, (i - 1).toString())
            }
        }
        printedField.forEach{ println(it.joinToString("")) }
    }


}
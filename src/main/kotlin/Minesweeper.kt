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
    println(mineLocations)
    mineField1.takeGuesses(mineLocations)
}

class MineField (private val rows: Int, private val columns: Int, private val numberOfMines: Int) {
    private val field = MutableList(rows) {MutableList(columns) {"."} }
    val displayedField = MutableList(rows) {MutableList(columns) {"."} }

    fun addMines (): MutableList<MutableList<Int>> {
        println("Set/unset mine marks or claim a cell as free:")
        val (freeY, freeX) = readln().split(" ").subList(0, 2).map {it.toInt() - 1}
        val mineLocations = mutableListOf<MutableList<Int>>()
        var n = 0

        while (n != numberOfMines) {
            val x: Int = Random.nextInt(0, rows)
            val y: Int = Random.nextInt(0, columns)

            if (field[x][y] != "X" && (x != freeX && y != freeY)) {
                field[x][y] = "X"
                mineLocations.add(mutableListOf(x, y))
                n++
            }

            for (i in maxOf(freeX - 1, 0) .. minOf(freeX + 1, ROWS - 1)) {
                for (j in maxOf(0, freeY - 1) .. minOf(freeY + 1, COLUMNS - 1)) {
                    if (field[i][j] == "X") {
                        field[i][j] = "."
                        n -= 1
                    }
                }
            }
        }
        addHints()
        floodFill(freeX,freeY)
        printMineField(displayedField)
        return mineLocations
    }

    private fun addHints () {
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
                        for (loc in locations) { displayedField[loc[0]][loc[1]] = "X" }
                        printMineField(displayedField)
                        break
                    } else {
                        floodFill(x, y)
                        printMineField(displayedField)
                    }
                }
                else -> {
                }
            }
        }

        println("Congratulations! You found all the mines!")
    }

    fun printMineField (sampleField: MutableList<MutableList<String>>) {
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

    private fun floodFill (x: Int, y: Int) {
        var count = 0
        val queue = mutableListOf(mutableListOf(x, y))
        do {
            for (loc in queue) {
                val a = loc[0]
                val b = loc[1]
                displayedField[a][b] = "/"
                count ++
                queue.remove(mutableListOf(a, b))
                for (i in maxOf(a - 1, 0) .. minOf(a + 1, ROWS - 1)) {
                    for (j in maxOf(0, b - 1) .. minOf(b + 1, COLUMNS - 1)) {
                        when (field[i][j]) {
                            "." -> queue.add(mutableListOf(i, j))
                            "1", "2", "3", "4", "5", "6", "7", "8" -> displayedField[i][j] = field[i][j]
                        }
                    }
                }
            }
        } while (queue.size != count)
    }


}
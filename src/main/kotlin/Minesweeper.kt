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

class MineField (rows: Int, private val columns: Int, private val numberOfMines: Int) {
    private val field = MutableList(rows) {MutableList(columns) {"."} }
    val displayedField = MutableList(rows) {MutableList(columns) {"."} }

    fun addMines (): MutableList<MutableList<Int>> {
        println("Set/unset mine marks or claim a cell as free:")
        val (firstY, firstX, action) = readln().split(" ")
        val fY = firstY.toInt() - 1
        val fX = firstX.toInt() - 1

        val mineLocations = mutableListOf<MutableList<Int>>()
        var n = 0

        val cells = mutableListOf<MutableList<Int>>()
        for (b in 0 until ROWS) {
            for (c in 0 until COLUMNS) {
                cells.add(mutableListOf(b,c))
            }
        }
        cells.remove(mutableListOf(fX, fY))

        when (action) {
            "free" -> {
                while (n != numberOfMines) {
                    val pos: Int = Random.nextInt(0, cells.lastIndex)
                    val x: Int = cells[pos][0]
                    val y: Int = cells[pos][1]

                    field[x][y] = "X"
                    mineLocations.add(mutableListOf(x, y))
                    cells.remove(mutableListOf(x, y))
                    n ++
                }
                addHints()
                dfs(fX,fY)
            }
            "mine" -> {
                displayedField[fX][fY] = "*"
                while (n != numberOfMines) {
                    val pos: Int = Random.nextInt(0, cells.lastIndex)
                    val x: Int = cells[pos][0]
                    val y: Int = cells[pos][1]

                    field[x][y] = "X"
                    mineLocations.add(mutableListOf(x, y))
                    cells.remove(mutableListOf(x, y))
                    n ++
                }
                addHints()
            }
        }
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
        val mLocations = mutableListOf<MutableList<Int>>() // list of actual mine locations adjusted according to guesses
        mLocations.addAll(locations)
        var correctMines = 0

        while (mLocations.isNotEmpty() && minesGuessedWrong.isNotEmpty() && correctMines != numberOfMines) {
            println("Set/unset mine marks or claim a cell as free:")
            val (coordinateY, coordinateX, action) = readln().split(" ")
            val y = coordinateY.toInt() - 1
            val x = coordinateX.toInt() - 1

            when (action) {
                "mine" -> {
                    if (mutableListOf(x, y) in mLocations && displayedField[x][y] != "*") {
                        mLocations.remove(mutableListOf(x, y))
                        minesGuessedWrong.remove(mutableListOf(x, y))
                        displayedField[x][y] = "*"
                        correctMines ++
                        printMineField(displayedField)
                    } else if (mutableListOf(x, y) in mLocations && displayedField[x][y] == "*") {
                        minesGuessedWrong.add(mutableListOf(x, y))
                        displayedField[x][y] = "."
                        printMineField(displayedField)
                    } else if (mutableListOf(x, y) !in mLocations && displayedField[x][y] != "*") {
                        minesGuessedWrong.add(mutableListOf(x, y))
                        displayedField[x][y] = "*"
                        printMineField(displayedField)
                    } else {
                        minesGuessedWrong.remove(mutableListOf(x, y))
                        displayedField[x][y] = "."
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
                        dfs(x, y)
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

    private fun dfs (x: Int, y: Int) {
        val list = listOf("1", "2", "3", "4", "5", "6", "7", "8")
        when {
            x < 0 || x >= ROWS || y < 0 || y >= COLUMNS -> return
            field[x][y] in list -> { displayedField[x][y] = field[x][y]; return }
            displayedField[x][y] == "/" -> return
            field[x][y] == "." -> displayedField[x][y] = "/"
        }
        dfs(x + 1, y)
        dfs(x - 1, y)
        dfs(x, y + 1)
        dfs(x, y - 1)
        dfs(x + 1, y + 1)
        dfs(x - 1, y - 1)
        dfs(x - 1, y + 1)
        dfs(x + 1, y - 1)
    }
}
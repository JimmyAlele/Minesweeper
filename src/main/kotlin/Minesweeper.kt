package minesweeper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9

fun main() {

    val numberOfMines = readln().toInt()
    val mineField1 = MineField(ROWS, COLUMNS, numberOfMines)
    val mineLocations = mineField1.addMines()
    println(mineLocations)
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
        beautifyField().forEach{
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
            val (y, x) = readln().split(" ").map { it.toInt()-1 }

            when (field[x][y]) {
                "." -> {
                    field[x][y] = "*"
                    if (mutableListOf(x, y) in mLocations) {
                        mLocations.remove(mutableListOf(x, y))
                        minesGuessedWrong.remove(mutableListOf(x, y))
                        printMineField()
                    } else {
                        minesGuessedWrong.add(mutableListOf(x, y))
                    }
                }
                "*" -> {
                    field[x][y] = "."
                    if (mutableListOf(x, y) in locations) {
                        mLocations.add(mutableListOf(x, y))
                        printMineField()
                    } else {
                        minesGuessedWrong.remove(mutableListOf(x, y))
                        //printMineField()
                    }
                }
                else -> {
                    println("There is a number here!")
                }
            }
        }
        println("Congratulations! You found all the mines!")
    }

    private fun beautifyField (): MutableList<MutableList<String>> {
        var printedField = mutableListOf<MutableList<String>>()
        printedField = field.clone()
        printedField.addAll(0, MutableList(1) {MutableList(columns) {"-"} })
        printedField.addAll(printedField.lastIndex + 1, MutableList(1) {MutableList(columns) {"-"} })
        printedField.addAll(0, MutableList(1) {MutableList(columns) {""} })
        for (i in 0 until columns) {
            printedField[0][i] = (i + 1).toString()
        }

        for (i in printedField.indices) {
            printedField[i].add(0, "|")
            printedField[i].add(printedField[i].lastIndex + 1, "|")
        }

        val stuff = listOf (" ", "-", 1, 2, 3, 4, 5, 6, 7, 8, 9, "-")
        for (i in printedField.indices) {
            printedField[i].add(0, stuff[i].toString())
        }
        return printedField
    }
}

fun <T> T.clone() : T
{
    val byteArrayOutputStream= ByteArrayOutputStream()
    ObjectOutputStream(byteArrayOutputStream).use { outputStream ->
        outputStream.writeObject(this)
    }

    val bytes=byteArrayOutputStream.toByteArray()

    ObjectInputStream(ByteArrayInputStream(bytes)).use { inputStream ->
        return inputStream.readObject() as T
    }
}
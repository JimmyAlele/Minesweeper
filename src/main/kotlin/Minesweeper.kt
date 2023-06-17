fun main() {
    val a: Int = readln().toInt()
    val b: Int = readln().toInt()
    var i = 0
    for (num in a .. b) {
        i += num
    }
    println(i)
}
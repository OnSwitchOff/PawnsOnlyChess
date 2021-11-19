fun main(args: Array<String>) {
    println("Pawns-Only Chess")
    println("First Player's name:")
    var fpName = readLine()!!
    fpName = if (fpName.isNullOrEmpty()) "Player1" else fpName
    println("Second Player's name:")
    var spName = readLine()!!
    spName = if (spName.isNullOrEmpty()) "Player2" else spName
    var isFpTurn: Boolean = true

    val field: PawnField = PawnField()
    field.setStartPosition()
    field.render()
    while (true) {
        var move: String? = ""
        var name: String = spName
        var side: Char = 'B'
        if (isFpTurn) {
            name = fpName
            side = 'W'
        }

        var enterMoveFlag = true;
        while(enterMoveFlag) {
            println("$name's turn:")
            move = readLine()!!

            if (move == "exit") {
                println("Bye!")
                return
            }

            if(move!!.matches(Regex("[a-hA-H][1-8][a-hA-H][1-8]"))) {
                val sCol = getColumnIndex(move[0])
                val sRow = move[1].toString().toInt()
                val fCol = getColumnIndex(move[2])
                val fRow = move[3].toString().toInt()

                if (!field.isExistPawn(sRow, sCol, side)) {
                    if (side == 'W') println("No white pawn at ${move.substring(0,2)}")
                    else println("No black pawn at ${move.substring(0,2)}")
                } else {
                    if(field.tryTo(sRow, sCol, fRow, fCol, side)) {
                        field.render()
                        enterMoveFlag = false
                    } else {
                        println("Invalid Input")
                    }
                }
            } else {
                println("Invalid Input")
            }
        }
        isFpTurn = !isFpTurn
    }
}
fun getColumnIndex(c: Char): Int = c.lowercaseChar().code - 96



class PawnField() {
    private val pawnList: MutableList<Pawn> = mutableListOf<Pawn>()
    var activePassant: Boolean = false

    private fun setPawn(p: Pawn): Unit {
        pawnList.add(p)
    }

    fun isExistPawn(row: Int, col: Int, side: Char? = null): Boolean {

        val test = pawnList.indexOfFirst { p -> p.side == side && p.row == row && p.col == col }
        return when (side) {
            'W', 'B' -> pawnList.indexOfFirst { p -> p.side == side && p.row == row && p.col == col } != -1
            else -> pawnList.indexOfFirst { p -> p.row == row && p.col == col } != -1
        }
    }

    fun tryTo(sRow: Int, sCol: Int, fRow: Int, fCol: Int, side: Char): Boolean {
        return when {
            side == 'W' && sCol == fCol && (fRow == sRow + 1) || (sRow == 2 && fRow == 4) -> tryToMove(
                sRow,
                sCol,
                fRow,
                fCol,
                side
            )
            (sCol == fCol - 1 || sCol == fCol + 1) && fRow == sRow + 1 -> tryToEatWhite(sRow, sCol, fRow, fCol)
            side == 'B' && sCol == fCol && (fRow == sRow - 1) || (sRow == 7 && fRow == 5) -> tryToMove(
                sRow,
                sCol,
                fRow,
                fCol,
                side
            )
            (sCol == fCol - 1 || sCol == fCol + 1) && fRow == sRow - 1 -> tryToEatBlack(sRow, sCol, fRow, fCol)
            else -> false
        }
    }


    private fun tryToMove(sRow: Int, sCol: Int, fRow: Int, fCol: Int, side: Char): Boolean {
        if ((sRow - fRow) * (sRow - fRow) == 1) {
            if (isExistPawn(sRow, sCol, side) && !isExistPawn(fRow, fCol)) {
                val selectedPawn = pawnList.first { p -> p.side == side && p.row == sRow && p.col == sCol }
                selectedPawn.row = fRow
                clearPassant()
                return true;
            }
        } else {
            val mRow = (sRow + fRow) / 2
            if (isExistPawn(sRow, sCol, side) && !isExistPawn(fRow, fCol) && !isExistPawn(mRow, sCol)) {
                val selectedPawn = pawnList.first { p -> p.side == side && p.row == sRow && p.col == sCol }
                selectedPawn.row = fRow
                clearPassant()
                val oSide: Char = if (side == 'W') 'B' else 'W'
                if (isExistPawn(fRow, fCol - 1, oSide)) {
                    val passantPawn = pawnList.first { p -> p.side == oSide && p.row == fRow && p.col == fCol - 1 }
                    passantPawn.rightPassant = true
                    activePassant = true;
                }
                if (isExistPawn(fRow, fCol + 1, oSide)) {
                    val passantPawn = pawnList.first { p -> p.side == oSide && p.row == fRow && p.col == fCol + 1 }
                    passantPawn.leftPassant = true
                    activePassant = true;
                }
                return true;
            }
        }
        return false
    }

    private fun tryToEatWhite(sRow: Int, sCol: Int, fRow: Int, fCol: Int): Boolean {
        val movingWhitePawn = pawnList.first { p -> p.side == 'W' && p.row == sRow && p.col == sCol }
        if (isExistPawn(fRow, fCol, 'B')) {
            val removingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == fRow && p.col == fCol }
            pawnList.remove(removingBlackPawn)
            movingWhitePawn.row = fRow
            movingWhitePawn.col = fCol
            clearPassant()
            return true;
        }

        if (isExistPawn(sRow, sCol - 1, 'B') && movingWhitePawn.leftPassant) {
            val removingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == sRow && p.col == sCol - 1 }
            pawnList.remove(removingBlackPawn)
            movingWhitePawn.row = fRow
            movingWhitePawn.col = fCol
            clearPassant()
            return true;
        }

        if (isExistPawn(sRow, sCol + 1, 'B') && movingWhitePawn.rightPassant) {
            val removingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == sRow && p.col == sCol + 1 }
            pawnList.remove(removingBlackPawn)
            movingWhitePawn.row = fRow
            movingWhitePawn.col = fCol
            clearPassant()
            return true;
        }
        return false
    }

    private fun tryToEatBlack(sRow: Int, sCol: Int, fRow: Int, fCol: Int): Boolean {
        val movingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == sRow && p.col == sCol }
        if (isExistPawn(sRow, sCol, 'B') && isExistPawn(fRow, fCol, 'W')) {
            val removingWhitePawn = pawnList.first { p -> p.side == 'W' && p.row == fRow && p.col == fCol }
            pawnList.remove(removingWhitePawn)
            movingBlackPawn.row = fRow
            movingBlackPawn.col = fCol
            clearPassant()
            return true;
        }
        if (isExistPawn(sRow, sCol - 1, 'W') && movingBlackPawn.leftPassant) {
            val removingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == sRow && p.col == sCol - 1 }
            pawnList.remove(removingBlackPawn)
            movingBlackPawn.row = fRow
            movingBlackPawn.col = fCol
            clearPassant()
            return true;
        }
        if (isExistPawn(sRow, sCol + 1, 'W') && movingBlackPawn.rightPassant) {
            val removingBlackPawn = pawnList.first { p -> p.side == 'B' && p.row == sRow && p.col == sCol + 1 }
            pawnList.remove(removingBlackPawn)
            movingBlackPawn.row = fRow
            movingBlackPawn.col = fCol
            clearPassant()
            return true;
        }
        return false
    }

    public fun render() {
        println("  +---+---+---+---+---+---+---+---+")
        for (i in 8 downTo 1) {
            print("$i |")
            for (j in 1..8) {
                when {
                    isExistPawn(i, j, 'W') -> print(" W |")
                    isExistPawn(i, j, 'B') -> print(" B |")
                    else -> print("   |")
                }
            }
            println()
            println("  +---+---+---+---+---+---+---+---+")
        }
        println("    a   b   c   d   e   f   g   h")
        println()
    }

    public fun setStartPosition() {
        for (i in 1..8) {
            setPawn(Pawn(2, i, 'W'))
            setPawn(Pawn(7, i, 'B'))
        }
    }

    private fun clearPassant() {
        pawnList.forEach { p -> p.leftPassant = false; p.rightPassant = false }
    }

    public fun isWhiteWins(): Boolean {
        return pawnList.any { p -> p.side == 'W' && p.row == 8 } || pawnList.all { p -> p.side == 'W'}
    }

    public fun isBlackWins(): Boolean {
        return pawnList.any { p -> p.side == 'B' && p.row == 1 }  || pawnList.all { p -> p.side == 'B'}
    }

    public fun isStalemate(side: Char): Boolean {
        return if (side == 'W') {
            pawnList.filter { pn -> pn.side == side }.all { p ->
                isExistPawn(p.row + 1, p.col)
                        && !isExistPawn(p.row + 1, p.col + 1, 'B')
                        && !isExistPawn(p.row + 1, p.col - 1, 'B')
                        && !p.leftPassant && !p.rightPassant
            }
        } else {
            pawnList.filter { pn -> pn.side == side }.all { p ->
                isExistPawn(p.row - 1, p.col)
                        && !isExistPawn(p.row - 1, p.col + 1, 'W')
                        && !isExistPawn(p.row - 1, p.col - 1, 'W')
                        && !p.leftPassant && !p.rightPassant
            }
        }
    }
}
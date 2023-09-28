package com.example.visionchess

@Suppress("USELESS_CAST")
class ChessGame {

    private var chessBoard = Array(8) { Array(8) { null as Piece? } }
    private val letterToNumberMapDeveloperVersion = mapOf(
        "A" to 0,
        "B" to 1,
        "C" to 2,
        "D" to 3,
        "E" to 4,
        "F" to 5,
        "G" to 6,
        "H" to 7
    )
    private val numberToLetterMapDeveloperVersion = mapOf(
        0 to "A",
        1 to "B",
        2 to "C",
        3 to "D",
        4 to "E",
        5 to "F",
        6 to "G",
        7 to "H"
    )
    private val letterToNumberMapPlayerVersion = mapOf(
        "A" to 1,
        "B" to 2,
        "C" to 3,
        "D" to 4,
        "E" to 5,
        "F" to 6,
        "G" to 7,
        "H" to 8
    )
    private val numberToLetterMapPlayerVersion = mapOf(
        1 to "A",
        2 to "B",
        3 to "C",
        4 to "D",
        5 to "E",
        6 to "F",
        7 to "G",
        8 to "H"
    )
    init {
        start()
    }

    private fun createChessGame(playerWithWhitePieces: Player, playerWithBlackPieces: Player,
                                time: String, type: String) {

        for (col in 0..7){
            var columnLetter = numberToLetterMapDeveloperVersion[col]
            chessBoard[1][col] = Pawn("P", "white", columnLetter + "2", true, isMoved = false)
            chessBoard[6][col] = Pawn("P", "black", columnLetter + "7", true, isMoved = false)
        }
        chessBoard[0][0] = Rook("R", "white", "A1", true, isMoved = false)
        chessBoard[0][7] = Rook("R", "white", "H1", true, isMoved = false)
        chessBoard[7][0] = Rook("R", "black", "A8", true, isMoved = false)
        chessBoard[7][7] = Rook("R", "black", "H8", true, isMoved = false)

        chessBoard[0][1] = Knight("N", "white", "B1", true, isMoved = false)
        chessBoard[0][6] = Knight("N", "white", "G1", true, isMoved = false)
        chessBoard[7][1] = Knight("N", "black", "B8", true, isMoved = false)
        chessBoard[7][6] = Knight("N", "black", "G8", true, isMoved = false)

        chessBoard[0][2] = Bishop("B", "white", "C1", true, isMoved = false)
        chessBoard[0][5] = Bishop("B", "white", "F1", true, isMoved = false)
        chessBoard[7][2] = Bishop("B", "black", "C8", true, isMoved = false)
        chessBoard[7][5] = Bishop("B", "black", "F8", true, isMoved = false)

        chessBoard[0][3] = Queen("Q", "white", "D1", true, isMoved = false)
        chessBoard[7][3] = Queen("Q", "black", "D8", true, isMoved = false)

        chessBoard[0][4] = King("K", "white", "E1", true, isMoved = false)
        chessBoard[7][4] = King("K", "black", "E8", true, isMoved = false)
    }


    private fun start(){
        val playerWithWhitePieces: Player
        val playerWithBlackPieces: Player

        //createChessGame(chessBoard, playerWithWhitePieces, playerWithBlackPieces)
    }
    fun movePiece(fromRow:Int,fromCol:Int, toRow:Int, toCol:Int): Boolean {
        val piece = chessBoard[fromRow][fromCol]
        if (piece != null) {
            if (piece.moveIsValid(fromRow, fromCol, toRow, toCol)) {
                if(chessBoard[fromRow][fromCol]!!.color != chessBoard[toRow][toCol]!!.color){
                    chessBoard[toRow][toCol]!!.isAlive = false
                }
                if(chessBoard[fromRow][fromCol]!!.color == chessBoard[toRow][toCol]!!.color){
                    return false
                }
                chessBoard[toRow][toCol] = piece
                chessBoard[fromRow][fromCol] = null
                return true
            }
        }
        return false
    }
    fun getChessBoard(): Array<Array<Piece?>> {
        return chessBoard
    }
    fun setChessBoard(cb : Array<Array<Piece?>>) {
        chessBoard = cb
    }
    fun endGameBecauseTimeout(whoWon: String): String {
        return "white"
    }
    fun isGameFinished(): Boolean {
        return false
    }
    fun isWhiteTurn(): Boolean {
        return false
    }


}
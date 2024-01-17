package com.example.visionchess

import com.example.visionchess.Pieces.Bishop
import com.example.visionchess.Pieces.King
import com.example.visionchess.Pieces.Knight
import com.example.visionchess.Pieces.Pawn
import com.example.visionchess.Pieces.Piece
import com.example.visionchess.Pieces.Queen
import com.example.visionchess.Pieces.Rook


class ChessGame {
    var chessBoard2 = HashMap<String, Piece?>()
    var isTimeUp = false
    var isGameFinished = false
    var isWhiteTurn = true

     val letterToNumberMapPlayerVersion = mapOf(
        "A" to 1,
        "B" to 2,
        "C" to 3,
        "D" to 4,
        "E" to 5,
        "F" to 6,
        "G" to 7,
        "H" to 8
    )
     val numberToLetterMapPlayerVersion = mapOf(
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
    private fun createChessGame() {

        for (row in 1..8) {
            for (col in 'A'..'H') {
                val position = "$col$row"
                if(row == 2){
                    chessBoard2[position] = Pawn("Pawn", "white", position, true, isMoved = false)
                }
                if(row == 7) {
                    chessBoard2[position] = Pawn("Pawn", "black", position, true, isMoved = false)
                }
                if(row == 1 && (col == 'A' || col == 'H')){
                    chessBoard2[position] = Rook("Rook", "white", position, true, isMoved = false)
                }
                if(row == 8 && (col == 'A' || col == 'H')){
                    chessBoard2[position] = Rook("Rook", "black", position, true, isMoved = false)
                }
                if(row == 1 && (col == 'B' || col == 'G')){
                    chessBoard2[position] = Knight("Knight", "white", position, true, isMoved = false)
                }
                if(row == 8 && (col == 'B' || col == 'G')){
                    chessBoard2[position] = Knight("Knight", "black", position, true, isMoved = false)
                }
                if(row == 1 && (col == 'C' || col == 'F')){
                    chessBoard2[position] = Bishop("Bishop", "white", position, true, isMoved = false)
                }
                if(row == 8 && (col == 'C' || col == 'F')){
                    chessBoard2[position] = Bishop("Bishop", "black", position, true, isMoved = false)
                }
                if(row == 1 && col == 'D'){
                    chessBoard2[position] = Queen("Queen", "white", position, true, isMoved = false)
                }
                if(row == 8 && col == 'D'){
                    chessBoard2[position] = Queen("Queen", "black", position, true, isMoved = false)
                }
                if(row == 1 && col == 'E'){
                    chessBoard2[position] = King("King", "white", position, true, isMoved = false)
                }
                if(row == 8 && col == 'E'){
                    chessBoard2[position] = King("King", "black", position, true, isMoved = false)
                }
                if(row in 3..6){
                    chessBoard2[position] = null
                }
            }
        }
    }
    private fun start(){
        createChessGame()
    }
    fun movePiece(message:String): Boolean {
        val fromRow = message[1].toString().toInt()
        val fromCol = message[0].toString()
        val toRow = message[3].toString().toInt()
        val toCol = message[2].toString()
        var isMovePossible = true
        if(isWhiteTurn){
            if(chessBoard2["$fromCol$fromRow"]?.color != "white"){
                isMovePossible = false
            }
        }
        else{
            if(chessBoard2["$fromCol$fromRow"]?.color != "black"){
                isMovePossible = false
            }
        }
        if(isMovePossible){
            val fromColInt = letterToNumberMapPlayerVersion[fromCol]
            val toColInt = letterToNumberMapPlayerVersion[toCol]
            val fromPosition = "$fromCol$fromRow"
            val toPosition = "$toCol$toRow"
            val piece = chessBoard2[fromPosition]
            if (piece != null) {
                if (piece.moveIsValid(fromRow,fromColInt!!,toRow,toColInt!!)) {
                    if(chessBoard2[fromPosition]!!.color != chessBoard2[toPosition]?.color){
                        chessBoard2[toPosition]?.isAlive = false
                    }
                    if(chessBoard2[fromPosition]!!.color == chessBoard2[toPosition]?.color){
                        return false
                    }
                    chessBoard2[toPosition] = piece
                    chessBoard2[fromPosition] = null
                    return true
                }
            }
        }


        return false
    }
    fun getPieceAtPosition(position: String): Piece? {
        return chessBoard2[position]
    }
    fun setChessBoard(chessBoard: HashMap<String, Piece?>){
        chessBoard2 = chessBoard
    }
    fun returnBoardAsString(): String {
        var board = ""
        for (row in 8 downTo 1) {
            if(row==8){
                board += "  ABCDEFGH\n"
            }
            board += "$row"
            for (col in 'A'..'H') {
                val position = "$col$row"
                if (chessBoard2[position] != null) {
                    if(chessBoard2[position]!!.name=="Knight"){
                        board += "N"
                    }
                    else{
                        board += chessBoard2[position]!!.name[0]
                    }
                } else {
                    board += "|  "
                }
            }
            board += "\n"
        }
        return board
    }
}
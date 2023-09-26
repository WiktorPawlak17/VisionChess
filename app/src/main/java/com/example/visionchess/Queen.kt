package com.example.visionchess

import kotlin.math.abs

class Queen(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {

    override fun moveIsValid(fromRow:Int,fromCol:Int, toRow:Int, toCol:Int): Boolean {
        val rowDiff = abs(toRow - fromRow)
        val colDiff = abs(toCol - fromCol)
        if (rowDiff == colDiff) {
            return true
        } else if (fromRow==toRow || fromCol==toCol){
            return true
        }
        return false
    }
}
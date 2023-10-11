package com.example.visionchess.Pieces

import kotlin.math.abs

class Knight(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {

    override fun moveIsValid(fromRow:Int,fromCol:Int, toRow:Int, toCol:Int): Boolean {
        val rowDiff = abs(fromRow - toRow)
        val colDiff = abs(fromCol - toCol)
        if (rowDiff == 2 && colDiff == 1) {
            return true
        } else if (rowDiff == 1 && colDiff == 2) {
            return true
        }
        return false
    }
}
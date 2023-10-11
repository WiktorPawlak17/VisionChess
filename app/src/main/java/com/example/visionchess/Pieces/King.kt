package com.example.visionchess.Pieces

import kotlin.math.abs

class King(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {

    override fun moveIsValid(fromRow:Int,fromCol:Int, toRow:Int, toCol:Int): Boolean {
        val rowDiff = abs(toRow - fromRow)
        val colDiff = abs(toCol - fromCol)
        if (rowDiff == 1 && colDiff == 0) {
            return true
        } else if (rowDiff == 0 && colDiff == 1) {
            return true
        } else if (rowDiff == 1 && colDiff == 1) {
            return true
        }
        return false
    }
}
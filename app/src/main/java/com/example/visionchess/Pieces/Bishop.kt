package com.example.visionchess.Pieces

import kotlin.math.abs

class Bishop(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {

    override fun moveIsValid(fromRow:Int, fromCol: Int, toRow:Int, toCol: Int): Boolean {
        val rowDiff = abs(toRow - fromRow)
        val colDiff = abs(toCol - fromCol)
        if (rowDiff == colDiff) {
            return true
        }
        return false
    }
    override fun setMoved(){
        super.setMoved()
    }
}
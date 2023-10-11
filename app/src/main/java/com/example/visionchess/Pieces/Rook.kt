package com.example.visionchess.Pieces

class Rook(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {


    override fun moveIsValid(fromRow:Int,fromCol:Int, toRow:Int, toCol:Int): Boolean {
        if(fromRow==toRow || fromCol==toCol){
            return true
        }
        return false
    }
}
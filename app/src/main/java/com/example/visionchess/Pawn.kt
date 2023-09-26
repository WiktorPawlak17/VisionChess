package com.example.visionchess

class Pawn(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "P"
    override fun move() {
        super.move()
    }

}
package com.example.visionchess

class Queen(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "Q"
    override fun move() {
        super.move()
    }
}
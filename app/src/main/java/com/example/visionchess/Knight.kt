package com.example.visionchess

class Knight(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "N"
    override fun move() {
        super.move()
    }
}
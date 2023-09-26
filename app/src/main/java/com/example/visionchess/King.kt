package com.example.visionchess

class King(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "K"
    override fun move() {
        super.move()
    }
}
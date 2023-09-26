package com.example.visionchess

class Bishop(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "B"


    override fun move() {
        super.move()
    }
}
package com.example.visionchess

class Rook(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {
    override val symbol = "R"


    override fun move() {
        super.move()
    }
}
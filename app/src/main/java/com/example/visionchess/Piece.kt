package com.example.visionchess

abstract class Piece(val name:String, val color:String, val position: String, val isAlive: Boolean, val isMoved: Boolean){
    abstract val symbol:String
    public open fun move(){

    }
}
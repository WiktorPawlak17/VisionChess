package com.example.visionchess

abstract class Piece(val name:String, val color:String, var position: String, var isAlive: Boolean, var isMoved: Boolean){
    open fun moveIsValid(fromRow:Int, fromCol:Int, toRow:Int, toCol:Int):Boolean{
        return false
    }
    open fun setMoved(){
        isMoved=true
    }
    fun setDead(){
        isAlive=false
    }


}
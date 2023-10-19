package com.example.visionchess.Pieces

abstract class Piece(val name:String, val color:String, var position: String, var isAlive: Boolean, var isMoved: Boolean){

    val letterToNumberMapPlayerVersion = mapOf(
        "A" to 1,
        "B" to 2,
        "C" to 3,
        "D" to 4,
        "E" to 5,
        "F" to 6,
        "G" to 7,
        "H" to 8
    )
    val numberToLetterMapPlayerVersion = mapOf(
        1 to "A",
        2 to "B",
        3 to "C",
        4 to "D",
        5 to "E",
        6 to "F",
        7 to "G",
        8 to "H"
    )

    open fun moveIsValid(fromRow:Int, fromCol: Int, toRow:Int, toCol: Int):Boolean{
        return false
    }
    open fun pieceSees():MutableList<String>{
        return mutableListOf()
    }

    open fun setMoved(){
        isMoved=true
    }
    fun setDead(){
        isAlive=false
    }


}
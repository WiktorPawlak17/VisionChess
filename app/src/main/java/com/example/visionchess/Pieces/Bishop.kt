package com.example.visionchess.Pieces

import android.widget.Toast
import kotlin.math.abs

class Bishop(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {

    override fun moveIsValid(fromRow:Int, fromCol: Int, toRow:Int, toCol: Int): Boolean {
        val rowDiff = abs(toRow - fromRow)
        val colDiff = abs(toCol - fromCol)
        if (rowDiff == colDiff) {
            return true
        }
        return false
    }

    //DONT KNOW IF IM GONNA NEED IT OR NOT ???
    override fun pieceSees(): MutableList<String> {
        val row = position[1].toString().toInt()
        val colString = position[0].toString()
        val col = letterToNumberMapPlayerVersion[colString]
        val sees = mutableListOf<String>()
        val allRows = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        val allCols = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        for(i in allRows){
            for(j in allCols){
                if(abs(i-row)==abs(j-col!!)){
                    sees.add("${numberToLetterMapPlayerVersion[j]}$i")
                }
            }
        }



        sees.remove(position)

        return sees
    }
}
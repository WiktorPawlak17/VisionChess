package com.example.visionchess.Pieces

import android.widget.Toast
import kotlin.math.abs
class Pawn(name: String, color: String, position: String, isAlive: Boolean, isMoved: Boolean) :
    Piece(name, color, position, isAlive, isMoved) {



    override fun moveIsValid(fromRow:Int, fromCol: Int, toRow:Int, toCol: Int): Boolean {
        if(color=="white"){
            if(!isMoved){
                if(toRow-fromRow == 2 && fromCol==toCol){
                 return true
                }
            }
            else{
                if(toRow-fromRow == 1 && fromCol==toCol){
                    return true
                }
            }
        }
        else if(color=="black"){
            if(!isMoved){
                if(fromRow-toRow == 2 && fromCol==toCol){
                    return true
                }
            }
            else{
                if(fromRow-toRow == 1 && fromCol==toCol){
                    return true
                }
            }
        }
        return false
    }

    override fun pieceSees(): MutableList<String> {
        val row = position[1].toString().toInt()
        val colString = position[0].toString()
        val col = letterToNumberMapPlayerVersion[colString]
        val sees = mutableListOf<String>()
        val allRows = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        val allCols = listOf(1, 2, 3, 4, 5, 6, 7, 8)

        for(i in allRows) {
            for (j in allCols) {
                if (color == "white") {
                    if ((i - row == 1) && abs(j - col!!) == 1) {
                        sees.add("${numberToLetterMapPlayerVersion[j]}$i")
                    }
                } else {
                        if ((row - i == 1) && abs(j - col!!) == 1) {
                            sees.add("${numberToLetterMapPlayerVersion[j]}$i")
                        }

                    }

            }


        }

        sees.remove(position)

        return sees
    }

}
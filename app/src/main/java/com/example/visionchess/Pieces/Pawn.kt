package com.example.visionchess.Pieces

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

}
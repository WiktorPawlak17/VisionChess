package com.example.visionchess

class Person(val nickname: String, val name:String,val surname:String,val ratings: MutableList<Int>,
             val games: MutableList<ChessGame>,val wins: Int, val losses: Int, val draws: Int, val lastGame: ChessGame,
             val lastGameTime:String, val lastGameType:String, val gamesRate:Float){


}
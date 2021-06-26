package com.raj.mycarride.ui.learning

class LambdaExpressions {

    fun demoLambda() {

        var tripple : (Int) -> Int = {number -> number *3}

        println ("tripple is ${tripple(3)}")
    }

    fun passmeAfunction(funcName : (Int) -> Int = {it -> it * it}) {
        println ( "square is " +funcName(4))
    }



}

fun main () {
    var lambdaExpressions : LambdaExpressions = LambdaExpressions()
//    lambdaExpressions.passmeAfunction{ it -> it * it}
    lambdaExpressions.passmeAfunction()
}
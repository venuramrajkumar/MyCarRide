package com.raj.mycarride.ui.utils

public class  TempDelete  constructor() : ConcreteInterface{

    override val number: Int
        get() {
            return 5
        }
    public val name : String  = "RAM"
    override fun getName(name: String) {
        super.getName(name)
    }

}

fun main() {
    var tempDelete = TempDelete()
    tempDelete.getName("RAM")

    System.out.println("number is ${tempDelete.number}")

    rajkumar@ for (i in 1..3) {
        System.out.println ("loop is $i")
        for (j in 1..2) {
            break@rajkumar
        }
    }


}

interface ConcreteInterface {
    val number : Int
    fun getName( name : String ) {
        System.out.println("DEFAULT NAME is ")
    }
 }
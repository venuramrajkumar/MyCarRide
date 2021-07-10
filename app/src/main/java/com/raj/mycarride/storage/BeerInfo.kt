package com.raj.mycarride.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Beers")
data class BeerInfo(
                     @ColumnInfo(name = "alchoholContent")
                     var abv : String,
                     var  ibu : String,
                     @PrimaryKey
                     @ColumnInfo (name = "beerId")
                     var id : Int,
                     @ColumnInfo (name = "beerName")
                     var name : String,
                     @ColumnInfo (name = "beerStyle")
                     var style : String,
                     var ounces :  Float){

}
package com.raj.mycarride.storage

import androidx.room.*
import com.raj.mycarride.storage.BeerInfo
import io.reactivex.Single

@Dao
interface BeerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBeer(beer: BeerInfo)

    @Query("SELECT * FROM Beers")
    fun getAllBeers() : Single<List<BeerInfo>>



//    @Update
//    fun updateBeers(beer : BeerInfo) : Single<Int>
//
//    @Delete
//    fun deleteBeer(beer:BeerInfo) : Single<Int>
//
//    @Delete
//    fun deleteAll(beerList: List<BeerInfo>) : Single<List<Int>>
//
//    @Query ("SELECT * FROM Beers where beerId = :id")
//    fun  getBeerById(id : Int) : Single<LiveData<BeerInfo>>


}
package com.raj.mycarride.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raj.mycarride.utilities.SingletonHolder


@Database(entities = [BeerInfo::class],version = 1, exportSchema = false)
abstract class BeerDataBase : RoomDatabase() {

    abstract fun getBeerDao() : BeerDao

    companion object : SingletonHolder<BeerDataBase, Context>({
         Room.databaseBuilder<BeerDataBase>(it.applicationContext, BeerDataBase::class.java,"beer_db").build()
    })
}
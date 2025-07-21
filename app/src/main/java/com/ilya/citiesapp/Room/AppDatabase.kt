package com.ilya.citiesapp.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ilya.citiesapp.Room.DAO.CityDao
import com.ilya.citiesapp.Room.Entity.CityEntity
import com.ilya.citiesapp.Room.Entity.CityListEntity

@Database(
    entities = [CityEntity::class, CityListEntity::class, CityListCrossRef::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cities.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

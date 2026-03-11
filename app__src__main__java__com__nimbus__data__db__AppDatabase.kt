package com.nimbus.data.db

import android.content.Context
import androidx.room.*
import com.nimbus.data.model.SessionEntity

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "nimbus_db")
                    .build().also { INSTANCE = it }
            }
    }
}
